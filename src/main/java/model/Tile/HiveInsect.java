package model.Tile;

import model.Hive;
import model.HiveLocation;

import java.util.ArrayList;

public interface HiveInsect {
    ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR) throws Hive.IllegalMove;

    Hive.Tile getTile();
}
