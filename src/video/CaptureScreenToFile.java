package video;

import com.xuggle.xuggler.*;
import com.xuggle.xuggler.IContainer.Type;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import string.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public class CaptureScreenToFile {
    private final IContainer outContainer;
    private final IStream outStream;
    private final IStreamCoder outStreamCoder;
    private final IRational frameRate;
    private long firstTimeStamp = -1L;
    private static Map<String, File> imageMap = new HashMap<String, File>();
    private static int test = 0;

    public static void main(String[] args) {
        String outFile = "video" + File.separator + "output.mp4";
        File folder = new File("video" + File.separator + "test_decoded1" + File.separator);
        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.equals(".DS_Store");
            }
        });

        int indexVal = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                indexVal++;
                //System.out.println("file.getName() :"+file.getName());
                int indexOfFile = StringUtils.getImageIndex(file.getName());
                String convertedNameOfFileNumber = String.valueOf(indexOfFile);
                imageMap.put(convertedNameOfFileNumber, file);
            }
        }

        System.out.println(imageMap.size());
        try {
            CaptureScreenToFile videoEncoder = new CaptureScreenToFile(outFile);

            for (int index = 0; index < listOfFiles.length; index++) {
                BufferedImage screen = getImage(index);
                long timeStamp = getImageTimeStamp(index);
                if (timeStamp == -1) {
                    System.out.println("some error occurs");
                }

                //System.out.println("encoded image");
                //videoEncoder.encodeImage(videoEncoder.takeSingleSnapshot());
                videoEncoder.encodeImage(screen, timeStamp);

//                try {
//                    Thread.sleep((long)(1000.0D / videoEncoder.frameRate.getDouble()));
//                } catch (InterruptedException var5) {
//                    var5.printStackTrace(System.out);
//                }
            }

            videoEncoder.closeStreams();
        } catch (RuntimeException var6) {
            System.err.println("we can't get permission to capture the screen");
        }


    }

    public CaptureScreenToFile(String outFile) {
//        try {
//            this.robot = new Robot();
//        } catch (AWTException var6) {
//            System.out.println(var6.getMessage());
//            throw new RuntimeException(var6);
//        }

        //this.toolkit = Toolkit.getDefaultToolkit();
        //this.screenBounds = new Rectangle(this.toolkit.getScreenSize());
        this.frameRate = IRational.make(30, 1);
        this.outContainer = IContainer.make();
        IContainerFormat formatOfVideo = IContainerFormat.make();
        formatOfVideo.setInputFormat("avc1");
        int retval = this.outContainer.open(outFile, Type.WRITE, (IContainerFormat) formatOfVideo);
        if (retval < 0) {
            throw new RuntimeException("could not open output file");
        } else {
            ICodec codec = ICodec.guessEncodingCodec((IContainerFormat) null, (String) "h264", outFile, (String) null, com.xuggle.xuggler.ICodec.Type.CODEC_TYPE_VIDEO);
            if (codec == null) {
                throw new RuntimeException("could not guess a codec");
            } else {
                this.outStream = this.outContainer.addNewStream(codec);
                this.outStreamCoder = this.outStream.getStreamCoder();
                this.outStreamCoder.setCodec(codec);
                this.outStreamCoder.setNumPicturesInGroupOfPictures(12);
                //this.outStreamCoder.setBitRate(1598294);
                //this.outStreamCoder.setBitRateTolerance(9000);
                int height = 512;//this.toolkit.getScreenSize().height;
                int width = 512;//this.toolkit.getScreenSize().width;
                this.outStreamCoder.setPixelType(IPixelFormat.Type.YUV420P);
                this.outStreamCoder.setHeight(height);
                this.outStreamCoder.setWidth(width);
                //this.outStreamCoder.setFlag(Flags.FLAG_QSCALE, true);
                this.outStreamCoder.setGlobalQuality(0);
                this.outStreamCoder.setFrameRate(this.frameRate);
                this.outStreamCoder.setTimeBase(IRational.make(1, 15360));
                retval = this.outStreamCoder.open((IMetaData) null, (IMetaData) null);
                if (retval < 0) {
                    throw new RuntimeException("could not open input decoder");
                } else {
                    retval = this.outContainer.writeHeader();
                    if (retval < 0) {
                        throw new RuntimeException("could not write file header");
                    }
                }
            }
        }
    }

    public void encodeImage(BufferedImage originalImage, long timeStamp) {
        BufferedImage worksWithXugglerBufferedImage = convertToType(originalImage, BufferedImage.TYPE_3BYTE_BGR);
        IPacket packet = IPacket.make();
//        long now = System.currentTimeMillis();
//        if(this.firstTimeStamp == -1L) {
//            this.firstTimeStamp = now;
//        }

        IConverter converter = null;

        try {
            converter = ConverterFactory.createConverter(worksWithXugglerBufferedImage, com.xuggle.xuggler.IPixelFormat.Type.YUV420P);
        } catch (UnsupportedOperationException var11) {
            System.out.println(var11.getMessage());
            var11.printStackTrace(System.out);
        }

        //long timeStamp = (now - this.firstTimeStamp) * 1000L;
        System.out.println(timeStamp);
        IVideoPicture outFrame = converter.toPicture(worksWithXugglerBufferedImage, timeStamp);
//        IVideoPicture outFrame = Utils.imageToVideoPicture(worksWithXugglerBufferedImage, timeStamp);
        outFrame.setQuality(0);
        outFrame.setComplete(true, com.xuggle.xuggler.IPixelFormat.Type.YUV420P, 512, 512, timeStamp);
        int retval = this.outStreamCoder.encodeVideo(packet, outFrame, 0);
        if (retval < 0) {
            throw new RuntimeException("could not encode video");
        } else {
//            do {
//                retval = this.outStreamCoder.encodeVideo(packet, null, 0);
//                if (retval < 0) {
//                    throw new RuntimeException("could not encode video");
//                }
//            } while (!packet.isComplete());
            if (packet.isComplete()) {
                //this.outStreamCoder.encodeVideo(packet, null, 0);
                retval = this.outContainer.writePacket(packet);
                if (retval < 0) {
                    throw new RuntimeException("could not save packet to container");
                }
                System.out.println("DTS "+packet.getDts());
                System.out.println("PTS "+packet.getPts());
                System.out.println(test++);
            }

        }
    }

    public void closeStreams() {
//        System.out.println(this.outStreamCoder.isOpen());
//        int retval = this.outContainer.close();
//        if(retval < 0) {
//            throw new RuntimeException("Could not close the output file");
//        }
        System.out.println(this.outStreamCoder.isOpen());
        /**
         * Some video coders (e.g. MP3) will often "read-ahead" in a stream and keep
         * extra data around to get efficient compression. But they need some way to
         * know they're never going to get more data. The convention for that case
         * is to pass null for the IMediaData (e.g. IAudioSamples or IVideoPicture)
         * in encodeAudio(...) or encodeVideo(...) once before closing the coder.
         *
         * In that case, the IStreamCoder will flush all data.
         */
        IPacket oPacket = IPacket.make();
        do {
            this.outStreamCoder.encodeVideo(oPacket, null, 0);
            if (oPacket.isComplete())
                this.outContainer.writePacket(oPacket, true);
        } while (oPacket.isComplete());

        /**
         * Some container formats require a trailer to be written to avoid a corrupt
         * files.
         *
         * Others, such as the FLV container muxer, will take a writeTrailer() call
         * to tell it to seek() back to the start of the output file and write the
         * (now known) duration into the Meta Data.
         *
         * So trailers are required. In general if a format is a streaming format,
         * then the writeTrailer() will never seek backwards.
         *
         * Make sure you don't close your codecs before you write your trailer, or
         * we'll complain loudly and not actually write a trailer.
         */
        int retval = this.outContainer.writeTrailer();
        if (retval < 0)
            throw new RuntimeException("Could not write trailer to output file");

        /**
         * We do a nice clean-up here to show you how you should do it.
         *
         * That said, Xuggler goes to great pains to clean up after you if you
         * forget to release things. But still, you should be a good boy or giral
         * and clean up yourself.
         */
        this.outStreamCoder.close();


        /**
         * Tell Xuggler it can close the output file, write all data, and free all
         * relevant memory.
         */
        this.outContainer.close();
    }

//    public BufferedImage takeSingleSnapshot() {
//        return this.robot.createScreenCapture(this.screenBounds);
//    }

    public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        BufferedImage image;
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        } else {
            image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, (ImageObserver) null);
        }

        return image;
    }

    private static BufferedImage getImage(int index) {

        try {
            String fileIndex = String.valueOf(index);
            //System.out.println("fileName :" + fileName);
            File img = imageMap.get(fileIndex);

            BufferedImage in = null;
            if (img != null) {
                //System.out.println("img :"+img.getName());
                in = ImageIO.read(img);
            } else {
                System.out.println("++++++++++++++++++++++++++++++++++++++index :" + index);
                img = imageMap.get(1);
                in = ImageIO.read(img);
            }
            return in;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static long getImageTimeStamp(int index) {
        long timeStamp = -1;
        try {
            String fileIndex = String.valueOf(index);
            //System.out.println("fileName :" + fileName);
            File img = imageMap.get(fileIndex);

            BufferedImage in = null;
            if (img != null) {
                String fileName = img.getName();
                timeStamp = StringUtils.getImageTimeframe(fileName);
            } else {
                System.out.println("++++++++++++++++++++++++++++++++++++++index :" + index);
            }
            return timeStamp;
        } catch (Exception e) {
            e.printStackTrace();
            return timeStamp;
        }

    }


}
