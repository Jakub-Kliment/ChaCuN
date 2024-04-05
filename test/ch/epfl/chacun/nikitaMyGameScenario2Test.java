package ch.epfl.chacun;
/*
 *	Author:      Maxime Riesen
 *	Date:
 */

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


//scenario 2
public class nikitaMyGameScenario2Test {
    //List of all the tiles
    List<Tile> tileList = new ArrayList<>(ch.epfl.chacun.tile.Tiles.TILES);

    //messageBoard empty
    MessageBoard messageBoard = new MessageBoard(new BasicTextMaker(),List.of());

    private TileDecks tileDecksConstructorScenario(List<Tile> startList, List<Tile> normalList, List<Tile> menhirList){
        return new TileDecks(startList,normalList,menhirList);
    }

    public Tile getNormal79(){
        // Tile 79
        var z0 = new Zone.Meadow(79_0, List.of(), null);
        var z1 = new Zone.Forest(79_1, Zone.Forest.Kind.WITH_MUSHROOMS);
        var z2 = new Zone.Meadow(79_2, List.of(), null);
        var z3 = new Zone.River(79_3, 0, null);
        var z4 = new Zone.Meadow(79_4, List.of(), null);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.Forest(z1);
        var sS = new TileSide.River(z2, z3, z4);
        var sW = new TileSide.Forest(z1);
        return new Tile(79, Tile.Kind.NORMAL, sN, sE, sS, sW);
    }
    public Tile getNormal82(){
        var l1 = new Zone.Lake(82_8, 2, null);
        var z0 = new Zone.Meadow(82_0, List.of(), null);
        var z1 = new Zone.River(82_1, 0, l1);
        var a2_0 = new Animal(82_2_0, Animal.Kind.DEER);
        var z2 = new Zone.Meadow(82_2, List.of(a2_0), null);
        var z3 = new Zone.Forest(82_3, Zone.Forest.Kind.PLAIN);
        var sN = new TileSide.River(z0, z1, z2);
        var sE = new TileSide.Meadow(z2);
        var sS = new TileSide.Meadow(z2);
        var sW = new TileSide.Forest(z3);
        return new Tile(82, Tile.Kind.NORMAL, sN, sE, sS, sW);
    }
    public Tile getNormalWithRaft82(){
        var l1 = new Zone.Lake(82_8, 2, Zone.SpecialPower.RAFT);
        var z0 = new Zone.Meadow(82_0, List.of(), null);
        var z1 = new Zone.River(82_1, 0, l1);
        var a2_0 = new Animal(82_2_0, Animal.Kind.DEER);
        var z2 = new Zone.Meadow(82_2, List.of(a2_0), null);
        var z3 = new Zone.Forest(82_3, Zone.Forest.Kind.PLAIN);
        var sN = new TileSide.River(z0, z1, z2);
        var sE = new TileSide.Meadow(z2);
        var sS = new TileSide.Meadow(z2);
        var sW = new TileSide.Forest(z3);
        return new Tile(82, Tile.Kind.NORMAL, sN, sE, sS, sW);
    }

    public Tile getNormal87(){
        var l1 = new Zone.Lake(87_8, 2, null);
        var z0 = new Zone.Meadow(87_0, List.of(), null);
        var z1 = new Zone.Forest(87_1, Zone.Forest.Kind.WITH_MUSHROOMS);
        var z2 = new Zone.Meadow(87_2, List.of(), null);
        var z3 = new Zone.River(87_3, 0, l1);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.Forest(z1);
        var sS = new TileSide.Forest(z1);
        var sW = new TileSide.River(z2, z3, z0);
        return new  Tile(87, Tile.Kind.NORMAL, sN, sE, sS, sW);
    }
    public Tile getNormal88(){
        var z0 = new Zone.Meadow(88_0, List.of(), Zone.SpecialPower.SHAMAN);
        var z1 = new Zone.River(88_1, 1, null);
        var z2 = new Zone.Meadow(88_2, List.of(), null);
        var z3 = new Zone.Forest(88_3, Zone.Forest.Kind.PLAIN);
        var sN = new TileSide.River(z0, z1, z2);
        var sE = new TileSide.River(z2, z1, z0);
        var sS = new TileSide.Forest(z3);
        var sW = new TileSide.Meadow(z0);
        return new Tile(88, Tile.Kind.NORMAL, sN, sE, sS, sW);
    }


    TileDecks tileDecks = tileDecksConstructorScenario(
            List.of(tileList.get(56)),
            List.of(tileList.get(27),getNormal79(),tileList.get(17),getNormal88(),getNormal87(),getNormal82(), tileList.get(13)),
            List.of());

    //Gamestate at beginning
    GameState gameState  = new GameState(
            List.of(PlayerColor.GREEN, PlayerColor.BLUE),
            tileDecks,
            null,
            Board.EMPTY,
            GameState.Action.START_GAME,
            messageBoard);
    GameState gameState1 = gameState.withStartingTilePlaced();


    //startTile
    @Test
    void  withStartingTilePlacedTest1(){
        // Vérifications Tas de tuile START vide
        assertEquals(List.of(), gameState1.tileDecks().startTiles());

        // Vérifications nextAction = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, gameState1.nextAction());

        // Vérifications TileToPlace = 27
        assertEquals(tileList.get(27), gameState1.tileToPlace());

        // Vérifications du board pos(0,0)
        assertEquals(new PlacedTile(tileList.get(56), null, Rotation.NONE, new Pos(0,0), null),
                gameState1.board().tileAt(Pos.ORIGIN));

        // Vérifications du board pos(0,1)
        assertEquals(null, gameState1.board().tileAt(new Pos(0,1)));

        //Vérification Tas de tuiles normale de taille 5, donc que la tile a été retirée du deck
        assertEquals(0, gameState1.tileDecks().startTiles().size());
    }

    PlacedTile placedTile27 = new PlacedTile(tileList.get(27), PlayerColor.GREEN,Rotation.NONE,new Pos(-1,0));
    GameState gameState2 = gameState1.withPlacedTile(placedTile27);

    @Test
    void withPlacedTile1(){
        //size of the normal deck
        assertEquals(6,gameState2.tileDecks().deckSize(Tile.Kind.NORMAL));
        // check if added to the board
        assertEquals(tileList.get(27), gameState2.board().tileWithId(27).tile());
        //Vérification next Action égale est OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gameState2.nextAction());
        //MessageBoard should be empty
        assertTrue(gameState2.messageBoard().messages().isEmpty());
    }

    // nouveau occupants sur le pre 0 de tuile 27 (celui a retirer avec le shaman)
    GameState gameState3 = gameState2.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 270));
    //GameState gameState3 = gameState2.withNewOccupant(null);

    // occupant sur 27 par les verts
    @Test
    void withNewOccupant1(){
        //Vérifications Occupants du board possèdent le nouveau pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN,270)),
                gameState3.board().occupants());
        //Vérifications nextAction = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, gameState3.nextAction());
        //ordre des joueurs correct
        assertEquals(List.of(PlayerColor.BLUE,PlayerColor.GREEN), gameState3.players());
        //Vérifications Tile to place = 79
        assertEquals(getNormal79(), gameState3.tileToPlace());
    }

    PlacedTile placedTile79 = new PlacedTile(getNormal79(),PlayerColor.BLUE,Rotation.HALF_TURN,new Pos(-1,1));
    GameState gameState4 = gameState3.withPlacedTile(placedTile79);
    @Test
    //Tile 79
    void withPlacedTile2(){
        //size of the normal deck
        assertEquals(5,gameState4.tileDecks().deckSize(Tile.Kind.NORMAL));
        // check if added to the board
        assertEquals(getNormal79(), gameState4.board().tileWithId(79).tile());
        //Vérification next Action égale est OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gameState4.nextAction());
        //MessageBoard should be empty
        assertTrue(gameState4.messageBoard().messages().isEmpty());
    }
    GameState gameState5 = gameState4.withNewOccupant(new Occupant(Occupant.Kind.PAWN,793));

    //occupant sur 79 placé par bleu
    @Test
    void withNewOccupant2(){
        //nb de message dans messageBoard
        assertEquals(1,gameState5.messageBoard().messages().size());
        //Vérifications Occupants du board possèdent le nouveau pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN,270)), gameState5.board().occupants());
        //Vérifications nextAction = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, gameState5.nextAction());
        //ordre des joueurs correct
        assertEquals(List.of(PlayerColor.GREEN,PlayerColor.BLUE), gameState5.players());
        //Vérifications Tile to place = 88 (because 17 has been skipped)
        assertEquals(getNormal88(), gameState5.tileToPlace());
    }

    PlacedTile placedTile88 = new PlacedTile(getNormal88(),PlayerColor.GREEN,Rotation.LEFT,new Pos(-1,-1));
    GameState gameState6 = gameState5.withPlacedTile(placedTile88);

    //on place 88 par vert
    @Test
    void withPlacedTile3(){
        //size of the normal deck
        assertEquals(3,gameState6.tileDecks().deckSize(Tile.Kind.NORMAL));
        // check if added to the board
        assertEquals(getNormal88(), gameState6.board().tileWithId(88).tile());
        //Vérification next Action égale est OCCUPY_TILE
        assertEquals(GameState.Action.RETAKE_PAWN, gameState6.nextAction());
        //nb de message dans messageBoard
        assertEquals(1,gameState5.messageBoard().messages().size());
    }

    // on remove le pion de la tuile 27
    GameState gameState7  = gameState6.withOccupantRemoved(new Occupant(Occupant.Kind.PAWN, 270));
    @Test
    void removePawn1(){
        //il y a 5 pions et 3 huutes par personnes
        assertEquals(5,gameState7.freeOccupantsCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(3,gameState7.freeOccupantsCount(PlayerColor.GREEN, Occupant.Kind.HUT));
        //nb d occupant du board (0 car celui qui a fermé une rivière est aussi revenu)
        assertEquals(Set.of(),gameState7.board().occupants());

    }

    //player green pose un pion sru 88
    GameState gameState8 = gameState7.withNewOccupant(new Occupant(Occupant.Kind.PAWN,881));
    @Test
    void withNewOccupant3(){
        //nb de message dans messageBoard
        assertEquals(1,gameState8.messageBoard().messages().size());
        //Vérifications Occupants du board possèdent le nouveau pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN,881)), gameState8.board().occupants());
        //Vérifications nextAction = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, gameState5.nextAction());
        //ordre des joueurs correct
        assertEquals(List.of(PlayerColor.BLUE,PlayerColor.GREEN), gameState8.players());
        //Vérifications Tile to place = 87
        assertEquals(getNormal87(), gameState8.tileToPlace());
    }

    //blue player places 87
    GameState gameState9 = gameState8.withPlacedTile(new PlacedTile(getNormal87(), PlayerColor.BLUE, Rotation.HALF_TURN, new Pos(-2,-1)));
    @Test
    void withPlacedTile4(){
        //size of the normal deck
        assertEquals(2,gameState9.tileDecks().deckSize(Tile.Kind.NORMAL));
        // check if added to the board
        assertEquals(getNormal88(), gameState9.board().tileWithId(88).tile());
        //Vérification next Action égale est OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gameState9.nextAction());
        //nb de message dans messageBoard
        assertEquals(1,gameState9.messageBoard().messages().size());
    }

    GameState gameState10 = gameState9.withNewOccupant(new Occupant(Occupant.Kind.HUT,878));
    @Test
    void withNewOccupant4(){
        //nb de message dans messageBoard
        assertEquals(1,gameState10.messageBoard().messages().size());
        //Vérifications Occupants du board possèdent le nouveau pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN,881),new Occupant(Occupant.Kind.HUT, 878)), gameState10.board().occupants());
        //Vérifications nextAction = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, gameState10.nextAction());
        //ordre des joueurs correct
        assertEquals(List.of(PlayerColor.GREEN,PlayerColor.BLUE), gameState10.players());
        //Vérifications Tile to place = 82
        assertEquals(getNormal82(), gameState10.tileToPlace());
    }
    /**
    PlacedTile placedTile82 = new PlacedTile(getNormal82(),PlayerColor.GREEN,Rotation.HALF_TURN,new Pos(-1,-2));
    GameState gameState11 = gameState10.withPlacedTile(placedTile82);

    //green places tile 82 sans radeau

    @Test
    void withPlacedTile5(){
        //size of the normal deck
        assertEquals(1,gameState11.tileDecks().deckSize(Tile.Kind.NORMAL));
        // check if added to the board
        assertEquals(getNormal82(), gameState11.board().tileWithId(82).tile());
        //Vérification next Action égale est OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gameState11.nextAction());
    }

    GameState gameState12 = gameState11.withNewOccupant(null);
    @Test
    void withNewOccupant5(){
        System.out.println(gameState12.messageBoard().messages());
        //Vérifications Tile to place = null
        assertEquals(null, gameState12.tileToPlace());
        //Vérifications Occupants du board possèdent le nouveau pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.HUT, 878)), gameState12.board().occupants());
        //Vérifications nextAction = PLACE_TILE
        assertEquals(GameState.Action.END_GAME, gameState12.nextAction());

        //nb de message dans messageBoard ( 5 messages a voir pourquoi) todo a aoijferjferbfhrbfr
        assertEquals(4,gameState12.messageBoard().messages().size());
    }
    */
    PlacedTile placedTile82 = new PlacedTile(getNormalWithRaft82(),PlayerColor.GREEN,Rotation.HALF_TURN,new Pos(-1,-2));
    GameState gameState11 = gameState10.withPlacedTile(placedTile82);

    //green places tile 82 but with raft
    @Test
    void withPlacedTile5(){
        //size of the normal deck
        assertEquals(1,gameState11.tileDecks().deckSize(Tile.Kind.NORMAL));
        // check if added to the board
        assertEquals(getNormalWithRaft82(), gameState11.board().tileWithId(82).tile());
        //Vérification next Action égale est OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gameState11.nextAction());
    }

    GameState gameState12 = gameState11.withNewOccupant(null);
    @Test
    void withNewOccupant5(){
        System.out.println(gameState12.messageBoard().messages());
        //Vérifications Tile to place = null
        assertEquals(null, gameState12.tileToPlace());
        //Vérifications Occupants du board possèdent le nouveau pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.HUT, 878)), gameState12.board().occupants());
        //Vérifications nextAction = PLACE_TILE
        assertEquals(GameState.Action.END_GAME, gameState12.nextAction());

        //nb de message dans messageBoard ( 5 messages a voir pourquoi) todo a aoijferjferbfhrbfr
        assertEquals(5,gameState12.messageBoard().messages().size());
    }
}


