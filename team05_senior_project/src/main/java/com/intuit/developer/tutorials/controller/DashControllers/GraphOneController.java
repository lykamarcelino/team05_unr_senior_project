/**
* Name: GraphOneController.java
* Description: Controller that fetches the GraphOne file and sends a json response with
* the most recent graph created data.
* Date: 04/29/2020
* Author: Liliana Pacheco
* */
package com.intuit.developer.tutorials.controller.DashControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

@CrossOrigin
        (origins = "http://localhost:3000")

@Controller
@RequestMapping("/getGraphOne")
public class GraphOneController {

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String getDashGraphOne() throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        File first = new File(path + "/graphOne.txt");

        if(first.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(first));
            String dashGraph = reader.readLine();
            reader.close();
            System.out.println(dashGraph);
            return dashGraph;
        }
        else{
            return "";
        }
    }
}
