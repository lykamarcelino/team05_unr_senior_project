package com.intuit.developer.tutorials.helper;

import com.intuit.ipp.data.*;
import com.intuit.ipp.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.tablesaw.api.*;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.selection.Selection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@Service
public class dataFrameHelper {
    String path = Paths.get("").toAbsolutePath().toString();
    String mainFolderName = "Data_Frames";
    String customerDataMainFileName = "All_Customer_Data";
    String generalSalesFileName = "General_Sales";

    String customerSalesFolderName = "All_Customer_Sales";
    String itemQuantitySalesFolderName = "All_Item_Quantity_Sales";
    String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    Table lastDateRecord = Table.create("Last_Date_Record", StringColumn.create("File_Name"), DateColumn.create("Last_Date"));

    @Autowired
    recordsHelper recordH;

    @Autowired
    tableOperations operationH;

    public void makeDataCSV(DataService service) throws Exception {
        Table allData;

        allData = getReceiptData(service);
        allData.append(getInvoiceData(service));
        allData.append(getEstimateData(service));
        allData.append(getMemoData(service));
        allData = allData.sortOn("Date"); //Should always be sorted by date

        saveAsCSV(allData, customerDataMainFileName);
        saveAsARFF(allData, customerDataMainFileName, getCustomerDataHeader(service));
    }

    /**
     * Prediction Frequency can only be daily, weekly, or monthly
     **/
    public void makeGeneralSalesARFF(DataService service) throws Exception {
        //All CSV files are a subset of customer Data no need to call api if data is already available
        Table allData = loadAllData(service);

        Table generalSales = Table.create(generalSalesFileName);
        generalSales.addColumns(allData.column("Date"), allData.column("Total_Amount"));

        generalSales = operationH.removeTableOutliers(generalSales, "Total_Amount");
        generalSales = operationH.processTable(generalSales, "Date", "Total_Amount");

        if (operationH.highDensityTable(generalSales, "Date")) { //if table has too much data then records should be processed by sections.
            List<Table> generalSalesByDay = operationH.fragmentTableNoise(generalSales, "Date");

            int count = 0;
            LocalDate lastDate = generalSalesByDay.get(0).dateColumn("Date").max();

            for (Table t : generalSalesByDay) {
                saveAsARFFWithFolder(t, "General_Sales_Frag_" + daysOfWeek[count], "General_Sales_By_Weekday", getGeneralSalesHeader());
                count = count + 1;
            }
        }
        else {
            saveAsARFF(generalSales, generalSalesFileName, getGeneralSalesHeader());
        }

        LocalDate date = generalSales.dateColumn("Date").max();
        lastDateRecord.dateColumn("Last_Date").append(generalSales.dateColumn("Date").max());
        lastDateRecord.stringColumn("File_Name").append("General_Sales");
    }

    public void makeItemSalesARFF(DataService service) throws Exception {
        List<Table> itemSalesTables = getItemTables(service);
        for (Table t : itemSalesTables) {
            t = operationH.removeTableOutliers(t, "Quantity");
            t = operationH.processTable(t, "Date", "Quantity"); //join by dates and compress to total quantity

            lastDateRecord.dateColumn("Last_Date").append(t.dateColumn("Date").max());
            lastDateRecord.stringColumn("File_Name").append(t.name());

            if (operationH.highDensityTable(t, "Date")) {
                List<Table> fragTables = operationH.fragmentTableNoise(t, "Date");
                int count = 0;
                for (Table ft : fragTables) {
                    saveAsARFFWithFolder(t, t.name() + "_frag_" + daysOfWeek[count], itemQuantitySalesFolderName + "/" + t.name(), getItemSalesHeader(t.name()));
                    count = count + 1;
                }
            }
            else {
                saveAsARFFWithFolder(t, t.name(), itemQuantitySalesFolderName, getItemSalesHeader(t.name()));
            }
        }
    }

    public void makeCustomerSalesARFF(DataService service) throws Exception {
        List<Table> customerSalesTables = getCustomerTables(service);
        for (Table t : customerSalesTables) {
            t = operationH.removeTableOutliers(t, "Total_Amount");
            t = operationH.processTable(t, "Date", "Total_Amount"); //join by dates and compress to total quantity

            lastDateRecord.dateColumn("Last_Date").append(t.dateColumn("Date").max());
            lastDateRecord.stringColumn("File_Name").append(t.name());

            if (operationH.highDensityTable(t, "Date")) {
                List<Table> fragTables = operationH.fragmentTableNoise(t, "Date");
                int count = 0;
                for (Table ft : fragTables) {
                    saveAsARFFWithFolder(t, t.name() + "_frag_" + daysOfWeek[count], customerSalesFolderName + "/" + t.name(), getCustomerPurchaseHeader(t.name()));
                    count = count + 1;
                }
            }
            else {
                saveAsARFFWithFolder(t, t.name(), customerSalesFolderName, getItemSalesHeader(t.name()));
            }
        }
    }

    public LocalDate getLastDateRecord(String fileName) throws Exception {
        int index;
        try{
            index =  lastDateRecord.stringColumn("File_Name").firstIndexOf(fileName);
        }
        catch (Exception e) {
            throw new Exception("Record does not Exist!");
        }

        return lastDateRecord.dateColumn("Last_Date").get(index);
    }

    private List<Table> getItemTables(DataService service) throws Exception {
        Table allData = loadAllData(service);
        allData = (Table) allData.removeColumns("Customer_Name", "Total_Amount");

        List<String> itemNames = getItemNames(service);

        List<Table> itemSaleTables = new Vector<>();
        Table temp;
        for (String itemName : itemNames) {
            if (!itemName.equals("Hours") && !itemName.equals("Services")) {
                Selection itemFilter = allData.stringColumn("Item_Name").isEqualTo(itemName);
                temp = allData.where(itemFilter);
                temp.removeColumns("Item_Name");
                temp.setName(itemName);

                if (!temp.isEmpty())
                    itemSaleTables.add(temp);
            }
        }

        return itemSaleTables;
    }

    private List<Table> getCustomerTables(DataService service) throws Exception {
        Table allData = loadAllData(service);
        allData = (Table) allData.removeColumns("Item_Name", "Quantity");

        List<String> customerNames = getCustomerNames(service);
        List<Table> customerPurchaseTables = new Vector<>();
        Table temp;

        for (String customerName : customerNames) {
            Selection customerFilter = allData.stringColumn("Customer_Name").isEqualTo(customerName);
            temp = allData.where(customerFilter);
            temp.removeColumns("Customer_Name");
            temp.setName(customerName);

            if (!temp.isEmpty())
                customerPurchaseTables.add(temp);
        }

        return customerPurchaseTables;
    }

    private Table getReceiptData(DataService service) throws Exception {
        Table receiptData = Table.create("Customer_Data");
        DateColumn dateColumn = DateColumn.create("Date");
        StringColumn customerNameColumn = StringColumn.create("Customer_Name");
        StringColumn itemNameColumn = StringColumn.create("Item_Name");
        DoubleColumn quantityColumn = DoubleColumn.create("Quantity");
        DoubleColumn saleAmountColumn = DoubleColumn.create("Total_Amount");

        int numReceipts = recordH.getNumReceipts(service),
                startPosition = 1;

        List<SalesReceipt> receipts;
        LocalDate date;
        String customerName, itemName;
        Double quantity, saleAmount;

        while (startPosition < numReceipts) {
            receipts = recordH.getSalesReceiptBatch(service, startPosition);

            for (SalesReceipt receipt : receipts) {
                //get Constant data of each receipt
                date = convertToLocalDate(receipt.getTxnDate());
                customerName = fixLabel(receipt.getCustomerRef().getName());

                //Traverse Receipt Transactions
                List<Line> subReceipts = receipt.getLine();
                for (Line line : subReceipts) {
                    if (line.getDetailType().equals(LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL)) {
                        itemName = fixLabel(line.getSalesItemLineDetail().getItemRef().getName());
                        try {
                            quantity = line.getSalesItemLineDetail().getQty().doubleValue();
                        } catch (Exception e) {
                            quantity = 1.0;
                        }
                        saleAmount = line.getAmount().doubleValue();

                        //append collected data to table
                        dateColumn.append(date);
                        customerNameColumn.append(customerName);
                        itemNameColumn.append(itemName);
                        quantityColumn.append(quantity);
                        saleAmountColumn.append(saleAmount);
                    }
                }
            }

            //get next batch of Receipts
            startPosition = startPosition + recordH.getPagination();
        }
        receiptData.addColumns(dateColumn, customerNameColumn, itemNameColumn, quantityColumn, saleAmountColumn);

        return receiptData;
    }

    private Table getInvoiceData(DataService service) throws Exception {
        Table invoiceData = Table.create("Customer_Data");
        DateColumn dateColumn = DateColumn.create("Date");
        StringColumn customerNameColumn = StringColumn.create("Customer_Name");
        StringColumn itemNameColumn = StringColumn.create("Item_Name");
        DoubleColumn quantityColumn = DoubleColumn.create("Quantity");
        DoubleColumn saleAmountColumn = DoubleColumn.create("Total_Amount");

        int numInvoices = recordH.getNumInvoices(service),
                startPosition = 1;

        List<Invoice> invoices;
        LocalDate date;
        String customerName, itemName;
        Double quantity, saleAmount;

        while (startPosition < numInvoices) {
            invoices = recordH.getInvoiceBatch(service, startPosition);

            for (Invoice invoice : invoices) {
                //get Constant data of each invoice
                date = convertToLocalDate(invoice.getTxnDate());
                customerName = fixLabel(invoice.getCustomerRef().getName());

                //Traverse sub Invoice Transactions
                List<Line> subInvoices = invoice.getLine();
                for (Line line : subInvoices) {
                    if (line.getDetailType().equals(LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL)) {
                        itemName = fixLabel(line.getSalesItemLineDetail().getItemRef().getName());
                        try {
                            quantity = line.getSalesItemLineDetail().getQty().doubleValue();
                        } catch (Exception e) {
                            quantity = 1.0;
                        }
                        saleAmount = line.getAmount().doubleValue();

                        //append collected data to table
                        dateColumn.append(date);
                        customerNameColumn.append(customerName);
                        itemNameColumn.append(itemName);
                        quantityColumn.append(quantity);
                        saleAmountColumn.append(saleAmount);
                    }
                }
            }

            //get next batch of invoices
            startPosition = startPosition + recordH.getPagination();
        }
        invoiceData.addColumns(dateColumn, customerNameColumn, itemNameColumn, quantityColumn, saleAmountColumn);

        return invoiceData;
    }

    private Table getEstimateData(DataService service) throws Exception {
        Table estimateData = Table.create("Customer_Data");
        DateColumn dateColumn = DateColumn.create("Date");
        StringColumn customerNameColumn = StringColumn.create("Customer_Name");
        StringColumn itemNameColumn = StringColumn.create("Item_Name");
        DoubleColumn quantityColumn = DoubleColumn.create("Quantity");
        DoubleColumn saleAmountColumn = DoubleColumn.create("Total_Amount");

        int numEstimates = recordH.getNumEstimates(service),
                startPosition = 1;

        List<Estimate> estimates;
        LocalDate date;
        String customerName, itemName;
        Double quantity, saleAmount;

        while (startPosition < numEstimates) {
            estimates = recordH.getEstimateBatch(service, startPosition);

            for (Estimate estimate : estimates) {
                //get Constant data of each estimate
                date = convertToLocalDate(estimate.getTxnDate());
                customerName = fixLabel(estimate.getCustomerRef().getName());

                //Traverse estimate Transactions
                List<Line> subEstimates = estimate.getLine();
                for (Line line : subEstimates) {
                    if (line.getDetailType().equals(LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL)) {
                        itemName = fixLabel(line.getSalesItemLineDetail().getItemRef().getName());
                        try {
                            quantity = line.getSalesItemLineDetail().getQty().doubleValue();
                        } catch (Exception e) {
                            quantity = 1.0;
                        }
                        saleAmount = line.getAmount().doubleValue();

                        //append collected data to table
                        dateColumn.append(date);
                        customerNameColumn.append(customerName);
                        itemNameColumn.append(itemName);
                        quantityColumn.append(quantity);
                        saleAmountColumn.append(saleAmount);
                    }
                }
            }

            //get next batch of Estimate
            startPosition = startPosition + recordH.getPagination();
        }
        estimateData.addColumns(dateColumn, customerNameColumn, itemNameColumn, quantityColumn, saleAmountColumn);

        return estimateData;
    }

    private Table getMemoData(DataService service) throws Exception {
        Table memoData = Table.create("Customer_Data");
        DateColumn dateColumn = DateColumn.create("Date");
        StringColumn customerNameColumn = StringColumn.create("Customer_Name");
        StringColumn itemNameColumn = StringColumn.create("Item_Name");
        DoubleColumn quantityColumn = DoubleColumn.create("Quantity");
        DoubleColumn saleAmountColumn = DoubleColumn.create("Total_Amount");

        int numMemos = recordH.getNumMemos(service),
                startPosition = 1;

        List<CreditMemo> memos;
        LocalDate date;
        String customerName, itemName;
        Double quantity, saleAmount;

        while (startPosition < numMemos) {
            memos = recordH.getCreditMemoBatch(service, startPosition);

            for (CreditMemo memo : memos) {
                //get Constant data of each memo
                date = convertToLocalDate(memo.getTxnDate());
                customerName = fixLabel(memo.getCustomerRef().getName());

                //Traverse memo Transactions
                List<Line> subMemos = memo.getLine();
                for (Line line : subMemos) {
                    if (line.getDetailType().equals(LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL)) {
                        itemName = fixLabel(line.getSalesItemLineDetail().getItemRef().getName());
                        try {
                            quantity = line.getSalesItemLineDetail().getQty().doubleValue();
                        } catch (Exception e) {
                            quantity = 1.0;
                        }
                        saleAmount = line.getAmount().doubleValue();

                        //append collected data to table
                        dateColumn.append(date);
                        customerNameColumn.append(customerName);
                        itemNameColumn.append(itemName);
                        quantityColumn.append(quantity);
                        saleAmountColumn.append(saleAmount);
                    }
                }
            }

            //get next batch of Memos
            startPosition = startPosition + recordH.getPagination();
        }
        memoData.addColumns(dateColumn, customerNameColumn, itemNameColumn, quantityColumn, saleAmountColumn);

        return memoData;
    }

    private List<String> getItemNames(DataService service) throws Exception {
        int numItems = recordH.getNumItems(service),
                start = 1;

        List<String> itemNames = new Vector<>();
        List<Item> items;

        while (start < numItems) {
            items = recordH.getItemBatch(service, start);
            start = start + recordH.getPagination();

            for (Item i : items) {
                itemNames.add(fixLabel(i.getName()));
            }
        }
        return itemNames;
    }

    private List<String> getCustomerNames(DataService service) throws Exception {
        int numCustomers = recordH.getNumCustomers(service),
                start = 1;

        List<String> customerNames = new Vector<>();
        List<Customer> customers;

        while (start < numCustomers) {
            customers = recordH.getCustomerBatch(service, start);
            start = start + recordH.getPagination();

            for (Customer c : customers) {
                customerNames.add(fixLabel(c.getDisplayName()));
            }
        }
        return customerNames;
    }

    public LocalDate convertToLocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH),
                month = (calendar.get(Calendar.MONTH) + 1),
                year = calendar.get(Calendar.YEAR);

        return LocalDate.of(year, month, day);
    }

    private String fixLabel(String label) {
        label = label.replace(" ", "_");
        label = label.replace("&", "and");
        label = label.replaceAll("[^a-zA-Z0-9_]", "");

        return label;
    }

    private Table loadAllData(DataService service) throws Exception {
        if (!CSVExists(customerDataMainFileName)) {
            makeDataCSV(service);
        }

        ColumnType[] types = {ColumnType.LOCAL_DATE, ColumnType.STRING, ColumnType.STRING, ColumnType.DOUBLE, ColumnType.DOUBLE};
        File file = new File(path + "/" + mainFolderName + "/" + customerDataMainFileName + ".csv");
        CsvReadOptions.Builder builder = CsvReadOptions.builder(file).columnTypes(types);
        Table allData = Table.read().csv(builder);

        return allData;
    }

    /**
     * Note this function check for the existence of a file only in main folder directory
     **/
    private boolean CSVExists(String fileName) {
        File file = new File(path + "/" + mainFolderName + "/" + fileName + ".csv");
        return file.exists();
    }

    private void saveAsARFF(Table table, String fileName, String header) throws IOException {
        File directory = new File(path + "/" + mainFolderName + "/");
        if (!directory.isDirectory() || !directory.exists()) {
            directory.mkdir();
        }

        File arrfFile = new File(path + "/" + mainFolderName + "/" + fileName + ".arff");
        arrfFile.createNewFile();

        FileWriter writer = new FileWriter(arrfFile);
        writer.write(header + "%");//make csv header a comment for arff file and fill appropriate arff file header

        if (!table.isEmpty())
            table.write().csv(writer);
        writer.close();
    }

    private void saveAsARFFWithFolder(Table table, String fileName, String directoryName, String header) throws IOException {
        File directory = new File(path + "/" + mainFolderName + "/");
        if (!directory.isDirectory() || !directory.exists()) {
            directory.mkdir();
        }

        File newDir = new File(path + "/" + mainFolderName + "/" + directoryName + "/");
        if (!newDir.isDirectory() || !newDir.exists()) {
            newDir.mkdir();
        }

        File arrfFile = new File(path + "/" + mainFolderName + "/" + directoryName + "/" + fileName + ".arff");
        arrfFile.createNewFile();

        FileWriter writer = new FileWriter(arrfFile);
        writer.write(header + "%");//make csv header a comment for arff file and fill appropriate arff file header

        if (!table.isEmpty())
            table.write().csv(writer);
        writer.close();
    }

    private void saveAsCSV(Table table, String fileName) throws IOException {
        File directory = new File(path + "/" + mainFolderName + "/");
        if (!directory.isDirectory() || !directory.exists()) {
            directory.mkdir();
        }

        File cvsFile = new File(path + "/" + mainFolderName + "/" + fileName + ".csv");
        cvsFile.createNewFile();
        if (!table.isEmpty())
            table.write().csv(cvsFile);
    }

    private void saveAsCSVWithFolder(Table table, String fileName, String directoryName) throws IOException {
        File directory = new File(path + "/" + mainFolderName + "/");
        if (!directory.isDirectory() || !directory.exists()) {
            directory.mkdir();
        }

        File newDir = new File(path + "/" + mainFolderName + "/" + directoryName + "/");
        if (!newDir.isDirectory() || !newDir.exists()) {
            newDir.mkdir();
        }

        File cvsFile = new File(path + "/" + mainFolderName + "/" + directoryName + "/" + fileName + ".csv");
        cvsFile.createNewFile();
        if (!table.isEmpty())
            table.write().csv(cvsFile);
    }

    private String getGeneralSalesHeader() {
        return "@relation General_Sales\n" +
                "@attribute Date date 'yyyy-MM-dd'\n" +
                "@attribute Total_Amount numeric\n\n" +
                "@data\n";
    }

    private String getItemSalesHeader(String itemName) {
        return "@relation " + itemName + "_Sales\n" +
                "@attribute Date date 'yyyy-MM-dd'\n" +
                "@attribute Quantity numeric\n\n" +
                "@data\n";
    }

    private String getCustomerPurchaseHeader(String customerName) {
        return "@relation " + customerName + "_Sales\n" +
                "@attribute Date date 'yyyy-MM-dd'\n" +
                "@attribute Total_Amount numeric\n\n" +
                "@data\n";
    }

    private String getCustomerDataHeader(DataService service) throws Exception {
        String header = new String();
        List<String> itemNames = getItemNames(service);
        List<String> customerNames = getCustomerNames(service);

        String itemNominalList = makeNominalList(itemNames),
                customerNominalList = makeNominalList(customerNames);

        header = "@relation ALL_DATA\n" +
                "@attribute Date date 'yyyy-MM-dd'\n" +
                "@attribute Customer_Name " + customerNominalList + "\n" +
                "@attribute Item_Name " + itemNominalList + "\n" +
                "@attribute Quantity numeric\n" +
                "@attribute Total_Amount numeric\n\n" +
                "@data\n";

        return header;
    }

    private String makeNominalList(List<String> labels) {
        String header = new String("{ ");
        for (String label : labels) {
            header = header + label + ", ";
        }
        header = header.substring(0, header.length() - 2) + "}";
        return header;
    }

}
