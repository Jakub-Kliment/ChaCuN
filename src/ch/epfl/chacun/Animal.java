package ch.epfl.chacun;

/**
 * Represents an animal in the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 *
 * @param id the id of the animal
 * @param kind the kind of the animal
 */

public record Animal(int id, Kind kind) {

    /**
     * The kind of the animal
     */
    public enum Kind {
        MAMMOTH,
        AUROCHS,
        DEER,
        TIGER
    }

    /**
     * The id of the tile the animal is located on.
     *
     * @return the id of the tile the animal is on
     */
    public int tileId() {
        return id / 100;
    }
}
