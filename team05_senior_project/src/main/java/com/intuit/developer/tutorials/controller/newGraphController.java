package com.intuit.developer.tutorials.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.developer.tutorials.helper.dataHelper;

import java.lang.*;
import java.util.regex.*;
import java.util.List;
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
import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;

@Controller
public class newGraphController {

	@Autowired
	OAuth2PlatformClientFactory factory;

	@Autowired
    public QBOServiceHelper helper;

	@Autowired
	recordsHelper recordsH;

	@Autowired
	dataHelper dataH;

	private static final Logger logger = Logger.getLogger(newGraphController.class);

	@ResponseBody
	@RequestMapping("/getNewGraph")
	public String getNewGraphDisplay(HttpSession session) {
		String realmId = (String)session.getAttribute("realmId");
		if (StringUtils.isEmpty(realmId)) {
			return new JSONObject().put("response", "No realm ID.  QBO calls only work if the accounting scope was passed!").toString();
		}
		String accessToken = (String)session.getAttribute("access_token");

		try {
			//get service
			DataService service = helper.getDataService(realmId, accessToken);

			List<SalesReceipt> receipts = recordsH.getSalesReceipts(service);
			List<Invoice> invoices = recordsH.getInvoices(service);
			List<Estimate> estimates = recordsH.getEstimates(service);
			List<CreditMemo> creditMemos = recordsH.getCreditMemos(service);

			dataH.parseReceipts(receipts);
			dataH.parseInvoices(invoices);
			dataH.parseEstimates(estimates);
			dataH.parseCreditMemos(creditMemos);

			String data = cleanData(createResponse(receipts));
			return data;

		} catch (Exception e) {
			return new JSONObject().put("response","Failed").toString();
		}
	}

	private String cleanData(String data) {
		Pattern descriptionP = Pattern.compile("(\"description\":\"([^\"]*)\")|(\"qty\":\\d+(\\.\\d+)?)|(\"unitPrice\":\\d+(\\.\\d+)?)|(\"docNumber\":\"([^\"]*)\")|(\"customerRef\":\\{(.*?)\\})");
		Matcher	desMatch = descriptionP.matcher(data);

		String cleanedData = new String();
		while(desMatch.find())
		{
			cleanedData = cleanedData + " " + desMatch.group() + "\t";
		}
		return cleanedData;
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
