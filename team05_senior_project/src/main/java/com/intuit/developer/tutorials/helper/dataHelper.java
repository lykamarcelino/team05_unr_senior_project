package com.intuit.developer.tutorials.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.developer.tutorials.controller.newGraphController;
import com.intuit.ipp.data.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import sun.misc.Cleaner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class dataHelper {

    final int DATA_COLUMNS = 6;
    /*  Data to be collected is constant for all data types. The to be collected includes:
     *  description -- item description or name
     *  qty -- quantity purchased
     *  unitPrice -- price for single sold item
     *  docNumber -- attached ID to receipt, invoice, estimate or creditMemo
     *  txnDate -- transaction Date
     *  customerRef -- name of customer
     */

    private String [][] dataframe;
    private static final Logger logger = Logger.getLogger(newGraphController.class);

    public String [][] parseReceipts(List<SalesReceipt> receipts){
        dataframe = new String[receipts.size()][DATA_COLUMNS];
        List<String> itemName = receipts.stream().map(receipt -> receipt.getLine().get(0).getSalesItemLineDetail().getItemRef().getName()).collect(Collectors.toList());
        List<String> description = receipts.stream().map(receipt -> receipt.getLine().get(0).getDescription()).collect(Collectors.toList());
        List<String> docNumber = receipts.stream().map(receipt -> receipt.getDocNumber()).collect(Collectors.toList());
        List<Date> txnDate = receipts.stream().map(receipt -> receipt.getTxnDate()).collect(Collectors.toList());
        List<BigDecimal> quantity = receipts.stream().map(receipt -> receipt.getLine().get(0).getSalesItemLineDetail().getQty()).collect(Collectors.toList());
        List<BigDecimal> unitPrice = receipts.stream().map(receipt -> receipt.getLine().get(0).getSalesItemLineDetail().getUnitPrice()).collect(Collectors.toList());

        System.out.println("RECEIPTS: ");
        for(int i = 0; i < receipts.size(); i++){
            System.out.print(createResponse(itemName.get(i)) + "\t");
            System.out.print(createResponse(description.get(i)) + "\t");
            System.out.print(createResponse(docNumber.get(i)) + "\t");
            System.out.print(txnDate.get(i) + "\t");
            System.out.print(quantity.get(i) + "\t");
            System.out.println(unitPrice.get(i) + "\t");
        }
        return dataframe;
    }

    public String [][] parseInvoices(List<Invoice> invoices){
        dataframe = new String[invoices.size()][DATA_COLUMNS];
        List<String> itemName = invoices.stream().map(invoice -> invoice.getLine().get(0).getSalesItemLineDetail().getItemRef().getName()).collect(Collectors.toList());
        List<String> description = invoices.stream().map(invoice -> invoice.getLine().get(0).getDescription()).collect(Collectors.toList());
        List<String> docNumber = invoices.stream().map(invoice -> invoice.getDocNumber()).collect(Collectors.toList());
        List<Date> txnDate = invoices.stream().map(invoice -> invoice.getTxnDate()).collect(Collectors.toList());
        List<BigDecimal> quantity = invoices.stream().map(invoice -> invoice.getLine().get(0).getSalesItemLineDetail().getQty()).collect(Collectors.toList());
        List<BigDecimal> unitPrice = invoices.stream().map(invoice -> invoice.getLine().get(0).getSalesItemLineDetail().getUnitPrice()).collect(Collectors.toList());

        System.out.println("INVOICES: ");
        for(int i = 0; i < invoices.size(); i++){
            System.out.print(createResponse(itemName.get(i)) + "\t");
            System.out.print(createResponse(description.get(i)) + "\t");
            System.out.print(createResponse(docNumber.get(i)) + "\t");
            System.out.print(txnDate.get(i) + "\t");
            System.out.print(quantity.get(i) + "\t");
            System.out.println(unitPrice.get(i) + "\t");
        }
        return dataframe;
    }

    public String [][] parseEstimates(List<Estimate> estimates){
        dataframe = new String[estimates.size()][DATA_COLUMNS];
        List<String> itemName = estimates.stream().map(estimate -> estimate.getLine().get(0).getSalesItemLineDetail().getItemRef().getName()).collect(Collectors.toList());
        List<String> description = estimates.stream().map(estimate -> estimate.getLine().get(0).getDescription()).collect(Collectors.toList());
        List<String> docNumber = estimates.stream().map(estimate -> estimate.getDocNumber()).collect(Collectors.toList());
        List<Date> txnDate = estimates.stream().map(estimate -> estimate.getTxnDate()).collect(Collectors.toList());
        List<BigDecimal> quantity = estimates.stream().map(estimate -> estimate.getLine().get(0).getSalesItemLineDetail().getQty()).collect(Collectors.toList());
        List<BigDecimal> unitPrice = estimates.stream().map(estimate -> estimate.getLine().get(0).getSalesItemLineDetail().getUnitPrice()).collect(Collectors.toList());

        System.out.println("ESTIMATES: ");
        for(int i = 0; i < estimates.size(); i++){
            System.out.print(createResponse(itemName.get(i)) + "\t");
            System.out.print(createResponse(description.get(i)) + "\t");
            System.out.print(createResponse(docNumber.get(i)) + "\t");
            System.out.print(txnDate.get(i) + "\t");
            System.out.print(quantity.get(i) + "\t");
            System.out.println(unitPrice.get(i) + "\t");
        }
        return dataframe;
    }

    public String [][] parseCreditMemos(List<CreditMemo> creditMemos){
        dataframe = new String[creditMemos.size()][DATA_COLUMNS];
        List<String> itemName = creditMemos.stream().map(creditMemo -> creditMemo.getLine().get(0).getSalesItemLineDetail().getItemRef().getName()).collect(Collectors.toList());
        List<String> description = creditMemos.stream().map(creditMemo -> creditMemo.getLine().get(0).getDescription()).collect(Collectors.toList());
        List<String> docNumber = creditMemos.stream().map(creditMemo -> creditMemo.getDocNumber()).collect(Collectors.toList());
        List<Date> txnDate = creditMemos.stream().map(creditMemo -> creditMemo.getTxnDate()).collect(Collectors.toList());
        List<BigDecimal> quantity = creditMemos.stream().map(creditMemo -> creditMemo.getLine().get(0).getSalesItemLineDetail().getQty()).collect(Collectors.toList());
        List<BigDecimal> unitPrice = creditMemos.stream().map(creditMemo -> creditMemo.getLine().get(0).getSalesItemLineDetail().getUnitPrice()).collect(Collectors.toList());

        System.out.println("Credit Memos: ");
        for(int i = 0; i < creditMemos.size(); i++){
            System.out.print(createResponse(itemName.get(i)) + "\t");
            System.out.print(createResponse(description.get(i)) + "\t");
            System.out.print(createResponse(docNumber.get(i)) + "\t");
            System.out.print(txnDate.get(i) + "\t");
            System.out.print(quantity.get(i) + "\t");
            System.out.println(unitPrice.get(i) + "\t");
        }
        return dataframe;
    }

    public String parseItems(List<Item> items){
        String cleanItems = new String();
        for(Item i: items){
            cleanItems = cleanItems +
                         i.getFullyQualifiedName() + "\t" +
                         i.getItemCategoryType() + "\t" +
                         i.getDescription() + " \t" +
                         i.getUnitPrice() + "\t" +
                         i.getQtyOnHand() + "\t\n";

        }

        return cleanItems;
    }

    public String parseCustomers(List<Customer> customers){
        String cleanCustomers = new String();
        for(Customer c: customers){
            cleanCustomers = cleanCustomers +
                             c.getDisplayName() + "\t" +
                             c.getAcctNum() + "\t" +
                             c.getBalance() + "\t" +
                             c.getPrimaryPhone() + "\t\n";
        }

        return cleanCustomers;
    }

    public String parseTransactions(List<Deposit> deposits, List<Estimate> estimates, List<Invoice> invoices,
                                    List<CreditMemo> memos, List<SalesReceipt> receipts, List<Payment> payments){

        String cleanEntities = new String();

        cleanEntities = "Deposits: \n";
        for(Deposit d: deposits){
            cleanEntities = cleanEntities +
                            d.getTxnDate() + "\t" +
                            "Deposit" + "\t" +
                            d.getDocNumber() + "\t" +
                            d.getLine().get(0).getSalesItemLineDetail().getItemRef().getName() + "\t" +
                            d.getLine().get(0).getSalesItemLineDetail().getQty() + "\t" +
                            d.getTotalAmt() + "\n";
        }

        cleanEntities = cleanEntities + "\n\nEstimates:\n";
        for(Estimate e: estimates){
            cleanEntities = cleanEntities +
                            e.getTxnDate() + "\t" +
                            "Estimate" + "\t" +
                            e.getDocNumber() + "\t" +
                            e.getLine().get(0).getSalesItemLineDetail().getItemRef().getName() + "\t" +
                            e.getLine().get(0).getSalesItemLineDetail().getQty() + "\t" +
                            e.getTotalAmt() + "\n";
        }

        cleanEntities = cleanEntities + "\n\nInvoices:\n";
        for(Invoice i: invoices){
            cleanEntities = cleanEntities +
                    i.getTxnDate() + "\t" +
                    "Invoice" + "\t" +
                    i.getDocNumber() + "\t" +
                    i.getLine().get(0).getSalesItemLineDetail().getItemRef().getName() + "\t" +
                    i.getLine().get(0).getSalesItemLineDetail().getQty() + "\t" +
                    i.getTotalAmt() + "\n";
        }

        cleanEntities = cleanEntities + "\n\nCredit Memos:\n";
        for(CreditMemo m: memos){
            cleanEntities = cleanEntities +
                    m.getTxnDate() + "\t" +
                    "Credit Memo" + "\t" +
                    m.getDocNumber() + "\t" +
                    m.getLine().get(0).getSalesItemLineDetail().getItemRef().getName() + "\t" +
                    m.getLine().get(0).getSalesItemLineDetail().getQty() + "\t" +
                    m.getTotalAmt() + "\n";
        }

        cleanEntities = cleanEntities + "\n\nSales Receipts:\n";
        for(SalesReceipt r: receipts){
            cleanEntities = cleanEntities +
                    r.getTxnDate() + "\t" +
                    "Sales Receipt" + "\t" +
                    r.getDocNumber() + "\t" +
                    r.getLine().get(0).getSalesItemLineDetail().getItemRef().getName() + "\t" +
                    r.getLine().get(0).getSalesItemLineDetail().getQty() + "\t" +
                    r.getTotalAmt() + "\n";
        }

        cleanEntities = cleanEntities + "\n\nPayments:\n";
        for(Payment p: payments){
            cleanEntities = cleanEntities +
                    p.getTxnDate() + "\t" +
                    "Payment" + "\t" +
                    p.getDocNumber() + "\t" +
                    p.getLine().get(0).getSalesItemLineDetail().getItemRef().getName() + "\t" +
                    p.getLine().get(0).getSalesItemLineDetail().getQty() + "\t" +
                    p.getTotalAmt() + "\n";
        }

        return cleanEntities;
    }


    /**
     * Map object to json string
     * @param entity
     * @return
     */
    private String createResponse(Object entity) {
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

    private String createErrorResponse(Exception e) {
        logger.error("Exception while calling QBO ", e);
        return new JSONObject().put("response","Failed").toString();
    }
}
