package com.intuit.developer.tutorials.controller.ReportControllers;

import com.intuit.developer.tutorials.controller.oauthController;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
@RequestMapping("/getMakeReport")
public class makeReportController {

    @Autowired
    public QBOServiceHelper helper;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String makeReport(@RequestHeader("graphName")String graphName,  @RequestHeader("graphType") String graphType, @RequestHeader("predictionType") String predictionType,
                      @RequestHeader("id") String id, @RequestHeader("dates") List<String> dates,
                      @RequestHeader("dataLabel") String dataLabel, @RequestHeader("data") List<String> data,
                      @RequestHeader("colors") String colors, @RequestHeader("xAxisLabel") String xAxis,
                      @RequestHeader("yAxisLabel") String yAxis, @RequestHeader("transactionData") Boolean transactionData,
                      @RequestHeader("customerInfo") Boolean customerInfo, @RequestHeader("inventoryInfo") Boolean inventoryInf,
                      @RequestHeader("graph") Boolean graph, @RequestHeader("gridlines") Boolean gridlines){
        String realmId = oauthController.realmIdHolder;
        String accessToken = oauthController.accessTokenHolder;

        try{
            System.out.println("graphName:" + graphName); //the graph name specified by the customer
            System.out.println("predictionType: " + predictionType); //will tell you waht kind of prediction it is 1)Sales 2)items 3)customers
            System.out.println("item or Customer id: " + id);   //id will be undefined if it comes from general sales if it is from
            //customers or items then it will be specific to one customer or item the customer or item label may also be fixed
            //what i mean by that is that the string does not have any special characters like ' or " or ? it they caused bugs so i removed them
            System.out.println("graphType: " + graphType); //pie, line, bar, or scatterplot

            //the date labels match to the data labels they are the plot points
            System.out.println("dates: ");
            for(String s: dates){
                System.out.println(s);
            }

            System.out.println("datalabel: " + dataLabel);
            System.out.println("data: ");
            for(String s: data){
                System.out.println(s);
            }

            //so these are the point colors but they cannot be directly converted to a string like the others because of it commas but im pretty sure you
            //dont need this anyway :)
            System.out.println("colors: ");

            //you should know
            System.out.println("xAxis: " + xAxis);
            System.out.println("yAxis: " + yAxis);

            //the flags you wanted:
            System.out.println("transactionData: " + transactionData);
            System.out.println("Customer info: " + customerInfo);
            System.out.println("inventory info: " + inventoryInf);
            System.out.println("graph: " + graph);
            System.out.println("gridlines: " + gridlines);


            return "{'response': \"Report Made\"}";
        }
        catch(Exception e){
            return "{'response': \"Failed to Make Report\"}";
        }
    }
}
