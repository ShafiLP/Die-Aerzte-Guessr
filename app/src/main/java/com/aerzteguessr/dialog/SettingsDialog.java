package com.aerzteguessr.dialog;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import java.awt.GraphicsEnvironment;

import com.aerzteguessr.Settings;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class SettingsDialog extends JDialog {
    private final Settings SETTINGS;
    private final JFrame PARENT;

    @SuppressWarnings("static-access")
    public SettingsDialog(JFrame PARENT) {
        // 1) Disable parent frame
        super(PARENT);
        this.PARENT = PARENT;
        PARENT.setEnabled(false);

        // 2) Load Settings
        this.SETTINGS = Settings.read();

        // 3) Dialog Settings
        this.setTitle("Einstellungen");
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridLayout(6, 2));

        // 4) Settings UI
        JCheckBox cbShowIcons = new JCheckBox("Icons anzeigen", SETTINGS.isShowIconsEnabled());
        cbShowIcons.setFont(SETTINGS.getFont());

        // Bonus library SETTINGS
        JCheckBox cbFarin = new JCheckBox("Füge Farins Diskografie hinzu", SETTINGS.isFarinEnabled());
        cbFarin.setFont(SETTINGS.getFont());
        JCheckBox cbBela = new JCheckBox("Füge Belas Diskografie hinzu", SETTINGS.isBelaEnabled());
        cbBela.setFont(SETTINGS.getFont());
        JCheckBox cbSahnie = new JCheckBox("Füge Sahnies Diskografie hinzu", SETTINGS.isSahnieEnabled());
        cbSahnie.setFont(SETTINGS.getFont());

        // Font type
        JPanel panFontType = new JPanel(new GridLayout(2, 1));
        panFontType.add(new JLabel("Schriftart") {{
            setFont(SETTINGS.getFont());
        }});
        JComboBox<String> cbFont = new JComboBox<>();
        // Load all fonts from system
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies  = ge.getAvailableFontFamilyNames();
        for (String f : fontFamilies) {
            cbFont.addItem(f);
        }
        cbFont.setSelectedItem(SETTINGS.getFontType());
        panFontType.add(cbFont);

        // Font size
        JPanel panFontSize = new JPanel(new GridLayout(2, 1));
        panFontSize.add(new JLabel("Schriftgröße") {{
            setFont(SETTINGS.getFont());
        }});
        JSlider slFontSize = new JSlider(8, 15, SETTINGS.getFontSize());
        slFontSize.setPaintLabels(true);
        slFontSize.setLabelTable(slFontSize.createStandardLabels(1));
        slFontSize.setSnapToTicks(true);
        slFontSize.setFont(SETTINGS.getFont());
        panFontSize.add(slFontSize);
        this.add(panFontSize);

        // Colourful GUI
        JCheckBox cbColourfulGui = new JCheckBox("Farbiges Design", SETTINGS.isColourfulGuiEnabled());
        cbColourfulGui.setFont(SETTINGS.getFont());

        // Automatic search for updates
        JCheckBox cbSearchForUpdates = new JCheckBox("Suche nach Updates", SETTINGS.isSearchForUpdatesEnabled());
        cbSearchForUpdates.setFont(SETTINGS.getFont());

        // Darkmode
        JCheckBox cbDarkMode = new JCheckBox("Dunkler Modus", SETTINGS.isDarkMode());
        cbDarkMode.setFont(SETTINGS.getFont());

        // Reset all Settings button
        JButton bReset = new JButton("Einstellungen zurücksetzen");
        bReset.setFont(SETTINGS.getFont());
        bReset.addActionListener(e -> {
            SETTINGS.write(new Settings());
            this.dispose();
        });

        // Save button
        JButton bSave = new JButton("Speichern");
        bSave.setFont(SETTINGS.getFont());
        bSave.addActionListener(e -> {
            SETTINGS.setFontType(cbFont.getSelectedItem().toString());
            SETTINGS.setFontSize(slFontSize.getValue());
            SETTINGS.setShowIcons(cbShowIcons.isSelected());
            SETTINGS.setFarinLibrary(cbFarin.isSelected());
            SETTINGS.setBelaLibrary(cbBela.isSelected());
            SETTINGS.setSahnieLibrary(cbSahnie.isSelected());
            SETTINGS.setColourfulGui(cbColourfulGui.isSelected());
            SETTINGS.setSearchForUpdates(cbSearchForUpdates.isSelected());
            SETTINGS.setDarkMode(cbDarkMode.isSelected());
            if (cbDarkMode.isSelected()) {
                FlatDarkLaf.setup();
            } else {
                FlatLightLaf.setup();
            }
            FlatLaf.updateUI();

            Settings.write(SETTINGS);
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
        this.add(cbSearchForUpdates);
        this.add(cbColourfulGui);
        this.add(new JLabel());
        this.add(bReset);
        this.add(bSave);
    }

    // Override dispose() method so parent gets enabled and focus after closing this frame
    @Override
    public void dispose() {
        super.dispose();
        PARENT.setEnabled(true);
        PARENT.requestFocus();
    }
}
