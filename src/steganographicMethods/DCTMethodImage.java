package steganographicMethods;

import img.ImageBMP;
import mathOperations.BitOperations;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DCTMethodImage {

    private String pathToEmptyContainer;
    private String pathToResultContainer;
    private int sizeOfSegments = 8;
    private double p = 0.25;
    private int u1 = 3;
    private int v1 = 4;
    private int u2 = 4;
    private int v2 = 3;
    private boolean doNormalizing = true;

    public DCTMethodImage(String inputImage, String outputImage, double comparisonCoefficient) {
        pathToEmptyContainer = inputImage;
        pathToResultContainer = outputImage;
        p = comparisonCoefficient;
    }

    public DCTMethodImage(String inputImage, String outputImage, double comparisonCoefficient, int newSizeOfSegments) {
        pathToEmptyContainer = inputImage;
        pathToResultContainer = outputImage;
        p = comparisonCoefficient;
        sizeOfSegments = newSizeOfSegments;
    }

    public void setSizeOfSegments(int newSize) {
        sizeOfSegments = newSize;
    }

    public int getSizeOfSegments() {
        return sizeOfSegments;
    }

    public void setdoNormalizing(boolean doNorm) {
        doNormalizing = doNorm;
    }

    public void setFirstXCoefOfDCT(int u) {
        u1 = u;
    }

    public int getFirstXCoefOfDCT() {
        return u1;
    }

    public void setFirstYCoefOfDCT(int v) {
        v1 = v;
    }

    public int getFirstYCoefOfDCT() {
        return v1;
    }

    public void setSecondXCoefOfDCT(int u) {
        u2 = u;
    }

    public int getSecondXCoefOfDCT() {
        return u2;
    }

    public void setSecondYCoefOfDCT(int v) {
        v2 = v;
    }

    public int getSecondYCoefOfDCT() {
        return v2;
    }

    public void setComparisonCoefficient(int comparisonCoefficient) {
        p = comparisonCoefficient;
    }

    public double getComparisonCoefficient() {
        return p;
    }

    public void encodeImage(byte[] byteCode) {
        ImageBMP imageInput = new ImageBMP(this.pathToEmptyContainer);
        ArrayList<short[][]> listOfBluePixel = this.imageToListOfBlueSegments(imageInput);
        ArrayList<double[][]> listOfSpectrCoefOfDCT = this.calculateSpectrCoefOfDCT(listOfBluePixel);
        ArrayList<double[][]> listOfSpectrCoefOfDCTWithInsertedMessage = this.insertMassage(listOfSpectrCoefOfDCT, byteCode);
        ArrayList<double[][]> listOfInvertSpectrCoefOfDCT = this.invertDCT(listOfSpectrCoefOfDCTWithInsertedMessage);
        double[][] listOfBluePixels = this.invertDCTToBluePixelsMassive(listOfInvertSpectrCoefOfDCT, imageInput);
        this.encodeImage(imageInput, listOfBluePixels);
        System.out.println("Message was inserted!");
    }

    public byte[] decodeMessageFromImage(String path) {
        ImageBMP imageDecode = new ImageBMP(path);
        ArrayList<short[][]> decodeBluePixel = this.imageToListOfBlueSegments(imageDecode);
        ArrayList<double[][]> listOfDecodeSpectrCoefOfDCT = this.calculateSpectrCoefOfDCT(decodeBluePixel);
        return this.decodeMessage(listOfDecodeSpectrCoefOfDCT);

    }

    private ArrayList<short[][]> imageToListOfBlueSegments(ImageBMP emptyContainer) {
        int numberOfRow = emptyContainer.getNumberOfRow();
        int numberOfColumn = emptyContainer.getNumberOfColumn();
        if (numberOfRow % sizeOfSegments != 0)
            numberOfRow = numberOfRow - numberOfRow % sizeOfSegments;
        if (numberOfColumn % sizeOfSegments != 0)
            numberOfColumn = numberOfColumn - numberOfColumn % sizeOfSegments;
        int numberOfSegments = numberOfColumn * numberOfRow / (sizeOfSegments * sizeOfSegments);
        ArrayList<short[][]> listOfSegments = new ArrayList<>();
        short[][] pixelBlue = new short[numberOfColumn][numberOfRow];
        for (int x = 0; x < numberOfColumn; x++) {
            for (int y = 0; y < numberOfRow; y++) {
                try {
                    Color pixel = emptyContainer.getRGB(x, y);
                    pixelBlue[x][y] = (short) pixel.getBlue();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.print("qwe");
                }
            }
        }
        int indexOfStartRow = 0;
        int indexOfEndRow = sizeOfSegments - 1;
        for (int i = 0; i < numberOfSegments; i++) {
            int indexOfStartColumn = (sizeOfSegments * i) % numberOfColumn;
            int indexOfEndColumn = indexOfStartColumn + sizeOfSegments - 1;
            short[][] tempMassive = submatrix(pixelBlue, indexOfStartColumn, indexOfEndColumn, indexOfStartRow, indexOfEndRow);
            if (indexOfEndColumn == numberOfColumn - 1) {
                indexOfStartRow = indexOfStartRow + sizeOfSegments;
                indexOfEndRow = indexOfEndRow + sizeOfSegments;
            }
            listOfSegments.add(tempMassive);
        }

        return listOfSegments;
    }

    private short[][] submatrix(short[][] pixelMatrix, int indexOfStartColumn, int indexOfEndColumn, int indexOfStartRow, int indexOfEndRow) {
        short[][] tempMassive = new short[indexOfEndColumn - indexOfStartColumn + 1][indexOfEndRow - indexOfStartRow + 1];
        for (int i = indexOfStartColumn, k = 0; i <= indexOfEndColumn; i++, k++)
            for (int j = indexOfStartRow, l = 0; j <= indexOfEndRow; j++, l++)
                tempMassive[k][l] = pixelMatrix[i][j];

        return tempMassive;
    }

    private void encodeImage(ImageBMP emptyContainer, double[][] bluePixels) {
        int numberOfRow = emptyContainer.getNumberOfRow();
        int numberOfColumn = emptyContainer.getNumberOfColumn();
//        if (numberOfRow % sizeOfSegments != 0)
//            numberOfRow = numberOfRow - numberOfRow % sizeOfSegments;
//        if (numberOfColumn % sizeOfSegments != 0)
//            numberOfColumn = numberOfColumn - numberOfColumn % sizeOfSegments;
        BufferedImage encImage = new BufferedImage(numberOfColumn, numberOfRow, BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < numberOfColumn; x++) {
            for (int y = 0; y < numberOfRow; y++) {
                Color pixel = new Color(emptyContainer.getRGB(x, y).getRed(), emptyContainer.getRGB(x, y).getGreen(), (int) Math.round(bluePixels[x][y]), emptyContainer.getRGB(x, y).getAlpha());
                encImage.setRGB(x, y, pixel.getRGB());
            }
        }
        try {
            ImageIO.write(encImage, "bmp", new File(this.pathToResultContainer));
            //ImageIO.write(encImage, "png", new File("images\\test.png"));
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }

    }

    private double sigmaCoefficient(int sigma) {
        if (sigma == 0) {
            return 1.0 / Math.sqrt(2);
        }
        return 1.0;
    }

    private ArrayList<double[][]> calculateSpectrCoefOfDCT(ArrayList<short[][]> listOfSegments) {
        ArrayList<double[][]> listOfSpectrCoefOfDCT = new ArrayList();
        for (int i = 0; i < listOfSegments.size(); i++) {
            double[][] tempMassive = new double[sizeOfSegments][sizeOfSegments];
            for (int j = 0; j < sizeOfSegments; j++) {
                for (int k = 0; k < sizeOfSegments; k++) {
                    double sum = 0;
                    for (int tempColumn = 0; tempColumn < sizeOfSegments; tempColumn++) {
                        for (int tempRow = 0; tempRow < sizeOfSegments; tempRow++) {
                            sum = sum + listOfSegments.get(i)[tempColumn][tempRow]*
                                            Math.cos(Math.PI * j * (2 * tempColumn + 1) / (2 * sizeOfSegments)) *
                                            Math.cos(Math.PI * k * (2 * tempRow + 1) / (2 * sizeOfSegments));
                        }
                    }
                    tempMassive[j][k] = sigmaCoefficient(j) * sigmaCoefficient(k) * sum / (Math.sqrt(2 * sizeOfSegments));
                }
            }
            listOfSpectrCoefOfDCT.add(tempMassive);
        }
        return listOfSpectrCoefOfDCT;
    }

    private ArrayList<double[][]> insertMassage(ArrayList<double[][]> spectrCoefOfDCT, byte[] byteCode) {
        int messageLength = byteCode.length;
        ArrayList<double[][]> result = new ArrayList<>(spectrCoefOfDCT.size());
        for (int i = 0; i < messageLength; i++) {

            double[][] tempSpectrCoef = spectrCoefOfDCT.get(i);

            double w1 = Math.abs(tempSpectrCoef[u1][v1]);
            double w2 = Math.abs(tempSpectrCoef[u2][v2]);
            double z1 = 0;
            double z2 = 0;
            if (tempSpectrCoef[u1][v1] >= 0) {
                z1 = 1;
            } else {
                z1 = -1;
            }
            if (tempSpectrCoef[u2][v2] >= 0) {
                z2 = 1;
            } else {
                z2 = -1;
            }
            if (byteCode[i] == 0) {
                if ((w1 - w2) <= p) {
                    w1 = p + w2 + 1;
                }
            }
            if (byteCode[i] == 1) {
                if ((w1 - w2) >= -p) {
                    w2 = p + w1 + 1;
                }
            }
            tempSpectrCoef[u1][v1] = z1 * w1;
            tempSpectrCoef[u2][v2] = z2 * w2;
            result.add(tempSpectrCoef);
        }
        return result;
    }

    private ArrayList<double[][]> invertDCT(ArrayList<double[][]> spectrCoefOfDCT) {
        ArrayList<double[][]> listOfInvertSpectrCoefOfDCT = new ArrayList();
        for (int i = 0; i < spectrCoefOfDCT.size(); i++) {
            double[][] tempMassive = new double[sizeOfSegments][sizeOfSegments];
            for (int j = 0; j < sizeOfSegments; j++) {
                for (int k = 0; k < sizeOfSegments; k++) {
                    double sum = 0;
                    for (int tempColumn = 0; tempColumn < sizeOfSegments; tempColumn++) {
                        for (int tempRow = 0; tempRow < sizeOfSegments; tempRow++) {
                            sum = sum + sigmaCoefficient(tempColumn) *
                                    sigmaCoefficient(tempRow) *
                                    spectrCoefOfDCT.get(i)[tempColumn][tempRow] *
                                    Math.cos(Math.PI * tempColumn * (2 * j + 1) / (2 * sizeOfSegments)) *
                                    Math.cos(Math.PI * tempRow * (2 * k + 1) / (2 * sizeOfSegments));
                        }
                        tempMassive[j][k] = sum / (Math.sqrt(2 * sizeOfSegments));

                    }
                }
            }
            listOfInvertSpectrCoefOfDCT.add(tempMassive);
        }
        return listOfInvertSpectrCoefOfDCT;
    }

    private void normFunction(double[][] input, int numberOfColumn, int numberOfRow) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRow; j++) {
                if (input[i][j] > max) max = input[i][j];
                if (input[i][j] < min) min = input[i][j];
            }
        }
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRow; j++) {
                input[i][j] = 255 * (input[i][j] + Math.abs(min)) / (max + Math.abs(min));
            }
        }
    }

    private double[][] invertDCTToBluePixelsMassive(ArrayList<double[][]> listOfInvertSpectrCoefOfDCT, ImageBMP emptyContainer) {
        int numberOfRow = emptyContainer.getNumberOfRow();
        int numberOfColumn = emptyContainer.getNumberOfColumn();
        if (numberOfRow % sizeOfSegments != 0)
            numberOfRow = numberOfRow - numberOfRow % sizeOfSegments;
        if (numberOfColumn % sizeOfSegments != 0)
            numberOfColumn = numberOfColumn - numberOfColumn % sizeOfSegments;
        int numberOfSegments = numberOfColumn * numberOfRow / (sizeOfSegments * sizeOfSegments);
        double[][] resultMassive = new double[emptyContainer.getNumberOfColumn()][emptyContainer.getNumberOfRow()];
        int segmentsInCoulmn = numberOfColumn / sizeOfSegments;
        int indexX = 0;
        int indexY = 0;
        for (int i = 0; i < numberOfSegments; i++) {
            if (indexX == segmentsInCoulmn) {
                indexX = 0;
                indexY++;
            }
            for (int j = 0; j < sizeOfSegments; j++) {
                for (int k = 0; k < sizeOfSegments; k++) {
                    try {
                        resultMassive[indexX * sizeOfSegments + j][indexY * sizeOfSegments + k] = listOfInvertSpectrCoefOfDCT.get(i)[j][k];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.print("q");
                    }
                }
            }
            indexX++;
        }
        numberOfRow = emptyContainer.getNumberOfRow();
        numberOfColumn = emptyContainer.getNumberOfColumn();
        if (numberOfRow % sizeOfSegments != 0) {
            for (int i = numberOfColumn - numberOfColumn % sizeOfSegments; i < numberOfColumn; i++)
                for (int j = 0; j < numberOfRow; j++)
                    resultMassive[i][j] = emptyContainer.getRGB(i, j).getBlue();
        }
        if (numberOfColumn % sizeOfSegments != 0) {
            for (int i = 0; i < numberOfColumn; i++)
                for (int j = numberOfRow - numberOfRow % sizeOfSegments; j < numberOfRow; j++)
                    resultMassive[i][j] = emptyContainer.getRGB(i, j).getBlue();
        }
        if (doNormalizing) {
            normFunction(resultMassive, numberOfColumn, numberOfRow);
        }
        return resultMassive;
    }

    private byte[] decodeMessage(ArrayList<double[][]> spectrCoefOfDCT) {
        byte[] decodeMessage = new byte[spectrCoefOfDCT.size()];
        for (int k = 0; k < spectrCoefOfDCT.size(); k++) {
            double w1 = Math.abs(spectrCoefOfDCT.get(k)[u1][v1]);
            double w2 = Math.abs(spectrCoefOfDCT.get(k)[u2][v2]);
            if (w1 > w2)
                decodeMessage[k] = 0;
            if (w1 < w2)
                decodeMessage[k] = 1;
        }
        return decodeMessage;
    }
}
