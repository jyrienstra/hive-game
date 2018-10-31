package model;

import model.Tile.HiveInsectBeetle;
import model.Tile.HiveInsectGrasshopper;
import org.junit.jupiter.api.Test;
import sun.security.ssl.HandshakeInStream;

import static org.junit.jupiter.api.Assertions.*;

class HivePlayerTileTest {
    @Test
    void testGetAndSetTile(){
        HiveGame hiveGame = new HiveGame();
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.BLACK);
        HivePlayerTile hivePlayerTile = new HivePlayerTile(hivePlayer, new HiveInsectBeetle(hiveGame));
        assertTrue(hivePlayerTile.getInsect().getTile() == Hive.Tile.BEETLE);
        hivePlayerTile.setInsect(new HiveInsectGrasshopper(hiveGame));
        assertTrue(hivePlayerTile.getInsect().getTile() == Hive.Tile.GRASSHOPPER);
    }

    @Test
    void testGetAndSetPlayer(){
        HiveGame hiveGame = new HiveGame();
        HiveBoard hiveBoard = hiveGame.getBoard();
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.WHITE);
        HivePlayerTile hivePlayerTile = new HivePlayerTile(hivePlayer, new HiveInsectGrasshopper(hiveGame));
        assertTrue(hivePlayerTile.getPlayer() == hivePlayer);
        HivePlayer hivePlayer2 = new HivePlayer(Hive.Player.BLACK);
        hivePlayerTile.setPlayer(hivePlayer2);
        assertTrue(hivePlayerTile.getPlayer() == hivePlayer2);
    }
}