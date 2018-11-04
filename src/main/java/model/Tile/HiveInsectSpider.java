package model.Tile;

import model.*;
import model.Tile.Exceptions.IllegalMoveSpider;

import java.util.ArrayList;
import java.util.Stack;

/**
 * a Een spin verplaatst zich door precies drie keer te verschuiven.
 * b Een spin mag zich niet verplaatsen naar het veld waar hij al staat.
 * c Een spin mag alleen verplaatst worden over en naar lege velden.
 * d Een spin mag tijdens zijn verplaatsing geen stap maken naar een veld waar hij tijdens de verplaatsing al is geweest.
 */
public class HiveInsectSpider implements HiveInsect {
    HiveGame hiveGame;

    public HiveInsectSpider(HiveGame hiveGame){
        this.hiveGame = hiveGame;
    }

    @Override
    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR) throws IllegalMoveSpider {
        ArrayList<HiveLocation> validPath  = getValidPath(fromQ, fromR, toQ, toR, 3);
        if (validPath == null) throw new IllegalMoveSpider("Could not find a valid path for the Spider Ant to Q = " + toQ + " and R = " + toR);
        return validPath;
    }

    @Override
    public Hive.Tile getTile() {
        return Hive.Tile.SPIDER;
    }


    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR, int maxCellMove) {
        ArrayList<HiveLocation> neighbours = hiveGame.getBoard().getNeighbourLocations(fromQ, fromR);
        ArrayList<HiveLocation> validPath = null;
        for(HiveLocation n: neighbours){
            HiveBoard copyBoard = hiveGame.getBoard().clone();
            validPath = findValidPath(fromQ, fromR, n.getQ(), n.getR(), toQ, toR, maxCellMove, new ArrayList<>(), copyBoard);
            if (validPath != null) return validPath;
        }
        return validPath;
    }

    public ArrayList<HiveLocation> findValidPath(int currFromQ, int currFromR, int currToQ, int currToR, int endQ, int endR, int maxCellMove, ArrayList<HiveLocation> path, HiveBoard copyBoard) {
        Stack<HivePlayerTile> tilesAtCurrentLocation = copyBoard.getPlayerTilesAt(currToQ, currToR);

        if (tilesAtCurrentLocation.size() > 0) return null; // 10b 10c Een spin mag zich niet verplaatsen naar het veld waar hij al staat. Een spin mag alleen verplaatst worden over en naar lege velden.
        if (path.contains(new HiveLocation(currToQ, currToR))) return null; // 10d Een spin mag tijdens zijn verplaatsing geen stap maken naar een veld waar hij tijdens de verplaatsing al is geweest.

        if (copyBoard.isValidShift(currFromQ, currFromR, currToQ, currToR)){
            path.add(new HiveLocation(currToQ, currToR)); // Simulate shift
            copyBoard.makeMove(currFromQ, currFromR, currToQ, currToR);
            currFromQ = currToQ;
            currFromR = currToR;
        }else{
            return null;
        }
        if (currFromQ == endQ && currFromR == endR) {
            if (path.size() != 3) return null; // 10a Een spin verplaatst zich door precies drie keer te verschuiven
            return path;
        }
        if (path.size() >= maxCellMove) return null;

        ArrayList<HiveLocation> neighbours = copyBoard.getNeighbourLocations(currFromQ, currFromR);
        for(HiveLocation n: neighbours){
            currToQ = n.getQ();
            currToR = n.getR();
            HiveLocation toLocation = new HiveLocation(currToQ, currToR);
            if (!path.contains(toLocation)) {
                if (findValidPath(currFromQ, currFromR, currToQ, currToR, endQ, endR, maxCellMove, path, copyBoard) != null) return path;
                if (path.contains(new HiveLocation(currToQ, currToR))){
                    path.remove(new HiveLocation(currToQ, currToR));
                    copyBoard.undoMove(currFromQ, currFromR, currToQ, currToR);
                }
            }
        }
        return null;
    }
}
