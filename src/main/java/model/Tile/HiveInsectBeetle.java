package model.Tile;

import model.*;
import model.Tile.Exceptions.IllegalMoveBeetle;

import java.util.ArrayList;

/**
 * Een kever verplaatst zich door precies één keer te verschuiven.
 */
public class HiveInsectBeetle implements HiveInsect{
    HiveGame hiveGame;
    HiveBoard hiveBoard;

    public HiveInsectBeetle(HiveGame hiveGame, HiveBoard hiveBoard){
        this.hiveGame = hiveGame;
        this.hiveBoard = hiveBoard;
    }

    @Override
    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR) throws IllegalMoveBeetle {
        ArrayList<HiveLocation> validPath  = getValidPath(fromQ, fromR, toQ, toR, 1);
        if (validPath == null) throw new IllegalMoveBeetle("Could not find a valid path for the Beetle to Q = " + toQ + " and R = " + toR);

        return validPath;
    }

    private ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR, int maxCellMove) {
        ArrayList<HiveLocation> neighbours = hiveBoard.getNeighbourLocations(fromQ, fromR);
        ArrayList<HiveLocation> validPath = null;
        for(HiveLocation n: neighbours){
            if (validPath != null) return validPath;
            validPath = findValidPath(fromQ, fromR, n.getQ(), n.getR(), toQ, toR, maxCellMove, new ArrayList<>());
        }
        return validPath;
    }

    private ArrayList<HiveLocation> findValidPath(int currFromQ, int currFromR, int currToQ, int currToR, int endQ, int endR, int maxCellMove, ArrayList<HiveLocation> path) {
        if (hiveGame.isValidShift(currFromQ, currFromR, currToQ, currToR)){
            path.add(new HiveLocation(currToQ, currToR)); // Simulate shift
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
            if(findValidPath(currFromQ, currFromR, currToQ, currToR, endQ, endR, maxCellMove, path) != null) return path;
            path.remove(new HiveLocation(currToQ, currToR));
        }
        return null;
    }
}
