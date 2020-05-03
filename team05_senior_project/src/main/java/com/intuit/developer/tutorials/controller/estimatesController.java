/**
 * Name: estimatesController.java
 * Description: Used to return the estimates information in JSON
 *  Uses EstimatesClass
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 * */

package com.intuit.developer.tutorials.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.ipp.data.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.ipp.services.DataService;
import com.intuit.developer.tutorials.objects.EstimatesClass;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
public class estimatesController {

    @Autowired
    OAuth2PlatformClientFactory factory;

    @Autowired
    recordsHelper recordsH;

    @Autowired
    public QBOServiceHelper helper;

    private static final Logger logger = Logger.getLogger(estimatesController.class);

    @RequestMapping(value = "/getEstimates", method = RequestMethod.GET)
    public @ResponseBody EstimatesClass transactionDisplay() {
        EstimatesClass newEstimate = new EstimatesClass();

        String realmId = oauthController.realmIdHolder;
        String accessToken = oauthController.accessTokenHolder;

        try {
            DataService service = helper.getDataService(realmId, accessToken);
            List<Estimate> estimates = recordsH.getEstimates(service);

            List<String> itemName = estimates.stream().map(estimate -> estimate.getLine().get(0).getSalesItemLineDetail().getItemRef().getName()).collect(Collectors.toList());
            List<String> description = estimates.stream().map(estimate -> estimate.getLine().get(0).getDescription()).collect(Collectors.toList());
            List<String> docNumber = estimates.stream().map(estimate -> estimate.getDocNumber()).collect(Collectors.toList());
            List<Date> txnDate = estimates.stream().map(estimate -> estimate.getTxnDate()).collect(Collectors.toList());
            List<BigDecimal> quantity = estimates.stream().map(estimate -> estimate.getLine().get(0).getSalesItemLineDetail().getQty()).collect(Collectors.toList());
            List<BigDecimal> unitPrice = estimates.stream().map(estimate -> estimate.getLine().get(0).getSalesItemLineDetail().getUnitPrice()).collect(Collectors.toList());

            newEstimate.setItemName(itemName);
            newEstimate.setDescription(description);
            newEstimate.setDocNumber(docNumber);
            newEstimate.setTxnDate(txnDate);
            newEstimate.setQuantity(quantity);
            newEstimate.setUnitPrice(unitPrice);



        } catch (Exception e) {
            System.out.println("Error creating inventory response");
        }
        return newEstimate;
    }
}