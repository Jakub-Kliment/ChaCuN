package ch.epfl.chacun;

/**
 * Non instantiable class for computing the
 * points of a given feature in the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class Points {

    /**
     * Private constructor that prevents instantiation
     */
    private Points() {}

    /**
     * Computes the points of a closed forest to the player with the
     * most occupants by adding the number of tiles multiplied by 2
     * and the number of mushroom groups multiplied by 3.
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
     * Computes the points of a closed river to the player with the most occupants
     * by adding the number of tiles and the number of fish in the river.
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
     * Computes the points of a meadow with different types of animals to
     * the player with the most occupants by adding the number of mammoths
     * multiplied by 3, the number of aurochs multiplied by 2 and the number of deer.
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
     * Computes the points of a river system to the player with
     * the most occupants by adding the number of fish in the river.
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
     * Computes the points for a placed logboat in a water zone to the
     * player who placed it by multiplying the number of lakes by 2.
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
     * Computes the points for a raft located in a water zone to the player
     * with the majority occupants by adding the number of lakes.
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
