package com.intuit.developer.tutorials.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.intuit.developer.tutorials.helper.dataHelper;
import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.ipp.data.*;
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
import com.intuit.ipp.services.DataService;

@Controller
public class InventoryController {

	@Autowired
	OAuth2PlatformClientFactory factory;

	@Autowired
	recordsHelper recordsH;

	@Autowired
	dataHelper dataH;

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
			//get DataService
			DataService service = helper.getDataService(realmId, accessToken);
			List<Item> items = recordsH.getItems(service);
			String itemData = dataH.parseItems(items);

			return itemData;
		} catch (Exception e) {
			return new JSONObject().put("response","Failed").toString();
		}
	}

	@ResponseBody
	@RequestMapping("/getCustomers")
	public String customerDisplay(HttpSession session) {
		String realmId = (String)session.getAttribute("realmId");
		if (StringUtils.isEmpty(realmId)) {
			return new JSONObject().put("response", "No realm ID.  QBO calls only work if the accounting scope was passed!").toString();
		}
		String accessToken = (String)session.getAttribute("access_token");

		try {
			//get DataService
			DataService service = helper.getDataService(realmId, accessToken);
			List<Customer> customers = recordsH.getCustomers(service);
			String customerData = dataH.parseCustomers(customers);

			return customerData;
		} catch (Exception e) {
			return new JSONObject().put("response","Failed").toString();
		}
	}

	@ResponseBody
	@RequestMapping("/getTransactions")
	public String transactionDisplay(HttpSession session) {
		String realmId = (String)session.getAttribute("realmId");
		if (StringUtils.isEmpty(realmId)) {
			return new JSONObject().put("response", "No realm ID.  QBO calls only work if the accounting scope was passed!").toString();
		}
		String accessToken = (String)session.getAttribute("access_token");

		try {
			//get DataService
			DataService service = helper.getDataService(realmId, accessToken);

			List<Deposit> deposits = recordsH.getDeposits(service);
			List<Estimate> estimates = recordsH.getEstimates(service);
			List<Invoice> invoices = recordsH.getInvoices(service);
			List<CreditMemo> memos = recordsH.getCreditMemos(service);
			List<SalesReceipt> receipts = recordsH.getSalesReceipts(service);
			List<Payment> payments = recordsH.getPayments(service);

			String transactions = dataH.parseTransactions(deposits, estimates, invoices, memos, receipts, payments);

			return transactions;
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
