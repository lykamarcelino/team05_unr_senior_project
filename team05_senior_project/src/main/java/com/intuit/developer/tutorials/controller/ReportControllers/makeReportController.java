/**
 * Name: makeReportController.java
 * Description:  will receive user form input and graph data to create appropriate report and
 * Graph PNG
 * Date: 04/29/2020
 * Author: Chantelle Marquez Suarez
 * */
package com.intuit.developer.tutorials.controller.ReportControllers;

import com.intuit.developer.tutorials.controller.oauthController;
import com.intuit.developer.tutorials.helper.PDFconverter;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.developer.tutorials.helper.graphGenerator;
import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
@RequestMapping("/getMakeReport")
public class makeReportController {
    //String title;

    @Autowired
    public QBOServiceHelper helper;

    @Autowired
    recordsHelper recordH;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String makeReport(@RequestHeader("graphName")String graphName,  @RequestHeader("graphType") String graphType, @RequestHeader("predictionType") String predictionType,
                      @RequestHeader("id") String id, @RequestHeader("dates") List<String> dates,
                      @RequestHeader("dataLabel") String dataLabel, @RequestHeader("data") List<String> data,
                      @RequestHeader("colors") String colors, @RequestHeader("xAxisLabel") String xAxis,
                      @RequestHeader("yAxisLabel") String yAxis, @RequestHeader("transactionData") Boolean transactionData,
                      @RequestHeader("customerInfo") Boolean customerInfo, @RequestHeader("inventoryInfo") Boolean inventoryInf,
                      @RequestHeader("graph") Boolean graph, @RequestHeader("gridlines") Boolean gridlines){

        String realmID = oauthController.realmIdHolder;
        String accessToken = oauthController.accessTokenHolder;


        try{
            DataService service = helper.getDataService(realmID, accessToken);
            CompanyInfo companyInfo = recordH.getUserInfo(service);

            PDFconverter rep = new PDFconverter();
            graphGenerator newGraph = new graphGenerator();

            newGraph.generateGraph(graphType, graphName, xAxis, yAxis, dates, data, dataLabel);
            rep.generatePDF(service, companyInfo, graphName, customerInfo, gridlines, transactionData, inventoryInf, graph);

            return "{'response': \"Report Made\"}";
        }
        catch(Exception e){
            return "{'response': \"Failed to Make Report\"}";
        }
    }
}