/**
 * Name: InvoiceClass.java
 * Class to hold the Invoice information
 * Used for easy return as a JSON Object
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 * */

package com.intuit.developer.tutorials.objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceClass {
    public List<String> itemName = new ArrayList<String>();
    public List<String> description = new ArrayList<String>();
    public List<String> docNumber = new ArrayList<String>();
    public List<Date> txnDate = new ArrayList<Date>();
    public List<BigDecimal> quantity = new ArrayList<BigDecimal>();
    public List<BigDecimal> unitPrice = new ArrayList<BigDecimal>();

    public InvoiceClass() {}
    public List<String> getItemName() {
        return itemName;
    }
    public void setItemName(List<String> names) {
        this.itemName = names;
    }

    public List<String> getDescription() {
        return description;
    }
    public void setDescription(List<String> desc) {
        this.description = desc;
    }

    public List<String> getDocNumber() {
        return docNumber;
    }
    public void setDocNumber(List<String> doc) {
        this.docNumber = doc;
    }

    public List<Date> getTxnDate() {
        return txnDate;
    }
    public void setTxnDate(List<Date> date) {
        this.txnDate = date;
    }

    public List<BigDecimal> getQuantity() {
        return quantity;
    }
    public void setQuantity(List<BigDecimal> qty) {
        this.quantity = qty;
    }

    public List<BigDecimal> getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(List<BigDecimal> price) {
        this.unitPrice = price;
    }

}
