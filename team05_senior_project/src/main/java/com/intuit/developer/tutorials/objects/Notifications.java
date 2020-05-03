/**
 * Name: Notifications.java
 * Class to hold the notification information
 * Used for easy return as a JSON Object
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 * */

package com.intuit.developer.tutorials.objects;

public class Notifications {
    public String warning;
    public String inventory;
    public String sales;

    public Notifications() {}
    public Notifications(String warning, String inventory, String sales) {
        this.warning = warning;
        this.inventory = inventory;
        this.sales = sales;
    }
    public String getInventory() {
        return inventory;
    }
    public void setInventory(String user) {
        this.inventory = inventory;
    }
    public String getSales() {
        return sales;
    }
    public void setSales(String company) {
        this.sales = sales;
    }
    public String getWarning() {
        return warning;
    }
    public void setWarning(String warning){this.warning = warning; }
}
