package ch.epfl.chacun;
/*
 *	Author:      Maxime Riesen
 *	Date:
 */
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class nikitaMyGameStateTest {
    List<Tile> tileList = new ArrayList<>(ch.epfl.chacun.tile.Tiles.TILES);
    //heap of startTile
    List<Tile> startList = List.of(tileList.get(56));
    //heap of Menhir
    List<Tile> menhirList = List.of(tileList.get(79),tileList.get(80),tileList.get(81),tileList.get(82),tileList.get(83),tileList.get(84),
            tileList.get(85),tileList.get(86),tileList.get(87),tileList.get(88),tileList.get(89),tileList.get(90),tileList.get(91),tileList.get(92),
            tileList.get(93),tileList.get(94));

    List <Tile> normalList = new ArrayList<>(tileList);

    MessageBoard messageBoard = new MessageBoard(new BasicTextMaker(),List.of());

    private List<Tile> normalTileList(){
        normalList.removeAll(menhirList);
        normalList.removeAll(startList);
        return normalList;
    }
    // tout le jeu
    private TileDecks tileDecksConstructor(){
        // deck complet du jeu
        return new TileDecks(startList,normalTileList(),menhirList);
    }

    private TileDecks tileDecksConstructorScenario(List<Tile> startList,List<Tile> normalList, List<Tile> menhirList){
        return new TileDecks(startList,normalList,menhirList);

    }

    //TODO Peut-être faire des tests tout con pour tester les préconditions de chaque méthode.
    // Car pas tjrs possible de le faire avec les scénarios




    @Test
    void constructorTest(){
        //player sup or egal 2
        assertThrows(IllegalArgumentException.class, () ->{new GameState(
                List.of(PlayerColor.GREEN),tileDecksConstructor(),
                tileList.get(56),Board.EMPTY, GameState.Action.START_GAME,messageBoard);});

        //tile to place == null and next action not place tile
        assertThrows(IllegalArgumentException.class, () ->{new GameState(
                List.of(PlayerColor.GREEN, PlayerColor.PURPLE),tileDecksConstructor(),
                tileList.get(56),Board.EMPTY, GameState.Action.START_GAME, messageBoard);});

        //message board null
        assertThrows(NullPointerException.class, () ->{new GameState(
                List.of(PlayerColor.GREEN, PlayerColor.PURPLE),tileDecksConstructor(),
                tileList.get(56),Board.EMPTY, GameState.Action.PLACE_TILE, null);});

        //tiledecks null
        assertThrows(NullPointerException.class, () ->{new GameState(
                List.of(PlayerColor.GREEN, PlayerColor.PURPLE), null,
                tileList.get(56),Board.EMPTY, GameState.Action.PLACE_TILE, messageBoard);});

        //board null
        assertThrows(NullPointerException.class, () ->{new GameState(
                List.of(PlayerColor.GREEN, PlayerColor.PURPLE), tileDecksConstructor(),
                tileList.get(56),null, GameState.Action.PLACE_TILE, messageBoard);});
    }

    @Test
    void currentPlayerIsCorrect(){
        GameState gameState = new GameState(List.of(PlayerColor.GREEN, PlayerColor.PURPLE), tileDecksConstructor(),
                null, Board.EMPTY, GameState.Action.START_GAME, messageBoard);
        GameState gameState1 = new GameState(List.of(PlayerColor.GREEN, PlayerColor.PURPLE), tileDecksConstructor(),
                tileList.get(35),Board.EMPTY, GameState.Action.PLACE_TILE,messageBoard);
        assertEquals(gameState.currentPlayer(),null);
        assertEquals(gameState1.currentPlayer(), PlayerColor.GREEN);
    }

    @Test
    void withStartingTilePlacedPreconditionTest(){
        GameState gameState  = new GameState(List.of(PlayerColor.GREEN, PlayerColor.PURPLE), tileDecksConstructor(),
                null, Board.EMPTY, GameState.Action.START_GAME, messageBoard);
        GameState gameStateFalseAction  = new GameState(List.of(PlayerColor.GREEN, PlayerColor.PURPLE),tileDecksConstructor(),null,Board.EMPTY, GameState.Action.END_GAME,messageBoard);

        GameState gameState1 = gameState.withStartingTilePlaced();
        assertEquals(gameState1.board().lastPlacedTile().tile(), tileList.get(56));
        assertEquals(gameState1.tileToPlace(), tileDecksConstructor().topTile(Tile.Kind.NORMAL));
        assertEquals(gameState1.nextAction(), GameState.Action.PLACE_TILE);
        assertThrows(IllegalArgumentException.class, () -> {gameStateFalseAction.withStartingTilePlaced();});
    }


    ////////////////////////////////////////////////////////
    //Scénario 1
    //Tile deck scenario 1
    TileDecks tileDecks = tileDecksConstructorScenario(
            List.of(tileList.get(56)),
            List.of(tileList.get(27),tileList.get(35),tileList.get(41),tileList.get(60),tileList.get(79)),
            List.of(tileList.get(17)));

    GameState gameState  = new GameState(
            List.of(PlayerColor.RED, PlayerColor.BLUE),
            tileDecks,
            null,
            Board.EMPTY,
            GameState.Action.START_GAME,
            messageBoard);

    GameState gameStateAfterWithStartingTilePlaced = gameState.withStartingTilePlaced();

    @Test
    void  withStartingTilePlacedTest1(){
        // Vérifications Tas de tuile START vide
        assertEquals(List.of(), gameStateAfterWithStartingTilePlaced.tileDecks().startTiles());

        // Vérifications nextAction = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, gameStateAfterWithStartingTilePlaced.nextAction());

        // Vérifications TileToPlace = 27
        assertEquals(tileList.get(27), gameStateAfterWithStartingTilePlaced.tileToPlace());

        // Vérifications du board pos(0,0)
        assertEquals(new PlacedTile(tileList.get(56), null, Rotation.NONE, new Pos(0,0), null),
                gameStateAfterWithStartingTilePlaced.board().tileAt(Pos.ORIGIN));

        // Vérifications du board pos(0,1)
        assertEquals(null, gameStateAfterWithStartingTilePlaced.board().tileAt(new Pos(0,1)));

        //Vérification Tas de tuiles normale de taille 4, donc que la tile a été retirée du deck
        assertEquals(4, gameStateAfterWithStartingTilePlaced.tileDecks().normalTiles().size());
    }

    /// Joueur rouge place la tuile 27 à la pos 1,0.
    PlacedTile placedTile27 = new PlacedTile(tileList.get(27), PlayerColor.RED,Rotation.NONE,new Pos(1,0));
    GameState gameStateAfterWithPlacedTile = gameStateAfterWithStartingTilePlaced.withPlacedTile(placedTile27);
    @Test
    void withPlacedTileTest1(){
        //Vérification Tas de tuiles normale de taille 4, donc que la tile a été retirée du deck
        assertEquals(4, gameStateAfterWithPlacedTile.tileDecks().normalTiles().size());

        //check if the tile has been added to the board
        assertEquals(tileList.get(27), gameStateAfterWithPlacedTile.board().tileWithId(27).tile());

        //Vérification next Action égale est OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gameStateAfterWithPlacedTile.nextAction());

        //MessageBoard should be empty
        assertTrue(gameStateAfterWithPlacedTile.messageBoard().messages().isEmpty());


    }


    /// Joueur Rouge Occupe Forêt de la tuile 27.
    GameState gameStateAfterWithNewOccupants = gameStateAfterWithPlacedTile.withNewOccupant
            (new Occupant(Occupant.Kind.PAWN, 27_3));
    @Test
    void withNewOccupantTest1(){
        //Vérifications Occupants du board possèdent le nouveau pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 27_3)),
                gameStateAfterWithNewOccupants.board().occupants());

        //Vérifications nextAction = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, gameStateAfterWithNewOccupants.nextAction());

        //Vérifications Liste des joueurs possède le joueur bleu devant (tours du rouge finis)
        assertEquals(PlayerColor.BLUE, gameStateAfterWithNewOccupants.players().getFirst());
        assertEquals(PlayerColor.RED, gameStateAfterWithNewOccupants.players().get(1));

        //Vérifications Tile to place = 35
        assertEquals(tileList.get(35), gameStateAfterWithNewOccupants.tileToPlace());

        //Vérification Tas de tuiles normale de taille 3, donc que la tile a été retirée du deck
        assertEquals(3, gameStateAfterWithNewOccupants.tileDecks().normalTiles().size());
    }

    /// Joueur Bleu place 35
    PlacedTile placedTile35 = new PlacedTile(
            tileList.get(35),PlayerColor.BLUE, Rotation.LEFT, new Pos(0,1), null);
    GameState gameStateBlue35 = gameStateAfterWithNewOccupants.withPlacedTile(placedTile35);
    @Test
    void withPlacedTileTest2(){
        //Vérification Tas de tuiles normale de taille 3
        assertEquals(3, gameStateBlue35.tileDecks().normalTiles().size());

        //Vérification next Action égale est OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gameStateBlue35.nextAction());

        //Vérification du board
        assertEquals(placedTile35, gameStateBlue35.board().lastPlacedTile());
        assertEquals(placedTile35, gameStateBlue35.board().tileAt(new Pos(0,1)));

        //MessageBoard should be empty
        assertTrue(gameStateBlue35.messageBoard().messages().isEmpty());

        //Vérifications Occupants du board possèdent le pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 27_3)),
                gameStateBlue35.board().occupants());

        //Vérification des pions de rouge et bleu
        assertEquals(4, gameStateBlue35.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(5, gameStateBlue35.freeOccupantsCount(PlayerColor.BLUE, Occupant.Kind.PAWN));


    }

    //Joueur Bleu Occupe Meadow de la tuile 35
    GameState gameWithNewOccupantBlue35 = gameStateBlue35.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 35_0));
    @Test
    void withNewOccupantTestBlue35(){
        //Vérifications Occupants du board possèdent le nouveau pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 35_0)), gameWithNewOccupantBlue35.board().occupants());

        //Vérifications next action = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, gameWithNewOccupantBlue35.nextAction());

        //Liste des joueurs possède le joueur bleu devant (fermé un menhir)
        assertEquals(PlayerColor.BLUE, gameWithNewOccupantBlue35.players().getFirst());

        //Le message board possède un nouveau message (fermeture de la forêt)
        assertTrue( !gameWithNewOccupantBlue35.messageBoard().messages().isEmpty() );
        //System.out.println(gameWithNewOccupantBlue35.messageBoard().messages());

        //Tile to place = 17
        assertEquals(tileList.get(17), gameWithNewOccupantBlue35.tileToPlace());

        //
        assertEquals(5, gameWithNewOccupantBlue35.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.PAWN));
    }

    ///Joueur Bleu place 17
    //methode pour faire la 17 menhirs
    public Tile getMenhirs17(){
        var z0 = new Zone.Meadow(17_0, List.of(), null);
        var z1 = new Zone.River(17_1, 0, null);
        var a2_0 = new Animal(17_2_0, Animal.Kind.DEER);
        var z2 = new Zone.Meadow(17_2, List.of(a2_0), null);
        var z3 = new Zone.River(17_3, 0, null);
        var a4_0 = new Animal(17_4_0, Animal.Kind.TIGER);
        var z4 = new Zone.Meadow(17_4, List.of(a4_0), null);
        var sN = new TileSide.River(z0, z1, z2);
        var sE = new TileSide.River(z2, z1, z0);
        var sS = new TileSide.River(z0, z3, z4);
        var sW = new TileSide.River(z4, z3, z0);
        return new  Tile(17, Tile.Kind.MENHIR, sN, sE, sS, sW);
    }

    PlacedTile placedTile17 = new PlacedTile(
            getMenhirs17(), PlayerColor.BLUE, Rotation.NONE, new Pos(-1,0));
    GameState gameBlue17 = gameWithNewOccupantBlue35.withPlacedTile(placedTile17);
    @Test
    void withPlacedTile17(){
        //Tas de tuiles menhir vide
        assertTrue(gameBlue17.tileDecks().menhirTiles().isEmpty());

        //nextAction = OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gameBlue17.nextAction());

        //player qui doit jouer le bleu
        assertEquals(PlayerColor.BLUE, gameBlue17.players().getFirst());

        //message board TODO savoir ce que le message board doit être
        //System.out.println(gameBlue17.messageBoard().messages());
    }


    //Joueur Bleu Occupe Meadow numéro 2 de la tuile 17
    GameState gameOccupy17 = gameBlue17.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 17_2));
    @Test
    void Occupy17(){
        //board a le pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 17_2), new Occupant(Occupant.Kind.PAWN, 35_0)),
                gameOccupy17.board().occupants());

        //nextAction = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, gameOccupy17.nextAction());

        //Liste des joueurs possède le joueur rouge devant (tours du joueur bleu fini)
        assertEquals(PlayerColor.RED, gameOccupy17.players().getFirst());

        //Tiletoplace = 41
        assertEquals(tileList.get(41), gameOccupy17.tileToPlace());
    }

    //Joueur Rouge place 41
    PlacedTile placedTile41 = new PlacedTile(
            tileList.get(41),PlayerColor.RED, Rotation.NONE, new Pos(0,-1), null);
    GameState gamePlace41 = gameOccupy17.withPlacedTile(placedTile41);
    @Test
    void place17Test(){
        //Tas de tuiles normal de taille 2
        assertEquals(2, gamePlace41.tileDecks().normalTiles().size());

        //nextAction = OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gamePlace41.nextAction());
    }

    //Joueur Rouge Occupe Meadow de la tuile 41
    GameState game41Occupy = gamePlace41.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 41_2));
    @Test
    void occupy41Test(){
        //board a le pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 17_2),
                        new Occupant(Occupant.Kind.PAWN, 35_0),
                        new Occupant(Occupant.Kind.PAWN, 41_2)),
                game41Occupy.board().occupants());

        //nextAction = PLACE_TILE
        assertEquals(GameState.Action.PLACE_TILE, game41Occupy.nextAction());

        //Liste des joueurs possède le joueur bleu devant
        assertEquals(PlayerColor.BLUE, game41Occupy.players().getFirst());

        //Tiletoplace = 40
        assertEquals(tileList.get(60), game41Occupy.tileToPlace());
        System.out.println(game41Occupy.messageBoard().messages());
    }

    //Joueur Bleu place 60
    PlacedTile placedTile60 = new PlacedTile(
            tileList.get(60), PlayerColor.BLUE, Rotation.NONE, new Pos(0,-2), null);
    GameState gamePlace60 = game41Occupy.withPlacedTile(placedTile60);
    @Test
    void place60Test(){
        //Tas de tuiles menhir vide
        assertEquals(1, gamePlace60.tileDecks().normalTiles().size());

        //nextAction = OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gamePlace60.nextAction());

        //player qui doit jouer le rouge
        assertEquals(PlayerColor.BLUE, gamePlace60.players().getFirst());

        //Tiletoplace = 79
        assertEquals(null, gamePlace60.tileToPlace());
    }

    //blue ne veut pas occuper
    GameState gameOccupy60 = gamePlace60.withNewOccupant(null);
    @Test
    void occupy17Test(){
        //board a le pion
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 17_2),
                        new Occupant(Occupant.Kind.PAWN, 35_0),
                        new Occupant(Occupant.Kind.PAWN, 41_2)),
                gameOccupy60.board().occupants());

        //action
        assertEquals(GameState.Action.PLACE_TILE, gameOccupy60.nextAction());

        //rouge
        assertEquals(PlayerColor.RED, gameOccupy60.players().getFirst());

        assertEquals(tileList.get(79), gameOccupy60.tileToPlace());
    }

    //Joueur Rouge place 79
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
    PlacedTile placedTile79 = new PlacedTile(
            getNormal79(), PlayerColor.RED, Rotation.NONE, new Pos(-1,-1), null);
    GameState gamePlace79 = gameOccupy60.withPlacedTile(placedTile79);

    @Test
    void place79Test(){
        //Tas de tuiles normal vide
        assertEquals(0, gamePlace79.tileDecks().normalTiles().size());

        //nextAction = OCCUPY_TILE
        assertEquals(GameState.Action.OCCUPY_TILE, gamePlace79.nextAction());

        //player qui doit jouer le rouge
        assertEquals(PlayerColor.RED, gamePlace79.players().getFirst());

        //Tiletoplace = 79
        assertNull(gamePlace79.tileToPlace());

        assertEquals(placedTile79, gamePlace79.board().tileAt(new Pos(-1, -1)));


    }

    GameState gameOccupy79 = gamePlace79.withNewOccupant(null);



    @Test
    void gameEndTest(){
        //action end game
        assertEquals(GameState.Action.END_GAME, gameOccupy79.nextAction());

        //
        System.out.println(gameOccupy79.messageBoard().messages());



    }

















}



































