import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import assessmentTests.NoiseGenerator;
import com.xuggle.xuggler.*;
import img.ImageBMP;
import steganographicMethods.DCTMethodVideo;
import string.StringComparer;

public class TestVideo {

    public static void main(String a[]) throws Exception {
        String format = ".bmp";
        String filenameInput = "video\\IMG_0065.mp4";
        String filenameOutput = "video\\EncodedPictures\\EncodedImage";
        String noiseFilenameOutput = "video\\NoisesEncodedPictures\\EncodedImage";
        DCTMethodVideo test = new DCTMethodVideo(filenameInput, filenameOutput, 40);
        //test.videoToListOfImages();
//        test.setNumberOfPicturesAll(8);
//        test.insertText("1.  Fix a crash issue by using wild card (*) to open files on command line.\n" +
//                "2.  Fix the problem of display refresh missing on exit.\n" +
//                "3.  Fix plugin shortcut configuration lost problem by using option -noPlugin.\n" +
//                "4.  Fix Norwegian localization bad display and wrong encoding.\n" +
//                "5.  Fix functionList display problem under high DPI.\n" +
//                "6.  Fix Norwegian localization bad display and wrong encoding.\n" +
//                "Included plugins:\n" +
//                "1.  NppFTP 0.26.3\n" +
//                "2.  NppExport v0.2.8\n" +
//                "3.  Plugin Manager 1.3.5\n" +
//                "Notepad++ v6\n" +
//                "3.  Plugin Manager 1.3.5\n");

        String firstString = test.decodeMessageFromImage(0, filenameOutput, format);
        System.out.println(firstString);
        System.out.println("Test result:");
        NoiseGenerator newGenerator = new NoiseGenerator(filenameOutput, noiseFilenameOutput, format, 100);
        newGenerator.addNoise(0);
        String secondString = test.decodeMessageFromImage(0, noiseFilenameOutput, format);
        System.out.println(secondString);
        StringComparer difValue = new StringComparer(firstString, secondString);
        System.out.println("Volume of noise is " + 95 + ", value of the error is " + difValue.checkDiffereneceByBit());
//        for (int i = 0; i < 99; i = i + 10) {
//            NoiseGenerator newGenerator = new NoiseGenerator(filenameOutput, noiseFilenameOutput, format, i);
//            newGenerator.addNoise(0);
//            String secondString = test.decodeMessageFromImage(0, noiseFilenameOutput, format);
//            System.out.println(secondString);
//            StringComparer difValue = new StringComparer(firstString, secondString);
//            System.out.println("Volume of noise is " + i + ", value of the error is " + difValue.checkDiffereneceByBit());
////        }


            //test.videoToListOfImages();
            //ArrayList<short[][][]> temp2 = test.ListOfImagesToListOfBlueSegments(temp1);

//        }
    }
}