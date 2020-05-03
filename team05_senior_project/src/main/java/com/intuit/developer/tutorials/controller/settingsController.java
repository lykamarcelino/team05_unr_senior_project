/**
 * Name: settingsController.java
 * Controller to handle changes to the user settings
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 * */

package com.intuit.developer.tutorials.controller;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.developer.tutorials.objects.Notifications;
import com.intuit.developer.tutorials.objects.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
@RequestMapping("/getSettings")
public class settingsController {

    @Autowired
    OAuth2PlatformClientFactory factory;

    @Autowired
    public QBOServiceHelper helper;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    void settingsDisplay(@RequestHeader("Notifications")String changeNotifications, @RequestHeader("Email") String changeEmail, @RequestHeader("Weekly") String changeWeekly) {

        //Will read in the input from the frontend and process to change the settings
        //Setting changes are not actually being made as notifications, email, and weekly changes are not being processed

        Settings settings = new Settings();

        settings.notifications = changeNotifications;
        settings.email = changeEmail;
        settings.weekly = changeWeekly;
    }

}
