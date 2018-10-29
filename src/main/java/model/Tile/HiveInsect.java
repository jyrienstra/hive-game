package model.Tile;

import model.Hive;
import model.HiveLocation;

import java.util.ArrayList;

public interface HiveInsect {
    ArrayList<HiveLocation> move(int fromQ, int fromR, int toQ, int toR) throws Hive.IllegalMove;

    Hive.Tile getTile();
}
