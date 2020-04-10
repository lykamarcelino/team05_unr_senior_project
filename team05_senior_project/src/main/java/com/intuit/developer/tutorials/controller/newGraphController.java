package com.intuit.developer.tutorials.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.developer.tutorials.helper.dataFrameHelper;
import com.intuit.developer.tutorials.helper.modelHelper;
import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.developer.tutorials.objects.Graph;
import com.intuit.ipp.services.DataService;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
(origins = "http://localhost:3000")

@Controller
@RequestMapping("/getNewGraph")
public class newGraphController {

	@Autowired
    OAuth2PlatformClientFactory factory;

	@Autowired
    public QBOServiceHelper helper;

	@Autowired
	recordsHelper recordsH;

	@Autowired
	dataFrameHelper dataFrameH;

	@Autowired
	modelHelper modelH;

	private static final Logger logger = Logger.getLogger(newGraphController.class);

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Graph getGraphInJson() throws Exception {
		String realmID = oauthController.realmIdHolder;
		String accessToke = oauthController.accessTokenHolder;

		try {
			DataService service = helper.getDataService(realmID, accessToke);

			Graph graph = new Graph();

			return graph;
		} catch (Exception e) {
			throw new Exception("Invalid Graph Object");
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
