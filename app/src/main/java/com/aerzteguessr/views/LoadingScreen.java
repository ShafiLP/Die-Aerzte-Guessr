package com.aerzteguessr.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;

import javax.swing.JPanel;
import javax.swing.Timer;

public class LoadingScreen extends JPanel {
    private int angle = 0;
    private final Timer timer;

    public LoadingScreen() {
        setOpaque(false);

        timer = new Timer(16, e -> {
            angle = (angle + 6) % 360;
            repaint();
        });
    }

    public void start() {
        setVisible(true);
        timer.start();
    }

    public void stop() {
        timer.stop();
        setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Abdunkelung
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Spinner
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int size = 60;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        g2.setStroke(new BasicStroke(6));
        g2.setColor(Color.WHITE);

        g2.draw(new Arc2D.Double(
                x, y, size, size,
                angle, 270,
                Arc2D.OPEN));

        g2.dispose();
    }
}
