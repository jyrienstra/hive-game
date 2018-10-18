package model.Tile.Exceptions;

import model.Hive;

public class IllegalMoveBeetle extends Hive.IllegalMove {
    public IllegalMoveBeetle(String message) {
        super(message);
    }
}
