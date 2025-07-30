import javax.swing.*;
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
        this.setSize(400, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center the window
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40)); // Padding

        // Heading
        JPanel headingPanel = new JPanel();
        JLabel lHeading = new JLabel("ÄrzteGuessr");
        lHeading.setFont(new Font("Folio Extra BT", Font.BOLD, 24));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        headingPanel.add(lHeading);
        this.add(headingPanel, BorderLayout.NORTH);

        // Button panel different game modes
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Buttons
        ImageIcon icon = new ImageIcon("images\\GTO.png");
        Image img = icon.getImage().getScaledInstance(270, 35, Image.SCALE_SMOOTH);
        JButton bGTO = new JButton(new ImageIcon(img));
        bGTO.addActionListener(_ -> {
            this.dispose(); // Close the current gui
            new GTOMenu();  // Start the game
        });

        icon = new ImageIcon("images\\CTL.png");
        img = icon.getImage().getScaledInstance(250, 35, Image.SCALE_SMOOTH);
        JButton bCTL = new JButton(new ImageIcon(img));
        bCTL.addActionListener(_ -> {
            this.dispose(); 
            new CTLMenu();
        });

        icon = new ImageIcon("images\\aerztle.png");
        img = icon.getImage().getScaledInstance(150, 35, Image.SCALE_SMOOTH);
        JButton bAerztle = new JButton(new ImageIcon(img));
        bAerztle.addActionListener(_ -> {
            this.dispose();
            new AerztleMenu();
        });

        Dimension buttonSize = new Dimension(300, 40);
        bGTO.setMaximumSize(buttonSize);
        bCTL.setMaximumSize(buttonSize);
        bAerztle.setMaximumSize(buttonSize);

        bGTO.setAlignmentX(Component.CENTER_ALIGNMENT);
        bCTL.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAerztle.setAlignmentX(Component.CENTER_ALIGNMENT);

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
