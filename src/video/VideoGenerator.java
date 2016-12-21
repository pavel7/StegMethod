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
import com.xuggle.xuggler.ICodec;

public class VideoGenerator {

    private static final double FRAME_RATE = 20;

    private static final int SECONDS_TO_RUN_FOR = 20;

    private static final String outputFilename = "testVideo" + File.separator + "myVideo.mp4";

    private static Dimension screenBounds;

    private static Map<String, File> imageMap = new HashMap<String, File>();

    public static void main(String[] args) {

        final IMediaWriter writer = ToolFactory.makeWriter(outputFilename);

        screenBounds = Toolkit.getDefaultToolkit().getScreenSize();

        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, 512, 512);

        File folder = new File("video" + File.separator + "EncodedPictures");
        File[] listOfFiles = folder.listFiles();

        int indexVal = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                indexVal++;
                System.out.println("file.getName() :"+file.getName());
                imageMap.put(file.getName(), file);
            }
        }

        //for (int index = 0; index < SECONDS_TO_RUN_FOR * FRAME_RATE; index++) {
        for (int index = 0; index < listOfFiles.length; index++) {
            BufferedImage screen = getImage("EncodedImage", index);
            BufferedImage bgrScreen = convertToType(screen, BufferedImage.TYPE_3BYTE_BGR);
            writer.encodeVideo(0, bgrScreen, 300*index, TimeUnit.MILLISECONDS);

        }
        // tell the writer to close and write the trailer if needed
        writer.close();
        System.out.println("Video Created");

    }

    public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
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
