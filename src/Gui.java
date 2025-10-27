import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.KeyStroke;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Component;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Gui extends JFrame implements EnterKeyListener {
    protected Settings settings;

    // TODO: Replace JSON with GSON
    /**
     * Creates a JComboBox object with all all items from song files as DropdownItem objects
     * @return JComboBox with all songs from .json files
     */
    protected JComboBox<DropdownItem> initializeJComboBox() {
        JComboBox<DropdownItem> comboBox = new JComboBox<>(dropdownArrayFromJson("data\\songs.json"));

        // Add Farin songs if pFarin = true
        if(settings.isFarinEnabled()) {
            JComboBox<DropdownItem> farinDropdown = new JComboBox<>(dropdownArrayFromJson("data\\songsFarin.json"));
            for(int i = 0; i < farinDropdown.getItemCount(); i++) {
                comboBox.addItem(farinDropdown.getItemAt(i));
            }
            farinDropdown = null;
        }

        // Add Bela songs if pBela = true
        if(settings.isBelaEnabled()) {
                JComboBox<DropdownItem> belaDropdown = new JComboBox<>(dropdownArrayFromJson("data\\songsBela.json"));
            for(int i = 0; i < belaDropdown.getItemCount(); i++) {
                comboBox.addItem(belaDropdown.getItemAt(i));
            }
            belaDropdown = null;
        }

        // Add Sahnie songs if pSahnie = true
        if(settings.isSahnieEnabled()) {
                JComboBox<DropdownItem> sahnieDropdown = new JComboBox<>(dropdownArrayFromJson("data\\songsSahnie.json"));
            for(int i = 0; i < sahnieDropdown.getItemCount(); i++) {
                comboBox.addItem(sahnieDropdown.getItemAt(i));
            }
            sahnieDropdown = null;
        }

        // Override the renderer to display items next to the dropdown items
        if(settings.isShowIconsEnabled()) // Only override if icons should be displayed
        comboBox.setRenderer(new DefaultListCellRenderer() {
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

        // Override KeyEvents for the dropdown menu that the space bar works
        comboBox.addKeyListener(new KeyAdapter() {
            private static String keyBuffer = "";
            private static long lastKeyTime = 0;

            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();

                // Space will be a valid char
                if (ch == ' ') {
                    ch = ' ';
                }

                long now = System.currentTimeMillis();
                if (now - lastKeyTime > 1000) {
                    keyBuffer = "";
                }
                lastKeyTime = now;

                if (Character.isLetterOrDigit(ch) || Character.isSpaceChar(ch)) {
                    keyBuffer += ch;

                    // Suche Eintrag, der mit dem Buffer beginnt
                    for (int i = 0; i < comboBox.getItemCount(); i++) {
                        String item = comboBox.getItemAt(i).getDropdownText().toLowerCase();
                        if (item.startsWith(keyBuffer.toLowerCase())) {
                            comboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        }); 
        comboBox.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("SPACE"), "none"); // Prevents closing the dropdown menu by pressing space bar

        comboBox.addKeyListener(new SubmitKeyListener(this));
        return comboBox;
    }

    /**
     * Creates an array of DropdownItems containing song name and icon from the given .json file
     * @param filename path where the song names with icons are located (must contain "song" and "icon" key)
     * @return an array of DropdownItems containing song names and icons
     */
    protected DropdownItem[] dropdownArrayFromJson(String pFilename) {
        LinkedList<DropdownItem> songs = new LinkedList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(pFilename)));
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

    /**
     * Returns a LinkedList with DropDownItems, containing all elements from the given filepath
     * @param pFilepath
     * @return Intitialized LinkedList with DropDownItems, containing all elements from pFilepath
     */
    protected LinkedList<DropdownItem> dropdownListFromJson() {
        LinkedList<DropdownItem> songs = new LinkedList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get("data\\songs.json")));
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
        if(settings.isFarinEnabled()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get("data\\songsFarin.json")));
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
        }
        if(settings.isBelaEnabled()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get("data\\songsBela.json")));
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
        }
        if(settings.isSahnieEnabled()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get("data\\songsSahnie.json")));
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
        }
        
        return songs;
    }
}