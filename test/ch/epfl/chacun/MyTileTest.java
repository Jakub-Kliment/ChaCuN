package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyTileTest {

    @Test
    void sides() {
        TileSide.Forest forest1 = new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        TileSide.Forest forest2 = new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        TileSide.Forest forest3 = new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.PLAIN));
        TileSide.Forest forest4 = new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN));
        Tile tile = new Tile(0, Tile.Kind.NORMAL, forest1, forest2, forest3, forest4);
        assertEquals(List.of(forest1, forest2, forest3, forest4), tile.sides());
        assertNotEquals(List.of(forest2, forest1, forest3, forest4), tile.sides());
    }

    @Test
    void sideZones() {
        Zone.Forest forest1z = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2z = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow1z = new Zone.Meadow(3, new ArrayList<>(), null);
        Zone.Meadow meadow2z = new Zone.Meadow(4, new ArrayList<>(), null);
        Zone.Meadow meadow3z = new Zone.Meadow(5, new ArrayList<>(), null);
        Zone.River river1z = new Zone.River(6, 0, null);
        Zone.River river2z = new Zone.River(7, 0, new Zone.Lake(8, 0, null));
        TileSide.Forest forest1 = new TileSide.Forest(forest1z);
        TileSide.Forest forest2 = new TileSide.Forest(forest2z);
        TileSide.Meadow meadow1 = new TileSide.Meadow(meadow1z);
        TileSide.Meadow meadow2 = new TileSide.Meadow(meadow2z);
        TileSide.River river1 = new TileSide.River(meadow1z, river1z, meadow3z);
        TileSide.River river2 = new TileSide.River(meadow1z, river2z, meadow3z);
        TileSide.River river3 = new TileSide.River(meadow3z, river1z, meadow1z);
        Tile tile1 = new Tile(10, Tile.Kind.NORMAL, river1, forest1, forest2, meadow1 );
        Tile tile2 = new Tile(11, Tile.Kind.NORMAL, river2, forest1, forest2, meadow1 );
        Tile tile3 = new Tile(12, Tile.Kind.NORMAL, river1, river3, meadow1, meadow1 );
        assertEquals(Set.of(meadow1z, river1z, meadow3z, forest1z, forest2z), tile1.sideZones());
        assertEquals(Set.of(meadow1z, river2z, meadow3z, forest1z, forest2z), tile2.sideZones());
        assertEquals(Set.of(meadow1z, river1z, meadow3z), tile3.sideZones());
    }

    @Test
    void zones() {
    }
}