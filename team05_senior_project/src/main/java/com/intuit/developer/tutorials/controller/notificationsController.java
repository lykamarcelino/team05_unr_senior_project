/**
 * Name: notificationsController.java
 * Description: Used to display the notifications for the User Dashboard
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 * */

package com.intuit.developer.tutorials.controller;

import com.intuit.developer.tutorials.objects.Notifications;
import com.intuit.developer.tutorials.objects.User;
import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.services.DataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.Date;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
@RequestMapping("/getNotifications")
public class notificationsController {
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Notifications getUserInJSON(){
        Notifications notifications = new Notifications();

            String warningHolder = "Lighting sales is predicted to be at a high for April.";
            notifications.warning = warningHolder;

            String inventoryHolder = "Rock Fountain is predicted to sell at the highest rate on 04/07.";
            notifications.inventory = inventoryHolder;

            String salesHolder = "05/14 is predicted to be the highest day of sales!";
            notifications.sales = salesHolder;

        return notifications;
    }
}
