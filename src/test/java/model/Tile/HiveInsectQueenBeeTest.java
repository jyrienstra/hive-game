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
    @Test
    void testGetValidMoveContainsExpectedMove() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        HiveBoard hiveBoard = hiveGame.getBoard();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectQueenBee hiveInsectQueenBee = new HiveInsectQueenBee(hiveGame, hiveBoard);
        ArrayList<HiveLocation> validPath = hiveInsectQueenBee.getValidPath(0,0,1,-1);
        assertTrue(validPath.get(0).equals(new HiveLocation(1,-1)));
    }

    // a. De bijenkoningin verplaatst zich door precies één keer te verschuiven.
    @Test
    void testGetValidMoveThrowsExceptionWhenShiftingMoreThanOnce() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        HiveBoard hiveBoard = hiveGame.getBoard();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectQueenBee hiveInsectQueenBee = new HiveInsectQueenBee(hiveGame, hiveBoard);
        assertThrows(IllegalMoveQueenBee.class, ()->{
            ArrayList<HiveLocation> validPath = hiveInsectQueenBee.getValidPath(0,0,2,-2);
        });
    }

    // b. De bijenkoningin mag alleen verplaatst worden naar een leeg veld.
    @Test
    void testGetvalidMoveThrowsExceptionWhenShiftingToFieldThatIsNotEmpty() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        HiveBoard hiveBoard = hiveGame.getBoard();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectQueenBee hiveInsectQueenBee = new HiveInsectQueenBee(hiveGame, hiveBoard);
        assertThrows(IllegalMoveQueenBee.class, ()-> {
                    ArrayList<HiveLocation> validPath1 = hiveInsectQueenBee.getValidPath(0, 0, 1, 0);
        });
        assertThrows(IllegalMoveQueenBee.class, ()-> {
            ArrayList<HiveLocation> validPath2 = hiveInsectQueenBee.getValidPath(0,0,-1,1);
        });
    }
}