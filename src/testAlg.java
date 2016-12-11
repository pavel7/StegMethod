import assessmentTests.NoiseGenerator;

import java.util.Random;

/**
 * Created by Павел on 02.10.2016.
 */
public class testAlg {

    public int[] createArray(int size) {
        Random rnd = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++)
            arr[i] = i;
        for (int i = size - 1; i > 0; i--) {
            int pos = rnd.nextInt(i + 1);
            int a = arr[pos];
            arr[pos] = arr[i];
            arr[i] = a;
        }
        return arr;
    }

    public static void main(String a[]) {
        String format = ".jpg";
        String filenameInput = "video\\IMG_0065.mp4";
        String filenameOutput = "video\\EncodedPictures\\EncodedImage";
        String noiseFilenameOutput = "video\\NoisesEncodedPictures\\EncodedImage";
        int startSegmentEncode = 0;
        NoiseGenerator newGenerator = new NoiseGenerator(filenameOutput, noiseFilenameOutput, format, 90, startSegmentEncode);
        newGenerator.makeEmpty(255);
    }
}
