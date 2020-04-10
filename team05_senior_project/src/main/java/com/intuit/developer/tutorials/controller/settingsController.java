package com.intuit.developer.tutorials.controller;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    String settingsDisplay() {
        String realmID = oauthController.realmIdHolder;
        String accessToke = oauthController.accessTokenHolder;

        return "Empty";
    }

}
