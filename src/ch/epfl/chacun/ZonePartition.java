package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        throw new IllegalArgumentException();
    }

    public static final class Builder<Z extends Zone> {
        private HashSet<Area<Z>> areaPartition;
        public Builder(ZonePartition<Z> zonePartition) {
            this.areaPartition = new HashSet<>(Set.copyOf(zonePartition.areas()));
        }

        public void addSingleton(Z zone, int openConnections) {
            Set<Z> setZone = new HashSet<>();
            setZone.add(zone);
            areaPartition.add(new Area<>(setZone, new ArrayList<>(), openConnections));
        }


        public void addInitialOccupant(Z zone, PlayerColor color) {
            for (Area<Z> area : areaPartition) {
                if (area.zones().contains(zone) && !area.isOccupied()) {
                    areaPartition.add(new Area<>(area.zones(),
                            new ArrayList<>(List.of(color)),
                            area.openConnections()));
                    areaPartition.remove(area);
                    return;
                }
            }
            throw new IllegalArgumentException();
        }


        public void removeOccupant(Z zone, PlayerColor color) {
            for (Area<Z> area : areaPartition ) {
                if (area.zones().contains(zone) && area.occupants().contains(color)) {
                    List<PlayerColor> newOccupants = new ArrayList<>(List.copyOf(area.occupants()));
                    newOccupants.remove(color);

                    areaPartition.add(new Area<>(area.zones(), newOccupants, area.openConnections()));
                    areaPartition.remove(area);
                    return;
                }
            }
            throw new IllegalArgumentException();
        }

        public void removeAllOccupantsOf(Area<Z> area) {
            for (Area<Z> partitonArea : areaPartition) {
                if (partitonArea.equals(area)) {
                    areaPartition.add(new Area<>(area.zones(), new ArrayList<>(), area.openConnections()));
                    areaPartition.remove(area);
                    return;
                }
            }
            throw new IllegalArgumentException();
        }

        public void union(Z zone1, Z zone2) {
            Area<Z> area1 = null;
            Area<Z> area2 = null;
            for (Area<Z> partitonArea : areaPartition ) {
                if (partitonArea.zones().contains(zone1)) {
                    area1 = partitonArea;
                }
                if (partitonArea.zones().contains(zone2)) {
                    area2 = partitonArea;
                }
            }
            if (area1 != null && area2 != null) {
                Area<Z> newArea = area1.connectTo(area2);
                areaPartition.add(newArea);
                areaPartition.remove(area1);
                areaPartition.remove(area2);
            } else {
                throw new IllegalArgumentException();
            }
        }

        public ZonePartition<Z> build(){
            return new ZonePartition<>(areaPartition);
        }
    }
}
