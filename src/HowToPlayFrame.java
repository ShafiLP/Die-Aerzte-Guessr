import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HowToPlayFrame extends JFrame {
    private final JFrame PARENT;

    /**
     * Opens an HTML file that explains how to play this game
     * @param pPath Path to How To Play HTML file
     */
    public HowToPlayFrame(JFrame PARENT, String pPath) {
        this.PARENT = PARENT;

        // Frame settings
        this.setTitle("Wie man spielt");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(500, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center the window
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());
        this.setLayout(new BorderLayout());
        PARENT.setEnabled(false);

        // Open file
        JEditorPane content = new JEditorPane();
        content.setEditable(false);
        content.setContentType("text/html");
        File htmlFile = new File(pPath);
        try {
            content.setPage(htmlFile.toURI().toURL());
        } catch(IOException e) {
            e.printStackTrace();
        }

        JScrollPane scorllableContent = new JScrollPane(content);

        JButton bBack = new JButton("Verstanden!");
        bBack.addActionListener(_ -> {
            this.dispose();
        });

        this.add(scorllableContent, BorderLayout.CENTER);
        this.add(bBack, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        PARENT.setEnabled(true);
        PARENT.requestFocus();
    }
}
