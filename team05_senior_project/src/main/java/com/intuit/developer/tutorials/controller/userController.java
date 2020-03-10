package com.intuit.developer.tutorials.controller;

import javax.servlet.http.HttpSession;

import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.ipp.data.*;
import com.intuit.ipp.services.DataService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;

import java.util.Date;

@Controller
public class userController {

    @Autowired
    OAuth2PlatformClientFactory factory;

    @Autowired
    recordsHelper recordH;

    @Autowired
    public QBOServiceHelper helper;

    private static final Logger logger = Logger.getLogger(userController.class);

    @ResponseBody
    @RequestMapping("/getUser")
    public String userDisplay(HttpSession session) {
        String realmId = (String)session.getAttribute("realmId");
        if (StringUtils.isEmpty(realmId)) {
            return new JSONObject().put("response", "No realm ID.  QBO calls only work if the accounting scope was passed!").toString();
        }
        String accessToken = (String)session.getAttribute("access_token");

        try {
            //get DataService
            DataService service = helper.getDataService(realmId, accessToken);
            CompanyInfo companyInfo = recordH.getUserInfo(service);

            String userName = companyInfo.getLegalName();
            String companyName = companyInfo.getCompanyName();
            Date dateJoined = companyInfo.getCompanyStartDate();
            String companyPhone = companyInfo.getPrimaryPhone().getFreeFormNumber();
            String companyEmail = companyInfo.getEmail().getAddress();

            String user = "User Name: " + userName + "\n" +
                          "Company Name: "+ companyName + "\n" +
                          "Date Joined: " + dateJoined + "\n" +
                          "Phone: " + companyPhone + "\n" +
                          "Email: " + createResponse(companyEmail) + "\n";

            return user;
        } catch (Exception e) {
            return new JSONObject().put("response","Failed").toString();
        }
    }

    /**
     * Map object to json string
     * @param entity
     * @return
     */
    private String createResponse(Object entity) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString;
        try {
            jsonInString = mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            return createErrorResponse(e);
        } catch (Exception e) {
            return createErrorResponse(e);
        }
        return jsonInString;
    }

    private String createErrorResponse(Exception e) {
        logger.error("Exception while calling QBO ", e);
        return new JSONObject().put("response","Failed").toString();
    }

}

