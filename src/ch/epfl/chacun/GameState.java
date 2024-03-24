package ch.epfl.chacun;

import java.util.*;

public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace,
                        Board board, Action nextAction, MessageBoard messageBoard) {

    public GameState {
        //Peut-être être truc à faire pour être immuable
        Preconditions.checkArgument(players.size() >= 2);
        players = List.copyOf(players);
        // Demander si cest un xor ou or !!!!!!
        Preconditions.checkArgument((tileToPlace == null) ^ nextAction.equals(Action.PLACE_TILE));
        Objects.requireNonNull(tileDecks);
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextAction);
        Objects.requireNonNull(messageBoard);
    }

    public enum Action {
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
    }

    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker) {
        return new GameState(players, tileDecks, null, Board.EMPTY,
                Action.START_GAME, new MessageBoard(textMaker, new ArrayList<>()));
    }

    public PlayerColor currentPlayer() {
        if (nextAction.equals(Action.START_GAME) || nextAction.equals(Action.END_GAME))
            return null;
        return players.getFirst();
    }

    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind) {
        return Occupant.occupantsCount(kind) - board.occupantCount(player, kind);
    }

    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(board.equals(Board.EMPTY));
        return board.lastPlacedTile().potentialOccupants();
    }

    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction.equals(Action.START_GAME));
        Board newBoard = board.withNewTile(new PlacedTile(tileDecks.topTile(Tile.Kind.START),
                null, Rotation.NONE, Pos.ORIGIN));
        Tile nextTile = tileDecks.topTile(Tile.Kind.NORMAL);
        TileDecks nextDecks = tileDecks.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL);
        return new GameState(players, nextDecks, nextTile, newBoard, Action.PLACE_TILE, messageBoard);
    }

    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction.equals(Action.PLACE_TILE) && tile.occupant() != null);
        Board newBoard = board.withNewTile(tile);
        MessageBoard newMessageBoard = new MessageBoard(messageBoard.textMaker(), messageBoard.messages());
        boolean hasShaman = false;
        int points;

        if (tile.specialPowerZone() != null) {
            if (tile.specialPowerZone() instanceof Zone.Meadow meadow
                    && tile.specialPowerZone().specialPower().equals(Zone.SpecialPower.HUNTING_TRAP)) {
                Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(tile.pos(), meadow);
                Set<Animal> animals = Area.animals(adjacentMeadow, new HashSet<>());
                Map<Animal.Kind, Integer> animalCount = new HashMap<>();

                for (Animal.Kind animalKind : Animal.Kind.values())
                    animalCount.put(animalKind, 0);

                for (Animal animal : animals)
                    animalCount.put(animal.kind(), animalCount.get(animal.kind()) + 1);

                int deerPoints = animalCount.get(Animal.Kind.TIGER) >= animalCount.get(Animal.Kind.DEER) ?
                        0 : animalCount.get(Animal.Kind.DEER) - animalCount.get(Animal.Kind.TIGER);
                points = Points.forMeadow(animalCount.get(Animal.Kind.MAMMOTH),
                        animalCount.get(Animal.Kind.AUROCHS), deerPoints);

                newBoard = newBoard.withMoreCancelledAnimals(animals);
                newMessageBoard = messageBoard.withScoredHuntingTrap(players.getFirst(), adjacentMeadow);

            } else if (tile.specialPowerZone() instanceof Zone.Lake lake
                    && tile.specialPowerZone().specialPower().equals(Zone.SpecialPower.LOGBOAT)) {
                points = Area.lakeCount(newBoard.riverSystemArea(lake));
                newMessageBoard = messageBoard.withScoredLogboat(players.getFirst(), newBoard.riverSystemArea(lake));
            }

            hasShaman = tile.specialPowerZone().specialPower().equals(Zone.SpecialPower.SHAMAN);
        }
        if (hasShaman)
            return new GameState(players, tileDecks.withTopTileDrawn(tile.kind()), null,
                    newBoard, Action.RETAKE_PAWN, newMessageBoard);

        return new GameState(players, tileDecks.withTopTileDrawn(tile.kind()), null,
                newBoard, Action.OCCUPY_TILE, newMessageBoard);
    }

    public GameState withOccupantRemoved(Occupant occupant) {
        return null;
    }
}
