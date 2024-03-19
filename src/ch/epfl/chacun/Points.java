package ch.epfl.chacun;

/**
 * Class for computing the points of a given feature.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class Points {

    // Prevents instantiation (immutable class)
    private Points() {}

    /**
     * Computes the points of a closed forest.
     *
     * @param tileCount the number of tiles
     * @param mushroomGroupCount the number of mushroom groups
     * @throws IllegalArgumentException if the tile count is smaller or equal to one or
     *                                  the number of mushroom groups is smaller than zero
     * @return the points of a closed forest
     */
    public static int forClosedForest(int tileCount, int mushroomGroupCount) {
        Preconditions.checkArgument(tileCount > 1 && mushroomGroupCount >= 0);
        return tileCount * 2 + mushroomGroupCount * 3;
    }

    /**
     * Computes the points of a closed river.
     *
     * @param tileCount the number of tiles
     * @param fishCount the number of fish
     * @throws IllegalArgumentException if the tile count is smaller or equal to one or
     *                                  the number of fish is smaller than zero
     * @return the points of the for a closed river
     */
    public static int forClosedRiver(int tileCount, int fishCount) {
        Preconditions.checkArgument(tileCount > 1 && fishCount >= 0);
        return tileCount + fishCount;
    }

    /**
     * Computes the points of a meadow with different types of animals.
     *
     * @param mammothCount the number of mammoths
     * @param aurochsCount the number of aurochs
     * @param deerCount the number of deer
     * @throws IllegalArgumentException if the number of any animal kind is smaller than zero
     * @return the points for a meadow
     */
    public static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        Preconditions.checkArgument(mammothCount >= 0 && aurochsCount >= 0 && deerCount >= 0);
        return mammothCount * 3 + aurochsCount * 2 + deerCount;
    }

    /**
     * Computes the points of a river system.
     *
     * @param fishCount the number of fish
     * @throws IllegalArgumentException if the number of fish is smaller than zero
     * @return the points for river system
     */
    public static int forRiverSystem(int fishCount) {
        Preconditions.checkArgument(fishCount >= 0);
        return fishCount;
    }

    /**
     * Computes the points if a logboat is located in a water zone.
     *
     * @param lakeCount the number of lakes
     * @throws IllegalArgumentException if the number of lakes is smaller or equal to zero
     * @return the additional points if a logboat is located in a water zone
     */
    public static int forLogboat(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount * 2;
    }

    /**
     * Computes the points if a raft is located in a water zone.
     *
     * @param lakeCount the number of lakes
     * @throws IllegalArgumentException if the number of lakes is smaller or equal to zero
     * @return the additional points if a raft is located in a water zone
     */
    public static int forRaft(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount;
    }
}
