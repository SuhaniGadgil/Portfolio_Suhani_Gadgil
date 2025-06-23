import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.Arrays;

import model.*;
import view.GameBoardPanel;
import view.PlayerTurnPanel;
import controller.GameController;

/**
 * Main class to launch the Santorini game.
 * A class for initialising the game, setting up the GUI, and handling user input.
 */
public class Main {

    /**
     * Launches the game.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Ask for board size
            int boardSize = 5;
            boolean validInput = false;

            while (!validInput) {
                try {
                    String message = String.format("Enter board size (%dx%d to %dx%d):", 5, 5, 10, 10);
                    String input = JOptionPane.showInputDialog(null, message, "5");
                
                    if (input == null) {
                        break;
                    }
                
                    boardSize = Integer.parseInt(input.trim());
                    if (boardSize >= 5 && boardSize <= 10) {
                        validInput = true;
                    } 
                    else {
                        JOptionPane.showMessageDialog(null,
                            "Please enter a number between 5 and 10.",
                            "Invalid Input",
                            JOptionPane.WARNING_MESSAGE);
                    }
                } 
                catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                        "Please enter a valid number.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
                }
            }

            GameFactory factory = new GameFactory();
            Game game = factory.createGame(2, boardSize, new ArrayList<>(Arrays.asList(new Demeter(), new Artemis(), new Zeus())), 4);

            JFrame frame = new JFrame("Santorini");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Add gaps between components
            frame.setLayout(new BorderLayout(10, 10));

            // Side panel to show god cards
            JPanel sidePanel = new JPanel();
            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
            sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            sidePanel.setPreferredSize(new Dimension(250, 0));

            JLabel godCardsTitle = new JLabel("God Cards", SwingConstants.CENTER);
            godCardsTitle.setFont(new Font("Arial", Font.BOLD, 18));
            godCardsTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            sidePanel.add(godCardsTitle);
            sidePanel.add(Box.createVerticalStrut(20));

            // Add god card details
            for (Player player : game.getPlayers()) {
                JPanel godCardPanel = new JPanel();
                godCardPanel.setLayout(new BoxLayout(godCardPanel, BoxLayout.Y_AXIS));
                godCardPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                godCardPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

                // Add player number and their god card name
                JLabel nameLabel = new JLabel("Player " + player.getPlayerNo() + " - " + player.getGodCard().getName());
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                nameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                
                Color playerColor;
                if (player.getPlayerNo() == 1) {
                    playerColor = Color.RED;
                } 
                else {
                    playerColor = Color.BLUE;
                }
                nameLabel.setForeground(playerColor);

                // Add god card image
                JLabel imageLabel = new JLabel(player.getGodCard().getImage());
                imageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                
                // Add description
                JTextArea descArea = new JTextArea(player.getGodCard().getDescription());
                descArea.setWrapStyleWord(true);
                descArea.setLineWrap(true);
                descArea.setEditable(false);
                descArea.setBackground(null);
                descArea.setFont(new Font("Arial", Font.PLAIN, 12));
                descArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                descArea.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                
                godCardPanel.add(Box.createVerticalStrut(10));
                godCardPanel.add(nameLabel);
                godCardPanel.add(Box.createVerticalStrut(10));
                godCardPanel.add(imageLabel);
                godCardPanel.add(Box.createVerticalStrut(10));
                godCardPanel.add(descArea);
                godCardPanel.add(Box.createVerticalStrut(10));
                
                sidePanel.add(godCardPanel);
                sidePanel.add(Box.createVerticalStrut(20));
            }

            JPanel playerInfoPanel = new JPanel();
            playerInfoPanel.setLayout(new GridLayout(game.getPlayers().size(), 1));

            for (Player player : game.getPlayers()) {
                JLabel playerLabel = new JLabel("Player " + player.getPlayerNo() + " - " + 
                player.getGodCard().getName(), SwingConstants.CENTER);

                // Set text colour based on player number
                switch (player.getPlayerNo()) {
                    case 1 -> playerLabel.setForeground(new Color(255, 102, 102));
                    case 2 -> playerLabel.setForeground(new Color(65, 105, 225));
                    default -> playerLabel.setForeground(Color.BLACK);
                }

                playerLabel.setToolTipText(player.getGodCard().getDescription());
                playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerInfoPanel.add(playerLabel);
            }

            JPanel mainGamePanel = new JPanel(new BorderLayout());
            mainGamePanel.add(playerInfoPanel, BorderLayout.NORTH);
            
            // Game board panel
            GameBoardPanel boardPanel = new GameBoardPanel(game.getBoard());
            mainGamePanel.add(boardPanel, BorderLayout.CENTER);

            PlayerTurnPanel turnPanel = new PlayerTurnPanel();
            mainGamePanel.add(turnPanel, BorderLayout.SOUTH);

            frame.add(mainGamePanel, BorderLayout.CENTER);
            frame.add(sidePanel, BorderLayout.EAST);

            // Initialise controller
            new GameController(game, boardPanel, turnPanel);
            turnPanel.updateTurn(game.getCurrentPlayer());

            frame.pack();
            frame.setVisible(true);
        });
    }
}