package ch.epfl.chacun;

public final class Points {
    private Points() {}

    static int forClosedForest(int tileCount, int mushroomGroupCount) {
        Preconditions.checkArgument(tileCount > 1 && mushroomGroupCount >= 0);
        return tileCount * 2 + mushroomGroupCount * 3;
    }

    static int forClosedRiver(int tileCount, int fishCount) {
        Preconditions.checkArgument(tileCount > 1 && fishCount >= 0);
        return tileCount + fishCount;
    }

    static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        Preconditions.checkArgument(mammothCount >= 0 && aurochsCount >= 0 && deerCount >= 0);
        return mammothCount * 3 + aurochsCount * 2 + deerCount;
    }

    static int forRiverSystem(int fishCount) {
        Preconditions.checkArgument(fishCount >= 0);
        return fishCount;
    }

    static int forLogboat(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount * 2;
    }

    static int forRaft(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount;
    }
}
