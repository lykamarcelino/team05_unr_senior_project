/**
 * Name: homeController.java
 * Description: Setup to link Home page with the backend
 * Date: 05/02/2020
 * Author: Lyka Marcelino
 * */
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
@RequestMapping("/getHome")
public class homeController {
	
	@Autowired
    OAuth2PlatformClientFactory factory;
	
	@Autowired
    public QBOServiceHelper helper;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	String homeDisplay() {

		return  "Empty";
	}
    
}

