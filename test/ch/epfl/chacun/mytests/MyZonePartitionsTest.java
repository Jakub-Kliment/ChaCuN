package ch.epfl.chacun.mytests;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        ZonePartitions.Builder zonePartitions = new ZonePartitions.Builder(new ZonePartitions(
                zonePartitionForest, zonePartitionMeadow, zonePartitionRiver, zonePartitionWater));

        Assertions.assertEquals(zonePartitions.build().forests(), zonePartitionForest);
        Assertions.assertEquals(zonePartitions.build().meadows(), zonePartitionMeadow);
        Assertions.assertEquals(zonePartitions.build().rivers(), zonePartitionRiver);
        Assertions.assertEquals(zonePartitions.build().riverSystems(), zonePartitionWater);
    }

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

    Zone.Meadow meadow1SecondTileZone = new Zone.Meadow(170, new ArrayList<>(), null);
    Zone.Meadow meadow2SecondTileZone = new Zone.Meadow(172, List.of(new Animal(1720, Animal.Kind.DEER)), null);
    Zone.Meadow meadow3SecondTileZone = new Zone.Meadow(174, List.of(new Animal(1720, Animal.Kind.TIGER)), null);

    Zone.River river1SecondTileZone = new Zone.River(171, 0, null);
    Zone.River river2SecondTileZone = new Zone.River(173, 0, null);
    TileSide n2 = new TileSide.River(meadow1SecondTileZone, river1SecondTileZone, meadow2SecondTileZone);
    TileSide e2 = new TileSide.River(meadow2SecondTileZone, river1SecondTileZone, meadow1SecondTileZone);
    TileSide s2 = new TileSide.River(meadow1SecondTileZone, river2SecondTileZone, meadow3SecondTileZone);
    TileSide w2 = new TileSide.River(meadow3SecondTileZone, river2SecondTileZone, meadow1SecondTileZone);
    Tile secondTile_17 = new Tile(17, Tile.Kind.NORMAL, n2, e2, s2, w2);

    @Test
    void addTileAddsATileToZonePartitionsCorrectly() {
        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(startingTile);

        ZonePartitions zonePartitions = new ZonePartitions(
                zonePartitionsBuilder.build().forests(),
                zonePartitionsBuilder.build().meadows(),
                zonePartitionsBuilder.build().rivers(),
                zonePartitionsBuilder.build().riverSystems());

        ZonePartition<Zone.Forest> forestZonePartition =  new ZonePartition<>(Set.of(
                new Area<>(Set.of(forestStartingZone), new ArrayList<>(), 2)));
        Assertions.assertEquals(forestZonePartition, zonePartitions.forests());

        ZonePartition<Zone.Meadow> meadowZonePartition =  new ZonePartition<>(Set.of(
                new Area<>(Set.of(meadowStartingZone), new ArrayList<>(), 2),
                new Area<>(Set.of(meadowStartingZone2), new ArrayList<>(), 1)));
        Assertions.assertEquals(meadowZonePartition, zonePartitions.meadows());

        ZonePartition<Zone.River> riverZonePartition =  new ZonePartition<>(Set.of(
                new Area<>(Set.of(riverStartingZone), new ArrayList<>(), 1)));
        Assertions.assertEquals(riverZonePartition, zonePartitions.rivers());

        ZonePartition<Zone.Water> waterZonePartition =  new ZonePartition<>(Set.of(
                new Area<>(Set.of(riverStartingZone, lakeStartingZone), new ArrayList<>(), 1)));
        Assertions.assertEquals(waterZonePartition, zonePartitions.riverSystems());
    }

    @Test
    void connectSidesWorksWithTrivialExample() {
        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(startingTile);
        zonePartitionsBuilder.addTile(secondTile_17);
        zonePartitionsBuilder.connectSides(w, e2);
    }

    @Test
    void connectSidesWorksWithNonTrivialExample() {
        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(startingTile);
        zonePartitionsBuilder.addTile(secondTile_17);
        zonePartitionsBuilder.connectSides(w, e2);

        ZonePartitions zonePartitions = new ZonePartitions(
                zonePartitionsBuilder.build().forests(),
                zonePartitionsBuilder.build().meadows(),
                zonePartitionsBuilder.build().rivers(),
                zonePartitionsBuilder.build().riverSystems());

        ZonePartition<Zone.Meadow> meadowZonePartition =  new ZonePartition<>(Set.of(
                new Area<>(Set.of(meadowStartingZone, meadow1SecondTileZone, meadow2SecondTileZone, meadow3SecondTileZone), new ArrayList<>(), 4)));
        //Assertions.assertThrows(IllegalArgumentException.class , () -> zonePartitions.meadows().areaContaining(meadowStartingZone));
    }
}
