package com.intuit.developer.tutorials.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.ipp.data.*;
import com.intuit.ipp.services.DataService;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.*;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.itextpdf.io.font.constants.StandardFonts.TIMES_BOLD;
import static com.itextpdf.io.font.constants.StandardFonts.TIMES_ROMAN;
import static com.itextpdf.layout.property.TabAlignment.CENTER;
import static com.itextpdf.layout.property.TabAlignment.LEFT;

public class PDFconverter {
    public static String folder = "CWAC Reports";
    boolean dirCheck = false; //ignore

    public void generatePDF(DataService service, CompanyInfo companyInfo, String graphName, boolean custCheck, boolean gridLineCheck, boolean transCheck, boolean inCheck, boolean disGraphCheck) throws Exception {
        String reportTitle = graphName;
        boolean gridCheck = gridLineCheck; //grid lines
        boolean customerCheck = custCheck; //customer info
        boolean transactionsCheck = transCheck;
        boolean itemsCheck = inCheck;
        boolean graphCheck = disGraphCheck;

        String  home = System.getProperty("user.home");
        final String directory = home + File.separator + "Downloads" + File.separator + folder;
        final String DEST = directory + File.separator +  reportTitle + ".pdf";
        final String graphDest = directory + File.separator +  reportTitle + ".png";

        File dir = new File(directory);
        File file = new File(DEST);

        if(!(dir.exists())) {
            dirCheck = dir.mkdir();
        }
        if(dir.exists()){
            if (file.createNewFile()) {
                PdfWriter writer = new PdfWriter(DEST);
                PdfDocument pdf = new PdfDocument(writer);
                Document doc = new Document(pdf);

                recordsHelper recHelper = new recordsHelper();

                AreaBreak aB = new AreaBreak();

                PdfFont title = PdfFontFactory.createFont(TIMES_BOLD);
                PdfFont regular = PdfFontFactory.createFont(TIMES_ROMAN);
                PdfFont bold = PdfFontFactory.createFont(TIMES_BOLD);

                String compName = getCompName(companyInfo);
                String compAdd = getCompAdd(companyInfo);
                String compEmail = getCompEmail(companyInfo);
                String compPhone = getCompNum(companyInfo);

                Paragraph cover = new Paragraph(reportTitle).setFont(title).setFontSize(28);
                Paragraph nameCover = new Paragraph(compName);
                Paragraph addCover = new Paragraph(compAdd);
                Paragraph emailCover = new Paragraph(compEmail);
                Paragraph phoneCover = new Paragraph(compPhone);

                PageSize pageSize = pdf.getDefaultPageSize();
                float width = pageSize.getWidth() - doc.getLeftMargin() - doc.getRightMargin();

                centerText(doc, width, cover);
                centerText(doc, width, nameCover);
                centerText(doc, width, addCover);
                centerText(doc, width, emailCover);
                centerText(doc, width, phoneCover);

                if(customerCheck) {
                    doc.add(aB);

                    Paragraph custSec = new Paragraph("CUSTOMERS' INFORMATION: ").setFont(bold);
                    doc.add(custSec);
                    getCustomerInfo(doc, (recHelper.getCustomers(service)), gridCheck);
                }

                if(transactionsCheck) {
                    doc.add(aB);

                    Paragraph receiptsSec = new Paragraph("RECEIPTS: ").setFont(bold);
                    doc.add(receiptsSec);
                    getReceiptInfo(doc, recHelper.getSalesReceipts(service), gridCheck);
                }

                if(transactionsCheck) {
                    doc.add(aB);

                    Paragraph invoiceSec = new Paragraph("INVOICES: ").setFont(bold);
                    doc.add((IBlockElement) invoiceSec);
                    getInvoiceInfo(doc, recHelper.getInvoices(service), gridCheck);
                }

                if(transactionsCheck) {
                    doc.add(aB);

                    Paragraph estimateSec = new Paragraph("ESTIMATES: ").setFont(bold);
                    doc.add(estimateSec);
                    getEstimatesInfo(doc, recHelper.getEstimates(service), gridCheck);
                }

                if(transactionsCheck) {
                    doc.add(aB);
                    Paragraph paymentsSec = new Paragraph("PAYMENTS: ").setFont(bold);
                    doc.add(paymentsSec);
                    getPaymentsInfo(doc, recHelper.getPayments(service), gridCheck);
                }

                if(itemsCheck) {
                    doc.add(aB);

                    Paragraph itemsSec = new Paragraph("ITEMS: ").setFont(bold);
                    doc.add(itemsSec);
                    getItemsInfo(doc, recHelper.getItems(service), gridCheck);
                }

                if(graphCheck) {
                    doc.add(aB);
                    Paragraph predictionsSec = new Paragraph("GRAPH PREDICTIONS: ").setFont(bold);
                    doc.add(predictionsSec);
                    Image graph = new Image(ImageDataFactory.create(graphDest));
                    doc.add(graph);
                }

                doc.close();
            }
        }
        openFile(file);
    }

    public static String getCompName(CompanyInfo userInfo) {
        String data = new String();
        data = createResponse("Company Name: " + userInfo.getCompanyName());
        data = data.replaceAll("\"", "");
        return data;
    }

    public static String getCompAdd(CompanyInfo userInfo) {
        String data = new String();
        data = createResponse("Address: ");
        data += createResponse(userInfo.getCompanyAddr().getLine1()) + " ";
        data += createResponse(userInfo.getCompanyAddr().getCity()) + " ";
        data += createResponse(userInfo.getCompanyAddr().getCountry()) + " ";
        data += createResponse(userInfo.getCompanyAddr().getPostalCode());
        data = data.replaceAll("\"", "");
        return data;
    }

    public static String getCompEmail(CompanyInfo userInfo) {
        String data = new String();
        data = createResponse("Email: " + userInfo.getEmail().getAddress());
        data = data.replaceAll("\"", "");
        return data;
    }

    public static String getCompNum(CompanyInfo userInfo) {
        String data = new String();
        data = createResponse("Phone Number: " + userInfo.getPrimaryPhone().getFreeFormNumber());
        data = data.replaceAll("\"", "");
        return data;
    }

    private static void centerText(Document document, float width, Paragraph text) {
        List<TabStop> tabStops = new ArrayList<>();

        // Create a TabStop at the middle of the page
        tabStops.add(new TabStop(width / 2, CENTER));

        // Create a TabStop at the end of the page
        tabStops.add(new TabStop(width, LEFT));

        Paragraph p = new Paragraph().addTabStops(tabStops);
        p
                .add(new Tab())
                .add(text)
                .add(new Tab());
        document.add(p);
    }

    public static void getCustomerInfo(Document document, List<Customer> customers, boolean tableChoice) {
        String customerInfo = new String();
        Paragraph custInfo = new Paragraph();

        if (tableChoice == false) {
            for (Customer c : customers) {
                customerInfo += "   " + createResponse(c.getDisplayName()) + "   ";
                customerInfo += "   " + createResponse(c.getDisplayName()) + "  ";
                customerInfo += "   " + createResponse(c.getBalance()) + "\n";
            }

            customerInfo = customerInfo.replaceAll("\"", "");
            custInfo.add(customerInfo);
            document.add(custInfo);
        } else {
            String customerInformation[];
            List<List<String>> data = new ArrayList<>();
            String[] titles = {" Name", " Account Number", " Balance"};
            Table table = new Table(titles.length);
            customerInformation = new String[titles.length];

            data.add(Arrays.asList(titles));

            for (Customer c : customers) {
                List<String> dataList = new ArrayList<>();
                customerInformation[0] = createResponse(c.getDisplayName()).replaceAll("\"", "");
                customerInformation[1] = createResponse(c.getDisplayName()).replaceAll("\"", "");
                customerInformation[2] = createResponse(c.getBalance()).replaceAll("\"", "");
                for (int i = 0; i < titles.length; i++) {
                    dataList.add(customerInformation[i]);
                }
                data.add(dataList);
            }
            for (List<String> d : data) {
                for (String c : d) {
                    table.addCell(c);
                }
            }
            document.add(table);
        }
    }

    public static void getReceiptInfo(Document document, List<SalesReceipt> receipts, boolean tableChoice) {
        String receiptInfo = new String();
        Paragraph recInfo = new Paragraph();

        List<String> itemName = receipts.stream().map(receipt -> receipt.getLine().get(0).getSalesItemLineDetail().getItemRef().getName()).collect(Collectors.toList());
        List<String> description = receipts.stream().map(receipt -> receipt.getLine().get(0).getDescription()).collect(Collectors.toList());
        List<String> docNumber = receipts.stream().map(receipt -> receipt.getDocNumber()).collect(Collectors.toList());
        List<Date> txnDate = receipts.stream().map(receipt -> receipt.getTxnDate()).collect(Collectors.toList());
        List<BigDecimal> quantity = receipts.stream().map(receipt -> receipt.getLine().get(0).getSalesItemLineDetail().getQty()).collect(Collectors.toList());
        List<BigDecimal> unitPrice = receipts.stream().map(receipt -> receipt.getLine().get(0).getSalesItemLineDetail().getUnitPrice()).collect(Collectors.toList());

        if (tableChoice == false) {
            for (int i = 0; i < receipts.size(); i++) {
                receiptInfo += createResponse(itemName.get(i)) + "\t";
                receiptInfo += createResponse(description.get(i)) + "\t";
                receiptInfo += createResponse(docNumber.get(i)) + "\t";
                receiptInfo += convertToLocalDate(txnDate.get(i)) + "\t";
                receiptInfo += quantity.get(i) + "\t";
                receiptInfo += unitPrice.get(i) + "\t\n";
            }
            receiptInfo = receiptInfo.replaceAll("\"", "");
            recInfo.add(receiptInfo);
            document.add(recInfo);
        } else {
            String receiptInformation[];
            List<List<String>> data = new ArrayList<>();
            String[] titles = {" Item Name", " Description", " Number", "Date", " Quantity", " Price"};
            Table table = new Table(titles.length);
            receiptInformation = new String[titles.length];

            data.add(Arrays.asList(titles));

            for (int i = 0; i < receipts.size(); i++) {
                List<String> dataList = new ArrayList<>();

                receiptInformation[0] = createResponse(itemName.get(i)).replaceAll("\"", "");
                receiptInformation[1] = createResponse(description.get(i)).replaceAll("\"", "");
                receiptInformation[2] = createResponse(docNumber.get(i)).replaceAll("\"", "");
                receiptInformation[3] = String.valueOf(convertToLocalDate(txnDate.get(i)));
                receiptInformation[4] = String.valueOf(quantity.get(i));
                receiptInformation[5] = String.valueOf(unitPrice.get(i));

                for (int j = 0; j < titles.length; j++) {
                    dataList.add(receiptInformation[j]);
                }
                data.add(dataList);
            }
            for (List<String> d : data) {
                for (String c : d) {
                    table.addCell(c);
                }
            }
            document.add(table);
        }
    }

    public static void getInvoiceInfo(Document document, List<Invoice> invoices, boolean tableChoice) {
        String invoiceInfo = new String();
        Paragraph inInfo = new Paragraph();

        List<String> itemName = invoices.stream().map(invoice -> invoice.getLine().get(0).getSalesItemLineDetail().getItemRef().getName()).collect(Collectors.toList());
        List<String> description = invoices.stream().map(invoice -> invoice.getLine().get(0).getDescription()).collect(Collectors.toList());
        List<String> docNumber = invoices.stream().map(invoice -> invoice.getDocNumber()).collect(Collectors.toList());
        List<Date> txnDate = invoices.stream().map(invoice -> invoice.getTxnDate()).collect(Collectors.toList());
        List<BigDecimal> quantity = invoices.stream().map(invoice -> invoice.getLine().get(0).getSalesItemLineDetail().getQty()).collect(Collectors.toList());
        List<BigDecimal> unitPrice = invoices.stream().map(invoice -> invoice.getLine().get(0).getSalesItemLineDetail().getUnitPrice()).collect(Collectors.toList());

        if (tableChoice == false) {
            for (int i = 0; i < invoices.size(); i++) {
                invoiceInfo += createResponse(itemName.get(i)) + "\t";
                invoiceInfo += createResponse(description.get(i)) + "\t";
                invoiceInfo += createResponse(docNumber.get(i)) + "\t";
                invoiceInfo += convertToLocalDate(txnDate.get(i)) + "\t";
                invoiceInfo += quantity.get(i) + "\t";
                invoiceInfo += unitPrice.get(i) + "\t\n";
            }
            invoiceInfo = invoiceInfo.replaceAll("\"", "");
            inInfo.add(invoiceInfo);
            document.add(inInfo);
        } else {
            String invoiceInformation[];
            List<List<String>> data = new ArrayList<>();
            String[] titles = {" Item Name", " Description", " Number", "Date", " Quantity", " Price"};
            Table table = new Table(titles.length);
            invoiceInformation = new String[titles.length];

            data.add(Arrays.asList(titles));

            for (int i = 0; i < invoices.size(); i++) {
                List<String> dataList = new ArrayList<>();

                invoiceInformation[0] = createResponse(itemName.get(i)).replaceAll("\"", "");
                invoiceInformation[1] = createResponse(description.get(i)).replaceAll("\"", "");
                invoiceInformation[2] = createResponse(docNumber.get(i)).replaceAll("\"", "");
                invoiceInformation[3] = String.valueOf(convertToLocalDate(txnDate.get(i)));
                invoiceInformation[4] = String.valueOf(quantity.get(i));
                invoiceInformation[5] = String.valueOf(unitPrice.get(i));

                for (int j = 0; j < titles.length; j++) {
                    dataList.add(invoiceInformation[j]);
                }
                data.add(dataList);
            }
            for (List<String> d : data) {
                for (String c : d) {
                    table.addCell(c);
                }
            }
            document.add(table);
        }
    }

    public static void getEstimatesInfo(Document document, List<Estimate> estimates, boolean tableChoice) {
        String estimatesInfo = new String();
        Paragraph estInfo = new Paragraph();

        List<String> itemName = estimates.stream().map(estimate -> estimate.getLine().get(0).getSalesItemLineDetail().getItemRef().getName()).collect(Collectors.toList());
        List<String> description = estimates.stream().map(estimate -> estimate.getLine().get(0).getDescription()).collect(Collectors.toList());
        List<String> docNumber = estimates.stream().map(estimate -> estimate.getDocNumber()).collect(Collectors.toList());
        List<Date> txnDate = estimates.stream().map(estimate -> estimate.getTxnDate()).collect(Collectors.toList());
        List<BigDecimal> quantity = estimates.stream().map(estimate -> estimate.getLine().get(0).getSalesItemLineDetail().getQty()).collect(Collectors.toList());
        List<BigDecimal> unitPrice = estimates.stream().map(estimate -> estimate.getLine().get(0).getSalesItemLineDetail().getUnitPrice()).collect(Collectors.toList());

        if (tableChoice == false) {
            for (int i = 0; i < estimates.size(); i++) {
                estimatesInfo += (createResponse(itemName.get(i)) + "\t");
                estimatesInfo += (createResponse(description.get(i)) + "\t");
                estimatesInfo += (createResponse(docNumber.get(i)) + "\t");
                estimatesInfo += convertToLocalDate(txnDate.get(i)) + "\t";
                estimatesInfo += (quantity.get(i) + "\t");
                estimatesInfo += (unitPrice.get(i) + "\t\n");
            }
            estimatesInfo = estimatesInfo.replaceAll("\"", "");
            estInfo.add(estimatesInfo);
            document.add(estInfo);
        } else {
            String estimateInformation[];
            List<List<String>> data = new ArrayList<>();
            String[] titles = {" Item Name", " Description", " Number", "Date", " Quantity", " Price"};
            Table table = new Table(titles.length);
            estimateInformation = new String[titles.length];

            data.add(Arrays.asList(titles));

            for (int i = 0; i < estimates.size(); i++) {
                List<String> dataList = new ArrayList<>();

                estimateInformation[0] = createResponse(itemName.get(i)).replaceAll("\"", "");
                estimateInformation[1] = createResponse(description.get(i)).replaceAll("\"", "");
                estimateInformation[2] = createResponse(docNumber.get(i)).replaceAll("\"", "");
                estimateInformation[3] = String.valueOf(convertToLocalDate(txnDate.get(i)));
                estimateInformation[4] = String.valueOf(quantity.get(i));
                estimateInformation[5] = String.valueOf(unitPrice.get(i));

                for (int j = 0; j < titles.length; j++) {
                    dataList.add(estimateInformation[j]);
                }
                data.add(dataList);
            }
            for (List<String> d : data) {
                for (String c : d) {
                    table.addCell(c);
                }
            }
            document.add(table);
        }
    }

    public static void getItemsInfo(Document document, List<Item> items, boolean tableChoice) {
        String itemsInfo = new String();
        Paragraph itInfo = new Paragraph();

        if (tableChoice == false) {
            for (Item i : items) {
                itemsInfo = itemsInfo;
                itemsInfo += createResponse(i.getFullyQualifiedName()) + "\t";
                itemsInfo += createResponse(i.getItemCategoryType()) + "\t";
                itemsInfo += createResponse(i.getDescription()) + "\t";
                itemsInfo += createResponse(i.getUnitPrice()) + "\t";
                itemsInfo += createResponse(i.getQtyOnHand()) + "\t\n";
            }
            itemsInfo = itemsInfo.replaceAll("\"", "");
            itInfo.add(itemsInfo);
            document.add(itInfo);
        } else {
            String itemsInformation[];
            List<List<String>> data = new ArrayList<>();
            String[] titles = {" Item Name", " Category", " Description", " Price", " Quantity"};
            Table table = new Table(titles.length);
            itemsInformation = new String[titles.length];

            data.add(Arrays.asList(titles));

            for (Item i : items) {
                List<String> dataList = new ArrayList<>();

                itemsInformation[0] = createResponse(i.getFullyQualifiedName()).replaceAll("\"", "");
                itemsInformation[1] = createResponse(i.getItemCategoryType()).replaceAll("\"", "");
                itemsInformation[2] = createResponse(i.getDescription()).replaceAll("\"", "");
                itemsInformation[3] = createResponse(i.getUnitPrice()).replaceAll("\"", "");
                itemsInformation[4] = createResponse(i.getQtyOnHand()).replaceAll("\"", "");

                for (int j = 0; j < titles.length; j++) {
                    dataList.add(itemsInformation[j]);
                }
                data.add(dataList);
            }
            for (List<String> d : data) {
                for (String c : d) {
                    table.addCell(c);
                }
            }
            document.add(table);
        }
    }

    public static void getPaymentsInfo(Document document, List<Payment> payments, boolean tableChoice) {
        String paymentsInfo = new String();
        Paragraph payInfo = new Paragraph();

        if (tableChoice == false) {
            for (Payment p : payments) {
                paymentsInfo += convertToLocalDate(p.getTxnDate()) + "  ";
                paymentsInfo += p.getDocNumber() + "  ";
                //paymentsInfo += p.getLine().get(0).getSalesItemLineDetail().getItemRef().getName() + "  ";
                //paymentsInfo += p.getLine().get(0).getSalesItemLineDetail().getQty() + "  ";
                paymentsInfo += p.getTotalAmt() + "\n";
            }
            paymentsInfo = paymentsInfo.replaceAll("\"", "");
            payInfo.add(paymentsInfo);
            document.add(payInfo);
        } else {
            String paymentsInformation[];
            List<List<String>> data = new ArrayList<>();
            String[] titles = {" Date", " Number", " Total Amount"};
            Table table = new Table(titles.length);
            paymentsInformation = new String[titles.length];

            data.add(Arrays.asList(titles));

            for (Payment p : payments) {
                List<String> dataList = new ArrayList<>();

                LocalDate date = convertToLocalDate(p.getTxnDate());
                paymentsInformation[0] = date.toString().replaceAll("\"", "");

                paymentsInformation[1] = createResponse(p.getDocNumber()).replaceAll("\"", "");
                paymentsInformation[2] = createResponse(p.getTotalAmt()).replaceAll("\"", "");

                for (int i = 0; i < titles.length; i++) {
                    dataList.add(paymentsInformation[i]);
                }
                data.add(dataList);
            }
            for (List<String> d : data) {
                for (String c : d) {
                    table.addCell(c);
                }
            }
            document.add(table);
        }
    }

    private static String createResponse(Object entity) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString;
        try {
            jsonInString = mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            return createErrorResponse(e);
        } catch (Exception e) {
            return createErrorResponse(e);
        }
        return jsonInString;
    }

    private static LocalDate convertToLocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH),
                month = calendar.get(Calendar.MONTH) + 1,
                year = calendar.get(Calendar.YEAR);

        return LocalDate.of(year, month, day);
    }

    private static void openFile(File file) {
        try {
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
                else {
                    System.out.println("Awt Desktop not supported.");
                }

            }
            else {
                System.out.println("File does not exists.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String createErrorResponse(Exception e) {
        return new JSONObject().put("response", "Failed").toString();
    }
}