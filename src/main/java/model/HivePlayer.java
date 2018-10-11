package model;

import java.util.ArrayList;

public class HivePlayer {
    private Hive.Player playerColor;
    private ArrayList<HivePlayerTile> playerTiles;
    private ArrayList<HivePlayerTile> tilesPlayed;

    public HivePlayer(Hive.Player playerColor){
        this.playerColor = playerColor;
        this.playerTiles = new ArrayList<HivePlayerTile>();
        this.tilesPlayed = new ArrayList<HivePlayerTile>();
    }

    public Hive.Player getPlayerColor(){
        return playerColor;
    }

    public ArrayList<HivePlayerTile> getPlayerTiles() {
        return playerTiles;
    }

    public HivePlayerTile getTile(Hive.Tile tile){
        for(HivePlayerTile hivePlayerTile : playerTiles){
            if(hivePlayerTile.getTile() == tile) return hivePlayerTile;
        }
        return null;
    }

    public void addPlayerTile(HivePlayerTile playerTile){
        playerTiles.add(playerTile);
    }

    public void removePlayerTile(HivePlayerTile playerTile){
        playerTiles.remove(playerTile);
        tilesPlayed.add(playerTile);
    }

    public boolean hasTile(Hive.Tile tile){
        if (getTile(tile) == null) return false;
        return true;
    }

    public ArrayList<HivePlayerTile> getTilesPlayed(){
        return tilesPlayed;
    }
}
