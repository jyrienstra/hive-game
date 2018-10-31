package model.Tile;

import model.Hive;
import model.HiveBoard;
import model.HiveGame;
import model.HiveLocation;
import model.Tile.Exceptions.IllegalMoveQueenBee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * a. De bijenkoningin verplaatst zich door precies één keer te verschuiven.
 * b. De bijenkoningin mag alleen verplaatst worden naar een leeg veld.
 */
class HiveInsectQueenBeeTest {
    // a. De bijenkoningin verplaatst zich door precies één keer te verschuiven.
    @Test
    void testGetValidMoveThrowsExceptionWhenShiftingMoreThanOnce() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectQueenBee hiveInsectQueenBee = new HiveInsectQueenBee(hiveGame);
        assertThrows(IllegalMoveQueenBee.class, ()->{
            ArrayList<HiveLocation> validPath = hiveInsectQueenBee.getValidPath(0,0,2,-2);
        });
    }

    // b. De bijenkoningin mag alleen verplaatst worden naar een leeg veld.
    @Test
    void testGetvalidMoveThrowsExceptionWhenShiftingToFieldThatIsNotEmpty() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectQueenBee hiveInsectQueenBee = new HiveInsectQueenBee(hiveGame);
        assertThrows(IllegalMoveQueenBee.class, ()-> {
                    ArrayList<HiveLocation> validPath1 = hiveInsectQueenBee.getValidPath(0, 0, 1, 0);
        });
        assertThrows(IllegalMoveQueenBee.class, ()-> {
            ArrayList<HiveLocation> validPath2 = hiveInsectQueenBee.getValidPath(0,0,-1,1);
        });
    }

    // Test of alle shifts vanuit getValidPath wel validShifts zijn, sinds de Queenbee zich verplaatst door te schuiven
    @Test
    void testGetValidPathContainsValidShifts() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 2, -1); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1,0); // zwart
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 3, -1); // wit
        hiveGame.play(Hive.Tile.BEETLE, 1, 1); // zwart
        HiveInsectQueenBee hiveInsectQueenBee = new HiveInsectQueenBee(hiveGame);
        assertThrows(IllegalMoveQueenBee.class, ()->{
            // Geen valide shift terwijl queen bee regels wel goed zijn, dus shiften kan niet
            ArrayList<HiveLocation> validPath = hiveInsectQueenBee.getValidPath(1,0,2,0);
        });
    }

    // Test of alle shifts vanuit getValidPath wel validShifts zijn, sinds de Queenbee zich verplaatst door te schuiven
    @Test
    void testGetValidPathContainsValidShifts2() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.BEETLE, 3, -1); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 3, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, 2, -1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 1); // zwart
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // wit
        HiveInsectQueenBee hiveInsectQueenBee = new HiveInsectQueenBee(hiveGame);
        assertThrows(IllegalMoveQueenBee.class, ()->{
           // Steen komt los wanneer we dit doen
            ArrayList<HiveLocation> validPath = hiveInsectQueenBee.getValidPath(1,0,1,1);
        });
        assertTrue(!hiveGame.getBoard().isValidShift(1,0,1,1));
    }
}