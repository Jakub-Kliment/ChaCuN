package ch.epfl.chacun;

import java.util.*;

import ch.epfl.chacun.tile.Tiles;
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
            Occupant occupant = new Occupant(Occupant.Kind.PAWN, i * 10 + i);
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

            Occupant occupant = new Occupant(Occupant.Kind.PAWN, i * 10 + i);
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

            Occupant occupant = new Occupant(occupantKind, i * 10 + i);

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
        for (int i = 0; i < 625; i++) {

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
                        new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(2, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(3, new ArrayList<>(), null)),
                        new TileSide.Meadow(new Zone.Meadow(4, new ArrayList<>(), null))),
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
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(85), PlayerColor.RED, Rotation.NONE, new Pos(-2, -1), red));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(92), null, Rotation.NONE, new Pos(0, -1)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(35), null, Rotation.RIGHT, new Pos(0, -2)));
        b = b.withNewTile(new PlacedTile(Tiles.TILES.get(61), null, Rotation.RIGHT, new Pos(-1, -1)));

        Zone.Meadow zoneWithHuntingTrap = new Zone.Meadow(921, new ArrayList<>(), Zone.SpecialPower.PIT_TRAP);
        Area<Zone.Meadow> expectedMeadow = new Area<>(
                Set.of(new Zone.Meadow(560, List.of(new Animal(0, Animal.Kind.AUROCHS)), null),
                        zoneWithHuntingTrap,
                        new Zone.Meadow(610, List.of(new Animal(0, Animal.Kind.MAMMOTH)), null),
                        new Zone.Meadow(850, List.of(new Animal(0, Animal.Kind.TIGER)), Zone.SpecialPower.WILD_FIRE),
                        new Zone.Meadow(490, List.of(new Animal(0, Animal.Kind.DEER)), null),
                        new Zone.Meadow(460, new ArrayList<>(), null)),
                List.of(PlayerColor.GREEN), 4);

        assertEquals(expectedMeadow, b.adjacentMeadow(new Pos(-1, -1), zoneWithHuntingTrap));
    }
}
