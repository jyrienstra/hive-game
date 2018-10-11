package model;

public class HivePlayerTile {
    private Hive.Tile tile;
    private HivePlayer player;

    public HivePlayerTile(HivePlayer player, Hive.Tile tile){
        this.tile = tile;
        this.player = player;
    }

    public Hive.Tile getTile() {
        return tile;
    }

    public void setTile(Hive.Tile tile) {
        this.tile = tile;
    }

    public HivePlayer getPlayer() {
        return player;
    }

    public void setPlayer(HivePlayer player) {
        this.player = player;
    }
}
