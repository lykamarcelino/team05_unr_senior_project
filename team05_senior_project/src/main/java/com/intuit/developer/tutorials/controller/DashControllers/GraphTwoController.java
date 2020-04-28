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
@RequestMapping("/getGraphTwo")
public class GraphTwoController {

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String getDashGraphTwo() throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        File second = new File(path + "/graphTwo.txt");

        if(second.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(second));
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
