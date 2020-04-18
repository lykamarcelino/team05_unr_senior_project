package com.intuit.developer.tutorials.controller;

import com.intuit.developer.tutorials.client.OAuth2PlatformClientFactory;
import com.intuit.developer.tutorials.helper.PDFconverter;
import com.intuit.developer.tutorials.helper.QBOServiceHelper;
import com.intuit.developer.tutorials.helper.recordsHelper;
import com.intuit.developer.tutorials.objects.Report;
import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.services.DataService;
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
public class GetReportListController {
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

        PDFconverter rep;
        try {
            DataService service = helper.getDataService(realmID, accessToken);
            CompanyInfo companyInfo = recordH.getUserInfo(service);

            rep = new PDFconverter();
            rep.generatePDF(service, companyInfo);

            List<Report> reportList = new Vector<>();
            Report report = new Report();
            reportList.add(report);
            reportList.add(report);
            reportList.add(report);

            return reportList;

        } catch (Exception e) {
            return null;
        }
    }
}
