package ch.epfl.chacun;

import java.util.*;

/**
 * Represents the board of the actual game
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public class Board {
    private PlacedTile[] placedTiles;
    private int[] index;
    private ZonePartitions zonePartitions;
    private Set<Animal> cancelledAnimals;

    // demander -> constante 625 !!!!!!!!
    // The number of possible tile placements (board of size 25 x 25)
    private static final int BOARD_SIZE = 625;
    public static final int REACH = 12;

    // Empty board
    public static final Board EMPTY = new Board(
            new PlacedTile[BOARD_SIZE],
            new int[0],
            new ZonePartitions.Builder(ZonePartitions.EMPTY).build(),
            new HashSet<>());

    // Private board constructor to keep the class immutable
    private Board(PlacedTile[] placedTiles, int[] index, ZonePartitions zonePartitions, Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.index = index;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    /**
     * Gets the placed tile at the given position
     *
     * @param pos position of the placed tile
     * @return placed tile at the given position, or null if there is no tile
     */
    public PlacedTile tileAt(Pos pos) {
        int position = indexFromPosition(pos);
        if (position < 0 || position > BOARD_SIZE - 1 || placedTiles[position] == null)
            return null;

        return placedTiles[position];
    }

    /**
     * Gets the placed tile by its id
     *
     * @param tileId id of the placed tile
     * @throws IllegalArgumentException if the id of the tile is not on the board
     * @return placed tile to which corresponds the id
     */
    public PlacedTile tileWithId(int tileId) {
        for (int i : index)
            if (placedTiles[i].id() == tileId)
                return placedTiles[i];

        throw new IllegalArgumentException();
    }

    /**
     * Returns a copy of cancelled animals to keep the class immutable
     *
     * @return copy of the set of cancelled animals
     */
    public Set<Animal> cancelledAnimals() {
        return Set.copyOf(cancelledAnimals);
    }

    /**
     * Returns the set of all occupants on the board
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

    // demander si on peut lancer une exception dans une autre methode !!!!!
    /**
     * Returns the area of the forest containing the given forest zone
     *
     * @param forest forest zone to find the area of
     * @return the area of the forest containing the given forest zone
     */
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        // Area<Zone.Forest> newArea = zonePartitions.forests().areaContaining(forest);
        // return newArea;
        return zonePartitions.forests().areaContaining(forest);
    }

    /**
     * Returns the area of the meadow containing the given meadow zone
     *
     * @param meadow meadow zone to find the area of
     * @return the area of the meadow containing the given meadow zone
     */
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        return zonePartitions.meadows().areaContaining(meadow);
    }

    /**
     * Returns the area of the river containing the given river zone
     *
     * @param riverZone river zone to find the area of
     * @return the area of the river containing the given river zone
     */
    public Area<Zone.River> riverArea(Zone.River riverZone) {
        return zonePartitions.rivers().areaContaining(riverZone);
    }

    /**
     * Returns the area of the water system containing the given water zone
     *
     * @param water water zone to find the area of
     * @return the area of the water system containing the given water zone
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return zonePartitions.riverSystems().areaContaining(water);
    }

    // verifier !!!!!!
    /**
     * Returns the set of all meadow areas on the board
     *
     * @return the set of all meadow areas on the board
     */
    public Set<Area<Zone.Meadow>> meadowAreas() {
        ZonePartition<Zone.Meadow> meadowAreas = new ZonePartition.Builder<>(zonePartitions.meadows()).build();
        return meadowAreas.areas();
    }

    // verifier !!!!!!
    /**
     * Returns the set of all river system areas on the board
     *
     * @return the set of all river system areas on the board
     */
    public Set<Area<Zone.Water>> riverSystemAreas() {
        ZonePartition<Zone.Water> riverSystemAreas = new ZonePartition.Builder<>(zonePartitions.riverSystems()).build();
        return riverSystemAreas.areas();
    }

    // peut etre faite avec les directions, mais il faut ajouter les diagonales !!!!!
    /**
     * Returns the area of the meadow and all its adjacent meadows that are in the same area
     *
     * @param pos position to find the meadow area of
     * @return the area of the meadow and its adjacent zones containing the given position
     */
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
        Set<Zone.Meadow> adjacentMeadow = new HashSet<>();
        Area<Zone.Meadow> meadowArea = meadowArea(meadowZone);

        // Loops through the 8 adjacent positions
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Pos adjacentPos = pos.translated(i, j);

                if (tileAt(adjacentPos) != null)
                    for (Zone.Meadow zone : tileAt(adjacentPos).meadowZones())
                        if (meadowArea.zones().contains(zone))
                            adjacentMeadow.add(zone);
            }
        }
        return new Area<>(adjacentMeadow, meadowArea.occupants(), 0);
    }

    /**
     * Returns the number of occupants of a given kind and player on the board
     *
     * @param player the player to count the occupants of
     * @param occupantKind the kind of the occupant to count
     * @return the number of occupants of the given kind and player on the board
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        int occupantCount = 0;
        for (int i : index) {
            Occupant occupant = placedTiles[i].occupant();
            PlayerColor placer = placedTiles[i].placer();

            if (occupant != null && placer != null && occupant.kind().equals(occupantKind) && placer.equals(player))
                occupantCount++;
        }
        return occupantCount;
    }

    /**
     * Returns the set of all insertion positions on the board (where a tile can potentially be placed)
     *
     * @return the set of all insertion positions on the board
     */
    public Set<Pos> insertionPositions() {
        Set<Pos> insertionPositions = new HashSet<>();
        for (int i : index) {
            Pos pos = positionFromIndex(i);
            insertionPositions.add(pos);

            for (Direction direction : Direction.ALL)
                insertionPositions.add(pos.neighbor(direction));
        }
        for (int i : index)
            insertionPositions.remove(positionFromIndex(i));

        return insertionPositions;
    }

    /**
     * Returns the last placed tile on the board
     *
     * @return the last placed tile on the board, or null if the board is empty
     */
    public PlacedTile lastPlacedTile() {
        if (this.equals(EMPTY))
            return null;

        return placedTiles[index[index.length - 1]];
    }

    // verifier !!!!!!!
    /**
     * Returns the set of all forest areas closed by the last placed tile
     *
     * @return the set of all forest areas closed by the last placed tile
     */
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        if (this.equals(EMPTY))
            return new HashSet<>();

        Set<Area<Zone.Forest>> forestAreas = new HashSet<>();
        for (Zone.Forest forestZone : placedTiles[index[index.length - 1]].forestZones())
            if (forestArea(forestZone).isClosed())
                forestAreas.add(forestArea(forestZone));

        return forestAreas;
    }

    /**
     * Returns the set of all river areas closed by the last placed tile
     *
     * @return the set of all river areas closed by the last placed tile
     */
    public Set<Area<Zone.River>> riversClosedByLastTile() {
        if (this.equals(EMPTY))
            return new HashSet<>();

        Set<Area<Zone.River>> riverAreas = new HashSet<>();
        for (Zone.River riverZone : placedTiles[index[index.length - 1]].riverZones())
            if (riverArea(riverZone).isClosed())
                riverAreas.add(riverArea(riverZone));

        return riverAreas;
    }

    /**
     * Checks if a tile can be added to the board at a certain position
     *
     * @param tile the tile to be placed
     * @return true if the tile can be added, false otherwise
     */
    public boolean canAddTile(PlacedTile tile) {
        boolean canAddTile = false;

        if (insertionPositions().contains(tile.pos())) {
            // Loops through the directions of the neighboring tiles
            for (Direction direction : Direction.values()) {
                if (tileAt(tile.pos().neighbor(direction)) != null) {
                    // Verifies if the tile can be added by checking if the sides of neighboring tiles are the same
                    canAddTile = tile.side(direction).isSameKindAs(
                            tileAt(tile.pos().neighbor(direction)).side(direction.opposite()));

                    // If there is one side that is not the same, the tile cannot be added
                    if (!canAddTile)
                        return canAddTile;
                }
            }
        }
        return canAddTile;
    }

    /**
     * Checks if a tile can be placed on the board
     *
     * @param tile the tile to be placed
     * @return true if the tile can be placed, false otherwise
     */
    public boolean couldPlaceTile(Tile tile) {
        for (Pos pos : insertionPositions()) {
            // Looks for all possible rotation in an insertion position
            for (Rotation rot : Rotation.values()) {
                PlacedTile possibleTile = new PlacedTile(tile, null, rot, pos, null);
                if (canAddTile(possibleTile))
                    return true;
            }
        }
        return false;
    }

    // est-ce que throws doit etre dans les commentaires !!!!!!
    /**
     * Adds a tile to the board and returns the new board with the tile placed
     *
     * @param tile the tile to be placed
     * @throws IllegalArgumentException if the tile cannot be placed
     * @return the new board with the tile placed
     */
    public Board withNewTile(PlacedTile tile) {
        if (!this.equals(EMPTY) && !canAddTile(tile))
            throw new IllegalArgumentException();

        // Defensive copy of placedTiles with the new tile added
        PlacedTile[] newPlacedTiles = placedTiles.clone();
        newPlacedTiles[indexFromPosition(tile.pos())] = tile;

        // Defensive copy of index with the new index added
        int[] newIndex = Arrays.copyOf(index, index.length + 1);
        newIndex[newIndex.length - 1] = indexFromPosition(tile.pos());

        // Defensive copy of zonePartitions with the new tile and its partitions added
        ZonePartitions.Builder newZonePartitionsBuilder = new ZonePartitions.Builder(zonePartitions);
        newZonePartitionsBuilder.addTile(tile.tile());
        ZonePartitions newZonePartitions = newZonePartitionsBuilder.build();

        return new Board(newPlacedTiles, newIndex, newZonePartitions, cancelledAnimals());
    }

    // autre methode lance exception !!!!!!
    /**
     * Adds an occupant to the board and returns the new board with the occupant placed
     *
     * @param occupant the occupant to be placed
     * @throws IllegalArgumentException if the occupant cannot be placed
     * @return the new board with the occupant placed
     */
    public Board withOccupant(Occupant occupant) {
        for (int i : index)
            if (placedTiles[i].id() == occupant.zoneId() / 10)
                placedTiles[i].withOccupant(occupant);

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        int[] newIndex = index.clone();
        // demander si l'occupant doit etre ajouter a zonePartitions !!!!!
        ZonePartitions newZonePartitions = new ZonePartitions.Builder(zonePartitions).build();

        return new Board(newPlacedTiles, newIndex, newZonePartitions, cancelledAnimals());
    }

    /**
     * Removes an occupant from the board and returns the new board without the occupant
     *
     * @param occupant the occupant to be removed
     * @return the new board without the occupant
     */
    public Board withoutOccupant(Occupant occupant) {
        for (int i : index)
            if (placedTiles[i].id() == occupant.zoneId() / 10)
                placedTiles[i].withNoOccupant();

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        int[] newIndex = index.clone();
        // demander si le occupant doit etre enleve de zonePartitions !!!!!
        ZonePartitions newZonePartitions = new ZonePartitions.Builder(zonePartitions).build();

        return new Board(newPlacedTiles, newIndex, newZonePartitions, cancelledAnimals());
    }

    // erreur dans l'enonce !!!!!! (zone)
    /**
     * Returns a new board with all gatherers or fishers removed from the given areas
     *
     * @param forests the set of all forest areas to remove gatherers from
     * @param rivers the set of all river areas to remove fishers from
     * @return the set of all gatherers on the board
     */
    public Board withoutGatherersOrFishersIn(Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers) {
        ZonePartitions.Builder newZonePartitions = new ZonePartitions.Builder(zonePartitions);
        for (Area<Zone.Forest> forestArea : forests)
            newZonePartitions.clearGatherers(forestArea);

        for (Area<Zone.River> riverArea : rivers)
            newZonePartitions.clearFishers(riverArea);

        // demander si on doit enlever les pions de PlacedTile aussi !!!!!
        PlacedTile[] newPlacedTiles = placedTiles.clone();
        int[] newIndex = index.clone();

        return new Board(newPlacedTiles, newIndex, newZonePartitions.build(), cancelledAnimals());
    }

    /**
     * Returns a new board with more animals cancelled
     *
     * @param newlyCancelledAnimals the set of animals to add to the cancelled animals
     * @return the new board with more animals cancelled
     */
    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        Set<Animal> newCancelledAnimals = new HashSet<>();
        newCancelledAnimals.addAll(cancelledAnimals());
        newCancelledAnimals.addAll(newlyCancelledAnimals);
        // est-ce quon doit ajouter les nouveaux animaux ou les remplacer !!!!!

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        int[] newIndex = index.clone();
        ZonePartitions newZonePartitions = new ZonePartitions.Builder(zonePartitions).build();

        return new Board(newPlacedTiles, newIndex, newZonePartitions, newCancelledAnimals);
    }

    // Demander si Object ou Board en parametre !!!!!! et si elle doit etre statique !!!!!!
    /**
     * Returns true if the board is equal to the given object
     *
     * @param that the object to compare the board to
     * @return true if the board is equal to the given object, false otherwise
     */
    @Override
    public boolean equals(Object that) {
        if (that instanceof Board board)
            return Arrays.equals(placedTiles, board.placedTiles) &&
                    Arrays.equals(index, board.index) &&
                    // Normalement pas de probleme (car immuables), mais demander si on peut comparer !!!!!
                    zonePartitions.equals(board.zonePartitions) &&
                    cancelledAnimals().equals(board.cancelledAnimals);

        return false;
    }

    /**
     * Returns the hash code of the board
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
     * Returns the index of a tile based on its position
     *
     * @param pos position of the tile
     * @return the index of the tile
     */
    private int indexFromPosition(Pos pos) {
        return (pos.y() + REACH) * (2 * REACH + 1) + (pos.x() + REACH);
    }

    /**
     * Returns the position of a tile based on its index
     *
     * @param index index of the tile
     * @return the position of the tile
     */
    private Pos positionFromIndex(int index) {
        return new Pos(index % (2 * REACH + 1) - REACH, index / (2 * REACH + 1) - REACH);
    }
}
