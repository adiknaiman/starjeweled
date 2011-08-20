package com.github.axet.starjeweled.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePane extends JPanel {

    private static final long serialVersionUID = -775581665000921461L;

    ImageIcon icon;

    public ImagePane() {
    }

    public void setImage(BufferedImage image) {
        icon = new ImageIcon(image);

        repaint();
    }

    protected void paintComponent(Graphics g) {
        ImageIcon icon = this.icon;

        if (icon == null)
            return;

        int iconWidth = icon.getIconWidth();
        int iconHeight = icon.getIconHeight();
        double iconAspect = (double) iconHeight / iconWidth;

        int w = getWidth();
        int h = getHeight();
        double canvasAspect = (double) h / w;

        int x = 0, y = 0;

        // Maintain aspect ratio.
        if (iconAspect < canvasAspect) {
            // Drawing space is taller than image.
            y = h;
            h = (int) (w * iconAspect);
            y = (y - h) / 2; // center it along vertical
        } else {
            // Drawing space is wider than image.
            x = w;
            w = (int) (h / iconAspect);
            x = (x - w) / 2; // center it along horizontal
        }

        Image img = icon.getImage();
        g.drawImage(img, x, y, w + x, h + y, 0, 0, iconWidth, iconHeight, null);
    }
}
