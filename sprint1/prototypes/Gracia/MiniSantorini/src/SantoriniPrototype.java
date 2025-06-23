import javax.swing.*;
import java.awt.*;

public class SantoriniPrototype {
    private static final int size = 5;

    public SantoriniPrototype() {
        JFrame frame = new JFrame("Mini Santorini");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);

        JPanel boardPanel = new JPanel(new GridLayout(size, size));
        JLabel statusLabel = new JLabel("Select a cell to place a worker.");

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                JButton button = new JButton();
                button.setBackground(Color.lightGray);
                int r = row, c = col;
                button.addActionListener(e -> {
                    button.setText("W");
                    statusLabel.setText("Worker placed at (" + r + ", " + c + ").");
                });
                boardPanel.add(button);
            }
        }

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SantoriniPrototype::new);
    }
}