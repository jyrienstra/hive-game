package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * @todo bord converteren naar key,value hivelocation,stack
 */
public class HiveBoard {
    private ArrayList<HiveCell> hiveCells;

    public HiveBoard(){
        this.hiveCells = new ArrayList<HiveCell>();
    }

    public ArrayList<HiveCell> getHiveCells(){
        return hiveCells;
    }

    public void addHiveCell(HiveCell hiveCell) {
        hiveCells.add(hiveCell);
        addNeighboursToCells();
    }

    /**
     * Checks if there is a tile besides the coordinate q,r where the owner of the tile, the player,
     * matches the param hivePlayer.
     * @param hivePlayer
     * @param q
     * @param r
     * @return true If there is a match
     * @return false If there is no match
     */
    public boolean hasNeighbourPlayerTileBesidesCoordinateWithPlayer(HivePlayer hivePlayer, int q, int r){
        for (HiveCell hiveCell : hiveCells){
            if (isNeighbour(hiveCell.getCoordinateQ(), hiveCell.getCoordinateR(), q, r)){
                if (hiveCell.getPlayerTilesAtCell().size() > 0 && hiveCell.getTopPlayerTileFromCell().getPlayer().equals(hivePlayer)) return true;
            }
        }
        return false;
    }

    /**
     * Return true if board has a neighbour cell with a tile besides coordinate q,r
     * @param q
     * @param r
     * @return boolean hasNeighbourTile
     */
    public boolean hasNeighbourPlayerTileBesidesCoordinate(int q, int r){
        for (HiveCell hiveCell : hiveCells){
            if (isNeighbour(hiveCell.getCoordinateQ(), hiveCell.getCoordinateR(), q, r)) return true;
        }
        return false;
    }

    public boolean hasPlayerTileInBoard() {
        for (HiveCell hiveCell : hiveCells) {
            if (hiveCell.hasPlayerTile()) return true;
        }
        return false;
    }

    /**
     * Return true if it is the first turn.
     * @return false When two or more cells exist in the board (both white and black made a turn)
     * @return true When <2 cells exist
     */
    public boolean firstTurn() {
        int tiles = 0;
        for(HiveCell hiveCell : hiveCells){
            if (hiveCell.getPlayerTilesAtCell().size() > 0) tiles++;
            if (tiles >=2) return false;
        }
        return true;
    }

    /**
     * Checks if the queen bee has been played by the player
     * @return true If Tile.QueenBee exists on board
     */
    public boolean playerHasPlayedQueenBee(HivePlayer hivePlayer){
        for(HiveCell hiveCell : hiveCells){
            for(HivePlayerTile hivePlayerTile : hiveCell.getPlayerTilesAtCell()){
                if(hivePlayerTile.getInsect().getTile().equals(Hive.Tile.QUEEN_BEE) && hivePlayerTile.getPlayer().equals(hivePlayer)) return true;
            }
        }
        return false;
    }

    /***
     * Get HiveCell from HiveBoard that matches coordinate q and coordinate r
     * If it does not exist a new cell is returned
     * @param q
     * @param r
     * @return existingHiveCell if there is a HiveCell that matches with the param coordinates q,r
     * @return newHiveCell If the cell does not exist yet
     */
    public HiveCell getCellAt(int q, int r){
        for(HiveCell hiveCell : hiveCells) {
            if (hiveCell.getCoordinateQ() == q && hiveCell.getCoordinateR() == r) return hiveCell;
        }
        HiveCell newCell = new HiveCell(q, r);
        addHiveCell(newCell);
        return newCell;
    }

    private void addNeighboursToCells(){
        ArrayList<HiveCell> hiveCellsWithUpdatedNeighbours = new ArrayList<HiveCell>();
        for(HiveCell hiveCell : hiveCells){
            hiveCell = addNeighboursToCell(hiveCell);
            hiveCellsWithUpdatedNeighbours.add(hiveCell);
        }
        this.hiveCells = hiveCellsWithUpdatedNeighbours;
    }

    private HiveCell addNeighboursToCell(HiveCell hiveCell){
        ArrayList<HiveCell> neighbours = new ArrayList<HiveCell>();
        for (HiveCell c : hiveCells){
            if (isNeighbour(hiveCell.getCoordinateQ(), hiveCell.getCoordinateR(), c.getCoordinateQ(), c.getCoordinateR())) neighbours.add(c);
            if (neighbours.size() == 6){ // Early exit
                hiveCell.setNeighbourHiveCells(neighbours);
                return hiveCell;
            }
        }
        hiveCell.setNeighbourHiveCells(neighbours);
        return hiveCell;
    }

    /**
     * Checks if the tiles in the board stay connected after removing a certain tile
     * @param fromQ
     * @param fromR
     * @param toQ
     * @param toR
     * @return true If the fromTile is a neighbour of the toTile
     * @return false If the fromTile is NOT a neighbour of the toTile AND the fromTile is connected to a tile that has no neighbours left after the move
     * @return true
     */
    public boolean allPlayerTilesStillConnectedAfterMoving(int fromQ, int fromR, int toQ, int toR){
        if (isNeighbour(fromQ, fromR, toQ, toR)) return true;
        HiveCell fromCell = getCellAt(fromQ, fromR);
        HivePlayerTile fromTile = fromCell.getTopPlayerTileFromCell();
        for(HiveCell hiveCell : hiveCells){
            Stack<HivePlayerTile> neighbourPlayerTiles = hiveCell.getPlayerTilesAtCell();
            if (neighbourPlayerTiles.contains(fromTile) && neighbourPlayerTiles.size() == 1) return false;
        }
        return true;
    }

    // Q=x=row
    // R=y=col
    public boolean isNeighbour(int aQ, int aR, int bQ, int bR){
        // Als beide cellen niet hetzelfde zijn
        if(!(aQ == bQ && aR == bR)){
            // Als bQ = aQ-1 && (bR = aR of bR = aR + 1)
            if(bQ == aQ -1 && (bR == aR || bR == aR + 1)) return true;
            // Als bQ = Aq && (bR = aR+1 of bR = aR-1)
            if(bQ == aQ && (bR == aR + 1 || bR == aR - 1)) return true;
            // Als bQ = aQ+1 && (bR = aR of bR = aR -1)
            if(bQ == aQ + 1 && (bR == aR || bR == aR - 1)) return true;

        }
        return false;
    }

    // A = first element
    // B = next element
    public String getDirectionAsString(int aQ, int aR, int bQ, int bR){
        if (bQ == aQ - 1 && bR == aR + 1) return "Left down";
        if (bQ == aQ && bR == aR + 1) return "Right down";
        if (bQ == aQ && bR == aR - 1) return "Left up";
        if (bQ == aQ + 1 && bR == aR - 1) return "Right up";
        return null;
    }

    /**
     * Get neigbours with locations((q1,r1),(q2,r2)..) for current location q,r
     * We define an old cell location as a and a new cell location as b
     * Neighbour cells are:
     *      - bQ = aQ-1 && (bR = aR of bR = aR + 1)
     *      - bQ = Aq && (bR = aR+1 of bR = aR-1)
     *      - bQ = aQ+1 && (bR = aR of bR = aR -1)
     * @param q
     * @param r
     * @return
     */
    public ArrayList<HiveLocation> getNeighbourLocations(int q, int r){
        ArrayList<HiveLocation> neighbourLocations = new ArrayList<>();
        neighbourLocations.add(new HiveLocation(q - 1, r));
        neighbourLocations.add(new HiveLocation(q - 1, r + 1));
        neighbourLocations.add(new HiveLocation(q, r + 1));
        neighbourLocations.add(new HiveLocation(q, r - 1));
        neighbourLocations.add(new HiveLocation(q + 1, r));
        neighbourLocations.add(new HiveLocation(q + 1, r - 1));
        return neighbourLocations;
    }
}
