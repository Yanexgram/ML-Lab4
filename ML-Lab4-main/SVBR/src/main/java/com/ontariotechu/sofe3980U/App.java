package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

public class App {

    public static void main(String[] args) {
        evaluateModel("model_1.csv");
        evaluateModel("model_2.csv");
        evaluateModel("model_3.csv");
    }

    public static void evaluateModel(String filePath) {
        double bce = 0;
        int TP = 0, FP = 0, TN = 0, FN = 0;
        int n = 0;
        double epsilon = 1e-10;

        try {
            FileReader filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();

            for (String[] row : allData) {
                int y = Integer.parseInt(row[0]);
                double y_hat = Double.parseDouble(row[1]);

                // BCE
                if (y == 1)
                    bce += Math.log(y_hat + epsilon);
                else
                    bce += Math.log(1 - y_hat + epsilon);

                // threshold = 0.5
                int y_pred = (y_hat >= 0.5) ? 1 : 0;

                // Confusion Matrix
                if (y == 1 && y_pred == 1) TP++;
                else if (y == 0 && y_pred == 1) FP++;
                else if (y == 0 && y_pred == 0) TN++;
                else if (y == 1 && y_pred == 0) FN++;

                n++;
            }

            bce = -bce / n;

            double accuracy = (double)(TP + TN) / n;
            double precision = (double)TP / (TP + FP);
            double recall = (double)TP / (TP + FN);
            double f1 = 2 * (precision * recall) / (precision + recall);

            // AUC ROC
            int steps = 100;
            double[] TPR = new double[steps];
            double[] FPR = new double[steps];

            int n_pos = TP + FN;
            int n_neg = TN + FP;

            for (int i = 0; i < steps; i++) {
                double th = i / 100.0;

                int tp = 0, fp = 0;

                for (String[] row : allData) {
                    int y = Integer.parseInt(row[0]);
                    double y_hat = Double.parseDouble(row[1]);

                    int pred = (y_hat >= th) ? 1 : 0;

                    if (y == 1 && pred == 1) tp++;
                    if (y == 0 && pred == 1) fp++;
                }

                TPR[i] = (double)tp / n_pos;
                FPR[i] = (double)fp / n_neg;
            }

            double auc = 0;
            for (int i = 1; i < steps; i++) {
                auc += (TPR[i] + TPR[i - 1]) * Math.abs(FPR[i] - FPR[i - 1]) / 2;
            }

            // OUTPUT
            System.out.println("for " + filePath);
            System.out.println("\tBCE = " + bce);

            System.out.println("\tConfusion matrix");
            System.out.println("\t\t\ty=1\t y=0");
            System.out.println("\ty^=1\t\t" + TP + "\t " + FP);
            System.out.println("\ty^=0\t\t" + FN + "\t " + TN);

            System.out.println("\tAccuracy = " + accuracy);
            System.out.println("\tPrecision = " + precision);
            System.out.println("\tRecall = " + recall);
            System.out.println("\tf1 score = " + f1);
            System.out.println("\tauc roc = " + auc);
            System.out.println();

        } catch (Exception e) {
            System.out.println("Error reading CSV");
        }
    }
}