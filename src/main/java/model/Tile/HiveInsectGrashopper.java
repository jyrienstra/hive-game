package model.Tile;

import model.*;

import java.util.ArrayList;

/**
 * Een sprinkhaan verplaatst zich door in een rechte lijn een sprong te maken naar een veld meteen achter een andere steen in de richting van de sprong.
 * Een sprinkhaan mag zich niet verplaatsen naar het veld waar hij al staat.
 * Een sprinkhaan moet over minimaal één steen springen.
 * Een sprinkhaan mag niet naar een bezet veld springen.
 * Een sprinkhaan mag niet over lege velden springen. Dit betekent dat alle velden tussen de start- en eindpositie bezet moeten zijn.
 */
public class HiveInsectGrashopper implements HiveInsect {
    HiveGame hiveGame;
    HiveBoard hiveBoard;

    public HiveInsectGrashopper(HiveGame hiveGame, HiveBoard hiveBoard){
        this.hiveGame = hiveGame;
        this.hiveBoard = hiveBoard;
    }

    @Override
    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR) throws Hive.IllegalMove {
        HiveCell toCell = hiveBoard.getCellAt(toQ, toR);
        if (toCell != null && !toCell.getTopPlayerTileFromCell().getTile().equals(Hive.Tile.GRASSHOPPER)) throw new Hive.IllegalMove("11b Een sprinkhaan mag zich niet verplaatsen naar het veld waar hij al staat.");
        if (toCell != null && toCell.getPlayerTilesAtCell().size() > 0) throw new Hive.IllegalMove("11d Een sprinkhaan mag niet naar een bezet veld springen.");

        ArrayList<HiveLocation> validPath  = getValidPath(fromQ, fromR, toQ, toR, Integer.MAX_VALUE);
        if (validPath == null) throw new Hive.IllegalMove("Could not find a valid path for the Grashopper to Q = " + toQ + " and R = " + toR);

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
        if (!path.isEmpty() && !pathIsFilledWithTiles(path)) // 11e Alle velden tussen de start- en eindpositie bezet moeten zijn.

        // Simulate shift
        path.add(new HiveLocation(currToQ, currToR)); // Simulate shift
        currFromQ = currToQ;
        currFromR = currToR;

        if (!pathIsStraightLine(path)) return null; // 11a Een sprinkhaan verplaatst zich door in een rechte lijn een sprong te maken
        HiveCell currCell = hiveBoard.getCellAt(currToQ, currToR);

        if (currFromQ == endQ && currFromR == endR) {
            if (path.size() < 2) return null; // 11c Een sprinkhaan moet over minimaal één steen springen.
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

    /**
     * Checks if the path is a straight line by checking direction of the current
     * and next location l[i] and l[i + 1]. If somewhere in this process the old
     * direction does not match the new direction the list does not contain a
     * straight path to the destination.
     * @param path
     * @return
     */
    public boolean pathIsStraightLine(ArrayList<HiveLocation> path){
        if (path.isEmpty()) return false;
        if (path.size() == 1) return true;
        String oldDirection = null;
        for(int i = 0; i < path.size() - 1; i++){
            String newDirection = hiveBoard.getDirectionAsString(path.get(i).getQ(), path.get(i).getR(), path.get(i + 1).getQ(), path.get(i + 1).getR());
            if (newDirection == null) return false;
            if (oldDirection == null) oldDirection = newDirection;
            if (!oldDirection.equals(newDirection)) return false;
        }
        return true;
    }


    public boolean pathIsFilledWithTiles(ArrayList<HiveLocation> path){
        HiveCell currCell = null;
        for(HiveLocation l : path){
            currCell = hiveBoard.getCellAt(l.getQ(), l.getR());
            if (currCell == null) return false;
            if (currCell.getPlayerTilesAtCell().isEmpty()) return false;
        }
        return true;
    }
}
