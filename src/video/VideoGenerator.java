package video;

/**
 * Created by pavelcerepanov on 20.12.16.
 */
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.*;

public class VideoGenerator {

    private static final String outputFilename = "testVideo" + File.separator + "myVideo.mp4";

    private static Dimension screenBounds;

    private static Map<String, File> imageMap = new HashMap<String, File>();

    public static void main(String[] args) {

        final IMediaWriter writer = ToolFactory.makeWriter(outputFilename);

        screenBounds = Toolkit.getDefaultToolkit().getScreenSize();

        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, 512, 512);

        File folder = new File("video" + File.separator + "EncodedPictures" + File.separator);
        File[] listOfFiles = folder.listFiles();

        int indexVal = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                indexVal++;
                System.out.println("file.getName() :"+file.getName());
                imageMap.put(file.getName(), file);
            }
        }

        String filename = "video" + File.separator + "IMG_0065.mp4";
        //ArrayList<short[][]> listOfSegments = new ArrayList<>();
        File outdir = new File("video" + File.separator + "pictures" + File.separator);
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
            BufferedImage screen = getImage("EncodedImage", index);
            BufferedImage bgrScreen = convertToType(screen, BufferedImage.TYPE_3BYTE_BGR);
            writer.encodeVideo(0, bgrScreen, start, TimeUnit.MICROSECONDS);
            start+=step;

        }
        // tell the writer to close and write the trailer if needed
        writer.close();
        System.out.println("Video Created");

    }

    private static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        BufferedImage image;
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        }
        else {
            image = new BufferedImage(sourceImage.getWidth(),
                    sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }
        return image;
    }

    private static BufferedImage getImage(String imgName,int index) {

        try {
            String fileName=imgName + index + ".jpg";
            System.out.println("fileName :" + fileName);
            File img = imageMap.get(fileName);

            BufferedImage in=null;
            if (img != null) {
                System.out.println("img :"+img.getName());
                in = ImageIO.read(img);
            }else
            {
                System.out.println("++++++++++++++++++++++++++++++++++++++index :" + index);
                img = imageMap.get(1);
                in = ImageIO.read(img);
            }
            return in;

        }

        catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }

}
