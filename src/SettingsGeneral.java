import javax.swing.*;
import java.awt.*;

public class SettingsGeneral extends JFrame {
    private final JFrame PARENT;

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

        // Bonus library settings
        JCheckBox cbFarin = new JCheckBox("Füge Farins Diskografie hinzu", settings.isFarinEnabled());
        JCheckBox cbBela = new JCheckBox("Füge Belas Diskografie hinzu", settings.isBelaEnabled());
        JCheckBox cbSahnie = new JCheckBox("Füge Sahnies Diskografie hinzu", settings.isSahnieEnabled());

        // Font type settings
        JPanel fontTypePanel = new JPanel(new GridLayout(2, 1));
        fontTypePanel.add(new JLabel("Schriftart"));
        JComboBox<String> cbFont = new JComboBox<>();
        // Load all fonts from system
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies  = ge.getAvailableFontFamilyNames();
        for (String f : fontFamilies) {
            cbFont.addItem(f);
        }
        cbFont.setSelectedItem(settings.getFontType());
        fontTypePanel.add(cbFont);

        // Font size settings
        JPanel panFontSize = new JPanel(new GridLayout(2, 1));
        panFontSize.add(new JLabel("Schriftgröße"));
        JSlider slFontSize = new JSlider(8, 15, settings.getFontSize());
        slFontSize.setPaintLabels(true);
        slFontSize.setLabelTable(slFontSize.createStandardLabels(1));
        slFontSize.setSnapToTicks(true);
        panFontSize.add(slFontSize);
        this.add(panFontSize);

        // Colourful GUI settings
        JCheckBox cbColourfulGui = new JCheckBox("Farbiges Design", settings.isColourfulGuiEnabled());

        // Automatic search for updates
        JCheckBox cbSearchForUpdates = new JCheckBox("Suche nach Updates", settings.isSearchForUpdatesEnabled());

        // Darkmode settings
        JCheckBox cbDarkMode = new JCheckBox("Dunkler Modus", settings.isDarkMode());

        // Reset all settings button
        JButton bReset = new JButton("Einstellungen zurücksetzen");
        bReset.addActionListener(_ -> {
            Settings.write(new Settings());
            this.dispose();
        });

        // Save button
        JButton bSave = new JButton("Speichern");
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

            Settings.write(settings);
            this.dispose();
        });

        // Add all to frame
        this.add(cbDarkMode);
        this.add(new JLabel());
        this.add(fontTypePanel);
        this.add(cbFarin);
        this.add(panFontSize);
        this.add(cbBela);
        this.add(cbShowIcons);
        this.add(cbSahnie);
        this.add(cbColourfulGui);
        this.add(cbSearchForUpdates);
        this.add(bReset);
        this.add(bSave);

        this.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        PARENT.setEnabled(true);
        PARENT.requestFocus();
    }
}
