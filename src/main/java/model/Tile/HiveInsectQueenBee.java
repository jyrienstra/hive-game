package model.Tile;

import model.*;
import model.Tile.Exceptions.IllegalMoveQueenBee;

import java.util.ArrayList;

/**
 * a. De bijenkoningin verplaatst zich door precies één keer te verschuiven.
 * b. De bijenkoningin mag alleen verplaatst worden naar een leeg veld.
 */
public class HiveInsectQueenBee implements HiveInsect {
    HiveGame hiveGame;
    HiveBoard hiveBoard;

    public HiveInsectQueenBee(HiveGame hiveGame, HiveBoard hiveBoard){
        this.hiveGame = hiveGame;
        this.hiveBoard = hiveBoard;
    }

    @Override
    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR) throws IllegalMoveQueenBee {
        HiveCell toCell = hiveBoard.getCellAt(toQ, toR);
        if (toCell != null && !toCell.getPlayerTilesAtCell().isEmpty()) throw new IllegalMoveQueenBee("The QUEEN_BEE can only be moved to an empty cell");

        ArrayList<HiveLocation> validPath  = getValidPath(fromQ, fromR, toQ, toR, 1);
        if (validPath == null) throw new IllegalMoveQueenBee("Could not find a valid path for the QueenBee to Q = " + toQ + " and R = " + toR);

        return validPath;
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
            HiveLocation toLocation = new HiveLocation(currToQ, currToR);
            if (!path.contains(toLocation)) {
                if (findValidPath(currFromQ, currFromR, currToQ, currToR, endQ, endR, maxCellMove, path) != null) return path;
                path.remove(new HiveLocation(currToQ, currToR));
            }
        }
        return null;
    }
}
