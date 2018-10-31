package model;

import model.Tile.HiveInsectQueenBee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class HiveBoardTest {
    @Test
    void testGetHiveCells(){
        HiveGame hiveGame = new HiveGame();
        HiveBoard hiveBoard = hiveGame.getBoard();
        HiveInsectQueenBee hiveInsectQueenBee = new HiveInsectQueenBee(hiveGame);
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.WHITE);
        HivePlayerTile hivePlayerTile = new HivePlayerTile(hivePlayer, hiveInsectQueenBee);
        HiveCell hiveCell1 = new HiveCell(hivePlayerTile,0, 0);
        HiveCell hiveCell2 = new HiveCell(hivePlayerTile, -1, 0);
        hiveBoard.addHiveCell(hiveCell1);
        hiveBoard.addHiveCell(hiveCell2);
        assertTrue(hiveBoard.getHiveCells().contains(hiveCell1));
        assertTrue(hiveBoard.getHiveCells().contains(hiveCell2));
    }

    @Test
    void testGetNeighbourLocationsForCell1(){
        HiveBoard hiveBoard = new HiveBoard();
        ArrayList<HiveLocation> n = hiveBoard.getNeighbourLocations(1, -2);
        assertTrue(n.size() == 6);
        int validNeighbours = 0;
        for(HiveLocation l : n){
            if(l.getQ() == 2 && l.getR() == -2) validNeighbours++;
            if(l.getQ() == 1 && l.getR() == -1) validNeighbours++;
            if(l.getQ() == 0 && l.getR() == -1) validNeighbours++;
            if(l.getQ() == 0 && l.getR() == -2) validNeighbours++;
            if(l.getQ() == 1 && l.getR() == -3) validNeighbours++;
            if(l.getQ() == 2 && l.getR() == -3) validNeighbours++;
        }
        assertTrue(validNeighbours == 6);
    }

    @Test
    void testGetNeighbourLocationsForCell2(){
        HiveBoard hiveBoard = new HiveBoard();
        ArrayList<HiveLocation> n = hiveBoard.getNeighbourLocations(0, 1);
        assertTrue(n.size() == 6);
        int validNeighbours = 0;
        for(HiveLocation l : n){
            if(l.getQ() == 0 && l.getR() == 2) validNeighbours++;
            if(l.getQ() == 1 && l.getR() == 1) validNeighbours++;
            if(l.getQ() == 1 && l.getR() == 0) validNeighbours++;
            if(l.getQ() == 0 && l.getR() == 0) validNeighbours++;
            if(l.getQ() == -1 && l.getR() == 1) validNeighbours++;
            if(l.getQ() == -1 && l.getR() == 2) validNeighbours++;
        }
        assertTrue(validNeighbours == 6);
    }

    @Test
    void testGetDirectionAsString(){
        HiveBoard hiveBoard = new HiveBoard();
        // Left
        assertTrue(hiveBoard.getDirection(2,0, 1, 0) == HiveBoard.Direction.LEFT);
        assertTrue(hiveBoard.getDirection(1,-2, 0, -2) == HiveBoard.Direction.LEFT);
        // Left down
        assertTrue(hiveBoard.getDirection(2,0,1,1) == HiveBoard.Direction.LEFT_DOWN);
        assertTrue(hiveBoard.getDirection(-1,0,-2,1) == HiveBoard.Direction.LEFT_DOWN);
        // Left up
        assertTrue(hiveBoard.getDirection(1,-1,1,-2) == HiveBoard.Direction.LEFT_UP);
        assertTrue(hiveBoard.getDirection(-1,0,-1,-1) == HiveBoard.Direction.LEFT_UP);
        // Right
        assertTrue(hiveBoard.getDirection(0,1,1,1) == HiveBoard.Direction.RIGHT);
        assertTrue(hiveBoard.getDirection(1,-2,2,-2) == HiveBoard.Direction.RIGHT);
        // Right down
        assertTrue(hiveBoard.getDirection(1,-1,1,0) == HiveBoard.Direction.RIGHT_DOWN);
        assertTrue(hiveBoard.getDirection(-1,-2,-1,-1) == HiveBoard.Direction.RIGHT_DOWN);
        // Right up
        assertTrue(hiveBoard.getDirection(1,1,2,0) == HiveBoard.Direction.RIGHT_UP);
        assertTrue(hiveBoard.getDirection(0,-2,1,-3) == HiveBoard.Direction.RIGHT_UP);
    }
}