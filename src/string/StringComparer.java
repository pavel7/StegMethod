package string;

import bit.BitComparer;
import mathOperations.BitOperations;

public class StringComparer {
    String firstString;
    String secondString;

    public StringComparer(String newFirstString, String newSecondString) {
        firstString = newFirstString;
        secondString = newSecondString;
    }

    public int checkDifferenece() {
        int sumOfDif = 0;
        for (int i = 0; i < firstString.length(); i++) {
            if (firstString.charAt(i) != secondString.charAt(i))
                sumOfDif++;
        }
        if (sumOfDif == 0)
            return 0;
        else
            return (int) (100.0 * sumOfDif / (double) firstString.length());
    }

    public int checkDiffereneceByBit() {
        short[] str2vecFirst = new short[firstString.length()];
        short[] str2vecSecond = new short[secondString.length()];
        for (int i = 0; i < firstString.length(); i++) {
            str2vecFirst[i] = (short) firstString.charAt(i);
            str2vecSecond[i] = (short) secondString.charAt(i);
        }

        byte[] symbolInDecFirst = new byte[8 * firstString.length()];
        byte[] symbolInDecSecond = new byte[8 * secondString.length()];
        for (int i = 0; i < firstString.length(); i++) {
            byte[] symbolInDecFirstTemp = BitOperations.decToBit(str2vecFirst[i]);
            byte[] symbolInDecSecondTemp = BitOperations.decToBit(str2vecSecond[i]);
            for (int k = 0; k < 8; k++) {
                symbolInDecFirst[i*8+k] = symbolInDecFirstTemp[k];
                symbolInDecSecond[i*8+k] = symbolInDecSecondTemp[k];
            }
        }
        BitComparer testComparer = new BitComparer(symbolInDecFirst, symbolInDecSecond);
        return testComparer.checkDifferenece();
    }

}
