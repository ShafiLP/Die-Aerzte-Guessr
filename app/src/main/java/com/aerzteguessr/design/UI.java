package com.aerzteguessr.design;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.aerzteguessr.dialog.SettingsDialog;

public final class UI {
    public static JButton createPrimaryButton(String text) {
        return new JButton();
    }
    public static JButton createSecondaryButton(String text) {
        return new JButton();
    }

    /**
     * Creates new round button with unicode symbol as text.
     * @param unicode Unicode symbol.
     * @return JButton with unicode symbol.
     */
    public static JButton iconButton(String unicode) {
        // TODO: Get colours from Theme/css
        return new JButton() {
            {
                setText(unicode);
                setFont(new Font("Segeo UI Symbol", Font.BOLD, 14));
                setForeground(new Color(40, 40, 40));
                setHorizontalTextPosition(SwingConstants.CENTER);
                setPreferredSize(new Dimension(30, 30));
                setFocusPainted(false);
                setBorderPainted(false);
                setContentAreaFilled(false);
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Hintergrund-Kreis (IMMER sichtbar)
                boolean hover = getModel().isRollover();


                g2.setColor(hover
                        ? new Color(80, 80, 80, 50)
                        : new Color(40, 40, 40, 25));

                g2.fillOval(0, 0, getWidth(), getHeight());

                // Symbol
                g2.setColor(new Color(40, 40, 40));
                g2.setFont(getFont());

                FontMetrics fm = g2.getFontMetrics();
                String text = getText();

                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.drawString(text, x, y);

                g2.dispose();
            }
       };
    }

    /**
     * Creates a new button to open general settings.
     * Round icon button with "⚙️" symbol.
     * @return Settings button.
     */
    public static JButton settingsButton() {
        JButton button = iconButton("\u2699");
        button.addActionListener(e -> {
            SettingsDialog dialog = new SettingsDialog();
            dialog.setVisible(true);
        });
        return button;
    }

    /**
     * Creates a new button to quit the application.
     * Round icon button with "❌" symbol.
     * @return Quit button.
     */
    public static JButton quitButton() {
        JButton button = iconButton("\u274C");
        button.addActionListener(e -> {
            // TODO: new exit dialog
            System.exit(0);
        });
        return button;
    }

    public static JPanel createGameModeCard(
        Image titleImage,
        Image background,
        Image icon,
        Color accent,
        Runnable onClick)
    {
        return new JPanel() {

            float hover = 0f;
            boolean hovered = false;

            {
                setOpaque(false);
                setPreferredSize(new Dimension(170, 130));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (onClick != null) onClick.run();
                    }
                });

                new Timer(16, e -> {
                    float speed = 0.10f;
                    hover += (hovered ? speed : -speed);
                    hover = Math.max(0f, Math.min(1f, hover));
                    repaint();
                }).start();
            }

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int arc = 22;

                // =========================
                // SHADOW
                // =========================
                g2.setColor(new Color(0, 0, 0, 90));
                g2.fillRoundRect(4, 6, w - 4, h - 4, arc, arc);

                // =========================
                // CLIP CARD
                // =========================
                g2.setClip(new RoundRectangle2D.Float(
                        0, 0, w, h, arc, arc));

                // =========================
                // BACKGROUND (NO SCALE → ONLY PAN)
                // =========================

                int imgW = background.getWidth(this);
                int imgH = background.getHeight(this);

                // Zoom nur als Ausschnitt (kein Resize!)
                double zoom = 1.5;

                int viewW = (int) (w * zoom);
                int viewH = (int) (h * zoom);

                viewW = Math.min(viewW, imgW);
                viewH = Math.min(viewH, imgH);

                // horizontal zentriert
                int bx = (imgW - viewW) / 2;

                // 🔥 WICHTIG: BOTTOM FIX (kein Überlaufen mehr!)
                int by = imgH - viewH;

                g2.drawImage(
                        background,
                        0, 0, w, h,        // Ziel = Card
                        bx, by, bx + viewW, by + viewH, // Quelle = Ausschnitt
                        this
                );

                // dark overlay
                g2.setColor(new Color(0, 0, 0, 120));
                g2.fillRect(0, 0, w, h);

                // reset clip
                g2.setClip(null);

                // =========================
                // BORDER
                // =========================
                float borderStroke = 2 + hover * 2;

                g2.setStroke(new BasicStroke(borderStroke));
                g2.setColor(accent);

                float half = borderStroke / 2f;

                g2.drawRoundRect(
                        (int)half,
                        (int)half,
                        (int)(w - borderStroke),
                        (int)(h - borderStroke),
                        arc, arc
                );

                // =========================
                // ICON (BIGGER + BETTER)
                // =========================

                int stroke = (int)(2 + hover * 2);
                int inset = (int)Math.ceil(stroke / 2.0);

                int innerBottom = h - inset;
                int innerTop = inset;

                int availableH = innerBottom - innerTop;

                // Originalgröße
                imgW = icon.getWidth(this);
                imgH = icon.getHeight(this);

                // 🔥 BASE SCALE: passt in verfügbare Höhe (contain)
                double baseScale = Math.min(
                        (double) availableH / imgH,
                        (double) (w * 0.9) / imgW
                );

                // Hover-Zoom drauf
                double scale = baseScale * (1.0 + hover * 0.08);

                int drawW = (int)(imgW * scale);
                int drawH = (int)(imgH * scale);

                // Bottom aligned
                int ix = (w - drawW) / 2;
                int iy = innerBottom - drawH;

                g2.drawImage(
                        icon,
                        ix, iy, ix + drawW, iy + drawH,
                        0, 0, imgW, imgH,
                        this
                );

                // =========================
                // TITLE (IMAGE)
                // =========================
                if (titleImage != null) {

                    int maxW = (int) (w * 0.90);
                    int maxH = 40;

                    imgW = titleImage.getWidth(this);
                    imgH = titleImage.getHeight(this);

                    // aspect ratio behalten
                    scale = Math.min((float) maxW / imgW, (float) maxH / imgH);

                    int tw = (int) (imgW * scale);
                    int th = (int) (imgH * scale);

                    int tx = (w - tw) / 2;
                    int ty = h - th - 5;

                    g2.drawImage(titleImage, tx, ty, tw, th, this);
                }
            }
        };
    }
}
