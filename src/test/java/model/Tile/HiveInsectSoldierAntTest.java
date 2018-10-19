package model.Tile;

import model.*;
import model.Tile.Exceptions.IllegalMoveSoldierAnt;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Een soldatenmier verplaatst zich door een onbeperkt aantal keren te verschuiven.
 * Een soldatenmier mag zich niet verplaatsen naar het veld waar hij al staat.
 * Een soldatenmier mag alleen verplaatst worden over en naar lege velden.
 */
class HiveInsectSoldierAntTest {
    //Een soldatenmier verplaatst zich door een onbeperkt aantal keren te verschuiven.
    @Test
    void testGetValidPathDoesNotThrowIllegalMove() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        HiveBoard hiveBoard = hiveGame.getBoard();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectSoldierAnt hiveInsectQueenBee = new HiveInsectSoldierAnt(hiveGame, hiveBoard);
        ArrayList<HiveLocation> validPath = hiveInsectQueenBee.getValidPath(2,0,1,-1);
        // erg veel paden mogelijk dus we testen niet een specifiek pad.
        // misschien @todo in de toekomst altijd het kortste pad terug geven?
    }

    // Een soldatenmier mag alleen verplaatst worden over en naar lege velden.
    @Test
    void testGetValidPathThrowsExceptionWhenMovingToFieldWhereHeAlreadyis() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        HiveBoard hiveBoard = hiveGame.getBoard();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectSoldierAnt hiveInsectSoldierAnt = new HiveInsectSoldierAnt(hiveGame, hiveBoard);
        assertThrows(IllegalMoveSoldierAnt.class, ()->{
            ArrayList<HiveLocation> validPath = hiveInsectSoldierAnt.getValidPath(3,0,2,0);
        });
    }

    // Een soldatenmier mag alleen verplaatst worden over en naar lege velden.
    @Test
    void testGetValidPathThrowsExceptionWhenShiftingTroughFieldThatIsNotEmpty() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        HiveBoard hiveBoard = hiveGame.getBoard();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectSoldierAnt hiveInsectSoldierAnt = new HiveInsectSoldierAnt(hiveGame, hiveBoard);
        ArrayList<HiveLocation> validPath = hiveInsectSoldierAnt.getValidPath(3,0,-1,0);
        for(int i = 0; i < validPath.size() - 1; i++){
            HiveLocation l = validPath.get(i);
            HiveCell cell = hiveBoard.getCellAt(l.getQ(), l.getR());
            assertTrue(cell.getPlayerTilesAtCell().size() == 0);
        }
        ArrayList<HiveLocation> validPath2 = hiveInsectSoldierAnt.getValidPath(0,1,1,-1);
        for(int i = 0; i < validPath2.size() - 1; i++){
            HiveLocation l = validPath2.get(i);
            HiveCell cell = hiveBoard.getCellAt(l.getQ(), l.getR());
            assertTrue(cell.getPlayerTilesAtCell().size() == 0);
        }
    }

    // Test of alle shifts vanuit getValidPath wel validShifts zijn, sinds de soldatenmier verplaatst door te schuiven
    @Test
    void testGetValidPathContainsValidShifts() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        HiveBoard hiveBoard = hiveGame.getBoard();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectSoldierAnt hiveInsectSoldierAnt = new HiveInsectSoldierAnt(hiveGame, hiveBoard);
        ArrayList<HiveLocation> validPath = hiveInsectSoldierAnt.getValidPath(3,0,-1,0);
        int fromQ = 3;
        int fromR = 0;
        for(HiveLocation l : validPath){
            int toQ = l.getQ();
            int toR = l.getR();
            assertTrue(hiveGame.isValidShift(fromQ, fromR, toQ, toR));
            fromQ = l.getQ();
            fromR = l.getR();
        }
    }
}