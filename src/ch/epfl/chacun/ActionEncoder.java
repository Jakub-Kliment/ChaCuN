package ch.epfl.chacun;

import java.util.Comparator;
import java.util.List;

/**
 * Encodes and decodes actions in a game.
 */
public final class ActionEncoder {

    /**
     * The representation of a null occupant.
     */
    private final static int NULL_OCCUPANT = 0b11111;

    /**
     * The mask to extract the rotation from the action.
     */
    private final static int ROTATION_MASK = 0b11;

    /**
     * The mask to extract the zone from the action.
     */
    private final static int ZONE_MASK = 0b1111;

    /**
     * The shift to extract the position from the action.
     */
    private final static int POSITION_SHIFT = 2;

    /**
     * The shift to extract the occupant kind from the action.
     */
    private final static int OCCUPANT_KIND_SHIFT = 4;

    /**
     * Private constructor to prevent instantiation.
     */
    private ActionEncoder() {}

    /**
     * Encodes the action of placing a tile. The action is encoded as a 10-bit
     * string, with the 2 least significant bits representing the rotation of the
     * tile and the 8 most significant bits representing the position of the tile.
     *
     * @param state the game state to add the tile to
     * @param tile the tile to place
     * @return the new game state and the encoded action
     */
    public static StateAction withPlacedTile(GameState state, PlacedTile tile) {
        int rot = tile.rotation().ordinal();
        int pos = sortedPositions(state).indexOf(tile.pos());
        String action = Base32.encodeBits10(pos << POSITION_SHIFT | rot);

        return new StateAction(state.withPlacedTile(tile), action);
    }

    /**
     * Encodes the action of placing an occupant on a tile. The action is encoded as
     * a 5-bit string, with the 4 least significant bits representing the zone of
     * the tile and the 1 most significant bit representing the kind of the occupant
     * (0 for a pawn and 1 for a hut).
     *
     * @param state the game state to add the occupant to
     * @param occupant the occupant to place
     * @return the new game state and the encoded action
     */
    public static StateAction withNewOccupant(GameState state, Occupant occupant) {
        if (occupant == null)
            return new StateAction(state.withNewOccupant(null), Base32.encodeBits5(NULL_OCCUPANT));

        int zoneId = Zone.localId(occupant.zoneId());
        int kind = occupant.kind().ordinal();
        String action = Base32.encodeBits5(kind << OCCUPANT_KIND_SHIFT | zoneId);

        return new StateAction(state.withNewOccupant(occupant), action);
    }

    /**
     * Encodes the action of removing a pawn from a tile. The action is encoded
     * as a 5-bit string, with all 5 least significant bits representing the index of
     * the pawn to remove. The function takes all pawns on the board and sorts them
     * by their zone ID before encoding the action (even pawns from other players).
     *
     * @param state the game state to remove the occupant from
     * @param occupant the occupant to remove
     * @return the new game state and the encoded action
     */
    public static StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        if (occupant == null)
            return new StateAction(state.withOccupantRemoved(null), Base32.encodeBits5(NULL_OCCUPANT));

        String action = Base32.encodeBits5(sortedPawns(state).indexOf(occupant));
        return new StateAction(state.withOccupantRemoved(occupant), action);
    }

    /**
     * Decodes and applies an action to a game state. If the action is invalid or
     * cannot be decoded, the function returns null by catching any exception
     * thrown during the decoding process, since there are a lot of possible
     * exceptions that can be thrown during the decoding process in the game state.
     *
     * @param state the game state to apply the action to
     * @param action the action to decode and apply
     * @return the new game state and the decoded action, or null if the action is invalid
     */
    public static StateAction decodeAndApply(GameState state, String action) {
        try {
            return decode(state, action);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * Decodes an action and applies it to a game state. The action is decoded
     * based on the next action of the game state, and the corresponding function
     * is called to apply the action.
     *
     * @param state the game state to apply the action to
     * @param action the action to decode and apply
     * @return the new game state and the decoded action
     */
    private static StateAction decode(GameState state, String action) {
        int actionRepresentation = Base32.decode(action);

        // Decoding the action based on the next action of the game state
        switch (state.nextAction()) {
            case PLACE_TILE -> {
                int rotation = actionRepresentation & ROTATION_MASK;
                int position = actionRepresentation >> POSITION_SHIFT;

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

                int zone = actionRepresentation & ZONE_MASK;
                int kind = actionRepresentation >> OCCUPANT_KIND_SHIFT;

                for (Occupant occupant : state.lastTilePotentialOccupants())
                    if (Zone.localId(occupant.zoneId()) == zone
                            && occupant.kind().ordinal() == kind)
                        return withNewOccupant(state, occupant);

                return null;
            }
            case RETAKE_PAWN -> {
                if (actionRepresentation == NULL_OCCUPANT)
                    return withOccupantRemoved(state, null);

                List<Occupant> sortedPawns = sortedPawns(state)
                        .stream()
                        .filter(occupant -> state
                                .board()
                                .tileWithId(Zone.tileId(occupant.zoneId()))
                                .placer() == state.currentPlayer())
                        .toList();

                for (Occupant occupant : state.board().occupants())
                    if (sortedPawns.contains(occupant))
                        return withOccupantRemoved(state, sortedPawns(state).get(actionRepresentation));
                return null;
            }
        }
        return null;
    }

    /**
     * Returns a list of all sorted insertion positions on the
     * board of a game state where the player could place his next tile.
     * Positions are sorted by their x-coordinate first, then by their y-coordinate.
     *
     * @param state the game state to get the positions from
     * @return the sorted list of positions
     */
    private static List<Pos> sortedPositions(GameState state) {
        return state
                .board()
                .insertionPositions()
                .stream()
                .sorted(Comparator.comparingInt(Pos::x).thenComparingInt(Pos::y))
                .toList();
    }

    /**
     * Returns a list of all pawns on the board of a game state.
     * The function returns all pawns ordered by their zone ID in ascending order.
     *
     * @param state the game state to get the pawns from
     * @return the sorted list of pawns
     */
    private static List<Occupant> sortedPawns(GameState state) {
        return state
                .board()
                .occupants()
                .stream()
                .filter(occupant -> occupant.kind() == Occupant.Kind.PAWN)
                .sorted(Comparator.comparingInt(Occupant::zoneId))
                .toList();
    }

    /**
     * Record that represents a game state and an action together as a single object
     * to simplify the return of the functions that encode and decode actions.
     */
    public record StateAction(GameState state, String action) {}
}
