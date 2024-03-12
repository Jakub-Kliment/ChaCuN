package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

public class MyZonePartitionsTest {

    Zone.Lake lake1 = new Zone.Lake(0, 1, null);
    Zone.Lake lake2 = new Zone.Lake(1, 1, Zone.SpecialPower.RAFT);

    Zone.River river1 = new Zone.River(2, 1, null);
    Zone.River river2 = new Zone.River(3, 0, lake2);
    Zone.River river3 = new Zone.River(4, 2, null);

    Zone.Forest forest1 = new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS);
    Zone.Forest forest2 = new Zone.Forest(6, Zone.Forest.Kind.PLAIN);
    Zone.Forest forest3 = new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR);




    @Test
    void zonePartitionsBuilderWorksWithNormalValues() {

    }
}
