package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyZonePartitionTest {
    ZonePartition<Zone.Forest> partition = new ZonePartition<>();
    @Test
    void setPartitionUnionTest(){
        Zone.Forest zone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Area<Zone.Forest> area1 = new Area<>(new HashSet<>(Set.of(zone1)), new ArrayList<>(), 1);
        Zone.Forest zone2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Area<Zone.Forest> area2 = new Area<>(new HashSet<>(Set.of(zone2)), new ArrayList<>(), 1);
        Area<Zone.Forest> area3 = area1.connectTo(area2);
        ZonePartition<Zone.Forest> partitionSur = new ZonePartition<>(new HashSet<>(Set.of(area3)));
        ZonePartition<Zone.Forest> partitionBase = new ZonePartition<>(new HashSet<>(Set.of(area1, area2)));
        ZonePartition.Builder<Zone.Forest> partitionTestBuilders = new ZonePartition.Builder<>(partitionBase);
        partitionTestBuilders.union(zone1, zone2);
        partitionBase = partitionTestBuilders.build();
        assertEquals(partitionSur, partitionBase);
    }
}