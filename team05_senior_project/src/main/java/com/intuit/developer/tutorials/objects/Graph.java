package com.intuit.developer.tutorials.objects;

import java.util.List;
import java.util.Vector;

public class Graph {
    private List<String> labels = new Vector<>();

    private String dataLabel = new String();
    private List<Double> data = new Vector<>();
    private List<String> colors = new Vector<>();

    private String legendPosition = new String();
    private String title = new String();
    private String xAxisLabel = new String();
    private String yAxisLabel = new String();
    private Boolean showLegend;

    public Graph() {
        dataLabel = "data Label";
        labels.add("Empty Label"); labels.add("Empty Label2");
        data.add(0.0); data.add(1.1);
        colors.add("rgba(255, 99, 132, 0.6)"); colors.add("rgba(255, 99, 132, 0.6)");
        legendPosition = "right";
        title = "Graph Title";
        xAxisLabel = "Date"; //should always be data in theory
        yAxisLabel = "None"; //may vary from USD to Quantity
        showLegend = true;
    }

    public Graph(List<String> labels, List<Double> data, List<String> colors, String legendPosition, String title, String xAxisTitle, String yAxisTitle, Boolean showLegend) throws Exception {
        if(checkData(labels, data) == false){
            throw new Exception("ERROR: invalid data entry");
        }

        this.labels = labels;
        this.data = data;
        this.colors = colors;
        this.legendPosition = legendPosition;
        this.title = title;
        this.xAxisLabel = xAxisTitle;
        this.yAxisLabel = yAxisTitle;
        this.showLegend = showLegend;
    }

    public void setGraphData(List<String> labels, List<Double> data, List<String> colors, String legendPosition, String title, String xAxisTitle, String yAxisTitle, Boolean showLegend, String dataLabel) throws Exception {
        if(checkData(labels, data) == false){
            throw new Exception("ERROR: invalid data entry");
        }

        this.labels = labels;
        this.data = data;
        this.colors = colors;
        this.legendPosition = legendPosition;
        this.title = title;
        this.xAxisLabel = xAxisTitle;
        this.yAxisLabel = yAxisTitle;
        this.showLegend = showLegend;
        this.dataLabel = dataLabel;
    }

    public void setLegendPosition(String legendPosition){
        this.legendPosition = legendPosition;
    }

    public String getLegendPosition() {
        return legendPosition;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public Boolean getShowLegend() {
        return showLegend;
    }

    public void setShowLegend(Boolean showLegend) {
        this.showLegend = showLegend;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisTitle) {
        this.xAxisLabel = xAxisTitle;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisTitle) {
        this.yAxisLabel = yAxisTitle;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public List<Double> getData() {
        return data;
    }

    public void setColors(List<String> backgroundColor) {
        this.colors = backgroundColor;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }

    public String getDataLabel() {
        return dataLabel;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void print(){
        System.out.println("Graph Title: " + title);
        System.out.println("X Axis Title: " + xAxisLabel);
        System.out.println("Y Axis Title: " + yAxisLabel);
        System.out.println("Show Legend: " + showLegend.toString());
        System.out.println("Legend Positions: " + legendPosition);
        System.out.println("Data:" );
        for(int i = 0; i < data.size(); i ++){
            System.out.print(labels.get(i)  + "\t");
            System.out.print(data.get(i) + "\n");
        }

        for(String color: colors){
            System.out.println(color);
        }
    }

    public void addColor(int r, int g, int b, int a){
        colors.add("rgba(" + r + ", " + g + ", " + b + ", " + a + ")");
    }

    private boolean checkData(List<String> labels, List<Double> data){
        if(labels.size() != data.size())
            return false;
        else
            return true;
    }

}