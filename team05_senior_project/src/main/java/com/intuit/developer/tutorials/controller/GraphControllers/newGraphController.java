/**
 * Name: newGraphController
 * Author: Liliana Pacheco
 * Description: controller that creates predictions for a user request and returns appropriate graph object
 */

package com.intuit.developer.tutorials.controller.GraphControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.controller.oauthController;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

@CrossOrigin
(origins = "http://localhost:3000")

@Controller
@RequestMapping("/getGeneralGraph")
public class newGraphController {
	String[] staticColor = { "rgba(80, 100, 200, 0.6)",
							"rgba(60, 170, 185, 0.6)",
							"rgba(110, 100, 110, 0.6)",
							"rgba(130, 195, 165, 0.6)",
							"rgba(30, 60, 110, 0.6)",
							"rgba(175, 180, 110, 0.6)",
							"rgba(30, 135, 190, 0.6)",
							"rgba(35, 160, 100, 0.6)",
							"rgba(60, 170, 185, 0.6)",
							"rgba(110, 100, 110, 0.6)",
							"rgba(30, 60, 110, 0.6)",
							"rgba(175, 180, 110, 0.6)",
							"rgba(30, 135, 190, 0.6)",
							"rgba(100, 90, 50, 0.6)",
							"rgba(35, 160, 100, 0.6)",};
	int numColor = 15;

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


	/**
	 * @Name getGraphInJson
	 * @param graphName - user specified graph title
	 * @param numPred - number of sales forcast predictions wanted
	 * @param predFrequency -  how often the predictions are wanted {daily, weekly, monthly}
	 * @param from - what form the request came from (General_Sales, Item_Quantity_Sales, Customer_Purchases)
	 * @param id -  the name of an item or customer may come as none when its from General_Sales
	 * @return returns a graph object with data to render a Chart.js graph with react
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Graph getGraphInJson(@RequestHeader("graphName") String graphName, @RequestHeader("numPred") String numPred,
						 @RequestHeader("predFrequency") String predFrequency, @RequestHeader("from") String from,
						 @RequestHeader("id") String id) throws Exception {
		String realmID = oauthController.realmIdHolder;
		String accessToke = oauthController.accessTokenHolder;

		id = fixLabel(id);

		try {
			DataService service = helper.getDataService(realmID, accessToke);
			if (!dataFrameH.CSVExists("All_Customer_Data")) {
				dataFrameH.makeDataCSV(service);
			}

			int totalPred = getPredAmount(predFrequency, numPred);
			List<Double> predictions = new Vector<>();
			List<String> dates = new Vector<>();
			List<String> color = new Vector<>();
			LocalDate lastDate;
			String dataLabel = new String();
			String yAxis = new String();

			if(from.equals("General_Sales")){
				//get data needed
				dataFrameH.makeGeneralSalesARFF(service);
				lastDate = dataFrameH.getLastDateRecord("General_Sales");

				/* make predictions and matching values */
				predictions = modelH.getGeneralSalesPredictions(totalPred);
				predictions = fixPredictions(predictions, totalPred);

				dates = modelH.getMatchingDates( lastDate,predictions.size());

				dataLabel = "Total" + predFrequency + "Sales";
				yAxis = "Sales in USD";
			}
			else if(from.equals("Item_Quantity_Sales")){
				dataFrameH.makeItemSalesARFF(service);
				lastDate = dataFrameH.getLastDateRecord(id);

				predictions = modelH.getItemQuantityPredictions(totalPred, id);
				predictions = fixPredictions(predictions, totalPred);
				dates = modelH.getMatchingDates(lastDate, predictions.size());

				dataLabel =  id + " Unit Sales";
				yAxis = "Quantity Sales by Unit";
			}
			else if(from.equals("Customer_Purchases")){
				dataFrameH.makeCustomerSalesARFF(service);
				lastDate = dataFrameH.getLastDateRecord(id);

				predictions = modelH.getCustomerSalePredictions(totalPred, id);
				predictions = fixPredictions(predictions, totalPred);
				dates = modelH.getMatchingDates(lastDate, predictions.size());

				dataLabel = "Purchases by " + id;
				yAxis = "Total Purchases in USD";
			}
			/* condense according to frequency */
			predictions = condensePredictions(predictions, predFrequency);
			dates = condenseDates(dates, predFrequency);
			/* get matching colors for plots*/
			color = getColorList(predictions.size());

			Graph graph = new Graph();
			graph.setGraphData(dates, predictions, color, "right", graphName, "Date", yAxis, true, dataLabel);

			return graph;
		} catch (Exception e) {
			throw new Exception("Invalid Graph Object");
		}
	}

	/**
	 * @Name fixPredictons
	 * @param predictions - the list of predictions made by the learning model
	 * @param totalPred - number of predictions actually wanted by customer
	 * @return will remove extra predictions from the list if there exists extra predictions. when a data
	 * set is too large the model will fragment the tables and give predictions on a by day basis. if model is
	 * asked to produce the predictions it will guarantee that all predictions neccessary are existent in the
	 * in the list even with low day availability. this may cause the model to produce 0-7 extra predictions
	 */
	private List<Double> fixPredictions(List<Double> predictions, int totalPred) {
		List<Double> fixedPred = new Vector<>();
		for(int i = predictions.size() - 1; predictions.size() > totalPred; i --){
			predictions.remove(i);
		}

		for(Double d: predictions){
			BigDecimal bd = new BigDecimal(d).setScale(2, RoundingMode.HALF_UP);
			fixedPred.add(bd.doubleValue());
		}
		return fixedPred;
	}

	/**
	 * @Name fixLabel
	 * @param label - the name of an item or customer
	 * @return returns the name of an item or customer with special characters removed
	 */
	private String fixLabel(String label) {
		label = label.replace(" ", "_");
		label = label.replace("&", "and");
		label = label.replaceAll("[^a-zA-Z0-9_]", "");

		return label;
	}

	/**
	 * @Name condenseDates
	 * @param dates - list of dates matching to the predictions (plot points)
	 * @param predFrequency - the frequency of the preditictions (daily, weekly, monthly)
	 * @return if frequency is weekly or monthly then the dates are selected accordingly to match for the
	 * desired predictions. list of dates is matched to daily frequency by default
	 */
	private List<String> condenseDates(List<String> dates, String predFrequency) {
		int frequency = matchFrequencyVal(predFrequency);
		List<String> newList = new Vector<>();

		for(int i = 0; i < dates.size(); i = i + frequency){
			newList.add(dates.get(i));
		}
		return newList;
	}

	/**
	 * @Name condensePredictions
	 * @param predictions -- list of machine learning predictions
	 * @param predFrequency -- the frequency of the predictions to be condensed to
	 * @return if frequency is weekly or monthly then the predictions are added accordingly to match for the
	 * desired. predictions are by default in a daily frequency
	 */
	private List<Double> condensePredictions(List<Double> predictions, String predFrequency) {
		int frequency = matchFrequencyVal(predFrequency);
		List<Double> newList = new Vector<>();

		if(frequency == 1){
			return predictions;
		}

		Double valTotal = 0.0;
		for(int i = 0; i < predictions.size(); i = i + frequency)
		{
			for(int j = 0; j < frequency; j++){
				valTotal = valTotal + predictions.get(i + j);
			}
			newList.add(valTotal);
			valTotal = 0.0;
		}

		return newList;
	}

	/**
	 * @Name matchFrequency Val
	 * @param predFrequency -- the frequency of the predictions as selected in the UI
	 * @return integer matching to the following strings Daily: 1, Weekly: 7, Monthly: 30
	 */
	private int matchFrequencyVal(String predFrequency) {
		int frequency = 0;
		if(predFrequency.equals("Daily")){
			frequency = 1;
		}
		else if(predFrequency.equals("Weekly")){
			frequency = 7;
		}
		else if(predFrequency.equals("Monthly")){
			frequency = 30;
		}
		return frequency;
	}

	/**
	 * @Name getPredAmount
	 * @param predFrequency - the frequency of the predictions as selected in the UI
	 * @param numPred - the number of predictions wanted by the user as selected in the UI
	 * @return this will return the Total daily predictions neccesary to git the user their
	 * predictions for instance 4 monthly predictions =  4 * 30 daily predictions
	 */
	private int getPredAmount(String predFrequency, String numPred) {
		int frequency = matchFrequencyVal(predFrequency);
		int pred = Integer.parseInt(numPred);
		return pred * frequency;
	}

	/**
	 * @Name getColorList
	 * @param size - number of colors wanted
	 * @return a list of strings of RGBA colors in a format usable by Chart.js
	 */
	private List<String> getColorList(int size) {
		List<String> colors = new Vector<>();
		for(int i = 0, j = 0; i < size; i++, j++){
			if(j >= 15){
				j = 0;
			}
			colors.add(staticColor[j]);
		}
		return colors;
	}
}
