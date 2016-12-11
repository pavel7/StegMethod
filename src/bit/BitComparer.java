package bit;

public class BitComparer {
    byte[] firstString;
    byte[] secondString;

    public BitComparer(byte[] newFirstString, byte[] newSecondString) {
        firstString = newFirstString;
        secondString = newSecondString;
    }

    public double checkBER() {
        int sumOfDif = 0;
        for (int i = 0; i < firstString.length; i++) {
            if (firstString[i] != secondString[i])
                sumOfDif++;
        }
        if (sumOfDif == 0)
            return 0;
        else
            return ((double) sumOfDif / (double) firstString.length);
    }

}