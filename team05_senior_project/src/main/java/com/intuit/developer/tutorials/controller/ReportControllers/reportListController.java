/**
 * Name: reportListController.java
 * Description:  Will retrieve the user saved reports and return the list to the UI
 * Date: 04/29/2020
 * Author: Chantelle Marquez Suarez
 * */
package com.intuit.developer.tutorials.controller.ReportControllers;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.controller.oauthController;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.developer.tutorials.objects.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Vector;


@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
@RequestMapping("/getListofReports")
public class reportListController {
    @Autowired
    OAuth2PlatformClientFactory factory;
    @Autowired
    public QBOServiceHelper helper;
    @Autowired
    recordsHelper recordH;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Report> reportsDisplay() {
        String realmID = oauthController.realmIdHolder;
        String accessToken = oauthController.accessTokenHolder;


        try {
            Report report = new Report();
            List<String> reportNames = report.getNames();
            List<String> reportDates = report.getDates();
            List<String> reportNumber = report.getNumbers();
            List<String> reportGraph = report.getGraphs();

            List<Report> reportList = new Vector<>();

            for(int i = 0; i < reportNames.size(); i++) {
                Report reportInfo = new Report();
                reportInfo.setDesc(reportNumber.get(i),reportNames.get(i), reportDates.get(i), reportGraph.get(i));
                reportList.add(reportInfo);
            }
            return reportList;

        } catch (Exception e) {
            return null;
        }
    }
}