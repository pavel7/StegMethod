package string;


public class StringTransformer {
    //private String input = null;
    private static final String startString = "n0c@m0k";
    private static final String endString = "KiHeu,6";

    public static String resultConnectedString (String input) {
        return startString+input+endString;
    }

    public static short[] stringToShort (String input){
        int stringLenght = input.length();
        short[] result = new short[stringLenght];
        for(int i = 0; i< stringLenght;i++) {
            result[i]=(short)input.charAt(i);
        }
        return result;
    }

    public static void main(String[] args) {
        String test = "Test Watermark";
        System.out.println(StringTransformer.resultConnectedString(test));
        System.out.println(StringTransformer.stringToShort(test));
    }

}
