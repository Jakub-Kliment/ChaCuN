package ch.epfl.chacun.etape5;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private final Zone.Forest forest1 = new Zone.Forest(562, Zone.Forest.Kind.WITH_MENHIR);
    private final Zone.Forest forest2 = new Zone.Forest(422, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest3 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest4 = new Zone.Forest(9, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest5 = new Zone.Forest(274, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest6 = new Zone.Forest(391, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest7 = new Zone.Forest(392, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest8 = new Zone.Forest(452, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest9 = new Zone.Forest(423, Zone.Forest.Kind.PLAIN);
    private final Zone.Forest forest10 = new Zone.Forest(235, Zone.Forest.Kind.PLAIN);
    private final Zone.Meadow meadow4 = new Zone.Meadow(171, List.of(), null);
    private final Zone.Meadow meadow5 = new Zone.Meadow(173, new ArrayList<>(
            new ArrayList<>(List.of(new Animal(66, Animal.Kind.DEER)))
    ), null);
    private final Zone.Meadow meadow7 = new Zone.Meadow(176, new ArrayList<>(
            List.of(new Animal(13, Animal.Kind.TIGER))), null);
    private final Zone.Meadow meadow6 = new Zone.Meadow(423, new ArrayList<>(), null);
    private final Zone.Meadow meadow8 = new Zone.Meadow(561, new ArrayList<>(List.of(
            new Animal(20, Animal.Kind.AUROCHS))), null);
    private final Zone.Meadow meadow9 = new Zone.Meadow(563, new ArrayList<>(
            List.of()), null);
    private final Zone.Meadow meadow10 = new Zone.Meadow(271, new ArrayList<>(), null);
    private final Zone.Meadow meadow11 = new Zone.Meadow(273, new ArrayList<>(
            List.of(new Animal(10, Animal.Kind.DEER))), null);
    private final Zone.Meadow meadow12 = new Zone.Meadow(455, new ArrayList<>(
            List.of(new Animal(11, Animal.Kind.TIGER))), null);
    private final Zone.Meadow meadow13 = new Zone.Meadow(456, new ArrayList<>(
            List.of(new Animal(12, Animal.Kind.AUROCHS))), null);
    private final Zone.Meadow meadow14 = new Zone.Meadow(232, new ArrayList<>(
            List.of(new Animal(13, Animal.Kind.DEER))), null);
    private final Zone.Meadow meadow15 = new Zone.Meadow(701, new ArrayList<>(
            List.of(new Animal(14, Animal.Kind.TIGER))), null);
    private final Zone.Meadow meadow16 = new Zone.Meadow(702, new ArrayList<>(
            List.of(new Animal(15, Animal.Kind.AUROCHS))), null);
    private final Zone.Meadow meadow17 = new Zone.Meadow(711, new ArrayList<>(
            List.of(new Animal(16, Animal.Kind.DEER))), null);
    private final Zone.Meadow meadow18 = new Zone.Meadow(712, new ArrayList<>(
            List.of(new Animal(17, Animal.Kind.TIGER))), null);
    private final Zone.Meadow meadow19 = new Zone.Meadow(721, new ArrayList<>(
            List.of(new Animal(18, Animal.Kind.AUROCHS))), null);
    private final Zone.Meadow meadow20 = new Zone.Meadow(722, new ArrayList<>(
            List.of(new Animal(19, Animal.Kind.DEER))), null);
    private final Zone.Meadow meadow21 = new Zone.Meadow(731, new ArrayList<>(
            List.of(new Animal(20, Animal.Kind.TIGER))), null);
    private final Zone.Lake lake6 = new Zone.Lake(569, 20, null);
    private final Zone.Lake lake7 = new Zone.Lake(427, 3, null);
    private final Zone.Lake lake8 = new Zone.Lake(728, 0, null);
    private final Zone.Lake lake9 = new Zone.Lake(739, 0, Zone.SpecialPower.LOGBOAT);
    private final Zone.River river1 = new Zone.River(172, 0, null);
    private final Zone.River river2 = new Zone.River(174, 0, null);
    private final Zone.River river3 = new Zone.River(564, 0, lake6);
    private final Zone.River river4 = new Zone.River(426, 0, null);
    private final Zone.River river5 = new Zone.River(272, 0, null);
    private final Zone.River river6 = new Zone.River(706, 0, null);
    private final Zone.River river7 = new Zone.River(716, 0, null);
    private final Zone.River river8 = new Zone.River(726, 0, null);
    private final Zone.River river9 = new Zone.River(736, 0, lake9);
    private final TileSide[] sides17 = new TileSide[] {
            new TileSide.River(meadow4, river1, meadow5), new TileSide.River(meadow5, river1, meadow4),
            new TileSide.River(meadow4, river2, meadow7), new TileSide.River(meadow7, river2, meadow4)
    };
    private final TileSide[] sides56 = new TileSide[] {
            new TileSide.Meadow(meadow8), new TileSide.Forest(forest1),
            new TileSide.Forest(forest1), new TileSide.River(meadow9, river3, meadow8)
    };
    private final TileSide[] sides27 = new TileSide[] {
            new TileSide.Meadow(meadow10), new TileSide.River(meadow10, river5, meadow11),
            new TileSide.River(meadow11, river5, meadow10), new TileSide.Forest(forest5)
    };
    private final TileSide[] sides39 = new TileSide[] {
            new TileSide.Forest(forest3), new TileSide.Forest(forest4),
            new TileSide.Forest(forest6), new TileSide.Forest(forest7)
    };
    private final TileSide[] sides42 = new TileSide[] {
            new TileSide.Forest(forest9), new TileSide.Forest(forest9),
            new TileSide.River(meadow6, river4, meadow6), new TileSide.Meadow(meadow6)
    };
    private final TileSide[] sides45 = new TileSide[] {
            new TileSide.Meadow(meadow13), new TileSide.Meadow(meadow12),
            new TileSide.Meadow(meadow13), new TileSide.Meadow(meadow13)
    };
    private final TileSide[] sides23 = new TileSide[] {
            new TileSide.Forest(forest10), new TileSide.Meadow(meadow14),
            new TileSide.Meadow(meadow14), new TileSide.Meadow(meadow14)
    };
    private final TileSide[] sides70 = new TileSide[] {
            new TileSide.Meadow(meadow15), new TileSide.Meadow(meadow15),
            new TileSide.River(meadow15, river6, meadow16), new TileSide.River(meadow16, river6, meadow15)
    };
    private final TileSide[] sides71 = new TileSide[] {
            new TileSide.River(meadow17, river7, meadow18), new TileSide.River(meadow18, river7, meadow17),
            new TileSide.Meadow(meadow17), new TileSide.Meadow(meadow17)
    };
    private final TileSide[] sides72 = new TileSide[] {
            new TileSide.Meadow(meadow19), new TileSide.River(meadow19, river8, meadow19),
            new TileSide.River(meadow19, river8, meadow19), new TileSide.Meadow(meadow20),
    };
    private final TileSide[] sides73 = new TileSide[] {
            new TileSide.River(meadow21, river9, meadow21), new TileSide.Meadow(meadow21),
            new TileSide.Meadow(meadow21), new TileSide.Meadow(meadow21)
    };
    private final Tile UnaddableTile = new Tile(21, Tile.Kind.NORMAL, sides27[0], sides27[1], sides27[2], sides27[3]);
    private final Tile tile17 = new Tile(17, Tile.Kind.NORMAL, sides17[0], sides17[1], sides17[2], sides17[3]);
    private final Tile tile56 = new Tile(56, Tile.Kind.START, sides56[0], sides56[1], sides56[2], sides56[3]);
    private final Tile tile27 = new Tile(27, Tile.Kind.NORMAL, sides27[0], sides27[1], sides27[2], sides27[3]);
    private final Tile tile42 = new Tile(42, Tile.Kind.NORMAL, sides42[0], sides42[1], sides42[2], sides42[3]);
    private final Tile tile45 = new Tile(45, Tile.Kind.NORMAL, sides45[0], sides45[1], sides45[2], sides45[3]);
    private final Tile tile23 = new Tile(23, Tile.Kind.NORMAL, sides23[0], sides23[1], sides23[2], sides23[3]);
    private final Tile tile70 = new Tile(70, Tile.Kind.NORMAL, sides70[0], sides70[1], sides70[2], sides70[3]);
    private final Tile tile71 = new Tile(71, Tile.Kind.NORMAL, sides71[0], sides71[1], sides71[2], sides71[3]);
    private final Tile tile72 = new Tile(72, Tile.Kind.NORMAL, sides72[0], sides72[1], sides72[2], sides72[3]);
    private final Tile tile73 = new Tile(73, Tile.Kind.NORMAL, sides73[0], sides73[1], sides73[2], sides73[3]);
    private final Tile withoudMeadowTile = new Tile(39, Tile.Kind.NORMAL, sides39[0], sides39[1], sides39[2], sides39[3]);
    private final PlacedTile placedTile56 = new PlacedTile(tile56, null, Rotation.NONE, new Pos(0, 0), null);
    private final PlacedTile UnaddablePlacedTile = new PlacedTile(UnaddableTile, PlayerColor.RED, Rotation.NONE, new Pos(1, 4), null);
    private final PlacedTile placedTile17 = new PlacedTile(tile17, PlayerColor.RED, Rotation.NONE, new Pos(-1, 0), null);
    private final PlacedTile placedTile27 = new PlacedTile(tile27, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 0), null);
    private final PlacedTile placedTile42 = new PlacedTile(tile42, PlayerColor.RED, Rotation.NONE, new Pos(0, 1), null);
    private final PlacedTile placedTile45 = new PlacedTile(tile45, PlayerColor.RED, Rotation.NONE, new Pos(0, -1), null);
    private final PlacedTile placedTile23 = new PlacedTile(tile23, PlayerColor.YELLOW, Rotation.NONE, new Pos(0, 1), null);
    private final PlacedTile placedTile70 = new PlacedTile(tile70, PlayerColor.YELLOW, Rotation.NONE, new Pos(-1, -1), null);
    private final PlacedTile placedTile71 = new PlacedTile(tile71, PlayerColor.YELLOW, Rotation.NONE, new Pos(-2, 0), null);
    private final PlacedTile placedTile72 = new PlacedTile(tile72, PlayerColor.YELLOW, Rotation.NONE, new Pos(-2, -1), null);
    private final PlacedTile placedTile73 = new PlacedTile(tile73, PlayerColor.GREEN, Rotation.NONE, new Pos(-1, 1), null);
    private final PlacedTile onlyForestPlacedTile = new PlacedTile(withoudMeadowTile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 0), null);
    private final PlacedTile[] placedTiles = new PlacedTile[625]; {{
            placedTiles[311] = placedTile17;
            placedTiles[312] = placedTile56;
            placedTiles[313] = placedTile27;
            placedTiles[314] = placedTile42;
            placedTiles[315] = placedTile45;
    }}

    private final int[] id = new int[625]; {{
            id[311] = 12;
            id[312] = 56;
            id[313] = 21;
            id[314] = 42;
            id[315] = 45;
    }}

    private final Board board1 = Board.EMPTY;
    private final Board board2 = Board.EMPTY;

    @Test
    void tileAt() {
        assertEquals(Tile.Kind.START, Objects.requireNonNull(board1.withNewTile(placedTile56).tileAt(new Pos(0, 0))).tile().kind());

    }
    @Test
    void tileAtIsCorrectlyDefined() {
        assertEquals(Tile.Kind.START, board1.withNewTile(placedTile56).tileAt(new Pos(0, 0)).tile().kind());

        assertEquals(placedTile56, board1.withNewTile(placedTile56).tileWithId(56));
        assertEquals(UnaddablePlacedTile, board1.withNewTile(UnaddablePlacedTile).tileWithId(21));
        assertEquals(placedTile17, board1.withNewTile(placedTile17).tileWithId(17));

        assertNull(board1.withNewTile(placedTile56).tileAt(new Pos(1, 4)));
        assertNull(board2.withNewTile(UnaddablePlacedTile).tileAt(new Pos(0, 0)));
    }

    @Test
    void tileWithIdIsCorrectlyDefined() {
        assertEquals(placedTile56, board1.withNewTile(placedTile56).tileWithId(56));
        assertEquals(UnaddablePlacedTile, board1.withNewTile(UnaddablePlacedTile).tileWithId(21));
        assertEquals(placedTile17, board1.withNewTile(placedTile17).tileWithId(17));

        assertThrows(IllegalArgumentException.class, () -> board1.tileWithId(60));
        assertThrows(IllegalArgumentException.class, () -> board2.tileWithId(0));
    }

    @Test
    void cancelledAnimalsIsCorrectlyDefined() {
        assertEquals(0, board1.cancelledAnimals().size());
        assertEquals(0, board2.cancelledAnimals().size());

        Set<Animal> cancelledAnimals = new HashSet<>(Set.of(new Animal(10, Animal.Kind.AUROCHS)));
        Board boardWithCancelledAnimals = board1.withNewTile(placedTile56).withMoreCancelledAnimals(cancelledAnimals);

        assertEquals(cancelledAnimals, boardWithCancelledAnimals.cancelledAnimals());

        assertNotEquals(cancelledAnimals, boardWithCancelledAnimals.withMoreCancelledAnimals(
                Set.of(new Animal(20, Animal.Kind.DEER))
        ).cancelledAnimals());

        cancelledAnimals.add(new Animal(20, Animal.Kind.DEER));
        assertEquals(cancelledAnimals, boardWithCancelledAnimals.withMoreCancelledAnimals(
                Set.of(new Animal(20, Animal.Kind.DEER))
        ).cancelledAnimals());
    }

    @Test
    void withOccupantIsCorrectlyDefined() {
        assertEquals(0, board1.occupants().size());

        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, 568);
        assertEquals(Set.of(occupant1), board1.withNewTile(placedTile56).withNewTile(placedTile17).withOccupant(occupant1).occupants());
        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN, 568);;
        assertThrows((IllegalArgumentException.class), () -> board1.withNewTile(placedTile56).withNewTile(placedTile17).withOccupant(occupant1).withOccupant(occupant2));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).withNewTile(placedTile17).withOccupant(occupant1).withOccupant(occupant1));

        Occupant occupant3 = new Occupant(Occupant.Kind.PAWN, 173);
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).withNewTile(placedTile17).withOccupant(occupant1).withOccupant(occupant3));


        Occupant occupant4 = new Occupant(Occupant.Kind.HUT, 739);
        Occupant occupant5 = new Occupant(Occupant.Kind.PAWN, 273);

        assertEquals(Set.of(occupant1, occupant4, occupant5), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile73).withNewTile(placedTile27).withOccupant(occupant1).withOccupant(occupant4).withOccupant(occupant5).occupants());
        assertEquals(Set.of(occupant1, occupant4), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile73).withNewTile(placedTile27).withOccupant(occupant1).withOccupant(occupant4).occupants());
        assertEquals(Set.of(occupant3, occupant4), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile73).withNewTile(placedTile27).withOccupant(occupant3).withOccupant(occupant4).occupants());
    }

    @Test
    void withoutOccupantIsCorrectlyDefined() {
        assertEquals(0, board1.occupants().size());

        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, 567);
        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN, 174);

        assertThrows(IllegalArgumentException.class, () -> board1.withoutOccupant(occupant1));
        assertEquals(Set.of(), board1.withNewTile(placedTile56).withNewTile(placedTile17)
                .withOccupant(occupant1).withoutOccupant(occupant1).occupants());

        assertEquals(Set.of(), board1.withNewTile(placedTile56).withNewTile(placedTile17)
                .withOccupant(occupant1).withoutOccupant(occupant1).occupants());
        assertEquals(Set.of(occupant1), board1.withNewTile(placedTile56).withNewTile(placedTile17)
                .withOccupant(occupant1).withOccupant(occupant2).withoutOccupant(occupant2).occupants());
    }
    @Test
    void occupantsIsCorrectlyDefined() {
        assertEquals(0, board1.occupants().size());
        assertEquals(0, board2.occupants().size());

        Board boardWithOccupants = board1.withNewTile(placedTile56).withNewTile(placedTile17);
        Occupant withTileOccupant1 = new Occupant(Occupant.Kind.PAWN, 567);
        Occupant withTileOccupant2 = new Occupant(Occupant.Kind.HUT, 174);

        assertEquals(Set.of(withTileOccupant1), boardWithOccupants.withOccupant(withTileOccupant1).occupants());
        assertEquals(Set.of(), boardWithOccupants.withoutOccupant(withTileOccupant1).occupants());
        assertEquals(Set.of(withTileOccupant1, withTileOccupant2), boardWithOccupants
                .withOccupant(withTileOccupant1).withOccupant(withTileOccupant2).occupants());
        assertEquals(Set.of(), board1.occupants());
    }

    @Test
    void forestAreaIsCorrectlyDefined() {
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).forestArea(forest1));
        assertThrows(IllegalArgumentException.class, () -> board1.forestArea(forest5));

        Area<Zone.Forest> forestArea1 = new Area<>(Set.of(forest1), List.of(), 2);
        Area<Zone.Forest> forestArea2 = new Area<>(Set.of(forest5), List.of(), 1);
        Area<Zone.Forest> forestArea = forestArea1.connectTo(forestArea2);

        assertEquals(forestArea, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).forestArea(forest1));
        assertEquals(forestArea, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).forestArea(forest5));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).forestArea(forest3));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).forestArea(forest4));
        assertEquals(forestArea1, board1.withNewTile(placedTile56).forestArea(forest1));
}

    @Test
    void meadowAreaIsCorrectlyDefined() {
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).meadowArea(meadow8));
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).meadowArea(meadow9));
        assertThrows(IllegalArgumentException.class, () -> board1.meadowArea(meadow5));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).meadowArea(meadow4));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile17).meadowArea(meadow10));


        Area<Zone.Meadow> meadowArea1 = new Area<>(Set.of(meadow4), List.of(), 4);
        Area<Zone.Meadow> meadowArea2 = new Area<>(Set.of(meadow5), List.of(), 2);
        Area<Zone.Meadow> meadowArea3 = new Area<>(Set.of(meadow7), List.of(), 2);
        Area<Zone.Meadow> meadowArea4 = new Area<>(Set.of(meadow8), List.of(), 2);
        Area<Zone.Meadow> meadowArea5 = new Area<>(Set.of(meadow9), List.of(), 1);

        Area<Zone.Meadow> meadow1 = meadowArea1.connectTo(meadowArea5);
        Area<Zone.Meadow> meadow2 = meadowArea2.connectTo(meadowArea4);

        assertEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow4),
                board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow9));

        board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow4);
        assertEquals(meadow1, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow4));
        assertEquals(meadow1, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow9));

        assertEquals(meadow2, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow5));
        assertEquals(meadowArea3, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow7));

        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowArea(meadow6));
        assertThrows(IllegalArgumentException.class, () ->board1.withNewTile(placedTile56).meadowArea(meadow4));
    }

    @Test
    void riverAreaIsCorrectlyDefined() {
        assertDoesNotThrow(() -> board1.withNewTile(placedTile17).riverArea(river1));
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).riverArea(river3));
        assertThrows(IllegalArgumentException.class, () -> board1.riverArea(river2));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile56).riverArea(river5));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile27).riverArea(river1));

        assertEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverArea(river1),
                board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverArea(river3));
        assertEquals(new Area<>(Set.of(river1, river3), List.of(), 1), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverArea(river1));


        assertEquals(new Area<>(Set.of(river3), List.of(), 1), board1.withNewTile(placedTile56).riverArea(river3));
        assertEquals(new Area<>(Set.of(river2), List.of(), 2), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverArea(river2));
        assertEquals(new Area<>(Set.of(river5), List.of(), 2), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverArea(river5));
        assertEquals(new Area<>(Set.of(river5), List.of(), 2), board1.withNewTile(placedTile56).withNewTile(placedTile27).riverArea(river5));
        assertEquals(new Area<>(Set.of(river5), List.of(), 2), board1.withNewTile(placedTile27).riverArea(river5));
    }

    @Test
    void riverSystemAreaIsCorrectlyDefined() {
        assertDoesNotThrow(() -> board1.withNewTile(placedTile17).riverSystemArea(river1));
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).riverSystemArea(lake6));
        assertDoesNotThrow(() -> board1.withNewTile(placedTile56).withNewTile(placedTile17).riverSystemArea(river3));
        assertThrows(IllegalArgumentException.class, () -> board1.riverSystemArea(lake7));
        assertThrows(IllegalArgumentException.class, () -> board1.withNewTile(placedTile27).riverSystemArea(river2));

        assertEquals(new Area<>(Set.of(lake6, river3, river1), List.of(), 1),
                board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverSystemArea(lake6));
        assertEquals(new Area<>(Set.of(river1, river3, lake6), List.of(), 1), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverSystemArea(river1));

        assertEquals(new Area<>(Set.of(river3, lake6), List.of(), 1), board1.withNewTile(placedTile56).riverSystemArea(river3));
        assertEquals(new Area<>(Set.of(river2), List.of(), 2), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverSystemArea(river2));
        assertEquals(new Area<>(Set.of(river5), List.of(), 2), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverSystemArea(river5));
        assertEquals(new Area<>(Set.of(river5), List.of(), 2), board1.withNewTile(placedTile56).withNewTile(placedTile27).riverSystemArea(river5));
        assertEquals(new Area<>(Set.of(river5), List.of(), 2), board1.withNewTile(placedTile27).riverSystemArea(river5));
    }

    @Test
    void meadowAreasIsCorrectlyDefined() {
        assertEquals(new HashSet<>(List.of(new Area<>(Set.of(meadow8), List.of(), 2), new Area<>(Set.of(meadow9), List.of(), 1))),
                board1.withNewTile(placedTile56).meadowAreas());
        assertEquals(new HashSet<>(List.of(new Area<>(Set.of(meadow4, meadow9), List.of(), 3),
                        new Area<>(Set.of(meadow7), List.of(), 2),
                        new Area<>(Set.of(meadow5, meadow8), List.of(), 2))),
                board1.withNewTile(placedTile56).withNewTile(placedTile17).meadowAreas());
        assertEquals(new HashSet<>(List.of(new Area<>(Set.of(meadow4, meadow9), List.of(), 3),
                        new Area<>(Set.of(meadow7), List.of(), 2),
                        new Area<>(Set.of(meadow5, meadow8), List.of(), 2),
                        new Area<>(Set.of(meadow10), List.of(), 3),
                        new Area<>(Set.of(meadow11), List.of(), 2))),
                board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).meadowAreas()); // 2 and 3
    }

    @Test
    void riverSystemAreasIsCorrectlyDefined() {
        assertEquals(new HashSet<>(List.of(
                new Area<>(Set.of(river3, lake6), List.of(), 1))),
                board1.withNewTile(placedTile56).riverSystemAreas());
        assertEquals(new HashSet<>(List.of(
                new Area<>(Set.of(lake6, river3, river1), List.of(), 1),
                        new Area<>(Set.of(river2), List.of(), 2))),
                board1.withNewTile(placedTile56).withNewTile(placedTile17).riverSystemAreas());
        assertEquals(new HashSet<>(List.of(
                new Area<>(Set.of(lake6, river3, river1), List.of(), 1),
                        new Area<>(Set.of(river2), List.of(), 2),
                        new Area<>(Set.of(river5), List.of(), 2))),
                board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riverSystemAreas());
    }

    @Test
    void adjacentMeadow() {
         assertDoesNotThrow(() -> board1.withNewTile(placedTile56).adjacentMeadow(new Pos(0, 0), meadow8));
       Area<Zone.Meadow> meadowArea = new Area<>(Set.of(meadow8, meadow5, meadow13), List.of(), 0);
       assertEquals(meadowArea, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27)
               .withNewTile(placedTile42).withNewTile(placedTile45).adjacentMeadow(new Pos(0, 0), meadow8));

       meadowArea = new Area<>(Set.of(meadow6), List.of(), 0);
         assertEquals(meadowArea, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27)
                .withNewTile(placedTile42).withNewTile(placedTile45).adjacentMeadow(new Pos(0, 1), meadow6));
    }

    @Test
    void occupantCount() {
        for (PlayerColor color : PlayerColor.ALL) {
            assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27)
                    .withNewTile(placedTile42).withNewTile(placedTile45).occupantCount(color, Occupant.Kind.HUT));
            assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27)
                    .withNewTile(placedTile42).withNewTile(placedTile45).occupantCount(color, Occupant.Kind.PAWN));
        }

        Board boardWithOccupants = board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27)
                .withNewTile(placedTile42).withNewTile(placedTile45)
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 568))
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 172))
                .withOccupant(new Occupant(Occupant.Kind.HUT, 427))
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 455))
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 273));

        assertEquals(2, boardWithOccupants.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(1, boardWithOccupants.occupantCount(PlayerColor.RED, Occupant.Kind.HUT));

        assertEquals(0, boardWithOccupants.occupantCount(PlayerColor.BLUE, Occupant.Kind.HUT));
        assertEquals(1, boardWithOccupants.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(1,  board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27)
                .withNewTile(placedTile42).withNewTile(placedTile45).withOccupant(new Occupant(Occupant.Kind.PAWN, 273))
                .occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));

        for (PlayerColor color : List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.PURPLE)) {
            assertEquals(0, boardWithOccupants.occupantCount(color, Occupant.Kind.HUT));
            assertEquals(0, boardWithOccupants.occupantCount(color, Occupant.Kind.PAWN));
        }
    }

    @Test
    void insertionPositions() {
        assertEquals(0, board1.insertionPositions().size());
        assertEquals(4, board1.withNewTile(placedTile56).insertionPositions().size());
        assertEquals(6, board1.withNewTile(placedTile56).withNewTile(placedTile17).insertionPositions().size());
        assertEquals(8, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).insertionPositions().size());
        assertEquals(8, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).insertionPositions().size());
        assertEquals(8, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).insertionPositions().size());

        Set<Pos> insertionPositions = board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).insertionPositions();
        Set<Pos> expectedInsertionPositions = new HashSet<>(List.of(
                new Pos(-2, 0), new Pos(2, 0), new Pos(0, -2), new Pos(0, 2),
                new Pos(-1, -1), new Pos(-1, 1), new Pos(1, -1), new Pos(1, 1)
        ));
        assertEquals(expectedInsertionPositions, insertionPositions);

        insertionPositions = board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).insertionPositions();
        expectedInsertionPositions = new HashSet<>(List.of(
                new Pos(-2, 0), new Pos(2, 0), new Pos(0, -1), new Pos(0, 2),
                new Pos(-1, -1), new Pos(-1, 1), new Pos(1, -1), new Pos(1, 1)
        ));
        assertEquals(expectedInsertionPositions, insertionPositions);

        insertionPositions = board1.withNewTile(placedTile56).insertionPositions();
        expectedInsertionPositions = new HashSet<>(List.of(
                new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)
        ));
        assertEquals(expectedInsertionPositions, insertionPositions);

        insertionPositions = board1.withNewTile(placedTile56).withNewTile(placedTile17).insertionPositions();
        expectedInsertionPositions = new HashSet<>(List.of(
                new Pos(-2, 0), new Pos(1, 0), new Pos(0, -1),
                new Pos(0, 1), new Pos(-1, -1), new Pos(-1, 1)
        ));
        assertEquals(expectedInsertionPositions, insertionPositions);
    }

    @Test
    void lastPlacedTile() {
        assertNull(board1.lastPlacedTile());
        assertEquals(placedTile56, board1.withNewTile(placedTile56).lastPlacedTile());
        assertEquals(placedTile17, board1.withNewTile(placedTile56).withNewTile(placedTile17).lastPlacedTile());
        assertEquals(placedTile27, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).lastPlacedTile());
        assertEquals(placedTile42, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).lastPlacedTile());
        assertEquals(placedTile45, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).lastPlacedTile());
    }

    @Test
    void forestsClosedByLastTile() {
        assertEquals(0, board1.forestsClosedByLastTile().size());
        assertEquals(0, board1.withNewTile(placedTile56).forestsClosedByLastTile().size());
        assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).forestsClosedByLastTile().size());
        assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).forestsClosedByLastTile().size());
        assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).forestsClosedByLastTile().size());
        assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).forestsClosedByLastTile().size());

        assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile23).withNewTile(placedTile45).forestsClosedByLastTile().size());
        assertEquals(1, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile45).withNewTile(placedTile23).forestsClosedByLastTile().size());

        assertEquals(Set.of(new Area<>(Set.of(forest1, forest5, forest10), List.of(), 0)),
                board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27)
                        .withNewTile(placedTile45).withNewTile(placedTile23).forestsClosedByLastTile());
    }

    @Test
    void riversClosedByLastTile() {
        assertEquals(0, board1.riversClosedByLastTile().size());
        assertEquals(0, board1.withNewTile(placedTile56).riversClosedByLastTile().size());
        assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).riversClosedByLastTile().size());
        assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).riversClosedByLastTile().size());
        assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).riversClosedByLastTile().size());

        assertEquals(1, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45)
                .withNewTile(placedTile70).withNewTile(placedTile71).withNewTile(placedTile72).riverArea(river1).openConnections());

        assertEquals(0, board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45)
                .withNewTile(placedTile70).withNewTile(placedTile71).withNewTile(placedTile72).withNewTile(placedTile73).riverArea(river1).openConnections());

        assertEquals(Set.of(
                new Area<>(Set.of(river1, river2, river3, river6, river7, river8, river9), List.of(), 0))
                , board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45)
                .withNewTile(placedTile70).withNewTile(placedTile71).withNewTile(placedTile73).withNewTile(placedTile72).riversClosedByLastTile());
        assertEquals(Set.of(), board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45)
                        .withNewTile(placedTile70).withNewTile(placedTile71).withNewTile(placedTile72).riversClosedByLastTile());
    }

    @Test
    void canAddTile() {
        assertFalse(board1.canAddTile(placedTile56));
        assertFalse(board1.canAddTile(placedTile17));
        assertFalse(board1.canAddTile(placedTile23));
        assertFalse(board1.canAddTile(placedTile27));
        assertFalse(board1.canAddTile(placedTile42));
        assertFalse(board1.canAddTile(placedTile45));
        assertFalse(board1.canAddTile(placedTile70));
        assertFalse(board1.canAddTile(placedTile71));
        assertFalse(board1.canAddTile(placedTile72));
        assertFalse(board1.canAddTile(placedTile73));

        assertTrue(board1.withNewTile(placedTile56).canAddTile(placedTile17));
        assertTrue(board1.withNewTile(placedTile56).canAddTile(placedTile23));
        assertTrue(board1.withNewTile(placedTile56).canAddTile(placedTile27));
        assertTrue(board1.withNewTile(placedTile56).canAddTile(placedTile42));
        assertTrue(board1.withNewTile(placedTile56).canAddTile(placedTile45));

        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).canAddTile(placedTile27));
        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).canAddTile(placedTile23));
        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).canAddTile(placedTile42));
        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).canAddTile(placedTile45));
        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).canAddTile(placedTile70));
        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).canAddTile(placedTile71));

        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).canAddTile(placedTile42));
        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).canAddTile(placedTile45));
        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).canAddTile(placedTile70));
        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).canAddTile(placedTile72));
        assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).canAddTile(placedTile73));

        assertFalse(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).canAddTile(placedTile56));
        assertFalse(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).canAddTile(placedTile17));
        assertFalse(board1.withNewTile(placedTile56).withNewTile(placedTile27).withNewTile(placedTile23).canAddTile(placedTile70));
        assertFalse(board1.withNewTile(placedTile56).withNewTile(placedTile27).withNewTile(placedTile23).canAddTile(placedTile71));
        assertFalse(board1.withNewTile(placedTile56).withNewTile(placedTile27).withNewTile(placedTile23).canAddTile(placedTile72));
        assertFalse(board1.withNewTile(placedTile56).withNewTile(placedTile27).withNewTile(placedTile23).canAddTile(placedTile71));
    }

    @Test
    void couldPlaceTile() {
        for (Rotation r :Rotation.ALL) {
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile56.tile(), PlayerColor.RED, r, placedTile56.pos(), null).tile()));
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile17.tile(), PlayerColor.RED, r, placedTile17.pos(), null).tile()));
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile23.tile(), PlayerColor.RED, r, placedTile23.pos(), null).tile()));
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile27.tile(), PlayerColor.RED, r, placedTile27.pos(), null).tile()));
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile42.tile(), PlayerColor.RED, r, placedTile42.pos(), null).tile()));
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile45.tile(), PlayerColor.RED, r, placedTile45.pos(), null).tile()));
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile70.tile(), PlayerColor.RED, r, placedTile70.pos(), null).tile()));
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile71.tile(), PlayerColor.RED, r, placedTile71.pos(), null).tile()));
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile72.tile(), PlayerColor.RED, r, placedTile72.pos(), null).tile()));
            assertFalse(board1.couldPlaceTile(new PlacedTile(placedTile73.tile(), PlayerColor.RED, r, placedTile73.pos(), null).tile()));

            assertTrue(board1.withNewTile(placedTile56).couldPlaceTile(new PlacedTile(placedTile17.tile(), PlayerColor.RED, r, placedTile17.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).couldPlaceTile(new PlacedTile(placedTile23.tile(), PlayerColor.RED, r, placedTile23.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).couldPlaceTile(new PlacedTile(placedTile27.tile(), PlayerColor.RED, r, placedTile27.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).couldPlaceTile(new PlacedTile(placedTile42.tile(), PlayerColor.RED, r, placedTile42.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).couldPlaceTile(new PlacedTile(placedTile45.tile(), PlayerColor.RED, r, placedTile45.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).couldPlaceTile(new PlacedTile(placedTile70.tile(), PlayerColor.RED, r, placedTile70.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).couldPlaceTile(new PlacedTile(placedTile71.tile(), PlayerColor.RED, r, placedTile71.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).couldPlaceTile(new PlacedTile(placedTile72.tile(), PlayerColor.RED, r, placedTile72.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).couldPlaceTile(new PlacedTile(placedTile73.tile(), PlayerColor.RED, r, placedTile73.pos(), null).tile()));


            assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).couldPlaceTile(new PlacedTile(placedTile27.tile(), PlayerColor.RED, r, placedTile27.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).couldPlaceTile(new PlacedTile(placedTile23.tile(), PlayerColor.RED, r, placedTile23.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).couldPlaceTile(new PlacedTile(placedTile42.tile(), PlayerColor.RED, r, placedTile42.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).couldPlaceTile(new PlacedTile(placedTile45.tile(), PlayerColor.RED, r, placedTile45.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).couldPlaceTile(new PlacedTile(placedTile70.tile(), PlayerColor.RED, r, placedTile70.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).couldPlaceTile(new PlacedTile(placedTile71.tile(), PlayerColor.RED, r, placedTile71.pos(), null).tile()));

            assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).couldPlaceTile(new PlacedTile(placedTile42.tile(), PlayerColor.RED, r, placedTile42.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).couldPlaceTile(new PlacedTile(placedTile45.tile(), PlayerColor.RED, r, placedTile45.pos(), null).tile()));
            assertTrue(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile71).couldPlaceTile(new PlacedTile(placedTile70.tile(), PlayerColor.RED, r, placedTile70.pos(), null).tile()));
        }
    }

    @Test
    void withNewTile() {
    }


    @Test
    void withoutGatherersOrFishersIn() {
        Area<Zone.Forest> forestArea1 = new Area<>(Set.of(forest1), List.of(PlayerColor.RED), 2);
        Area<Zone.Forest> forestArea2 = new Area<>(Set.of(forest5), List.of(PlayerColor.GREEN), 1);
        Area<Zone.River> riverArea1 = new Area<>(Set.of(river1), List.of(PlayerColor.BLUE), 2);
        Area<Zone.River> riverArea2 = new Area<>(Set.of(river3), List.of(PlayerColor.YELLOW), 1);
        Board board = board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27);
        board.withOccupant(new Occupant(Occupant.Kind.PAWN, forest1.id())).withOccupant(new Occupant(Occupant.Kind.PAWN, meadow5.id()));

        assertEquals(Set.of(), board.withoutGatherersOrFishersIn(Set.of(forestArea1.connectTo(forestArea2)), Set.of(riverArea1, riverArea2)).occupants());
    }

    @Test
    void withMoreCancelledAnimals() {

    }

    @Test
    void testEquals() {
        assertEquals(board2.withNewTile(placedTile56), board1.withNewTile(placedTile56));
        assertEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17), board2.withNewTile(placedTile56).withNewTile(placedTile17));
        assertEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17), board2.withNewTile(placedTile56).withNewTile(placedTile17));
        assertEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45), board2.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45));
        assertEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).withOccupant(new Occupant(Occupant.Kind.PAWN, 568)), board2.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).withOccupant(new Occupant(Occupant.Kind.PAWN, 568)));
        assertEquals(board1, board2);
        assertEquals(board1, board2);
        assertEquals(board1, board2);
        assertEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).withOccupant(new Occupant(Occupant.Kind.PAWN, 568)).withOccupant(new Occupant(Occupant.Kind.HUT, 427)), board2.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).withOccupant(new Occupant(Occupant.Kind.PAWN, 568)).withOccupant(new Occupant(Occupant.Kind.HUT, 427)));


        assertNotEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).withOccupant(new Occupant(Occupant.Kind.PAWN, 568)), board2.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).withOccupant(new Occupant(Occupant.Kind.PAWN, 567)));

        assertNotEquals(board1.withNewTile(placedTile56), board2.withNewTile(placedTile17));
        assertNotEquals(board2.withNewTile(placedTile56).withNewTile(placedTile17), meadow5);
        assertNotEquals(board1.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).withOccupant(new Occupant(Occupant.Kind.PAWN, 568)), board2.withNewTile(placedTile56).withNewTile(placedTile17).withNewTile(placedTile27).withNewTile(placedTile42).withNewTile(placedTile45).withOccupant(new Occupant(Occupant.Kind.HUT, 427)));
        assertNotEquals(board1.withNewTile(placedTile56).withOccupant(new Occupant(Occupant.Kind.PAWN, 568)), board2);
    }
}