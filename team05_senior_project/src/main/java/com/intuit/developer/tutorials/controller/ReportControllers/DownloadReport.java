/*work in progress*/
package com.intuit.developer.tutorials.controller.ReportControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
@RequestMapping("/openReports")
public class DownloadReport {

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String openExistingReport(@RequestHeader("reportName") String reportName){
        //this should down load the specified report



        return "done";
    }

}
