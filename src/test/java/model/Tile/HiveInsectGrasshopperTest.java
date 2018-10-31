package model.Tile;

import model.Hive;
import model.HiveBoard;
import model.HiveGame;
import model.HiveLocation;
import model.Tile.Exceptions.IllegalMoveGrasshopper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * a Een sprinkhaan verplaatst zich door in een rechte lijn een sprong te maken naar een veld meteen achter een andere steen in de richting van de sprong.
 * b Een sprinkhaan mag zich niet verplaatsen naar het veld waar hij al staat.
 * c Een sprinkhaan moet over minimaal één steen springen.
 * d Een sprinkhaan mag niet naar een bezet veld springen.
 * e Een sprinkhaan mag niet over lege velden springen. Dit betekent dat alle velden tussen de start- en eindpositie bezet moeten zijn.
 */
class HiveInsectGrasshopperTest {

    // Valide pad verwachten we een pad zonder tussenstappen (sinds een sprong wordt gemaakt en geen shifts)
    @Test
    void testGetValidMoveReturnsTheExpectedMoveWhenMakingAValidMove() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        HiveInsectGrasshopper hiveInsectGrasshopper = new HiveInsectGrasshopper(hiveGame);
        ArrayList<HiveLocation> validPath = hiveInsectGrasshopper.getValidPath(-1,1,1,-1);
        assertTrue(validPath.size() == 1);
        assertTrue(validPath.get(0).equals(new HiveLocation(1, -1)));
    }

    // a Een sprinkhaan verplaatst zich door in een rechte lijn een sprong te maken naar een veld meteen achter een andere steen in de richting van de sprong.
    @Test
    void testGetValidMoveThrowsExceptionWhenAStraightJumpFromTheHopperIsNotPossibleToCell() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        hiveGame.play(Hive.Tile.SPIDER, -2, 2); // wit
        HiveInsectGrasshopper hiveInsectGrasshopper = new HiveInsectGrasshopper(hiveGame);
        assertThrows(IllegalMoveGrasshopper.class, ()-> {
            ArrayList<HiveLocation> validPath1 = hiveInsectGrasshopper.getValidPath(-2,2,0,1);
        });
        assertThrows(IllegalMoveGrasshopper.class, ()-> {
            ArrayList<HiveLocation> validPath2 = hiveInsectGrasshopper.getValidPath(-2,2,-1,0);
        });
        ArrayList<HiveLocation> validPath3 = hiveInsectGrasshopper.getValidPath(-2,2,1,-1);
        assertTrue(validPath3.get(0).equals(new HiveLocation(1, -1)));
    }

     // c Een sprinkhaan moet over minimaal één steen springen.
     @Test
     void testGetValidMoveThrowsExceptionWhenTheJumpIsNotOverAtLeastOneTile() throws Hive.IllegalMove {
         HiveGame hiveGame = new HiveGame();
         hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
         hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
         hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
         hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
         hiveGame.play(Hive.Tile.SPIDER, -2, 2); // wit
         HiveInsectGrasshopper hiveInsectGrasshopper = new HiveInsectGrasshopper(hiveGame);
         assertThrows(IllegalMoveGrasshopper.class, ()->{
             ArrayList<HiveLocation> validPath1 = hiveInsectGrasshopper.getValidPath(0,1,1,1); // een cel verder
         });
     }

     // e Een sprinkhaan mag niet over lege velden springen. Dit betekent dat alle velden tussen de start- en eindpositie bezet moeten zijn.
     @Test
     void testGetValidMoveThrowsExceptionWhenTheJumpContainsEmptyFieldInBetween() throws Hive.IllegalMove {
         HiveGame hiveGame = new HiveGame();
         hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
         hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
         hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
         hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
         hiveGame.play(Hive.Tile.SPIDER, -2, 2); // wit
         HiveInsectGrasshopper hiveInsectGrasshopper = new HiveInsectGrasshopper(hiveGame);
         assertThrows(IllegalMoveGrasshopper.class, ()->{
             ArrayList<HiveLocation> validPath2 = hiveInsectGrasshopper.getValidPath(0,1,2,1); // 2 cel verder maar er is een lege cell ertussen
         });
     }

    // b Een sprinkhaan mag zich niet verplaatsen naar het veld waar hij al staat.
    // d Een sprinkhaan mag niet naar een bezet veld springen.
    @Test
    void testGetValidMoveThrowsExceptionWhenToFieldIsNotEmpty() throws Hive.IllegalMove {
        HiveGame hiveGame = new HiveGame();
        hiveGame.play(Hive.Tile.QUEEN_BEE, 0,0); // wit
        hiveGame.play(Hive.Tile.QUEEN_BEE, 1, 0); // zwart
        hiveGame.play(Hive.Tile.BEETLE, -1, 1); // wit
        hiveGame.play(Hive.Tile.SOLDIER_ANT, 2, 0); // zwart
        hiveGame.play(Hive.Tile.SPIDER, -2, 2); // wit
        HiveInsectGrasshopper hiveInsectGrasshopper = new HiveInsectGrasshopper(hiveGame);
        assertThrows(IllegalMoveGrasshopper.class, ()-> {
            ArrayList<HiveLocation> validPath = hiveInsectGrasshopper.getValidPath(-2,2,0,0);
        });
        assertThrows(IllegalMoveGrasshopper.class, ()-> {
            ArrayList<HiveLocation> validPath = hiveInsectGrasshopper.getValidPath(0,0,2,0);
        });
    }
}