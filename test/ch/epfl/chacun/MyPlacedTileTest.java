package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyPlacedTileTest {

    Zone.Meadow meadowWithSuperpower = new Zone.Meadow(560, List.of(new Animal(1, Animal.Kind.AUROCHS)), Zone.SpecialPower.HUNTING_TRAP);
    Zone.Forest forest = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow meadowWithNoSuperpower = new Zone.Meadow(562, List.of(), null);
    Zone.River river = new Zone.River(563, 1, null);

    TileSide n = new TileSide.Meadow(meadowWithSuperpower);
    TileSide e = new TileSide.Forest(forest);
    TileSide s = new TileSide.Forest(forest);
    TileSide w = new TileSide.River(meadowWithNoSuperpower, river, meadowWithSuperpower);
    TileSide n1 = new TileSide.Meadow(meadowWithNoSuperpower);

    public Tile startTile = new Tile(1, Tile.Kind.START, n, e, s, w);

    public Tile tileWithNoSuperpower = new Tile(1, Tile.Kind.START, n1, n1, n1, n1);

    public Tile tileWithNoForest = new Tile(1, Tile.Kind.START, n, n, n, n);

    public Tile tileWithNoMeadow = new Tile(1, Tile.Kind.START, e, e, e, e);

    public Tile tileWithNoRiver = new Tile(1, Tile.Kind.START, s, s, s, s);

    @Test
    void placedTileConstructorThrowsOnNullTile(){
        assertThrows(NullPointerException.class, () -> {
            new PlacedTile(null, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        });
    }

    @Test
    void placedTileConstructorThrowsOnNullRotation(){
        assertThrows(NullPointerException.class, () -> {
            new PlacedTile(startTile, PlayerColor.BLUE, null, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        });
    }

    @Test
    void placedTileConstructorThrowsOnNullPos(){
        assertThrows(NullPointerException.class, () -> {
            new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, null, new Occupant(Occupant.Kind.PAWN, 0));
        });
    }

    @Test
    void idWorks(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(1, placedTile.id());
    }

    @Test
    void kindWorks(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Tile.Kind.START, placedTile.kind());
    }

    @Test
    void sideWorksForAllDirectionsAndRotations(){
        PlacedTile placedTile1 = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(w, placedTile1.side(Direction.N));
        assertEquals(n, placedTile1.side(Direction.E));
        assertEquals(e, placedTile1.side(Direction.S));
        assertEquals(s, placedTile1.side(Direction.W));

        PlacedTile placedTile2 = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.LEFT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(e, placedTile2.side(Direction.N));
        assertEquals(s, placedTile2.side(Direction.E));
        assertEquals(w, placedTile2.side(Direction.S));
        assertEquals(n, placedTile2.side(Direction.W));

        PlacedTile placedTile3 = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.HALF_TURN, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(s, placedTile3.side(Direction.N));
        assertEquals(w, placedTile3.side(Direction.E));
        assertEquals(n, placedTile3.side(Direction.S));
        assertEquals(e, placedTile3.side(Direction.W));

        PlacedTile placedTile4 = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.NONE, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(n, placedTile4.side(Direction.N));
        assertEquals(e, placedTile4.side(Direction.E));
        assertEquals(s, placedTile4.side(Direction.S));
        assertEquals(w, placedTile4.side(Direction.W));
    }

    @Test
    void zoneWithIdWorksForAllZones(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(meadowWithSuperpower, placedTile.zoneWithId(560));
        assertEquals(forest, placedTile.zoneWithId(561));
        assertEquals(meadowWithNoSuperpower, placedTile.zoneWithId(562));
        assertEquals(river, placedTile.zoneWithId(563));
    }

    @Test
    void zoneWithIdThrowsOnInvalidId(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertThrows(IllegalArgumentException.class, () -> {
            placedTile.zoneWithId(564);
        });
    }

    @Test
    void specialPowerZoneWorksForStartTile(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(meadowWithSuperpower, placedTile.specialPowerZone());
    }

    @Test
    void specialPowerZoneReturnsNullIfNoZoneHasSpecialPower(){
        PlacedTile placedTile = new PlacedTile(tileWithNoSuperpower, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertNull(placedTile.specialPowerZone());
    }

    @Test
    void forestZonesWorksForStartTile(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Set.of(forest), placedTile.forestZones());
    }

    @Test
    void forestZonesReturnsEmptySetIfNoForestZones(){
        PlacedTile placedTile = new PlacedTile(tileWithNoForest, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Set.of(), placedTile.forestZones());
    }

    @Test
    void meadowZonesWorksForStartTile(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Set.of(meadowWithSuperpower, meadowWithNoSuperpower), placedTile.meadowZones());
    }

    @Test
    void meadowZonesReturnsEmptySetIfNoMeadowZones(){
        PlacedTile placedTile = new PlacedTile(tileWithNoMeadow, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Set.of(), placedTile.meadowZones());
    }

    @Test
    void riverZonesWorksForStartTile(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Set.of(river), placedTile.riverZones());
    }

    @Test
    void riverZonesReturnsEmptySetIfNoRiverZones(){
        PlacedTile placedTile = new PlacedTile(tileWithNoRiver, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Set.of(), placedTile.riverZones());
    }

    @Test
    void potentialOccupantsWorksForStartTile(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 560), new Occupant(Occupant.Kind.PAWN, 561), new Occupant(Occupant.Kind.PAWN, 562), new Occupant(Occupant.Kind.PAWN, 563), new Occupant(Occupant.Kind.HUT, 563)), placedTile.potentialOccupants());
    }

    @Test
    void potentialOccupantsReturnsEmptySetForTileWithNoPlacer(){
        PlacedTile placedTile = new PlacedTile(startTile, null, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Set.of(), placedTile.potentialOccupants());
    }

    @Test
    void withOccupantWorksForStartTileWithNoOccupant(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, null);
        PlacedTile placedTileWithOccupant = placedTile.withOccupant(new Occupant(Occupant.Kind.HUT, 560));
        assertEquals(new Occupant(Occupant.Kind.HUT, 560), placedTileWithOccupant.occupant());
    }

    @Test
    void withOccupantThrowsOnOccupiedZone(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.HUT, 560));
        assertThrows(IllegalArgumentException.class, () -> {
            placedTile.withOccupant(new Occupant(Occupant.Kind.HUT, 560));
        });
    }

    @Test
    void withNoOccupantWorksForStartTileWithNoOccupant(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, null);
        PlacedTile placedTileWithNoOccupant = placedTile.withNoOccupant();
        assertNull(placedTileWithNoOccupant.occupant());
    }

    @Test
    void withNoOccupantWorksForStartTileWithOccupant(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.HUT, 560));
        PlacedTile placedTileWithNoOccupant = placedTile.withNoOccupant();
        assertNull(placedTileWithNoOccupant.occupant());
    }

    @Test
    void idOfZoneOccupiedByWorksForStartTile(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.HUT, 560));
        assertEquals(560, placedTile.idOfZoneOccupiedBy(Occupant.Kind.HUT));
    }

    @Test
    void idOfZoneOccupiedWorksForStartTileWithNoOccupant(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, null);
        assertEquals(-1, placedTile.idOfZoneOccupiedBy(Occupant.Kind.HUT));
    }

    @Test
    void idOfZoneOccupiedByThrowsOnInvalidOccupantKind(){
        PlacedTile placedTile = new PlacedTile(startTile, PlayerColor.BLUE, Rotation.RIGHT, Pos.ORIGIN, new Occupant(Occupant.Kind.HUT, 560));
        assertEquals(-1, placedTile.idOfZoneOccupiedBy(Occupant.Kind.PAWN));
    }
}
