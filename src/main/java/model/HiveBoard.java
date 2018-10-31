package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * @todo bord converteren naar key,value hivelocation,stack
 */
public class HiveBoard {
    private ArrayList<HiveCell> hiveCells;

    public enum Direction {
        LEFT, LEFT_DOWN, LEFT_UP,
        RIGHT, RIGHT_DOWN, RIGHT_UP
    }

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

    public void makeMove(int fromQ, int fromR, int toQ, int toR){
        HiveCell fromCell = this.getCellAt(fromQ, fromR);
        HiveCell toCell = this.getCellAt(toQ, toR);
        if (toCell == null) {
            toCell = new HiveCell(toQ, toR);
            this.addHiveCell(toCell);
        }
        HivePlayerTile fromHivePlayerTile = fromCell.getTopPlayerTileFromCell();
        toCell.addPlayerTile(fromHivePlayerTile);
        fromCell.removePlayerTile(fromHivePlayerTile);
    }

    /**
     * Provide same coordinates as makeMove, undo's that move
     * @param fromQ
     * @param fromR
     * @param toQ
     * @param toR
     */
    public void undoMove(int fromQ, int fromR, int toQ, int toR){
        HiveCell fromCell = this.getCellAt(toQ, toR);
        HiveCell toCell = this.getCellAt(fromQ, fromR);
        HivePlayerTile fromHivePlayerTile = fromCell.getTopPlayerTileFromCell();
        toCell.addPlayerTile(fromHivePlayerTile);
        fromCell.removePlayerTile(fromHivePlayerTile);
    }

    public boolean isValidShift(int fromQ, int fromR, int toQ, int toR){
        try {
            HiveCell fromCell = this.getCellAt(fromQ, fromR);
            HiveCell toCell = this.getCellAt(toQ, toR);
            if (fromCell == null){
                fromCell = new HiveCell(fromQ, fromR);
                this.addHiveCell(fromCell);
            }
            if (toCell == null) {
                toCell = new HiveCell(toQ, toR);
                this.addHiveCell(toCell);
            }
            isValidShift(fromCell, toCell);
        } catch (Hive.IllegalMove illegalMove) {
            return false;
        }
        return true;
    }

    /**
     * Shift a tile to a neighbour field.
     * @param fromCell
     * @param toCell
     * @throws Hive.IllegalMove When the toCell is not a neighbour of the fromCell
     * @throws Hive.IllegalMove When the toCell and fromCell have 0 neighbours in common, which means the tile will be lose when shifting
     * @throws Hive.IllegalMove When the toCell and fromCell have >1 neighbours in common and we can't shift the tile because of a stack at a certain cell that is to high.
     */
    private void isValidShift(HiveCell fromCell, HiveCell toCell) throws Hive.IllegalMove {
        ArrayList<HiveCell> neighbourCellsForFromCellAndToCell = new ArrayList<>();
        int amountOfNeighbourTilesForToCellAndFromCell = 0;
        ArrayList<HiveCell> fromCellNeighbours = fromCell.getNeighbourCells();
        ArrayList<HiveCell> toCellNeighbours = toCell.getNeighbourCells();
        System.out.println("from" + fromCell.getCoordinateQ() + "," + fromCell.getCoordinateR() + "to" + toCell.getCoordinateQ() + "," + toCell.getCoordinateR());
        if (!this.isNeighbour(fromCell.getCoordinateQ(), fromCell.getCoordinateR(), toCell.getCoordinateQ(), toCell.getCoordinateR())) throw new Hive.IllegalMove("We kunnen niet schuiven sinds we de steen proberen te verplaatsen naar een vak die niet grenst aan onze oorsproonkelijke locatie.");
        for(HiveCell a : fromCellNeighbours){
            for(HiveCell b: toCellNeighbours){
                // Buur cell voor zowel fromCell als toCell
                if(a == b){
                    neighbourCellsForFromCellAndToCell.add(a);
                    if (a.getPlayerTilesAtCell().size()> 0) amountOfNeighbourTilesForToCellAndFromCell++;
                }
            }
        }
        System.out.println(amountOfNeighbourTilesForToCellAndFromCell);

        // 6c Tijdens een verschuiving moet de steen continu in contact blijven met minstens één andere steen.
        if (amountOfNeighbourTilesForToCellAndFromCell == 0) throw new Hive.IllegalMove("De fromCell en toCell hebben 0 gelijke buren wat betekent dat tijdens het schuiven de steen los komt van een andere steen, dit mag niet.");

        // Als we niet minsten 2 buren hebben dan kunnen we altijd schuiven natuurlijk
        if (amountOfNeighbourTilesForToCellAndFromCell > 1){
            // 6b Kijk naar de hoogstes van de stenen of we kunnen schuiven als we meer dan 1 gelijke stenen als buren hebben voor toCell en fromCell
            int cellLowestValue = 99;
            for(HiveCell neighbourForAandB : neighbourCellsForFromCellAndToCell){
                Stack<HivePlayerTile> tilesAtCell = neighbourForAandB.getPlayerTilesAtCell();
                if (tilesAtCell.size() < cellLowestValue){
                    cellLowestValue = tilesAtCell.size();
                }
            }
            int cellHighestValue = toCell.getPlayerTilesAtCell().size();
            if(fromCell.getPlayerTilesAtCell().size() - 1 > cellHighestValue){
                cellHighestValue = fromCell.getPlayerTilesAtCell().size();
            }
            if(cellLowestValue > cellHighestValue){
                throw new Hive.IllegalMove("We kunnen deze cell niet verplaatsten, sinds het schuiven niet past");
            }
        }
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
        HiveCell fromCell = getCellAt(fromQ, fromR);
        HiveCell toCell = getCellAt(toQ, toR);
        for(HiveCell neighbourFrom : fromCell.getNeighbourCells()) {
            // Voor alle buren van de from cell
            if (neighbourFrom.getPlayerTilesAtCell().size() > 0) {
                // Waar van de to cell niet de buur is
                if (toQ != neighbourFrom.getCoordinateQ() || toR != neighbourFrom.getCoordinateR()) {
                    System.out.println("expect" + neighbourFrom.getCoordinateQ() + "," + neighbourFrom.getCoordinateR());
                    if(!areCellsLinked(fromCell, toCell, neighbourFrom, new ArrayList<>())){
                        // Als één van de buren niet verbonden is met de toCell return false
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Is there a path where currentCell can reach the toCell without visiting the cellToMove
     * because that cell will be non existend after moving but is currenty still in the
     * board.
     */
    private boolean areCellsLinked(HiveCell cellToMove, HiveCell toCell, HiveCell currentCell, ArrayList<HiveCell> visited){
        visited.add(currentCell);
        if (currentCell.getCoordinateQ() == toCell.getCoordinateQ() && currentCell.getCoordinateR() == toCell.getCoordinateR()){
            return true;
        }

        for (HiveCell n: currentCell.getNeighbourCells()){
            if (!visited.contains(n) && n != cellToMove){
                if (areCellsLinked(cellToMove, toCell, n, visited)) return true;
            }
        }
        return false;
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

    /***
     * Get direction from position A to position B
     * @param fQ fromQ
     * @param fR fromR
     * @param tQ toQ
     * @param tR toR
     * @return direction as a String value
     */
    public Direction getDirection(int fQ, int fR, int tQ, int tR){
        if (tQ == fQ - 1 && tR == fR) return Direction.LEFT;
        if (tQ == fQ + 1 && tR == fR) return Direction.RIGHT;
        if (tQ == fQ - 1 && tR == fR + 1) return Direction.LEFT_DOWN;
        if (tQ == fQ && tR == fR + 1) return Direction.RIGHT_DOWN;
        if (tQ == fQ && tR == fR - 1) return Direction.LEFT_UP;
        if (tQ == fQ + 1 && tR == fR - 1) return Direction.RIGHT_UP;
        System.out.println(fQ + "," + fR + " > " + tQ + ","+ tR);
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

    /**
     * Overwrite clone function because we want to correctly add neighbours to cells
     * @return
     */
    @Override
    public HiveBoard clone(){
        HiveBoard copyBoard = new HiveBoard();
        for(HiveCell hiveCell : hiveCells){
            HiveCell c = new HiveCell(hiveCell.getCoordinateQ(), hiveCell.getCoordinateR());
            c.setNeighbourHiveCells(new ArrayList<>(hiveCell.getNeighbourCells()));
            Stack<HivePlayerTile> hivePlayerTiles = (Stack<HivePlayerTile>) hiveCell.getPlayerTilesAtCell().clone();
            c.setPlayerTilesAtCell(hivePlayerTiles);
            copyBoard.addHiveCell(c);
        }
        return copyBoard;
    }

    public boolean isPathFilledWithTiles(ArrayList<HiveLocation> path){
        HiveCell currCell = null;
        for(HiveLocation l : path){
            currCell = getCellAt(l.getQ(), l.getR());
            if (currCell == null) return false;
            if (currCell.getPlayerTilesAtCell().isEmpty()) return false;
        }
        return true;
    }

    /**
     * Checks if the path is a straight line by checking direction of the current
     * and next location l[i] and l[i + 1]. If somewhere in this process the old
     * direction does not match the new direction the list does not contain a
     * straight path to the destination.
     * @param startQ
     * @param startR
     * @param pathWithoutFromCell
     * @return
     */
    public boolean isPathStraightLine(int startQ, int startR, ArrayList<HiveLocation> pathWithoutFromCell){
        ArrayList<HiveLocation> fullPath = new ArrayList();
        fullPath.add(new HiveLocation(startQ, startR));
        fullPath.addAll(pathWithoutFromCell);

        HiveBoard.Direction oldDirection = null;
        for(int i = 0; i < fullPath.size() - 1; i++){
            HiveBoard.Direction newDirection = getDirection(fullPath.get(i).getQ(), fullPath.get(i).getR(), fullPath.get(i + 1).getQ(), fullPath.get(i + 1).getR());
            if (newDirection == null) return false;
            if (oldDirection == null) oldDirection = newDirection;
            if (!oldDirection.name().equals(newDirection.name())) return false;
        }
        return true;
    }
}
