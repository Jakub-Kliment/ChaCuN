package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import javafx.scene.paint.Color;

/**
 * A utility class that maps player colors to JavaFX colors for the GUI.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public class ColorMap {

    /**
     * Private constructor to prevent instantiation.
     */
    private ColorMap() {}

    /**
     * Returns the fill color for the given player of the game.
     *
     * @param color the player color
     * @return the corresponding fill color
     */
    public static Color fillColor(PlayerColor color) {
        return switch (color) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.LIME;
            case YELLOW -> Color.YELLOW;
            case PURPLE -> Color.PURPLE;
        };
    }

    /**
     * Returns the stroke color for the given player of the game.
     * It is white by default, except for green and yellow players,
     * whose stroke colors are a lighter version of the fill color.
     *
     * @param color the player color
     * @return the corresponding stroke color
     */
    public static Color strokeColor(PlayerColor color) {
        return switch (color) {
            case GREEN -> Color.LIME.deriveColor(0, 1, 0.6, 1);
            case YELLOW -> Color.YELLOW.deriveColor(0, 1, 0.6, 1);
            default -> Color.WHITE;
        };
    }
}
