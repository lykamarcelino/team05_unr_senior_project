package com.intuit.developer.tutorials.helper;

import org.springframework.stereotype.Service;
import tech.tablesaw.api.*;
import tech.tablesaw.selection.Selection;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

@Service
public class tableOperations {

    /**
     * note unique column is assumed to be a date column and condense column is assumed to be a double column and table should only be two columns
     **/
    public Table processTable(Table table, String uniqueColumn, String condenseColumn) throws IOException {
        Table processedTable = Table.create(table.name());

        DateColumn dates = DateColumn.create(uniqueColumn);
        DoubleColumn totals = DoubleColumn.create(condenseColumn); //can be quantity, amount, price ect

        table = table.sortOn(uniqueColumn);
        LocalDate date;
        try {
            date = table.dateColumn(uniqueColumn).get(0);
        } catch (Exception e) {
            return Table.create("EMPTY");
        }

        Double totalVal = 0.0;
        for (Row row : table) {
            if (date.isEqual(row.getDate(uniqueColumn))) {
                totalVal = totalVal + row.getDouble(condenseColumn);
            } else {
                dates.append(date);
                totals.append(totalVal);

                date = row.getDate(uniqueColumn);
                totalVal = row.getDouble(condenseColumn);
            }
        }
        dates.append(date);
        totals.append(totalVal);

        processedTable.addColumns(dates, totals);
        return processedTable;
    }

    public Table removeTableOutliers(Table table, String columnName) throws Exception {
        if (table.column(columnName).type() != ColumnType.DOUBLE) {
            throw new Exception("Wrong Column Type!!!");
        }

        if (table.column(columnName).size() < 10) {
            return table; //table is too small and removing outliers is not needed
        }

        Table cleanTable;
        table = table.sortOn(columnName);

        //Remove Extreme Outliers to not affect mean and standard deviation
        List<Double> quartiles = getIQRs(table.doubleColumn(columnName));
        Double quartileDifference = quartiles.get(2) - quartiles.get(0);
        Double quartileCeiling = quartiles.get(2) + (quartileDifference * 1.5);
        Double quartileFloor = quartiles.get(0) - (quartileDifference * 1.5);

        Selection outlierTopFilter = table.doubleColumn(columnName).isGreaterThan(quartileCeiling);
        Selection outlierBottomFilter = table.doubleColumn(columnName).isLessThan(quartileFloor);
        Selection outlierFilter = outlierTopFilter.or(outlierBottomFilter);

        table = table.dropWhere(outlierFilter);

        //Remove general outliers
        Double mean = getColumnMean(table.doubleColumn(columnName));
        Double standardDeviation = getColumnDeviation(table.doubleColumn(columnName), mean);
        Double outlierCeiling = mean + (standardDeviation * 3);
        Double outlierFloor = mean - (standardDeviation * 3);

        outlierTopFilter = table.doubleColumn(columnName).isGreaterThan(outlierCeiling);
        outlierBottomFilter = table.doubleColumn(columnName).isLessThan(outlierFloor);
        outlierFilter = outlierTopFilter.or(outlierBottomFilter);

        cleanTable = table.dropWhere(outlierFilter);
        cleanTable = cleanTable.sortOn(0);

        return cleanTable;
    }

    /**
     * This will alwasy return seven tables one for each day of the Week
     **/
    public List<Table> fragmentTableNoise(Table table, String dateColumn) throws Exception {
        List<Table> reducedTables = new Vector<>();

        List<Selection> dayFilters = new Vector<>();
        dayFilters.add(table.dateColumn(dateColumn).isMonday()); //filter to get all the mondays of the data
        dayFilters.add(table.dateColumn(dateColumn).isTuesday()); //filter to get all the tuesdays of the data
        dayFilters.add(table.dateColumn(dateColumn).isWednesday()); //so on and so forth
        dayFilters.add(table.dateColumn(dateColumn).isThursday());
        dayFilters.add(table.dateColumn(dateColumn).isFriday());
        dayFilters.add(table.dateColumn(dateColumn).isSaturday());
        dayFilters.add(table.dateColumn(dateColumn).isSunday());

        //break up table into segments by day -- processing the data in weekly pieces allows the model
        //to learn the patterns with reduced noise as well as maintaining all the data.
        for (Selection dayFilter : dayFilters) {
            reducedTables.add(table.where(dayFilter));
        }

        return reducedTables;
    }

    public boolean highDensityTable(Table table, String dateCol) {
        if (table.dateColumn(dateCol).size() > 200) { //if the table has sales on more then 200 days a year than it is high density
            return true;
        } else {
            return false;
        }
    }

    public Double getColumnSum(DoubleColumn column) {
        Double columnSum = 0.0;
        int size = column.size();

        for (int i = 0; i < size; i++) {
            columnSum = columnSum + column.get(i);
        }

        return columnSum;
    }

    public Double getColumnMean(DoubleColumn column) {
        Double columnSum = getColumnSum(column);
        Double mean = columnSum / column.size();
        return mean;
    }

    public Double getColumnDeviation(DoubleColumn column, Double mean) {
        Double sum = 0.0;
        for (int i = 0; i < column.size(); i++) {
            sum = sum + ((column.get(i) - mean) * (column.get(i) - mean));
        }
        Double squareDiff = sum / column.size();
        Double standardDeviation = Math.sqrt(squareDiff);
        return standardDeviation;
    }

    public List<Double> getIQRs(DoubleColumn column) {
        int size = column.size();
        List<Double> IQRs = new Vector<>();
        int medianIndex;
        if (size % 2 == 1) {  //if size odd
            medianIndex = ((size - 1) / 2) + 1;
        } else {
            medianIndex = size / 2;
        }

        int leftIQR;
        if (medianIndex % 2 == 1) {
            leftIQR = ((medianIndex - 1) / 2) + 1;
        } else {
            leftIQR = medianIndex / 2;
        }

        int rightIQR;
        if ((size - medianIndex) % 2 == 1) {
            rightIQR = medianIndex + ((((size - medianIndex) - 1) / 2) + 1);
        } else {
            rightIQR = medianIndex + ((size - medianIndex) / 2);
        }

        IQRs.add(column.getDouble(leftIQR));
        IQRs.add(column.getDouble(medianIndex));
        IQRs.add(column.getDouble(rightIQR));
        return IQRs;
    }

}
