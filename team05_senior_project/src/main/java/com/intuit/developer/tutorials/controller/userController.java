package com.intuit.developer.tutorials.controller;

import javax.servlet.http.HttpSession;

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
import com.intuit.ipp.data.Customer;
import com.intuit.ipp.data.EmailAddress;
import com.intuit.developer.tutorials.helper.userHelper;

@Controller
public class userController {

    @Autowired
    OAuth2PlatformClientFactory factory;

    @Autowired
    userHelper userH;

    @Autowired
    public QBOServiceHelper helper;

    @ResponseBody
    @RequestMapping("/getUser")
    public String userDisplay(HttpSession session) {
        String realmId = (String)session.getAttribute("realmId");
        if (StringUtils.isEmpty(realmId)) {
            return new JSONObject().put("response", "No realm ID.  QBO calls only work if the accounting scope was passed!").toString();
        }
        String accessToken = (String)session.getAttribute("access_token");

        try {
            String info = userH.getUserInfo(realmId, accessToken);
            info = formatData(info);
            return info;
        } catch (Exception e) {
            return new JSONObject().put("response","Failed").toString();
        }
    }

    private String formatData(String info) {
        String[] commaSplit = info.split(",");

        String formattedData = new String();

        for(int i = 0; i < commaSplit.length; i++){
            String[] colonSplit;
            if(commaSplit[i].contains("{")) {
                colonSplit = commaSplit[i].split(":");
            }
            else {
                colonSplit = commaSplit[i].split(":", 2);
            }

            for(int j = 0; j < colonSplit.length; j++) {
                colonSplit[j] = rmPunctuation(colonSplit[j]);
            }

            if(colonSplit.length == 3){
                formattedData = formattedData + colonSplit[0] + "\n";
                formattedData = formattedData + colonSplit[1] + " " + colonSplit[2] + "\n" ;
            }
            else{
                formattedData = formattedData + colonSplit[0] + " " + colonSplit[1] + "\n";
            }
        }
        return formattedData;
    }

    String rmPunctuation (String s)
    {
        if(!s.equals("[]")) {
            s = s.replaceAll("[\"{}\\[\\]]", "");
        }
        return s;
    }
}

