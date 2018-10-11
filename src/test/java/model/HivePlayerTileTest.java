package model;

import org.junit.jupiter.api.Test;
import sun.security.ssl.HandshakeInStream;

import static org.junit.jupiter.api.Assertions.*;

class HivePlayerTileTest {
    @Test
    void testGetAndSetTile(){
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.BLACK);
        HivePlayerTile hivePlayerTile = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        assertTrue(hivePlayerTile.getTile() == Hive.Tile.BEETLE);
        hivePlayerTile.setTile(Hive.Tile.GRASSHOPPER);
        assertTrue(hivePlayerTile.getTile() == Hive.Tile.GRASSHOPPER);
    }

    @Test
    void testGetAndSetPlayer(){
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.WHITE);
        HivePlayerTile hivePlayerTile = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        assertTrue(hivePlayerTile.getPlayer() == hivePlayer);
        HivePlayer hivePlayer2 = new HivePlayer(Hive.Player.BLACK);
        hivePlayerTile.setPlayer(hivePlayer2);
        assertTrue(hivePlayerTile.getPlayer() == hivePlayer2);
    }
}