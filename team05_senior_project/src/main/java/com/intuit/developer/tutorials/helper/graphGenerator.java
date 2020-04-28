package com.intuit.developer.tutorials.helper;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class graphGenerator {
    public static String folder = "CWAC Reports";
    String  home = System.getProperty("user.home");
    final String directory = home + File.separator + "Downloads" + File.separator + folder;

    public void generateGraph(String graphType, String title, String xAxis, String yAxis, List<String> dates, List<String> data, String label) throws IOException {
        File mkdir = new File(directory + File.separator);
        if(!mkdir.exists()){
            mkdir.mkdir();
        }

        final String DEST = directory + File.separator +  title + ".png";

        if(graphType.equals("Line")) {
            lineGraph(title, xAxis, yAxis, dates, data, label, DEST);
        }else if(graphType.equals("Bar")) {
            barGraph(title, xAxis, yAxis, dates, data, label, DEST);
        }else if(graphType.equals("Scatter")) {
            scatterGraph(title, xAxis, yAxis, dates, data, label, DEST);
        }else if(graphType.equals("Pie")) {
            pieGraph(title, xAxis, yAxis, dates, data, label, DEST);
        }else {
            System.out.println("ERROR: PDF GRAPH ERROR.");
        }
    }

    private void lineGraph(String title, String xAxis, String yAxis, List<String> dates, List<String> data, String label, String file) throws IOException {
        CategoryPlot plot = new CategoryPlot();
        CategoryItemRenderer chartRenderer = new LineAndShapeRenderer();

        plot.setDataset(0, createDataset(dates, data, label));
        plot.setRenderer(0, chartRenderer);

        plot.setDomainAxis(new CategoryAxis(xAxis));
        plot.setRangeAxis(new NumberAxis(yAxis));

        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle(title);

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);

        ChartUtilities.saveChartAsPNG(new File(file), chart, 1000,500 );
    }

    private void barGraph(String title, String xAxis, String yAxis, List<String> dates, List<String> data, String label, String file) throws IOException {
        CategoryPlot plot = new CategoryPlot();
        CategoryItemRenderer chartRenderer = new BarRenderer();

        plot.setDataset(0, createDataset(dates, data, label));
        plot.setRenderer(0, chartRenderer);

        plot.setDomainAxis(new CategoryAxis(xAxis));
        plot.setRangeAxis(new NumberAxis(yAxis));

        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle(title);

        ChartUtilities.saveChartAsPNG(new File(file), chart, 1000,500 );
    }

    private void scatterGraph(String title, String xAxis, String yAxis, List<String> dates, List<String> data, String label, String file) throws IOException {
        CategoryPlot plot = new CategoryPlot();
        CategoryItemRenderer chartRenderer = new LineAndShapeRenderer(false, true);

        plot.setDataset(0, createDataset(dates, data, label));
        plot.setRenderer(0, chartRenderer);

        plot.setDomainAxis(new CategoryAxis(xAxis));
        plot.setRangeAxis(new NumberAxis(yAxis));

        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle(title);

        ChartUtilities.saveChartAsPNG(new File(file), chart, 1000,500 );
    }

    private void pieGraph(String title, String xAxis, String yAxis, List<String> dates, List<String> data, String label, String file) throws IOException {
        PieDataset dataset = pieCreateDataset(dates, data);
        JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);

        ChartUtilities.saveChartAsPNG(new File(file), chart, 1000,500 );
    }

    private PieDataset pieCreateDataset(List<String> dates, List<String> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for(int i = 0; i < data.size(); i++) {
            dataset.setValue(dates.get(i), Double.parseDouble(data.get(i)));
        }

        return dataset;
    }

    private void setContentPane(ChartPanel panel) {
    }

    private DefaultCategoryDataset createDataset(List<String> dates, List<String> data, String label) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series1 = label;

        for(int i = 0; i < dates.size(); i++) {
            dataset.addValue(Double.parseDouble(data.get(i)), label, dates.get(i));
        }
        return dataset;
    }
}