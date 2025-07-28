import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        this.setTitle("ÄrzteGuessr");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center the window

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
            this.dispose();       // Close the current gui
            new GuessTheOrigin(); // Start the game
        });

        JButton bCTL = new JButton("Lyrics vervollständigen");
        bCTL.addActionListener(_ -> {
            // TODO
        });

        JButton bAerztle = new JButton("Ärztle");
        bAerztle.addActionListener(_ -> {
            // TODO
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
