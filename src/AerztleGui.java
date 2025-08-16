import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AerztleGui extends JFrame{
    private AerztleGame game;
    private final int TRIES = 7;

    public AerztleGui(AerztleGame pGame) {
        game = pGame;

        // JFrame settings
        this.setTitle("Ärztle");
        this.setSize(800, 700);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null); // Center the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        // JPanel with songs to guess
        JPanel guessingPanel = new JPanel(new GridLayout(1, 1));
        JTextField tfGuess = new JTextField(); //TODO replace with search bar / dropdown
        guessingPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40)); // Padding
        guessingPanel.add(tfGuess);

        // JPanel with categories
        JLabel[][] lTable = new JLabel[7][TRIES];
        for(int x = 0; x < lTable.length; x++) {
            for(int y = 0; y < lTable[0].length; y++) {
                lTable[x][y] = new JLabel("/", SwingConstants.CENTER);
            }
        }
        lTable[0][0].setText("Album");
        lTable[0][1].setText("Erscheinungsjahr");
        lTable[0][2].setText("Spotify Streams");
        lTable[0][3].setText("Dauer");
        lTable[0][4].setText("Wortanzahl");
        lTable[0][5].setText("Sänger");
        lTable[0][6].setText("Single");

        JPanel tablePanel = new JPanel(new GridLayout(7, TRIES));
        for(int x = 0; x < lTable.length; x++) {
            for(int y = 0; y < lTable[0].length; y++) {
                tablePanel.add(lTable[x][y]);
            }
        }
        tablePanel.setBackground(new Color(230, 230, 230));

        // Show GUI
        this.add(guessingPanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
