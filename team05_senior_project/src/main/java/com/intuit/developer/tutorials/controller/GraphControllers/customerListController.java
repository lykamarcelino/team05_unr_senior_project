/**
 * Name: customerListController.java
 * Description: Controller that returns a list of the user's active customers
 * Date: 04/29/2020
 * Author: Liliana Pacheco
 * */

package com.intuit.developer.tutorials.controller.GraphControllers;

import com.intuit.developer.tutorials.controller.oauthController;
import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.ipp.data.Customer;
import com.intuit.ipp.services.DataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.developer.tutorials.helper.dataFrameHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.tablesaw.api.Table;

import java.util.List;
import java.util.Vector;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
@RequestMapping("/getCustomerList")
public class customerListController {
    @Autowired
    public QBOServiceHelper helper;

    @Autowired
    dataFrameHelper dfHelper;

    /**
     * @Name getCustomers
     * @return returns json response to customer list request
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<String> getCustomers(){
        String realmId = oauthController.realmIdHolder;
        String accessToken = oauthController.accessTokenHolder;

        try{
            DataService service = helper.getDataService(realmId, accessToken);
            List<String> customers = getCustomerNames(service);
            return customers;
        }
        catch(Exception e){
            System.out.println("Failed to retrieve items list.");
        }
        return null;
    }

    /**
     * @Name getCustomerNames
     * @param service - services with intuit api
     * @return returns a list of the Quickbooks business customers
     * @throws Exception
     */
    public List<String> getCustomerNames(DataService service) throws Exception {
        Table allData = dfHelper.loadAllData(service);
        allData = allData.sortAscendingOn("Customer_Name");
        List<String> customerNames = allData.stringColumn("Customer_Name").unique().asList();

        List<String> newLabels = new Vector<>();
        for(String s : customerNames){
            s = s.replaceAll("_", " ");
            if(!s.contains("deleted"))
                newLabels.add(s);
        }

        return newLabels;
    }
}
