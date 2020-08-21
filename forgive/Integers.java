package forgive;

public class Integers {
    private Integers(){}

    public static byte[] asByteArray(int data, int length) {
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            int r = length - i - 1;
            int mask = 0xFF << (r * 8);
            bytes[i] = (byte)((data & mask) >>> (r * 8));
        }

        return bytes;
    }
}