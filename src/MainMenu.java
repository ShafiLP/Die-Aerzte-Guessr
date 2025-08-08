import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;

/**
 * Class for the game main menu
 * Contains paths to all games
 */
public class MainMenu extends JFrame {

    /**
     * Constructor of main menu
     * Contains paths to all games
     */
    public MainMenu() {
        this.setTitle("ÄrzteGuessr");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 380);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center the window
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40)); // Padding

        // Heading
        JPanel headingPanel = new JPanel();
        ImageIcon icon = new ImageIcon("images\\AerzteGuessr.png");
        Image img = icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        JLabel lHeading = new JLabel(new ImageIcon(img));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        headingPanel.add(lHeading);
        this.add(headingPanel, BorderLayout.NORTH);

        // Button panel different game modes
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Buttons
        icon = new ImageIcon("images\\GTO.png");
        img = icon.getImage().getScaledInstance(270, 35, Image.SCALE_SMOOTH);
        JButton bGTO = new JButton(new ImageIcon(img));
        bGTO.addActionListener(_ -> {
            this.dispose(); // Close the current gui
            new GTOMenu();  // Start the game
        });
        bGTO.setBorder(new LineBorder(new Color(150, 100, 100), 2, true));
        bGTO.setBackground(new Color(255, 220, 220));

        icon = new ImageIcon("images\\CTL.png");
        img = icon.getImage().getScaledInstance(260, 35, Image.SCALE_SMOOTH);
        JButton bCTL = new JButton(new ImageIcon(img));
        bCTL.addActionListener(_ -> {
            this.dispose(); 
            new CTLMenu();
        });
        bCTL.setBorder(new LineBorder(new Color(100, 100, 150), 2, true));
        bCTL.setBackground(new Color(220, 220, 255));

        icon = new ImageIcon("images\\aerztle.png");
        img = icon.getImage().getScaledInstance(140, 35, Image.SCALE_SMOOTH);
        JButton bAerztle = new JButton(new ImageIcon(img));
        bAerztle.addActionListener(_ -> {
            this.dispose();
            new AerztleMenu();
        });
        bAerztle.setBorder(new LineBorder(new Color(100, 150, 100), 2, true));
        bAerztle.setBackground(new Color(220, 255, 220));

        Dimension buttonSize = new Dimension(300, 40);
        bGTO.setMaximumSize(buttonSize);
        bCTL.setMaximumSize(buttonSize);
        bAerztle.setMaximumSize(buttonSize);

        bGTO.setAlignmentX(Component.CENTER_ALIGNMENT);
        bCTL.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAerztle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Author + Version
        JPanel authorVersionPanel = new JPanel(new GridLayout(1, 2));
        JLabel lVersion = new JLabel("Version 0.1.6", SwingConstants.LEFT);
        JLabel lAuthor = new JLabel("@ShafiLP", SwingConstants.RIGHT);
        authorVersionPanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 15));
        authorVersionPanel.add(lVersion);
        authorVersionPanel.add(lAuthor);
        this.add(authorVersionPanel, BorderLayout.SOUTH);

        // Add with vertical padding
        buttonPanel.add(bGTO);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(bCTL);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(bAerztle);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        this.add(mainPanel);
        this.setVisible(true);
    }
}
