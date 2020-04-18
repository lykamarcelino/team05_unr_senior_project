package com.intuit.developer.tutorials.objects;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class Report {
    public static String folder = "CWAC Reports";
    public static String  home = System.getProperty("user.home");
    public static final String directory = home + File.separator + "Downloads" + File.separator + folder;

    int number = 0;
    String name = new String();
    String date = new String();
    String graph = new String();

    public Report() {
        number = 1;
        name = "My Report";
        date = "04/20/2020";
        graph = "Graph";
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getGraph() {
        return graph;
    }

    public void setDesc(int number, String name, String date, String graph) {
        this.number = number;
        this.name = name;
        this.date = date;
        this.graph = graph;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static List<String> getNames(){
        String[] fileNames;
        List<String> reportNames = new Vector<>();
        File f = new File(directory);

        fileNames = f.list();

        // For each pathname in the pathnames array
        for (String fileName : fileNames) {
            reportNames.add(fileName);
        }
        return reportNames;
    }

    public static List<String> getDates() {
        List<String> dates = new Vector<>();
        List<String> names = getNames();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        for(String n : names) {
            File file = new File(directory + File.separator + n);
            String date = sdf.format(file.lastModified());
            dates.add(date);
        }
        return dates;
    }

    public static List<String> getNumbers() {
        int num = new File(directory).list().length;
        List<String> numbers = new Vector<>();
        for(int i = 1; i <= num; i++) {
            numbers.add(String.valueOf(i));
        }
        return  numbers;
    }
}
