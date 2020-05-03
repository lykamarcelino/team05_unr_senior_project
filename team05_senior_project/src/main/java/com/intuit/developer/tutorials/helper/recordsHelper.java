/**
 * Name: recordsHelper.java
 * Description:  query petitions to the quickbooks API and returns the correct list of
 * objects/data. provides scalable and non scalable queries for petitions over 1000 records.
 * Date: 04/29/2020
 * Author: Liliana Pacheco
 * */
package com.intuit.developer.tutorials.helper;

import com.intuit.ipp.data.*;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class recordsHelper {
    int pagination = 25;

    @Autowired
    public QBOServiceHelper helper;

    public int getPagination() {
        return pagination;
    }

    /**
     * Retrieve the number of Customers
     **/
    public int getNumCustomers(DataService service) throws FMSException {
        int num = service.executeQuery("SELECT COUNT(*)FROM Customer").getTotalCount();
        return num;
    }

    /**
     * Retrieve pagination amount Customer Records
     **/
    public List<Customer> getCustomerBatch(DataService service, int start) throws FMSException, Exception {
        if ((start < 0) || (start > getNumCustomers(service))) {
            throw new Exception("Invalid Invalid start position was used.");
        }

        List<Customer> customers = (List<Customer>) service.executeQuery("select * from Customer STARTPOSITION " + start + " MAXRESULTS " + pagination).getEntities();
        return customers;
    }

    /**
     * Retrieve All Customer Records
     **/
    public List<Customer> getCustomers(DataService service) throws FMSException {
        List<Customer> customers = (List<Customer>) service.executeQuery("select * from Customer").getEntities();
        return customers;
    }

    /**
     * Retrieve Specific Customer Record
     **/
    public List<Customer> getCustomer(DataService service, String customerID) throws Exception {
        List<Customer> customers = (List<Customer>) service.executeQuery("select * from Customer Where DisplayName = " + "'" + customerID + "'").getEntities();
        return customers;
    }


    /**
     * Retrieve the number of Items
     **/
    public int getNumItems(DataService service) throws FMSException {
        int num = service.executeQuery("SELECT COUNT(*)FROM Item").getTotalCount();
        return num;
    }

    /**
     * Retrieve pagination Item Records
     **/
    public List<Item> getItemBatch(DataService service, int start) throws FMSException, Exception {
        if ((start < 0) || (start > getNumItems(service))) {
            throw new Exception("Invalid Invalid start position was used.");
        }

        List<Item> items = (List<Item>) service.executeQuery("select * from Item STARTPOSITION " + start + " MAXRESULTS " + pagination).getEntities();
        return items;
    }

    /**
     * Retrieve All Item Records
     **/
    public List<Item> getItems(DataService service) throws FMSException {
        List<Item> items = (List<Item>) service.executeQuery("select * from Item").getEntities();
        return items;
    }

    /**
     * Retrieve Specific Item record
     **/
    public List<Item> getItem(DataService service, String itemID) throws FMSException {
        List<Item> items = (List<Item>) service.executeQuery("select * from Item Where Name = " + "'" + itemID + "'").getEntities();
        return items;
    }

    /**
     * Retrieve the number of Estimates
     **/
    public int getNumEstimates(DataService service) throws FMSException {
        int num = service.executeQuery("SELECT COUNT(*)FROM Estimate").getTotalCount();
        return num;
    }

    /**
     * Retrieve pagination Estimate Records
     **/
    public List<Estimate> getEstimateBatch(DataService service, int start) throws FMSException, Exception {
        if ((start < 0) || (start > getNumEstimates(service))) {
            throw new Exception("Invalid Invalid start position was used.");
        }

        List<Estimate> estimates = (List<Estimate>) service.executeQuery("select * from Estimate STARTPOSITION " + start + " MAXRESULTS " + pagination).getEntities();
        return estimates;
    }

    /**
     * Retrieve All Estimate Records
     **/
    public List<Estimate> getEstimates(DataService service) throws FMSException {
        List<Estimate> estimates = (List<Estimate>) service.executeQuery("select * from Estimate").getEntities();
        return estimates;
    }

    /**
     * Retrieve the number of Invoices
     **/
    public int getNumInvoices(DataService service) throws FMSException {
        int num = service.executeQuery("SELECT COUNT(*)FROM Invoice").getTotalCount();
        return num;
    }

    /**
     * Retrieve pagination Invoice Records
     **/
    public List<Invoice> getInvoiceBatch(DataService service, int start) throws Exception, Exception {
        if ((start < 0) || (start > getNumInvoices(service))) {
            throw new Exception("Invalid Invalid start position was used.");
        }

        List<Invoice> invoices = (List<Invoice>) service.executeQuery("select * from Invoice STARTPOSITION " + start + " MAXRESULTS " + pagination).getEntities();
        return invoices;
    }

    /**
     * Retrieve All Invoice Records
     **/
    public List<Invoice> getInvoices(DataService service) throws Exception {
        List<Invoice> invoices = (List<Invoice>) service.executeQuery("select * from Invoice").getEntities();
        return invoices;
    }

    /**
     * Retrieve the number of Sale Receipts
     **/
    public int getNumReceipts(DataService service) throws FMSException {
        int num = service.executeQuery("SELECT COUNT(*)FROM SalesReceipt").getTotalCount();
        return num;
    }

    /**
     * Retrieve pagination Sales Receipt Records
     **/
    public List<SalesReceipt> getSalesReceiptBatch(DataService service, int start) throws Exception {
        if ((start < 0) || (start > getNumReceipts(service))) {
            throw new Exception("Invalid Invalid start position was used.");
        }

        List<SalesReceipt> salesReceipts = (List<SalesReceipt>) service.executeQuery("select * from SalesReceipt STARTPOSITION " + start + " MAXRESULTS " + pagination).getEntities();
        return salesReceipts;
    }

    /**
     * Retrieve All Sales Receipt Records
     **/
    public List<SalesReceipt> getSalesReceipts(DataService service) throws Exception {
        List<SalesReceipt> salesReceipts = (List<SalesReceipt>) service.executeQuery("select * from SalesReceipt").getEntities();
        return salesReceipts;
    }

    /**
     * Retrieve the number of Credit Memos
     **/
    public int getNumMemos(DataService service) throws FMSException {
        int num = service.executeQuery("SELECT COUNT(*)FROM CreditMemo").getTotalCount();
        return num;
    }

    /**
     * Retrieve pagination Credit Memo Records
     **/
    public List<CreditMemo> getCreditMemoBatch(DataService service, int start) throws Exception {
        if ((start < 0) || (start > getNumMemos(service))) {
            throw new Exception("Invalid Invalid start position was used.");
        }

        List<CreditMemo> creditMemos = (List<CreditMemo>) service.executeQuery("select * from CreditMemo STARTPOSITION " + start + " MAXRESULTS " + pagination).getEntities();
        return creditMemos;
    }

    /**
     * Retrieve All Credit Memo Records
     **/
    public List<CreditMemo> getCreditMemos(DataService service) throws Exception {
        List<CreditMemo> creditMemos = (List<CreditMemo>) service.executeQuery("select * from CreditMemo").getEntities();
        return creditMemos;
    }

    /**
     * Retrieve All Deposit Records
     **/
    public List<Deposit> getDeposits(DataService service) throws Exception {
        List<Deposit> deposits = (List<Deposit>) service.executeQuery("select * from Deposit").getEntities();
        return deposits;
    }

    /**
     * Retrieve All Payment Records
     **/
    public List<Payment> getPayments(DataService service) throws Exception {
        List<Payment> payments = (List<Payment>) service.executeQuery("select * from Payment").getEntities();
        return payments;
    }

    /**
     * Retrieve All Bill Records
     **/
    public List<Bill> getBills(DataService service) throws Exception {
        List<Bill> bills = (List<Bill>) service.executeQuery("select * from Bill").getEntities();
        return bills;
    }

    public CompanyInfo getUserInfo(DataService service) throws Exception {
        // get all company info
        CompanyInfo companyInfo = (CompanyInfo) service.executeQuery("select * from companyinfo").getEntities().get(0);
        return companyInfo;
    }
}
