package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a placed tile in the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public record PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant) {

    public PlacedTile {
        Objects.requireNonNull(tile);
        Objects.requireNonNull(rotation);
        Objects.requireNonNull(pos);
    }

    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }

    public int id() {
        return tile.id();
    }

    public Tile.Kind kind() {
        return tile.kind();
    }

    public TileSide side(Direction direction) {
        return tile.sides().get(direction.rotated(rotation.negated()).ordinal());
    }

    public Zone zoneWithId(int id) {
        for (Zone zone : tile.zones()) {
            if (zone.id() == id) {
                return zone;
            }
        }
        throw new IllegalArgumentException();
    }

    public Zone specialPowerZone() {
        for (Zone zone : tile.zones()) {
            if (zone.specialPower() != null) {
                return zone;
            }
        }
        return null;
    }

    public Set<Zone.Forest> forestZones() {
        Set<Zone.Forest> forests = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Forest forest) {
                forests.add(forest);
            }
        }
        return forests;
    }

    public Set<Zone.Meadow> meadowZones() {
        Set<Zone.Meadow> meadows = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Meadow meadow) {
                meadows.add(meadow);
            }
        }
        return meadows;
    }

    public Set<Zone.River> riverZones() {
        Set<Zone.River> rivers = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.River river) {
                rivers.add(river);
            }
        }
        return rivers;
    }

    public Set<Occupant> potentialOccupants() {
        Set<Occupant> occupants = new HashSet<>();

        if (placer == null) {
            return occupants;
        }

        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Lake) {
                occupants.add(new Occupant(Occupant.Kind.HUT, zone.id()));
            } else if (zone instanceof Zone.River river && !river.hasLake()) {
                occupants.add(new Occupant(Occupant.Kind.HUT, zone.id()));
            } else {
                occupants.add(new Occupant(Occupant.Kind.PAWN, zone.id()));
            }
        }
        return occupants;
    }

    public PlacedTile withOccupant(Occupant occupant) {
        if (occupant() != null) {
            throw new IllegalArgumentException();
        }
        return new PlacedTile(tile, placer, rotation, pos, occupant);
    }

    public PlacedTile withNoOccupant() {
        return new PlacedTile(tile, placer, rotation, pos);
    }

    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        if (occupant != null && occupant.kind() == occupantKind){
            return occupant.zoneId();
        }
        return -1;
    }
}
