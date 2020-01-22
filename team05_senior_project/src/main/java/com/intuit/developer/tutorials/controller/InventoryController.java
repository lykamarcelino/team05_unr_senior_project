package com.intuit.developer.tutorials.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.intuit.developer.tutorials.helper.InventoryHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
import com.intuit.ipp.core.IEntity;
import com.intuit.ipp.data.Account;
import com.intuit.ipp.data.AccountSubTypeEnum;
import com.intuit.ipp.data.AccountTypeEnum;
import com.intuit.ipp.data.Customer;
import com.intuit.ipp.data.EmailAddress;
import com.intuit.ipp.data.Error;
import com.intuit.ipp.data.IntuitEntity;
import com.intuit.ipp.data.Invoice;
import com.intuit.ipp.data.Item;
import com.intuit.ipp.data.ItemTypeEnum;
import com.intuit.ipp.data.Line;
import com.intuit.ipp.data.LineDetailTypeEnum;
import com.intuit.ipp.data.ReferenceType;
import com.intuit.ipp.data.SalesItemLineDetail;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.exception.InvalidTokenException;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.services.QueryResult;

@Controller
public class InventoryController {

	@Autowired
	OAuth2PlatformClientFactory factory;

	@Autowired
	InventoryHelper inventoryH;

	@Autowired
	public QBOServiceHelper helper;

	private static final Logger logger = Logger.getLogger(InventoryController.class);

	@ResponseBody
	@RequestMapping("/getInventory")
	public String inventoryDisplay(HttpSession session) {
		String realmId = (String)session.getAttribute("realmId");
		if (StringUtils.isEmpty(realmId)) {
			return new JSONObject().put("response", "No realm ID.  QBO calls only work if the accounting scope was passed!").toString();
		}
		String accessToken = (String)session.getAttribute("access_token");

		try {
			List<Item> items = inventoryH.getAllInventory(realmId, accessToken);
			String allInventory = formatData(createResponse(items));
			return allInventory;
		} catch (Exception e) {
			return new JSONObject().put("response","Failed").toString();
		}
	}

	//make format data public for later
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
