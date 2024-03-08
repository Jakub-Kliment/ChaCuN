package ch.epfl.chacun;
/*
 *	Author:      Rami El Mallah
 *	Date:   07/03/2024
 */

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ZonePartitionTest48 {
    @Test
    void constructorWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.PURPLE, PlayerColor.PURPLE, PlayerColor.GREEN), 3);
        var forest4 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        var area2 = new Area<>(Set.of(forest4), List.of(), 1);
        assertDoesNotThrow(() -> {
            var zone1 = new ZonePartition<>(Set.of(area1, area2));
            var zone2 = new ZonePartition<>();
            assertEquals(Set.of(area1, area2), zone1.areas());
            assertEquals(Set.of(), zone2.areas());
        });
    }

    @Test
    void areaContainingWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.PURPLE, PlayerColor.PURPLE, PlayerColor.GREEN), 3);
        var forest4 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        var area2 = new Area<>(Set.of(forest4), List.of(), 1);
        var area3 = new Area<Zone.Forest>(Set.of(), List.of(), 3);
        var forest5 = new Zone.Forest(4, Zone.Forest.Kind.PLAIN);
        var zonePartition1 = new ZonePartition<>(Set.of(area1, area2, area3));
        var zonePartition2 = new ZonePartition<Zone.River>(Set.of());
        var river = new Zone.River(5, 2, null);
        assertEquals(area1, zonePartition1.areaContaining(forest2));
        assertEquals(area2, zonePartition1.areaContaining(forest4));
        assertThrows(IllegalArgumentException.class, () -> {
            zonePartition1.areaContaining(forest5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            zonePartition2.areaContaining(river);
        });
    }

    @Test
    void builderConstructorWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.PURPLE, PlayerColor.PURPLE, PlayerColor.GREEN), 3);
        var forest4 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        var area2 = new Area<>(Set.of(forest4), List.of(), 1);
        var area3 = new Area<Zone.Forest>(Set.of(), List.of(), 3);
        var zonePartition1 = new ZonePartition<>(Set.of(area1, area2, area3));
        var zonePartition2 = new ZonePartition<Zone.River>(Set.of());
        var builder1 = new ZonePartition.Builder<>(zonePartition1);
        var builder2 = new ZonePartition.Builder<>(zonePartition2);
        assertEquals(Set.of(area1, area2, area3), builder1.build().areas());
        assertEquals(Set.of(), builder2.build().areas());
    }

    @Test
    void builderAddSingletonWorks() {
        var river1 = new Zone.River(0, 2, null);
        var river2 = new Zone.River(1, 0, new Zone.Lake(8, 1, Zone.SpecialPower.LOGBOAT));
        var area1 = new Area<>(Set.of(river1, river2), List.of(PlayerColor.YELLOW, PlayerColor.GREEN), 3);
        var river3 = new Zone.River(2, 4, null);
        var area2 = new Area<>(Set.of(river3), List.of(), 1);
        var builder1 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area1, area2)));
        var river4 = new Zone.River(3, 1, new Zone.Lake(9, 0, null));
        builder1.addSingleton(river4, 2);
        var expectedArea1 = new Area<>(Set.of(river4), List.of(), 2);
        var builder2 = new ZonePartition.Builder<>(new ZonePartition<Zone.Meadow>());
        var meadow = new Zone.Meadow(4, List.of(new Animal(11, Animal.Kind.DEER)), null);
        builder2.addSingleton(meadow, 0);
        var expectedArea2 = new Area<>(Set.of(meadow), List.of(), 0);
        assertEquals(Set.of(area1, area2, expectedArea1), builder1.build().areas());
        assertEquals(Set.of(expectedArea2), builder2.build().areas());
    }

    @Test
    void builderAddInitialOccupantWorks() {
        var meadow1 = new Zone.Meadow(0, List.of(new Animal(10, Animal.Kind.MAMMOTH), new Animal(11, Animal.Kind.TIGER)), Zone.SpecialPower.WILD_FIRE);
        var meadow2 = new Zone.Meadow(1, List.of(), null);
        var meadow3 = new Zone.Meadow(2, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var meadow4 = new Zone.Meadow(3, List.of(new Animal(12, Animal.Kind.MAMMOTH)), null);
        var meadow5 = new Zone.Meadow(4, List.of(new Animal(13, Animal.Kind.AUROCHS)), null);
        var meadow6 = new Zone.Meadow(5, List.of(), Zone.SpecialPower.SHAMAN);
        var area1 = new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(), 2);
        var area2 = new Area<>(Set.of(meadow4, meadow5), List.of(PlayerColor.YELLOW), 1);

        var builder1 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area1, area2)));
        var builder2 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area1, area2)));
        builder1.addInitialOccupant(meadow2, PlayerColor.PURPLE);
        assertEquals(Set.of(area2, new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(PlayerColor.PURPLE), 2)), builder1.build().areas());
        assertThrows(IllegalArgumentException.class, () -> {
            builder2.addInitialOccupant(meadow5, PlayerColor.YELLOW);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            builder2.addInitialOccupant(meadow5, PlayerColor.RED);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            builder2.addInitialOccupant(meadow6, PlayerColor.BLUE);
        });
    }

    @Test
    void builderRemoveOccupantWorks() {
        var meadow1 = new Zone.Meadow(0, List.of(new Animal(10, Animal.Kind.MAMMOTH), new Animal(11, Animal.Kind.TIGER)), Zone.SpecialPower.WILD_FIRE);
        var meadow2 = new Zone.Meadow(1, List.of(), null);
        var meadow3 = new Zone.Meadow(2, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var meadow4 = new Zone.Meadow(3, List.of(new Animal(12, Animal.Kind.MAMMOTH)), null);
        var meadow5 = new Zone.Meadow(4, List.of(new Animal(13, Animal.Kind.AUROCHS)), null);
        var meadow6 = new Zone.Meadow(5, List.of(), Zone.SpecialPower.SHAMAN);
        var area1 = new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN), 2);
        var area2 = new Area<>(Set.of(meadow4, meadow5), List.of(PlayerColor.YELLOW, PlayerColor.PURPLE), 1);
        var area3 = new Area<>(Set.of(meadow6), List.of(), 3);

        var builder1 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area1, area2)));
        var builder2 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area1, area2)));
        var builder3 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area3)));
        builder1.removeOccupant(meadow2, PlayerColor.RED);
        assertEquals(Set.of(area2, new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(PlayerColor.RED, PlayerColor.GREEN), 2)), builder1.build().areas());
        assertThrows(IllegalArgumentException.class, () -> {
            builder2.removeOccupant(meadow4, PlayerColor.BLUE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            builder2.removeOccupant(meadow1, PlayerColor.BLUE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            builder3.removeOccupant(meadow6, PlayerColor.YELLOW);
        });
    }

    @Test
    void builderRemoveAllOccupantsOfWorks() {
        var river1 = new Zone.River(0, 2, null);
        var river2 = new Zone.River(1, 0, new Zone.Lake(8, 1, Zone.SpecialPower.LOGBOAT));
        var area1 = new Area<>(Set.of(river1, river2), List.of(PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.RED), 3);
        var river3 = new Zone.River(2, 4, null);
        var area2 = new Area<>(Set.of(river3), List.of(PlayerColor.RED, PlayerColor.BLUE), 1);
        var river4 = new Zone.River(3, 3, null);
        var area3 = new Area<>(Set.of(river4), List.of(), 2);

        var builder1 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area1, area2)));
        assertThrows(IllegalArgumentException.class, () -> {
            builder1.removeAllOccupantsOf(area3);
        });
        builder1.removeAllOccupantsOf(area1);
        assertEquals(Set.of(area2, new Area<>(Set.of(river1, river2), List.of(), 3)), builder1.build().areas());
    }

    @Test
    void builderUnionWorks() {
        var river1 = new Zone.River(0, 2, null);
        var river2 = new Zone.River(1, 0, new Zone.Lake(8, 1, Zone.SpecialPower.LOGBOAT));
        var area1 = new Area<>(Set.of(river1, river2), List.of(PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.RED), 3);
        var river3 = new Zone.River(2, 4, null);
        var area2 = new Area<>(Set.of(river3), List.of(PlayerColor.RED, PlayerColor.BLUE), 1);
        var river4 = new Zone.River(3, 3, null);

        var builder1 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area1, area2)));
        var builder2 = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(area1, area2)));
        assertThrows(IllegalArgumentException.class, () -> {
            builder1.union(river1, river4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            builder1.union(river4, river1);
        });
        builder1.union(river1, river3);
        assertEquals(Set.of(new Area<>(Set.of(river1, river3, river2), List.of(PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.RED, PlayerColor.BLUE), 2)), builder1.build().areas());
        builder2.union(river1, river2);
        assertEquals(Set.of(new Area<>(Set.of(river1, river2), List.of(PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.RED), 1), area2), builder2.build().areas());
    }
}
