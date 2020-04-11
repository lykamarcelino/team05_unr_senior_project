/*
* This will be used to have the user logout and revoke the client token
* */
package com.intuit.developer.tutorials.controller;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
public class logoutController {

    @RequestMapping("/logout")
    public void connectToQuickbooks() {

        oauthController.accessTokenHolder = null;
        oauthController.realmIdHolder = null;

    }

}

