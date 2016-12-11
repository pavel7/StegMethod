import assessmentTests.NoiseGeneratorImage;
import bit.BitComparer;
import img.BinaryImageBMP;
import img.ByteImageBMP;
import img.ImageBMP;
import mathOperations.BitOperations;
import steganographicMethods.DCTMethodImage;
import string.StringComparer;

public class Test {

    public static void main(String[] args) {
        String pathToEmptyContainer = "images\\Lenna.jpg";
        String pathToResultContainer = "images\\EncodedImage.jpg";
        String noiseFilenameOutput = "images\\testq";
        String format = ".jpg";
        double comparisonCoefficient = 120;
        int sizeOfSegments = 8;
        double tr = Math.cos(Math.PI * 8 * (2 * 8 + 1) / (2 * sizeOfSegments));
        String message = "Notepad++ v6.8.3 bug-fixes:\n" +
                "1.  Fix a crash issue by using wild card (*) to open files on command line.\n" +
                "2.  Fix the problem of display refresh missing on exit.\n" +
                "3.  Fix plugin shortcut configuration lost problem by using option -noPlugin.\n" +
                "4.  Fix Norwegian localization bad display and wrong encoding.\n" + "t" +
                "5.  Fix functionList display problem under high DPI.\n"+
                "6.  Fix Norwegian localization bad display and wrong encoding.\n" +
                "Included plugins:\n" +
                "1.  NppFTP 0.26.3\n" +
                "2.  NppExport v0.2";
//                "3.  Plugin Manager 1.3.5\n";// +
                //"Notepad++ v6.";

        DCTMethodImage test = new DCTMethodImage(pathToEmptyContainer, pathToResultContainer, comparisonCoefficient);
        test.setdoNormalizing(false);
//        BinaryImageBMP newB = new BinaryImageBMP("images\\test.png");
//        byte[] testBinary = newB.getByteCodeOfImage();
        ByteImageBMP newC = new ByteImageBMP("images\\test.png");
        short[] tempPix = newC.getByteCodeOfImage();
        byte[] testByte = BitOperations.shortToBit(tempPix);
        test.encodeImage(testByte);
//        byte[] outputByte = test.decodeMessageFromImage(pathToResultContainer);
//        BitComparer temp = new BitComparer(testBinary, outputByte);
//        System.out.println("Value of the error is " + temp.checkBER());
//        newB.saveImageFromByteCode("images\\test"+56,outputByte);
        //byte[] outputByte1 = test.decodeMessageFromImage(pathToResultContainer);
        //newC.saveImageFromByteCode("images\\test1",outputByte);

        for (int i = 30; i < 31; i ++) {
           NoiseGeneratorImage newGenerator = new NoiseGeneratorImage(pathToResultContainer, noiseFilenameOutput, format, i);
            newGenerator.addNoiseFast(255);
            //    newGenerator.addNoise(0);
            byte[] outputByte = test.decodeMessageFromImage(noiseFilenameOutput+i+format);
            short[] symbol = BitOperations.bitToShort(outputByte);
            newC.saveImageFromByteCode("images\\test"+i,symbol);
            BitComparer temp = new BitComparer(testByte, outputByte);
            System.out.println("Volume of noise is " + i + ", value of the error is " + temp.checkBER());
        }
    }
}
