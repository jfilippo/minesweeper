import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Main Class in the Minesweeper Game
 * @author jfilippo
 *
 */
public class Game implements Runnable {
    
    private static int stopwatch = 0;
    
    public static int getStopwatch() {
        return stopwatch;
    }
    
    public static void resetStopwatch() {
        stopwatch = 0;
    }

    public void run() {
        // creates and positions the top-level frame
        final JFrame frame = new JFrame("MINESWEEPER");
        frame.setLayout(new BorderLayout());
        frame.setLocation(1000, 1000);
        frame.setPreferredSize(new Dimension(Board.getBoardWidth() + 200, 
                Board.getBoardHeight() + 200));
        
        //create status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        // add status label to status_panel
        final JLabel status = new JLabel("Clear the mines!");
        status.setFont(new Font("Arial", Font.PLAIN, 24));
        status_panel.add(status);
        
        // creates timer box
        final JLabel timerBox = new JLabel("00:00");
        timerBox.setFont(new Font("Arial", Font.PLAIN, 18));
        
        timerBox.setHorizontalAlignment(JLabel.CENTER);      
        TitledBorder timerBoxTitle = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 4), "Timer");
        timerBoxTitle.setTitleJustification(TitledBorder.CENTER);
        timerBoxTitle.setTitleFont(new Font("Arial", Font.PLAIN, 18));
        timerBox.setBorder(timerBoxTitle);   
        timerBox.setPreferredSize(new Dimension(120, 80));
        
        // create timer
        final Timer t = new Timer(1000, new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                stopwatch++;
                timerBox.setText(format());
                timerBox.setHorizontalAlignment(JLabel.CENTER);
                timerBox.setFont(new Font("Arial", Font.PLAIN, 18));
            }
            
            public String format() {
                String formattedTime = "";
                int minutes = stopwatch / 60;
                if (minutes < 10) {
                    formattedTime += "0" + minutes;
                } else {
                    formattedTime += minutes;
                }
                int seconds = stopwatch - minutes * 60;
                if (seconds < 10) {
                    formattedTime += ":0" + seconds;
                } else {
                    formattedTime += ":" + seconds;
                }
                return formattedTime;
            }
        });       

        // creates left side tab panel for instructions and leaderboard display
        final JPanel tab = new JPanel();
        tab.setLayout(new GridLayout(3, 1));      
        tab.setPreferredSize(new Dimension(240, 1000));
        frame.add(tab, BorderLayout.WEST);

        // total moves label
        final JLabel totalMoves = new JLabel("0");
        totalMoves.setHorizontalAlignment(JLabel.CENTER);
        totalMoves.setPreferredSize(new Dimension(150, 80));
        TitledBorder totalMovesTitle = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 4), "Total Moves");
        totalMovesTitle.setTitleJustification(TitledBorder.CENTER);
        totalMovesTitle.setTitleFont(new Font("Arial", Font.PLAIN, 18));
        totalMoves.setBorder(totalMovesTitle);   
        totalMoves.setFont(new Font("Arial", Font.PLAIN, 18));
        // remainingBombs label
        final JLabel remainingBombs = new JLabel("");
        remainingBombs.setPreferredSize(new Dimension(200, 80));
        TitledBorder remainingBombsTitle = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 4), "Remaining Bombs");
        remainingBombsTitle.setTitleJustification(TitledBorder.CENTER);
        remainingBombsTitle.setTitleFont(new Font("Arial", Font.PLAIN, 18));
        remainingBombs.setBorder(remainingBombsTitle);
        remainingBombs.setFont(new Font("Arial", Font.PLAIN, 18));
        remainingBombs.setHorizontalAlignment(JLabel.CENTER);

        
        // Reset button
        final JPanel control_panel  = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        
        // creates instruction button
        final JButton instructions = new JButton("Instructions");
        instructions.setFont(new Font("Arial", Font.PLAIN, 18));
        instructions.setPreferredSize(new Dimension(120, 200));
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(instructions, 
                        "This is Minesweeper, like the traditional game, "
                        + "we have tiles with hidden mines. \n"
                        + "The number on each tile represents the number of mines surround"
                        + "ing that tile! \n"
                        + "Clear out the tiles and have fun! Avoid the mines! \n"
                        + "Left click to clear out tiles, right click to flag potential mines! \n" 
                        + "Click Reset to restart! \n"
                        + "Win to be added to the leaderboard and click on the Leaderboard "
                        + "to see scores!", 
                        "Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        
        // creates CENTER panel with game grid
        final Board board = new Board(status, totalMoves, remainingBombs, t);
        frame.add(board, BorderLayout.CENTER);
        
        // creates scoreboard button
        final JButton fullScoreboard = new JButton("Detailed Scoreboard");
        fullScoreboard.setFont(new Font("Arial", Font.PLAIN, 18));
        fullScoreboard.setPreferredSize(new Dimension(120, 200));
        fullScoreboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Map<String, Integer> scores = board.getScoreboard();
                String message = "";
                for (Entry<String, Integer> entry : scores.entrySet()) {
                    message += "Player: " + entry.getKey() + "    Total Moves: " 
                            + entry.getValue() + "\n";
                }
                JOptionPane.showMessageDialog(fullScoreboard, 
                        message, "Scoreboard", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // handles reset button
        final JButton reset = new JButton("Reset");
        reset.setPreferredSize(new Dimension(120, 200));
        reset.setFont(new Font("Arial", Font.PLAIN, 18));
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(remainingBombs);
        control_panel.add(timerBox);
        control_panel.add(totalMoves);
        
        tab.add(instructions);
        tab.add(reset);
        tab.add(fullScoreboard);
                

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        board.reset();        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
    
}
