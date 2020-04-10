/*
* Reformatted controller to return the User information
* 04/04/2020 Returns JSON object
*/

package com.intuit.developer.tutorials.controller;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.developer.tutorials.helper.dataFrameHelper;
import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.developer.tutorials.objects.User;
import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.services.DataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/getUser")
public class userController {
    @Autowired
    recordsHelper recordH;
    @Autowired
    OAuth2PlatformClientFactory factory;
    @Autowired
    public QBOServiceHelper helper;

    private static final Logger logger = Logger.getLogger(userController.class);

    @Autowired
    dataFrameHelper dfhelper;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    User getUserInJSON(){
        User user = new User();

        String realmId = oauthController.realmIdHolder;
        String accessToken = oauthController.accessTokenHolder;

        try {
            //Get DataService, used to query data from QuickBooks
            DataService service = helper.getDataService(realmId, accessToken);
            CompanyInfo companyInfo = recordH.getUserInfo(service);

            String userName = companyInfo.getLegalName();
            user.setUser(userName);
            String companyName = companyInfo.getCompanyName();
            user.setCompany(companyName);
            Date dateJoined = companyInfo.getCompanyStartDate();
            LocalDate date = dfhelper.convertToLocalDate(dateJoined);
            user.setDate(date.toString());
            String companyPhone = companyInfo.getPrimaryPhone().getFreeFormNumber();
            user.setCompany(companyPhone);
            String companyEmail = companyInfo.getEmail().getAddress();
            user.setEmail(companyEmail);

        } catch (Exception e) {
            System.out.println("Error creating response");
        }

        return user;
    }
}
