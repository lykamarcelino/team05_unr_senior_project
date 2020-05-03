/**
 * Name: InventoryClass.java
 * Class to hold the Inventory information
 * Used for easy return as a JSON Object
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 * */

package com.intuit.developer.tutorials.objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InventoryClass{
    public List<String> namesArray = new ArrayList<String>();
    public List<String> itemsArray = new ArrayList<String>();
    public List<String> descArray = new ArrayList<String>();
    public List<String> priceArray = new ArrayList<String>();
    public List<BigDecimal> qtyArray = new ArrayList<BigDecimal>();

    public InventoryClass() {}
    public List<String> getNamesArray() {
        return namesArray;
    }
    public void setNamesArray(List<String> names) {
        this.namesArray = names;
    }

    public List<String> getItemsArray() {
        return itemsArray;
    }
    public void setItemsArray(List<String> items) {
        this.itemsArray = items;
    }

    public List<String> getDescArray() {
        return descArray;
    }
    public void setDescArray(List<String> desc) {
        this.descArray = desc;
    }

    public List<String> getPriceArray() {
        return priceArray;
    }
    public void setPriceArray(List<String> price) {
        this.priceArray = price;
    }

    public List<BigDecimal> getQtyArray() {
        return qtyArray;
    }
    public void setQtyArray(List<BigDecimal> qty) {
        this.qtyArray = qty;
    }

}
