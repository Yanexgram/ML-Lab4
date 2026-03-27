package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

public class App {

    public static void main(String[] args) {
        evaluateModel("model.csv");
    }

    public static void evaluateModel(String filePath) {

        double ce = 0;
        int n = 0;
        double epsilon = 1e-10;

        int classes = 5;
        int[][] confusion = new int[classes][classes];

        try {
            FileReader filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();

            for (String[] row : allData) {

                int y_true = Integer.parseInt(row[0]); // actual class (1–5)

                double[] probs = new double[classes];

                for (int i = 0; i < classes; i++) {
                    probs[i] = Double.parseDouble(row[i + 1]);
                }

                // Cross Entropy
                ce += Math.log(probs[y_true - 1] + epsilon);

                // Prediction = argmax
                int predicted = 0;
                double max = probs[0];

                for (int i = 1; i < classes; i++) {
                    if (probs[i] > max) {
                        max = probs[i];
                        predicted = i;
                    }
                }

                // confusion matrix
                confusion[predicted][y_true - 1]++;

                n++;
            }

            ce = -ce / n;

            // OUTPUT
            System.out.println("CE = " + ce);
            System.out.println("Confusion matrix");

            System.out.print("\t");
            for (int i = 1; i <= classes; i++) {
                System.out.print("y=" + i + "\t");
            }
            System.out.println();

            for (int i = 0; i < classes; i++) {
                System.out.print("y^=" + (i + 1) + "\t");
                for (int j = 0; j < classes; j++) {
                    System.out.print(confusion[i][j] + "\t");
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("Error reading CSV");
        }
    }
}