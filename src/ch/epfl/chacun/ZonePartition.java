package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a partition of areas.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public record ZonePartition<Z extends Zone> (Set<Area<Z>> areas) {

    /**
     * Immutable constructor
     *
     * @param areas the areas of the partition
     */
    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    /**
     * Constructor without areas
     */
    public ZonePartition() {
        this(new HashSet<>());
    }

    /**
     * Checks whether a zone is in the area partition.
     *
     * @throws IllegalArgumentException if the area is not in the partition
     * @return the area containing the zone
     */
    public Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas)
            if (area.zones().contains(zone))
                return area;

        throw new IllegalArgumentException();
    }

    /**
     * Builder for the zone partition.
     */
    public static final class Builder<Z extends Zone> {

        private final HashSet<Area<Z>> areaPartition;

        /**
         * Constructor for the builder.
         *
         * @param zonePartition the zone partition to build
         */
        public Builder(ZonePartition<Z> zonePartition) {
            this.areaPartition = new HashSet<>(zonePartition.areas());
        }

        /**
         * Adds a single zone to the partition of an area.
         *
         * @param zone the zone to add
         * @param openConnections the number of open connections
         */
        public void addSingleton(Z zone, int openConnections) {
            Set<Z> setZone = new HashSet<>();
            setZone.add(zone);
            areaPartition.add(new Area<>(setZone, new ArrayList<>(), openConnections));
        }


        /**
         * Adds an initial occupant to the area.
         *
         * @param zone the zone to add the occupant to
         * @param color the color of the occupant
         * @throws IllegalArgumentException if the zone is not in the partition
         */
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

        /**
         * Removes an occupant from the area.
         *
         * @param zone the zone to remove the occupant from
         * @param color the color of the occupant
         * @throws IllegalArgumentException if the zone is not in the partition
         */
        public void removeOccupant(Z zone, PlayerColor color) {
            for (Area<Z> area : areaPartition) {
                if (area.zones().contains(zone) && area.occupants().contains(color)) {
                    List<PlayerColor> newOccupants = new ArrayList<>(area.occupants());
                    newOccupants.remove(color);

                    areaPartition.add(new Area<>(area.zones(), newOccupants, area.openConnections()));
                    areaPartition.remove(area);
                    return;
                }
            }
            throw new IllegalArgumentException();
        }

        /**
         * Removes all occupants from the area.
         *
         * @param area the area to remove the occupants from
         * @throws IllegalArgumentException if the area is not in the partition
         */
        public void removeAllOccupantsOf(Area<Z> area) {
            for (Area<Z> partitonArea : areaPartition) {
                if (partitonArea.equals(area)) {
                    areaPartition.remove(area);
                    areaPartition.add(new Area<>(area.zones(), new ArrayList<>(), area.openConnections()));
                    return;
                }
            }
            throw new IllegalArgumentException();
        }

        /**
         * Creates a union of two areas.
         *
         * @param zone1 the first zone
         * @param zone2 the second zone
         * @throws IllegalArgumentException if the zones are not in the partition
         */
        public void union(Z zone1, Z zone2) {
            Area<Z> area1 = null;
            Area<Z> area2 = null;

            for (Area<Z> onePartition : areaPartition) {
                if (onePartition.zones().contains(zone1))
                    area1 = onePartition;

                if (onePartition.zones().contains(zone2))
                    area2 = onePartition;
            }

            if (area1 != null && area2 != null) {

                Area<Z> newArea = area1.connectTo(area2);
                areaPartition.add(newArea);
                areaPartition.remove(area1);

                if (!area1.equals(area2))
                    areaPartition.remove(area2);

                return;
            }
            throw new IllegalArgumentException();
        }

        /**
         * Builds the zone partition.
         *
         * @return the zone partition
         */
        public ZonePartition<Z> build() {
            return new ZonePartition<>(areaPartition);
        }
    }
}
