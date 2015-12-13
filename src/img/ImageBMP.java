package img;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ImageBMP {
    private BufferedImage img = null;

    public ImageBMP(String imgPath) {
        try {
            img = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            System.out.println("Image not founded" + new File(".").getAbsolutePath() + imgPath);
        }
    }

    public ImageBMP(BufferedImage imgContainer){
            img = imgContainer;
    }

    public Color getRGB(int x, int y) {
        return new Color(img.getRGB(x, y));
    }

    public int getNumberOfColumn() {
        return img.getHeight();
    }

    public int getNumberOfRow() {
        return img.getWidth();
    }

    public static double difference(ArrayList<double[][]> one, ArrayList<short[][]> two) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < one.size(); i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    if (max < Math.abs(one.get(i)[j][k] - two.get(i)[j][k]))
                        max = Math.abs(one.get(i)[j][k] - two.get(i)[j][k]);
                }
            }
        }
        return max;
    }

    public static void main(String[] args) {
        //        ImageBMP test = new ImageBMP("images\\Lenna.bmp");
//        ArrayList<short[][]> testBluePixel = DCTMethodImage.imageToListOfBlueSegments(test);
//        ArrayList<double[][]> listOfSpectrCoefOfDCT = DCTMethodImage.calculateSpectrCoefOfDCT(testBluePixel);
//        //DCTMethodImage.insertMassage(listOfSpectrCoefOfDCT, "Green 2015");
//        DCTMethodImage.insertMassage(listOfSpectrCoefOfDCT, "Notepad++ v6.8.3 bug-fixes:\n" +
//                "1.  Fix a crash issue by using wild card (*) to open files on command line.\n" +
//                "2.  Fix the problem of display refresh missing on exit.\n" +
//                "3.  Fix plugin shortcut configuration lost problem by using option -noPlugin.\n" +
//                "4.  Fix Norwegian localization bad display and wrong encoding.\n" +
//                "5.  Fix functionList display problem under high DPI.\n" +
//                "6.  Fix Norwegian localization bad display and wrong encoding.\n" +
//                "Included plugins:\n" +
//                "1.  NppFTP 0.26.3\n" +
//                "2.  NppExport v0.2.8\n" +
//                "3.  Plugin Manager 1.3.5\n" +
//                "Notepad++ v6");
////        System.out.println(DCTMethodImage.decodeMessage(listOfSpectrCoefOfDCT));
//        ArrayList<double[][]> listOfInvertSpectrCoefOfDCT = DCTMethodImage.invertDCT(listOfSpectrCoefOfDCT);
//        double dif = ImageBMP.difference(listOfInvertSpectrCoefOfDCT, testBluePixel);
//        double[][] listOfBluePixels = DCTMethodImage.invertDCTToBluePixelsMassive(listOfInvertSpectrCoefOfDCT, test);
//        DCTMethodImage.encodeImage(test, listOfBluePixels);
//        ImageBMP testDecode = new ImageBMP("images\\test.png");
//        ArrayList<short[][]> testDecodeBluePixel = DCTMethodImage.imageToListOfBlueSegments(testDecode);
//        ArrayList<double[][]> listOfDecodeSpectrCoefOfDCT = DCTMethodImage.calculateSpectrCoefOfDCT(testDecodeBluePixel);
//        System.out.println(DCTMethodImage.decodeMessage(listOfDecodeSpectrCoefOfDCT));
    }

}
