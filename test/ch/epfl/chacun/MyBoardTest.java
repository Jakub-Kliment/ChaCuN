package ch.epfl.chacun;

import java.util.Random;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyBoardTest {

    Board emptyBoard = Board.EMPTY;
    Random rand = new Random();

    Zone.Meadow meadowStartingZone = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)), null);
    Zone.Forest forestStartingZone = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow meadowStartingZone2 = new Zone.Meadow(562, new ArrayList<>(), null);
    Zone.Lake lakeStartingZone = new Zone.Lake(568, 1, null);
    Zone.River riverStartingZone = new Zone.River(563, 0, lakeStartingZone);
    TileSide n = new TileSide.Meadow(meadowStartingZone);
    TileSide e = new TileSide.Forest(forestStartingZone);
    TileSide s = new TileSide.Forest(forestStartingZone);
    TileSide w = new TileSide.River(meadowStartingZone2, riverStartingZone, meadowStartingZone);
    Tile startingTile = new Tile(56, Tile.Kind.START, n, e, s, w);

    @Test
    void boardEmptyBoardWorksCorrectly() {
        assertEquals(emptyBoard, Board.EMPTY);
    }

    @Test
    void boardPlacesStartingTileCorrectly() {
        Board startingBoard = emptyBoard.withNewTile(
                new PlacedTile(startingTile, null, Rotation.NONE, new Pos(0, 0)));

        assertEquals(startingBoard, Board.EMPTY.withNewTile(
                new PlacedTile(startingTile, null, Rotation.NONE, new Pos(0, 0))));
    }

    PlacedTile placedStartingTile = new PlacedTile(startingTile, null, Rotation.NONE, new Pos(0, 0));
    Board startingBoard = emptyBoard.withNewTile(placedStartingTile);


    @Test
    void tileAtReturnsCorrectStartingTile() {
        assertEquals(startingBoard.tileAt(Pos.ORIGIN), placedStartingTile);
    }

    @Test
    void tileReturnsNullForPositionWhereNoTileIsPlaced() {
        for (int i = -12; i < 13; i++) {
            for (int j = -12; j < 13; j++) {
                if (i != 0 && j != 0)
                    assertNull(startingBoard.tileAt(new Pos(i, j)));
            }
        }
    }

    @Test
    void tileReturnsNullForValuesOffTheBoard() {
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(13, 100);
            int y = rand.nextInt(13, 100);
            assertNull(startingBoard.tileAt(new Pos(x, y)));
        }

        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(13, 100);
            int y = rand.nextInt(13, 100);
            assertNull(startingBoard.tileAt(new Pos(-x, -y)));
        }

        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(13, 100);
            int y = rand.nextInt(13, 100);
            assertNull(startingBoard.tileAt(new Pos(-x, y)));
        }

        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(13, 100);
            int y = rand.nextInt(13, 100);
            assertNull(startingBoard.tileAt(new Pos(x, -y)));
        }

        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(13, 100);
            int y = rand.nextInt(0, 12);
            assertNull(startingBoard.tileAt(new Pos(x, y)));
        }

        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(0, 12);
            int y = rand.nextInt(13, 100);
            assertNull(startingBoard.tileAt(new Pos(x, y)));
        }
    }

    @Test
    void tileWithIdWorksForStartingTile() {
        assertEquals(placedStartingTile, startingBoard.tileWithId(56));
    }

}
