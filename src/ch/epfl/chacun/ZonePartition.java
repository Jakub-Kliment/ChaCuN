package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Set;

public record ZonePartition<Z extends Zone> (Set<Area<Z>> areas) {
    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    public ZonePartition() {
        this(new HashSet<>());
    }

    public Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas) {
            if (area.zones().contains(zone)) {
                return area;
            }
        }
        Preconditions.checkArgument(false);
        return null;
    }

    public static final class Builder<Z> {
        //private HashSet<Area<Z>>
    }
}
