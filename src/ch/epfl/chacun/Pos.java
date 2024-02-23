package ch.epfl.chacun;

/**
 * The position of a tile.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public record Pos(int x, int y) {

    // The origin of the game
    public static final Pos ORIGIN = new Pos(0, 0);

    /**
     * Change the position of the tile by a given amount.
     *
     * @param dX change of the x coordinate
     * @param dY change of the y coordinate
     * @return the translated position
     */

    public Pos translated(int dX, int dY) {
        return new Pos(x + dX, y + dY);
    }

    /**
     * Returns the neighbor of the tile in a given direction.
     *
     * @param direction the direction of the neighbor
     * @return the neighbor of the tile in the given direction
     */
    public Pos neighbor(Direction direction) {
        return switch (direction) {
            case N -> new Pos(x, y - 1);
            case E -> new Pos(x + 1, y);
            case S -> new Pos(x, y + 1);
            case W -> new Pos(x - 1, y);
        };
    }
}