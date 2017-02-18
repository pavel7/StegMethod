package video;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by pavelcherepanov on 16.02.17.
 */
public class VideoUtils {
    //private String pathToEmptyContainer;
    //private String pathToOutputFolderForImages = "video" + File.separator + "pictures" + File.separator;
    //private String pathToEncodeVideo;
    //private int numberOfPicturesAll = 0;
    private final static int typeOfImage = BufferedImage.TYPE_3BYTE_BGR;
    private static Map<String, File> imageMap = new HashMap<String, File>();

    private static boolean isCorrectType(BufferedImage sourceImage) {
        if (sourceImage.getType() == typeOfImage)
            return false;
        return false;
    }

    private static BufferedImage convertToType(BufferedImage sourceImage) {
        BufferedImage image;
        image = new BufferedImage(sourceImage.getWidth(),
                sourceImage.getHeight(), typeOfImage);
        image.getGraphics().drawImage(sourceImage, 0, 0, null);
        return image;
    }

    private static BufferedImage getImage(String imgName) {
        try {
            //String fileName = imgName + index + ".jpg";
            String fileName = imgName;
            System.out.println("fileName :" + fileName);
            File img = imageMap.get(fileName);

            BufferedImage in = null;
            if (img != null) {
                System.out.println("img :" + img.getName());
                in = ImageIO.read(img);
            } else {
                System.out.println("The following file is not found" + fileName);
            }
            return in;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int videoToListOfImages(String pathToEmptyContainer, String pathToOutputFolderForImages) throws IOException {
        String filename = pathToEmptyContainer;
        File outdir = new File(pathToOutputFolderForImages);
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

        long start = 0;
        //long end = container.getDuration();

        long step = (1 * 1000 * 1000) / videoCoder.getFrameRate().getNumerator();

//        END:
        int indexOfFrame = 0;
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
                            String fileName = indexOfFrame + ".jpg";
                            ImageIO.write(javaImage, "JPG", new File(outdir,
                                    fileName));
                            indexOfFrame++;
                            start += step;
                        }
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
        int numberOfPicturesAll = indexOfFrame;
        return  numberOfPicturesAll;
    }

    public void listOfImagesToVideo(String pathToEncodeVideo, String pathToImages,ICodec.ID codecID, int numberOfColumn, int numberOfRow) {
        final IMediaWriter writer = ToolFactory.makeWriter(pathToEncodeVideo);
        imageMap.clear();
        //writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, 512, 512);
        writer.addVideoStream(0, 0, codecID, numberOfColumn, numberOfRow);

        //File folder = new File("video" + File.separator + "EncodedPictures");
        File folder = new File (pathToImages);
        File[] listOfFiles = folder.listFiles();

        int indexVal = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                indexVal++;
                System.out.println("file.getName() :" + file.getName());
                imageMap.put(file.getName(), file);
            }
        }

        String filename = "video" + File.separator + "IMG_0065.mp4";
        //ArrayList<short[][]> listOfSegments = new ArrayList<>();
        File outdir = new File("video" + File.separator + "pictures");
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

        long step = (1 * 1000 * 1000) / videoCoder.getFrameRate().getNumerator();

        //for (int index = 0; index < SECONDS_TO_RUN_FOR * FRAME_RATE; index++) {
        for (int index = 0; index < listOfFiles.length; index++) {
            BufferedImage screen = getImage("EncodedImage" + index);
            BufferedImage bgrScreen;
            if (!isCorrectType(screen)) {
                bgrScreen = convertToType(screen);
            } else {
                bgrScreen = screen;
            }
            writer.encodeVideo(0, bgrScreen, start, TimeUnit.MICROSECONDS);
            start += step;

        }
        // tell the writer to close and write the trailer if needed
        writer.close();
        System.out.println("Video Created");
    }
}
