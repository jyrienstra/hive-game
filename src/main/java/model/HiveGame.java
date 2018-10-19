package model;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Stack;


public class HiveGame implements Hive {
    private HiveBoard hiveBoard;
    private ArrayList<HivePlayer> hivePlayers;
    private HivePlayer currentPlayer;

    /**
     * Er zijn twee spelers zwart en wit.
     * Elke speler heeft aan het begin van het spel de beschikking over één
     * bijenkoningin, twee spinnen, twee kevers, drie soldatenmieren en drie
     * sprinkhanen in zijn eigen kleur.
     * Wit heeft de eerste beurt
     *
     */
    public HiveGame(){
        hiveBoard = new HiveBoard();
        hivePlayers = new ArrayList<HivePlayer>();
        HivePlayer pBlack = new HivePlayer(Player.BLACK);
        HivePlayer pWhite = new HivePlayer(Player.WHITE);
        hivePlayers.add(pBlack);
        hivePlayers.add(pWhite);
        for(HivePlayer hivePlayer : hivePlayers){
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.QUEEN_BEE));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.SPIDER));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.SPIDER));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.BEETLE));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.BEETLE));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.SOLDIER_ANT));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.SOLDIER_ANT));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.SOLDIER_ANT));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.GRASSHOPPER));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.GRASSHOPPER));
            hivePlayer.addPlayerTile(new HivePlayerTile(hivePlayer, Tile.GRASSHOPPER));
        }
        currentPlayer = pWhite;
    }

    public HiveBoard getBoard(){
        return this.hiveBoard;
    }

    private void switchPlayer(){
        for(HivePlayer hivePlayer: hivePlayers){
            if (currentPlayer != hivePlayer){
                currentPlayer = hivePlayer;
                return;
            }
        }
    }

    private Hive.Player getOtherPlayerColor(Hive.Player playerColor) {
        for(Hive.Player p : Hive.Player.values()){
            if(p != playerColor){
                return p;
            }
        }
        return playerColor;
    }

    private HivePlayer getOtherPlayer(){
        for(HivePlayer hivePlayer : hivePlayers){
            if(hivePlayer != currentPlayer) return hivePlayer;
        }
        return null;
    }

    /**
     * Plays tile at q,r
     * @param tile HiveCell to play
     * @param q Q coordinate of hexagon to play to
     * @param r R coordinate of hexagon to play to
     * @throws IllegalMove When in the fourth move of one player the queen bee has not been played yet
     * @throws IllegalMove When the player does not have the tile that is being played (tile in playerTiles)
     * @throws IllegalMove When there are already existing tiles in the board and the tile being played does not have a neighbour tile
     * @throws IllegalMove When a tile is being placed besides a tile from the opponent after the first turn
     * @throws IllegalMove When the field q,r is not empty
     */
    public void play(Tile tile, int q, int r) throws IllegalMove {
        if (!currentPlayer.hasTile(tile)) throw new IllegalMove("Een speler mag alleen zijn eigen nog niet gespeelde stenen spelen.");
        if (currentPlayer.getTilesPlayed().size() == 3 && !tile.equals(Tile.QUEEN_BEE)) throw new IllegalMove("Als een speler al drie stenen gespeeld heeft maar zijn bijenkoningin nog niet dan moet deze gespeeld worden.");

        HivePlayerTile tileFromCurrentPlayer = currentPlayer.getTile(tile);
        HiveCell existingHiveCell = hiveBoard.getCellAt(q, r);
        if (existingHiveCell == null){
            if(hiveBoard.hasPlayerTileInBoard() && !hiveBoard.hasNeighbourPlayerTileBesidesCoordinate(q, r)) throw new IllegalMove("Er liggen al stenen op het bord. De steen moet dus naast een andere steen gespeeld worden");
            if (!hiveBoard.firstTurn() && hiveBoard.hasNeighbourPlayerTileBesidesCoordinateWithPlayer(getOtherPlayer(), q, r)) throw new IllegalMove("Alleen in de eerste beurt is het toegestaan om een insect tegen de tegenstander aan te leggen.");
            hiveBoard.addHiveCell(new HiveCell(tileFromCurrentPlayer, q, r));
            currentPlayer.removePlayerTile(tileFromCurrentPlayer);
        }else{
            if (existingHiveCell.hasPlayerTile()) throw new IllegalMove("Dit veld is niet leeg. De nieuwe steen kan hier dus niet gespeeld worden");
            if (hiveBoard.hasPlayerTileInBoard() && !existingHiveCell.hasNeighbourPlayerTile()) throw new IllegalMove("Er liggen al stenen op het bord. De steen moet dus naast een andere steen gespeeld worden");
            if (!hiveBoard.firstTurn() && hiveBoard.hasNeighbourPlayerTileBesidesCoordinateWithPlayer(getOtherPlayer(), q, r)) throw new IllegalMove("Alleen in de eerste beurt is het toegestaan om een insect tegen de tegenstander aan te leggen.");
            existingHiveCell.addPlayerTile(tileFromCurrentPlayer);
            currentPlayer.removePlayerTile(tileFromCurrentPlayer);
        }
        switchPlayer();
    }


    /**
     * Shift a tile to a neighbour field.
     * @param fromCell
     * @param toCell
     * @throws IllegalMove When the toCell is not a neighbour of the fromCell
     * @throws IllegalMove When the toCell and fromCell have 0 neighbours in common, which means the tile will be lose when shifting
     * @throws IllegalMove When the toCell and fromCell have >1 neighbours in common and we can't shift the tile because of a stack at a certain cell that is to high.
     */
    private void isValidShift(HiveCell fromCell, HiveCell toCell) throws IllegalMove{
        ArrayList<HiveCell> neighbourCellsForFromCellAndToCell = new ArrayList<>();
        int amountOfNeighbourTilesForToCellAndFromCell = 0;
        ArrayList<HiveCell> fromCellNeighbours = fromCell.getNeighbourCells();
        ArrayList<HiveCell> toCellNeighbours = toCell.getNeighbourCells();
        if (!hiveBoard.isNeighbour(fromCell.getCoordinateQ(), fromCell.getCoordinateR(), toCell.getCoordinateQ(), toCell.getCoordinateR())) throw new IllegalMove("We kunnen niet schuiven sinds we de steen proberen te verplaatsen naar een vak die niet grenst aan onze oorsproonkelijke locatie.");
        for(HiveCell a : fromCellNeighbours){
            for(HiveCell b: toCellNeighbours){
                // Buur cell voor zowel fromCell als toCell
                if(a == b){
                    neighbourCellsForFromCellAndToCell.add(a);
                    if (a.getPlayerTilesAtCell().size()> 0) amountOfNeighbourTilesForToCellAndFromCell++;
                }
            }
        }

        // 6c Tijdens een verschuiving moet de steen continu in contact blijven met minstens één andere steen.
        if (amountOfNeighbourTilesForToCellAndFromCell == 0) throw new IllegalMove("De fromCell en toCell hebben 0 gelijke buren wat betekent dat tijdens het schuiven de steen los komt van een andere steen, dit mag niet.");

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
                throw new IllegalMove("We kunnen deze cell niet verplaatsten, sinds het schuiven niet past");
            }
        }
    }

    public boolean isValidShift(int fromQ, int fromR, int toQ, int toR){
        try {
            HiveCell fromCell = hiveBoard.getCellAt(fromQ, fromR);
            HiveCell toCell = hiveBoard.getCellAt(toQ, toR);
            if (fromCell == null){
                fromCell = new HiveCell(fromQ, fromR);
                hiveBoard.addHiveCell(fromCell);
            }
            if (toCell == null) {
                toCell = new HiveCell(toQ, toR);
                hiveBoard.addHiveCell(toCell);
            }
            isValidShift(fromCell, toCell);
        } catch (IllegalMove illegalMove) {
            return false;
        }
        return true;
    }


    public void throwIllegalMoveWhenMoveIsNotValid(int fromQ, int fromR, int toQ, int toR) throws IllegalMove{
        HiveCell fromCell = hiveBoard.getCellAt(fromQ, fromR);
        if (fromCell == null) throw new IllegalMove("Er bestaat geen steen met deze coordinaten");
        if (fromCell.getPlayerTilesAtCell().isEmpty()) throw new IllegalMove("Er bestaat geen steen met deze coordinaten");
        if (!fromCell.getTopPlayerTileFromCell().getPlayer().equals(currentPlayer)) throw new IllegalMove("Deze steen is van een andere speler. De steen mag dus niet verplaatst worden");
        if (!hiveBoard.playerHasPlayedQueenBee(currentPlayer)) throw new IllegalMove("Een speler mag pas stenen verplaatsen als zijn bijenkoningin gespeeld is");
        if (!hiveBoard.hasNeighbourPlayerTileBesidesCoordinate(toQ, toR)) throw new IllegalMove("Een steen moet na het verplaatsen in contact zijn met minstens één andere steen");
        if (!hiveBoard.allPlayerTilesStillConnectedAfterMoving(fromQ, fromR, toQ, toR)) throw new IllegalMove("Een steen mag niet verplaatst worden als er door het weghalen van de steen twee niet onderling verbonden groepen stenen ontstaan.");
    }

    public void makeMove(int fromQ, int fromR, int toQ, int toR){
        HiveCell fromCell = hiveBoard.getCellAt(fromQ, fromR);
        HiveCell toCell = hiveBoard.getCellAt(toQ, toR);
        if (toCell == null) {
            toCell = new HiveCell(toQ, toR);
            hiveBoard.addHiveCell(toCell);
        }
        HivePlayerTile fromHivePlayerTile = fromCell.getTopPlayerTileFromCell();
        toCell.addPlayerTile(fromCell.getTopPlayerTileFromCell());
        fromCell.removePlayerTile(fromCell.getTopPlayerTileFromCell());
    }

//    e. Elk van de types stenen heeft zijn eigen manier van verplaatsen.
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        throwIllegalMoveWhenMoveIsNotValid(fromQ, fromR, toQ,  toR);

        HiveCell fromCell = hiveBoard.getCellAt(fromQ, fromR);
        HivePlayerTile fromHivePlayerTile = fromCell.getTopPlayerTileFromCell();

//        int sFromQ = fromQ;
//        int sFromR = fromR;
//        int sToQ;
//        int sToR;
//        for (HiveLocation hiveLocation : validPath){
//            sToQ = hiveLocation.getQ();
//            sToR = hiveLocation.getR();
//            hiveGame.makeMove(sFromQ, sFromR, sToQ, sToR);
//            sFromQ = sToQ;
//            sFromR = sToR;
//        }
       //@todo make moves

        switchPlayer();
    }

    public void pass() throws IllegalMove {
        switchPlayer();
    }

    /**
     * Een speler wint als alle zes velden naast de bijenkoningin van de
     * tegenstander bezet zijn
     * @param player Player to check
     * @returnfgetNeighbourHiveCells
     */
    public boolean isWinner(Player player) {
        for(HiveCell hiveCell : hiveBoard.getHiveCells()){
            for (HivePlayerTile playerTile: hiveCell.getPlayerTilesAtCell()){
                if (playerTile.getPlayer().getPlayerColor().equals(getOtherPlayerColor(player)) && playerTile.getTile().equals(Tile.QUEEN_BEE) && hiveCell.getTopNeighbourPlayerTiles().size() == 6) return true;
            }
        }
        return false;
    }

    /**
     * Als beide spelers tegelijk zouden winnen is het in plaats daarvan een
     * gelijkspel.
     * @return
     */
    public boolean isDraw() {
        boolean allPlayersAreWinners = true;
        for(HivePlayer hivePlayer : hivePlayers){
            if (!isWinner(hivePlayer.getPlayerColor())){
                allPlayersAreWinners = false;
            }
        }
        if (allPlayersAreWinners) return true;
        return false;
    }
}
