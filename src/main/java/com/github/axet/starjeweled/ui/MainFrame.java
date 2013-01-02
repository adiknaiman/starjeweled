package com.github.axet.starjeweled.ui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = -629525004019187458L;

    JTabbedPane pane;

    public OutputPane out = new OutputPane();
    public ImagePane capture = new ImagePane();
    public ImagePane mask = new ImagePane();
    public ImagePane noise = new ImagePane();
    public ImagePane bounds = new ImagePane();
    public Bounds2Pane bounds2 = new Bounds2Pane();
    public ImagePane board = new ImagePane();
    public ColoredPane analyse = new ColoredPane();

    public MainFrame() {
        super("Starjeweled");

        ImageIcon icon = createImageIcon("diamond.png");

        pane = new JTabbedPane();
        pane.addTab("Analyse", icon, analyse, "Analyse screen");
        pane.addTab("Capture", icon, capture, "Capture screen");
        pane.addTab("Mask", icon, mask, "Filtered screen");
        pane.addTab("Noise", icon, noise, "Remove noise screen");
        pane.addTab("Bounds", icon, bounds, "Bounds screen");
        // pane.addTab("Bounds2", icon, bounds2, "Bounds2 screen");
        pane.addTab("Board", icon, board, "Cropped board");
        pane.addTab("Output", icon, out, "Application debug output");
        pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        bounds2.frame = this;

        this.getRootPane().setLayout(new BorderLayout());
        this.getRootPane().add(pane, BorderLayout.CENTER);
    }

    static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainFrame.class.getResource(path);
        return new ImageIcon(imgURL);
    }

}
