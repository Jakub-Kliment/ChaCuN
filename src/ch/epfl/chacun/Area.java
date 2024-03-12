package ch.epfl.chacun;

import java.util.*;

/**
 * Represents an area of the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public record Area<Z extends Zone> (Set<Z> zones, List<PlayerColor> occupants, int openConnections) {

    // Immutable constructor
    public Area {
        Preconditions.checkArgument(openConnections >= 0);
        zones = Set.copyOf(zones);

        List<PlayerColor> unsortedOccupants = new ArrayList<>(occupants);
        Collections.sort(unsortedOccupants);
        occupants = List.copyOf(unsortedOccupants);
    }

    /**
     * Checks whether an area contains a forest of menhir kind
     *
     * @return true if an area contains a forest of kind menhir, false otherwise
     */
    public static boolean hasMenhir(Area<Zone.Forest> forest) {
        for (Zone.Forest zone : forest.zones()) {
            if (zone.kind().equals(Zone.Forest.Kind.WITH_MENHIR))
                return true;
        }
        return false;
    }


    /**
     * Returns the number of mushroom groups a forest area contains
     *
     * @param forest area of a forest zone
     * @return the number of forests with mushrooms
     */
    public static int mushroomGroupCount(Area<Zone.Forest> forest) {
        int mushroomCount = 0;
        for (Zone.Forest zone : forest.zones()) {
            if (zone.kind().equals(Zone.Forest.Kind.WITH_MUSHROOMS))
                mushroomCount++;

        }
        return mushroomCount;
    }

    /**
     * Returns the set of animals in a meadow area
     *
     * @param meadow area of a meadow zone
     * @return the set of animals in the meadow zone
     */
    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> animals = new HashSet<>();

        for (Zone.Meadow zone : meadow.zones()) {
            for (Animal animal : zone.animals()) {
                if (!cancelledAnimals.contains(animal))
                    animals.add(animal);
            }
        }
        return animals;
    }

    /**
     * Returns the number of fish in a river area
     *
     * @param river area of a river zone
     * @return the number of fish in the area of river zone
     */
    public static int riverFishCount(Area<Zone.River> river) {
        int fishCount = 0;
        Set<Zone.Lake> countedLake = new HashSet<>();
        for (Zone.River zone : river.zones()) {
                fishCount += zone.fishCount();

                // If the river is connected to a lake at both ends, the lake should only be counted once
                if (zone.hasLake() && countedLake.add(zone.lake()))
                    fishCount += zone.lake().fishCount();
        }
        return fishCount;
    }


    /**
     * Returns the number of fish in a river system
     *
     * @param riverSystem area of a river system (rivers and lakes)
     * @return the number of fish in the river system
     */
    public static int riverSystemFishCount(Area<Zone.Water> riverSystem) {
        int fishCount = 0;
        for (Zone.Water zone : riverSystem.zones())
                fishCount += zone.fishCount();

        return fishCount;
    }

    /**
     * Returns the number of lakes in a river system
     *
     * @param riverSystem area of a river system (rivers and lakes)
     * @return the number of lakes in the river system
     */
    public static int lakeCount(Area<Zone.Water> riverSystem) {
        int lakeCount = 0;
        for (Zone zone : riverSystem.zones()) {
            if (zone instanceof Zone.Lake)
                lakeCount++;
        }
        return lakeCount;
    }

    /**
     * Check whether an area is closed
     *
     * @return true if the area is closed, false otherwise
     */
    public boolean isClosed() {
        return openConnections == 0;
    }

    /**
     * Check whether an area is occupied
     *
     * @return true if the area is occupied, false otherwise
     */
    public boolean isOccupied() {
        return !occupants.isEmpty();
    }


    /**
     * Returns the set of player colors of majority occupants of the area
     *
     * @return the majority occupants of the area
     */
    public Set<PlayerColor> majorityOccupants() {

        // If the area is not occupied, return an empty set
        if (!isOccupied())
            return new HashSet<>();

        int[] occupantCountsByColor = new int[PlayerColor.ALL.size()];
        int max = 0;
        for (PlayerColor occupant : occupants) {
            occupantCountsByColor[occupant.ordinal()] ++;

            // Update the max count
            if (occupantCountsByColor[occupant.ordinal()] > max)
                max = occupantCountsByColor[occupant.ordinal()];
        }

        // Find the majority occupants
        Set<PlayerColor> majorityOccupants = new HashSet<>();
        for (int i = 0; i < occupantCountsByColor.length; i++) {
            if (occupantCountsByColor[i] == max)
                majorityOccupants.add(PlayerColor.ALL.get(i));
        }
        return majorityOccupants;
    }

    /**
     * Adds two areas together to form a new area
     *
     * @return the new area formed by the two areas
     */
    public Area<Z> connectTo(Area<Z> that) {

        Set<Z> setArea = new HashSet<>(zones);
        setArea.addAll(that.zones());

        List<PlayerColor> listColor = new ArrayList<>(occupants);
        if (!this.equals(that))
            listColor.addAll(that.occupants());


        // The number of open connections the number of open connections of this minus 2
        // If they are not the same we
        int nbConnections = openConnections - 2 + (!this.equals(that) ? that.openConnections : 0);

        return new Area<>(setArea, listColor, nbConnections);
    }

    /**
     * Returns a new area with the initial occupant added
     *
     * @return the hash code of the area
     */
    public Area<Z> withInitialOccupant(PlayerColor occupant) {
        Preconditions.checkArgument(!isOccupied());
        List<PlayerColor> newOccupants = new ArrayList<>(occupants);
        newOccupants.add(occupant);
        return new Area<>(zones(), newOccupants, openConnections());
    }

    /**
     * Returns a new area without the specified occupant
     *
     * @return the new area without the specified occupant
     */
    public Area<Z> withoutOccupant(PlayerColor occupant) {
        Preconditions.checkArgument(occupants().contains(occupant));
        List<PlayerColor> newOccupants = new ArrayList<>(occupants);
        newOccupants.remove(occupant);
        return new Area<>(zones(), newOccupants, openConnections());
    }

    /**
     * Returns a new area without any occupants
     *
     * @return the new area without any occupants
     */
    public Area<Z> withoutOccupants() {
        return new Area<>(zones(), new ArrayList<>(), openConnections());
    }

    /**
     * Returns a set of tile ids of the area
     *
     * @return the tile ids of the area
     */
    public Set<Integer> tileIds() {
        Set<Integer> tileIds = new HashSet<>();
        for (Z zone : zones())
            tileIds.add(Zone.tileId(zone.id()));

        return tileIds;
    }

    /**
     * Returns the zone with the specified special power if there is any
     *
     * @return the zone with the specified special power
     */
    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower) {
        for (Z zone : zones()) {
            if (zone.specialPower() != null && zone.specialPower().equals(specialPower))
                return zone;
        }
        return null;
    }
}
