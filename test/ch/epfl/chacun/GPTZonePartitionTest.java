package ch.epfl.chacun;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GPTZonePartitionTest {
    private ZonePartition<Zone> zonePartition;

    @Test
    void setUp() {
        zonePartition = new ZonePartition<>();
    }

    @Test
    void immutableConstructor() {
        Set<Area<Zone>> areas = new HashSet<>();
        Set<Zone> zones1 = new HashSet<>();
        zones1.add(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        Set<Zone> zones2 = new HashSet<>();
        zones2.add(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR));

        areas.add(new Area<>(zones1, new ArrayList<>(), 3));
        areas.add(new Area<>(zones2, new ArrayList<>(), 4));

        ZonePartition<Zone> partition = new ZonePartition<>(areas);

        // Ensure areas are copied to prevent external modification
        assertNotSame(areas, partition.areas());
    }


    @Test
    void areaContainingZone() {
        // Create some mock zones and areas
        Zone zone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone zone2 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR);
        Zone zone3 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Zone zone4 = new Zone.Forest(4, Zone.Forest.Kind.WITH_MENHIR);

        Set<Zone> zones1 = Set.of(zone1, zone2);
        Set<Zone> zones2 = Set.of(zone3, zone4);

        Area<Zone> area1 = new Area<>(zones1, new ArrayList<>(), 3);
        Area<Zone> area2 = new Area<>(zones2, new ArrayList<>(), 4);

        Set<Area<Zone>> areas = new HashSet<>();
        areas.add(area1);
        areas.add(area2);

        zonePartition = new ZonePartition<>(areas);

        // Test areaContaining method
        assertEquals(area1, zonePartition.areaContaining(zone1));
        assertEquals(area1, zonePartition.areaContaining(zone2));
        assertEquals(area2, zonePartition.areaContaining(zone3));
        assertEquals(area2, zonePartition.areaContaining(zone4));

        // Test exception when zone is not in any area
        assertThrows(IllegalArgumentException.class, () -> zonePartition.areaContaining(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)));
    }

    @Test
    void builderConstructor() {
        // Create a sample ZonePartition
        Set<Area<Zone>> areas = new HashSet<>();
        Set<Zone> zones1 = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN),
                new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR));
        Set<Zone> zones2 = Set.of(new Zone.Forest(3, Zone.Forest.Kind.PLAIN),
                new Zone.Forest(4, Zone.Forest.Kind.WITH_MENHIR));
        areas.add(new Area<>(zones1, new ArrayList<>(), 3));
        areas.add(new Area<>(zones2, new ArrayList<>(), 4));
        ZonePartition<Zone> zonePartition = new ZonePartition<>(areas);

        // Create a ZonePartition.Builder using the constructor
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(zonePartition);

        // Build a ZonePartition using the builder
        ZonePartition<Zone> builtZonePartition = builder.build();

        // Verify that the built ZonePartition is the same as the original one
        assertEquals(zonePartition, builtZonePartition);
    }

    @Test
    void addSingleton() {
        // Create a sample ZonePartition
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(new ZonePartition<>());

        // Add a singleton zone to the partition using the addSingleton method
        Zone zoneToAdd = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        int openConnections = 3;
        builder.addSingleton(zoneToAdd, openConnections);

        // Build the ZonePartition
        ZonePartition<Zone> zonePartition = builder.build();

        // Verify that the ZonePartition contains the added singleton zone with the correct open connections
        boolean zoneFound = false;
        for (Area<Zone> area : zonePartition.areas()) {
            if (area.zones().contains(zoneToAdd) && area.openConnections() == openConnections) {
                zoneFound = true;
                break;
            }
        }
        assertTrue(zoneFound);
    }

    @Test
    void addInitialOccupant() {
        // Create a sample ZonePartition with an empty area
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(new ZonePartition<>());
        Zone zoneToAdd = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        int openConnections = 3;
        builder.addSingleton(zoneToAdd, openConnections);

        // Add an initial occupant to the area
        PlayerColor initialOccupant = PlayerColor.RED;
        builder.addInitialOccupant(zoneToAdd, initialOccupant);

        // Build the ZonePartition
        ZonePartition<Zone> zonePartition = builder.build();

        // Verify that the ZonePartition contains the added initial occupant in the correct area
        boolean occupantFound = false;
        for (Area<Zone> area : zonePartition.areas()) {
            if (area.zones().contains(zoneToAdd) && area.isOccupied() && area.occupants().contains(initialOccupant)) {
                occupantFound = true;
                break;
            }
        }
        assertTrue(occupantFound);
    }

    @Test
    void removeOccupant() {
        // Create a sample ZonePartition with an area containing an initial occupant
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(new ZonePartition<>());
        Zone zoneToAdd = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        int openConnections = 3;
        builder.addSingleton(zoneToAdd, openConnections);
        PlayerColor initialOccupant = PlayerColor.RED;
        builder.addInitialOccupant(zoneToAdd, initialOccupant);

        // Remove the initial occupant from the area
        builder.removeOccupant(zoneToAdd, initialOccupant);

        // Build the ZonePartition
        ZonePartition<Zone> zonePartition = builder.build();

        // Verify that the ZonePartition contains the area without the removed occupant
        boolean occupantRemoved = true;
        for (Area<Zone> area : zonePartition.areas()) {
            if (area.zones().contains(zoneToAdd) && area.isOccupied() && area.occupants().contains(initialOccupant)) {
                occupantRemoved = false;
                break;
            }
        }
        assertTrue(occupantRemoved);
    }

    @Test
    void removeAllOccupantsOf() {
        // Create a sample ZonePartition with an area containing occupants
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(new ZonePartition<>());
        Zone zoneToAdd = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        int openConnections = 3;
        builder.addSingleton(zoneToAdd, openConnections);
        PlayerColor occupant = PlayerColor.RED;
        builder.addInitialOccupant(zoneToAdd, occupant);

        // Remove all occupants from the area
        builder.removeAllOccupantsOf(new Area<>(Set.of(zoneToAdd), List.of(occupant), openConnections));

        // Build the ZonePartition
        ZonePartition<Zone> zonePartition = builder.build();

        // Verify that the ZonePartition contains the area without any occupants
        boolean noOccupants = true;
        for (Area<Zone> area : zonePartition.areas()) {
            if (area.zones().contains(zoneToAdd) && area.isOccupied()) {
                noOccupants = false;
                break;
            }
        }
        assertTrue(noOccupants);
    }


    @Test
    void union() {
        // Create a sample ZonePartition with two areas
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(new ZonePartition<>());
        Zone zone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone zone2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        int openConnections = 3;
        builder.addSingleton(zone1, openConnections);
        builder.addSingleton(zone2, openConnections);

        // Perform a union operation on the two zones
        builder.union(zone1, zone2);

        // Build the ZonePartition
        ZonePartition<Zone> zonePartition = builder.build();

        // Verify that the ZonePartition contains a single area after the union
        assertEquals(1, zonePartition.areas().size());

        // Verify that the new area contains both zones
        Set<Zone> expectedZones = new HashSet<>();
        expectedZones.add(zone1);
        expectedZones.add(zone2);
        for (Area<Zone> area : zonePartition.areas()) {
            assertEquals(expectedZones, area.zones());
        }
    }

    @Test
    void unionSameAreaTest() {
        // Create a sample ZonePartition with one area containing two zones
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(new ZonePartition<>());
        Zone zone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone zone2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        int openConnections = 3;
        builder.addSingleton(zone1, openConnections);
        builder.addSingleton(zone2, openConnections);

        // Perform a union operation on the two zones belonging to the same area
        builder.union(zone1, zone2);

        // Build the ZonePartition
        ZonePartition<Zone> zonePartition = builder.build();

        // Verify that the ZonePartition contains only one area
        assertEquals(1, zonePartition.areas().size());

        // Verify that the area still contains both zones
        Set<Zone> expectedZones = new HashSet<>();
        expectedZones.add(zone1);
        expectedZones.add(zone2);
        for (Area<Zone> area : zonePartition.areas()) {
            assertEquals(expectedZones, area.zones());
        }
    }

    @Test
    void buildEmptyBuilder() {
        // Create an empty ZonePartition.Builder
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(new ZonePartition<>());

        // Build the ZonePartition
        ZonePartition<Zone> zonePartition = builder.build();

        // Verify that the built ZonePartition contains no areas
        assertTrue(zonePartition.areas().isEmpty());
    }

    /*
    @Test
    void buildWithAreas() {
        // Create a sample ZonePartition with one area containing two zones
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(new ZonePartition<>());
        Zone zone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone zone2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        int openConnections = 3;
        builder.addSingleton(zone1, openConnections);
        builder.addSingleton(zone2, openConnections);

        // Build the ZonePartition
        ZonePartition<Zone> zonePartition = builder.build();

        // Verify that the built ZonePartition contains the correct areas
        Set<Area<Zone>> expectedAreas = new HashSet<>();
        expectedAreas.add(new Area<>(Set.of(zone1, zone2), List.of(), openConnections));
        assertEquals(expectedAreas, zonePartition.areas());

    }
     */
}