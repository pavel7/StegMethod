import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import assessmentTests.NoiseGenerator;
import bit.BitComparer;
import com.xuggle.xuggler.*;
import img.BinaryImageBMP;
import img.ByteImageBMP;
import img.Hologram;
import img.ImageBMP;
import mathOperations.BitOperations;
import steganographicMethods.DCTMethodVideo;
import string.StringComparer;

public class TestVideo {

    public static void main(String a[]) throws Exception {
        String format = ".jpg";
        String filenameInput = "video" + File.separator + "IMG_0065.mp4";
        String filenameOutput = "video" + File.separator + "EncodedPictures" + File.separator + "EncodedImage";
        String noiseFilenameOutput = "video" + File.separator + "NoisesEncodedPictures" + File.separator + "EncodedImage";
        DCTMethodVideo test = new DCTMethodVideo(filenameInput, filenameOutput, 170);
        //test.videoToListOfImages();
        test.setNumberOfPicturesAll(8);
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
//                "3.  Plugin Manager 1.3.5123\n");
        String copyrightSymbol = "images" + File.separator + "test1.bmp";
        String hologramOfCopyrightSymbol = "images" + File.separator + "hologramOfNewBMP";
        String pathToPartsOfCopyrightSymbol = "images" + File.separator + "dividedIM" + File.separator;
        Hologram.useHologram(copyrightSymbol, hologramOfCopyrightSymbol, 128.0, 3, 128, 128);
        //Hologram.useHologram(hologramOfCopyrightSymbol + ".bmp", "images\\testFromHologram", 0.0, 3, 128, 128);
        ByteImageBMP newCopyright = new ByteImageBMP(hologramOfCopyrightSymbol+".bmp");
        //ByteImageBMP newCopyright = new ByteImageBMP(copyrightSymbol);

        int numberOfSegments = newCopyright.divideImageOnPart(32, 16, pathToPartsOfCopyrightSymbol);
        int startSegment = 0;

        for (int i = 0; i < numberOfSegments; i++) {
            startSegment = 8 * i;
            ByteImageBMP newCopyrightPart = new ByteImageBMP(pathToPartsOfCopyrightSymbol + i + ".bmp");
            short[] tempPix = newCopyrightPart.getByteCodeOfImage();
            byte[] testByte = BitOperations.shortToBit(tempPix);
//        BinaryImageBMP newC = new BinaryImageBMP("images\\test.bmp");
//        byte[] testByte = newC.getByteCodeOfImage();
            test.insertByteCode(testByte, startSegment);
        }
//
//
//
//        for (int i = 0; i < 9; i = i + 10) {
//            for (int j = 0; j < numberOfSegments; j++) {
//                startSegment = 8*j;
//                NoiseGenerator newGenerator = new NoiseGenerator(filenameOutput, noiseFilenameOutput, format, i, startSegment);
//                newGenerator.addNoiseFast();
//                ByteImageBMP newCopyrightPart = new ByteImageBMP(pathToPartsOfCopyrightSymbol + j + ".bmp");
//                byte[] outputByte = test.decodeByteCodeFromImage(startSegment, noiseFilenameOutput, format);
//                newCopyrightPart.saveImageFromByteCode("images\\test\\" + i + "-" + j, BitOperations.bitToShort(outputByte));
//            }
//        }
        //int startSegmentEncode = 0;
        for (int i = 0; i < 90; i = i + 10) {
            int startSegmentEncode = 0;
            NoiseGenerator newGenerator = new NoiseGenerator(filenameOutput, noiseFilenameOutput, format, i, startSegmentEncode);
            newGenerator.makeEmpty(8*numberOfSegments);
            for (int j = 0; j < numberOfSegments; j++) {
                startSegment = 8*j;
                //NoiseGenerator newGenerator = new NoiseGenerator(filenameOutput, noiseFilenameOutput, format, i, startSegment);
                //newGenerator.addNoiseFast();
                ByteImageBMP newCopyrightPart = new ByteImageBMP(pathToPartsOfCopyrightSymbol + j + ".bmp");
                byte[] outputByte = test.decodeByteCodeFromImage(startSegment, noiseFilenameOutput, format);
                newCopyrightPart.saveImageFromByteCode("images" + File.separator + "test" + File.separator + i + "-" + j, BitOperations.bitToShort(outputByte));
            }
        }

//        for (int j = 0; j < numberOfSegments; j++) {
//            startSegment = 8*j;
//            NoiseGenerator newGenerator = new NoiseGenerator(filenameOutput, noiseFilenameOutput, format, 0, startSegment);
//            newGenerator.addNoiseFast();
//            ByteImageBMP newCopyrightPart = new ByteImageBMP(pathToPartsOfCopyrightSymbol + j + ".bmp");
//            byte[] outputByte = test.decodeByteCodeFromImage(startSegment, noiseFilenameOutput, format);
//            newCopyrightPart.saveImageFromByteCode("images\\test\\" + 0 + "-" + j, BitOperations.bitToShort(outputByte));
//        }


            //byte[] outputByte = test.decodeByteCodeFromImage(0, filenameOutput, format);
            //newC.saveImageFromByteCode("images\\test123",BitOperations.bitToShort(outputByte));
//        BitComparer temp = new BitComparer(testByte, outputByte);
//        System.out.println(temp.checkDifferenece());
//        String firstString = test.decodeMessageFromImage(0, filenameOutput, format);
//        System.out.println(firstString);
//        System.out.println("Test result:");
//        NoiseGenerator newGenerator = new NoiseGenerator(filenameOutput, noiseFilenameOutput, format, 100);
//        newGenerator.addNoise(0);
//        String secondString = test.decodeMessageFromImage(0, noiseFilenameOutput, format);
//        System.out.println(secondString);
//        StringComparer difValue = new StringComparer(firstString, secondString);
//        System.out.println("Volume of noise is " + 95 + ", value of the error is " + difValue.checkDiffereneceByBit());
//            byte[] outputByte = test.decodeByteCodeFromImage(startSegment, filenameOutput, format);
//            newC.saveImageFromByteCode("images\\testqq" + j, BitOperations.bitToShort(outputByte));
//            for (int i = 99; i < 99; i = i + 10) {
//                NoiseGenerator newGenerator = new NoiseGenerator(filenameOutput, noiseFilenameOutput, format, i, startSegment);
//                newGenerator.addNoiseFast(255);
//                //    newGenerator.addNoise(0);
//                byte[] outputByte = test.decodeByteCodeFromImage(0, noiseFilenameOutput, format);
//                newC.saveImageFromByteCode("images\\test" + i, BitOperations.bitToShort(outputByte));
//                BitComparer temp = new BitComparer(testByte, outputByte);
//                System.out.println("Volume of noise is " + i + ", value of the error is " + temp.checkBER());
//            }
        }
}
