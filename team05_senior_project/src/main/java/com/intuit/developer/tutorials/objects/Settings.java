/**
 * Name: Settings.java
 * Class to hold the setting information
 * Used for easy return as a JSON Object
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 * */

package com.intuit.developer.tutorials.objects;

public class Settings {
    public String notifications;
    public String email;
    public String weekly;

    public Settings() {}
    public Settings(String notifications, String email, String weekly) {
        this.notifications = notifications;
        this.email  = email;
        this.weekly = weekly;
    }
    public String getNotifications() {
        return notifications;
    }
    public void setNotifications(String user) {
        this.notifications = notifications;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String company) {
        this.email = email;
    }
    public String getWeekly() {
        return weekly;
    }
    public void setWarning(String warning){this.weekly = weekly; }
}
