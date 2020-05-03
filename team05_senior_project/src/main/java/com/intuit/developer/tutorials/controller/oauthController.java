/**
 * Name: oauthController.java
 * Controller to handle the auth code, state, and realmId from the React app
 * Creates the /oauth endpoint
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 * */

package com.intuit.developer.tutorials.controller;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.data.BearerTokenResponse;
import com.intuit.oauth2.exception.OAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
public class oauthController {

    public static String accessTokenHolder;
    public static String realmIdHolder;

    @Autowired
    OAuth2PlatformClientFactory factory;

    @ResponseBody
    @RequestMapping("/oauth")
    public String callBackFromOAuth(@RequestHeader("code") String authCode, @RequestHeader("state") String state, @RequestHeader("realmId") String realmId) {
        try {
            OAuth2PlatformClient client = factory.getOAuth2PlatformClient();
            String redirectUri = factory.getPropertyValue("OAuth2AppRedirectUri");

            BearerTokenResponse bearerTokenResponse = client.retrieveBearerTokens(authCode, redirectUri);

            realmIdHolder = realmId;
            accessTokenHolder = bearerTokenResponse.getAccessToken();

            String temp = realmIdHolder + accessTokenHolder;

            return temp;

        } catch (OAuthException e) {
            System.out.println("Error creating response");
        }
        return null;

    }

}
