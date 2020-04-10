/*
* Java class to format the User information
*/

package com.intuit.developer.tutorials.objects;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.controller.userController;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.developer.tutorials.helper.recordsHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class User {

    @Autowired
    recordsHelper recordH;
    @Autowired
    OAuth2PlatformClientFactory factory;
    @Autowired
    public QBOServiceHelper helper;

    private static final Logger logger = Logger.getLogger(userController.class);

    public String user;
    public String company;
    public String phone;
    public String email;
    public String date;

    public User() {}
    public User(String user, String company, String phone, String email, String date) {
        this.user = user;
        this.company = company;
        this.phone = phone;
        this.email = email;
        this.date = date;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}

