package model.Tile;

import model.*;

import java.util.ArrayList;

/**
 * Een spin verplaatst zich door precies drie keer te verschuiven.
 * Een spin mag zich niet verplaatsen naar het veld waar hij al staat.
 * Een spin mag alleen verplaatst worden over en naar lege velden.
 * Een spin mag tijdens zijn verplaatsing geen stap maken naar een veld waar hij tijdens de verplaatsing al is geweest.
 */
public class HiveInsectSpider implements HiveInsect {
    HiveGame hiveGame;
    HiveBoard hiveBoard;

    public HiveInsectSpider(HiveGame hiveGame, HiveBoard hiveBoard){
        this.hiveGame = hiveGame;
        this.hiveBoard = hiveBoard;
    }

    @Override
    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR) throws Hive.IllegalMove {
        ArrayList<HiveLocation> validPath  = getValidPath(fromQ, fromR, toQ, toR, 3);
        if (validPath == null) throw new Hive.IllegalMove("Could not find a valid path for the Spider Ant to Q = " + toQ + " and R = " + toR);
        return validPath;
    }


    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR, int maxCellMove) throws Hive.IllegalMove {
        ArrayList<HiveLocation> neighbours = hiveBoard.getNeighbourLocations(fromQ, fromR);
        ArrayList<HiveLocation> validPath = null;
        for(HiveLocation n: neighbours){
            if (validPath != null) return validPath;
            validPath = findValidPath(fromQ, fromR, n.getQ(), n.getR(), toQ, toR, maxCellMove, new ArrayList<>());
        }
        return validPath;
    }

    public ArrayList<HiveLocation> findValidPath(int currFromQ, int currFromR, int currToQ, int currToR, int endQ, int endR, int maxCellMove, ArrayList<HiveLocation> path) throws Hive.IllegalMove {
        HiveCell currCell = hiveBoard.getCellAt(currToQ, currToR);
        if (currCell.getPlayerTilesAtCell().size() > 0) return null; // 10b 10c Een spin mag zich niet verplaatsen naar het veld waar hij al staat. Een spin mag alleen verplaatst worden over en naar lege velden.
        if (path.contains(new HiveLocation(currToQ, currToR))) return null; // 10d Een spin mag tijdens zijn verplaatsing geen stap maken naar een veld waar hij tijdens de verplaatsing al is geweest.

        if (hiveGame.isValidShift(currFromQ, currFromR, currToQ, currToR)){
            path.add(new HiveLocation(currToQ, currToR)); // Simulate shift
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
