package img;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BinaryImageBMP {
    private BufferedImage img = null;

    public BinaryImageBMP(String imgPath) {
        try {
            img = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            System.out.println("Image not founded" + new File(".").getAbsolutePath() + imgPath);
        }
    }

    public BinaryImageBMP(BufferedImage imgContainer) {
        img = imgContainer;
    }

    private Color getRGB(int x, int y) {
        return new Color(img.getRGB(x, y));
    }

    public byte getColor(int x, int y) {
        Color pixel = new Color(img.getRGB(x, y));
        int temp = pixel.getBlue() + pixel.getGreen() + pixel.getRed();
        byte result;
        if (temp == 765)
            result = 0;
        else
            result = 1;
        return result;
    }

    public int getNumberOfColumn() {
        return img.getWidth();
    }

    public int getNumberOfRow() {
        return img.getHeight();
    }

    public byte[] getByteCodeOfImage() {
        byte[] result = new byte[this.getNumberOfColumn() * this.getNumberOfRow()];
        int k = 0;
        for (int i = 0; i < this.getNumberOfColumn(); i++) {
            for (int j = 0; j < this.getNumberOfRow(); j++) {
                result[k] = this.getColor(i, j);
                k++;
            }
        }
        return result;
    }

    public void saveImageFromByteCode (String path, byte[] image){
        int numberOfRow = this.getNumberOfRow();
        int numberOfColumn = this.getNumberOfColumn();
        BufferedImage encImage = new BufferedImage(numberOfColumn, numberOfRow, BufferedImage.TYPE_3BYTE_BGR);
        int k = 0;
        for (int x = 0; x < numberOfColumn; x++) {
            for (int y = 0; y < numberOfRow; y++) {
                if (image[k] > 0) {
                    Color pixel = new Color(0,0,0);
                    encImage.setRGB(x, y, pixel.getRGB());
                } else {
                    Color pixel = new Color(255,255,255);
                    encImage.setRGB(x, y, pixel.getRGB());
                }
                k++;
            }
        }
        try {
            ImageIO.write(encImage, "bmp", new File(path + ".bmp"));
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }

    }

        public static void main (String[] args){
//        BinaryImageBMP newC = new BinaryImageBMP("images\\C.bmp");
//        for (int i = 0; i < newC.getNumberOfRow(); i++) {
//            for (int j = 0; j < newC.getNumberOfColumn(); j++) {
//                System.out.print(newC.getColor(i, j));
//            }
//            System.out.println();
//        }
//
//        BufferedImage encImage = new BufferedImage(newC.getNumberOfRow(), newC.getNumberOfColumn(), BufferedImage.TYPE_INT_RGB);
//        for (int x = 0; x < newC.getNumberOfRow(); x++) {
//            for (int y = 0; y < newC.getNumberOfColumn(); y++) {
//                if (newC.getColor(x, y) > 0) {
//                    Color pixel = new Color(0,0,0);
//                    encImage.setRGB(x, y, pixel.getRGB());
//                } else {
//                    Color pixel = new Color(255,255,255);
//                    encImage.setRGB(x, y, pixel.getRGB());
//                }
//            }
//        }
//        try {
//            ImageIO.write(encImage, "bmp", new File("images" + File.separator + "test" + ".bmp"));
//            //ImageIO.write(encImage, "png", new File("images" + File.separator + "test.png"));
//        } catch (IOException e) {
//            System.out.println("error " + e.getMessage());
//        }

        }
    }
