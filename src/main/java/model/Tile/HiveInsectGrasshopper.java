package model.Tile;

import model.*;
import model.Tile.Exceptions.IllegalMoveGrasshopper;

import java.util.ArrayList;

/**
 * Een sprinkhaan verplaatst zich door in een rechte lijn een sprong te maken naar een veld meteen achter een andere steen in de richting van de sprong.
 * Een sprinkhaan mag zich niet verplaatsen naar het veld waar hij al staat.
 * Een sprinkhaan moet over minimaal één steen springen.
 * Een sprinkhaan mag niet naar een bezet veld springen.
 * Een sprinkhaan mag niet over lege velden springen. Dit betekent dat alle velden tussen de start- en eindpositie bezet moeten zijn.
 */
public class HiveInsectGrasshopper implements HiveInsect {
    HiveGame hiveGame;

    public HiveInsectGrasshopper(HiveGame hiveGame){
        this.hiveGame = hiveGame;
    }

    @Override
    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR) throws IllegalMoveGrasshopper {
        HiveCell toCell = hiveGame.getBoard().getCellAt(toQ, toR);
        if (toCell != null && toCell.getPlayerTilesAtCell().size() > 0 && !toCell.getTopPlayerTileFromCell().getInsect().getTile().equals(Hive.Tile.GRASSHOPPER)) throw new IllegalMoveGrasshopper("11b Een sprinkhaan mag zich niet verplaatsen naar het veld waar hij al staat.");
        if (toCell != null && toCell.getPlayerTilesAtCell().size() > 0) throw new IllegalMoveGrasshopper("11d Een sprinkhaan mag niet naar een bezet veld springen.");

        ArrayList<HiveLocation> validPath  = getValidPath(fromQ, fromR, toQ, toR, Integer.MAX_VALUE);
        if (validPath == null) throw new IllegalMoveGrasshopper("Could not find a valid path for the Grashopper to Q = " + toQ + " and R = " + toR);

        // Alle cellen tussen laatste cel in hoeven niet terug gegeven te worden sinds we niet schuiven maar springen met een grasshopper
        // We geven de laatste cel specifiek terug ipv de toQ, toR hierboven in de functie zodat we in tests kunnen controleren of het gevonden pad wel klopt
        ArrayList<HiveLocation> movesToqToR = new ArrayList<>();
        HiveLocation shouldBeEndCell = validPath.get(validPath.size() - 1);
        movesToqToR.add(shouldBeEndCell);
        return movesToqToR;
    }

    @Override
    public Hive.Tile getTile() {
        return Hive.Tile.GRASSHOPPER;
    }

    public ArrayList<HiveLocation> getValidPath(int fromQ, int fromR, int toQ, int toR, int maxCellMove) {
        ArrayList<HiveLocation> neighbours = hiveGame.getBoard().getNeighbourLocations(fromQ, fromR);
        ArrayList<HiveLocation> validPath = null;
        for(HiveLocation n: neighbours){
            if (validPath != null) return validPath;
            validPath = findValidPath(fromQ, fromR, n.getQ(), n.getR(), fromQ, fromR, toQ, toR, maxCellMove, new ArrayList<>());
        }
        return validPath;
    }

    public ArrayList<HiveLocation> findValidPath(int currFromQ, int currFromR, int currToQ, int currToR, int startQ, int startR, int endQ, int endR, int maxCellMove, ArrayList<HiveLocation> path) {
        if (!hiveGame.getBoard().isPathFilledWithTiles(path)) return null; // 11e Alle velden tussen de start- en eindpositie bezet moeten zijn.

        // Simulate shift
        path.add(new HiveLocation(currToQ, currToR)); // Simulate shift
        currFromQ = currToQ;
        currFromR = currToR;

        if (!hiveGame.getBoard().isPathStraightLine(startQ, startR, path)) return null; // 11a Een sprinkhaan verplaatst zich door in een rechte lijn een sprong te maken

        if (currFromQ == endQ && currFromR == endR) {
            if (path.size() < 2) return null; // 11c Een sprinkhaan moet over minimaal één steen springen.
            return path;
        }
        if (path.size() >= maxCellMove) return null;

        ArrayList<HiveLocation> neighbours = hiveGame.getBoard().getNeighbourLocations(currFromQ, currFromR);
        for(HiveLocation n: neighbours){
            currToQ = n.getQ();
            currToR = n.getR();
            HiveLocation toLocation = new HiveLocation(currToQ, currToR);
            if (!path.contains(toLocation)) {
                if (findValidPath(currFromQ, currFromR, currToQ, currToR, startQ, startR, endQ, endR, maxCellMove, path) != null) return path;
                path.remove(new HiveLocation(currToQ, currToR));
            }
        }
        return null;
    }
}
