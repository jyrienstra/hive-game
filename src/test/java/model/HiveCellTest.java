package model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
class HiveCellTest {
    @Test
    void testSetCoordinateQ() throws NoSuchFieldException, IllegalAccessException {
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.WHITE);
        HivePlayerTile tile1 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        HiveCell hiveCell = new HiveCell(tile1, 0,-1);
        hiveCell.setCoordinateQ(1);
        final Field field = hiveCell.getClass().getDeclaredField("coordinateQ");
        field.setAccessible(true);
        assertTrue(field.getInt(hiveCell) == 1);
    }

    @Test
    void testSetCoordinateR() throws NoSuchFieldException, IllegalAccessException {
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.WHITE);
        HivePlayerTile tile1 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        HiveCell hiveCell = new HiveCell(tile1,1,-3);
        hiveCell.setCoordinateR(3);
        final Field field = hiveCell.getClass().getDeclaredField("coordinateR");
        field.setAccessible(true);
        assertTrue(field.getInt(hiveCell) == 3);
    }

    @Test
    void testGetCoordinateR() throws NoSuchFieldException, IllegalAccessException {
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.WHITE);
        HivePlayerTile tile1 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        HiveCell hiveCell = new HiveCell(tile1, 2,-3);
        final Field field = hiveCell.getClass().getDeclaredField("coordinateR");
        field.setAccessible(true);
        field.set(hiveCell, 5);
        assertTrue(hiveCell.getCoordinateR() == 5);
    }

    @Test
    void testGetCoordinateQ() throws NoSuchFieldException, IllegalAccessException {
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.WHITE);
        HivePlayerTile tile1 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        HiveCell hiveCell = new HiveCell(tile1,3,-3);
        final Field field = hiveCell.getClass().getDeclaredField("coordinateQ");
        field.setAccessible(true);
        field.set(hiveCell, 7);
        assertTrue(hiveCell.getCoordinateQ() == 7);
    }

    @Test
    void testAddPlayerTileToCell() throws NoSuchFieldException, IllegalAccessException {
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.WHITE);
        HivePlayerTile tile1 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        HiveCell hiveCell = new HiveCell(tile1, -1,-2);
        HivePlayerTile tile2 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        HivePlayerTile tile3 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        hiveCell.addPlayerTile(tile2);
        hiveCell.addPlayerTile(tile3);
        assertTrue(hiveCell.getPlayerTilesAtCell().contains(tile1));
        assertTrue(hiveCell.getPlayerTilesAtCell().contains(tile2));
        assertTrue(hiveCell.getPlayerTilesAtCell().contains(tile3));
    }

    @Test
    void testGetTopTileFromCell(){
        HivePlayer hivePlayer = new HivePlayer(Hive.Player.BLACK);
        HivePlayerTile tile1 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        HiveCell hiveCell = new HiveCell(tile1, 0,-2);
        HivePlayerTile tile2 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        HivePlayerTile tile3 = new HivePlayerTile(hivePlayer, Hive.Tile.GRASSHOPPER);
        hiveCell.addPlayerTile(tile2);
        hiveCell.addPlayerTile(tile3);
        assertTrue(hiveCell.getTopPlayerTileFromCell() == tile3);
    }
}