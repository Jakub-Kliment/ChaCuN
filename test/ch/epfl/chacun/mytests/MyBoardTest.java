package ch.epfl.chacun.mytests;

import java.util.*;

import ch.epfl.chacun.tile.Tiles;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 1, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 3, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 4, new ArrayList<>(), null))),
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
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 1, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 3, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 4, new ArrayList<>(), null))),
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
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 1, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 3, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 4, new ArrayList<>(), null))),
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
        Board board = startingBoard;
        board = board.withMoreCancelledAnimals(cancelledAnimalSet);

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

    @Test
    void occupantsProducesSetOfOccupantsCorrectly() {
        Board board = Board.EMPTY;
        Set<Occupant> occupants = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            PlayerColor color;
            Occupant occupant = new Occupant(Occupant.Kind.PAWN, i * 10 + i);
            if (i % 3 == 0)
                color = PlayerColor.BLUE;
            else if (i % 3 == 1) {
                color = PlayerColor.RED;
                occupant = null;
            }
            else
                color = PlayerColor.GREEN;

            PlacedTile placedTile = new PlacedTile(
                    new Tile(i, Tile.Kind.NORMAL,
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null))),
                    color, Rotation.NONE, new Pos(i % 25 - 12, i / 25 - 12), occupant);

            board = board.withNewTile(placedTile);
            if (occupant != null)
                occupants.add(occupant);
        }
        assertEquals(occupants, board.occupants());
    }

    @Test
    void occupantsProducesSetOfOccupantsCorrectlyAfterAddingOccupantsOffBoard() {
        Board board = Board.EMPTY;
        Set<Occupant> occupants = new HashSet<>();
        int index = 0;

        for (int i = 0; i < 100; i++) {
            PlayerColor color;
            Occupant occupant = new Occupant(Occupant.Kind.PAWN, i * 10 + i);

            if (i % 3 == 0)
                color = PlayerColor.BLUE;
            else if (i % 3 == 1) {
                color = PlayerColor.RED;
                occupant = null;
            } else
                color = PlayerColor.GREEN;

            PlacedTile placedTile = new PlacedTile(
                    new Tile(i, Tile.Kind.NORMAL,
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null))),
                    color, Rotation.NONE, new Pos(index % 25 - 12, index / 25 - 12), occupant);

            if (i % 7 == 4) {
                board = board.withNewTile(placedTile);
                index++;
            }
            if (occupant != null)
                occupants.add(occupant);
        }
        assertNotEquals(occupants, board.occupants());
    }

    @Test
    void occupantsWorksForBoardWithNoOccupants() {
        Board board = Board.EMPTY;

        for (int i = 0; i < 100; i++) {
            PlacedTile placedTile = new PlacedTile(
                    new Tile(i, Tile.Kind.NORMAL,
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null))),
                    PlayerColor.RED, Rotation.NONE, new Pos(i % 25 - 12, i / 25 - 12));

            board = board.withNewTile(placedTile);
        }
        assertEquals(new HashSet<>(), board.occupants());
    }

    @Test
    void occupantCountWorksForNormalValues() {
        Board board = Board.EMPTY;
        Set<Occupant> occupants = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            Occupant occupant = new Occupant(Occupant.Kind.PAWN, i * 10 + i % 10);
            PlacedTile placedTile = new PlacedTile(
                    new Tile(i, Tile.Kind.NORMAL,
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null))),
                    PlayerColor.RED, Rotation.NONE, new Pos(i % 25 - 12, i / 25 - 12), occupant);

            board = board.withNewTile(placedTile);
            occupants.add(occupant);
        }
        assertEquals(occupants.size(), board.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
    }

    @Test
    void occupantCountWorksForDifferentColorValues() {
        Board board = Board.EMPTY;
        List<Occupant> redOccupants = new ArrayList<>();
        List<Occupant> blueOccupants = new ArrayList<>();
        List<Occupant> greenOccupants = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            PlayerColor color;
            if (i % 3 == 0)
                color = PlayerColor.BLUE;
            else if (i % 3 == 1)
                color = PlayerColor.RED;
            else
                color = PlayerColor.GREEN;

            Occupant occupant = new Occupant(Occupant.Kind.PAWN, i * 10 + i % 10);
            PlacedTile placedTile = new PlacedTile(
                    new Tile(i, Tile.Kind.NORMAL,
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null))),
                    color, Rotation.NONE, new Pos(i % 25 - 12, i / 25 - 12), occupant);

            board = board.withNewTile(placedTile);

            switch (color) {
                case RED -> redOccupants.add(occupant);
                case BLUE -> blueOccupants.add(occupant);
                case GREEN -> greenOccupants.add(occupant);
            }

        }
        assertEquals(blueOccupants.size(), board.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(redOccupants.size(), board.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(greenOccupants.size(), board.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(0, board.occupantCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
    }

    @Test
    void occupantCountWorksForDifferentColorValuesAndDifferentKinds() {
        Board board = Board.EMPTY;

        List<Occupant> redPawns = new ArrayList<>();
        List<Occupant> bluePawns = new ArrayList<>();
        List<Occupant> greenPawns = new ArrayList<>();
        List<Occupant> redHuts = new ArrayList<>();
        List<Occupant> blueHuts = new ArrayList<>();
        List<Occupant> greenHuts = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            PlayerColor color;
            Occupant.Kind occupantKind;

            if (i % 3 == 0)
                color = PlayerColor.BLUE;
            else if (i % 3 == 1)
                color = PlayerColor.RED;
            else
                color = PlayerColor.GREEN;

            if (i % 2 == 0)
                occupantKind = Occupant.Kind.PAWN;
            else
                occupantKind = Occupant.Kind.HUT;

            Occupant occupant = new Occupant(occupantKind, i * 10 + i % 10);

            Zone.Lake lake = new Zone.Lake(i, 0, null);
            Zone.Meadow meadow1 = new Zone.Meadow(i, new ArrayList<>(), null);
            Zone.Meadow meadow2 = new Zone.Meadow(i, new ArrayList<>(), null);
            TileSide n = new TileSide.Meadow(meadow1);
            TileSide s = new TileSide.Meadow(meadow2);
            TileSide e = new TileSide.River(meadow1, new Zone.River(i, 0, lake), meadow2);
            TileSide w = new TileSide.River(meadow1, new Zone.River(i, 0, lake), meadow2);

            PlacedTile placedTile = new PlacedTile(
                    new Tile(i, Tile.Kind.NORMAL, n, e, s, w),
                    color, Rotation.NONE, new Pos(i % 25 - 12, i / 25 - 12), occupant);

            board = board.withNewTile(placedTile);

            if (occupantKind == Occupant.Kind.PAWN) {
                switch (color) {
                    case RED -> redPawns.add(occupant);
                    case GREEN -> greenPawns.add(occupant);
                    case BLUE -> bluePawns.add(occupant);
                }
            } else {
                switch (color) {
                    case RED -> redHuts.add(occupant);
                    case GREEN -> greenHuts.add(occupant);
                    case BLUE -> blueHuts.add(occupant);
                }
            }

        }
        assertEquals(bluePawns.size(), board.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(redPawns.size(), board.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(greenPawns.size(), board.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(0, board.occupantCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));

        assertEquals(blueHuts.size(), board.occupantCount(PlayerColor.BLUE, Occupant.Kind.HUT));
        assertEquals(redHuts.size(), board.occupantCount(PlayerColor.RED, Occupant.Kind.HUT));
        assertEquals(greenHuts.size(), board.occupantCount(PlayerColor.GREEN, Occupant.Kind.HUT));
        assertEquals(0, board.occupantCount(PlayerColor.YELLOW, Occupant.Kind.HUT));
    }

    @Test
    void occupantCountReturnsZeroForEmptyBoard() {
        Board board = Board.EMPTY;
        assertEquals(0, board.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
    }

    @Test
    void occupantCountReturnsZeroForBoardWithNoOccupants() {
        Board board = Board.EMPTY;

        for (int i = 0; i < 100; i++) {
            PlacedTile placedTile = new PlacedTile(
                    new Tile(i, Tile.Kind.NORMAL,
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                            new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null))),
                    PlayerColor.RED, Rotation.NONE, new Pos(i % 25 - 12, i / 25 - 12));

            board = board.withNewTile(placedTile);
        }
        assertEquals(0, board.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(0, board.occupantCount(PlayerColor.RED, Occupant.Kind.HUT));
    }

    @Test
    void couldPlaceTileWorksForStartingBoard() {
        for (int i = 0; i < 100; i++) {
            Tile tile = new Tile(i, Tile.Kind.NORMAL,
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                    new TileSide.Forest(new Zone.Forest(i, Zone.Forest.Kind.PLAIN)),
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)));

            assertTrue(startingBoard.couldPlaceTile(tile));
        }
    }

    @Test
    void couldPlaceTileReturnsFalseForTrivialExample() {
        Board b = Board.EMPTY;

        Tile tile = new Tile(100, Tile.Kind.NORMAL,
                    new TileSide.Meadow(new Zone.Meadow(101, new ArrayList<>(), null)),
                    new TileSide.Forest(new Zone.Forest(102, Zone.Forest.Kind.PLAIN)),
                    new TileSide.Meadow(new Zone.Meadow(103, new ArrayList<>(), null)),
                    new TileSide.Meadow(new Zone.Meadow(104, new ArrayList<>(), null)));

        assertFalse(b.couldPlaceTile(tile));

        b = b.withNewTile(new PlacedTile(tile, null, Rotation.NONE, Pos.ORIGIN));

        Tile tile2 = new Tile(200, Tile.Kind.NORMAL, w, w, w, w);
        assertFalse(b.couldPlaceTile(tile2));
    }

    @Test
    void couldPlaceTileWorksForWholeBoard() {
        Board board = Board.EMPTY;
        for (int i = 0; i < 624; i++) {

            Tile tile = new Tile(i, Tile.Kind.NORMAL,
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)));

            PlacedTile placedTile = new PlacedTile(tile, null, Rotation.NONE, new Pos(i % 25 - 12, i / 25 - 12));


            board = board.withNewTile(placedTile);
            assertTrue(board.couldPlaceTile(tile));
        }
    }

    @Test
    void couldPlaceTileReturnsFalseForWholeBoard() {
        Board board = Board.EMPTY;
        for (int i = 0; i < 625; i++) {

            Tile tile = new Tile(i, Tile.Kind.NORMAL,
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)),
                    new TileSide.Meadow(new Zone.Meadow(i, new ArrayList<>(), null)));

            Tile tile2 = new Tile(200, Tile.Kind.NORMAL, w, w, w, w);
            PlacedTile placedTile = new PlacedTile(tile, null, Rotation.NONE, new Pos(i % 25 - 12, i / 25 - 12));

            board = board.withNewTile(placedTile);
            assertFalse(board.couldPlaceTile(tile2));
        }
    }

    @Test
    void couldPlaceTileWorksForOutsideOfTheBoardTiles() {
        Board board = Board.EMPTY;
        Tile tile;
        tile = new Tile(
                0, Tile.Kind.NORMAL,
                new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(), null)),
                new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), null)),
                new TileSide.Meadow(new Zone.Meadow(4, new ArrayList<>(), null)));

        PlacedTile starter = new PlacedTile(tile, null, Rotation.NONE, Pos.ORIGIN);
        board = board.withNewTile(starter);

        Tile tile2 = new Tile(
                0, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)));

        assertFalse(board.couldPlaceTile(tile2));
    }

    @Test
    void couldPlaceTileWorksForRealGameScenario() {
        Board b = Board.EMPTY;
        //assertTrue(b.canAddTile(placedStartingTile));

        b = b.withNewTile(placedStartingTile);
        for (Tile tile : Tiles.TILES)
            assertTrue(b.couldPlaceTile(tile));

        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(17), PlayerColor.RED, Rotation.NONE, new Pos(-1, 0)));
        PlacedTile randomPlacedTile = new PlacedTile(Tiles.TILES.get(27), null, Rotation.NONE, new Pos(-1, 1));
        assertFalse(b.canAddTile(randomPlacedTile));

        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(27), PlayerColor.RED, Rotation.NONE, new Pos(1, 0)));

        for (Tile tile : Tiles.TILES)
            assertTrue(b.couldPlaceTile(tile));
    }

    @Test
    void couldPlaceTileReturnsFalseIfTileCannotBePlacedForRealScenario() {
        Board b = Board.EMPTY;
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(56), PlayerColor.RED, Rotation.NONE, new Pos(0, 0)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(14), PlayerColor.RED, Rotation.RIGHT, new Pos(-1, 0)));
        assertFalse(b.couldPlaceTile(Tiles.TILES.get(13)));
    }

    @Test
    void canAddTileWorksForRandomExamples() {
        Board b = startingBoard;
        PlacedTile placedTile = new PlacedTile(Tiles.TILES.get(56), null, Rotation.HALF_TURN, new Pos(1, 0));
        assertTrue(b.canAddTile(placedTile));

        PlacedTile placedTile2 = new PlacedTile(Tiles.TILES.get(27), null, Rotation.RIGHT, new Pos(0, 1));
        assertTrue(b.canAddTile(placedTile2));

        PlacedTile placedTile3 = new PlacedTile(Tiles.TILES.get(2), null, Rotation.NONE, new Pos(-1, 0));
        assertTrue(b.canAddTile(placedTile3));

        for (Rotation rot : Rotation.values()) {
            PlacedTile placedTile4 = new PlacedTile(Tiles.TILES.get(4), null, rot, new Pos(-1, 0));
            PlacedTile placedTile5 = new PlacedTile(Tiles.TILES.get(4), null, rot, new Pos(1, 0));
            PlacedTile placedTile6 = new PlacedTile(Tiles.TILES.get(4), null, rot, new Pos(0, 1));
            PlacedTile placedTile7 = new PlacedTile(Tiles.TILES.get(4), null, rot, new Pos(0, -1));

            switch (rot) {
                case NONE -> {
                    assertFalse(b.canAddTile(placedTile4));
                    assertFalse(b.canAddTile(placedTile5));
                    assertFalse(b.canAddTile(placedTile6));
                    assertFalse(b.canAddTile(placedTile7));
                }
                case RIGHT -> {
                    assertTrue(b.canAddTile(placedTile4));
                    assertFalse(b.canAddTile(placedTile5));
                    assertFalse(b.canAddTile(placedTile6));
                    assertFalse(b.canAddTile(placedTile7));
                }
                case HALF_TURN -> {
                    assertFalse(b.canAddTile(placedTile4));
                    assertTrue(b.canAddTile(placedTile5));
                    assertFalse(b.canAddTile(placedTile6));
                    assertFalse(b.canAddTile(placedTile7));
                }
                case LEFT -> {
                    assertTrue(b.canAddTile(placedTile4));
                    assertFalse(b.canAddTile(placedTile5));
                    assertTrue(b.canAddTile(placedTile6));
                    assertTrue(b.canAddTile(placedTile7));
                }
                default -> fail();
            }
        }
    }

    @Test
    void canAddTileWorksForOutsideOfBoardValues() {
        Board b = startingBoard;
        for (Tile tile : Tiles.TILES)
            assertFalse(b.canAddTile(new PlacedTile(tile, null, Rotation.HALF_TURN, new Pos(20, -14))));
    }

    @Test
    void canAddTileWorksForOccupiedSpaces() {
        for (Tile tile : Tiles.TILES)
            assertFalse(startingBoard.canAddTile(new PlacedTile(tile, null, Rotation.NONE, new Pos(0,0))));
    }

    @Test
    void canAddTileWorksForClosedSpaces() {
        Board b = startingBoard;
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(76), null, Rotation.NONE, new Pos(1, 0)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(79), null, Rotation.RIGHT, new Pos(0, 1)));
        PlacedTile closedTile = new PlacedTile(Tiles.TILES.get(77), null, Rotation.LEFT, new Pos(1, 1));
        assertTrue(b.canAddTile(closedTile));

        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(49), null, Rotation.NONE, new Pos(-1, 0)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(46), null, Rotation.NONE, new Pos(-2, 0)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(85), null, Rotation.NONE, new Pos(-2, -1)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(92), null, Rotation.NONE, new Pos(0, -1)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(35), null, Rotation.RIGHT, new Pos(0, -2)));
        assertTrue(b.canAddTile(new PlacedTile(Tiles.TILES.get(61), null, Rotation.RIGHT, new Pos(-1, -1))));
        assertFalse(b.canAddTile(new PlacedTile(Tiles.TILES.get(81), null, Rotation.HALF_TURN, new Pos(-1, -1))));
        assertFalse(b.canAddTile(new PlacedTile(Tiles.TILES.get(81), null, Rotation.LEFT, new Pos(-1, -1))));
    }

    @Test
    void lastPlacedTileWorksForTrivialExamples() {
        Board board = Board.EMPTY;
        List<PlacedTile> placedTiles = new ArrayList<>();
        int index = 0;
        for (int i = -12; i < 13; i++) {
            for (int j = -12; j < 13; j++) {
                PlacedTile newTile = new PlacedTile(new Tile(
                        index, Tile.Kind.NORMAL,
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 1, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 3, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(index * 10 + 4, new ArrayList<>(), null))),
                        null, Rotation.NONE, new Pos(i, j));

                placedTiles.add(newTile);
                board = board.withNewTile(newTile);
                index++;

                assertEquals(placedTiles.getLast(), board.lastPlacedTile());
            }
        }
    }

    @Test
    void lastPlacedTileReturnsNullForEmptyBoard() {
        assertNull(Board.EMPTY.lastPlacedTile());
    }
/*
    @Test
    void adjacentMeadowWorksForBookExample() {
        Board b = startingBoard;
        Occupant red = new Occupant(Occupant.Kind.PAWN, 850);
        Occupant blue = new Occupant(Occupant.Kind.PAWN, 760);
        Occupant green = new Occupant(Occupant.Kind.PAWN, 490);
        Occupant yellow = new Occupant(Occupant.Kind.PAWN, 462);

        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(76), PlayerColor.BLUE, Rotation.NONE, new Pos(1, 0), blue));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(79), null, Rotation.RIGHT, new Pos(0, 1)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(49), PlayerColor.GREEN, Rotation.NONE, new Pos(-1, 0), green));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(46), PlayerColor.YELLOW, Rotation.NONE, new Pos(-2, 0), yellow));
        //b = b.withNewTile(new PlacedTile(Tiles.TILES.get(62), PlayerColor.RED, Rotation.NONE, new Pos(-2, -1), red));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(92), null, Rotation.NONE, new Pos(0, -1)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(35), null, Rotation.RIGHT, new Pos(0, -2)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(61), null, Rotation.RIGHT, new Pos(-1, -1)));

        Zone.Meadow zoneWithHuntingTrap = new Zone.Meadow(921, new ArrayList<>(), Zone.SpecialPower.PIT_TRAP);
        Area<Zone.Meadow> expectedMeadow = new Area<>(
                Set.of(new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)), null),
                        zoneWithHuntingTrap,
                        new Zone.Meadow(610, List.of(new Animal(6100, Animal.Kind.MAMMOTH)), null),
                        //new Zone.Meadow(620, List.of(new Animal(6100, Animal.Kind.DEER)), null),
                        new Zone.Meadow(490, List.of(new Animal(4900, Animal.Kind.DEER)), null),
                        new Zone.Meadow(460, new ArrayList<>(), null)),
                List.of(PlayerColor.GREEN), 0);

        assertEquals(expectedMeadow, b.adjacentMeadow(new Pos(-1, -1), zoneWithHuntingTrap));
    }

 */

    @Test
    void withNewTile() {
        Board b = startingBoard;
        PlacedTile placedTile = new PlacedTile(Tiles.TILES.get(72), null, Rotation.NONE, new Pos(1, 0));
        b = b.withNewTile(placedTile);
        assertEquals(placedTile, b.tileAt(new Pos(1, 0)));
    }

    Board boardWithStartTile() {
        // Ajout de la tuile de départ
        // Tile 56
        var l1 = new Zone.Lake(56_8, 1, null);
        var a0_0 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z0 = new Zone.Meadow(56_0, List.of(a0_0), null);
        var z1 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z2 = new Zone.Meadow(56_2, List.of(), null);
        var z3 = new Zone.River(56_3, 0, l1);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.Forest(z1);
        var sS = new TileSide.Forest(z1);
        var sW = new TileSide.River(z2, z3, z0);
        Tile startTile =  new Tile(56, Tile.Kind.START, sN, sE, sS, sW);

        PlacedTile startPlacedTile = new PlacedTile(startTile,
                null,
                Rotation.NONE,
                new Pos(0, 0),
                null);

        return Board.EMPTY.withNewTile(startPlacedTile);
    }

    Board boardWithTilesAround56NoOccupant() {
        //Placement de la tuile 15 à gauche de la tuile de départ
        // Tile 15
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z1_15 = new Zone.River(15_1, 0, null);
        var z2_15 = new Zone.Meadow(15_2, List.of(), null);
        var sN_15 = new TileSide.Meadow(z0_15);
        var sE_15 = new TileSide.River(z0_15, z1_15, z2_15);
        var sS_15 = new TileSide.River(z2_15, z1_15, z0_15);
        var sW_15 = new TileSide.Meadow(z0_15);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN_15, sE_15, sS_15, sW_15);
        PlacedTile placedTile15 = new PlacedTile(tile15,
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(-1, 0),
                null);
        Board boardWithEast = boardWithStartTile().withNewTile(placedTile15);

        //Tile added North
        // Tile 8
        var l1 = new Zone.Lake(8_8, 1, null);
        var a0_0_8 = new Animal(8_0_0, Animal.Kind.MAMMOTH);
        var z0_8 = new Zone.Meadow(8_0, List.of(a0_0_8), null);
        var z1_8 = new Zone.River(8_1, 0, l1);
        var a2_0_8 = new Animal(8_2_0, Animal.Kind.DEER);
        var z2_8 = new Zone.Meadow(8_2, List.of(a2_0_8), null);
        var z3_8 = new Zone.River(8_3, 0, l1);
        var z4_8 = new Zone.Meadow(8_4, List.of(), null);
        var z5_8 = new Zone.River(8_5, 0, l1);
        var sn_8 = new TileSide.River(z0_8, z1_8, z2_8);
        var sE_8 = new TileSide.River(z2_8, z3_8, z4_8);
        var sS_8 = new TileSide.Meadow(z4_8);
        var sW_8 = new TileSide.River(z4_8, z5_8, z0_8);
        Tile tile8 = new Tile(8, Tile.Kind.NORMAL, sn_8, sE_8, sS_8, sW_8);
        PlacedTile placedTile8 = new PlacedTile(tile8,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0, -1),
                null);
        Board boardWithN_W = boardWithEast.withNewTile(placedTile8);

        //Tile added South
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31_South = new PlacedTile(tile31,
                PlayerColor.PURPLE,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        Board boardWithN_S_W = boardWithN_W.withNewTile(placedTile31_South);

        //Tile added a droite (East)
        // Tile 29
        var z0 = new Zone.Forest(29_0, Zone.Forest.Kind.PLAIN);
        var z1 = new Zone.Meadow(29_1, List.of(), null);
        var z2 = new Zone.River(29_2, 0, null);
        var z3 = new Zone.Meadow(29_3, List.of(), null);
        var sN = new TileSide.Forest(z0);
        var sE = new TileSide.River(z1, z2, z3);
        var sS = new TileSide.River(z3, z2, z1);
        var sW = new TileSide.Forest(z0);
        Tile tile29 = new Tile(29, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile29_East = new PlacedTile(tile29,
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(1, 0),
                null);
        return boardWithN_S_W.withNewTile(placedTile29_East);
    }

    Board boardWithTilesAround56Occupants() {
        //Placement de la tuile 15 à gauche de la tuile de départ
        // Tile 15
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z1_15 = new Zone.River(15_1, 0, null);
        var z2_15 = new Zone.Meadow(15_2, List.of(), null);
        var sN_15 = new TileSide.Meadow(z0_15);
        var sE_15 = new TileSide.River(z0_15, z1_15, z2_15);
        var sS_15 = new TileSide.River(z2_15, z1_15, z0_15);
        var sW_15 = new TileSide.Meadow(z0_15);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN_15, sE_15, sS_15, sW_15);
        PlacedTile placedTile15 = new PlacedTile(tile15,
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(-1, 0));
        Board boardWithEast = boardWithStartTile().withNewTile(placedTile15);
        PlacedTile occupiedPlacedTile15 = placedTile15.withOccupant(new Occupant(Occupant.Kind.HUT, 15_1));
        Board boardWithEastFinal = boardWithEast.withOccupant(occupiedPlacedTile15.occupant());

        //Tile added North
        // Tile 8
        var l1 = new Zone.Lake(8_8, 1, null);
        var a0_0_8 = new Animal(8_0_0, Animal.Kind.MAMMOTH);
        var z0_8 = new Zone.Meadow(8_0, List.of(a0_0_8), null);
        var z1_8 = new Zone.River(8_1, 0, l1);
        var a2_0_8 = new Animal(8_2_0, Animal.Kind.DEER);
        var z2_8 = new Zone.Meadow(8_2, List.of(a2_0_8), null);
        var z3_8 = new Zone.River(8_3, 0, l1);
        var z4_8 = new Zone.Meadow(8_4, List.of(), null);
        var z5_8 = new Zone.River(8_5, 0, l1);
        var sn_8 = new TileSide.River(z0_8, z1_8, z2_8);
        var sE_8 = new TileSide.River(z2_8, z3_8, z4_8);
        var sS_8 = new TileSide.Meadow(z4_8);
        var sW_8 = new TileSide.River(z4_8, z5_8, z0_8);
        Tile tile8 = new Tile(8, Tile.Kind.NORMAL, sn_8, sE_8, sS_8, sW_8);
        PlacedTile placedTile8 = new PlacedTile(tile8,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0, -1));
        Board boardWithN_W = boardWithEastFinal.withNewTile(placedTile8);
        PlacedTile occupiedPlacedTile8 = placedTile8.withOccupant(new Occupant(Occupant.Kind.PAWN, 8_4));
        Board boardWith_N_W_Final = boardWithN_W.withOccupant(occupiedPlacedTile8.occupant());

        //Tile added South
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31_South = new PlacedTile(tile31,
                PlayerColor.PURPLE,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        Board boardWithN_S_W_notOccupied = boardWith_N_W_Final.withNewTile(placedTile31_South);
        PlacedTile occupiedPlacedTile31 = placedTile31_South.withOccupant(new Occupant(Occupant.Kind.PAWN, 31_0));
        Board boardWithN_S_W = boardWithN_S_W_notOccupied.withOccupant(occupiedPlacedTile31.occupant());

        //Tile added a droite (East)
        // Tile 29
        var z0 = new Zone.Forest(29_0, Zone.Forest.Kind.PLAIN);
        var z1 = new Zone.Meadow(29_1, List.of(), null);
        var z2 = new Zone.River(29_2, 0, null);
        var z3 = new Zone.Meadow(29_3, List.of(), null);
        var sN = new TileSide.Forest(z0);
        var sE = new TileSide.River(z1, z2, z3);
        var sS = new TileSide.River(z3, z2, z1);
        var sW = new TileSide.Forest(z0);
        Tile tile29 = new Tile(29, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile29_East = new PlacedTile(tile29,
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(1, 0));
        Board boardFinal = boardWithN_S_W.withNewTile(placedTile29_East);
        PlacedTile occupiedPlacedTile29 = placedTile29_East.withOccupant(new Occupant(Occupant.Kind.PAWN, 29_2));
        return boardFinal.withOccupant(occupiedPlacedTile29.occupant());

    }

    PlacedTile startTile() {
        // Tile 56
        var l1 = new Zone.Lake(56_8, 1, null);
        var a0_0 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z0 = new Zone.Meadow(56_0, List.of(a0_0), null);
        var z1 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z2 = new Zone.Meadow(56_2, List.of(), null);
        var z3 = new Zone.River(56_3, 0, l1);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.Forest(z1);
        var sS = new TileSide.Forest(z1);
        var sW = new TileSide.River(z2, z3, z0);
        Tile startTile = new Tile(56, Tile.Kind.START, sN, sE, sS, sW);

        return new PlacedTile(startTile,
                null,
                Rotation.NONE,
                new Pos(0, 0),
                null);
    }

    //                                              **TESTS**
    // ----------------------------------------------------------------------------------------------------------------
    @Test
    void tileAtWorksWhenEmptyBoard() {
        assertNull(Board.EMPTY.tileAt(new Pos(3,4)));
    }

    @Test
    void tileAtWorksWhenPositionOutOfBounds() {
        assertNull(Board.EMPTY.tileAt(new Pos(0,20)));
        assertNull(Board.EMPTY.tileAt(new Pos(20,0)));
        assertNull(Board.EMPTY.tileAt(new Pos(-13,4)));
        assertNull(Board.EMPTY.tileAt(new Pos(3,-14)));
    }

    @Test
    void tileAtWorks() {
        PlacedTile startPlacedTile = startTile();
        PlacedTile actual = boardWithStartTile().tileAt(new Pos(0,0));
        assertEquals(startPlacedTile, actual);


        //Placement de la tuile 15 à gauche de la tuile de départ
        // Tile 15
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z1_15 = new Zone.River(15_1, 0, null);
        var z2_15 = new Zone.Meadow(15_2, List.of(), null);
        var sN_15 = new TileSide.Meadow(z0_15);
        var sE_15 = new TileSide.River(z0_15, z1_15, z2_15);
        var sS_15 = new TileSide.River(z2_15, z1_15, z0_15);
        var sW_15 = new TileSide.Meadow(z0_15);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN_15, sE_15, sS_15, sW_15);
        PlacedTile placedTile15 = new PlacedTile(tile15,
                null,
                Rotation.NONE,
                new Pos(-1, 0),
                null);

        Board boardWith15 = boardWithStartTile().withNewTile(placedTile15);
        PlacedTile actual15 = boardWith15.tileAt(new Pos(-1,0));
        assertEquals(placedTile15, actual15);

        //Placement de la tuile 31 en bas de la tuile de départ (south)
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31_South = new PlacedTile(tile31,
                null,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        Board boardWith31 = boardWithStartTile().withNewTile(placedTile31_South);
        PlacedTile actualSouth = boardWith31.tileAt(new Pos(0, 1));
        assertEquals(placedTile31_South, actualSouth);

    }

    @Test
    void CanBeAddedWorksWhenCannotBeAdded() {
        Board boardWithStartTile = boardWithStartTile();
        //Cannot be added bc TileSides do not match
        // Tile 74
        var z0 = new Zone.Meadow(74_0, List.of(), null);
        var z1 = new Zone.River(74_1, 1, null);
        var z2 = new Zone.Meadow(74_2, List.of(), null);
        var z3 = new Zone.Forest(74_3, Zone.Forest.Kind.PLAIN);
        var z4 = new Zone.Forest(74_4, Zone.Forest.Kind.PLAIN);
        var sN = new TileSide.River(z0, z1, z2);
        var sE = new TileSide.Forest(z3);
        var sS = new TileSide.River(z2, z1, z0);
        var sW = new TileSide.Forest(z4);
        Tile tile74 = new Tile(74, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile74 = new PlacedTile(tile74,
                null,
                Rotation.NONE,
                new Pos(-1, 0),
                null);

        assertFalse(boardWithStartTile.canAddTile(placedTile74));
        ///Cannot be added bc No corresponding insertion position
        PlacedTile placedTile74_NoInsertionPos = new PlacedTile(tile74,
                null,
                Rotation.NONE,
                new Pos(-10, 0),
                null);
        assertFalse(boardWithStartTile().canAddTile(placedTile74_NoInsertionPos));
    }

    @Test
    void CanBeAddedWorks() {
        // Board with start tile, trying to add a tile next to it
        Board boardWithStartTile = boardWithStartTile();
        //Tile added east
        // Tile 19
        var a0_0 = new Animal(19_0_0, Animal.Kind.DEER);
        var z0 = new Zone.Meadow(19_0, List.of(a0_0), null);
        var z1 = new Zone.River(19_1, 1, null);
        var z2 = new Zone.Meadow(19_2, List.of(), null);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.River(z0, z1, z2);
        var sS = new TileSide.River(z2, z1, z0);
        var sW = new TileSide.Meadow(z0);
        Tile tile19 = new Tile(19, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile19 = new PlacedTile(tile19,
                null,
                Rotation.NONE,
                new Pos(-1, 0),
                null);
        assertTrue(boardWithStartTile.canAddTile(placedTile19));

        //Tile added North
        // Tile 8
        var l1 = new Zone.Lake(8_8, 1, null);
        var a0_0_8 = new Animal(8_0_0, Animal.Kind.MAMMOTH);
        var z0_8 = new Zone.Meadow(8_0, List.of(a0_0_8), null);
        var z1_8 = new Zone.River(8_1, 0, l1);
        var a2_0_8 = new Animal(8_2_0, Animal.Kind.DEER);
        var z2_8 = new Zone.Meadow(8_2, List.of(a2_0_8), null);
        var z3_8 = new Zone.River(8_3, 0, l1);
        var z4_8 = new Zone.Meadow(8_4, List.of(), null);
        var z5_8 = new Zone.River(8_5, 0, l1);
        var sn_8 = new TileSide.River(z0_8, z1_8, z2_8);
        var sE_8 = new TileSide.River(z2_8, z3_8, z4_8);
        var sS_8 = new TileSide.Meadow(z4_8);
        var sW_8 = new TileSide.River(z4_8, z5_8, z0_8);
        Tile tile8 = new Tile(8, Tile.Kind.NORMAL, sn_8, sE_8, sS_8, sW_8);
        PlacedTile placedTile8 = new PlacedTile(tile8,
                null,
                Rotation.NONE,
                new Pos(0, -1),
                null);
        assertTrue(boardWithStartTile.canAddTile(placedTile8));

        //Tile added South
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31_South = new PlacedTile(tile31,
                null,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        assertTrue(boardWithStartTile.canAddTile(placedTile31_South));
        //Tile added West
        PlacedTile placedTile31_West = new PlacedTile(tile31,
                null,
                Rotation.NONE,
                new Pos(1, 0),
                null);
        assertTrue(boardWithStartTile.canAddTile(placedTile31_West));

        //Tile added east with rotation
        // Tile 42
        var z0_42 = new Zone.Forest(42_0, Zone.Forest.Kind.WITH_MENHIR);
        var z1_42 = new Zone.Meadow(42_1, List.of(), null);
        var z2_42 = new Zone.Meadow(42_2, List.of(), null);
        var sN_42 = new TileSide.Forest(z0_42);
        var sE_42 = new TileSide.Forest(z0_42);
        var sS_42 = new TileSide.Meadow(z1_42);
        var sW_42 = new TileSide.Meadow(z2_42);
        Tile tile42 = new Tile(42, Tile.Kind.NORMAL, sN_42, sE_42, sS_42, sW_42);
        PlacedTile placedTile42 = new PlacedTile(tile42,
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(1, 0),
                new Occupant(Occupant.Kind.PAWN, 422));

        assertTrue(boardWithStartTile.canAddTile(placedTile42));
    }

    @Test
    void tileWithIdWorksWhenTileNotInBoard() {
        Board boardWithStartTile = boardWithStartTile();

        assertThrows(IllegalArgumentException.class, () -> {
            boardWithStartTile.tileWithId(40);
        });

    }

    @Test
    void tileWithIdWorksOnStartTile() {
        Board boardWithStartTile = boardWithStartTile();
        PlacedTile expected = startTile();
        PlacedTile actual = boardWithStartTile.tileWithId(56);
        assertEquals(expected, actual);
    }

    @Test
    void cancelledAnimalsWorksWhenNoCancelledAnimals() {
        Board boardWithStartTile = boardWithStartTile();
        Set<Animal> expected = new HashSet<>();
        Set<Animal> actual = boardWithStartTile.cancelledAnimals();
        assertEquals(expected, actual);
    }

    //Teste + ou - withMoreCancelledAnimals aussi puisque l'on a besoin de cette méthode pour ajouter des cancelled animals
    @Test
    void cancelledAnimalsWorks() {
        Board boardWithStartTile = boardWithStartTile();
        Set<Animal> newCancelledAnimals = new HashSet<>();
        Collections.addAll(newCancelledAnimals,
                new Animal(4, Animal.Kind.TIGER),
                new Animal(5, Animal.Kind.AUROCHS));
        Board expected = boardWithStartTile.withMoreCancelledAnimals(newCancelledAnimals);
        assertEquals(newCancelledAnimals, expected.cancelledAnimals());
    }


    @Test
    void occupantsWorksWhenNoOccupants() {
        Board boardWithTiles = boardWithTilesAround56NoOccupant();
        Set<Occupant> expected = new HashSet<>();
        assertEquals(expected, boardWithTiles.occupants());
    }

    @Test
    void occupantsWorksWhenOccupants() {
        Board boardWithTiles = boardWithTilesAround56Occupants();
        Set<Occupant> expected = new HashSet<>();
        Collections.addAll(expected, new Occupant(Occupant.Kind.HUT, 15_1), new Occupant(Occupant.Kind.PAWN, 29_2), new Occupant(Occupant.Kind.PAWN, 8_4),
                new Occupant(Occupant.Kind.PAWN, 31_0));
        assertEquals(expected, boardWithTiles.occupants());
    }

    @Test
    void forestAreaThrows() {
        //Placement de la tuile 15 qui n'a pas de foret sur un emptyBoard
        // Tile 15
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z1_15 = new Zone.River(15_1, 0, null);
        var z2_15 = new Zone.Meadow(15_2, List.of(), null);
        var sN_15 = new TileSide.Meadow(z0_15);
        var sE_15 = new TileSide.River(z0_15, z1_15, z2_15);
        var sS_15 = new TileSide.River(z2_15, z1_15, z0_15);
        var sW_15 = new TileSide.Meadow(z0_15);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN_15, sE_15, sS_15, sW_15);
        PlacedTile placedTile15 = new PlacedTile(tile15,
                null,
                Rotation.NONE,
                new Pos(-1, 0),
                null);
        Board boardWith15 = Board.EMPTY.withNewTile(placedTile15);

        Zone.Forest forestZone = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);

        assertThrows(IllegalArgumentException.class, () -> {
            boardWith15.forestArea(forestZone);
            Board.EMPTY.forestArea(forestZone);
        });

        //Cherche une aire qui contient une zone qui n'est pas sur le plateau
        Board board = boardWithTilesAround56Occupants();
        assertThrows(IllegalArgumentException.class, () -> {
            board.forestArea(forestZone);
        });
    }

    @Test
    void forestAreaWorks() {
        Board board = boardWithTilesAround56Occupants();
        //Tile 29 zone foret
        var z0_29 = new Zone.Forest(29_0, Zone.Forest.Kind.PLAIN);
        var z1_56 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);

        Set<Zone.Forest> forestZones = Set.of(z0_29, z0_31, z1_56);

        Area<Zone.Forest> expected = new Area<>(forestZones, List.of(PlayerColor.PURPLE), 2);
        assertEquals(expected, board.forestArea(z0_29));
    }

    @Test
    void meadowAreaThrows() {
        //Cherche une aire qui contient une zone qui n'est pas sur le plateau
        Board board = boardWithTilesAround56Occupants();
        Zone.Meadow meadowZone = new Zone.Meadow(3, new ArrayList<>(), null);
        assertThrows(IllegalArgumentException.class, () -> {
            board.meadowArea(meadowZone);
            Board.EMPTY.meadowArea(meadowZone);
        });
    }

    @Test
    void meadowAreaWorks() {
        Board board = boardWithTilesAround56Occupants();
        //Tile 15 zone de pré
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var a0_0_56 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z0_56 = new Zone.Meadow(56_0, List.of(a0_0_56), null);
        var z4_8 = new Zone.Meadow(8_4, List.of(), null);

        Set<Zone.Meadow> meadowZones = Set.of(z0_15, z0_56, z4_8);
        Area<Zone.Meadow> expected = new Area<>(meadowZones, List.of(PlayerColor.YELLOW), 5);
        assertEquals(expected, board.meadowArea(z0_15));
    }

    @Test
    void riverAreaThrows() {
        //Placement de la tuile 30 qui n'a pas de foret sur un emptyBoard
        // Tile 30
        var a0_0 = new Animal(30_0_0, Animal.Kind.DEER);
        var z0 = new Zone.Meadow(30_0, List.of(a0_0), null);
        var z1 = new Zone.Forest(30_1, Zone.Forest.Kind.WITH_MENHIR);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.Meadow(z0);
        var sS = new TileSide.Forest(z1);
        var sW = new TileSide.Forest(z1);
        Tile tile30 = new Tile(30, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile30 = new PlacedTile(tile30,
                null,
                Rotation.NONE,
                new Pos(0, 0),
                null);
        Board boardWith30 = Board.EMPTY.withNewTile(placedTile30);
        Zone.River riverZone = new Zone.River(40, 4, null);

        assertThrows(IllegalArgumentException.class, () -> {
            boardWith30.riverArea(riverZone);
            Board.EMPTY.riverArea(riverZone);
        });

        //Cherche une aire qui contient une zone qui n'est pas sur le plateau
        Board board = boardWithTilesAround56Occupants();
        assertThrows(IllegalArgumentException.class, () -> {
            board.riverArea(riverZone);
        });
    }

    @Test
    void riverAreaWorks() {
        Board board = boardWithTilesAround56Occupants();
        //Tuile 15 zone de rivière
        var z1_15 = new Zone.River(15_1, 0, null);
        var l1_56 = new Zone.Lake(56_8, 1, null);
        var z3_56 = new Zone.River(56_3, 0, l1_56);
        Set<Zone.River> riverZones15 = Set.of(z3_56, z1_15);
        //TODO bien d'accord que l'Area<Zone.River> contient que des PAWN et pas les HUT ?
        Area<Zone.River> expected15 = new Area<>(riverZones15, new ArrayList<>(), 1);
        //Area<Zone.River> expected = new Area<>(riverZones15, List.of(PlayerColor.RED), 1);
        assertEquals(expected15, board.riverArea(z1_15));


        //Tuile 29 zone de rivière
        var z2 = new Zone.River(29_2, 0, null);
        Set<Zone.River> riverZones29 = Set.of(z2);
        Area<Zone.River> expected29 = new Area<>(riverZones29, List.of(PlayerColor.RED), 2);
        assertEquals(expected29, board.riverArea(z2));


    }

    @Test
    void riverSystemAreaThrows() {
        //Placement tuile 31 qui n'a ni lac ni rivière sur un empty board
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31 = new PlacedTile(tile31,
                null,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        Board board = Board.EMPTY.withNewTile(placedTile31);
        Zone.Water riverSystemZone = new Zone.River(3, 4 , null);
        assertThrows(IllegalArgumentException.class, () -> {
            board.riverSystemArea(riverSystemZone);
            Board.EMPTY.riverSystemArea(riverSystemZone);
        });

        //Cherche l'aire d'une zone qui n'est pas sur le plateau
        Board fullBoard = boardWithTilesAround56Occupants();
        assertThrows(IllegalArgumentException.class, () -> {
            fullBoard.riverSystemArea(riverSystemZone);
        });
    }

    @Test
    void riverSystemAreaWorks() {
        Board board = boardWithTilesAround56Occupants();
        //Tuile 15 zone de rivière
        var z1_15 = new Zone.River(15_1, 0, null);
        var l1_56 = new Zone.Lake(56_8, 1, null);
        var z3_56 = new Zone.River(56_3, 0, l1_56);

        Set<Zone.Water> riverSystemZones15_56 = Set.of(z3_56, z1_15, l1_56);
        Area<Zone.Water> expected15 = new Area<>(riverSystemZones15_56, List.of(PlayerColor.RED) , 1);
        assertEquals(expected15, board.riverSystemArea(z1_15));

        //Tuile 8 lac
        var l1 = new Zone.Lake(8_8, 1, null);
        var z1_8 = new Zone.River(8_1, 0, l1);
        var z3_8 = new Zone.River(8_3, 0, l1);
        var z5_8 = new Zone.River(8_5, 0, l1);
        Set<Zone.Water> zones = new HashSet<>();
        Collections.addAll(zones, l1, z1_8, z3_8, z5_8);
        Area<Zone.Water> expected8 = new Area<>(zones, new ArrayList<>(), 3);
        assertEquals(expected8, board.riverSystemArea(l1));

        //Tuile 29 rivière
        var z2 = new Zone.River(29_2, 0, null);
        Area<Zone.Water> expected29 = new Area<>(Set.of(z2), new ArrayList<>(), 2);
    }

    @Test
    void withMoreCancelledAnimalsWorksTrivially() {
        Board board = boardWithTilesAround56Occupants();
        Board actual = board.withMoreCancelledAnimals(Set.of());

        assertEquals(board.cancelledAnimals(), actual.cancelledAnimals());
    }

    @Test
    void withMoreCancelledAnimalsWorks() {
        Board board = boardWithTilesAround56Occupants();

        //Adding a first set of cancelled animals
        Animal deer = new Animal(23, Animal.Kind.DEER);
        Animal auroch = new Animal(140, Animal.Kind.AUROCHS);
        Set<Animal> addedAnimals = Set.of(deer, auroch);
        Board actual = board.withMoreCancelledAnimals(addedAnimals);
        assertEquals(addedAnimals, actual.cancelledAnimals());

        //Adding a second set of cancelled animals
        Animal mammoth = new Animal(30, Animal.Kind.MAMMOTH);
        Animal tiger = new Animal(300, Animal.Kind.TIGER);
        Animal deer2 = new Animal(9, Animal.Kind.DEER);
        Set<Animal> addedAnimals2 = Set.of(mammoth, tiger, deer2);
        Set<Animal> expectedCancelledAnimals = new HashSet<>();
        expectedCancelledAnimals.addAll(addedAnimals);
        expectedCancelledAnimals.addAll(addedAnimals2);
        Board actual2 = actual.withMoreCancelledAnimals(addedAnimals2);
        assertEquals(expectedCancelledAnimals, actual2.cancelledAnimals());
    }

    @Test
    void withoutGatherersOrFisherWorksTrivially() {
        Board boardWithNoOccupants = boardWithTilesAround56NoOccupant();

        //Zone de forets
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        Area<Zone.Forest> forestArea31 = boardWithNoOccupants.forestArea(z0_31);
        //Set d'aires de foret
        Set<Area<Zone.Forest>> forestAreaSet = Set.of(forestArea31);

        //Area de river tile 15
        var z1_15 = new Zone.River(15_1, 0, null);
        Area<Zone.River> associatedArea15 =  boardWithNoOccupants.riverArea(z1_15);
        //Area de river tile 8
        var l1 = new Zone.Lake(8_8, 1, null);
        var z3_8 = new Zone.River(8_3, 0, l1);
        Area<Zone.River> associatedArea8 =  boardWithNoOccupants.riverArea(z3_8);
        //Set d'aires de river
        Set<Area<Zone.River>> riverAreaSet = Set.of(associatedArea15, associatedArea8);

        Board actual = boardWithNoOccupants.withoutGatherersOrFishersIn(forestAreaSet, riverAreaSet);

        assertEquals(boardWithNoOccupants.occupants(), actual.occupants());

    }

    @Test
    void withoutGatherersOrFisherWorksWhenRemoving1ForestOccAnd1RiverOcc() {
        Board boardWithOccupants = boardWithTilesAround56Occupants();

        //Zone de foret
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        Area<Zone.Forest> forestArea = boardWithOccupants.forestArea(z0_31);
        Set<Area<Zone.Forest>> forestAreaSet = Set.of(forestArea);

        //Aire de river
        var z2 = new Zone.River(29_2, 0, null);
        Area<Zone.River> riverArea = boardWithOccupants.riverArea(z2);
        Set<Area<Zone.River>> riverAreaSet = Set.of(riverArea);

        Board boardwithoutGatherers = boardWithOccupants.withoutGatherersOrFishersIn(forestAreaSet, riverAreaSet);

        Set<Occupant> expected = Set.of(new Occupant(Occupant.Kind.PAWN, 8_4), new Occupant(Occupant.Kind.HUT, 15_1));

        assertEquals(expected, boardwithoutGatherers.occupants());
    }

    @Test
    void equalsWorks() {
        Board board = Board.EMPTY;
        assertTrue(board.equals(Board.EMPTY));
        Board board2 = board.withNewTile(startTile());
        assertNotEquals(board, board2);
        assertFalse(board.equals(null));
        assertFalse(board.equals(new HashSet<>()));
        assertTrue(board2.equals(board.withNewTile(startTile())));
    }
}
