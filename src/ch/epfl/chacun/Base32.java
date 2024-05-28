package ch.epfl.chacun;

/**
 * Class that provides utility methods for encoding and decoding base-32 strings.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class Base32 {

    /**
     * Private constructor to prevent instantiation.
     */
    private Base32() {}

    /**
     * The alphabet consisting of 32 characters used for encoding and decoding.
     */
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    /**
     * The number of bits in a single letter.
     */
    public static final int CHARACTER_BIT_SIZE = 5;

    /**
     * The mask for extracting the 5 least significant bits of an integer.
     */
    public static final int CHARACTER_MASK = (1 << CHARACTER_BIT_SIZE) - 1;

    /**
     * Checks if the given string is a valid base-32 string.
     *
     * @param s the string to check
     * @return true if the string is valid, false otherwise
     */
    public static boolean isValid(String s) {
        Preconditions.checkArgument(s != null);
        if (s.isEmpty()) return false;
        return s.chars().allMatch(c -> ALPHABET.indexOf(c) != -1);
    }

    /**
     * Encodes the 5 least significant bits of an integer into a base-32 character.
     * Encodes one character at a time.
     *
     * @param bits the bits to encode
     * @return the encoded character
     */
    public static String encodeBits5(int bits) {
        return Character.toString(ALPHABET.charAt(bits & CHARACTER_MASK));
    }

    /**
     * Encodes the 10 least significant bits of an integer into a base-32 string.
     * Encodes two characters at a time.
     *
     * @param bits the bits to encode
     * @return the encoded string
     */
    public static String encodeBits10(int bits) {
        return encodeBits5(bits >> CHARACTER_BIT_SIZE) + encodeBits5(bits);
    }

    /**
     * Decodes a base-32 character into the 5 least significant bits of an integer.
     *
     * @param s the character(s) (string) to decode
     * @return the decoded bits
     */
    public static int decode(String s) {
        return s.chars()
                .map(ALPHABET::indexOf)
                .reduce(0, (acc, c) -> (acc << CHARACTER_BIT_SIZE) | c);
    }
}