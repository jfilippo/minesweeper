import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Class that defined Tile object
 * Implements functionality for the object used as a building block
 * of a minesweeper game. By extending JPanel, the Tile class can be
 * constructed and interacted with through ActionListeners that may
 * be added.
 * @author jfilippo
 *
 */

@SuppressWarnings("serial")
public class Tile extends JPanel {
    
    // private fields
    private int row;
    private int col;

    private boolean isBomb;
    private boolean isFlagged;
    
    private boolean isHidden;
    private int neighborBombCount;    
    
    // constants
    public final static int WIDTH = Board.getBoardWidth() / Board.getGridSize();
    public final static int HEIGHT = Board.getBoardHeight() / Board.getGridSize();
    
    /**
     * Tile constructor
     * @param row
     * @param col
     * @param isBomb
     */
    public Tile(int row, int col, boolean isBomb) {
        this.row = row;
        this.col = col;
        this.isBomb = isBomb;
        this.isFlagged = false;
        this.isHidden = true;
        this.neighborBombCount = 0;
    }    
    
    /**
     * resets a tile to original state
     */
    public void reset() {
        this.isHidden = true;
        neighborBombCount = 0;
        if (isFlagged) {
            flag();
        }
    }
    
    /**
     * handles when a tile is clicked
     * @return boolean returns true if happens to be a bomb tile
     */
    public boolean handleTile() {
        this.isHidden = false;
        repaint();
        if (this.isBomb) {
            return true;
        }
        return false;
    }
    
    /**
     * flags a tile
     */
    public void flag() {
        isFlagged = !isFlagged;
        repaint();
    }

    /**
     * getter for Tile row
     * @return
     */
    public int getRow() {
        return row;
    }
    
    /**
     * getter for Tile column
     * @return
     */
    public int getCol() {
        return col;
    }
    
    /**
     * getter for if that tile contains a bomb
     * @return
     */
    public boolean isBomb() {
        return isBomb;
    }
    
    /**
     * getter for if that tile is hidden or revealed
     * @return boolean isHidden
     */
    public boolean isHidden() {
        return isHidden;
    }
    
    /**
     * getter for if a tile is "flagged"
     * @return
     */
    public boolean getFlagged() {
        return isFlagged;
    }
        
    /**
     * setter for surroundingBombs
     * @param bombCount
     */
    public void setSurroundingBombs(int bombCount) {
        this.neighborBombCount = bombCount;
    }
    
    /**
     * getter for surroundingBombs
     * @return
     */
    public int getSurroudingBombs() {
        return neighborBombCount;
    }
    
    /**
     * draws tile if already revealed
     */
    public void drawHidden(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH, HEIGHT);        
    }
    
    /**
     * draws Tile if revealed
     */
    public void drawRevealed(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);        
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH, HEIGHT);    
        if (!isBomb) {
            if (this.neighborBombCount != 0) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.drawString(this.neighborBombCount + "", 1 * WIDTH / 3, 7 * HEIGHT / 12);
            }
        } else {
            g.setColor(Color.WHITE);
            g.drawLine(0, 0, WIDTH, HEIGHT);
            g.drawLine(0, HEIGHT, WIDTH, 0);
        }
    }
    
    /**
     * draws flagged tiles
     * @param g
     */
    public void drawFlagged(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.RED);
        g.fillRect(WIDTH / 4, HEIGHT / 4, WIDTH / 2, HEIGHT / 2);
    }
    
    /**
     * Overrides the paint component method and draws the tile appropriately.
     */
    @Override
    public void paintComponent(Graphics g) {
        if (isHidden) {
            if (!isFlagged) {
                drawHidden(g);
            } else {
                drawFlagged(g);
            }
        } else {
            drawRevealed(g);
        }
    }

}
