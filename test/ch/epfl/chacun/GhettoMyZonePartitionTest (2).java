package ch.epfl.chacun.oldTests;

import static org.junit.jupiter.api.Assertions.*;

import ch.epfl.chacun.Area;
import ch.epfl.chacun.Zone;
import ch.epfl.chacun.ZonePartition;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MyZonePartitionTest {

    private final ZonePartition<Zone> partition = new ZonePartition<>(new HashSet<>());
    private final ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(partition);


    @Test
    void emptyPartition() {
        assertTrue(partition.areas().isEmpty());
    }

    @Test
    void newPartition() {
        Zone.Forest forest = new Zone.Forest(101, Zone.Forest.Kind.PLAIN);
        Set<Zone> zoneSet = Set.of(forest);
        Area<Zone> area = new Area<>(zoneSet, new ArrayList<>(), 0);
        Set<Area<Zone>> areas = Set.of(area);
        ZonePartition<Zone> customPartition = new ZonePartition<>(areas);

        assertEquals(area, customPartition.areaContaining(forest));
    }

    @Test
    void addSingleton() {
        Zone.Meadow meadow = new Zone.Meadow(202, List.of(), Zone.SpecialPower.SHAMAN);
        builder.addSingleton(meadow, 1);
        assertEquals(1, builder.build().areas().size());
    }

    @Test
    void union() {


        Zone.River river1 = new Zone.River(408, 10, null);
        Zone.River river2 = new Zone.River(409, 15, null);
        builder.addSingleton(river1, 4);
        builder.addSingleton(river2, 5);
        builder.union(river1, river2);


    }
}