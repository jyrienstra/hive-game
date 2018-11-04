package model;

import java.util.*;

/**
 * @todo bord converteren naar key,value hivelocation,stack
 */
public class HiveBoard {
    private HashMap<HiveLocation, Stack<HivePlayerTile>> board;

    public enum Direction {
        LEFT, LEFT_DOWN, LEFT_UP,
        RIGHT, RIGHT_DOWN, RIGHT_UP
    }

    public HiveBoard(){
        this.board = new HashMap<>();
    }

    /**
     * Add a tile on top of the stack at location q,r if it already exists
     * Else push a new entry at q,r with Stack<HivePlayerTile>
     * @param q
     * @param r
     * @param hivePlayerTile
     */
    public void addPlayerTile(HivePlayerTile hivePlayerTile, int q, int r){
        HiveLocation location = new HiveLocation(q,r);
        Stack<HivePlayerTile> tilesAtLocation;
        if (board.containsKey(location)){
            tilesAtLocation = board.get(new HiveLocation(q,r));
        }else{
            tilesAtLocation = new Stack<HivePlayerTile>();
        }
        tilesAtLocation.push(hivePlayerTile);
        board.put(location, tilesAtLocation);
    }

    /**
     * Remove player from Stack<HivePlayerTile> at HiveLocation q,r
     * and update this location. Or when stack size is empty
     * after removal remove the entire entry from the
     * board HashMap.
     * @param q
     * @param r
     * @param hivePlayerTile
     */
    public void removePlayerTile(HivePlayerTile hivePlayerTile, int q, int r){
        HiveLocation location = new HiveLocation(q,r);
        Stack<HivePlayerTile> tilesAtLocation = board.get(new HiveLocation(q,r));
        tilesAtLocation.remove(hivePlayerTile);
        if (tilesAtLocation.size() == 0){
            board.remove(location);
        }else{
            board.put(location, tilesAtLocation);
        }
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
        ArrayList<HiveLocation> neighbours = new ArrayList<>();
        neighbours.add(new HiveLocation(q - 1, r)); // LEFT
        neighbours.add(new HiveLocation(q + 1, r)); // RIGHT
        neighbours.add(new HiveLocation(q - 1, r + 1)); // LEFT_DOWN
        neighbours.add(new HiveLocation(q, r + 1)); // RIGHT_DOWN
        neighbours.add(new HiveLocation(q, r - 1)); // LEFT_UP
        neighbours.add(new HiveLocation(q + 1, r - 1)); // RIGHT_UP
        return neighbours;
    }

    /**
     * Retrieves all neighbour locations and checks
     * if there is a tile placed at this location.
     * @param q
     * @param r
     * @return true If one of the neighbours locations of q,r has a tile placed at it's location
     */
    public boolean hasNeighbourPlayerTile(int q, int r){
        ArrayList<HiveLocation> neighbours = getNeighbourLocations(q,r);
        for(HiveLocation n : neighbours){
            if (hasTileAt(n.getQ(), n.getR())) return true;
        }
        return false;
    }

    /**
     * Checks if there is a tile at HiveLocation q,r
     * @param q
     * @param r
     * @return true If there is a tile at q,r
     */
    public boolean hasTileAt(int q, int r){
        HiveLocation location = new HiveLocation(q,r);
        if (board.containsKey(location)){
            Stack<HivePlayerTile> tiles = board.get(location);
            if (!tiles.isEmpty()) return true;
        }
        return false;
    }

    /**
     * Remove tile at HiveLocation(fromQ, fromR) an
     * add tile at HiveLocation(fromQ, fromR)
     * @param fromQ
     * @param fromR
     * @param toQ
     * @param toR
     */
    public void makeMove(int fromQ, int fromR, int toQ, int toR){
        HivePlayerTile tile = board.get(new HiveLocation(fromQ, fromR)).peek();
        removePlayerTile(tile, fromQ, fromR);

        HiveLocation toLocation = new HiveLocation(toQ, toR);
        Stack<HivePlayerTile> tilesAtToLocation;
        if (!board.containsKey(toLocation)){
            tilesAtToLocation = new Stack<>();
        }else{
            tilesAtToLocation = board.get(toLocation);
        }
        tilesAtToLocation.add(tile);
        board.put(toLocation, tilesAtToLocation);
    }

    /**
     * Provide same coordinates as makeMove()
     * undo's that move by removing tile at HiveLocation(toQ, toR)
     * and adding tile at HiveLocation(fromQ, fromR)
     * @param fromQ
     * @param fromR
     * @param toQ
     * @param toR
     */
    public void undoMove(int fromQ, int fromR, int toQ, int toR){
        makeMove(toQ, toR, fromQ, fromR);
    }

    /**
     * Get Stack<HivePlayerTile> hivePlayerTiles at HiveLocation(q,r)
     * @param q
     * @param r
     * @return
     */
    public Stack<HivePlayerTile> getPlayerTilesAt(int q, int r){
        HiveLocation location = new HiveLocation(q,r);
        Stack<HivePlayerTile> tilesAtLocation = null;
        if (board.containsKey(location)){
            tilesAtLocation = board.get(new HiveLocation(q,r));
        }else{
            tilesAtLocation = new Stack<>();
        }
        return tilesAtLocation;
    }

    /**
     * Shift a tile to a neighbour field.
     * @param fromQ
     * @param fromR
     * @param toQ
     * @param toR
     * @return false When the toCell is not a neighbour of the fromCell
     * @return false When the toCell and fromCell have 0 neighbours in common, which means the tile will be lose when shifting
     * @return false When the toCell and fromCell have >1 neighbours in common and we can't shift the tile because of a stack at a certain cell that is to high.
     * @return true
     */
    public boolean isValidShift(int fromQ, int fromR, int toQ, int toR) {
        ArrayList<HiveLocation> neighbourLocationsForFromCellAndToCell = new ArrayList<>();
        int amountOfNeighbourTilesForToCellAndFromCell = 0;
        ArrayList<HiveLocation> fromNeighbourLocations = getNeighbourLocations(fromQ, fromR);
        ArrayList<HiveLocation> toNeighbourLocations = getNeighbourLocations(toQ, toR);
        System.out.println("from" + fromQ + "," + fromR + "to" + toQ + "," + toR);
        if (!this.isNeighbour(fromQ, fromR, toQ, toR)) return false; // We kunnen niet schuiven sinds we de steen proberen te verplaatsen naar een vak die niet grenst aan onze oorsproonkelijke locatie
        for(HiveLocation a : fromNeighbourLocations){
            for(HiveLocation b: toNeighbourLocations){
                // Buur HiveLocation voor zowel from als to
                if(a.equals(b)){
                    neighbourLocationsForFromCellAndToCell.add(a);
                    if (hasTileAt(a.getQ(), a.getR())) amountOfNeighbourTilesForToCellAndFromCell++;
                }
            }
        }

        // 6c Tijdens een verschuiving moet de steen continu in contact blijven met minstens één andere steen.
        if (amountOfNeighbourTilesForToCellAndFromCell == 0) return false; // De fromCell en toCell hebben 0 gelijke buren wat betekent dat tijdens het schuiven de steen los komt van een andere steen, dit mag niet

        // Als we niet minsten 2 buren hebben dan kunnen we altijd schuiven natuurlijk
        if (amountOfNeighbourTilesForToCellAndFromCell > 1){
            // 6b Kijk naar de hoogstes van de stenen of we kunnen schuiven als we meer dan 1 gelijke stenen als buren hebben voor toCell en fromCell
            int cellLowestValue = 99;
            for(HiveLocation neighbourForAandB : neighbourLocationsForFromCellAndToCell){
                Stack<HivePlayerTile> tilesAtLocation = board.get(neighbourForAandB);
                if (tilesAtLocation.size() < cellLowestValue){
                    cellLowestValue = tilesAtLocation.size();
                }
            }
            int sizePlayerTilesAtFromLocation = getPlayerTilesAt(fromQ, fromR).size();
            int sizePlayerTilesAtToLocation = getPlayerTilesAt(toQ, toR).size();
            int cellHighestValue = sizePlayerTilesAtFromLocation;
            if (sizePlayerTilesAtToLocation > sizePlayerTilesAtFromLocation){
                cellHighestValue = sizePlayerTilesAtToLocation;
            }
            if (cellLowestValue <= cellHighestValue){
                return false;
            }
        }
        return true;
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
        ArrayList<HiveLocation> neighbours = getNeighbourLocations(q, r);
        for(HiveLocation neighbour : neighbours){
            if (board.containsKey(neighbour)){
                Stack<HivePlayerTile> tilesAtNeighbour;
                tilesAtNeighbour = board.get(neighbour);
                if (tilesAtNeighbour.size() > 0 && tilesAtNeighbour.peek().getPlayer().equals(hivePlayer)) return true;
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
        System.out.println("start " + q + "," + r);
        ArrayList<HiveLocation> neighbours = getNeighbourLocations(q, r);

        Iterator it = board.keySet().iterator();
        while(it.hasNext()){
            HiveLocation location = (HiveLocation) it.next();
            System.out.println("In bord:" + location.getQ() + "," + location.getR());
        }

        for(HiveLocation neighbour : neighbours){
            System.out.println("neighbour" + neighbour.getQ() + "," + neighbour.getR());
            if (board.containsKey(neighbour)){
                //@todo bug vind locatie niet
                System.out.println(q + "," + r + "neighbour is " + neighbour.getQ() + "," + neighbour.getR());
                Stack<HivePlayerTile> tilesAtNeighbour;
                tilesAtNeighbour = board.get(neighbour);
                if (tilesAtNeighbour.size() > 0) return true;
            }
        }
        return false;
    }

    public boolean hasPlayerTileInBoard() {
        if (board.size() > 0) return true;
        return false;
    }

    /**
     * Return true if it is the first turn.
     * @return false When two or more cells exist in the board (both white and black made a turn)
     * @return true When <2 cells exist
     */
    public boolean isFirstTurn() {
        if (board.size() >= 2) return false;
        return true;
    }

    /**
     * Checks if the queen bee has been played by the player
     * @return true If Tile.QueenBee exists on board
     */
    public boolean playerHasPlayedQueenBee(HivePlayer hivePlayer){
        Iterator it = board.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Stack<HivePlayerTile> tiles = (Stack<HivePlayerTile>) pair.getValue();
            for(HivePlayerTile tile : tiles){
                if (tile.getPlayer().equals(hivePlayer)) return true;
            }
        }
        return false;
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
        ArrayList<HiveLocation> neighboursFrom = getNeighbourLocations(fromQ, fromR);
        ArrayList<HiveLocation> neighboursTo = getNeighbourLocations(toQ, toR);
        HiveLocation fromLocation = new HiveLocation(fromQ, fromR);
        HiveLocation toLocation = new HiveLocation(toQ, toR);

        for(HiveLocation neighbourFrom : neighboursFrom) {
            // Voor alle buren van de from cell met tiles
            if (getPlayerTilesAt(neighbourFrom.getQ(), neighbourFrom.getR()).size() > 0) {
                // Waar van de to cell niet de buur isot
                if (toQ != neighbourFrom.getQ() || toR != neighbourFrom.getR()) {
                    System.out.println("expect" + neighbourFrom.getQ() + "," + neighbourFrom.getR());
                    if(!areCellsLinked(fromLocation, toLocation, neighbourFrom, new ArrayList<>())){
                        System.out.println("hier komen we nooit");
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
    private boolean areCellsLinked(HiveLocation locationToMove, HiveLocation toLocation, HiveLocation currentLocation, ArrayList<HiveLocation> visited){
        System.out.println(currentLocation.getQ() + "," + currentLocation.getR());
        visited.add(currentLocation);

        if (currentLocation.getQ() == toLocation.getQ() && currentLocation.getR() == toLocation.getR()){
            return true;
        }

        if (!board.containsKey(currentLocation)) return false; // geen tile op deze locatie

        if (currentLocation.getQ() == locationToMove.getQ() && currentLocation.getR() == locationToMove.getR()) return false; // mag niet via de tile die verplaatst wordt


        for (HiveLocation n: getNeighbourLocations(currentLocation.getQ(), currentLocation.getR())){
            System.out.println("n: " + n.getQ() + "," + n.getR());
            if (!visited.contains(n)){
                System.out.println("b: " + n.getQ() + "," + n.getR()) ;
                if (areCellsLinked(locationToMove, toLocation, n, visited)) return true;
            }
        }
        return false;
    }

    /**
     * Check if location a and location b are neighbours of eachother
     * @param aQ
     * @param aR
     * @param bQ
     * @param bR
     * @return true if location(aQ,aR) is a neighbour of location(bQ, bR)
     * @return false
     */
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
     * Clone HiveBoard by deep copying all values in the board
     * @return
     */
    @Override
    public HiveBoard clone(){
        HiveBoard copyBoard = new HiveBoard();
        Iterator it = this.getBoard().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            HiveLocation location = (HiveLocation) pair.getKey();
            Stack<HivePlayerTile> oldTilesAtLocation = (Stack<HivePlayerTile>) pair.getValue();
            Stack<HivePlayerTile> copyTilesAtLocation = (Stack<HivePlayerTile>) oldTilesAtLocation.clone();
            copyBoard.getBoard().put(location, copyTilesAtLocation);
        }
        return copyBoard;
    }

    /**
     * Get board
     * @return
     */
    public HashMap<HiveLocation, Stack<HivePlayerTile>> getBoard(){
        return this.board;
    }

    /**
     * Set board
     * @param board
     */
    public void setBoard(HashMap<HiveLocation, Stack<HivePlayerTile>> board){
        this.board = board;
    }

    /**
     * Check if there is a tile at every location in the provided
     * parameter path
     * @param path
     * @return True if all elements in path have a tile
     */
    public boolean isPathFilledWithTiles(ArrayList<HiveLocation> path){
        for(HiveLocation l : path){
            if (!hasTileAt(l.getQ(), l.getR())) return false;
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
