/**
 * Name: logoutController.java
 * Description: Used to revoke the tokens and close the session
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 *
 **/

package com.intuit.developer.tutorials.controller;

import org.jfree.data.json.impl.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
        (origins = "http://localhost:3000")

@RequestMapping("/logout")
@Controller
public class logoutController {

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    JSONObject getLogout()
    {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("Response", "Logout endpoint has been hit");

        oauthController.accessTokenHolder = null;
        oauthController.realmIdHolder = null;

        return jsonObj;
    }

}

