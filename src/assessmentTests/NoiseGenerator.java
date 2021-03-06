package assessmentTests;


import img.ImageBMP;
import img.Pixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class NoiseGenerator {
    private String pathToEncodedContainer;
    private String pathToResultContainer;
    private String format;
    private int coefOfNoise;
    private int sizeOfSegments = 8;
    private int startSegment;

    public NoiseGenerator(String inputImage, String outputImage, String newFormat, int newCoefOfNoise, int newStartSegment) {
        pathToEncodedContainer = inputImage;
        pathToResultContainer = outputImage;
        coefOfNoise = newCoefOfNoise;
        format = newFormat;
        startSegment = newStartSegment;
    }

    public void addNoise(int bluePixelValue) {
        boolean invertCoef = false;
        if (this.coefOfNoise > 50) {
            invertCoef = true;
        }
        ImageBMP firstImage = new ImageBMP(this.pathToEncodedContainer + "0" + this.format);
        int numberOfRow = firstImage.getNumberOfRow();
        int numberOfColumn = firstImage.getNumberOfColumn();
        int numberOfPixels = 0;
        if (invertCoef == false) {
            numberOfPixels = (int) ((numberOfRow * numberOfColumn / 100.0) * coefOfNoise);
        } else {
            numberOfPixels = (int) ((numberOfRow * numberOfColumn / 100.0) * (100 - coefOfNoise));
        }
        for (int i = 0; i < this.sizeOfSegments; i++) {
            Pixel[] massiveOfPixels = new Pixel[numberOfPixels];
            for (int j = 0; j < numberOfPixels; j++) {
                Pixel noisePixel;
                boolean ok = false;
                while (ok != true) {
                    noisePixel = new Pixel((int) (Math.random() * numberOfRow), (int) (Math.random() * numberOfColumn));
                    for (int k = 0; k < j; k++) {
                        if (massiveOfPixels[k].equals(noisePixel)) {
                            ok = true;
                        }
                    }
                    if (ok) {
                        ok = false;
                    } else {
                        massiveOfPixels[j] = noisePixel;
                        ok = true;
                    }
                }
            }
            ImageBMP tempImage = new ImageBMP(this.pathToEncodedContainer + i + this.format);
            BufferedImage encImage = new BufferedImage(numberOfRow, numberOfColumn, BufferedImage.TYPE_3BYTE_BGR);
            if (invertCoef == false) {
                for (int x = 0; x < numberOfRow; x++) {
                    for (int y = 0; y < numberOfColumn; y++) {
                        Color pixel = new Color(tempImage.getRGB(x, y).getRed(), tempImage.getRGB(x, y).getGreen(), tempImage.getRGB(x, y).getBlue(), tempImage.getRGB(x, y).getAlpha());
                        encImage.setRGB(x, y, pixel.getRGB());
                    }
                }
                for (int k = 0; k < numberOfPixels; k++) {
                    int x = massiveOfPixels[k].getX();
                    int y = massiveOfPixels[k].getY();
                    encImage.setRGB(x, y, new Color(bluePixelValue, bluePixelValue, bluePixelValue, bluePixelValue).getRGB());

                }
            } else {
                for (int x = 0; x < numberOfRow; x++) {
                    for (int y = 0; y < numberOfColumn; y++) {
                        Color pixel = new Color(bluePixelValue, bluePixelValue, bluePixelValue, bluePixelValue);
                        encImage.setRGB(x, y, pixel.getRGB());
                    }
                }
                for (int k = 0; k < numberOfPixels; k++) {
                    int x = massiveOfPixels[k].getX();
                    int y = massiveOfPixels[k].getY();
                    encImage.setRGB(x, y, new Color(tempImage.getRGB(x, y).getRed(), tempImage.getRGB(x, y).getGreen(), tempImage.getRGB(x, y).getBlue(), tempImage.getRGB(x, y).getAlpha()).getRGB());

                }
            }
            try {
                ImageIO.write(encImage, "bmp", new File(this.pathToResultContainer + i + this.format));
                //ImageIO.write(encImage, "png", new File("images\\test.png"));
            } catch (IOException e) {
                System.out.println("error " + e.getMessage());
            }

        }

    }

    public void addNoiseFast() {
        boolean invertCoef = false;
        if (this.coefOfNoise > 50) {
            invertCoef = true;
        }
        ImageBMP firstImage = new ImageBMP(this.pathToEncodedContainer + this.startSegment + this.format);
        int numberOfRow = firstImage.getNumberOfRow();
        int numberOfColumn = firstImage.getNumberOfColumn();
        int numberOfElements = numberOfRow * numberOfColumn;
        Point[] massivOfPoints = new Point[numberOfElements];
        int numberOfPixels = 0;
        if (invertCoef == false) {
            numberOfPixels = (int) ((numberOfElements / 100.0) * coefOfNoise);
        } else {
            numberOfPixels = (int) ((numberOfElements / 100.0) * (100 - coefOfNoise));
        }
        int k = 0;
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRow; j++) {
                massivOfPoints[k] = new Point(i, j);
                k++;
            }
        }
        for (int i = 0; i < this.sizeOfSegments; i++) {
            int[] massivOfRandElems = this.createArray();
            ImageBMP tempImage = new ImageBMP(this.pathToEncodedContainer + (this.startSegment + i) + this.format);
            BufferedImage encImage = new BufferedImage(numberOfColumn, numberOfRow, BufferedImage.TYPE_3BYTE_BGR);
            if (invertCoef == false) {
                for (int x = 0; x < numberOfColumn; x++) {
                    for (int y = 0; y < numberOfRow; y++) {
                        Color pixel = new Color(tempImage.getRGB(x, y).getRed(), tempImage.getRGB(x, y).getGreen(), tempImage.getRGB(x, y).getBlue(), tempImage.getRGB(x, y).getAlpha());
                        encImage.setRGB(x, y, pixel.getRGB());
                    }
                }
                for (int t = 0; t < numberOfPixels; t++) {
                    int x = (int) massivOfPoints[massivOfRandElems[t]].getX();
                    int y = (int) massivOfPoints[massivOfRandElems[t]].getY();
                    Random random = new Random(System.currentTimeMillis());
                    int bluePixelValue = random.nextInt(256);
                    encImage.setRGB(x, y, new Color(bluePixelValue, bluePixelValue, bluePixelValue, bluePixelValue).getRGB());

                }
            } else {
                for (int x = 0; x < numberOfColumn; x++) {
                    for (int y = 0; y < numberOfRow; y++) {
                        Random random = new Random(System.currentTimeMillis());
                        int bluePixelValue = random.nextInt(256);
                        Color pixel = new Color(bluePixelValue, bluePixelValue, bluePixelValue, bluePixelValue);
                        encImage.setRGB(x, y, pixel.getRGB());
                    }
                }
                for (int t = 0; t < numberOfPixels; t++) {
                    int x = (int) massivOfPoints[massivOfRandElems[t]].getX();
                    int y = (int) massivOfPoints[massivOfRandElems[t]].getY();
                    encImage.setRGB(x, y, new Color(tempImage.getRGB(x, y).getRed(), tempImage.getRGB(x, y).getGreen(), tempImage.getRGB(x, y).getBlue(), tempImage.getRGB(x, y).getAlpha()).getRGB());

                }
            }
            try {
                ImageIO.write(encImage, "bmp", new File(this.pathToResultContainer + (this.startSegment + i) + this.format));
                //ImageIO.write(encImage, "png", new File("images\\test.png"));
            } catch (IOException e) {
                System.out.println("error " + e.getMessage());
            }

        }
    }

    public int[] createArrayOfRandomNumber(int size) {
        Random rnd = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++)
            arr[i] = i;
        for (int i = size - 1; i > 0; i--) {
            int pos = rnd.nextInt(i + 1);
            int a = arr[pos];
            arr[pos] = arr[i];
            arr[i] = a;
        }
        return arr;
    }

    private boolean isPresent(int[] massive, int elem) {
        for (int i = 0; i < massive.length; i++) {
            if (massive[i] == elem)
                return true;
        }
        return false;
    }

    public void makeEmpty(int numberOfAllImages) {
        ImageBMP firstImage = new ImageBMP(this.pathToEncodedContainer + this.startSegment + this.format);
        int numberOfRow = firstImage.getNumberOfRow();
        int numberOfColumn = firstImage.getNumberOfColumn();
        //int numberOfElements = numberOfRow * numberOfColumn;
        int numberOfImages = (int) ((numberOfAllImages / 100.0) * coefOfNoise);
        int[] massiveOfRandImages = createArrayOfRandomNumber(numberOfAllImages);
        int[] necessaryMassiveOfRandomImages = new int[numberOfImages];
        for (int i = 0; i < numberOfImages; i++) {
            necessaryMassiveOfRandomImages[i] = massiveOfRandImages[i];
        }
        for (int i = 0; i < numberOfAllImages; i++) {
            ImageBMP tempImage = new ImageBMP(this.pathToEncodedContainer + (this.startSegment + i) + this.format);
            BufferedImage encImage = new BufferedImage(numberOfColumn, numberOfRow, BufferedImage.TYPE_3BYTE_BGR);
            if (isPresent(necessaryMassiveOfRandomImages, i)) {
                for (int x = 0; x < numberOfColumn; x++) {
                    for (int y = 0; y < numberOfRow; y++) {
                        int bluePixelValue = 255;
                        Color pixel = new Color(bluePixelValue, bluePixelValue, bluePixelValue, bluePixelValue);
                        encImage.setRGB(x, y, pixel.getRGB());
                    }
                }
            } else {
                for (int x = 0; x < numberOfColumn; x++) {
                    for (int y = 0; y < numberOfRow; y++) {
                        Color pixel = new Color(tempImage.getRGB(x, y).getRed(), tempImage.getRGB(x, y).getGreen(), tempImage.getRGB(x, y).getBlue(), tempImage.getRGB(x, y).getAlpha());
                        encImage.setRGB(x, y, pixel.getRGB());
                    }
                }
            }

            try {
                ImageIO.write(encImage, "bmp", new File(this.pathToResultContainer + (this.startSegment + i) + this.format));
                //ImageIO.write(encImage, "png", new File("images\\test.png"));
            } catch (IOException e) {
                System.out.println("error " + e.getMessage());
            }

        }


    }


//        try {
//            ImageIO.write(encImage, "bmp", new File(this.pathToResultContainer + (this.startSegment + i) + this.format));
//            //ImageIO.write(encImage, "png", new File("images\\test.png"));
//        } catch (IOException e) {
//            System.out.println("error " + e.getMessage());
//        }

    //}


//    public void makeClear() {
//
//        ImageBMP firstImage = new ImageBMP(this.pathToEncodedContainer + this.startSegment + this.format);
//        int numberOfRow = firstImage.getNumberOfRow();
//        int numberOfColumn = firstImage.getNumberOfColumn();
//        int numberOfElements = numberOfRow * numberOfColumn;
//        Point[] massivOfPoints = new Point[numberOfElements];
//        int numberOfFrames = this.coefOfNoise/10;
//        int k = 0;
//        for (int i = 0; i < numberOfColumn; i++) {
//            for (int j = 0; j < numberOfRow; j++) {
//                massivOfPoints[k] = new Point(i, j);
//                k++;
//            }
//        }
//        int positionOfRandom = 0 + (int)(Math.random() * ((8 - 0) + 1));
//        for (int i = 0; i < this.sizeOfSegments; i++) {
//            int[] massivOfRandElems = this.createArray();
//            ImageBMP tempImage = new ImageBMP(this.pathToEncodedContainer + (this.startSegment + i) + this.format);
//            BufferedImage encImage = new BufferedImage(numberOfColumn, numberOfRow , BufferedImage.TYPE_INT_RGB);
//            if (invertCoef == false) {
//                for (int x = 0; x < numberOfColumn; x++) {
//                    for (int y = 0; y < numberOfRow; y++) {
//                        Color pixel = new Color(tempImage.getRGB(x, y).getRed(), tempImage.getRGB(x, y).getGreen(), tempImage.getRGB(x, y).getBlue(), tempImage.getRGB(x, y).getAlpha());
//                        encImage.setRGB(x, y, pixel.getRGB());
//                    }
//                }
//                for (int t = 0; t < numberOfPixels; t++) {
//                    int x = (int) massivOfPoints[massivOfRandElems[t]].getX();
//                    int y = (int) massivOfPoints[massivOfRandElems[t]].getY();
//                    Random random = new Random(System.currentTimeMillis());
//                    int bluePixelValue =  random.nextInt(256);
//                    encImage.setRGB(x, y, new Color(bluePixelValue, bluePixelValue, bluePixelValue, bluePixelValue).getRGB());
//
//                }
//            }
//            try {
//                ImageIO.write(encImage, "bmp", new File(this.pathToResultContainer + ( this.startSegment +  i ) + this.format));
//                //ImageIO.write(encImage, "png", new File("images\\test.png"));
//            } catch (IOException e) {
//                System.out.println("error " + e.getMessage());
//            }
//
//        }
//    }

    private int[] createArray() {
        boolean invertCoef = false;
        if (this.coefOfNoise > 50) {
            invertCoef = true;
        }
        ImageBMP firstImage = new ImageBMP(this.pathToEncodedContainer + startSegment + this.format);
        int numberOfRow = firstImage.getNumberOfRow();
        int numberOfColumn = firstImage.getNumberOfColumn();
        int libSize = numberOfRow * numberOfColumn;
        int countRandTales = 0;
        if (invertCoef == false) {
            countRandTales = (int) ((libSize / 100.0) * coefOfNoise);
        } else {
            countRandTales = (int) ((libSize / 100.0) * (100 - coefOfNoise));
        }
        Random rnd = new Random();
        int[] arr = new int[libSize];
        for (int i = 0; i < libSize; i++) arr[i] = i;
        for (int i = libSize - 1; i > 0; i--) {
            int pos = rnd.nextInt(i + 1);
            int a = arr[pos];
            arr[pos] = arr[i];
            arr[i] = a;
        }
        return Arrays.copyOf(arr, countRandTales);
    }

}
