package model.Tile;


import model.*;
import model.Tile.Exceptions.IllegalMoveSoldierAnt;

import java.util.ArrayList;

/**
 * Een soldatenmier verplaatst zich door een onbeperkt aantal keren te verschuiven.
 * Een soldatenmier mag zich niet verplaatsen naar het veld waar hij al staat.
 * Een soldatenmier mag alleen verplaatst worden over en naar lege velden.
 */
public class HiveInsectSoldierAnt implements HiveInsect {
    HiveGame hiveGame;
    HiveBoard hiveBoard;

    public HiveInsectSoldierAnt(HiveGame hiveGame, HiveBoard hiveBoard){
        this.hiveGame = hiveGame;
        this.hiveBoard = hiveBoard;
    }

    @Override
    public ArrayList<HiveLocation> move(int fromQ, int fromR, int toQ, int toR) throws IllegalMoveSoldierAnt {
        ArrayList<HiveLocation> validPath  = getValidPath(fromQ, fromR, toQ, toR, Integer.MAX_VALUE);
        if (validPath == null) throw new IllegalMoveSoldierAnt("Could not find a valid path for the Soldier Ant to Q = " + toQ + " and R = " + toR);

        return validPath;
    }

    @Override
    public Hive.Tile getTile() {
        return Hive.Tile.SOLDIER_ANT;
    }

    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR, int maxCellMove) {
        ArrayList<HiveLocation> neighbours = hiveBoard.getNeighbourLocations(fromQ, fromR);
        ArrayList<HiveLocation> validPath = null;
        for(HiveLocation n: neighbours){
            if (validPath != null) return validPath;
            validPath = findValidPath(fromQ, fromR, n.getQ(), n.getR(), toQ, toR, maxCellMove, new ArrayList<>());
        }
        return validPath;
    }

    public ArrayList<HiveLocation> findValidPath(int currFromQ, int currFromR, int currToQ, int currToR, int endQ, int endR, int maxCellMove, ArrayList<HiveLocation> path) {
        HiveCell currCell = hiveBoard.getCellAt(currToQ, currToR);
        if (currCell.getPlayerTilesAtCell().size() > 0) return null; //Een soldatenmier mag zich niet verplaatsen naar het veld waar hij al staat. Een soldatenmier mag alleen verplaatst worden over en naar lege velden.

        if (hiveGame.isValidShift(currFromQ, currFromR, currToQ, currToR)){
            path.add(new HiveLocation(currToQ, currToR)); // Simulate shift
            hiveGame.makeMove(currFromQ, currFromR, currToQ, currToR);
            currFromQ = currToQ;
            currFromR = currToR;
        }else{
            return null;
        }
        if (currFromQ == endQ && currFromR == endR) return path;
        if (path.size() >= maxCellMove) return null;

        ArrayList<HiveLocation> neighbours = hiveBoard.getNeighbourLocations(currFromQ, currFromR);
        for(HiveLocation n: neighbours){
            currToQ = n.getQ();
            currToR = n.getR();
            HiveLocation toLocation = new HiveLocation(currToQ, currToR);
            if (!path.contains(toLocation)) {
                if (findValidPath(currFromQ, currFromR, currToQ, currToR, endQ, endR, maxCellMove, path) != null) return path;
                if (path.size() > 0 && path.get(path.size()-1).equals(new HiveLocation(currToQ, currToR))){
                    hiveGame.undoMove(currFromQ, currFromR, currToQ, currToR);
                    path.remove(new HiveLocation(currToQ, currToR));
                }
            }
        }
        return null;
    }
}
