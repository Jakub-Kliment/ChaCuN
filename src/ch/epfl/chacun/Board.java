package ch.epfl.chacun;

import java.util.*;

public class Board {
    private PlacedTile[] placedTiles;
    private int[] index;
    private ZonePartitions zonePartitions;
    private Set<Animal> cancelledAnimals;

    // demander -> constante 625 !!!!!!!!
    private static final int BOARD_SIZE = 625;
    public static final int REACH = 12;
    public static final Board EMPTY = new Board(
            new PlacedTile[BOARD_SIZE],
            new int[0],
            new ZonePartitions.Builder(ZonePartitions.EMPTY).build(),
            new HashSet<>());

    private Board(PlacedTile[] placedTiles, int[] index, ZonePartitions zonePartitions, Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.index = index;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    public PlacedTile tileAt(Pos pos) {
        int position = indexFromPosition(pos);

        if (position < 0 || position > BOARD_SIZE || placedTiles[position] == null)
            return null;
        return placedTiles[position];
    }

    public PlacedTile tileWithId(int tileId) {
        for (int i : index) {
            if (placedTiles[i].id() == tileId)
                return placedTiles[i];
        }
        throw new IllegalArgumentException();
    }

    public Set<Animal> cancelledAnimals() {
        return Set.copyOf(cancelledAnimals);
    }

    public Set<Occupant> occupants() {
        Set<Occupant> occupants = new HashSet<>();
        for (int i : index)
            occupants.add(placedTiles[i].occupant());

        return occupants;
    }

    // demander si on peut lancer une exception dans une autre methode
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        // Area<Zone.Forest> newArea = zonePartitions.forests().areaContaining(forest);
        // return newArea;
        return zonePartitions.forests().areaContaining(forest);
    }

    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        return zonePartitions.meadows().areaContaining(meadow);
    }

    public Area<Zone.River> riverArea(Zone.River riverZone) {
        return zonePartitions.rivers().areaContaining(riverZone);
    }

    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return zonePartitions.riverSystems().areaContaining(water);
    }

    // verifier !!!!!!
    public Set<Area<Zone.Meadow>> meadowAreas() {
        ZonePartition<Zone.Meadow> meadowAreas = new ZonePartition.Builder<>(zonePartitions.meadows()).build();
        return meadowAreas.areas();
    }

    // verifier !!!!!!
    public Set<Area<Zone.Water>> riverSystemAreas() {
        ZonePartition<Zone.Water> riverSystemAreas = new ZonePartition.Builder<>(zonePartitions.riverSystems()).build();
        return riverSystemAreas.areas();
    }

    // peut etre fait avec les directions, mais il faut ajouter les diagonales !!!!!
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
        Set<Zone.Meadow> adjacentMeadow = new HashSet<>();
        Area<Zone.Meadow> meadowArea = meadowArea(meadowZone);

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Pos adjacentPos = pos.translated(i, j);

                if (tileAt(adjacentPos) != null) {
                    for (Zone.Meadow zone : tileAt(adjacentPos).meadowZones()) {
                        if (meadowArea.zones().contains(zone))
                            adjacentMeadow.add(zone);
                    }
                }
            }
        }
        return new Area<>(adjacentMeadow, meadowArea.occupants(), 0);
    }

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

    public PlacedTile lastPlacedTile() {
        if (this.equals(EMPTY))
            return null;

        return placedTiles[index[index.length - 1]];
    }

    // verifier !!!!!!!
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        if (this.equals(EMPTY))
            return new HashSet<>();

        Set<Area<Zone.Forest>> forestAreas = new HashSet<>();
        for (Zone.Forest forestZone : placedTiles[index[index.length - 1]].forestZones()) {
            if (forestArea(forestZone).isClosed())
                forestAreas.add(forestArea(forestZone));
        }
        return forestAreas;
    }

    public Set<Area<Zone.River>> riversClosedByLastTile() {
        if (this.equals(EMPTY))
            return new HashSet<>();

        Set<Area<Zone.River>> riverAreas = new HashSet<>();
        for (Zone.River riverZone : placedTiles[index[index.length - 1]].riverZones()) {
            if (riverArea(riverZone).isClosed())
                riverAreas.add(riverArea(riverZone));
        }
        return riverAreas;
    }

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

    // faire une map<direction, bool>
    // refaire
    public boolean couldPlaceTile(Tile tile) {
        boolean couldPlaceTile = false;

        for (Pos position : insertionPositions()) {
            for (Direction direction : Direction.values()) {
                if (tileAt(position.neighbor(direction)) != null) {
                    for (TileSide side : tile.sides()) {
                        if (side.isSameKindAs(tileAt(position.neighbor(direction)).side(direction.opposite()))) {
                            couldPlaceTile = true;
                            break;
                        } else
                            couldPlaceTile = false;
                    }
                }
            }
            if (couldPlaceTile)
                return couldPlaceTile;
        }
        return couldPlaceTile;
    }

    public Board withNewTile(PlacedTile tile) {
        if (this == EMPTY || !canAddTile(tile))
            throw new IllegalArgumentException();

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        newPlacedTiles[indexFromPosition(tile.pos())] = tile;

        int[] newIndex = Arrays.copyOf(index, index.length + 1);
        newIndex[newIndex.length - 1] = tile.id();

        ZonePartitions.Builder newZonePartitionsBuilder = new ZonePartitions.Builder(zonePartitions);
        newZonePartitionsBuilder.addTile(tile.tile());
        ZonePartitions newZonePartitions = newZonePartitionsBuilder.build();

        return new Board(newPlacedTiles, newIndex, newZonePartitions, cancelledAnimals());
    }

    // autre methode lance exception !!!!!!
    public Board withOccupant(Occupant occupant) {
        for (int i : index) {
            if (placedTiles[i].id() == occupant.zoneId() / 10)
                placedTiles[i].withOccupant(occupant);
        }

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        int[] newIndex = index.clone();
        // demander si le occupant doit etre ajoute a zonePartitions !!!!!
        ZonePartitions newZonePartitions = new ZonePartitions.Builder(zonePartitions).build();

        return new Board(newPlacedTiles, newIndex, newZonePartitions, cancelledAnimals());
    }

    public Board withoutOccupant(Occupant occupant) {
        for (int i : index) {
            if (placedTiles[i].id() == occupant.zoneId() / 10)
                placedTiles[i].withNoOccupant();
        }

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        int[] newIndex = index.clone();
        // demander si le occupant doit etre enleve de zonePartitions !!!!!
        ZonePartitions newZonePartitions = new ZonePartitions.Builder(zonePartitions).build();

        return new Board(newPlacedTiles, newIndex, newZonePartitions, cancelledAnimals());
    }

    // erreur dans l'enonce !!!!!! (zone)
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

    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        Set<Animal> newCancelledAnimals = cancelledAnimals();
        newCancelledAnimals.addAll(newlyCancelledAnimals);

        PlacedTile[] newPlacedTiles = placedTiles.clone();
        int[] newIndex = index.clone();
        ZonePartitions newZonePartitions = new ZonePartitions.Builder(zonePartitions).build();

        return new Board(newPlacedTiles, newIndex, newZonePartitions, newCancelledAnimals);
    }

    // Demander si Object ou Board en parametre !!!!!! et si elle doit etre statique
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

    @Override
    public int hashCode() {
        return Objects.hash(
                Arrays.hashCode(placedTiles),
                Arrays.hashCode(index),
                zonePartitions,
                cancelledAnimals);
    }

    private int indexFromPosition(Pos pos) {
        return (pos.y() + REACH) * (2 * REACH + 1) + (pos.x() + REACH);
    }

    private Pos positionFromIndex(int index) {
        return new Pos(index % (2 * REACH + 1) - REACH, index / (2 * REACH + 1) - REACH);
    }

}
