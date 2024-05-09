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
        return areaContaining(areas, zone);
    }

    /**
     * Private function that does the job of finding the
     * area of a particular zone in the partition.
     * It is defined static to be used in the builder which
     * is also static and will need this function multiple times.
     *
     * @param <Z> the type of zones in the areas of partition
     * @param areas the set of areas to search in
     * @param zone the zone to search for
     * @return the area containing the zone
     * @throws IllegalArgumentException if the area is not in the partition
     */
    private static <Z extends Zone> Area<Z> areaContaining(Set<Area<Z>> areas, Z zone) {
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

        // Set of areas of the zone partition
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
         *                                  or if the area is already occupied
         */
        public void addInitialOccupant(Z zone, PlayerColor color) {
            Area<Z> area = areaContaining(areas, zone);
            areas.add(area.withInitialOccupant(color));
            areas.remove(area);
        }

        /**
         * Removes an occupant from the area from the specified zone
         * if the player (color) has at least one occupant there.
         *
         * @param zone the zone to remove the occupant from
         * @param color the color of the occupant
         * @throws IllegalArgumentException if the zone is not in the partition
         *                                  or if the player has no occupant there
         */
        public void removeOccupant(Z zone, PlayerColor color) {
            Area<Z> area = areaContaining(areas, zone);
            areas.add(area.withoutOccupant(color));
            areas.remove(area);
        }

        /**
         * Removes all occupants from the area if the area is in the partition.
         *
         * @param area the area to remove the occupants from
         * @throws IllegalArgumentException if the area is not in the partition
         */
        public void removeAllOccupantsOf(Area<Z> area) {
            Preconditions.checkArgument(areas.remove(area));
            areas.add(area.withoutOccupants());
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
            Area<Z> area1 = areaContaining(areas, zone1);
            Area<Z> area2 = areaContaining(areas, zone2);

            areas.add(area1.connectTo(area2));
            areas.remove(area1);
            areas.remove(area2);
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
