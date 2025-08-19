import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.json.JSONArray;
import org.json.JSONObject;

public class AerztleGui extends JFrame implements EnterKeyListener {
    private AerztleGame game;
    private final int TRIES = 7;
    private JLabel[][] lTable;

    private AutoCompleteTextField songSearchBar;

    public AerztleGui(AerztleGame pGame) {
        game = pGame;

        // JFrame settings
        this.setTitle("Ärztle");
        this.setSize(900, 700);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null); // Center the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        // JPanel with songs to guess
        JPanel guessingPanel = new JPanel(new GridBagLayout());

        // TODO changable with settings
        LinkedList<DropdownItem> suggestions = dropdownListFromJson("data\\songs.json");
        songSearchBar = new AutoCompleteTextField(suggestions);
        songSearchBar.addKeyListener(new SubmitKeyListener(this));

        JButton bSubmit = new JButton("Raten");
        bSubmit.addActionListener(_ -> {
            submitButtonPressed();
        });
        guessingPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40)); // Padding
        guessingPanel.add(songSearchBar, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0.9;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        guessingPanel.add(bSubmit, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0.1;
            anchor = GridBagConstraints.LINE_END;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // JPanel with categories
        lTable = new JLabel[TRIES+1][8];
        for(int x = 0; x < lTable.length; x++) {
            for(int y = 0; y < lTable[0].length; y++) {
                lTable[x][y] = new JLabel("", SwingConstants.CENTER);
            }
        }
        lTable[0][0].setText("Name");
        lTable[0][1].setText("Album");
        lTable[0][2].setText("Erscheinungsjahr");
        lTable[0][3].setText("Spotify Streams");
        lTable[0][4].setText("Dauer");
        lTable[0][5].setText("Wortanzahl");
        lTable[0][6].setText("Sänger");
        lTable[0][7].setText("Single");

        JPanel tablePanel = new JPanel(new GridLayout(TRIES+1, 8));
        for(int x = 0; x < lTable.length; x++) {
            for(int y = 0; y < lTable[0].length; y++) {
                tablePanel.add(lTable[x][y]);
                lTable[x][y].setOpaque(true);
                lTable[x][y].setBorder(new LineBorder(getForeground(), 1));
            }
        }
        tablePanel.setBackground(new Color(230, 230, 230));

        // Show GUI
        this.add(guessingPanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void submitButtonPressed() {
        game.submitButtonPressed(songSearchBar.getText());
    }

    public void paintSongName(int pIndex, Color pColor, String pText) {
        lTable[pIndex][0].setBackground(pColor);
        lTable[pIndex][0].setText(pText);
    }

    public void paintAlbum(int pIndex, Color pColor, String pText) {
        lTable[pIndex][1].setBackground(pColor);
        ImageIcon albumIcon = new ImageIcon("images\\" + pText + ".png");
        Image albumImg = albumIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        lTable[pIndex][1].setIcon(new ImageIcon(albumImg));
    }

    public void paintReleaseYear(int pIndex, Color pColor, String pText) {
        lTable[pIndex][2].setBackground(pColor);
        lTable[pIndex][2].setText(pText);
    }

    public void paintStreams(int pIndex, Color pColor, String pText) {
        lTable[pIndex][3].setBackground(pColor);
        lTable[pIndex][3].setText(pText);
    }

    public void paintDuration(int pIndex, Color pColor, String pText) {
        lTable[pIndex][4].setBackground(pColor);
        lTable[pIndex][4].setText(pText);
    }

    public void paintWordCount(int pIndex, Color pColor, String pText) {
        lTable[pIndex][5].setBackground(pColor);
        lTable[pIndex][5].setText(pText);
    }

    public void paintSinger(int pIndex, Color pColor, String pText) {
        lTable[pIndex][6].setBackground(pColor);
        lTable[pIndex][6].setText(pText);
    }

    public void paintSingle(int pIndex, Color pColor, String pText) {
        lTable[pIndex][7].setBackground(pColor);
        lTable[pIndex][7].setText(pText);
    }

    private LinkedList<DropdownItem> dropdownListFromJson(String filename) {
        LinkedList<DropdownItem> songs = new LinkedList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String songName = obj.getString("song");
                String iconPath = obj.getString("icon");
                Icon icon = new ImageIcon("images\\" + iconPath + ".png");
                songs.add(new DropdownItem(songName, icon));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }
}
