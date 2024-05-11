package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base32Test {
    @Test
    void alphabetIsCorrect() {
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        assertEquals(expected, Base32.ALPHABET);
    }

    @Test
    void encodeBits5Works() {
        assertEquals("A", Base32.encodeBits5(0));
        assertEquals("B", Base32.encodeBits5(1));
        assertEquals("C", Base32.encodeBits5(2));
        assertEquals("D", Base32.encodeBits5(3));
        assertEquals("E", Base32.encodeBits5(4));
        assertEquals("F", Base32.encodeBits5(5));
        assertEquals("G", Base32.encodeBits5(6));
        assertEquals("H", Base32.encodeBits5(7));
        assertEquals("I", Base32.encodeBits5(8));
        assertEquals("J", Base32.encodeBits5(9));
        assertEquals("K", Base32.encodeBits5(10));
        assertEquals("L", Base32.encodeBits5(11));
        assertEquals("M", Base32.encodeBits5(12));
        assertEquals("N", Base32.encodeBits5(13));
        assertEquals("O", Base32.encodeBits5(14));
        assertEquals("P", Base32.encodeBits5(15));
        assertEquals("Q", Base32.encodeBits5(16));
        assertEquals("R", Base32.encodeBits5(17));
        assertEquals("S", Base32.encodeBits5(18));
        assertEquals("T", Base32.encodeBits5(19));
        assertEquals("U", Base32.encodeBits5(20));
        assertEquals("V", Base32.encodeBits5(21));
        assertEquals("W", Base32.encodeBits5(22));
        assertEquals("X", Base32.encodeBits5(23));
        assertEquals("Y", Base32.encodeBits5(24));
        assertEquals("Z", Base32.encodeBits5(25));
        assertEquals("2", Base32.encodeBits5(26));
        assertEquals("3", Base32.encodeBits5(27));
        assertEquals("4", Base32.encodeBits5(28));
        assertEquals("5", Base32.encodeBits5(29));
        assertEquals("6", Base32.encodeBits5(30));
        assertEquals("7", Base32.encodeBits5(31));
        assertEquals("7", Base32.encodeBits5(63));
        assertNotEquals("6", Base32.encodeBits5(40));
    }

    @Test
    void encodeBits10Works() {
        assertEquals("AA", Base32.encodeBits10(0));
        assertEquals("AB", Base32.encodeBits10(1));
        assertEquals("AC", Base32.encodeBits10(2));
        assertEquals("AD", Base32.encodeBits10(3));
        assertEquals("77", Base32.encodeBits10(0xFFFF));
        assertNotEquals("A6", Base32.encodeBits10(40));
    }

    @Test
    void decodeWorks() {
        assertEquals(0, Base32.decode("A"));
        assertEquals(1, Base32.decode("B"));
        assertEquals(2, Base32.decode("C"));
        assertEquals(3, Base32.decode("D"));
        assertEquals(4, Base32.decode("E"));
        assertEquals(5, Base32.decode("F"));
        assertEquals(6, Base32.decode("G"));
        assertEquals(7, Base32.decode("H"));
        assertEquals(8, Base32.decode("I"));
        assertEquals(9, Base32.decode("J"));
        assertEquals(10, Base32.decode("K"));
        assertEquals(11, Base32.decode("L"));
        assertEquals(12, Base32.decode("M"));
        assertEquals(13, Base32.decode("N"));
        assertEquals(14, Base32.decode("O"));
        assertEquals(15, Base32.decode("P"));
        assertEquals(16, Base32.decode("Q"));
        assertEquals(17, Base32.decode("R"));
        assertEquals(18, Base32.decode("S"));
        assertEquals(19, Base32.decode("T"));
        assertEquals(20, Base32.decode("U"));
        assertEquals(21, Base32.decode("V"));
        assertEquals(22, Base32.decode("W"));
        assertEquals(23, Base32.decode("X"));
        assertEquals(24, Base32.decode("Y"));
        assertEquals(25, Base32.decode("Z"));
        assertEquals(26, Base32.decode("2"));
        assertEquals(27, Base32.decode("3"));
        assertEquals(28, Base32.decode("4"));
        assertEquals(29, Base32.decode("5"));
        assertEquals(30, Base32.decode("6"));
        assertEquals(31, Base32.decode("7"));
        assertEquals(31, Base32.decode("7"));
        assertNotEquals(40, Base32.decode("6"));
    }

    @Test
    void isValidWorks() {
        assertTrue(Base32.isValid("A"));
        assertTrue(Base32.isValid("B"));
        assertTrue(Base32.isValid("C"));
        assertTrue(Base32.isValid("7"));
        assertFalse(Base32.isValid("8"));
        assertFalse(Base32.isValid("a"));
        assertFalse(Base32.isValid(" "));
        assertFalse(Base32.isValid(""));
        assertTrue(Base32.isValid("A2"));
        assertTrue(Base32.isValid("B33ER"));
    }

    @Test
    void isValidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Base32.isValid(null));
    }

    @Test
    void decodeWorksCorrectly() {
        assertEquals(0, Base32.decode("A"));
        assertEquals(1, Base32.decode("B"));
        assertEquals(2, Base32.decode("C"));
        assertEquals(3, Base32.decode("D"));
        assertEquals(4, Base32.decode("E"));
        assertEquals(5, Base32.decode("F"));
        assertEquals(6, Base32.decode("G"));
        assertEquals(7, Base32.decode("H"));
        assertEquals(8, Base32.decode("I"));
        assertEquals(9, Base32.decode("J"));
        assertEquals(10, Base32.decode("K"));
        assertEquals(11, Base32.decode("L"));
        assertEquals(12, Base32.decode("M"));
        assertEquals(13, Base32.decode("N"));
        assertEquals(14, Base32.decode("O"));
        assertEquals(15, Base32.decode("P"));
        assertEquals(16, Base32.decode("Q"));
        assertEquals(17, Base32.decode("R"));
        assertEquals(18, Base32.decode("S"));
        assertEquals(19, Base32.decode("T"));
        assertEquals(20, Base32.decode("U"));
        assertEquals(21, Base32.decode("V"));
        assertEquals(22, Base32.decode("W"));
        assertEquals(23, Base32.decode("X"));
        assertEquals(24, Base32.decode("Y"));
        assertEquals(25, Base32.decode("Z"));
        assertEquals(26, Base32.decode("2"));
        assertEquals(27, Base32.decode("3"));
        assertEquals(28, Base32.decode("4"));
        assertEquals(29, Base32.decode("5"));
        assertEquals(30, Base32.decode("6"));
        assertEquals(31, Base32.decode("7"));
        assertEquals(31, Base32.decode("7"));
        assertNotEquals(40, Base32.decode("6"));

        assertEquals(0, Base32.decode("AA"));
        assertEquals(1, Base32.decode("AB"));
        assertEquals(2, Base32.decode("AC"));
        assertNotEquals(40, Base32.decode("A6"));
        assertEquals(0b1110101001, Base32.decode("5J"));
    }
}