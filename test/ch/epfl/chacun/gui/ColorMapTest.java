package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ColorMapTest {

    @Test
    void fillReturnsCorrectColor() {
        Color red = ColorMap.fillColor(PlayerColor.RED);
        assertEquals(Color.RED, red);
        assertNotEquals(Color.BLUE, red);

        Color blue = ColorMap.fillColor(PlayerColor.BLUE);
        assertEquals(Color.BLUE, blue);
        assertNotEquals(Color.YELLOW, blue);

        Color green = ColorMap.fillColor(PlayerColor.GREEN);
        assertEquals(Color.LIME, green);
        assertNotEquals(Color.GREEN, green);

        Color yellow = ColorMap.fillColor(PlayerColor.YELLOW);
        assertEquals(Color.YELLOW, yellow);
        assertNotEquals(Color.PURPLE, yellow);

        Color purple = ColorMap.fillColor(PlayerColor.PURPLE);
        assertEquals(Color.PURPLE, purple);
        assertNotEquals(Color.RED, purple);
    }

    @Test
    void strokeReturnsCorrectColor() {
        Color red = ColorMap.strokeColor(PlayerColor.RED);
        assertEquals(Color.WHITE, red);
        assertNotEquals(Color.RED, red);

        Color blue = ColorMap.strokeColor(PlayerColor.BLUE);
        assertEquals(Color.WHITE, blue);
        assertNotEquals(Color.BLUE, blue);

        Color green = ColorMap.strokeColor(PlayerColor.GREEN);
        assertEquals(Color.LIME.deriveColor(0, 1, 0.6, 1), green);
        assertNotEquals(Color.LIME, green);

        Color yellow = ColorMap.strokeColor(PlayerColor.YELLOW);
        assertEquals(Color.YELLOW.deriveColor(0,1,0.6,1), yellow);
        assertNotEquals(Color.YELLOW, yellow);

        Color purple = ColorMap.strokeColor(PlayerColor.PURPLE);
        assertEquals(Color.WHITE, purple);
        assertNotEquals(Color.PURPLE, purple);
    }
}
