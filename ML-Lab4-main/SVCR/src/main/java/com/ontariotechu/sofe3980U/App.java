package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

public class App 
{
    public static void main(String[] args)
    {
        evaluateModel("model_1.csv");
        evaluateModel("model_2.csv");
        evaluateModel("model_3.csv");
    }

    public static void evaluateModel(String filePath)
    {
        double mse = 0;
        double mae = 0;
        double mare = 0;
        int n = 0;
        double epsilon = 1e-10;

        try {
            FileReader filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();

            for (String[] row : allData) {
                double y_true = Double.parseDouble(row[0]);
                double y_predicted = Double.parseDouble(row[1]);

                double error = y_true - y_predicted;

                mse += Math.pow(error, 2);
                mae += Math.abs(error);
                mare += Math.abs(error) / (Math.abs(y_true) + epsilon);

                n++;
            }

            mse /= n;
            mae /= n;
            mare /= n;

            System.out.println("for " + filePath);
            System.out.println("\tMSE = " + mse);
            System.out.println("\tMAE = " + mae);
            System.out.println("\tMARE = " + mare);
            System.out.println();

        } catch (Exception e) {
            System.out.println("Error reading the CSV file");
        }
    }
}