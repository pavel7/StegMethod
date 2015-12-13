import steganographicMethods.DCTMethodImage;

public class Test {

    public static void main(String[] args) {
        String pathToEmptyContainer = "images\\Lenna.bmp";
        String pathToResultContainer = "images\\EncodedImage.bmp";
        int comparisonCoefficient = 25;
        String message = "Notepad++ v6.8.3 bug-fixes:\n" +
                "1.  Fix a crash issue by using wild card (*) to open files on command line.\n" +
                "2.  Fix the problem of display refresh missing on exit.\n" +
                "3.  Fix plugin shortcut configuration lost problem by using option -noPlugin.\n" +
                "4.  Fix Norwegian localization bad display and wrong encoding.\n" + "t";
//                "5.  Fix functionList display problem under high DPI.\n" +
//                "6.  Fix Norwegian localization bad display and wrong encoding.\n" +
//                "Included plugins:\n" +
//                "1.  NppFTP 0.26.3\n" +
//                "2.  NppExport v0.2.8\n" +
//                "3.  Plugin Manager 1.3.5\n" +
//                "Notepad++ v6.";

        DCTMethodImage test = new DCTMethodImage(pathToEmptyContainer, pathToResultContainer, comparisonCoefficient);
        //test.setdoNormalizing(false);
        test.encodeImage(message);
        test.decodeMessageFromImage(pathToResultContainer);
    }
}
