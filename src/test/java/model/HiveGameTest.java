package model;

import model.Tile.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HiveGameTest {
    @Test
    void testInitHiveGame2PlayersExistsBlackAndWhite() throws NoSuchFieldException, IllegalAccessException {
        HiveGame hiveGame = new HiveGame();
        final Field field = hiveGame.getClass().getDeclaredField("hivePlayers");
        field.setAccessible(true);
        ArrayList<HivePlayer> hivePlayers = (ArrayList<HivePlayer>) field.get(hiveGame);
        assertTrue(hivePlayers.size() == 2);

        ArrayList<Hive.Player> playerColors = new ArrayList<Hive.Player>();
        for(HivePlayer hivePlayer : hivePlayers){
            playerColors.add(hivePlayer.getPlayerColor());
        }
        assertTrue(playerColors.contains(Hive.Player.WHITE));
        assertTrue(playerColors.contains(Hive.Player.BLACK));
    }

    @Test
    void testInitHiveGameEachPlayerHasCorrectTiles() throws NoSuchFieldException, IllegalAccessException {
        HiveGame hiveGame = new HiveGame();
        final Field field = hiveGame.getClass().getDeclaredField("hivePlayers");
        field.setAccessible(true);
        ArrayList<HivePlayer> hivePlayers = (ArrayList<HivePlayer>) field.get(hiveGame);
        ArrayList<Hive.Tile> requiredTiles = new ArrayList<Hive.Tile>();
        int queenBee = 0;
        int grasHopper = 0;
        int beetle = 0;
        int soldierAnt = 0;
        int spider = 0;
        for(HivePlayer hivePlayer : hivePlayers){
            for (HivePlayerTile hivePlayerTile : hivePlayer.getPlayerTiles()){
                if(hivePlayerTile.getInsect().getTile().equals(Hive.Tile.QUEEN_BEE)) queenBee+=1;
                if(hivePlayerTile.getInsect().getTile().equals(Hive.Tile.GRASSHOPPER)) grasHopper+=1;
                if(hivePlayerTile.getInsect().getTile().equals(Hive.Tile.BEETLE)) beetle+=1;
                if(hivePlayerTile.getInsect().getTile().equals(Hive.Tile.SOLDIER_ANT)) soldierAnt+=1;
                if(hivePlayerTile.getInsect().getTile().equals(Hive.Tile.SPIDER)) spider+=1;
            }
            assertTrue(queenBee == 1);
            assertTrue(spider == 2);
            assertTrue(beetle == 2);
            assertTrue(soldierAnt == 3);
            assertTrue(grasHopper == 3);
            queenBee = grasHopper = beetle = soldierAnt = spider = 0;
        }
    }

    // Wit heeft de eerste beurt
    @Test
    void testInitHiveGameWhitePlayerStarts() throws NoSuchFieldException, IllegalAccessException {
        HiveGame hiveGame = new HiveGame();
        final Field field = hiveGame.getClass().getDeclaredField("currentPlayer");
        field.setAccessible(true);
        HivePlayer hivePlayer = (HivePlayer ) field.get(hiveGame);
        assertTrue(hivePlayer.getPlayerColor().equals(Hive.Player.WHITE));
    }

    // Aan het begin van het spel is het speelveld leeg.
    @Test
    void testInitHiveGameIsPlayFieldEmpty() throws NoSuchFieldException, IllegalAccessException {
        HiveGame hiveGame = new HiveGame();
        final Field field = hiveGame.getClass().getDeclaredField("hiveBoard");
        field.setAccessible(true);
        HiveBoard hiveBoard = (HiveBoard) field.get(hiveGame);
        assertTrue(hiveBoard.getBoard().isEmpty());
    }

    //Tijdens zijn beurt kan een speler een steen spelen, een steen verplaatsen of passen; daarna is de tegenstander aan de beurt.
    //Passen is moelijk te testen dus die laten we even weg (dit mag namelijk alleen als een speler echt niet kan)
    @Test
    void testIfCurrentPlayerChangesAfterPlayerMakesAmove() throws NoSuchFieldException, IllegalAccessException, Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        final Field field = hiveGame.getClass().getDeclaredField("currentPlayer");
        field.setAccessible(true);
        HivePlayer currPlayer = (HivePlayer ) field.get(hiveGame);
        assertTrue(currPlayer.getPlayerColor().equals(Hive.Player.WHITE));
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit speelt QUEEN_BEE 0,0
        HivePlayer currPlayer2 = (HivePlayer ) field.get(hiveGame);
        assertTrue(currPlayer2.getPlayerColor().equals(Hive.Player.BLACK));
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1,0); // zwart speelt QUEEN_BEE 1,0
        HivePlayer currPlayer3 = (HivePlayer ) field.get(hiveGame);
        assertTrue(currPlayer3.getPlayerColor().equals(Hive.Player.WHITE));
        hiveGame.move(0,0,0,1); // wit moved QUEEN_BEE 0,0 naar 0,1
        HivePlayer currPlayer4 = (HivePlayer ) field.get(hiveGame);
        assertTrue(currPlayer4.getPlayerColor().equals(Hive.Player.BLACK));
    }

    // Een speler wint als alle zes velden naast de bijenkoningin van de tegenstander bezet zijn
    @Test
    void testIsWinnerFalseWhenQueenBeeIsNotSurrounded() throws NoSuchFieldException, IllegalAccessException {
        HiveGame hiveGame = new HiveGame();
        final Field field = hiveGame.getClass().getDeclaredField("hiveBoard");
        field.setAccessible(true);
        HiveBoard hiveBoard = (HiveBoard) field.get(hiveGame);
        HiveInsectQueenBee queenBee = new HiveInsectQueenBee(hiveGame);
        HiveInsectSpider spider = new HiveInsectSpider(hiveGame);
        HiveInsectBeetle beetle = new HiveInsectBeetle(hiveGame);
        HiveInsectSoldierAnt soldierAnt = new HiveInsectSoldierAnt(hiveGame);
        HiveInsectGrasshopper grasshopper = new HiveInsectGrasshopper(hiveGame);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), queenBee), 0, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), grasshopper), -1, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), soldierAnt), -1, 1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), beetle), 0, 1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), beetle), 1, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), beetle), 1, -1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), spider), 0, -2); // deze is fout
        assertTrue(!hiveGame.isWinner(Hive.Player.WHITE));
        assertTrue(!hiveGame.isWinner(Hive.Player.BLACK));
    }

    @Test
    void testIsWinnerTrueWhenQueenBeeIsSurrounded() throws NoSuchFieldException, IllegalAccessException {
        HiveGame hiveGame = new HiveGame();
        final Field field = hiveGame.getClass().getDeclaredField("hiveBoard");
        field.setAccessible(true);
        HiveBoard hiveBoard = (HiveBoard) field.get(hiveGame);
        HiveInsectQueenBee queenBee = new HiveInsectQueenBee(hiveGame);
        HiveInsectSpider spider = new HiveInsectSpider(hiveGame);
        HiveInsectBeetle beetle = new HiveInsectBeetle(hiveGame);
        HiveInsectSoldierAnt soldierAnt = new HiveInsectSoldierAnt(hiveGame);
        HiveInsectGrasshopper grasshopper = new HiveInsectGrasshopper(hiveGame);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), queenBee), 0, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), grasshopper), 0, -1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), soldierAnt), 1, -1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), beetle), 1, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), beetle), -1, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), beetle), 0, 1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), spider), -1, 1);
        assertTrue(!hiveGame.isWinner(Hive.Player.WHITE));
        assertTrue(hiveGame.isWinner(Hive.Player.BLACK));
    }

    // Wel zes neighbour cellen maar geen playerTile in een van de cellen
    @Test
    void testIsWinnerFalseIfCellInNeighboursButNoTile() throws NoSuchFieldException, IllegalAccessException, Hive.IllegalMove {
        // Voeg HivePlayerTile toe aan bord
        HiveGame hiveGame = new HiveGame();
        final Field field = hiveGame.getClass().getDeclaredField("hiveBoard");
        field.setAccessible(true);
        HiveBoard hiveBoard = (HiveBoard) field.get(hiveGame);
        HiveInsectQueenBee queenBee = new HiveInsectQueenBee(hiveGame);
        HiveInsectSpider spider = new HiveInsectSpider(hiveGame);
        HiveInsectBeetle beetle = new HiveInsectBeetle(hiveGame);
        HiveInsectSoldierAnt soldierAnt = new HiveInsectSoldierAnt(hiveGame);
        HiveInsectGrasshopper grasshopper = new HiveInsectGrasshopper(hiveGame);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), queenBee), 0,0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), grasshopper), 0, -1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), soldierAnt), 1, -1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), beetle), 1, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), beetle), -1, 0);
        HivePlayerTile tileToRemove = new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), beetle);
        hiveBoard.addPlayerTile(tileToRemove, 0, 1);
        hiveBoard.removePlayerTile(tileToRemove, 0, 1); // geen tile meer in locatie 0,1
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), spider), -1, 1);
        assertTrue(!hiveGame.isWinner(Hive.Player.WHITE));
        assertTrue(!hiveGame.isWinner(Hive.Player.BLACK));
    }


    //Als beide spelers tegelijk zouden winnen is het in plaats daarvan een gelijkspel.
    @Test
    void testIsDraw() throws NoSuchFieldException, IllegalAccessException {
        HiveGame hiveGame = new HiveGame();
        final Field field = hiveGame.getClass().getDeclaredField("hiveBoard");
        field.setAccessible(true);
        HiveBoard hiveBoard = (HiveBoard) field.get(hiveGame);
        // Surround the queen bee of white with 6 cells == white winner
        HiveInsectQueenBee queenBee = new HiveInsectQueenBee(hiveGame);
        HiveInsectSpider spider = new HiveInsectSpider(hiveGame);
        HiveInsectBeetle beetle = new HiveInsectBeetle(hiveGame);
        HiveInsectSoldierAnt soldierAnt = new HiveInsectSoldierAnt(hiveGame);
        HiveInsectGrasshopper grasshopper = new HiveInsectGrasshopper(hiveGame);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), queenBee), 0, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), grasshopper), -1, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), soldierAnt), -1, 1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), beetle), 1, 0);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), beetle), 1, -1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), spider), 0, -1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), queenBee), 0, 1);
        assertTrue(!hiveGame.isDraw());
        // Make the white player also a winner by surrounding black's queen bee
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), spider), 1, 1);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.BLACK), beetle), 0, 2);
        hiveBoard.addPlayerTile(new HivePlayerTile(new HivePlayer(Hive.Player.WHITE), grasshopper), -1, 2);
        // Beide spelers zijn dus nu een winnaar dus we verwachten een draw
        assertTrue(hiveGame.isDraw());
    }

    //Een speler mag alleen zijn eigen nog niet gespeelde stenen spelen.
    @Test
    void testPlayTileThrowsIllegalMoveWhenPlayerDoesNotHaveTheTileToMoveInItsPlayerTiles() throws Hive.IllegalMove, NoSuchFieldException, IllegalAccessException {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // Wit speelt queen bee 0,0
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,1); // Zwart speelt queen bee 0,1
        assertThrows(Hive.IllegalMove.class,
                ()->{
                    hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // Wit speelt queen bee 1,0 weer (dit kan niet want queen bee is al gespeeld)
                });
    }

    // Een speler speelt een steen door deze op een leeg vlak in het speelveld te leggen.
    @Test
    void testPlayTileThrowsIllegalMoveWhenTilePlayedIsOnFieldThatAlreadyHasAnotherTile() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.GRASSHOPPER, 0, 0); // Wit speelt grasshopper q0 r0
        assertThrows(Hive.IllegalMove.class, ()->{
            hiveGame.play(Hive.Tile.SOLDIER_ANT, 0, 0); // Zwart speelt grashopper q0 r0 op dezelfde tile
        });
    }

    // Als er al stenen op het bord liggen moet een naast een andere steen gespeeld worden
    @Test
    void testPlayTileThrowsIllegalMoveWhenTilePlayedIsNotNextToOtherTileWhenThereAreAlreadyTilesOnBoard1() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.GRASSHOPPER, 0, 0); // Wit speelt grasshopper q0 r0
        assertThrows(Hive.IllegalMove.class, () -> {
            hiveGame.play(Hive.Tile.BEETLE, 1, -2); // Zwart speelt een steen die NIET naast een andere steen in het bord ligt
        });
    }

    // Als er stenen van beide spelers op het bord liggen mag een steen niet naast een steen van de tegenstander geplaatst worden
    // alleen in de eerste beurt is het toegestaan om een insect tegen de tegenstander aan te leggen
    @Test
    void testPlayTileThrowsIllegalMoveWhenNewPlayedTileIsBesidesOpponentTileWhenBothPlayersHaveTilesOnBoard() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.BEETLE, 0, 0); // wit speelt steen
        hiveGame.play(Hive.Tile.GRASSHOPPER, 0, 1); // zwart speelt steen naast wit zijn steen (1e beurt mag dit)
        assertThrows(Hive.IllegalMove.class, ()->{
            hiveGame.play(Hive.Tile.SOLDIER_ANT, 1, 0); // wit speelt steen naast zwart zijn steen (2e beurt mag dit niet)
        });
    }

    //Als een speler al drie stenen gespeeld heeft maar zijn bijenkoningin nog niet dan moet deze gespeeld worden.
    @Test
    void testPlayTileThrowsIllegalMoveWhenBeeQuenIsNotPlayedAfterHeHasPlayedThreeTiles() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.BEETLE, 1, 0); // wit speelt steen
        hiveGame.play(Hive.Tile.GRASSHOPPER, 0, 1); // zwart speelt steen
        hiveGame.play(Hive.Tile.BEETLE, 2, 0); // wit speelt steen
        hiveGame.play(Hive.Tile.GRASSHOPPER, -1, 2); // zwart speelt steen
        hiveGame.play(Hive.Tile.GRASSHOPPER, 3, 0); // wit speelt steen
        hiveGame.play(Hive.Tile.GRASSHOPPER, -2, 3); // zwart speelt steen
        assertThrows(Hive.IllegalMove.class, ()-> {
            hiveGame.play(Hive.Tile.SOLDIER_ANT, 3, -1); // wit speelt steen maar het is de 4e beurt, queen bee moet gespeeld worden
        });
    }

    // 5a Een speler mag alleen zijn eigen eerder gespeelde stenen verplaatsen.
    @Test
    void testMoveTileThrowsIllegalMoveWhenMovingTileDoesNotExistsOnBoardOrIsFromOtherPlayer() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // wit speelt steen
        hiveGame.play(Hive.Tile.GRASSHOPPER, 0, 1); // zwart speelt steen
        assertThrows(Hive.IllegalMove.class, ()->{
            hiveGame.move(0, 1, 2, 0); // verplaats steen beetle van 1,0 naar 2,0
        });
    }

    // 5b Een speler mag pas stenen verplaatsen als zijn bijenkoningin gespeeld is.
    @Test
    void testMoveTileThrowsIllegalMoveWhenQueenBeeHasNotBeenPlayedYet() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.BEETLE, 1, 0); // wit speelt steen
        hiveGame.play(Hive.Tile.GRASSHOPPER, 0, 1); // zwart speelt steen
        assertThrows(Hive.IllegalMove.class, ()->{
            hiveGame.move(1, 0, 2, 0); // wit moved steen op een valide plek maar queen bee is nog niet gespeeld
        });
    }

    // 5c. Een steen moet na het verplaatsen in contact zijn met minstens één andere steen.
    @Test
    void testMoveTileThrowsIllegalMoveWhenTileAfterMoveIsNotConnectedToAtLeastOneOtherTile() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // wit speelt steen
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0, 1); // zwart speelt steen
        hiveGame.move(1, 0, 1, 1); // wit moved zijn eigen steen 1,0 naas zwarte steen
        assertThrows(Hive.IllegalMove.class, ()->{
           hiveGame.move(0,1, -3,0); // zwart speelt steen niet naast een andere steen
        });
    }

    // 5d. Een steen mag niet verplaatst worden als er door het weghalen van de steen twee niet onderling verbonden groepen stenen ontstaan.
    @Test
    void testMoveTileThrowsIllegalMoveWhenAfterMoveSomeTilesAreNotConnectedAnymore() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.BEETLE, -1, 0); // wit speelt steen
        hiveGame.play(Hive.Tile.GRASSHOPPER, 0, 0); // zwart speelt steen naast wit (dit mag in de eerste beurt)
        hiveGame.play(Hive.Tile.BEETLE, -2, 0); // wit speelt steen
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart speelt steen
        hiveGame.play(Hive.Tile.QUEEN_BEE, -3, 0); // wit speelt steen
        assertThrows(Hive.IllegalMove.class, ()->{
            // 0,0 verbind dus nu de stenen
            hiveGame.move(0, 0, 2, 0); // zwart moved steen op q,r 0,0 en we verwachten een error
        });
    }

    // 6a Sommige stenen verplaatsen zich door te verschuiven. Een verschuiving is
    // een verschuiving naar een aangrenzend veld; dit mag een bezet of onbezet
    // veld zijn.
    @Test
    void testMoveTileThrowsIllegalMoveWhenShiftTileNotNextToField() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 2, -1); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1,0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, 3, -1); // wit
        hiveGame.play(Hive.Tile.BEETLE, 1, 1); // zwart
        hiveGame.move(3, -1, 2, 0); // wit Move naar onbezet veld
        hiveGame.move(1,1, 2, 0); // zwart move naar bezet veld
        assertThrows(Hive.IllegalMove.class, ()->{
           hiveGame.move(2, 0, 4, 0); // zwart moved veld naar veld die niet grezend is (slaat 1 cell over)
        });
    }

    // 6b. Een verschuiving moet schuivend uitgevoerd kunnen worden. Dit betekent dat tijdens de verschuiving moet gelden dat de laagste van de twee stapels
    // die aan het begin- en eindpunt grenzen niet hoger mag zijn dan de hoogste van de twee stapels op het begin- en eindpunt, waarbij de te verplaatsen
    // steen niet mee telt. In onderstaande figuur is een verschuiving te zien die niet schuivend uitgevoerd kan worden.
    @Test
    void testMoveTileThrowsIllegalMoveWhenShiftTileDoesNotFitTroughSpace() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 2, -1); // wit
        hiveGame.play(Hive.Tile.BEETLE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, 3, -1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 1, 1);
        assertThrows(Hive.IllegalMove.class, ()->{
            hiveGame.move(2, -1, 2, 0); // deze move past niet
        });
    }

    // 6c. Tijdens een verschuiving moet de steen continu in contact blijven met
    // minstens één andere steen. In onderstaande figuur is een verschuiving te
    // zien waarbij de steen zowel voor als na de verschuiving in contact met
    // andere stenen is, maar tijdens de verschuiving niet. Deze verschuiving is dus
    // niet toegestaan.
    @Test
    void testMoveTileThrowsIllegalMoveWhenShiftTileIsNotConnectedWhileShfitingTile() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 3, -1); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 3, 0); // zwart
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, -1); // wit
        hiveGame.play(Hive.Tile.BEETLE, 2, 1); // zwart
        hiveGame.play(Hive.Tile.BEETLE, 1, 0); // wit
        assertThrows(Hive.IllegalMove.class, ()-> {
            // Beetle kan maar één stap doen
            // 2,1 -> 1,1 dan zijn de tiles niet verbonden
            // dus dit gaat fout
            hiveGame.move(2, 1, 1, 1);
        });
    }

    @Test
    void testMoveTileThrowsIllegalMoveWhenShiftTileBeetleShiftsMoreThanOnePlace() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        assertThrows(Hive.IllegalMove.class, ()->{
            hiveGame.move(-1,1, 1,1); // dit mag niet zijn 3 stappen
        });
    }


//    // Expect true when calling isValidShift When the toCell is a neighbour of the fromCell
//    @Test
//    void testIsValidShiftSucceedsWhenShiftIsValid() throws Hive.IllegalMove {
//        HiveGame hiveGame = new HiveGame();
//        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
//        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
//        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
//        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
//        boolean valid = hiveGame.isValidShift(0,-1,1,-1);
//        assertTrue(valid == true);
//        boolean valid2 = hiveGame.isValidShift(1,1,2,1);
//        assertTrue(valid2 == true);
//    }
//
//    // Expect false when calling isValidShift When the toCell is not a neighbour of the fromCell
//    @Test
//    void testIsValidShiftFailsWhenToCellIsNotANeighbourOfFromCell() throws Hive.IllegalMove {
//        HiveGame hiveGame = new HiveGame();
//        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
//        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
//        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
//        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
//        boolean valid = hiveGame.isValidShift(0,-1,2,-1); // probeer een neppe cel, dit moet kunnen
//        assertTrue(valid == false);
//    }
//
//
//    // Expect false when calling isValidShift When the toCell and fromCell have 0 neighbours in common, which means the tile will be lose when shifting
//    @Test
//    void testIsValidShiftFailsWhenToCellAndFromCellHaveZeroNeighboursInCommon() throws Hive.IllegalMove {
//        HiveGame hiveGame = new HiveGame();
//        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
//        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
//        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
//        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
//        boolean valid = hiveGame.isValidShift(0,-1,1,-2); // probeer een neppe cel, dit moet kunnen
//        assertTrue(valid == false);
//    }

    @Test
    void testIfFirstTurnFunctionHasExpectedResult() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        assertTrue(hiveGame.getBoard().isFirstTurn() == true);
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        assertTrue(hiveGame.getBoard().isFirstTurn() == true);
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        assertTrue(hiveGame.getBoard().isFirstTurn() == false);
    }

    @Test
    void testExceptionIsThroneWhenTilesAreNotConnected() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        assertThrows(Hive.IllegalMove.class, ()->{
            // Want -1,1 is niet connected met 1,0 of -1,-1
            hiveGame.move(0,0,1,-1);
        });
    }

    @Test
    void testExceptionIsThroneWhenTilesAreNotConnected2() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        assertThrows(Hive.IllegalMove.class, ()->{
            // Want -1,1 is niet connected met 1,0 of -1,0
            hiveGame.move(0,0,-1, 0);
        });
    }

    @Test
    void testExceptionIsThroneWhenTilesAreNotConnected3() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        hiveGame.play(Hive.Tile.SOLDIER_ANT, -2, 2); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 3, 0); // zwart
        assertThrows(Hive.IllegalMove.class, ()->{
            // Want -1,1 is niet connected met 1,0 of -1,-1
            hiveGame.move(0,0,1, -1);
        });
    }


    @Test
    // Queen bee
    void testExceptionIsThroneWhenTilesAreNotConnected4() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectQueenBee hiveInsectQueenBee = new HiveInsectQueenBee(hiveGame);
        assertThrows(Hive.IllegalMove.class, ()->{
            hiveGame.move(0,0, 1,-1);
        });
    }
}