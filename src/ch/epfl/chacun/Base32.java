package ch.epfl.chacun;

public class Base32 {

    private Base32() {}

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    public static final int LETTER_BITS = 0x0000_001F;

    public static final int LETTER_SIZE = 5;

    public static boolean isValid(String s) {
        Preconditions.checkArgument(s != null);
        return s.chars()
                .allMatch(c -> ALPHABET.indexOf(c) != -1);
    }

    public static String encodeBits5(int bits) {
        return Character.toString(ALPHABET.charAt(bits & LETTER_BITS));
    }

    public static String encodeBits10(int bits) {
        return encodeBits5(bits >> LETTER_SIZE) + encodeBits5(bits);
    }

    public static int decode(String s) {
        Preconditions.checkArgument(isValid(s) && s.length() <= 2);
        return s.chars()
                .map(ALPHABET::indexOf)
                .reduce(0, (acc, c) -> (acc << LETTER_SIZE) | c);
    }
}