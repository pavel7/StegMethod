package assessmentTests;


import img.ImageBMP;
import img.Pixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NoiseGenerator {
    private String pathToEncodedContainer;
    private String pathToResultContainer;
    private String format;
    private int coefOfNoise;
    private int sizeOfSegments = 8;

    public NoiseGenerator(String inputImage, String outputImage, String newFormat, int newCoefOfNoise) {
        pathToEncodedContainer = inputImage;
        pathToResultContainer = outputImage;
        coefOfNoise = newCoefOfNoise;
        format = newFormat;
    }

    public void addNoise(int bluePixelValue) {
        boolean invertCoef = false;
        if (this.coefOfNoise > 50){
            invertCoef = true;
        }
        ImageBMP firstImage = new ImageBMP(this.pathToEncodedContainer + "0" + this.format);
        int numberOfRow = firstImage.getNumberOfRow();
        int numberOfColumn = firstImage.getNumberOfColumn();
        int numberOfPixels = 0;
        if (invertCoef == false) {
            numberOfPixels = (int) ((numberOfRow * numberOfColumn / 100.0) * coefOfNoise);
        }
        else {
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
            BufferedImage encImage = new BufferedImage(numberOfRow, numberOfColumn, BufferedImage.TYPE_INT_RGB);
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
            }
            else {
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
}
