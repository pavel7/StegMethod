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
    private int p = 25;
    private int u1 = 3;
    private int v1 = 4;
    private int u2 = 4;
    private int v2 = 3;
    private boolean doNormalizing = true;

    public DCTMethodImage(String inputImage, String outputImage, int comparisonCoefficient) {
        pathToEmptyContainer = inputImage;
        pathToResultContainer = outputImage;
        p = comparisonCoefficient;
    }

    public DCTMethodImage(String inputImage, String outputImage, int comparisonCoefficient, int newSizeOfSegments) {
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

    public int getComparisonCoefficient() {
        return p;
    }

    public void encodeImage(String message) {
        ImageBMP imageInput = new ImageBMP(this.pathToEmptyContainer);
        ArrayList<short[][]> listOfBluePixel = this.imageToListOfBlueSegments(imageInput);
        ArrayList<double[][]> listOfSpectrCoefOfDCT = this.calculateSpectrCoefOfDCT(listOfBluePixel);
        this.insertMassage(listOfSpectrCoefOfDCT, message);
        ArrayList<double[][]> listOfInvertSpectrCoefOfDCT = this.invertDCT(listOfSpectrCoefOfDCT);
        double[][] listOfBluePixels = this.invertDCTToBluePixelsMassive(listOfInvertSpectrCoefOfDCT, imageInput);
        this.encodeImage(imageInput, listOfBluePixels);
        System.out.println("Message was inserted!");
    }

    public void decodeMessageFromImage(String path) {
        ImageBMP imageDecode = new ImageBMP(path);
        ArrayList<short[][]> decodeBluePixel = this.imageToListOfBlueSegments(imageDecode);
        ArrayList<double[][]> listOfDecodeSpectrCoefOfDCT = this.calculateSpectrCoefOfDCT(decodeBluePixel);
        System.out.println(this.decodeMessage(listOfDecodeSpectrCoefOfDCT));

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
        short[][] pixelBlue = new short[numberOfRow][numberOfColumn];
        for (int x = 0; x < numberOfRow; x++) {
            for (int y = 0; y < numberOfColumn; y++) {
                try {
                    Color pixel = emptyContainer.getRGB(x, y);
                    pixelBlue[x][y] = (short) pixel.getBlue();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.print("qwe");
                }
            }
        }
        int indexOfStartColumn = 0;
        int indexOfEndColumn = sizeOfSegments - 1;
        for (int i = 0; i < numberOfSegments; i++) {
            int indexOfStartRow = (sizeOfSegments * i) % numberOfRow;
            int indexOfEndRow = indexOfStartRow + sizeOfSegments - 1;
            short[][] tempMassive = submatrix(pixelBlue, indexOfStartRow, indexOfEndRow, indexOfStartColumn, indexOfEndColumn);
            if (indexOfEndRow == numberOfRow - 1) {
                indexOfStartColumn = indexOfStartColumn + sizeOfSegments;
                indexOfEndColumn = indexOfEndColumn + sizeOfSegments;
            }
            listOfSegments.add(tempMassive);
        }

        return listOfSegments;
    }

    private short[][] submatrix(short[][] pixelMatrix, int indexOfStartRow, int indexOfEndRow, int indexOfStartColumn, int indexOfEndColumn) {
        short[][] tempMassive = new short[indexOfEndRow - indexOfStartRow + 1][indexOfEndColumn - indexOfStartColumn + 1];
        for (int i = indexOfStartRow, k = 0; i <= indexOfEndRow; i++, k++)
            for (int j = indexOfStartColumn, l = 0; j <= indexOfEndColumn; j++, l++)
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
        BufferedImage encImage = new BufferedImage(numberOfRow, numberOfColumn, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < numberOfRow; x++) {
            for (int y = 0; y < numberOfColumn; y++) {
                Color pixel = new Color(emptyContainer.getRGB(x, y).getRed(), emptyContainer.getRGB(x, y).getGreen(), (int) Math.round(bluePixels[x][y]), emptyContainer.getRGB(x, y).getAlpha());
                int test1 = emptyContainer.getRGB(x, y).getBlue();
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
        return 1;
    }

    private ArrayList<double[][]> calculateSpectrCoefOfDCT(ArrayList<short[][]> listOfSegments) {
        ArrayList<double[][]> listOfSpectrCoefOfDCT = new ArrayList();
        for (int i = 0; i < listOfSegments.size(); i++) {
            double[][] tempMassive = new double[sizeOfSegments][sizeOfSegments];
            for (int j = 0; j < sizeOfSegments; j++) {
                for (int k = 0; k < sizeOfSegments; k++) {
                    double sum = 0;
                    for (int tempRow = 0; tempRow < sizeOfSegments; tempRow++) {
                        for (int tempColumn = 0; tempColumn < sizeOfSegments; tempColumn++) {
                            sum = sum +
                                    listOfSegments.get(i)[tempRow][tempColumn] *
                                            Math.cos(Math.PI * j * (2 * tempRow + 1) / (2 * sizeOfSegments)) *
                                            Math.cos(Math.PI * k * (2 * tempColumn + 1) / (2 * sizeOfSegments));
                        }
                    }
                    tempMassive[j][k] = sigmaCoefficient(j) * sigmaCoefficient(k) * sum / (Math.sqrt(2 * sizeOfSegments));
                }
            }
            listOfSpectrCoefOfDCT.add(tempMassive);
        }
        return listOfSpectrCoefOfDCT;
    }

    private void insertMassage(ArrayList<double[][]> spectrCoefOfDCT, String message) {
        int messageLength = message.length();
        //ArrayList<double[][]> listOfSpectrCoefOfDCT = new ArrayList();
        short[] str2vec = new short[messageLength];
        for (int i = 0; i < messageLength; i++) {
            str2vec[i] = (short) message.charAt(i);
        }
        for (int i = 0; i < messageLength; i++) {
            byte[] symbolInDec = BitOperations.decToBit(str2vec[i]);
            for (int j = 0; j < 8; j++) {
                double[][] tempSpectrCoef = spectrCoefOfDCT.get(j + sizeOfSegments * i);
                double w1 = Math.abs(tempSpectrCoef[u1][v1]);
                double w2 = Math.abs(tempSpectrCoef[u2][v2]);
                int z1 = 0;
                int z2 = 0;
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
                if (symbolInDec[j] == 0) {
                    if ((w1 - w2) <= p) {
                        w1 = p + w2 + 1;
                    }
                }
                if (symbolInDec[j] == 1) {
                    if ((w1 - w2) >= -p) {
                        w2 = p + w1 + 1;
                    }
                }
                spectrCoefOfDCT.get(j + sizeOfSegments * i)[u1][v1] = z1 * w1;
                spectrCoefOfDCT.get(j + sizeOfSegments * i)[u2][v2] = z2 * w2;
            }
        }
    }

    private ArrayList<double[][]> invertDCT(ArrayList<double[][]> spectrCoefOfDCT) {
        ArrayList<double[][]> listOfInvertSpectrCoefOfDCT = new ArrayList();
        for (int i = 0; i < spectrCoefOfDCT.size(); i++) {
            double[][] tempMassive = new double[sizeOfSegments][sizeOfSegments];
            for (int j = 0; j < sizeOfSegments; j++) {
                for (int k = 0; k < sizeOfSegments; k++) {
                    double sum = 0;
                    for (int tempRow = 0; tempRow < sizeOfSegments; tempRow++) {
                        for (int tempColumn = 0; tempColumn < sizeOfSegments; tempColumn++) {
                            sum = sum + sigmaCoefficient(tempRow) *
                                    sigmaCoefficient(tempColumn) *
                                    spectrCoefOfDCT.get(i)[tempRow][tempColumn] *
                                    Math.cos(Math.PI * tempRow * (2 * j + 1) / (2 * sizeOfSegments)) *
                                    Math.cos(Math.PI * tempColumn * (2 * k + 1) / (2 * sizeOfSegments));
                        }
                        tempMassive[j][k] = sum / (Math.sqrt(2 * sizeOfSegments));

                    }
                }
            }
            listOfInvertSpectrCoefOfDCT.add(tempMassive);
        }
        return listOfInvertSpectrCoefOfDCT;
    }

    private void normFunction(double[][] input, int numberOfRow, int numberOfColumn) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                if (input[i][j] > max) max = input[i][j];
                if (input[i][j] < min) min = input[i][j];
            }
        }
        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
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
        double[][] resultMassive = new double[emptyContainer.getNumberOfRow()][emptyContainer.getNumberOfColumn()];
        int segmentsInRow = numberOfRow / sizeOfSegments;
        int indexX = 0;
        int indexY = 0;
        for (int i = 0; i < numberOfSegments; i++) {
            if (indexX == segmentsInRow) {
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
            for (int i = numberOfRow - numberOfRow % sizeOfSegments; i < numberOfRow; i++)
                for (int j = 0; j < numberOfColumn; j++)
                    resultMassive[i][j] = emptyContainer.getRGB(i, j).getBlue();
        }
        if (numberOfColumn % sizeOfSegments != 0) {
            for (int i = 0; i < numberOfRow; i++)
                for (int j = numberOfColumn - numberOfColumn % sizeOfSegments; j < numberOfColumn; j++)
                    resultMassive[i][j] = emptyContainer.getRGB(i, j).getBlue();
        }
        if (doNormalizing) {
            normFunction(resultMassive, numberOfRow, numberOfColumn);
        }
        return resultMassive;
    }

    private String decodeMessage(ArrayList<double[][]> spectrCoefOfDCT) {
        int i = 0;
        int j = 0;
        char[] decodeMessage = new char[spectrCoefOfDCT.size() / 8];
        byte[] temp = new byte[8];
        for (int k = 0; k < spectrCoefOfDCT.size(); k++) {
            double w1 = Math.abs(spectrCoefOfDCT.get(k)[u1][v1]);
            double w2 = Math.abs(spectrCoefOfDCT.get(k)[u2][v2]);
            if (w1 > w2)
                temp[i] = 0;
            if (w1 < w2)
                temp[i] = 1;
            i++;
            if (i == 8) {
                i = 0;
                char newSymbol = (char) BitOperations.bitToDec(temp);
                decodeMessage[j] = newSymbol;
                j++;
                for (int arrayIndex = 0; arrayIndex < temp.length; arrayIndex++)
                    temp[arrayIndex] = 0;
            }
        }
        String result = new String(decodeMessage);
        return result;
    }
}
