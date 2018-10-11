package model;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Represents a place of the board where an insect can be placed
 * The insects at a certain tile are inserted in a Stack so we
 * can keep track which tile/insect is on top.
 */
public class HiveCell {
    private int coordinateQ;
    private int coordinateR;
    private Stack<HivePlayerTile> playerTilesAtCell;
    private ArrayList<HiveCell> neighbourHiveCells;

    public HiveCell(int coordinateQ, int coordinateR){
        this.playerTilesAtCell = new Stack<HivePlayerTile>();
        this.neighbourHiveCells = new ArrayList<HiveCell>();
        this.coordinateQ = coordinateQ;
        this.coordinateR = coordinateR;
    }

    public HiveCell(HivePlayerTile playerTile, int coordinateQ, int coordinateR){
        this.playerTilesAtCell = new Stack<HivePlayerTile>();
        this.neighbourHiveCells = new ArrayList<HiveCell>();
        this.playerTilesAtCell.add(playerTile);
        this.coordinateQ = coordinateQ;
        this.coordinateR = coordinateR;
    }

    public void addPlayerTile(HivePlayerTile playerTile){
        playerTilesAtCell.push(playerTile);
    }

    public void removePlayerTile(HivePlayerTile playerTile){
        playerTilesAtCell.remove(playerTile);
    }

    public void setNeighbourHiveCells(ArrayList<HiveCell> neighbours){
        neighbourHiveCells = neighbours;
    }

    public boolean hasNeighbourPlayerTile(){
        if (getTopNeighbourPlayerTiles().size() > 0) return true;
        return false;
    }

    public boolean hasPlayerTile(){
        if (getPlayerTilesAtCell().size() > 0) return true;
        return false;
    }

    /**
     * Get the player tile at the top for each neighbour cell
     * @return
     */
    public ArrayList<HivePlayerTile> getTopNeighbourPlayerTiles(){
        ArrayList<HivePlayerTile> neighbourPlayerTiles = new ArrayList<>();
        for(HiveCell hiveCell : neighbourHiveCells){
            if (hiveCell.getPlayerTilesAtCell().size() > 0){
                neighbourPlayerTiles.add(hiveCell.getTopPlayerTileFromCell());
            }
        }
        return neighbourPlayerTiles;
    }

    public ArrayList<HiveCell> getNeighbourCells(){
        return neighbourHiveCells;
    }

    public HivePlayerTile getTopPlayerTileFromCell(){
        return playerTilesAtCell.peek();
    }

    public Stack<HivePlayerTile> getPlayerTilesAtCell() {
        return playerTilesAtCell;
    }

    public int getCoordinateQ() {
        return coordinateQ;
    }

    public void setCoordinateQ(int coordinateQ) {
        this.coordinateQ = coordinateQ;
    }

    public int getCoordinateR() {
        return coordinateR;
    }

    public void setCoordinateR(int coordinateR) {
        this.coordinateR = coordinateR;
    }
}

