import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class SettingsGeneral extends JFrame {
    private final JFrame PARENT; // Parent component

    /**
     * Constructor of SettingsGeneral
     * Creates new frame with settings to be configured by user
     * @param PARENT Parent component
     * @param settings Settings object
     */
    public SettingsGeneral(JFrame PARENT, Settings settings) {
        this.PARENT = PARENT;

        // Frame settings
        this.setTitle("Einstellungen");
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridLayout(6, 2));
        PARENT.setEnabled(false);

        // Icon settings
        JCheckBox cbShowIcons = new JCheckBox("Icons anzeigen", settings.isShowIconsEnabled());
        cbShowIcons.setFont(settings.getFont());

        // Bonus library settings
        JCheckBox cbFarin = new JCheckBox("Füge Farins Diskografie hinzu", settings.isFarinEnabled());
        cbFarin.setFont(settings.getFont());
        JCheckBox cbBela = new JCheckBox("Füge Belas Diskografie hinzu", settings.isBelaEnabled());
        cbBela.setFont(settings.getFont());
        JCheckBox cbSahnie = new JCheckBox("Füge Sahnies Diskografie hinzu", settings.isSahnieEnabled());
        cbSahnie.setFont(settings.getFont());

        // Font type settings
        JPanel panFontType = new JPanel(new GridLayout(2, 1));
        panFontType.add(new JLabel("Schriftart") {{
            setFont(settings.getFont());
        }});
        JComboBox<String> cbFont = new JComboBox<>();
        // Load all fonts from system
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies  = ge.getAvailableFontFamilyNames();
        for (String f : fontFamilies) {
            cbFont.addItem(f);
        }
        cbFont.setSelectedItem(settings.getFontType());
        panFontType.add(cbFont);

        // Font size settings
        JPanel panFontSize = new JPanel(new GridLayout(2, 1));
        panFontSize.add(new JLabel("Schriftgröße") {{
            setFont(settings.getFont());
        }});
        JSlider slFontSize = new JSlider(8, 15, settings.getFontSize());
        slFontSize.setPaintLabels(true);
        slFontSize.setLabelTable(slFontSize.createStandardLabels(1));
        slFontSize.setSnapToTicks(true);
        slFontSize.setFont(settings.getFont());
        panFontSize.add(slFontSize);
        this.add(panFontSize);

        // Colourful GUI settings
        JCheckBox cbColourfulGui = new JCheckBox("Farbiges Design", settings.isColourfulGuiEnabled());
        cbColourfulGui.setFont(settings.getFont());

        // Automatic search for updates
        JCheckBox cbSearchForUpdates = new JCheckBox("Suche nach Updates", settings.isSearchForUpdatesEnabled());
        cbSearchForUpdates.setFont(settings.getFont());

        // Darkmode settings
        JCheckBox cbDarkMode = new JCheckBox("Dunkler Modus", settings.isDarkMode());
        cbDarkMode.setFont(settings.getFont());

        // Reset all settings button
        JButton bReset = new JButton("Einstellungen zurücksetzen");
        bReset.setFont(settings.getFont());
        bReset.addActionListener(_ -> {
            Settings.write(new Settings());
            this.dispose();
        });

        // Save button
        JButton bSave = new JButton("Speichern");
        bSave.setFont(settings.getFont());
        bSave.addActionListener(_ -> {
            settings.setFontType(cbFont.getSelectedItem().toString());
            settings.setFontSize(slFontSize.getValue());
            settings.setShowIcons(cbShowIcons.isSelected());
            settings.setFarinLibrary(cbFarin.isSelected());
            settings.setBelaLibrary(cbBela.isSelected());
            settings.setSahnieLibrary(cbSahnie.isSelected());
            settings.setColourfulGui(cbColourfulGui.isSelected());
            settings.setSearchForUpdates(cbSearchForUpdates.isSelected());
            settings.setDarkMode(cbDarkMode.isSelected());
            if (cbDarkMode.isSelected()) {
                FlatDarkLaf.setup();
            } else {
                FlatLightLaf.setup();
            }
            FlatLaf.updateUI();

            Settings.write(settings);
            this.dispose();
        });

        // Add all to frame
        this.add(cbDarkMode);
        this.add(cbFarin);
        this.add(panFontType);
        this.add(cbBela);
        this.add(panFontSize);
        this.add(cbSahnie);
        this.add(cbShowIcons);
        this.add(new JLabel());
        this.add(cbColourfulGui);
        this.add(cbSearchForUpdates);
        this.add(bReset);
        this.add(bSave);

        this.setVisible(true);
    }

    // Override dispose() method so parent gets enabled and focus after closing this frame
    @Override
    public void dispose() {
        super.dispose();
        PARENT.setEnabled(true);
        PARENT.requestFocus();
    }
}
