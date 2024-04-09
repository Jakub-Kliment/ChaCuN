package ch.epfl.chacun;

import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a placed tile in the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 *
 * @param tile tile to be placed (not null)
 * @param placer player who places the tile
 * @param rotation rotation of the tile (not null)
 * @param pos position where the tile will be placed (not null)
 * @param occupant occupant of the tile
 */

public record PlacedTile(Tile tile,
                         PlayerColor placer,
                         Rotation rotation,
                         Pos pos,
                         Occupant occupant) {

    /**
     * Immutable constructor
     *
     * @throws NullPointerException if the tile, rotation or position is null
     */
    public PlacedTile {
        Objects.requireNonNull(tile);
        Objects.requireNonNull(rotation);
        Objects.requireNonNull(pos);
    }

    /**
     * Constructor that does not take an occupant
      */
    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }

    /**
     * Returns the id of the tile.
     *
     * @return the id of the tile
     */
    public int id() {
        return tile.id();
    }


    /**
     * Returns the kind of the tile.
     *
     * @return the kind of the tile
     */
    public Tile.Kind kind() {
        return tile.kind();
    }

    /**
     * Returns the side of the tile.
     *
     * @param direction the direction of the side
     * @return the side of the tile
     */
    public TileSide side(Direction direction) {
        return tile.sides().get(direction.rotated(rotation.negated()).ordinal());
    }

    /**
     * Returns a zone of the tile by its id.
     *
     * @param id the id of the zone
     * @throws IllegalArgumentException if the zone is not on the tile
     * @return the zone of the tile
     */
    public Zone zoneWithId(int id) {
        for (Zone zone : tile.zones())
            if (zone.id() == id)
                return zone;

        throw new IllegalArgumentException();
    }

    /**
     * Returns the zone with a special power if there is one.
     *
     * @return the zone of the tile with a special power
     */
    public Zone specialPowerZone() {
        for (Zone zone : tile.zones())
            if (zone.specialPower() != null)
                return zone;

        return null;
    }

    /**
     * Returns a set of forest zones of the tile.
     *
     * @return set of forest zones of the tile
     */
    public Set<Zone.Forest> forestZones() {
        Set<Zone.Forest> forests = new HashSet<>();
        for (Zone zone : tile.zones())
            if (zone instanceof Zone.Forest forest)
                forests.add(forest);

        return forests;
    }

    /**
     * Returns a set of meadow zones of the tile.
     *
     * @return set of meadow zones of the tile
     */
    public Set<Zone.Meadow> meadowZones() {
        Set<Zone.Meadow> meadows = new HashSet<>();
        for (Zone zone : tile.zones())
            if (zone instanceof Zone.Meadow meadow)
                meadows.add(meadow);

        return meadows;
    }

    /**
     * Returns a set of river zones of the tile.
     *
     * @return set of river zones of the tile
     */
    public Set<Zone.River> riverZones() {
        Set<Zone.River> rivers = new HashSet<>();
        for (Zone zone : tile.zones())
            if (zone instanceof Zone.River river)
                rivers.add(river);

        return rivers;
    }

    /**
     * Returns a set of all potential occupants of the tile.
     *
     * @return set of potential occupants of the tile
     */
    public Set<Occupant> potentialOccupants() {
        Set<Occupant> occupants = new HashSet<>();
        if (placer == null)
            return occupants;

        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Lake)
                occupants.add(new Occupant(Occupant.Kind.HUT, zone.id()));
            else if (zone instanceof Zone.River river && !river.hasLake()) {
                occupants.add(new Occupant(Occupant.Kind.HUT, zone.id()));
                occupants.add(new Occupant(Occupant.Kind.PAWN, zone.id()));
            } else
                occupants.add(new Occupant(Occupant.Kind.PAWN, zone.id()));
        }
        return occupants;
    }

    /**
     * Returns a place tile with a given occupant.
     *
     * @throws IllegalArgumentException if this placed tile is already occupied
     * @return the placed tile with the given occupant
     */
    public PlacedTile withOccupant(Occupant occupant) {
        Preconditions.checkArgument(this.occupant == null);
        return new PlacedTile(tile, placer, rotation, pos, occupant);
    }

    /**
     * Returns a placed tile with no occupant.
     *
     * @return the placed tile with no occupant
     */
    public PlacedTile withNoOccupant() {
        return new PlacedTile(tile, placer, rotation, pos);
    }

    /**
     * Returns the id of the zone occupied by a given occupant kind.
     *
     * @param occupantKind the kind of the occupant
     * @return the id of the zone occupied by the given occupant kind
     *         or -1 if the zone is not occupied by the given kind
     */
    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        if (occupant != null && occupant.kind() == occupantKind)
            return occupant.zoneId();

        return -1;
    }
}
