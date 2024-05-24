package ch.epfl.chacun;

import java.util.Comparator;
import java.util.List;

public class ActionEncoder {

    private final static int NULL_OCCUPANT = 0b11111;

    private ActionEncoder() {}

    public static StateAction withPlacedTile(GameState state, PlacedTile tile) {
        int rot = tile.rotation().ordinal();
        int pos = sortedPositions(state).indexOf(tile.pos());
        String action = Base32.encodeBits10(pos << 2 | rot);

        return new StateAction(state.withPlacedTile(tile), action);
    }

    public static StateAction withNewOccupant(GameState state, Occupant occupant) {
        if (occupant == null)
            return new StateAction(state.withNewOccupant(null), Base32.encodeBits5(NULL_OCCUPANT));

        int zoneId = Zone.localId(occupant.zoneId());
        int kind = occupant.kind().ordinal();
        String action = Base32.encodeBits5(kind << 4 | zoneId);

        return new StateAction(state.withNewOccupant(occupant), action);
    }

    public static StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        if (occupant == null)
            return new StateAction(state.withOccupantRemoved(null), Base32.encodeBits5(NULL_OCCUPANT));

        String action = Base32.encodeBits5(sortedPawns(state).indexOf(occupant));
        return new StateAction(state.withOccupantRemoved(occupant), action);
    }

    public static StateAction decodeAndApply(GameState state, String action) {
        try {
            return decode(state, action);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    private static StateAction decode(GameState state, String action) {
        int actionRepresentation = Base32.decode(action);
        switch (state.nextAction()) {
            case PLACE_TILE -> {
                int rotation = actionRepresentation & 0b11;
                int position = actionRepresentation >> 2;

                PlacedTile tile = new PlacedTile(
                        state.tileToPlace(),
                        state.currentPlayer(),
                        Rotation.ALL.get(rotation),
                        sortedPositions(state).get(position));

                return withPlacedTile(state, tile);
            }
            case OCCUPY_TILE -> {
                if (actionRepresentation == NULL_OCCUPANT)
                    return withNewOccupant(state, null);

                int zone = actionRepresentation & 0b1111;
                int kind = actionRepresentation >> 4;
                // peut etre trouver mieux !!!!!!!
                int zoneId = state.board().lastPlacedTile().id() * 10 + zone;
                Occupant.Kind occupantKind = kind == 0 ? Occupant.Kind.PAWN : Occupant.Kind.HUT;
                Preconditions.checkArgument(state.freeOccupantsCount(state.currentPlayer(), occupantKind) > 0);

                return withNewOccupant(state, new Occupant(occupantKind, zoneId));
            }
            case RETAKE_PAWN -> {
                if (actionRepresentation == NULL_OCCUPANT)
                    return withOccupantRemoved(state, null);
                Preconditions.checkArgument(state.board().tileWithId(Zone.tileId(sortedPawns(state).get(actionRepresentation).zoneId())).placer() == state.currentPlayer());
                return withOccupantRemoved(state, sortedPawns(state).get(actionRepresentation));
            }
        }
        return null;
    }

    private static List<Pos> sortedPositions(GameState state) {
        return state
                .board()
                .insertionPositions()
                .stream()
                .sorted(Comparator.comparingInt(Pos::x).thenComparingInt(Pos::y))
                .toList();
    }

    private static List<Occupant> sortedPawns(GameState state) {
        return state
                .board()
                .occupants()
                .stream()
                .filter(occupant -> occupant.kind() == Occupant.Kind.PAWN)
                .sorted(Comparator.comparingInt(Occupant::zoneId))
                .toList();
    }

    public record StateAction(GameState state, String action) {}
}
