package ch.epfl.chacun.Etape3;



import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import ch.epfl.chacun.ZonePartition;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class etape3test{

    @Test
    void testAreaConstructorValidatesOpenConnections() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Area<>(Set.of(), List.of(PlayerColor.RED), -1));
        // assertTrue(thrown.getMessage().contains("openConnections must be positive or zero"));
    }

    @Test
    void testAreaConstructorSortsOccupants() {
        Area<Zone.Forest> area = new Area<>(Set.of(), List.of(PlayerColor.BLUE, PlayerColor.RED, PlayerColor.GREEN), 2);
        assertEquals(List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN), area.occupants());
    }
    @Test
    void testAddSingleton() {
        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(new ZonePartition<>(Set.of()));
        builder.addSingleton(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), 1);
        builder.addSingleton(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS),0);
        builder.addSingleton(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), 1);
        ZonePartition<Zone.Forest> partition = builder.build();
        assertEquals(3, partition.areas().size());
    }

    @Test
    void testAddInitialOccupant() {
        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(new ZonePartition<>(Set.of()));
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        builder.addSingleton(forestZone, 0);
        builder.addInitialOccupant(forestZone, PlayerColor.RED);
        ZonePartition<Zone.Forest> partition = builder.build();

        Area<Zone.Forest> area = partition.areaContaining(forestZone);
        assertTrue(area.isOccupied());
        assertEquals(List.of(PlayerColor.RED), area.occupants());
    }
    @Test
    public void testIsClosedWithClosedArea() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> forestArea = new Area<>(zones, List.of(), 0);
        assertTrue(forestArea.isClosed());
    }
    @Test
    public void testIsOccupiedWithOccupiedArea() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        List<PlayerColor> occupants = List.of(PlayerColor.RED);
        Area<Zone.Forest> forestArea = new Area<>(zones, occupants, 1);
        assertTrue(forestArea.isOccupied());
    }
    @Test
    public void testAreaContainingWithKnownZone() {
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Set<Area<Zone.Forest>> areas = Set.of(new Area<>(Set.of(forestZone), new ArrayList<>(), 0));
        ZonePartition<Zone.Forest> partition = new ZonePartition<>(areas);

        Area<Zone.Forest> containingArea = partition.areaContaining(forestZone);
        assertNotNull(containingArea);
        assertTrue(containingArea.zones().contains(forestZone));
    }
    @Test
    public void testBuilderAddSingletonAndBuild() {
        Zone.Forest forestZone = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(new ZonePartition<>(Set.of()));

        builder.addSingleton(forestZone, 0);
        ZonePartition<Zone.Forest> partition = builder.build();

        Area<Zone.Forest> area = partition.areaContaining(forestZone);
        assertNotNull(area);
        assertEquals(1, area.zones().size());
        assertTrue(area.zones().contains(forestZone));
    }
    @Test
    public void testAreaIsClosedWhenOpenConnectionsIsZero() {
        Area<Zone.Forest> area = new Area<>(new HashSet<>(), new ArrayList<>(), 0);
        assertTrue(area.isClosed());
    }

    @Test
    public void testAreaIsNotClosedWhenOpenConnectionsIsNotZero() {
        Area<Zone.Forest> area = new Area<>(new HashSet<>(), new ArrayList<>(), 1);
        assertFalse(area.isClosed());
    }
    @Test
    public void testAreaIsOccupiedWhenThereAreOccupants() {
        List<PlayerColor> occupants = List.of(PlayerColor.RED);
        Area<Zone.Forest> area = new Area<>(new HashSet<>(), occupants, 0);
        assertTrue(area.isOccupied());
    }

    @Test
    public void testAreaIsNotOccupiedWhenThereAreNoOccupants() {
        Area<Zone.Forest> area = new Area<>(new HashSet<>(), new ArrayList<>(), 0);
        assertFalse(area.isOccupied());
    }
    @Test
    public void testMajorityOccupantsWithClearMajority() {
        List<PlayerColor> occupants = new ArrayList<>();
        occupants.add(PlayerColor.RED);
        occupants.add(PlayerColor.RED);
        occupants.add(PlayerColor.BLUE);
        Area<Zone.Forest> area = new Area<>(new HashSet<>(), occupants, 0);
        assertEquals(Set.of(PlayerColor.RED), area.majorityOccupants());
    }

    @Test
    public void testMajorityOccupantsWithNoClearMajority() {
        List<PlayerColor> occupants = new ArrayList<>();
        occupants.add(PlayerColor.RED);
        occupants.add(PlayerColor.BLUE);

        Area<Zone.Forest> area = new Area<>(new HashSet<>(), occupants, 0);
        assertEquals(area.majorityOccupants(), Set.of(PlayerColor.BLUE, PlayerColor.RED));
    }
    @Test
    public void testConnectToAreas() {
        List<PlayerColor> occu = new ArrayList<>();
        Area<Zone.Forest> area1 = new Area<>(new HashSet<>(Collections.singleton(new Zone.Forest(1, Zone.Forest.Kind.PLAIN))), List.of(PlayerColor.RED), 1);
        Area<Zone.Forest> area2 = new Area<>(Set.of(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)), List.of(PlayerColor.BLUE), 1);
        Area<Zone.Forest> connectedArea = area1.connectTo(area2);

        assertEquals(2, connectedArea.zones().size());
        assertEquals(2, connectedArea.occupants().size());
        assertEquals(0, connectedArea.openConnections());
    }
    @Test
    public void testAreaContainingKnownZone() {
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Area<Zone.Forest> area = new Area<>(Set.of(forestZone), List.of(), 0);
        Set<Area<Zone.Forest>> areas = Set.of(area);
        ZonePartition<Zone.Forest> partition = new ZonePartition<>(areas);

        assertEquals(area, partition.areaContaining(forestZone));
    }

    @Test
    public void testAreaContainingThrowsForUnknownZone() {
        ZonePartition<Zone.Forest> partition = new ZonePartition<>(new HashSet<>());
        Zone.Forest unknownZone = new Zone.Forest(99, Zone.Forest.Kind.PLAIN);

        assertThrows(IllegalArgumentException.class, () -> partition.areaContaining(unknownZone));
    }
    @Test
    public void testBuilderAddSingleton() {
        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(new ZonePartition<>(Set.of()));
        Zone.Forest newZone = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);

        builder.addSingleton(newZone, 2);
        ZonePartition<Zone.Forest> partition = builder.build();

        assertEquals(1, partition.areas().size());
        Area<Zone.Forest> area = partition.areas().iterator().next();
        assertTrue(area.zones().contains(newZone));
        assertEquals(2, area.openConnections());
    }

    @Test
    public void testUnionOfTwoAreas() {
        Zone.Forest zone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest zone2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Area<Zone.Forest> area1 = new Area<>(Set.of(zone1), List.of(PlayerColor.RED), 1);
        Area<Zone.Forest> area2 = new Area<>(Set.of(zone2), List.of(PlayerColor.BLUE), 1);
        ZonePartition<Zone.Forest> partition = new ZonePartition<>(Set.of(area1, area2));

        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(partition);
        builder.union(zone1, zone2);
        ZonePartition<Zone.Forest> newPartition = builder.build();

        assertEquals(1, newPartition.areas().size());
        Area<Zone.Forest> newArea = newPartition.areas().iterator().next();
        assertEquals(Set.of(zone1, zone2), newArea.zones());
    }
    @Test
    public void testAreaConstructorSortsOccupants2() {
        Set<Zone> zones = Set.of();
        List<PlayerColor> unsortedOccupants = List.of(PlayerColor.BLUE, PlayerColor.RED, PlayerColor.GREEN,PlayerColor.PURPLE,PlayerColor.YELLOW);
        Area<Zone> area = new Area<>(zones, unsortedOccupants, 0);
        assertEquals(List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN,PlayerColor.YELLOW,PlayerColor.PURPLE), area.occupants(), "c'est mal trié mgl");
    }
    @Test
    public void testHasMenhirWithEmptyForest() {
        Area<Zone.Forest> emptyForest = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        assertFalse(Area.hasMenhir(emptyForest), "ya pas de zone ");
    }
    @Test
    public void testHasMenhirWithForestWithoutMenhirs() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> forestWithoutMenhirs = new Area<>(zones, Collections.emptyList(), 0);
        assertFalse(Area.hasMenhir(forestWithoutMenhirs), "ya pas de menhir dans une foret mgl");
    }
    @Test
    public void testHasMenhirWithForestContainingMenhir() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> forestWithMenhir = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(forestWithMenhir), "Doit retourner true pour une forêt contenant au moins un menhir.");
    }
    @Test
    public void testHasMenhirWithMixedZones() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> mixedForest = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(mixedForest), "Doit retourner true pour une forêt avec des zones mixtes, incluant au moins un menhir.");
    }
    @Test
    public void testHasMenhirWithMultipleMenhirZones() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> forestWithMultipleMenhirs = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(forestWithMultipleMenhirs), "Doit retourner true pour une forêt avec plusieurs zones de menhirs.");
    }
    @Test
    public void testHasMenhirWithForestContainingMenhirAndOtherFeatures() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS));
        Area<Zone.Forest> forestWithMenhirAndOtherFeatures = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(forestWithMenhirAndOtherFeatures), "Doit retourner true pour une forêt contenant des menhirs, même avec d'autres caractéristiques.");
    }
    @Test
    public void testHasMenhirWithManyNonMenhirZonesBeforeMenhirZone() {
        Set<Zone.Forest> zones = new HashSet<>();
        for (int i = 1; i <= 100; i++) {
            zones.add(new Zone.Forest(i, Zone.Forest.Kind.PLAIN));
        }
        zones.add(new Zone.Forest(101, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> largeForest = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(largeForest), "Doit retourner true même si de nombreuses zones sans menhirs précèdent une zone avec menhir.");
    }
    @Test
    public void testHasMenhirWithManyNonMenhirZonesBefoeMenhirZone() {
        Set<Zone.Forest> zones = new HashSet<>();
        for (int i = 1; i <= 100; i++) {
            zones.add(new Zone.Forest(i, Zone.Forest.Kind.PLAIN));
        }
        Area<Zone.Forest> largeForest = new Area<>(zones, Collections.emptyList(), 0);
        assertFalse(Area.hasMenhir(largeForest), "ya aucune zone avec menhir parmi les 100");
    }
    @Test
    public void testHasMenhirWithAllZonesContainingMenhirs() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> forestAllMenhirs = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(forestAllMenhirs), "Doit retourner true pour une forêt où toutes les zones contiennent des menhirs.");
    }
    @Test
    public void testHasMenhirWithNullZonesInForest() {
        Set<Zone.Forest> zones = new HashSet<>(List.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR)));
        Area<Zone.Forest> forestWithNullZones = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(forestWithNullZones), "Doit gérer correctement les zones nulles et retourner true si un menhir est présent.");
    }












    @Test
    public void testMajorityOccupantsIdentifiesCorrectMajority() {
        Set<Zone> zones = Set.of(); // Supposons que Zone est une interface ou classe existante
        List<PlayerColor> occupants = List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN, PlayerColor.GREEN, PlayerColor.GREEN, PlayerColor.BLUE);
        Area<Zone> area = new Area<>(zones, occupants, 0);

        Set<PlayerColor> majority = area.majorityOccupants();
        assertEquals(Set.of(PlayerColor.GREEN), majority, "GREEN devrait être la couleur majoritaire");
    }

    @Test
    public void testRiverFishCountWithNoRiversOrLakes() {
        Area<Zone.River> emptyRiverArea = new Area<>(new HashSet<>(), new ArrayList<>(), 0);
        assertEquals(0, Area.riverFishCount(emptyRiverArea), "Aucun poisson ne devrait être compté dans une aire sans rivières ni lacs");
    }
}

