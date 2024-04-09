package ch.epfl.chacun;

import java.util.*;

/**
 * Immutable record that represents a partition
 * of areas with the same zone type.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 *
 * @param <Z> the type of zones in the areas of partition
 * @param areas the areas of the partition
 */
public record ZonePartition<Z extends Zone> (Set<Area<Z>> areas) {

    /**
     * Compact constructor that ensures immutability
     * with a defensive copy of the set of areas
     */
    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    /**
     * Compact constructor without areas
     */
    public ZonePartition() {
        this(new HashSet<>());
    }

    /**
     * Returns the area where the zone is located in
     *
     * @return the area containing the zone
     * @throws IllegalArgumentException if the area is not in the partition
     */
    public Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas)
            if (area.zones().contains(zone))
                return area;
        throw new IllegalArgumentException();
    }

    /**
     * Builder for the zone partition.
     *
     * @param <Z> the type of zones in the areas of partition
     */
    public static final class Builder<Z extends Zone> {

        // Set of areas.
        private final HashSet<Area<Z>> areas;

        /**
         * Constructor for the builder that takes an existing
         * zone partition and helps build a new one from it.
         *
         * @param zonePartition the zone partition to build
         */
        public Builder(ZonePartition<Z> zonePartition) {
            areas = new HashSet<>(zonePartition.areas());
        }

        /**
         * Adds a single zone to the partition with no occupants
         * and a given number of open connections.
         *
         * @param zone the zone to add
         * @param openConnections the number of open connections
         */
        public void addSingleton(Z zone, int openConnections) {
            areas.add(new Area<>(Set.of(zone), new ArrayList<>(), openConnections));
        }


        /**
         * Adds an initial occupant of the given color
         * and places it to the area to the given zone
         *
         * @param zone the zone to add the occupant to
         * @param color the color of the player that places the occupant
         * @throws IllegalArgumentException if the zone is not in the partition
         */
        public void addInitialOccupant(Z zone, PlayerColor color) {
            for (Area<Z> area : areas) {
                if (area.zones().contains(zone) && !area.isOccupied()) {
                    areas.add(new Area<>(
                            area.zones(),
                            List.of(color),
                            area.openConnections()));
                    areas.remove(area);
                    return;
                }
            }
            throw new IllegalArgumentException();
        }

        /**
         * Removes an occupant from the area from the specified zone
         * if the player (color) has at least one occupant there.
         *
         * @param zone the zone to remove the occupant from
         * @param color the color of the occupant
         * @throws IllegalArgumentException if the zone is not in the partition
         */
        public void removeOccupant(Z zone, PlayerColor color) {
            for (Area<Z> area : areas) {
                if (area.zones().contains(zone) && area.occupants().contains(color)) {
                    List<PlayerColor> newOccupants = new ArrayList<>(area.occupants());
                    newOccupants.remove(color);

                    areas.add(new Area<>(
                            area.zones(),
                            newOccupants,
                            area.openConnections()));
                    areas.remove(area);
                    return;
                }
            }
            throw new IllegalArgumentException();
        }

        /**
         * Removes all occupants from the area if the area is in the partition.
         *
         * @param area the area to remove the occupants from
         * @throws IllegalArgumentException if the area is not in the partition
         */
        public void removeAllOccupantsOf(Area<Z> area) {
            for (Area<Z> areaPartition : areas) {
                if (areaPartition == area) {
                    areas.remove(area);
                    areas.add(new Area<>(
                            area.zones(),
                            new ArrayList<>(),
                            area.openConnections()));
                    return;
                }
            }
            throw new IllegalArgumentException();
        }

        /**
         * Creates a union of two areas to form a new one.
         *
         * @param zone1 the first zone
         * @param zone2 the second zone
         * @throws IllegalArgumentException if at least one of the zones
         *                                  is not in the partition
         */
        public void union(Z zone1, Z zone2) {
            Area<Z> area1 = null;
            Area<Z> area2 = null;

            for (Area<Z> onePartition : areas) {
                if (onePartition.zones().contains(zone1))
                    area1 = onePartition;

                if (onePartition.zones().contains(zone2))
                    area2 = onePartition;
            }

            if (area1 != null && area2 != null) {

                Area<Z> newArea = area1.connectTo(area2);
                areas.add(newArea);
                areas.remove(area1);

                if (!area1.equals(area2))
                    areas.remove(area2);

                return;
            }
            throw new IllegalArgumentException();

            /*
            ZonePartition<Z> partition = new ZonePartition<>(areas);
            Area<Z> area1 = partition.areaContaining(zone1);
            Area<Z> area2 = partition.areaContaining(zone2);

            areas.add(area1.connectTo(area2));
            areas.remove(area1);
            areas.remove(area2);
             */
        }

        /**
         * Builds the zone partition.
         *
         * @return the built zone partition
         */
        public ZonePartition<Z> build() {
            return new ZonePartition<>(areas);
        }
    }
}
