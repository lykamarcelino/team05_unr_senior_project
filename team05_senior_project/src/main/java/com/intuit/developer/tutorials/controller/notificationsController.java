/*
 * Reformatted controller to return the notification information
 * 04/04/2020 Returns JSON object
 */

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

            String warning = "Inventory Warning: There is not enough concrete to complete sales. ";
            notifications.setWarning(warning);

            String inventory = "Current inventory of item sprinklers will last until next week";
            notifications.setInventory(inventory);

            String sales = "Sales for sand are projected to sell at a higher rate than expected!" ;
            notifications.setSales(sales);

        return notifications;
    }
}
