package com.intuit.developer.tutorials.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instances;
import weka.filters.supervised.attribute.TSLagMaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

@Service
public class modelHelper {
    @Autowired
    recordsHelper recordH;

    @Autowired
    dataFrameHelper dfHelper;

    String path = Paths.get("").toAbsolutePath().toString();
    String mainFolderName = "Data_Frames";

    String generalSalesFragmentedFolderPath = path + "/" + mainFolderName + "/General_Sales_By_WeekDay";
    String generalSalesFilePath = path + "/" + mainFolderName + "/General_Sales.arff";

    String itemQuantitySalesFolderPath = path + "/" + mainFolderName + "/All_Item_Quantity_Sales";
    String customerSaleFolderPath = path + "/" + mainFolderName + "/All_Customer_Sales";

    String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Sunday"};

    /**Note the making DATACSV should be made only once and fetched multiple times from folder as it is a time expensive task **/
    public List<Double> getGeneralSalesPredictions(int numPredictions) throws Exception {
        File generalSalesFile = new File(generalSalesFilePath);
        File generalSalesFragmentedFolder = new File(generalSalesFragmentedFolderPath);

        List<Double> finalResults = new Vector<>();

        if(generalSalesFile.exists()){
            finalResults = getPredictionList(numPredictions, generalSalesFilePath, "Total_Amount", "DAILY");
        }
        else if (generalSalesFragmentedFolder.exists()) {
            List<List<Double>> predictions = new Vector<>();

            numPredictions = (numPredictions/7) + 1;
            for (String day : daysOfWeek) {
                predictions.add(getPredictionList(numPredictions, generalSalesFragmentedFolderPath + "/" + "General_Sales_Frag_" + day + ".arff", "Total_Amount", "WEEKLY"));
            }

            for (int i = 0; i < numPredictions; i++) {
                for (int j = 0; j < 7; j++) {
                    try {
                        finalResults.add(predictions.get(j).get(i));
                    } catch (Exception e) {
                        finalResults.add(0.0);
                        //missing day just ignore when displaying
                        // somedays may not produce any data
                    }
                }
            }
        }
        else {
            throw new Exception("General Sales File or Folder do not exists! No predictions can be made no data");
        }

        return finalResults;
    }

    public List<Double> getItemQuantityPredictions(int numPredictions, String itemName) throws Exception {
        File specifiedItemSaleFile = new File(itemQuantitySalesFolderPath + "/" + itemName + ".arff");
        File specifiedItemSaleFolder = new File( itemQuantitySalesFolderPath + "/" + itemName);

        List<Double> finalPredictions = new Vector<>();
        if(specifiedItemSaleFile.exists()){
            finalPredictions = getPredictionList(numPredictions, specifiedItemSaleFile.getPath(), "Quantity", "DAILY");
        }
        else if ( specifiedItemSaleFolder.exists()){
            numPredictions = (numPredictions/7) + 1;
            List<List<Double>> predictions = new Vector<>();

            for (String day : daysOfWeek) {
                predictions.add(getPredictionList(numPredictions,   specifiedItemSaleFolder + "/" + itemName +"_frag_"+ day + ".arff", "Quantity", "WEEKLY"));
            }

            for (int i = 0; i < numPredictions; i++) {
                for (int j = 0; j < 7; j++) {
                    try {
                        finalPredictions.add(predictions.get(j).get(i));
                    } catch (Exception e) {
                        finalPredictions.add(0.0);
                        //missing day just ignore when displaying
                        //some days may not produce any data
                    }
                }
            }
        }
        else {
            throw new Exception("Item File File or Folder do not exists! No predictions can be made no data");
        }

        return finalPredictions;
    }

    public List<Double> getCustomerSalePredictions(int numPredictions, String customerName) throws Exception {
        File specifiedCustomerSaleFile = new File(customerSaleFolderPath + "/" + customerName + ".arff");



        File specifiedCustomerSaleFolder = new File( customerSaleFolderPath + "/" + customerName);

        List<Double> finalPredictions = new Vector<>();
        if(specifiedCustomerSaleFile.exists()){
            finalPredictions = getPredictionList(numPredictions, specifiedCustomerSaleFile.getPath(), "Total_Amount", "DAILY");
        }
        else if ( specifiedCustomerSaleFolder.exists()){
            List<List<Double>> predictions = new Vector<>();

            numPredictions = (numPredictions/7) + 1;
            for (String day : daysOfWeek) {
                predictions.add(getPredictionList(numPredictions,   specifiedCustomerSaleFolder + "/" + customerName +"_frag_"+ day + ".arff", "Total_Amount", "WEEKLY"));
            }

            for (int i = 0; i < numPredictions; i++) {
                for (int j = 0; j < 7; j++) {
                    try {
                        finalPredictions.add(predictions.get(j).get(i));
                    } catch (Exception e) {
                        finalPredictions.add(0.0);
                        //missing day just ignore when displaying
                        // somedays may not produce any data
                    }
                }
            }
        }
        else {
            throw new Exception("Item File File or Folder do not exists! No predictions can be made no data");
        }

        return finalPredictions;
    }

    public List<Double> getPredictionList(int numPredictions, String FilePath, String columnName, String periodicity) throws Exception {
       System.out.println(FilePath);
        List<List<NumericPrediction>> forecast = null;
        List<Double> predictionVal = new Vector<>();

        try {
            Instances data = new Instances(new BufferedReader(new FileReader(FilePath)));

            // new forecaster
            WekaForecaster forecaster = new WekaForecaster();

            // set the targets we want to forecast
            forecaster.setFieldsToForecast(columnName);

            forecaster.setBaseForecaster(new GaussianProcesses());

            forecaster.getTSLagMaker().setTimeStampField("Date"); // date time stamp
            forecaster.getTSLagMaker().setMinLag(1);
            forecaster.getTSLagMaker().setMaxLag(24);
            forecaster.getTSLagMaker().setPeriodicity(TSLagMaker.Periodicity.valueOf(periodicity));

            // add a month and quarter of the year indicator field
            forecaster.getTSLagMaker().setAddDayOfMonth(true);
            forecaster.getTSLagMaker().setAddMonthOfYear(true);
            forecaster.getTSLagMaker().setAddQuarterOfYear(true);

            // build the model
            forecaster.buildForecaster(data, System.out);

            // prime the forecaster with enough recent historical data
            // to cover up to the maximum lag
            forecaster.primeForecaster(data);


            // forecast
            forecast = forecaster.forecast(numPredictions, System.out);

            // output the predictions. Outer list is over the steps; inner list is over
            // the targets

            System.out.println("Predictions: \n\n\n");
            for (List<NumericPrediction> pred : forecast) {
                for (NumericPrediction npred : pred) {
                    if (!npred.equals(null)) {
                        predictionVal.add(npred.predicted());
                        System.out.print("" + npred.predicted() + " " + npred.predictionIntervals().toString());
                    } else {
                        System.out.print("" + "none" + " ");
                    }
                }
                System.out.println();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return predictionVal;
    }

    public List<String> getMatchingDates(LocalDate lastDateAvailable, int numPred){
        List<String> dates = new Vector<>();
        for(int i = 0; i < numPred; i ++){
            dates.add(lastDateAvailable.plusDays(i + 1).toString());
        }
        return  dates;
    }

}
