package model;

import model.Tile.HiveInsect;

public class HivePlayerTile {
    private HiveInsect insect;
    private HivePlayer player;

    public HivePlayerTile(HivePlayer player, HiveInsect insect){
        this.insect = insect;
        this.player = player;
    }

    public HiveInsect getInsect() {
        return insect;
    }

    public void setInsect(HiveInsect insect) {
        this.insect = insect;
    }

    public HivePlayer getPlayer() {
        return player;
    }

    public void setPlayer(HivePlayer player) {
        this.player = player;
    }
}
