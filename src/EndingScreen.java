import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class EndingScreen extends JFrame {
    private final JFrame PARENT;

    public EndingScreen(GameMode control, JFrame PARENT, String pGameName, Color pBackgroundColor, Color pEdgeColor, String pFirstRow, String pSecondRow, Settings settings) {
        this.PARENT = PARENT;

        // Frame settings
        this.setTitle(pGameName);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(380, 300);
        this.setResizable(true); // TODO true while width is not automatically adjusted
        this.setLocationRelativeTo(null); // Center the window
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());
        this.setLayout(new BorderLayout());
        PARENT.setEnabled(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(pBackgroundColor);

        // Heading
        String filepath;
        int width;
        switch(pGameName) {
            case "Straight Outta":
                filepath = "images\\StraightOutta.png";
                width = 270;
                break;
            case "Lückenfüller":
                filepath = "images\\Lueckenfueller.png";
                width = 270;
                break;
            case "Ärztle":
                filepath = "images\\Aerztle.png";
                width = 150;
                break;
            default:
                filepath = "images\\sahne.png";
                width = 35;
                break;
        }
        JPanel headingPanel = new JPanel();
        ImageIcon icon = new ImageIcon(filepath);
        Image img = icon.getImage().getScaledInstance(width, 35, Image.SCALE_SMOOTH);
        JLabel lHeading = new JLabel(new ImageIcon(img));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        headingPanel.add(lHeading);
        headingPanel.setBackground(pBackgroundColor);
        this.add(headingPanel, BorderLayout.NORTH);

        // Ending text
        JPanel endingTextPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        endingTextPanel.setMaximumSize(new Dimension(340, 60));
        endingTextPanel.setOpaque(false);
        JLabel lFirstRow = new JLabel(pFirstRow, SwingConstants.CENTER);
        lFirstRow.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
        JLabel lSecondRow = new JLabel(pSecondRow, SwingConstants.CENTER);
        lSecondRow.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
        endingTextPanel.add(lFirstRow);
        endingTextPanel.add(lSecondRow);
        mainPanel.add(endingTextPanel);
        

        // Buttons
        JButton bPlayAgain = new JButton("Nochmal spielen");
        bPlayAgain.addActionListener(_ -> {
            control.restartGame();
            this.dispose();
        });

        JButton bBack = new JButton("Zum Hauptmenü");
        bBack.addActionListener(_ -> {
            control.closeGame();
            new MainMenu();
            this.dispose();
        });

        Dimension buttonSize = new Dimension(250, 40);
        
        bPlayAgain.setBorder(new LineBorder(pEdgeColor, 2, true));
        bPlayAgain.setBackground(Color.WHITE);
        bPlayAgain.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
        bPlayAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
        bPlayAgain.setMaximumSize(buttonSize);

        bBack.setBorder(new LineBorder(pEdgeColor, 2, true));
        bBack.setBackground(Color.WHITE);
        bBack.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
        bBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        bBack.setMaximumSize(buttonSize);

        if(settings.isDarkMode()) {
            bPlayAgain.setBackground(new Color(50, 50, 50));
            bBack.setBackground(new Color(50, 50, 50));
        }

        // Add buttons to panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(bPlayAgain);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(bBack);

        this.add(mainPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        PARENT.setEnabled(true);
        PARENT.requestFocus();
    }
}

interface GameMode {
    void openEndingScreen(String p1, String p2);
    void restartGame();
    void closeGame();
}

//TODO automatically adjust frame width