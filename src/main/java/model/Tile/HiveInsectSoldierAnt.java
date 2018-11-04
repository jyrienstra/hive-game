package model.Tile;


import model.*;
import model.Tile.Exceptions.IllegalMoveSoldierAnt;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Een soldatenmier verplaatst zich door een onbeperkt aantal keren te verschuiven.
 * Een soldatenmier mag zich niet verplaatsen naar het veld waar hij al staat.
 * Een soldatenmier mag alleen verplaatst worden over en naar lege velden.
 */
public class HiveInsectSoldierAnt implements HiveInsect {
    HiveGame hiveGame;

    public HiveInsectSoldierAnt(HiveGame hiveGame){
        this.hiveGame = hiveGame;
    }

    @Override
    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR) throws IllegalMoveSoldierAnt {
        ArrayList<HiveLocation> validPath  = getValidPath(fromQ, fromR, toQ, toR, Integer.MAX_VALUE);

        if (validPath == null) throw new IllegalMoveSoldierAnt("Could not find a valid path for the Soldier Ant to Q = " + toQ + " and R = " + toR);


        for(HiveLocation location: validPath){
            System.out.println("pad"+ location.getQ() +"," + location.getR());
        }

        return validPath;
    }

    @Override
    public Hive.Tile getTile() {
        return Hive.Tile.SOLDIER_ANT;
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
        if (tilesAtCurrentLocation.size() > 0) return null; //Een soldatenmier mag zich niet verplaatsen naar het veld waar hij al staat. Een soldatenmier mag alleen verplaatst worden over en naar lege velden.

        if (copyBoard.isValidShift(currFromQ, currFromR, currToQ, currToR)){
            path.add(new HiveLocation(currToQ, currToR)); // Simulate shift
            copyBoard.makeMove(currFromQ, currFromR, currToQ, currToR);
            currFromQ = currToQ;
            currFromR = currToR;
        }else{
            return null;
        }
        if (currFromQ == endQ && currFromR == endR) return path;
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
                    System.out.println();
                }
            }
        }
        return null;
    }
}
