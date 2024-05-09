package ch.epfl.chacun;

import java.util.*;

/**
 * Represents the board of the game with all its components.
 * Is an immutable class with a private constructor and a set
 * of methods to manipulate the board that ensure immutability.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class Board {

    // Placed tiles of the board on their positions (index)
    private final PlacedTile[] placedTiles;

    // Indexes of the placed tiles in order of their placement
    private final int[] index;

    // Partitions of areas on the board
    private final ZonePartitions zonePartitions;

    // Set of all cancelled animals on the board
    private final Set<Animal> cancelledAnimals;

    /**
     * The size of the board (total number of possible positions)
     */
    private static final int BOARD_SIZE = 625;

    /**
     * The reach of a board from its center to its edges
     */
    public static final int REACH = 12;

    /**
     * The empty board with no placed tiles, no indexes,
     * no partitions and no cancelled animals.
     */
    public static final Board EMPTY = new Board(
            new PlacedTile[BOARD_SIZE],
            new int[0],
            ZonePartitions.EMPTY,
            new HashSet<>());

    /**
     * Private board constructor to keep the class immutable.
     * Since private, it is only called by the methods of the class
     * that ensure immutability by returning new instances of the board.
     *
     * @param placedTiles the placed tiles on the board
     * @param index the index of the placed tiles
     * @param zonePartitions the partitions of the zones on the board
     * @param cancelledAnimals the set of all cancelled animals
      */
    private Board(PlacedTile[] placedTiles,
                  int[] index,
                  ZonePartitions zonePartitions,
                  Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.index = index;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    /**
     * Returns the placed tile from the given position
     * on the board or null if there is no tile or if
     * the position is out of bounds.
     *
     * @param pos position of the placed tile
     * @return placed tile at the given position,
     *         or null if there is no tile
     */
    public PlacedTile tileAt(Pos pos) {
        int position = indexFromPosition(pos);
        if (position < 0 || position >= BOARD_SIZE) return null;
        return placedTiles[position];
    }

    /**
     * Gets the placed tile by its id on the board
     * or throws an exception if the tile is not found.
     *
     * @param tileId id of the placed tile
     * @return placed tile to which corresponds the id
     * @throws IllegalArgumentException if the id of the tile does not
     *                                  match any tile on the board
     */
    public PlacedTile tileWithId(int tileId) {
        for (int i : index)
            if (placedTiles[i].id() == tileId)
                return placedTiles[i];
        throw new IllegalArgumentException();
    }

    /**
     * Returns a defensive copy of cancelled animals
     * to keep the class immutable.
     *
     * @return copy of the set of cancelled animals
     */
    public Set<Animal> cancelledAnimals() {
        return cancelledAnimals;
    }

    /**
     * Returns the set of all occupants on the board.
     *
     * @return set of all occupants on the board
     */
    public Set<Occupant> occupants() {
        Set<Occupant> occupants = new HashSet<>();
        for (int i : index)
            if (placedTiles[i].occupant() != null)
                occupants.add(placedTiles[i].occupant());
        return occupants;
    }

    /**
     * Returns the forest area containing the given forest zone
     * or throws an exception if the zone is not found in the partitions.
     *
     * @param forest forest zone to find the area of
     * @return the area of the forest containing the given forest zone
     */
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        return zonePartitions.forests().areaContaining(forest);
    }

    /**
     * Returns the meadow area containing the given meadow zone or
     * throws an exception if the zone is not found in the partitions.
     *
     * @param meadow meadow zone to find the area of
     * @return the area of the meadow containing the given meadow zone
     */
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        return zonePartitions.meadows().areaContaining(meadow);
    }

    /**
     * Returns the river area containing the given river zone or
     * throws an exception if the zone is not found in the partitions.
     *
     * @param riverZone river zone to find the area of
     * @return the area of the river containing the given river zone
     */
    public Area<Zone.River> riverArea(Zone.River riverZone) {
        return zonePartitions.rivers().areaContaining(riverZone);
    }

    /**
     * Returns the river system area containing the given water zone
     * or throws an exception if the zone is not found in the partitions.
     *
     * @param water water zone to find the area of
     * @return the area of the water system containing the given water zone
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return zonePartitions.riverSystems().areaContaining(water);
    }


    /**
     * Returns the set of all meadow areas on the board.
     *
     * @return the set of all meadow areas on the board
     */
    public Set<Area<Zone.Meadow>> meadowAreas() {
        return zonePartitions.meadows().areas();
    }


    /**
     * Returns the set of all river system areas on the board.
     *
     * @return the set of all river system areas on the board
     */
    public Set<Area<Zone.Water>> riverSystemAreas() {
        return zonePartitions.riverSystems().areas();
    }

    /**
     * Returns a meadow area of adjacent meadow zones that are in the same area.
     * For adjacent meadows we consider the 8 positions around the given position
     * in the 3x3 square centered at the given position (including the given position).
     *
     * @param pos position to find the adjacent meadow area of
     * @return the area of adjacent meadows that are in the same area
     */
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
        Area<Zone.Meadow> meadowArea = meadowArea(meadowZone);
        Set<Zone.Meadow> adjacentMeadow = new HashSet<>();

        // Creates a set of all adjacent positions to the given position
        Set<Pos> adjacentPositions = new HashSet<>();
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                adjacentPositions.add(pos.translated(i, j));

        // Adds all meadow zones that are adjacent to the given position
        for (Zone.Meadow zone : meadowArea.zones())
            if (adjacentPositions.contains(tileWithId(zone.tileId()).pos()))
                adjacentMeadow.add(zone);

        return new Area<>(adjacentMeadow, meadowArea.occupants(), 0);
    }

    /**
     * Counts the number of occupants of a given kind
     * on the board that are placed by a given player.
     *
     * @param player the player to count the occupants of
     * @param occupantKind the kind of the occupant to count
     * @return the number of occupants of the given kind and player on the board
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        int occupantCount = 0;
        for (Occupant occupant : occupants())
            if (tileWithId(Zone.tileId(occupant.zoneId())).placer() == player
                    && occupant.kind() == occupantKind)
                occupantCount++;
        return occupantCount;
    }

    /**
     * Returns the set of all insertion positions on the board where
     * a tile can potentially be placed (next to an already  placed tile).
     *
     * @return the set of all insertion positions on the board
     */
    public Set<Pos> insertionPositions() {
        Set<Pos> insertionPositions = new HashSet<>();
        for (int i : index) {
            Pos pos = positionFromIndex(i);
            for (Direction direction : Direction.ALL)
                if (tileAt(pos.neighbor(direction)) == null
                        && indexFromPosition(pos.neighbor(direction)) != -1)
                    insertionPositions.add(pos.neighbor(direction));
        }
        return insertionPositions;
    }

    /**
     * Returns the last placed tile on the board.
     *
     * @return the last placed tile on the board,
     *         or null if the board is empty
     */
    public PlacedTile lastPlacedTile() {
        if (index.length == 0) return null;
        return placedTiles[index[index.length - 1]];
    }

    /**
     * Returns the set of all forest areas closed by the last placed tile
     * on the board or an empty set if the board is empty (last placed tile is null).
     *
     * @return the set of forest areas closed by the last placed tile
     *         (or empty set if the board is empty)
     */
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        if (lastPlacedTile() == null) return new HashSet<>();

        Set<Area<Zone.Forest>> forestAreas = new HashSet<>();
        for (Zone.Forest forestZone : lastPlacedTile().forestZones())
            if (forestArea(forestZone).isClosed())
                forestAreas.add(forestArea(forestZone));
        return forestAreas;
    }

    /**
     * Returns the set of all river areas closed by the last placed tile
     * on the board or an empty set if the board is empty (last placed tile is null).
     *
     * @return the set of river areas closed by the last placed tile
     *         (or empty set if the board is empty)
     */
    public Set<Area<Zone.River>> riversClosedByLastTile() {
        if (lastPlacedTile() == null) return new HashSet<>();

        Set<Area<Zone.River>> riverAreas = new HashSet<>();
        for (Zone.River riverZone : lastPlacedTile().riverZones())
            if (riverArea(riverZone).isClosed())
                riverAreas.add(riverArea(riverZone));
        return riverAreas;
    }

    /**
     * Looks whether a tile can be added to the board at a certain
     * position by checking if its position is an insertion position
     * and if the sides match the sides of the neighboring tiles.
     *
     * @param tile the tile to be placed
     * @return true if the tile can be added, false otherwise
     */
    public boolean canAddTile(PlacedTile tile) {
        if (!insertionPositions().contains(tile.pos())) return false;

        for (Direction dir : Direction.ALL) {
            PlacedTile neighbor = tileAt(tile.pos().neighbor(dir));
            // If the sides differ, the tile cannot be added
            if (neighbor != null && !tile.side(dir)
                    .isSameKindAs(neighbor.side(dir.opposite())))
                return false;
        }
        return true;
    }

    /**
     * Checks if a tile can be placed on the board by checking all
     * possible rotations of the tile in all insertion positions.
     *
     * @param tile the tile to be placed
     * @return true if the tile can be placed, false otherwise
     */
    public boolean couldPlaceTile(Tile tile) {
        for (Pos pos : insertionPositions()) {
            // Looks for all possible rotations in an insertion position
            for (Rotation rot : Rotation.ALL) {
                PlacedTile possibleTile = new PlacedTile(
                        tile, null, rot, pos, null);
                if (canAddTile(possibleTile)) return true;
            }
        }
        return false;
    }

    /**
     * Adds a tile to the board and returns the new board with the tile placed.
     * The function assures that all the attributes of the board are kept immutable
     * and updated correctly with the new tile placed.
     * To keep the board immutable, a new board is returned with all modifications.
     *
     * @param tile the tile to be placed
     * @return the new board with the tile placed
     * @throws IllegalArgumentException if the tile cannot be placed
     *                                  and the board is not empty
     */
    public Board withNewTile(PlacedTile tile) {
        Preconditions.checkArgument(index.length == 0 || canAddTile(tile));

        // Defensive copy of placedTiles with the new tile added
        PlacedTile[] newPlacedTiles = placedTiles.clone();
        newPlacedTiles[indexFromPosition(tile.pos())] = tile;

        // Defensive copy of index with the new index added
        int[] newIndex = Arrays.copyOf(index, index.length + 1);
        newIndex[newIndex.length - 1] = indexFromPosition(tile.pos());

        // Defensive copy of zonePartitions with the new tile and its partitions added
        ZonePartitions.Builder newPartitions =
                new ZonePartitions.Builder(zonePartitions);
        newPartitions.addTile(tile.tile());
        for (Direction dir : Direction.ALL) {
            PlacedTile neighbor = tileAt(tile.pos().neighbor(dir));
            if (neighbor != null)
                newPartitions.connectSides(
                    tile.side(dir), neighbor.side(dir.opposite()));
        }
        return new Board(newPlacedTiles, newIndex,
                newPartitions.build(), cancelledAnimals());
    }

    /**
     * Adds an occupant to the board and returns the new board with the occupant placed.
     * The function ensures that all the attributes of the board are kept immutable by
     * creating copies and returning a new board with all modifications.
     *
     *
     *
     * @param occupant the occupant to be placed
     * @return the new board with the occupant placed
     * @throws IllegalArgumentException if the occupant cannot be placed because the tile
     *                                  is already occupied by another occupant
     */
    public Board withOccupant(Occupant occupant) {
        PlacedTile[] newTiles = placedTiles.clone();
        PlacedTile occupantTile = tileWithId(
                Zone.tileId(occupant.zoneId())).withOccupant(occupant);
        newTiles[indexFromPosition(occupantTile.pos())] = occupantTile;
        
        ZonePartitions.Builder newPartitions = 
                new ZonePartitions.Builder(zonePartitions);
        newPartitions.addInitialOccupant(
                occupantTile.placer(),
                occupant.kind(),
                occupantTile.zoneWithId(occupant.zoneId()));

        return new Board(newTiles, index,
                newPartitions.build(), cancelledAnimals());
    }

    /**
     * Removes an occupant from the board and returns the new board without the occupant.
     * The function ensures that all the attributes of the board are kept immutable by
     * creating copies and returning a new board with all modifications.
     *
     * @param occupant the occupant to be removed
     * @return the new board without the occupant
     */
    public Board withoutOccupant(Occupant occupant) {
        PlacedTile[] newTiles = placedTiles.clone();
        PlacedTile occupantTile = tileWithId(
                Zone.tileId(occupant.zoneId())).withNoOccupant();
        newTiles[indexFromPosition(occupantTile.pos())] = occupantTile;

        ZonePartitions.Builder newPartitions =
                new ZonePartitions.Builder(zonePartitions);
        newPartitions.removePawn(
                occupantTile.placer(),
                occupantTile.zoneWithId(occupant.zoneId()));

        return new Board(newTiles, index,
                newPartitions.build(), cancelledAnimals());
    }

    /**
     * Returns a new board with all gatherers or fishers removed from the given areas.
     * The function ensures that all the attributes of the board are kept immutable by
     * creating copies and returning a new board with all pawn occupants removed.
     *
     * @param forests the set of all forest areas to remove gatherers from
     * @param rivers the set of all river areas to remove fishers from
     * @return the set of all gatherers on the board
     */
    public Board withoutGatherersOrFishersIn(Set<Area<Zone.Forest>> forests,
                                             Set<Area<Zone.River>> rivers) {
        ZonePartitions.Builder newPartitions =
                new ZonePartitions.Builder(zonePartitions);
        PlacedTile[] newTiles = placedTiles.clone();

        Set<Integer> zoneIds = new HashSet<>();
        for (Area<Zone.Forest> forestArea : forests) {
            newPartitions.clearGatherers(forestArea);
            for (Zone.Forest forest : forestArea.zones())
                zoneIds.add(forest.id());
        }
        for (Area<Zone.River> riverArea : rivers) {
            newPartitions.clearFishers(riverArea);
            for (Zone.River river : riverArea.zones())
                zoneIds.add(river.id());
        }
        for (int i : index)
            if (zoneIds.contains(newTiles[i]
                    .idOfZoneOccupiedBy(Occupant.Kind.PAWN)))
                newTiles[i] = newTiles[i].withNoOccupant();

        return new Board(newTiles, index,
                newPartitions.build(), cancelledAnimals());
    }

    /**
     * Returns a new board with more animals cancelled by adding the given set of animals.
     * The function ensures the immutability of cancelled animals by creating a new set
     * with a copy of already cancelled animals and the newly cancelled animals.
     *
     * @param newlyCancelledAnimals the set of animals to add to the cancelled animals
     * @return the new board with more animals cancelled
     */
    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        Set<Animal> allCancelledAnimals = new HashSet<>(newlyCancelledAnimals);
        allCancelledAnimals.addAll(cancelledAnimals);
        return new Board(placedTiles, index, zonePartitions, Set.copyOf(allCancelledAnimals));
    }

    /**
     * Compares the board to the given object to check if they are equal,
     * which is the case if the object is a board and all its attributes are equal.
     *
     * @param that the object to compare the board to
     * @return true if the board is equal to the given object, false otherwise
     */
    @Override
    public boolean equals(Object that) {
        return that instanceof Board board
                && Arrays.equals(placedTiles, board.placedTiles)
                && Arrays.equals(index, board.index)
                && zonePartitions.equals(board.zonePartitions)
                && cancelledAnimals.equals(board.cancelledAnimals);
    }

    /**
     * Returns the hash code of the board based on its attributes
     *
     * @return the hash code of the board
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                Arrays.hashCode(placedTiles),
                Arrays.hashCode(index),
                zonePartitions,
                cancelledAnimals);
    }

    /**
     * Private function that returns the index of a tile
     * based on its position on the board. Returns -1 if
     * the position is out of bounds (reach of the board).
     *
     * @param pos position of the tile
     * @return the index of the tile
     */
    private int indexFromPosition(Pos pos) {
        if (Math.abs(pos.x()) > REACH || Math.abs(pos.y()) > REACH)
            return -1;
        return (pos.y() + REACH) * (2 * REACH + 1) + (pos.x() + REACH);
    }

    /**
     * Private function that returns the position of a tile
     * on the board from its index in placed tiles.
     *
     * @param index index of the tile
     * @return the position of the tile
     */
    private Pos positionFromIndex(int index) {
        return new Pos(index % (2 * REACH + 1) - REACH,
                index / (2 * REACH + 1) - REACH);
    }
}
