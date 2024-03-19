package ch.epfl.chacun;

/**
 * A utility class for checking preconditions.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class Preconditions {

    // Prevents instantiation
    private Preconditions() {}

    /**
     * Checks if an argument is true, otherwise throws an IllegalArgumentException.
     *
     * @param shouldBeTrue the boolean to check
     * @throws IllegalArgumentException if the given boolean is false
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue)
            throw new IllegalArgumentException();
    }
}
