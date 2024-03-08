package ch.epfl.chacun.Etape3;


import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class MyZonePartitionTest3 {
    // an arbitrary value for open connections for zones that are built separately
    private static final int OPEN_CONNECTIONS = 2;
    private static final PlayerColor NEW_PLAYER_COLOR = PlayerColor.GREEN;

    private static List<Zone.Meadow> getMeadowZones() {
        Zone.Meadow meadow1 = new Zone.Meadow(12, new ArrayList<>(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(33, List.of(new Animal(0, Animal.Kind.AUROCHS),
                new Animal(1, Animal.Kind.TIGER)), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow3 = new Zone.Meadow(40, List.of(new Animal(0, Animal.Kind.MAMMOTH)), null);
        Zone.Meadow meadow4 = new Zone.Meadow(51, List.of(new Animal(0, Animal.Kind.DEER),
                new Animal(1, Animal.Kind.DEER)), Zone.SpecialPower.SHAMAN);

        return List.of(meadow1, meadow2, meadow3, meadow4);
    }

    private static Set<Area<Zone.Meadow>> getMeadowAreas() {
        Set<Area<Zone.Meadow>> meadowAreas = new HashSet<>();

        List<Zone.Meadow> meadowList = getMeadowZones();

        Set<Zone.Meadow> meadowZones1 = Set.of(meadowList.get(0), meadowList.get(2));
        Set<Zone.Meadow> meadowZones2 = Set.of(meadowList.get(1));
        Set<Zone.Meadow> meadowZones3 = Set.of(meadowList.get(3));
        Area<Zone.Meadow> meadowArea1 = new Area<>(meadowZones1, List.of(PlayerColor.RED, PlayerColor.GREEN), 4);
        Area<Zone.Meadow> meadowArea2 = new Area<>(meadowZones2, new ArrayList<>(), 2);
        Area<Zone.Meadow> meadowArea3 = new Area<>(meadowZones3, List.of(PlayerColor.PURPLE), 3);

        meadowAreas.add(meadowArea1);
        meadowAreas.add(meadowArea2);
        meadowAreas.add(meadowArea3);

        return meadowAreas;
    }

    private static List<Area<Zone.Meadow>> getMeadowAreasList() {
        List<Area<Zone.Meadow>> meadowAreas = new ArrayList<>();

        List<Zone.Meadow> meadowList = getMeadowZones();

        Set<Zone.Meadow> meadowZones1 = Set.of(meadowList.get(0), meadowList.get(2));
        Set<Zone.Meadow> meadowZones2 = Set.of(meadowList.get(1));
        Set<Zone.Meadow> meadowZones3 = Set.of(meadowList.get(3));
        Area<Zone.Meadow> meadowArea1 = new Area<>(meadowZones1, List.of(PlayerColor.RED, PlayerColor.GREEN), 4);
        Area<Zone.Meadow> meadowArea2 = new Area<>(meadowZones2, new ArrayList<>(), 2);
        Area<Zone.Meadow> meadowArea3 = new Area<>(meadowZones3, List.of(PlayerColor.PURPLE), 3);

        meadowAreas.add(meadowArea1);
        meadowAreas.add(meadowArea2);
        meadowAreas.add(meadowArea3);

        return meadowAreas;
    }

    private static ZonePartition<Zone.Meadow> getMeadowPartition() {
        return new ZonePartition<>(getMeadowAreas());
    }

    private static ZonePartition.Builder<Zone.Meadow> getMeadowPartitionBuilder() {
        return new ZonePartition.Builder<>(getMeadowPartition());
    }

    private static Zone.Meadow getMeadowNotInPartition() {
        return new Zone.Meadow(5, List.of(new Animal(0, Animal.Kind.TIGER)), Zone.SpecialPower.WILD_FIRE);
    }

    private static List<Zone.Water> getRiverSystemsZones() {
        Zone.River river1 = new Zone.River(12, 22, null);
        Zone.Lake lake1 = new Zone.Lake(3, 9, Zone.SpecialPower.RAFT);
        Zone.River river2 = new Zone.River(11, 0, lake1);
        Zone.Lake lake2 = new Zone.Lake(77, 17, null);
        Zone.River river3 = new Zone.River(50, 10, lake2);

        return List.of(river1, lake1, river2, lake2, river3);
    }

    private static List<Area<Zone.Water>> getRiverSystemsAreasList() {
        List<Area<Zone.Water>> riverSystemsArea = new ArrayList<>();

        List<Zone.Water> riverSystemsZones = getRiverSystemsZones();

        Set<Zone.Water> riverSystemsZone1 = Set.of(riverSystemsZones.get(0));
        Set<Zone.Water> riverSystemsZone2 = Set.of(riverSystemsZones.get(1), riverSystemsZones.get(2));
        Set<Zone.Water> riverSystemsZone3 = Set.of(riverSystemsZones.get(3), riverSystemsZones.get(4));
        Area<Zone.Water> riverSystemsArea1 = new Area<>(riverSystemsZone1, new ArrayList<>(), 2);
        Area<Zone.Water> riverSystemsArea2 = new Area<>(riverSystemsZone2, List.of(PlayerColor.BLUE, PlayerColor.GREEN), 1);
        Area<Zone.Water> riverSystemsArea3 = new Area<>(riverSystemsZone3, List.of(PlayerColor.PURPLE), 2);

        riverSystemsArea.add(riverSystemsArea1);
        riverSystemsArea.add(riverSystemsArea2);
        riverSystemsArea.add(riverSystemsArea3);

        return riverSystemsArea;
    }

    private static Set<Area<Zone.Water>> getRiverSystemsAreas() {
        Set<Area<Zone.Water>> riverSystemsArea = new HashSet<>();

        List<Zone.Water> riverSystemsZones = getRiverSystemsZones();

        Set<Zone.Water> riverSystemsZone1 = Set.of(riverSystemsZones.get(0));
        Set<Zone.Water> riverSystemsZone2 = Set.of(riverSystemsZones.get(1), riverSystemsZones.get(2));
        Set<Zone.Water> riverSystemsZone3 = Set.of(riverSystemsZones.get(3), riverSystemsZones.get(4));
        Area<Zone.Water> riverSystemsArea1 = new Area<>(riverSystemsZone1, new ArrayList<>(), 2);
        Area<Zone.Water> riverSystemsArea2 = new Area<>(riverSystemsZone2, List.of(PlayerColor.BLUE, PlayerColor.GREEN), 1);
        Area<Zone.Water> riverSystemsArea3 = new Area<>(riverSystemsZone3, List.of(PlayerColor.PURPLE), 2);

        riverSystemsArea.add(riverSystemsArea1);
        riverSystemsArea.add(riverSystemsArea2);
        riverSystemsArea.add(riverSystemsArea3);

        return riverSystemsArea;
    }

    private static ZonePartition<Zone.Water> getRiverSystemsPartition() {
        return new ZonePartition<>(getRiverSystemsAreas());
    }

    private static ZonePartition.Builder<Zone.Water> getRiverSystemsPartitionBuilder() {
        return new ZonePartition.Builder<>(getRiverSystemsPartition());
    }

    private static Zone.Water getRiverSystemNotInPartition() {
        return new Zone.River(88, 0, null);
    }

    @Test
    void zonePartitionConstructorGuaranteesImmutability() {
        // For meadows
        List<Area<Zone.Meadow>> meadowAreas = getMeadowAreasList();
        Set<Area<Zone.Meadow>> meadowAreasMutable = new HashSet<>(meadowAreas);
        Set<Area<Zone.Meadow>> meadowAreasExpected = new HashSet<>(meadowAreas);
        ZonePartition<Zone.Meadow> meadowPartition = new ZonePartition<>(meadowAreasMutable);
        meadowAreasMutable.remove(meadowAreas.get(1));
        assertEquals(meadowAreasExpected, meadowPartition.areas());

        // For river systems
        List<Area<Zone.Water>> riverSystemsAreas = getRiverSystemsAreasList();
        Set<Area<Zone.Water>> riverSystemsAreasMutable = new HashSet<>(riverSystemsAreas);
        Set<Area<Zone.Water>> riverSystemsAreasExpected = new HashSet<>(riverSystemsAreas);
        ZonePartition<Zone.Water> riverSystemsPartition = new ZonePartition<>(riverSystemsAreasMutable);
        riverSystemsAreasMutable.remove(meadowAreas.get(1));
        assertEquals(riverSystemsAreasExpected, riverSystemsPartition.areas());
    }

    @Test
    void areaContainingReturnsCorrectZone() {
        // For meadows
        List<Zone.Meadow> meadowZones = getMeadowZones();
        List<Area<Zone.Meadow>> meadowAreas = getMeadowAreasList();
        ZonePartition<Zone.Meadow> meadowPartition = new ZonePartition<>(new HashSet<>(meadowAreas));
        assertEquals(meadowAreas.get(0), meadowPartition.areaContaining(meadowZones.get(0)));
        assertEquals(meadowAreas.get(0), meadowPartition.areaContaining(meadowZones.get(2)));
        assertEquals(meadowAreas.get(1), meadowPartition.areaContaining(meadowZones.get(1)));
        assertEquals(meadowAreas.get(2), meadowPartition.areaContaining(meadowZones.get(3)));
        assertNotEquals(meadowAreas.get(0), meadowPartition.areaContaining(meadowZones.get(3)));

        // For river systems
        List<Zone.Water> waterZones = getRiverSystemsZones();
        List<Area<Zone.Water>> riverSystemsAreas = getRiverSystemsAreasList();
        ZonePartition<Zone.Water> riverSystemsPartition = new ZonePartition<>(new HashSet<>(riverSystemsAreas));
        assertEquals(riverSystemsAreas.get(0), riverSystemsPartition.areaContaining(waterZones.get(0)));
        assertEquals(riverSystemsAreas.get(1), riverSystemsPartition.areaContaining(waterZones.get(1)));
        assertEquals(riverSystemsAreas.get(1), riverSystemsPartition.areaContaining(waterZones.get(2)));
        assertEquals(riverSystemsAreas.get(2), riverSystemsPartition.areaContaining(waterZones.get(3)));
        assertEquals(riverSystemsAreas.get(2), riverSystemsPartition.areaContaining(waterZones.get(4)));
        assertNotEquals(riverSystemsAreas.get(0), riverSystemsPartition.areaContaining(waterZones.get(3)));
    }

    @Test
    void areaContainingThrowsIllegalArgumentException() {
        // For meadows
        ZonePartition<Zone.Meadow> meadowPartition = getMeadowPartition();
        assertThrows(IllegalArgumentException.class, () -> meadowPartition.areaContaining(getMeadowNotInPartition()));
        // For river systems
        ZonePartition<Zone.Water> riverSystemsPartition = getRiverSystemsPartition();
        assertThrows(IllegalArgumentException.class, () -> riverSystemsPartition.areaContaining(getRiverSystemNotInPartition()));
    }

    @Test
    void addSingletonAddsNewUnoccupiedAreaToThePartition() {
        // For meadows
        ZonePartition.Builder<Zone.Meadow> meadowPartitonBuilder = getMeadowPartitionBuilder();
        Set<Area<Zone.Meadow>> meadowAreasExpected = getMeadowAreas();
        meadowAreasExpected.add(new Area<>(Set.of(getMeadowNotInPartition()), new ArrayList<>(), OPEN_CONNECTIONS));
        meadowPartitonBuilder.addSingleton(getMeadowNotInPartition(), OPEN_CONNECTIONS);
        assertEquals(meadowAreasExpected, meadowPartitonBuilder.build().areas());
        // For river systems
        ZonePartition.Builder<Zone.Water> riverSystemsPartitonBuilder = getRiverSystemsPartitionBuilder();
        Set<Area<Zone.Water>> riverSystemsAreasExpected = getRiverSystemsAreas();
        riverSystemsAreasExpected.add(new Area<>(Set.of(getRiverSystemNotInPartition()), new ArrayList<>(), OPEN_CONNECTIONS));
        riverSystemsPartitonBuilder.addSingleton(getRiverSystemNotInPartition(), OPEN_CONNECTIONS);
        assertEquals(riverSystemsAreasExpected, riverSystemsPartitonBuilder.build().areas());
    }

    @Test
    void addInitialOccupantAddNewAreaWithOccupantToPartition() {
        // For meadows
        List<Zone.Meadow> meadowZones = getMeadowZones();
        List<Area<Zone.Meadow>> meadowAreas = getMeadowAreasList();
        meadowAreas.set(1, new Area<>(Set.of(meadowZones.get(1)), List.of(NEW_PLAYER_COLOR), OPEN_CONNECTIONS));
        ZonePartition<Zone.Meadow> expectedMeadowZonePartition = new ZonePartition<>(new HashSet<>(meadowAreas));
        ZonePartition.Builder<Zone.Meadow> meadowPartitonBuilder = getMeadowPartitionBuilder();
        meadowPartitonBuilder.addInitialOccupant(meadowZones.get(1), NEW_PLAYER_COLOR);
        assertEquals(expectedMeadowZonePartition, meadowPartitonBuilder.build());
        // For river systems
        List<Zone.Water> riverSystemsZones = getRiverSystemsZones();
        List<Area<Zone.Water>> riverSystemsAreas = getRiverSystemsAreasList();
        riverSystemsAreas.set(0, new Area<>(Set.of(riverSystemsZones.getFirst()), List.of(NEW_PLAYER_COLOR), OPEN_CONNECTIONS));
        ZonePartition<Zone.Water> expectedRiverSystemsZonePartition = new ZonePartition<>(new HashSet<>(riverSystemsAreas));
        ZonePartition.Builder<Zone.Water> riverSystemsPartitionBuilder = getRiverSystemsPartitionBuilder();
        riverSystemsPartitionBuilder.addInitialOccupant(riverSystemsZones.getFirst(), NEW_PLAYER_COLOR);
        assertEquals(expectedRiverSystemsZonePartition, riverSystemsPartitionBuilder.build());

    }


    /**


    @Test
    void addInitialOccupantThrowsIllegalArgumentExceptionIfAreaAlreadyOccupied() {
        PlayerColor newplayerColor = PlayerColor.GREEN;
        Zone.Meadow> meadowArea2 = new Area<>(meadowZones2, new ArrayList<>(), 2);
        Area<Zone.Meadow> meadowArea3 = new Area<>(meadowZones3, List.of(PlayerColor.PURPLE), 3);
        expectedMeadowAreas.add(meadowArea1);
        expectedMeadowAreas.add(meadowArea2);
        expectedMeadowAreas.add(meadowArea3);
        ZonePartition<Zone.Meadow> expectedZonePartition = new ZonePartition<>(expectedMeadowAreas);

        ZonePartition.Builder<Zone.Meadow> meadowPartitionBuilder = getMeadowPartitionBuilder();
        meadowPartitionBuilder.removeAllOccupantsOf(
                new Area<>(meadowZones1, List.of(PlayerColor.RED, PlayerColor.GREEN), 4));

        assertEquals(expectedZonePartition, meadowPartitionBuilder.build());


        expectedMeadowAreas.clear();
        meadowArea1 = new Area<>(meadowZones1,  List.of(PlayerColor.RED, PlayerColor.GREEN), 4);
        meadowArea2 = new Area<>(meadowZones2, new ArrayList<>(), 2);
        meadowArea3 = new Area<>(meadowZones3, new ArrayList<>(), 3);
        expectedMeadowAreas.add(meadowArea1);
        expectedMeadowAreas.add(meadowArea2);
        expectedMeadowAreas.add(meadowArea3);
        expectedZonePartition = new ZonePartition<>(expectedMeadowAreas);

        meadowPartitionBuilder = getMeadowPartitionBuilder();
        meadowPartitionBuilder.removeAllOccupantsOf(
                new Area<>(meadowZones3, List.of(PlayerColor.PURPLE), 3));

        assertEquals(expectedZonePartition, meadowPartitionBuilder.build());
    }
     */

    @Test
    void removeAllOccupantsThrowsIllegalArgumentExceptionIfAreaIsNotPartOfPartition() {
        Zone.Meadow meadowNotInPartition = new Zone.Meadow(5, List.of(new Animal(0, Animal.Kind.TIGER)), Zone.SpecialPower.WILD_FIRE);

        Area<Zone.Meadow> areaNotInPartition = new Area<>(Set.of(meadowNotInPartition), new ArrayList<>(), 3);
        ZonePartition.Builder<Zone.Meadow> meadowPartitionBuilder = getMeadowPartitionBuilder();

        assertThrows(IllegalArgumentException.class, () -> meadowPartitionBuilder.removeAllOccupantsOf(areaNotInPartition));
    }

    @Test
    void unionConnectsTwoDifferentZones() {
        Set<Area<Zone.Meadow>> expectedMeadowAreas = new HashSet<>();
        List<Zone.Meadow> meadowZones = getMeadowZones();
        Set<Zone.Meadow> meadowZones1 = Set.of(meadowZones.get(0), meadowZones.get(2));
        Set<Zone.Meadow> meadowZones2 = Set.of(meadowZones.get(1));
        Set<Zone.Meadow> meadowZones3 = Set.of(meadowZones.get(3));

        Area<Zone.Meadow> meadowArea1 = new Area<>(meadowZones1, List.of(PlayerColor.RED, PlayerColor.GREEN), 4);
        Area<Zone.Meadow> meadowArea2 = new Area<>(meadowZones2, new ArrayList<>(), 2);
        Area<Zone.Meadow> meadowArea3 = new Area<>(meadowZones3, List.of(PlayerColor.PURPLE), 3);

        expectedMeadowAreas.add(meadowArea2);
        expectedMeadowAreas.add(meadowArea1.connectTo(meadowArea3));
        ZonePartition<Zone.Meadow> expectedZonePartition = new ZonePartition<>(expectedMeadowAreas);

        ZonePartition.Builder<Zone.Meadow> meadowPartitionBuilder = getMeadowPartitionBuilder();
        meadowPartitionBuilder.union(meadowZones.get(2), meadowZones.get(3));

        assertEquals(expectedZonePartition, meadowPartitionBuilder.build());

        expectedMeadowAreas.clear();
        expectedMeadowAreas.add(meadowArea3);
        expectedMeadowAreas.add(meadowArea2.connectTo(meadowArea1));
        expectedZonePartition = new ZonePartition<>(expectedMeadowAreas);

        meadowPartitionBuilder = getMeadowPartitionBuilder();
        meadowPartitionBuilder.union(meadowZones.get(1), meadowZones.get(0));

        assertEquals(expectedZonePartition, meadowPartitionBuilder.build());
    }

    @Test
    void unionDoesNothingIfZoneAreTheSame() {
        List<Zone.Meadow> meadowZones = getMeadowZones();
        ZonePartition.Builder<Zone.Meadow> meadowPartitionBuilder = getMeadowPartitionBuilder();
        meadowPartitionBuilder.union(meadowZones.getFirst(), meadowZones.getFirst());
        assertEquals(getMeadowPartition(), meadowPartitionBuilder.build());

        meadowPartitionBuilder.union(meadowZones.get(1), meadowZones.get(1));
        assertEquals(getMeadowPartition(), meadowPartitionBuilder.build());
    }

    @Test
    void unionThrowsIllegalArgumentExceptionIfOneOfTwoZonesIsNotInPartition() {
        Zone.Meadow meadowNotInPartition = new Zone.Meadow(5, List.of(new Animal(0, Animal.Kind.TIGER)), Zone.SpecialPower.WILD_FIRE);
        List<Zone.Meadow> meadowZones = getMeadowZones();
        ZonePartition.Builder<Zone.Meadow> meadowPartitionBuilder = getMeadowPartitionBuilder();
        assertThrows(IllegalArgumentException.class, () -> meadowPartitionBuilder.union(meadowNotInPartition, meadowZones.getFirst()));
        assertThrows(IllegalArgumentException.class, () -> meadowPartitionBuilder.union(meadowZones.getFirst(), meadowNotInPartition));
    }

    @Test
    void unionThrowsIllegalArgumentExceptionTwoZonesAreNotInPartition() {
        Zone.Meadow meadowNotInPartition = new Zone.Meadow(5, List.of(new Animal(0, Animal.Kind.TIGER)), Zone.SpecialPower.WILD_FIRE);
        ZonePartition.Builder<Zone.Meadow> meadowPartitionBuilder = getMeadowPartitionBuilder();
        assertThrows(IllegalArgumentException.class, () -> meadowPartitionBuilder.union(meadowNotInPartition, meadowNotInPartition));
    }

    @Test
    void buildBuildsACorrectZonePartition() {
        ZonePartition.Builder<Zone.Meadow> meadowPartitionBuilder = new ZonePartition.Builder<>(getMeadowPartition());
        assertEquals(getMeadowPartition(), meadowPartitionBuilder.build());
    }





}

