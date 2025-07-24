import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;

/**
 * Class for the GUI of the game "Guess The Origin"
 */
public class GTOGui extends JFrame{
    GTOGame game;
    private JPanel center;
    private JComboBox<DropdownItem> songDropdown;
    private JLabel lyricLabel = new JLabel();
    private JLabel timerLabel = new JLabel("Timer: 30s", SwingConstants.RIGHT);
    private JLabel scoreLabel = new JLabel("Punktzahl: 0", SwingConstants.LEFT);

    /**
     * Constructor for the GTGGui class
     */
    public GTOGui() {
        this.setTitle("ÄrzteGuessr");
        this.setLayout(new BorderLayout());
        this.pack();
        this.setSize(600, 200);
        this.setLocationRelativeTo(null); // Center the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Creates a GUI for the game "Guess The Origin"
     */
    public void guessTheOriginWindow(GTOGame pGame, String pLyric) {
        game = pGame;
        center = new JPanel();
        center.setLayout(new GridBagLayout());

        //Dropdown menu with all the songs from the file
        songDropdown = new JComboBox<>(readSongsFromJson("data\\songs.json"));
        songDropdown.setRenderer(new DefaultListCellRenderer() {
            // Override the method to customize the rendering of the dropdown items to display icons
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DropdownItem) {
                    DropdownItem item = (DropdownItem) value;
                    label.setText(item.getDropdownText());
                    label.setIcon(item.getDropdownIcon());
                }
                return label;
            }
        });
        songDropdown.addKeyListener(new GuessTheOriginKeyListener(this));

        //Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(_ -> {
            submitButtonPressed();
        });

        // Guessing bar for dropdown menu and submit button
        JPanel guessBar = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        guessBar.add(songDropdown, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        guessBar.add(submitButton, gbc);

        // Lyrics to guess in the center
        lyricLabel.setText("„" + pLyric + "“");
        lyricLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lyricLabel.setFont(new Font("Folio Extra BT", Font.BOLD, 15));
        center.add(lyricLabel);

        // Info bar with score and timer (currentSong just for debugging)
        JPanel infoBar = new JPanel(new GridLayout(1, 3));
        infoBar.add(scoreLabel);
        infoBar.add(new JLabel()); // Placeholder
        infoBar.add(timerLabel);
        infoBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        infoBar.setBackground(Color.LIGHT_GRAY);
        
        // Add all components to the main frame
        this.add(guessBar, BorderLayout.SOUTH);
        this.add(infoBar, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Updates the lyric label with a new lyric
     * @param pLyric the new lyric to display
     */
    public void guessTheOriginUpdate(String pLyric) {
        lyricLabel.setText("„" + pLyric + "“");
    }

    public void setTimerLabel(String text) {
        timerLabel.setText("Timer: " + text);
    }

    public void updateScore(int pScore) {
        scoreLabel.setText("Punktzahl : " + pScore);
    }

    /**
     * Handles the submit button press event and the enter key press event
     */
    public void submitButtonPressed() {
        DropdownItem selected = (DropdownItem) songDropdown.getSelectedItem();
        if(selected.getDropdownText().trim().equals(game.getCurrentSong().trim())) {
                game.songGuessed();
            } else {
                game.wrongGuess();
            }
    }

    /**
     * Reads songs from a JSON file and returns them as an array of DropdownItem
     * @param filename
     * @return an array of DropdownItem containing song names and icons
     */
    private DropdownItem[] readSongsFromJson(String filename) {
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
        return songs.toArray(new DropdownItem[0]);
    }
}
