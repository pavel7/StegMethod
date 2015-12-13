package mathOperations;

public class BitOperations {
    public static final byte numberOfBit = 8;

    public static byte[] decToBit(short number) {
        short tempNumber = number;
        byte[] bitRepresentation = new byte[numberOfBit];
        for (int i = 0; i < numberOfBit; i++) {
            bitRepresentation[numberOfBit - i - 1] = (byte) (tempNumber % 2);
            tempNumber = (short) (tempNumber / 2);
        }
        return bitRepresentation;
    }

    public static short bitToDec(byte[] number) {
        short result = 0;
        for (int i = 0; i < numberOfBit; i++) {
            result = (short)(result + number[numberOfBit - i - 1]*Math.pow(2,i));
        }
        return result;
    }
}
