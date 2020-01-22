package com.intuit.developer.tutorials.controller;

import java.util.List;

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
import com.intuit.ipp.data.Error;
import com.intuit.ipp.data.Report;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.exception.InvalidTokenException;
import com.intuit.ipp.services.ReportName;
import com.intuit.ipp.services.ReportService;

@Controller
public class newGraphController {

	@Autowired
	OAuth2PlatformClientFactory factory;

	@Autowired
    public QBOServiceHelper helper;

	@ResponseBody
    	@RequestMapping("/getNewGraph")
    	public String getNewGraphDisplay(HttpSession session) {
			String realmId = (String)session.getAttribute("realmId");
			if (StringUtils.isEmpty(realmId)) {
				return new JSONObject().put("response", "No realm ID.  QBO calls only work if the accounting scope was passed!").toString();
			}
			String accessToken = (String)session.getAttribute("access_token");

			try {
				return "New graphs will be displayed here.[Work in Progress]";
			} catch (Exception e) {
				return new JSONObject().put("response","Failed").toString();
			}
		}
}
