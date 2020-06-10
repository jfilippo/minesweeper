import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;

/**
 * 
 * @author jfilippo
 *
 */
@SuppressWarnings("serial")
public class Board extends JPanel {
    
    // private fields
    private static int gridSize = Integer.parseInt(
            JOptionPane.showInputDialog("What grid size do you want to play? \n" + 
                                            "For example, type 8 to play a 8x8 grid!"));
    private static int numBombs = gridSize * gridSize / 5;
    private static int boardWidth = gridSize * 120;
    private static int boardHeight = gridSize * 120;     
    
    private static int clearedTiles = 0;
    private int flaggedBombs;
    private JLabel status;
    private JLabel moves;
    private JLabel remainingBombs;
    private Tile[][] grid;
    private int totalMoves;
    private boolean gameRunning;
    private int[][] solution;
    private Map<String, Integer> scoreboard;
    
    private Timer timer;
    
    /**
     * public Board constructor
     * @param status
     * @param moves
     * @param remainingBombs
     * @param leaderboard
     */
    public Board(JLabel status, JLabel moves, JLabel remainingBombs, Timer timer) {
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLayout(new GridLayout(gridSize, gridSize));
        this.solution = makeBoard();
        this.totalMoves = 0;
        this.status = status;
        this.moves = moves;
        this.timer = timer;
        timer.start();
        remainingBombs.setText((numBombs - flaggedBombs) + "");
        this.gameRunning = true;       
        this.flaggedBombs = numBombs;
        this.scoreboard = new TreeMap<String, Integer>();
        
        makeGrid(moves, remainingBombs);
        tileSetSurroundingBombs();             
    }
    
    /**
     * makeBoard function populates the 2D array
     */
    private int[][] makeBoard() {
        int[][] gridBoard = new int[gridSize][gridSize];
        int remainingPlacedBombs = numBombs;
        
        for (int i = 0; i < gridBoard.length; i++) {
            for (int j = 0; j < gridBoard[i].length; j++) {
                gridBoard[i][j] = 0;
            }
        }        
        while (remainingPlacedBombs > 0) {
            int row = (int) (Math.random() * gridSize);
            int col = (int) (Math.random() * gridSize);
            if (gridBoard[row][col] == 0) {
                gridBoard[row][col] = 1;
                remainingPlacedBombs--;
            }            
        }        
        return gridBoard;
    }
    
    /**
     * makes the grid 2d array
     * @param moves
     * @param remainingBombs
     */
    private void makeGrid(JLabel moves, JLabel remainingBombs) {
        this.grid = new Tile[gridSize][gridSize];   
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                if (solution[r][c] == 1) {
                    grid[r][c] = new Tile(r, c, true);
                } else {
                    grid[r][c] = new Tile(r, c, false);
                }
                final Tile t = grid[r][c];
                t.repaint();
                handleClicksAndMoves(t, moves, remainingBombs);                
            }
        }
    }
    
    /**
     * Helper function that deals with MouseListener and updating the number of moves
     * @param t
     * @param moves
     */
    private void handleClicksAndMoves(Tile t, JLabel moves, JLabel remainingBombs) {
        this.add(t);
        t.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                    if (gameRunning) {
                        if (!t.getFlagged()) {
                            if (t.isHidden()) {
                                totalMoves++;
                                moves.setText("" + totalMoves);
                            }                            
                            handleGame(t);
                        }
                    }
                } else if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {                    
                    if (gameRunning) {
                        if (t.isHidden()) {
                            if (t.getFlagged()) {
                                flaggedBombs--;
                            } else {
                                flaggedBombs++;
                            }
                            t.flag();
                        }
                    }
                }
                remainingBombs.setText("" + (numBombs - flaggedBombs));
            }
        });
    }
    
    /**
     * For each tile, tells it how may surrounding bombs it has and updates that field
     */
    private void tileSetSurroundingBombs() {
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                int neighborBombs = neighborBombCount(grid[r][c]);
                grid[r][c].setSurroundingBombs(neighborBombs);
            }
        }   
    }
    
    /**
     * Recursive function that opens up the floor
     * @param t
     */
    private void openFloor(Tile t) {
        int tileRow = t.getRow();
        int tileCol = t.getCol();
        if (t.getSurroudingBombs() == 0) {                  
            for (int r = tileRow - 1; r <= tileRow + 1; r++) {
                for (int c = tileCol - 1; c <= tileCol + 1; c++) {
                    if (r >= 0 && r < gridSize && c >= 0  && c < gridSize) {
                        Tile inspectionTile = grid[r][c]; 
                        if (!(r == tileRow && c == tileCol) && inspectionTile.isHidden() 
                                && !inspectionTile.getFlagged()) {
                            if (!inspectionTile.isBomb()) {
                                inspectionTile.handleTile();
                                if (inspectionTile.getSurroudingBombs() == 0) {
                                    openFloor(inspectionTile);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (int r = tileRow - 1; r <= tileRow + 1; r++) {
                for (int c = tileCol - 1; c <= tileCol + 1; c++) {
                    if (r >= 0 && r < gridSize && c >= 0  && c < gridSize) {
                        Tile inspectionTile = grid[r][c]; 
                        if ((r != tileRow - 1 && c != tileCol - 1) ||
                                (r != tileRow - 1 && c != tileCol + 1) ||
                                (r != tileRow + 1 && c != tileCol - 1) ||
                                (r != tileRow + 1 && c != tileCol + 1) && 
                                !(r == tileRow && c == tileCol) && inspectionTile.isHidden()) {
                            if (!inspectionTile.isBomb()) {
                                inspectionTile.handleTile();
                                if (inspectionTile.getSurroudingBombs() == 0) {
                                    openFloor(inspectionTile);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Handles game every time a tile is clicked
     * @param t
     */
    private void handleGame(Tile t) {
        boolean clickedOnBomb = t.handleTile();        
        if (clickedOnBomb) {
            gameRunning = false;
            status.setText("You Lost! Press Reset to try again!");
            timer.stop();
        } else {
            openFloor(t);
            updateClearedTiles();
        }
        if (clearedTiles >= gridSize * gridSize - numBombs) {
            gameRunning = false;
            status.setText("You Win! Press Reset to play more!");
            Game.resetStopwatch();
            timer.stop();
            String nickname = JOptionPane.showInputDialog(
                    "Congrats! What name would you like to add to the leaderboard?");
            scoreboard.put(nickname, totalMoves);
        }    
    }
    
    /**
     * gets the amount of clearedTiles
     */
    private void updateClearedTiles() {
        int count = 0;
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                if (!grid[r][c].isHidden() && !grid[r][c].isBomb()) {
                    count++;
                }
            }
        }
        clearedTiles = count;
    }
    
    /**
     * Handles the counting of surrounding bombs
     * @param t
     * @return
     */
    private int neighborBombCount(Tile t) {
        int tileRow = t.getRow();
        int tileCol = t.getCol();
        int bombCount = 0;
        for (int r = tileRow - 1; r <= tileRow + 1; r++) {
            for (int c = tileCol - 1; c <= tileCol + 1; c++) {
                if (r >= 0 && r < gridSize && c >= 0  && c < gridSize) {                
                    if (grid[r][c].isBomb()) {
                        bombCount++;
                    }                    
                }
            }
        }
        return bombCount;        
    }
    
   
    /**
     * resets game
     */
    public void reset() { 
        solution = makeBoard();
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                Tile t = grid[r][c];
                t.reset();
                t.repaint();
            }
        }
        tileSetSurroundingBombs();        
        gameRunning = true;
        totalMoves = 0;
        flaggedBombs = 0;
        clearedTiles = 0;
        status.setText("Clear the mines!");
        timer.restart();
        Game.resetStopwatch();
        moves.setText("0");
    }
    
    /**
     * getter for totalMoves
     * @return
     */
    public int getTotalMoves() {
        return totalMoves;
    }
    
    /**
     * getter for boardWidth
     * @return
     */
    public static int getBoardWidth() {
        return boardWidth;
    }
    
    /**
     * getter for boardHeight
     * @return
     */
    public static int getBoardHeight() {
        return boardHeight;
    }
    
    /**
     * getter for gridSize
     * @return
     */
    public static int getGridSize() {
        return gridSize;
    }
    
    /**
     * getter for scoreboard
     * @return
     */
    public Map<String, Integer> getScoreboard() {
        return scoreboard;
    }
    
    /**
     * getter for remainingBombs
     * @return
     */
    public JLabel getRemainingBombs() {
        return remainingBombs;
    }
    
    /**
     * Overrides getPrefferedSize and return new Dimension
     * determined from boardWidth and boardHeight
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(boardWidth, boardHeight);
    }    


}
