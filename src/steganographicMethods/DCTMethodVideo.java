package steganographicMethods;

import com.xuggle.mediatool.event.ARawMediaMixin;
import com.xuggle.xuggler.*;
import img.ImageBMP;
import mathOperations.BitOperations;
import mathOperations.VideoDCT;
import mathOperations.VideoInvertDCT;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ����� on 04.11.2015.
 */
public class DCTMethodVideo {
    private String pathToEmptyContainer;
    private String pathToResultContainer;
    private int sizeOfSegments = 8;
    private double p = 0.25;
    private int t1 = 4;
    private int u1 = 3;
    private int v1 = 4;
    private int t2 = 3;
    private int u2 = 4;
    private int v2 = 3;
    private boolean doNormalizing = true;
    private int numberOfPicturesAll = 0;
    private int numberofRowAll = 0;
    private int numberofColumnAll = 0;

    public DCTMethodVideo(String inputImage, String outputImage, int comparisonCoefficient) {
        pathToEmptyContainer = inputImage;
        pathToResultContainer = outputImage;
        p = comparisonCoefficient;
    }

    public DCTMethodVideo(String inputImage, String outputImage, int comparisonCoefficient, int newSizeOfSegments) {
        pathToEmptyContainer = inputImage;
        pathToResultContainer = outputImage;
        p = comparisonCoefficient;
        sizeOfSegments = newSizeOfSegments;
    }

    public void setNumberOfPicturesAll(int newSize) {
        numberOfPicturesAll = newSize;
    }

    public int getNumberOfPicturesAll() {
        return numberOfPicturesAll;
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

    public void setComparisonCoefficient(double comparisonCoefficient) {
        p = comparisonCoefficient;
    }

    public double getComparisonCoefficient() {
        return p;
    }

//    public ArrayList<ArrayList<short[][]>> videoToListOfBlueSegments() throws IOException {
//        String filename = pathToEmptyContainer;
//        ArrayList<ArrayList<short[][]>> listOfSegments = new ArrayList<>();
//        File outdir = new File("video\\pictures");
//        IContainer container = IContainer.make();
//
//        if (container.open(filename, IContainer.Type.READ, null) < 0)
//            throw new IllegalArgumentException("could not open file: "
//                    + filename);
//        int numStreams = container.getNumStreams();
//        int videoStreamId = -1;
//        IStreamCoder videoCoder = null;
//
//        for (int i = 0; i < numStreams; i++) {
//            IStream stream = container.getStream(i);
//            IStreamCoder coder = stream.getStreamCoder();
//            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
//                videoStreamId = i;
//                videoCoder = coder;
//                break;
//            }
//        }
//
//        if (videoStreamId == -1)
//            throw new RuntimeException("could not find video stream in container: "
//                    + filename);
//
//        if (videoCoder.open() < 0)
//            throw new RuntimeException(
//                    "could not open video decoder for container: " + filename);
//
//        IPacket packet = IPacket.make();
//
//        long start = 0;//6 * 1000 * 1000;
//        long end = container.getDuration();
//        ;
//
//        long step = (1 * 1000 * 1000) / videoCoder.getFrameRate().getNumerator();
//
////        END:
//        int numberOfFrames = 0;
//        while (container.readNextPacket(packet) >= 0) {
//            if (packet.getStreamIndex() == videoStreamId) {
//                IVideoPicture picture = IVideoPicture.make(
//                        videoCoder.getPixelType(), videoCoder.getWidth(),
//                        videoCoder.getHeight());
//                int offset = 0;
//                while (offset < packet.getSize()) {
//                    int bytesDecoded = videoCoder.decodeVideo(picture, packet,
//                            offset);
//
//                    if (bytesDecoded < 0)
//                        throw new RuntimeException("got error decoding video in: "
//                                + filename);
//                    offset += bytesDecoded;
//
//                    if (picture.isComplete()) {
//                        IVideoPicture newPic = picture;
//                        // � �������������
//                        long timestamp = picture.getTimeStamp();
//                        if (timestamp > start) {
//                            // �������� ����������� BufferedImage
//                            BufferedImage javaImage = Utils
//                                    .videoPictureToImage(newPic);
//                            String fileName = numberOfFrames + ".png";
//                            ImageIO.write(javaImage, "PNG", new File(outdir,
//                                    fileName));
//                            numberOfFrames++;
//                            ImageBMP imageInput = new ImageBMP(outdir + "\\" + fileName);
//                            listOfSegments.add(imageToListOfBlueSegments(imageInput));
//                            start += step;
//                        }
////                        if (timestamp > end) {
////                            break END;
////                        }
//                    }
//                }
//            }
//        }
//        if (videoCoder != null) {
//            videoCoder.close();
//            videoCoder = null;
//        }
//        if (container != null) {
//            container.close();
//            container = null;
//        }
//        numberOfPictures = numberOfFrames;
//        while (numberOfPictures % sizeOfSegments != 0) {
//            listOfSegments.remove(numberOfPictures - 1);
//            numberOfFrames = numberOfFrames - 1;
//        }
//        return listOfSegments;
//    }

    public void videoToListOfImages() throws IOException {
        String filename = pathToEmptyContainer;
        //ArrayList<short[][]> listOfSegments = new ArrayList<>();
        File outdir = new File("video\\pictures");
        IContainer container = IContainer.make();

        if (container.open(filename, IContainer.Type.READ, null) < 0)
            throw new IllegalArgumentException("could not open file: "
                    + filename);
        int numStreams = container.getNumStreams();
        int videoStreamId = -1;
        IStreamCoder videoCoder = null;

        for (int i = 0; i < numStreams; i++) {
            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();
            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                videoStreamId = i;
                videoCoder = coder;
                break;
            }
        }

        if (videoStreamId == -1)
            throw new RuntimeException("could not find video stream in container: "
                    + filename);

        if (videoCoder.open() < 0)
            throw new RuntimeException(
                    "could not open video decoder for container: " + filename);

        IPacket packet = IPacket.make();

        long start = 0;//6 * 1000 * 1000;
        long end = container.getDuration();
        ;

        long step = (1 * 1000 * 1000) / videoCoder.getFrameRate().getNumerator();

//        END:
        int numberOfFrames = 0;
        while (container.readNextPacket(packet) >= 0) {
            if (packet.getStreamIndex() == videoStreamId) {
                IVideoPicture picture = IVideoPicture.make(
                        videoCoder.getPixelType(), videoCoder.getWidth(),
                        videoCoder.getHeight());
                int offset = 0;
                while (offset < packet.getSize()) {
                    int bytesDecoded = videoCoder.decodeVideo(picture, packet,
                            offset);

                    if (bytesDecoded < 0)
                        throw new RuntimeException("got error decoding video in: "
                                + filename);
                    offset += bytesDecoded;

                    if (picture.isComplete()) {
                        IVideoPicture newPic = picture;
                        long timestamp = picture.getTimeStamp();
                        if (timestamp > start) {
                            BufferedImage javaImage = Utils
                                    .videoPictureToImage(newPic);
                            String fileName = numberOfFrames + ".png";
                            ImageIO.write(javaImage, "PNG", new File(outdir,
                                    fileName));
                            numberOfFrames++;
//                            ImageBMP imageInput = new ImageBMP(javaImage);
//                            numberofRowAll = imageInput.getNumberOfRow();
//                            numberofColumnAll = imageInput.getNumberOfColumn();
//                            short[][] pixelBlue = new short[numberofRowAll][numberofColumnAll];
//                            for (int i = 0; i < numberofRowAll; i++)
//                                for (int j = 0; j < numberofColumnAll; j++)
//                                    pixelBlue[i][j] = (short) imageInput.getRGB(i, j).getBlue();
//                            listOfSegments.add(pixelBlue);
                            start += step;
                        }
//                        if (timestamp > end) {
//                            break END;
//                        }
                    }
                }
            }
        }
        if (videoCoder != null) {
            videoCoder.close();
            videoCoder = null;
        }
        if (container != null) {
            container.close();
            container = null;
        }
        numberOfPicturesAll = numberOfFrames;
//        while (numberOfFrames % sizeOfSegments != 0) {
//            listOfSegments.remove(numberOfFrames - 1);
//            numberOfFrames = numberOfFrames - 1;
//        }
        //return listOfSegments;
    }

    public void insertText(String message) throws Exception {
        int messageLength = message.length();
        short[] str2vec = new short[messageLength];
        ImageBMP firstImage = new ImageBMP("video\\pictures\\1.png");
        int numberOfRow = firstImage.getNumberOfRow();
        int numberOfColumn = firstImage.getNumberOfColumn();
        int numberOfImages = numberOfPicturesAll;
        if (numberOfRow % sizeOfSegments != 0)
            numberOfRow = numberOfRow - numberOfRow % sizeOfSegments;
        if (numberOfColumn % sizeOfSegments != 0)
            numberOfColumn = numberOfColumn - numberOfColumn % sizeOfSegments;
        if (numberOfImages % sizeOfSegments != 0)
            numberOfImages = numberOfImages - numberOfImages % sizeOfSegments;
        int numberOfSegments = numberOfColumn * numberOfRow * numberOfImages / (sizeOfSegments * sizeOfSegments * sizeOfSegments);
        if (numberOfSegments < messageLength)
            throw (new Exception("String is too long"));
        //for (int i = 0; i < numberOfImages; i++) {
        ArrayList<short[][][]> listOfBlueSegment = getListOfBlueSegmentsAccordingSegmentSize(0, "video\\pictures\\", ".png", numberOfRow, numberOfColumn);
        int threadsNum = new Double(Math.ceil(Runtime.getRuntime().availableProcessors())).intValue();
        int sizeOfListOfBlueSegment = listOfBlueSegment.size();
        VideoDCT[] listOfDCT = new VideoDCT[threadsNum];
        for (int k = 0; k < threadsNum; k++) {
            if (k != threadsNum - 1)
                listOfDCT[k] = new VideoDCT(listOfBlueSegment, k * (sizeOfListOfBlueSegment / threadsNum), (k + 1) * (sizeOfListOfBlueSegment / threadsNum));
            else {
                listOfDCT[k] = new VideoDCT(listOfBlueSegment, k * (sizeOfListOfBlueSegment / threadsNum), sizeOfListOfBlueSegment);
            }
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<ArrayList<double[][][]>>> futuresDCT = new ArrayList<>();
        for (int k = 0; k < threadsNum; k++) {
            futuresDCT.add(executorService.submit(listOfDCT[k]));
        }
        ArrayList<double[][][]> coefOfDCT = new ArrayList<>();
        for (int k = 0; k < threadsNum; k++) {
            ArrayList<double[][][]> temp = futuresDCT.get(k).get();
            for (int i = 0; i < temp.size(); i++) {
                coefOfDCT.add(temp.get(i).clone());
            }

        }

        insertMassage(coefOfDCT, message);

        VideoInvertDCT[] listOfInvertDCT = new VideoInvertDCT[threadsNum];
        for (int k = 0; k < threadsNum; k++) {
            if (k != threadsNum - 1)
                listOfInvertDCT[k] = new VideoInvertDCT(coefOfDCT, k * (sizeOfListOfBlueSegment / threadsNum), (k + 1) * (sizeOfListOfBlueSegment / threadsNum));
            else {
                listOfInvertDCT[k] = new VideoInvertDCT(coefOfDCT, k * (sizeOfListOfBlueSegment / threadsNum), sizeOfListOfBlueSegment);
            }
        }

        ArrayList<Future<ArrayList<double[][][]>>> futuresInvertDCT = new ArrayList<>();
        for (int k = 0; k < threadsNum; k++) {
            futuresInvertDCT.add(executorService.submit(listOfInvertDCT[k]));
        }
        ArrayList<double[][][]> encodedSegments = new ArrayList<>();
        for (int k = 0; k < threadsNum; k++) {
            ArrayList<double[][][]> temp = futuresInvertDCT.get(k).get();
            for (int i = 0; i < temp.size(); i++) {
                encodedSegments.add(temp.get(i).clone());
            }
        }
        executorService.shutdown();
        double[][][] resultedBluePixels = invertDCTToBluePixelsMassive(encodedSegments, numberOfRow, numberOfColumn);
        for (int imageNumber = 0; imageNumber < sizeOfSegments; imageNumber++) {
            normFunction(resultedBluePixels, imageNumber, numberOfRow, numberOfColumn);
            encodeImage(numberOfRow, numberOfColumn, resultedBluePixels, imageNumber);
        }
        System.out.print("e");

        //}

    }

    public String decodeMessageFromImage(int numberOfImages, String inputImage, String newFormat) throws ExecutionException, InterruptedException {
        ImageBMP imageDecode = new ImageBMP(inputImage + numberOfImages + newFormat);
        ArrayList<short[][][]> listOfBlueSegment = getListOfBlueSegmentsAccordingSegmentSize(numberOfImages, inputImage, newFormat, imageDecode.getNumberOfRow(), imageDecode.getNumberOfColumn());
        int threadsNum = new Double(Math.ceil(Runtime.getRuntime().availableProcessors())).intValue();
        int sizeOfListOfBlueSegment = listOfBlueSegment.size();
        VideoDCT[] listOfDCT = new VideoDCT[threadsNum];
        for (int k = 0; k < threadsNum; k++) {
            if (k != threadsNum - 1)
                listOfDCT[k] = new VideoDCT(listOfBlueSegment, k * (sizeOfListOfBlueSegment / threadsNum), (k + 1) * (sizeOfListOfBlueSegment / threadsNum));
            else {
                listOfDCT[k] = new VideoDCT(listOfBlueSegment, k * (sizeOfListOfBlueSegment / threadsNum), sizeOfListOfBlueSegment);
            }
        }
        System.out.println("start");
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<ArrayList<double[][][]>>> futuresDCT = new ArrayList<>();
        for (int k = 0; k < threadsNum; k++) {
            futuresDCT.add(executorService.submit(listOfDCT[k]));
        }
        ArrayList<double[][][]> listOfDecodeSpectrCoefOfDCT = new ArrayList<>();
        for (int k = 0; k < threadsNum; k++) {
            ArrayList<double[][][]> temp = futuresDCT.get(k).get();
            for (int i = 0; i < temp.size(); i++) {
                listOfDecodeSpectrCoefOfDCT.add(temp.get(i).clone());
            }
        }
        executorService.shutdown();
        System.out.println("finish");
        return this.decodeMessage(listOfDecodeSpectrCoefOfDCT);

    }

    public ArrayList<short[][][]> getListOfBlueSegmentsAccordingSegmentSize(int numberOfImage, String path, String format, int rowSize, int columnSize) {
        ArrayList<short[][][]> sizeOfSegmentsPisctures = new ArrayList<>(rowSize * columnSize * sizeOfSegments);
        short[][][] matrixOfBluePixels = new short[sizeOfSegments][rowSize][columnSize];
        for (int i = numberOfImage, j = 0; i < numberOfImage + sizeOfSegments; i++, j++) {
            ImageBMP firstImage = new ImageBMP(path + i + format);
            for (int x = 0; x < rowSize; x++)
                for (int y = 0; y < columnSize; y++) {
                    matrixOfBluePixels[j][x][y] = (short) firstImage.getRGB(x, y).getBlue();
                }
        }
        int numberOfSegments = (sizeOfSegments * rowSize * columnSize) / (sizeOfSegments * sizeOfSegments * sizeOfSegments);
        int indexOfStartPicture = 0;
        int indexOfEndPicture = sizeOfSegments - 1;
        int indexOfStartColumn = 0;
        int indexOfEndColumn = sizeOfSegments - 1;
        for (int i = 0; i < numberOfSegments; i++) {
            int indexOfStartRow = (sizeOfSegments * i) % rowSize;
            int indexOfEndRow = indexOfStartRow + sizeOfSegments - 1;
            short[][][] tempMassive = submatrix(matrixOfBluePixels, indexOfStartPicture, indexOfEndPicture, indexOfStartRow, indexOfEndRow, indexOfStartColumn, indexOfEndColumn);
            if (indexOfEndRow == rowSize - 1) {
                indexOfStartColumn = indexOfStartColumn + sizeOfSegments;
                indexOfEndColumn = indexOfEndColumn + sizeOfSegments;
            }
//            if ((indexOfEndRow == rowSize - 1) && (indexOfEndColumn == columnSize - 1)) {
//                indexOfStartColumn = 0;
//                indexOfEndColumn = sizeOfSegments - 1;
//                indexOfStartPicture = indexOfStartPicture + sizeOfSegments;
//                indexOfEndPicture = indexOfEndPicture + sizeOfSegments;
//            }
            sizeOfSegmentsPisctures.add(tempMassive);
        }
        return sizeOfSegmentsPisctures;
    }

    private void insertMassage(ArrayList<double[][][]> spectrCoefOfDCT, String message) {
        int messageLength = message.length();
        //ArrayList<double[][]> listOfSpectrCoefOfDCT = new ArrayList();
        short[] str2vec = new short[messageLength];
        for (int i = 0; i < messageLength; i++) {
            str2vec[i] = (short) message.charAt(i);
        }
        for (int i = 0; i < messageLength; i++) {
            byte[] symbolInDec = BitOperations.decToBit(str2vec[i]);
            for (int j = 0; j < 8; j++) {
                double[][][] tempSpectrCoef = spectrCoefOfDCT.get(j + sizeOfSegments * i);
                double w1 = Math.abs(tempSpectrCoef[t1][u1][v1]);
                double w2 = Math.abs(tempSpectrCoef[t2][u2][v2]);
                int z1 = 0;
                int z2 = 0;
                if (tempSpectrCoef[t1][u1][v1] >= 0) {
                    z1 = 1;
                } else {
                    z1 = -1;
                }
                if (tempSpectrCoef[t2][u2][v2] >= 0) {
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
                spectrCoefOfDCT.get(j + sizeOfSegments * i)[t1][u1][v1] = z1 * w1;
                spectrCoefOfDCT.get(j + sizeOfSegments * i)[t2][u2][v2] = z2 * w2;
            }
        }
    }


    private double[][][] invertDCTToBluePixelsMassive(ArrayList<double[][][]> listOfInvertSpectrCoefOfDCT, int rowSize, int columnSize) {
        double[][][] resultMassive = new double[sizeOfSegments][rowSize][columnSize];
        int numberOfSegments = (sizeOfSegments * rowSize * columnSize) / (sizeOfSegments * sizeOfSegments * sizeOfSegments);
        int segmentsInRow = rowSize / sizeOfSegments;
        int indexX = 0;
        int indexY = 0;
        for (int i = 0; i < numberOfSegments; i++) {
            if (indexX == segmentsInRow) {
                indexX = 0;
                indexY++;
            }
            for (int t = 0; t < sizeOfSegments; t++) {
                for (int j = 0; j < sizeOfSegments; j++) {
                    for (int k = 0; k < sizeOfSegments; k++) {
                        try {
                            resultMassive[t][indexX * sizeOfSegments + j][indexY * sizeOfSegments + k] = listOfInvertSpectrCoefOfDCT.get(i)[t][j][k];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.print("q");
                        }
                    }
                }
            }
            indexX++;
        }
//        numberOfRow = emptyContainer.getNumberOfRow();
//        numberOfColumn = emptyContainer.getNumberOfColumn();
//        if (numberOfRow % sizeOfSegments != 0) {
//            for (int i = numberOfRow - numberOfRow % sizeOfSegments; i < numberOfRow; i++)
//                for (int j = 0; j < numberOfColumn; j++)
//                    resultMassive[i][j] = emptyContainer.getRGB(i, j).getBlue();
//        }
//        if (numberOfColumn % sizeOfSegments != 0) {
//            for (int i = 0; i < numberOfRow; i++)
//                for (int j = numberOfColumn - numberOfColumn % sizeOfSegments; j < numberOfColumn; j++)
//                    resultMassive[i][j] = emptyContainer.getRGB(i, j).getBlue();
//        }
//        if (doNormalizing) {
//            normFunction(resultMassive, numberOfRow, numberOfColumn);
//        }
        return resultMassive;
    }

    private void encodeImage(int numberOfRow, int numberOfColumn, double[][][] bluePixels, int numberOfImg) {
//        if (numberOfRow % sizeOfSegments != 0)
//            numberOfRow = numberOfRow - numberOfRow % sizeOfSegments;
//        if (numberOfColumn % sizeOfSegments != 0)
//            numberOfColumn = numberOfColumn - numberOfColumn % sizeOfSegments;
        ImageBMP emptyContainer = new ImageBMP("video\\pictures\\" + numberOfImg + ".png");
        BufferedImage encImage = new BufferedImage(numberOfRow, numberOfColumn, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < numberOfRow; x++) {
            for (int y = 0; y < numberOfColumn; y++) {
                Color pixel = new Color(emptyContainer.getRGB(x, y).getRed(), emptyContainer.getRGB(x, y).getGreen(), (int) Math.round(bluePixels[numberOfImg][x][y]), emptyContainer.getRGB(x, y).getAlpha());
                encImage.setRGB(x, y, pixel.getRGB());
            }
        }
        try {
            ImageIO.write(encImage, "bmp", new File(this.pathToResultContainer + numberOfImg + ".bmp"));
            //ImageIO.write(encImage, "png", new File("images\\test.png"));
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }

    }


    private short[][][] submatrix(short[][][] pixelMatrix, int indexOfStartPicture, int indexOfEndPicture, int indexOfStartRow, int indexOfEndRow, int indexOfStartColumn, int indexOfEndColumn) {
        short[][][] tempMassive = new short[indexOfEndPicture - indexOfStartPicture + 1][indexOfEndRow - indexOfStartRow + 1][indexOfEndColumn - indexOfStartColumn + 1];
        for (int t = indexOfStartPicture, n = 0; t <= indexOfEndPicture; t++, n++)
            for (int i = indexOfStartRow, k = 0; i <= indexOfEndRow; i++, k++)
                for (int j = indexOfStartColumn, l = 0; j <= indexOfEndColumn; j++, l++)
                    tempMassive[n][k][l] = pixelMatrix[t][i][j];

        return tempMassive;
    }

    private void normFunction(double[][][] input,int numberOfImg, int numberOfRow, int numberOfColumn) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                if (input[numberOfImg][i][j] > max) max = input[numberOfImg][i][j];
                if (input[numberOfImg][i][j] < min) min = input[numberOfImg][i][j];
            }
        }
        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                input[numberOfImg][i][j] = 255 * (input[numberOfImg][i][j] + Math.abs(min)) / (max + Math.abs(min));
            }
        }
    }

    private String decodeMessage(ArrayList<double[][][]> spectrCoefOfDCT) {
        int i = 0;
        int j = 0;
        char[] decodeMessage = new char[spectrCoefOfDCT.size() / 8];
        byte[] temp = new byte[8];
        for (int k = 0; k < spectrCoefOfDCT.size(); k++) {
            double w1 = Math.abs(spectrCoefOfDCT.get(k)[t1][u1][v1]);
            double w2 = Math.abs(spectrCoefOfDCT.get(k)[t2][u2][v2]);
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