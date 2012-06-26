package com.github.axet.starjeweled.core;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_ProfileRGB;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;

import javax.imageio.ImageIO;

public class Capture {

    public static BufferedImage capture() {
        try {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            Robot robot = new Robot();
            BufferedImage img = robot.createScreenCapture(new Rectangle(size));
            return img;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage capture(Rectangle rec) {
        try {
            Robot robot = new Robot();
            BufferedImage img = robot.createScreenCapture(rec);
            return img;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static double getGamma(int screenNumber) {
        GraphicsDevice[] screens = getScreenDevices();
        if (screenNumber >= 0 && screenNumber < screens.length) {
            return getGamma(screens[screenNumber]);
        } else {
            return 0.0;
        }
    }

    public static double getGamma(GraphicsDevice screen) {
        ColorSpace cs = screen.getDefaultConfiguration().getColorModel().getColorSpace();
        if (cs.isCS_sRGB()) {
            return 2.2;
        } else {
            try {
                return ((ICC_ProfileRGB) ((ICC_ColorSpace) cs).getProfile()).getGamma(0);
            } catch (Exception e) {
            }
        }
        return 0.0;
    }

    public static GraphicsDevice getScreenDevice(int screenNumber) throws Exception {
        GraphicsDevice[] screens = getScreenDevices();
        if (screenNumber >= screens.length) {
            throw new Exception("CanvasFrame Error: Screen number " + screenNumber + " not found. " + "There are only "
                    + screens.length + " screens.");
        }
        return screens[screenNumber];// .getDefaultConfiguration();
    }

    public static GraphicsDevice[] getScreenDevices() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
    }

    public static BufferedImage load(File path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(path);
            return img;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static BufferedImage load(File path, Rectangle rect) {
        BufferedImage src = load(path);

        BufferedImage dest = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), src.getType());
        Graphics g = dest.getGraphics();
        g.drawImage(src, 0, 0, (int) rect.getWidth(), (int) rect.getHeight(), (int) rect.getX(), (int) rect.getY(),
                (int) rect.getX() + (int) rect.getWidth(), (int) rect.getY() + (int) rect.getHeight(), null);
        g.dispose();

        return dest;
    }

    public static void save(BufferedImage img, String file) {
        try {
            ImageIO.write(img, "PNG", new File(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage filterGamma(BufferedImage img) {
        Raster r = img.getData();
        DataBufferByte b = (DataBufferByte) r.getDataBuffer();
        byte[] bb = b.getData();
        int[] i = r.getPixel(0, 0, new int[] {});
        System.out.println(String.format("should be 0xf3 %x", img.getRGB(0, 0)));
        return img;
    }
}
