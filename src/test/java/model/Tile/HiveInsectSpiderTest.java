package model.Tile;

import javafx.scene.control.Cell;
import model.*;
import model.Tile.Exceptions.IllegalMoveSpider;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * a Een spin verplaatst zich door precies drie keer te verschuiven.
 * b Een spin mag zich niet verplaatsen naar het veld waar hij al staat.
 * c Een spin mag alleen verplaatst worden over en naar lege velden.
 * d Een spin mag tijdens zijn verplaatsing geen stap maken naar een veld waar hij tijdens de verplaatsing al is geweest.
 */
class HiveInsectSpiderTest {
    // a Een spin verplaatst zich door precies drie keer te verschuiven.
    @Test
    void getValidPathContainsExpectedPath() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SPIDER, 2, 0); // zwart
        HiveInsectSpider hiveInsectSpider = new HiveInsectSpider(hiveGame);
        ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(0,1,3,0);
        assertTrue(validPath.size() == 3);
        assertTrue(validPath.get(0).equals(new HiveLocation(1,1)));
        assertTrue(validPath.get(1).equals(new HiveLocation(2,1)));
        assertTrue(validPath.get(2).equals(new HiveLocation(3,0)));
    }

    // b Een spin mag zich niet verplaatsen naar het veld waar hij al staat.
    @Test
    void testGetValidMoveThrowsExceptionWhenShiftingToFieldWhereSpiderIsAt() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SPIDER, 2, 0); // zwart
        HiveInsectSpider hiveInsectSpider = new HiveInsectSpider(hiveGame);
        assertThrows(IllegalMoveSpider.class, ()->{
            ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(-1,2,2,0); // precies 3 schuiven maar cel bevat spider
        });
    }

    // c Een spin mag alleen verplaatst worden over en naar lege velden.
    // d Een spin mag tijdens zijn verplaatsing geen stap maken naar een veld waar hij tijdens de verplaatsing al is geweest.
    @Test
    void testGetValidMoveThrowsExceptionWhenSpiderIsShiftedOnCellThatIsNotEmptyAndNotADuplicateVisit1() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SPIDER, 2, 0); // zwart
        HiveInsectSpider hiveInsectSpider = new HiveInsectSpider(hiveGame);
        ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(0,1,-2,1);
        HashSet<HiveLocation> visited = new HashSet<>();
        for(HiveLocation l : validPath){
            HiveCell c = hiveGame.getBoard().getCellAt(l.getQ(), l.getR());
            assertTrue(!visited.contains(l));
            assertTrue(c.getPlayerTilesAtCell().size() == 0);
            visited.add(l);
        }
    }

    // c Een spin mag alleen verplaatst worden over en naar lege velden.
    // d Een spin mag tijdens zijn verplaatsing geen stap maken naar een veld waar hij tijdens de verplaatsing al is geweest.
    @Test
    void testGetValidMoveThrowsExceptionWhenSpiderIsShiftedOnCellThatIsNotEmptyAndNotADuplicateVisit2() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SPIDER, 2, 0); // zwart
        HiveInsectSpider hiveInsectSpider = new HiveInsectSpider(hiveGame);
        ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(0,-1,3,-1);
        HashSet<HiveLocation> visited = new HashSet<>();
        for(HiveLocation l : validPath){
            HiveCell c = hiveGame.getBoard().getCellAt(l.getQ(), l.getR());
            assertTrue(!visited.contains(l));
            assertTrue(c.getPlayerTilesAtCell().size() == 0);
            visited.add(l);
        }
    }

    // Test of alle shifts vanuit getValidPath wel validShifts zijn, sinds de spin verplaatst door te schuiven
    @Test
    void testGetValidPathContainsValidShifts() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0, 0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SPIDER, 2, 0); // zwart
        HiveInsectSpider hiveInsectSpider = new HiveInsectSpider(hiveGame);
        ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(0, -1, 3, -1);
        int fromQ = 0;
        int fromR = -1;
        for(HiveLocation l : validPath){
            int toQ = l.getQ();
            int toR = l.getR();
            assertTrue(hiveGame.getBoard().isValidShift(fromQ, fromR, toQ, toR));
            fromQ = l.getQ();
            fromR = l.getR();
        }
    }
}