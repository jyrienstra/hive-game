package model.Tile;

import javafx.scene.control.Cell;
import model.*;
import model.Tile.Exceptions.IllegalMoveSpider;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

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
    void getValidPathContainsExpectedPath1() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SPIDER, 2, 0); // zwart
        HiveInsectSpider hiveInsectSpider = new HiveInsectSpider(hiveGame);
        ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(2,0,-1,2);
        assertTrue(validPath.size() == 3);
        assertTrue(validPath.get(0).equals(new HiveLocation(1,1)));
        assertTrue(validPath.get(1).equals(new HiveLocation(0,1)));
        assertTrue(validPath.get(2).equals(new HiveLocation(-1,2)));
    }

    @Test
    void getValidPathContainsExpectedPath2() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SPIDER, 2, 0); // zwart
        HiveInsectSpider hiveInsectSpider = new HiveInsectSpider(hiveGame);
        ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(2,0,0,-1);
        assertTrue(validPath.size() == 3);
        assertTrue(validPath.get(0).equals(new HiveLocation(2,-1)));
        assertTrue(validPath.get(1).equals(new HiveLocation(1,-1)));
        assertTrue(validPath.get(2).equals(new HiveLocation(0,-1)));
    }

    // b Een spin mag zich niet verplaatsen naar het veld waar hij al staat.
    @Test
    void testGetValidMoveThrowsExceptionWhenShiftingToFieldWhereSpiderIsAt() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SPIDER, 2, 0); // zwart
        hiveGame.play(Hive.Tile.SPIDER, -1, 0); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 1); // zwart
        HiveInsectSpider hiveInsectSpider = new HiveInsectSpider(hiveGame);
        assertThrows(IllegalMoveSpider.class, ()->{
            ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(2,0,2,0); // schuift maar 1 x
        });
        assertThrows(IllegalMoveSpider.class, ()->{
            ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(2,1,2,0); // precies 3 schuiven maar cel bevat spider
        });
        assertThrows(IllegalMoveSpider.class, ()->{
            ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(2,1,1,0); // precies 3 schuiven maar cel bevat al een andere tile(niet spider maar queen bee)
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
        ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(2,0,0,-1);
        HashSet<HiveLocation> visited = new HashSet<>();
        for(HiveLocation l : validPath){
            Stack<HivePlayerTile> tilesAtLocation = hiveGame.getBoard().getPlayerTilesAt(l.getQ(), l.getR());
            assertTrue(!visited.contains(l));
            assertTrue(tilesAtLocation.size() == 0);
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
        ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(2,0,-1,2);
        HashSet<HiveLocation> visited = new HashSet<>();
        for(HiveLocation l : validPath){
            Stack<HivePlayerTile> tilesAtLocation = hiveGame.getBoard().getPlayerTilesAt(l.getQ(), l.getR());
            assertTrue(!visited.contains(l));
            assertTrue(tilesAtLocation.size() == 0);
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
        ArrayList<HiveLocation> validPath = hiveInsectSpider.getValidPath(2, 0, 0, -1);
        int fromQ = 2;
        int fromR = 0;
        for(HiveLocation l : validPath){
            int toQ = l.getQ();
            int toR = l.getR();
            // doet niet daadwerkelijk de move dus halve waarheid
            assertTrue(hiveGame.getBoard().isValidShift(fromQ, fromR, toQ, toR));
            fromQ = l.getQ();
            fromR = l.getR();
        }
    }
}