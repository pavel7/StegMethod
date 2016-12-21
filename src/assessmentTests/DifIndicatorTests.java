package assessmentTests;

import img.ImageBMP;

import java.awt.*;
import java.io.File;

public class DifIndicatorTests {
    private String pathToEmptyContainer;
    private String pathToResultContainer;
    private int imageHeight;
    private int imageWidth;
    private int sizeOfSegments = 8;
    private int numberOfSegments;
    private ImageBMP emptyContainer;
    private ImageBMP resultContainer;


    public DifIndicatorTests(String newEmptyContainer, String newResultContainer) {
        pathToEmptyContainer = newEmptyContainer;
        pathToResultContainer = newResultContainer;
        emptyContainer = new ImageBMP(pathToEmptyContainer);
        resultContainer = new ImageBMP(pathToResultContainer);
        imageHeight = emptyContainer.getNumberOfRow();
        imageWidth = emptyContainer.getNumberOfColumn();
        numberOfSegments = (imageHeight * imageWidth) / (sizeOfSegments * sizeOfSegments);
    }

    public DifIndicatorTests(String newEmptyContainer, String newResultConatine, int newSizeOfSegments) {
        pathToEmptyContainer = newEmptyContainer;
        pathToResultContainer = newResultConatine;
        emptyContainer = new ImageBMP(pathToEmptyContainer);
        resultContainer = new ImageBMP(pathToResultContainer);
        sizeOfSegments = newSizeOfSegments;
        imageHeight = emptyContainer.getNumberOfRow();
        imageWidth = emptyContainer.getNumberOfColumn();
        numberOfSegments = (imageHeight * imageWidth) / (sizeOfSegments * sizeOfSegments);
    }

    public void calculateDifIndicator() {
        int p = 2;
        System.out.println("Maximum Difference is " + calculateOfMaximumDifference());
        System.out.println("Average Absolute Difference is " + calculateAverageAbsoluteDifference());
        System.out.println("Normalized Average Absolute Difference is " + calculateNormalizedAverageAbsoluteDifference());
        System.out.println("Mean Square Error is " + calculateMeanSquareError());
        System.out.println("Normalized Mean Square Error is " + calculateNormalizedMeanSquareError());
        System.out.println("Lp-Norm is " + calculateLpNorm(p) + ", where p is " + p);
    }

    private double calculateOfMaximumDifference() {
        double result = Double.MIN_VALUE;
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                Color pixelEmpty = emptyContainer.getRGB(i, j);
                Color pixelResult = resultContainer.getRGB(i, j);
                double redDif = Math.abs(pixelEmpty.getRed() - pixelResult.getRed());
                double greenDif = Math.abs(pixelEmpty.getGreen() - pixelResult.getGreen());
                double blueDif = Math.abs(pixelEmpty.getBlue() - pixelResult.getBlue());
                if (result < redDif)
                    result = redDif;
                if (result < greenDif)
                    result = greenDif;
                if (result < blueDif)
                    result = blueDif;
            }
        }
        return result;
    }

    private double calculateAverageAbsoluteDifference() {
        double result = 0;
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                Color pixelEmpty = emptyContainer.getRGB(i, j);
                Color pixelResult = resultContainer.getRGB(i, j);
                double redDif = Math.abs(pixelEmpty.getRed() - pixelResult.getRed());
                double greenDif = Math.abs(pixelEmpty.getGreen() - pixelResult.getGreen());
                double blueDif = Math.abs(pixelEmpty.getBlue() - pixelResult.getBlue());
                result = result + redDif + greenDif + blueDif;
            }
        }
        result = result / (imageHeight * imageWidth);
        return result;
    }

    private double calculateNormalizedAverageAbsoluteDifference() {
        double sum1 = 0;
        double sum2 = 0;
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                Color pixelEmpty = emptyContainer.getRGB(i, j);
                Color pixelResult = resultContainer.getRGB(i, j);
                double redDif = Math.abs(pixelEmpty.getRed() - pixelResult.getRed());
                double greenDif = Math.abs(pixelEmpty.getGreen() - pixelResult.getGreen());
                double blueDif = Math.abs(pixelEmpty.getBlue() - pixelResult.getBlue());
                sum1 = sum1 + redDif + greenDif + blueDif;
                //sum2 = sum2 + pixelEmpty.getRed() + pixelEmpty.getGreen() + pixelEmpty.getBlue();
                sum2 = sum2 + pixelEmpty.getBlue();
            }
        }
        return sum1 / sum2;
    }

    private double calculateMeanSquareError() {
        double result = 0;
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                Color pixelEmpty = emptyContainer.getRGB(i, j);
                Color pixelResult = resultContainer.getRGB(i, j);
                double redDif = Math.pow(pixelEmpty.getRed() - pixelResult.getRed(), 2);
                double greenDif = Math.pow(pixelEmpty.getGreen() - pixelResult.getGreen(), 2);
                double blueDif = Math.pow(pixelEmpty.getBlue() - pixelResult.getBlue(), 2);
                result = result + redDif + greenDif + blueDif;
            }
        }
        result = result / (imageHeight * imageWidth);
        return result;
    }

    private double calculateNormalizedMeanSquareError() {
        double sum1 = 0;
        double sum2 = 0;
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                Color pixelEmpty = emptyContainer.getRGB(i, j);
                Color pixelResult = resultContainer.getRGB(i, j);
                double redDif = Math.pow(pixelEmpty.getRed() - pixelResult.getRed(), 2);
                double greenDif = Math.pow(pixelEmpty.getGreen() - pixelResult.getGreen(), 2);
                double blueDif = Math.pow(pixelEmpty.getBlue() - pixelResult.getBlue(), 2);
                sum1 = sum1 + redDif + greenDif + blueDif;
                //sum2 = sum2 + pixelEmpty.getRed() + pixelEmpty.getGreen() + pixelEmpty.getBlue();
                sum2 = sum2 + Math.pow(pixelEmpty.getBlue(), 2);
            }
        }
        return sum1 / sum2;
    }

    private double calculateLpNorm(int p) {
        double sum = 0;
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                Color pixelEmpty = emptyContainer.getRGB(i, j);
                Color pixelResult = resultContainer.getRGB(i, j);
                double redDif = Math.pow(Math.abs(pixelEmpty.getRed() - pixelResult.getRed()), p);
                double greenDif = Math.pow(Math.abs(pixelEmpty.getGreen() - pixelResult.getGreen()), p);
                double blueDif = Math.pow(Math.abs(pixelEmpty.getBlue() - pixelResult.getBlue()), p);
                sum = sum + redDif + greenDif + blueDif;
            }
        }
        return Math.pow(sum / (imageHeight * imageWidth), 1.0 / p);
    }

    public static void main(String[] args) {
        String pathToEmptyContainer = "images" + File.separator + "Lenna.bmp";
        String pathToResultContainer = "images" + File.separator + "EncodedImage.bmp";
        DifIndicatorTests test = new DifIndicatorTests(pathToEmptyContainer, pathToResultContainer);
        test.calculateDifIndicator();
    }
}
