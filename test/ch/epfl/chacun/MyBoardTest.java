package ch.epfl.chacun;

import java.util.*;

import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
    void tileAtReturnsNullForPositionWhereNoTileIsPlaced() {
        for (int i = -12; i < 13; i++) {
            for (int j = -12; j < 13; j++) {
                if (i != 0 && j != 0)
                    assertNull(startingBoard.tileAt(new Pos(i, j)));
            }
        }
    }

    @Test
    void tileAtReturnsCorrectPositionForPlacedTile() {
        Board board = Board.EMPTY;
        List<PlacedTile> placedTiles = new ArrayList<>();
        int index = 0;
        for (int i = -12; i < 13; i++) {
            for (int j = -12; j < 13; j++) {
                PlacedTile newTile = new PlacedTile(new Tile(
                        index, Tile.Kind.NORMAL,
                        new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(4, new ArrayList<>(), null))),
                        null, Rotation.NONE, new Pos(i, j));

                placedTiles.add(newTile);
                board = board.withNewTile(newTile);
                index++;
            }
        }
        index = 0;
        for (int i = -12; i < 13; i++) {
            for (int j = -12; j < 13; j++) {
                assertEquals(placedTiles.get(index), board.tileAt(new Pos(i, j)));
                index++;
            }
        }
    }

    @Test
    void tileAtReturnsNullForValuesOffTheBoard() {
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

    @Test
    void tileWithIdThrowsIllegalArgumentExceptionForNotFoundIds() {
        for (int i = -20; i < 125; i++) {
            if (i != 56) {
                int tileId = i;
                assertThrows(IllegalArgumentException.class, () -> startingBoard.tileWithId(tileId));
            }
        }
    }

    @Test
    void tileWithIdWorksForAllValues() {
        Board board = Board.EMPTY;
        List<PlacedTile> placedTiles = new ArrayList<>();
        int index = 0;
        for (int i = -12; i < 13; i++) {
            for (int j = -12; j < 13; j++) {
                PlacedTile newTile = new PlacedTile(new Tile(
                        index, Tile.Kind.NORMAL,
                        new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(4, new ArrayList<>(), null))),
                        null, Rotation.NONE, new Pos(i, j));

                placedTiles.add(newTile);
                board = board.withNewTile(newTile);
                index++;
            }
        }

        for (int i = 0; i < 625; i++)
            assertEquals(placedTiles.get(i), board.tileWithId(i));
    }

    @Test
    void tileWithIdThrowsIllegalArgumentExceptionIfTileIsNotFound() {
        Board board = Board.EMPTY;
        int index = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                PlacedTile newTile = new PlacedTile(new Tile(
                        index, Tile.Kind.NORMAL,
                        new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(4, new ArrayList<>(), null))),
                        null, Rotation.NONE, new Pos(i, j));

                board = board.withNewTile(newTile);
                index++;
            }
        }
        for (int i = 43; i < 96; i++) {
            int a = i;
            Board finalBoard = board;
            if (i != 56) {
                assertThrows(IllegalArgumentException.class, () -> finalBoard.tileWithId(a));
            }
        }
    }

    @Test
    void tileWithIdThrowsIllegalArgumentExceptionForOutOfBoundsIndex() {
        for (int i = 0; i < 100; i++) {
            int r = rand.nextInt(625, 1000);
            assertThrows(IllegalArgumentException.class, () -> startingBoard.tileWithId(r));
        }
    }

    @Test
    void canceledAnimalsCreatesImmutableCopyCorrectly() {
        Set<Animal> cancelledAnimalSet = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0)
                cancelledAnimalSet.add(new Animal(i, Animal.Kind.DEER));
            else if (i % 3 == 1)
                cancelledAnimalSet.add(new Animal(i, Animal.Kind.MAMMOTH));
            else
                cancelledAnimalSet.add(new Animal(i, Animal.Kind.AUROCHS));
        }
        Board board = startingBoard.withMoreCancelledAnimals(cancelledAnimalSet);

        for (int i = 100; i < 200; i++) {
            if (i % 3 == 0)
                cancelledAnimalSet.add(new Animal(i, Animal.Kind.DEER));
            else if (i % 3 == 1)
                cancelledAnimalSet.add(new Animal(i, Animal.Kind.MAMMOTH));
            else
                cancelledAnimalSet.add(new Animal(i, Animal.Kind.AUROCHS));
        }
        assertNotEquals(cancelledAnimalSet, board.cancelledAnimals());
    }
}
