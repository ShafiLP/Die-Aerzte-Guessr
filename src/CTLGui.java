import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Image;

public class CTLGui extends JFrame{
    private Settings settings;
    private CTLGame game;

    private JPanel infoBar;

    private JLabel lTimer;
    private JLabel lScore;
    private JLabel lBefore;
    private JLabel lAfter;
    private JTextField tfInput;

    public CTLGui(CTLGame pGame, Settings pSettings, String pBeforeGap, String pAfterGap, String pSongName, String pAlbumName) {
        game = pGame;

        // Read settings
        settings = pSettings;
        lTimer = new JLabel("Timer: " + settings.getCtlTimeLimit() + "s", SwingConstants.CENTER);
        lScore = new JLabel("Punktzahl: " + 0);

        tfInput = new JTextField();
        lBefore = new JLabel(pBeforeGap, SwingConstants.CENTER);
        lAfter = new JLabel(pAfterGap, SwingConstants.CENTER);

        // JFrame settings
        this.setTitle("Lückenfüller");
        this.setSize(600, 300);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null); // Center the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        // LyricPanel for GridLayout
        JPanel lyricPanel = new JPanel(new GridLayout(3, 1));
        lyricPanel.add(lBefore);
        lyricPanel.add(tfInput);
        lyricPanel.add(lAfter);

        // Main panel for lyric display
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(lyricPanel, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.VERTICAL;
        }});

        // Lower bar for submit and hint button
        JPanel lowerPanel = new JPanel(new GridBagLayout());
        JButton bSubmit = new JButton("Raten");
        bSubmit.addActionListener(_ -> {
            game.submitPressed(tfInput.getText());
        });
        JButton bHint = new JButton("Hinweis");
        bHint.addActionListener(_ -> {
            //TODO
        });
        lowerPanel.add(bSubmit, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0;
            anchor = GridBagConstraints.LINE_END;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        lowerPanel.add(bHint, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Song name and album cover in the upper middle
        JPanel mPanel = new JPanel(new GridBagLayout());
        ImageIcon albumIcon = new ImageIcon((new ImageIcon(pAlbumName)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        JLabel lAlbum = new JLabel(albumIcon);
        JLabel lSongName = new JLabel(pSongName, SwingConstants.CENTER);
        mPanel.add(lAlbum, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        mPanel.add(lSongName, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        mPanel.setOpaque(false);

        // Timer and Score in upper right corner
        JPanel urPanel = new JPanel(new GridLayout(2, 1));
        urPanel.add(lTimer);
        urPanel.add(lScore);
        urPanel.setOpaque(false);

        // Info bar for health, current song, timer, etc.
        infoBar = new JPanel(new GridBagLayout());
        infoBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        infoBar.setBackground(Color.LIGHT_GRAY);
        infoBar.add(mPanel, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});
        infoBar.add(urPanel, new GridBagConstraints() {{
            gridx = 2;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.LINE_END;
            fill = GridBagConstraints.NONE;
        }});
        
        // Add all components to the main frame
        this.add(infoBar, BorderLayout.NORTH);
        this.add(lowerPanel, BorderLayout.SOUTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
