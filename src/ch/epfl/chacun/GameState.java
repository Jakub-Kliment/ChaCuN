package ch.epfl.chacun;

import ch.epfl.chacun.tile.Tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record GameState(List<PlayerColor> players,
                        TileDecks tileDecks,
                        Tile tileToPlace,
                        Board board,
                        Action nextAction,
                        MessageBoard messageBoard) {

    public GameState{
        //Peut-être être truc à faire pour être immuable
        Preconditions.checkArgument(players.size() >= 2);
        players = List.copyOf(players);
        Preconditions.checkArgument((tileToPlace == null) ^ (nextAction == Action.PLACE_TILE));
        Preconditions.checkArgument((tileDecks != null) && (board != null) && (nextAction != null) && (messageBoard != null));


    }

    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker){
        return new GameState(players, tileDecks, null, Board.EMPTY, Action.PLACE_TILE, new MessageBoard(textMaker, new ArrayList<>()));
    }

    public PlayerColor currentPlayer(){
        if (nextAction == Action.START_GAME || nextAction == Action.END_GAME){
            return null;
        }
        return players.getFirst();
    }

    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind){
        return Occupant.occupantsCount(kind) - board.occupantCount(player, kind);
    }

    public Set<Occupant> lastTilePotentialOccupants(){
        Preconditions.checkArgument(board == Board.EMPTY);
        return board.lastPlacedTile().potentialOccupants();
    }

    public GameState withStartingTilePlaced(){
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE);
        Board nextBoard = board.withNewTile(new PlacedTile(tileDecks.topTile(Tile.Kind.START), null, Rotation.NONE, new Pos(0,0)));
        Tile nextTile = tileDecks.topTile(Tile.Kind.NORMAL);
        TileDecks nextDeck = tileDecks.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL);
        return new GameState(players, nextDeck, nextTile, nextBoard, nextAction, messageBoard);
    }

    public GameState withPlacedTile(PlacedTile tile){
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE && tile.occupant() != null);
        Board nextBoard = board.withNewTile(tile);

    }

    public enum Action {
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
    }


}
