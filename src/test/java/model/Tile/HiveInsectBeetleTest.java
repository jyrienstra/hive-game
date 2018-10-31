package model.Tile;

import model.Tile.Exceptions.IllegalMoveBeetle;
import model.Hive;
import model.HiveBoard;
import model.HiveGame;
import model.HiveLocation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Een kever verplaatst zich door precies één keer te verschuiven.
 */
class HiveInsectBeetleTest {
    @Test
    void testGetValidMoveContainsExpectedMoveWhenMakingAValidMove() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectBeetle hiveInsectBeetle = new HiveInsectBeetle(hiveGame);

        ArrayList<HiveLocation> validPath = hiveInsectBeetle.getValidPath(-1,1,0,1);
        assertTrue(validPath.size() == 1);
        assertTrue(validPath.get(0).equals(new HiveLocation(0,1)));
    }

    @Test
    void testGetValidMoveThrowsIllegalMoveBeetleClassWhenShiftingMoreThanOnce() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectBeetle hiveInsectBeetle = new HiveInsectBeetle(hiveGame);

        assertThrows(IllegalMoveBeetle.class, ()->{
            ArrayList<HiveLocation> validPathMaxDepth1 = hiveInsectBeetle.getValidPath(-1,1,1,1); // beelte max depth is 1
        });
    }

    // Test of alle shifts vanuit getValidPath wel validShifts zijn, sinds de Queenbee zich verplaatst door te schuiven
    @Test
    void testGetValidPathContainsValidShifts() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectBeetle hiveInsectBeetle = new HiveInsectBeetle(hiveGame);
        ArrayList<HiveLocation> validPath = hiveInsectBeetle.getValidPath(-1,1,0,1);
        int fromQ = -1;
        int fromR = 1;
        for(HiveLocation l : validPath){
            int toQ = l.getQ();
            int toR = l.getR();
            assertTrue(hiveGame.getBoard().isValidShift(fromQ, fromR, toQ, toR));
            fromQ = l.getQ();
            fromR = l.getR();
        }
    }

    //6c. Tijdens een verschuiving moet de steen continu in contact blijven met minstens één andere steen
    @Test
    void testGetValidPathThrowsExceptionWhenTilesAreNotConnected() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 2,-1); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 3,-1); // zwart
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 1,0); // wit
        hiveGame.play(Hive.Tile.BEETLE, 3,0); // zwart
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 1,1); // wit
        HiveInsectBeetle hiveInsectBeetle = new HiveInsectBeetle(hiveGame);
        assertThrows(IllegalMoveBeetle.class, ()->{
            ArrayList<HiveLocation> validPath = hiveInsectBeetle.getValidPath(3,0,2,1);
        });
    }
}