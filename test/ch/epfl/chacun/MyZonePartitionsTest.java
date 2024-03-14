package ch.epfl.chacun;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyZonePartitionsTest {

    Zone.Lake lake1 = new Zone.Lake(0, 1, null);
    Zone.Lake lake2 = new Zone.Lake(1, 1, Zone.SpecialPower.RAFT);

    Zone.River river1 = new Zone.River(2, 1, null);
    Zone.River river2 = new Zone.River(3, 0, lake2);
    Zone.River river3 = new Zone.River(4, 2, null);

    Zone.Forest forest1 = new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS);
    Zone.Forest forest2 = new Zone.Forest(6, Zone.Forest.Kind.PLAIN);
    Zone.Forest forest3 = new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR);

    Animal animal1 = new Animal(0, Animal.Kind.AUROCHS);
    Animal animal2 = new Animal(1, Animal.Kind.DEER);
    Zone.Meadow meadow1 = new Zone.Meadow(8, List.of(animal1, animal2), null);
    Zone.Meadow meadow2 = new Zone.Meadow(9, List.of(animal1), Zone.SpecialPower.HUNTING_TRAP);
    Zone.Meadow meadow3 = new Zone.Meadow(10, List.of(animal2), Zone.SpecialPower.WILD_FIRE);

    Area<Zone.Forest> forestArea = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.RED), 2);
    ZonePartition<Zone.Forest> zonePartitionForest = new ZonePartition<>(Set.of(forestArea));
    ZonePartition<Zone.Meadow> zonePartitionMeadow = new ZonePartition<>(Set.of(new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(PlayerColor.BLUE), 3)));
    ZonePartition<Zone.River> zonePartitionRiver = new ZonePartition<>(Set.of(new Area<>(Set.of(river1, river2), List.of(PlayerColor.GREEN), 1)));
    ZonePartition<Zone.Water> zonePartitionWater = new ZonePartition<>(Set.of(new Area<>(Set.of(lake2, river2, river3), List.of(PlayerColor.YELLOW), 2)));
    Area<Zone> area = new Area<>(Set.of(lake1, river2, forest3, meadow1, meadow3), List.of(PlayerColor.RED), 1);

    @Test
    void zonePartitionsBuilderWorksWithNormalValues() {
        ZonePartitions zonePartitions = new ZonePartitions(
                zonePartitionForest, zonePartitionMeadow, zonePartitionRiver, zonePartitionWater);
        ZonePartitions zonePartitionsNew = new ZonePartitions.Builder(zonePartitions).build();

        Assertions.assertEquals(zonePartitions.forests(), zonePartitionForest);
        Assertions.assertEquals(zonePartitions.meadows(), zonePartitionMeadow);
        Assertions.assertEquals(zonePartitions.rivers(), zonePartitionRiver);
        Assertions.assertEquals(zonePartitions.riverSystems(), zonePartitionWater);
    }

    Zone.Meadow meadowStartingZone = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)), null);
    Zone.Forest forestStartingZone = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow meadowStartingZone2 = new Zone.Meadow(562, null, null);
    Zone.Lake lakeStartingZone = new Zone.Lake(568, 1, null);
    Zone.River riverStartingZone = new Zone.River(563, 0, lakeStartingZone);
    TileSide n = new TileSide.Meadow(meadowStartingZone);
    TileSide e = new TileSide.Forest(forestStartingZone);
    TileSide s = new TileSide.Forest(forestStartingZone);
    TileSide w = new TileSide.River(meadowStartingZone2, riverStartingZone, meadowStartingZone);
    Tile startingTile = new Tile(56, Tile.Kind.NORMAL, n, e, s, w);

    @Test
    void addTileAddsATileToZonePartitionsCorrectly() {
        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(startingTile);

        ZonePartitions zonePartitions = new ZonePartitions(
                zonePartitionsBuilder.build().forests(),
                zonePartitionsBuilder.build().meadows(),
                zonePartitionsBuilder.build().rivers(),
                zonePartitionsBuilder.build().riverSystems());

        ZonePartition<Zone.Forest> forestZonePartition =  new ZonePartition<Zone.Forest>();
        //Assertions.assertEquals(, zonePartitions.forests());
    }
}
