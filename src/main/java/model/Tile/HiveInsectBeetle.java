package model.Tile;

import model.*;
import model.Tile.Exceptions.IllegalMoveBeetle;

import java.util.ArrayList;

/**
 * Een kever verplaatst zich door precies één keer te verschuiven.
 *
 * Een beetle stapt altijd maar één keer we hoeven dus niet echt te gaan schuiven in het bord. isValidShift() controleert dit namelijk.
 * Stel we zouden meerdere keren kunnen schuiven dan moet het bord wel geüpdatet worden.
 */
public class HiveInsectBeetle implements HiveInsect{
    HiveGame hiveGame;

    public HiveInsectBeetle(HiveGame hiveGame){
        this.hiveGame = hiveGame;
    }

    @Override
    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR) throws IllegalMoveBeetle {
        ArrayList<HiveLocation> validPath  = getValidPath(fromQ, fromR, toQ, toR, 1);
        if (validPath == null) throw new IllegalMoveBeetle("Could not find a valid path for the Beetle to Q = " + toQ + " and R = " + toR);

        return validPath;
    }

    @Override
    public Hive.Tile getTile() {
        return Hive.Tile.BEETLE;
    }

    private ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR, int maxCellMove) {
        ArrayList<HiveLocation> neighbours = hiveGame.getBoard().getNeighbourLocations(fromQ, fromR);
        ArrayList<HiveLocation> validPath = null;
        for(HiveLocation n: neighbours){
            if (validPath != null) return validPath;
            validPath = findValidPath(fromQ, fromR, n.getQ(), n.getR(), toQ, toR, maxCellMove, new ArrayList<>());
        }
        return validPath;
    }

    private ArrayList<HiveLocation> findValidPath(int currFromQ, int currFromR, int currToQ, int currToR, int endQ, int endR, int maxCellMove, ArrayList<HiveLocation> path) {
        if (hiveGame.getBoard().isValidShift(currFromQ, currFromR, currToQ, currToR)){
            path.add(new HiveLocation(currToQ, currToR)); // Simulate shift
            currFromQ = currToQ;
            currFromR = currToR;
        }else{
            return null;
        }
        if (currFromQ == endQ && currFromR == endR) return path;
        if (path.size() >= maxCellMove) return null;

        ArrayList<HiveLocation> neighbours = hiveGame.getBoard().getNeighbourLocations(currFromQ, currFromR);
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
