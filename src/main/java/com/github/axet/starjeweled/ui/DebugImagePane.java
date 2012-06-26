package com.github.axet.starjeweled.ui;

import java.awt.Graphics;
import java.awt.Image;

public class DebugImagePane extends ImagePane {

    private static final long serialVersionUID = -775581665000921461L;

    public DebugImagePane() {
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int iconWidth = icon.getIconWidth();
        int iconHeight = icon.getIconHeight();
        double iconAspect = (double) iconHeight / iconWidth;

        int cx = getWidth();
        int cy = getHeight();
        double canvasAspect = (double) cy / cx;

        int x = 0, y = 0;

        // Maintain aspect ratio.
        if (iconAspect < canvasAspect) {
            // Drawing space is taller than image.
            y = cy;
            cy = (int) (cx * iconAspect);
            y = (y - cy) / 2; // center it along vertical
        } else {
            // Drawing space is wider than image.
            x = cx;
            cx = (int) (cy / iconAspect);
            x = (x - cx) / 2; // center it along horizontal
        }

        ;
    }
}
