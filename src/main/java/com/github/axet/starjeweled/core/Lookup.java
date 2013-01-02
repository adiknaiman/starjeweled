package com.github.axet.starjeweled.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.Kernel;
import java.awt.image.RGBImageFilter;
import java.awt.image.Raster;
import java.io.File;
import java.util.Arrays;

import com.github.axet.starjeweled.common.RangeColor;
import com.jhlabs.image.DoGFilter;
import com.jhlabs.image.EdgeFilter;
import com.jhlabs.image.LaplaceFilter;

public class Lookup {

    public static class NotFound extends RuntimeException {

        private static final long serialVersionUID = 5393563026702192412L;

    }

    static final int WHITE_MARK = 0xffff0000;
    static final int BLACK_MARK = 0xff00ff00;

    static class BoardFilter extends RGBImageFilter {

        public BoardFilter() {
        }

        public int filterRGB(int x, int y, int rgb) {
            int na = rgb & 0x00ffffff;

            RangeColor white = ColorsTable.TABLE_WHITE;
            if (white.inRange(na))
                return WHITE_MARK;

            RangeColor black = ColorsTable.TABLE_BLACK;
            if (black.inRange(na)) // black
                return BLACK_MARK;

            return 0x00000000;
        }
    }

    public BufferedImage convert(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
        return bufferedImage;
    }

    public BufferedImage crop(BufferedImage image, Rectangle r) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
        g2.setColor(new Color(0xff0000ff));
        g2.fillRect(r.x, r.y, r.width, r.height);
        return bufferedImage;
    }

    /**
     * filter screen for board colors
     * 
     * @return
     */
    public BufferedImage filterMask(Image img) {
        ImageFilter filter = new BoardFilter();
        FilteredImageSource fimg = new FilteredImageSource(img.getSource(), filter);
        Image newimg = Toolkit.getDefaultToolkit().createImage(fimg);
        return convert(newimg);
    }

    public BufferedImage filterResize(BufferedImage bi) {
        int cx = bi.getWidth() / 7;
        int cy = bi.getHeight() / 7;
        BufferedImage resizedImage = new BufferedImage(cx, cy, bi.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bi, 0, 0, cx, cy, null);
        g.dispose();

        return resizedImage;
    }

    public BufferedImage filterBlur(BufferedImage bi) {
        BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());

        float n = 1f / 25f;
        Kernel kernel = new Kernel(5, 5, new float[] { n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n,
                n, n, n, n });

        ConvolveOp op = new ConvolveOp(kernel);
        op.filter(bi, buff);

        return buff;
    }

    public BufferedImage filterBlur3(BufferedImage bi) {
        BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());

        float n = 1f / 9f;
        Kernel kernel = new Kernel(3, 3, new float[] { n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n,
                n, n, n, n });

        ConvolveOp op = new ConvolveOp(kernel);
        op.filter(bi, buff);

        return buff;
    }

    public BufferedImage filterNoise(BufferedImage bi) {
        BufferedImage buff = convert(bi);

        int cx = 3;
        int cy = 3;

        int[] buf_w = new int[cx * cy];
        Arrays.fill(buf_w, WHITE_MARK);
        int[] buf_b = new int[cx * cy];
        Arrays.fill(buf_b, BLACK_MARK);
        int[] buf_z = new int[cx * cy];
        Arrays.fill(buf_z, 0);

        for (int y = 0; y < bi.getHeight() - cy; y += cy) {
            for (int x = 0; x < bi.getWidth() - cx; x += cx) {
                int[] buf = new int[cx * cy];

                bi.getRGB(x, y, cx, cy, buf, 0, cx);

                int vol_w = 0;
                int vol_b = 0;
                for (int yy = 0; yy < cy; yy++) {
                    for (int xx = 0; xx < cx; xx++) {
                        if (buf[yy * cx + xx] == WHITE_MARK)
                            vol_w++;
                        if (buf[yy * cx + xx] == BLACK_MARK)
                            vol_b++;
                    }
                }

                if (vol_w > 3 && vol_b < 3)
                    buff.setRGB(x, y, cx, cy, buf_w, 0, 1);
                else if (vol_b > 3 && vol_w < 3)
                    buff.setRGB(x, y, cx, cy, buf_b, 0, 1);
                else
                    buff.setRGB(x, y, cx, cy, buf_z, 0, 1);

            }
        }

        return buff;
    }

    public BufferedImage filterDoGFilter(BufferedImage bi) {
        BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());

        DoGFilter ef = new DoGFilter();
        ef.filter(bi, buff);

        return buff;
    }

    public BufferedImage filterEdgeFilter(BufferedImage bi) {
        BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());

        EdgeFilter ef = new EdgeFilter();
        ef.filter(bi, buff);

        return buff;
    }

    public BufferedImage filterLaplaceFilter(BufferedImage bi) {
        BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());

        LaplaceFilter ef = new LaplaceFilter();
        ef.filter(bi, buff);

        return buff;
    }

    public Rectangle getBounds(BufferedImage bi) {

        int cx = 3;
        int cy = 3;

        int[] buf_y = new int[bi.getHeight() / cy];
        int[] buf_x = new int[bi.getWidth() / cx];

        for (int y = 0; y < bi.getHeight() - cy; y += cy) {
            for (int x = 0; x < bi.getWidth() - cx; x += cx) {
                if ((bi.getRGB(x + cx / 2, y + cy / 2) & 0x00ffffff) != 0)
                    buf_y[y / cy]++;
            }
        }

        for (int x = 0; x < bi.getWidth() - cx; x += cx) {
            for (int y = 0; y < bi.getHeight() - cy; y += cy) {
                if ((bi.getRGB(x + cx / 2, y + cy / 2) & 0x00ffffff) != 0)
                    buf_x[x / cx]++;
            }
        }

        Rectangle r = new Rectangle();

        for (int x = 0; x < buf_x.length; x++) {
            if (buf_x[x] > 10 && buf_x[x] < 100) {
                r.x = x * cx;
                break;
            }
        }

        for (int x = buf_x.length - 1; x > 0; x--) {
            if (buf_x[x] > 10 && buf_x[x] < 100) {
                r.width = x * cx - r.x + cx;
                break;
            }
        }

        for (int y = 0; y < buf_y.length; y++) {
            if (buf_y[y] > 10 && buf_y[y] < 100) {
                r.y = y * cy;
                break;
            }
        }

        for (int y = buf_y.length - 1; y > 0; y--) {
            if (buf_y[y] > 10 && buf_y[y] < 100) {
                r.height = y * cy - r.y + cy;
                break;
            }
        }

        if (r.width <= 100 || r.height <= 100)
            throw new NotFound();

        return r;
    }

    public BufferedImage filterSimply(BufferedImage bi) {
        BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());

        float n = 1f / 25f;
        Kernel kernel = new Kernel(5, 5, new float[] { n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n,
                n, n, n, n });

        ConvolveOp op = new ConvolveOp(kernel);
        op.filter(bi, buff);

        return buff;
    }

    float patternAlpha(BufferedImage pattern) {
        float c = 0;
        int cx = pattern.getWidth();
        int cy = pattern.getHeight();
        int size = cy * cx;
        int xp = 0;
        int yp = 0;
        for (int y = 0; y < cy; y++) {
            for (int x = 0; x < cx; x++) {
                int rgbp = pattern.getRGB(xp + x, yp + y);
                int ap = (rgbp & 0xff000000) >> 24 & 0xff;
                c += ap / 255f;
            }
        }
        return c / size;
    }

    int patternAverage(BufferedImage pattern) {
        int s = 0;

        Raster r = pattern.getData();

        int cx = pattern.getWidth();
        int cy = pattern.getHeight();
        for (int y = 0; y < cy; y++) {
            for (int x = 0; x < cx; x++) {
                s += r.getSample(x, y, 0);
            }
        }
        return s;
    }

    BufferedImage resize(BufferedImage img, float scale) {
        int width = (int) (img.getWidth() * scale);
        int height = (int) (img.getHeight() * scale);

        Image scaledImage = img.getScaledInstance((int) width, (int) height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = resizedImage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();

        // BufferedImage resizedImage = new BufferedImage(width, height,
        // BufferedImage.TYPE_INT_ARGB);
        // Graphics2D g = resizedImage.createGraphics();
        // g.setComposite(AlphaComposite.Src);
        //
        // g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        // RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // g.setRenderingHint(RenderingHints.KEY_RENDERING,
        // RenderingHints.VALUE_RENDER_QUALITY);
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        //
        // g.drawImage(img, 0, 0, width, height, null);
        // g.dispose();

        return resizedImage;
    }

    public Rectangle getBounds2(BufferedImage desktopImage) {
        BufferedImage test1 = Capture.load(new File("/Users/axet/Desktop/test1.png"));
        BufferedImage test2 = Capture.load(new File("/Users/axet/Desktop/test2.png"));
        BufferedImage pattern1 = Capture.load(new File("/Users/axet/Desktop/logoL.png"));
        BufferedImage pattern2 = Capture.load(new File("/Users/axet/Desktop/logoR.png"));
        desktopImage = Capture.load(new File("/Users/axet/Desktop/small2.png"));

        test1 = resize(pattern1, 0.769f);

        Average averagePattern = new Average(test1);
        averagePattern.calcFull();
        Average averageDesign = new Average(test2);

        averageDesign.debug(averagePattern, 1190, 719, 0.80f);

        Capture.save(test1, "/Users/axet/Desktop/test11.png");
        Capture.save(desktopImage, "/Users/axet/Desktop/test2.png");

        patternLookup(pattern1, desktopImage);

        System.out.println("----");

        patternLookup(pattern2, desktopImage);

        return null;
    }

    static class Average {

        // [y][x]
        int average[][] = new int[1][1];
        int av;

        BufferedImage img;
        IntegralImage i;

        public Average(BufferedImage img) {
            this.img = img;
            i = new IntegralImage(img);
        }

        float compare(int rgbp, int rgbl) {
            int rp = (rgbp & 0xff0000) >> 16 & 0xff;
            int gp = (rgbp & 0x00ff00) >> 8 & 0xff;
            int bp = (rgbp & 0x0000ff) >> 0 & 0xff;

            int rl = (rgbl & 0xff0000) >> 16 & 0xff;
            int gl = (rgbl & 0x00ff00) >> 8 & 0xff;
            int bl = (rgbl & 0x0000ff) >> 0 & 0xff;

            float rm = 1 - Math.abs(rp - rl) / 255f;
            float gm = 1 - Math.abs(gp - gl) / 255f;
            float bm = 1 - Math.abs(bp - bl) / 255f;

            float c = (rm + gm + bm) / 3;

            return c;
        }

        float compareAlpha(int rgbp, int rgbl) {
            int ap = (rgbp & 0xff000000) >> 24 & 0xff;
            int rp = (rgbp & 0xff0000) >> 16 & 0xff;
            int gp = (rgbp & 0x00ff00) >> 8 & 0xff;
            int bp = (rgbp & 0x0000ff) >> 0 & 0xff;

            int al = (rgbp & 0xff000000) >> 24 & 0xff;
            int rl = (rgbl & 0xff0000) >> 16 & 0xff;
            int gl = (rgbl & 0x00ff00) >> 8 & 0xff;
            int bl = (rgbl & 0x0000ff) >> 0 & 0xff;

            float am = 1 - Math.abs(ap - al) / 255f;
            float rm = 1 - Math.abs(rp - rl) / 255f;
            float gm = 1 - Math.abs(gp - gl) / 255f;
            float bm = 1 - Math.abs(bp - bl) / 255f;

            float c = (am + rm + gm + bm) / 4;

            float a = ap / 255f;

            return (1 - a) + c * a;
        }

        float compare(Average lookup, int xl, int yl) {
            float c = 0;
            int cx = img.getWidth();
            int cy = img.getHeight();
            int size = cy * cx;
            int xp = 0;
            int yp = 0;
            for (int y = 0; y < cy; y++) {
                for (int x = 0; x < cx; x++) {
                    int rgbp = img.getRGB(xp + x, yp + y);
                    int rgbl = lookup.img.getRGB(xl + x, yl + y);

                    c += compare(rgbp, rgbl);
                }
            }
            return c / size;
        }

        /**
         * calculate average for full image
         */
        public void calcFull() {
            calc(0, 0, img.getWidth(), img.getHeight());
        }

        /**
         * calculate average for x,y and pattern size
         */
        public void calc(int x, int y, Average pattern) {
            calc(x, y, pattern.img.getWidth(), pattern.img.getHeight());
        }

        /**
         * calculate average for specified coords
         */
        public void calc(int x, int y, int cx, int cy) {
            av = i.total(x, y, x + cx - 1, y + cy - 1);

            int ciy = cy / average.length;
            for (int iy = 0; iy < average.length; iy++) {
                int cix = cx / average[iy].length;
                for (int ix = 0; ix < average[iy].length; ix++) {
                    average[iy][ix] = i.total(x + cix * ix, y + ciy * iy, x + cix * ix + cix - 1, y + ciy * iy + ciy
                            - 1);
                }
            }
        }

        boolean pass(Average pattern, int x, int y, float lim) {
            calc(x, y, pattern);

            if (compare(av, pattern.av) < lim)
                return false;

            for (int iy = 0; iy < average.length; iy++) {
                for (int ix = 0; ix < average[iy].length; ix++) {
                    if (compare(pattern.average[iy][ix], average[iy][ix]) < lim)
                        return false;
                }
            }

            return true;
        }

        void debug(Average pattern, int x, int y, float lim) {
            calc(x, y, pattern);

            System.out.println("av=" + compare(av, pattern.av));

            for (int iy = 0; iy < average.length; iy++) {
                for (int ix = 0; ix < average[iy].length; ix++) {
                    System.out.println(compare(pattern.average[iy][ix], average[iy][ix]));
                }
            }
        }
    }

    public Rectangle patternLookup(BufferedImage pattern, BufferedImage desktopImage) {
        // desktopImage = resize(desktopImage, 0.3f);
        // pattern = resize(pattern, 0.3f);

        Average averageLookup = new Average(desktopImage);
        // IntegralImage integralLookup = new IntegralImage(desktopImage);

        double step = Math.min(1. / pattern.getWidth(), 1. / pattern.getHeight());

        for (float scale = 0.80f; scale > 0.60; scale -= step) {
            System.out.println("scale " + scale);

            BufferedImage patternSized = resize(pattern, scale);

            // int averagePattern = patternAverage(patternSized);
            Average averagePattern = new Average(patternSized);
            averagePattern.calcFull();

            int cx = desktopImage.getWidth() - patternSized.getWidth();
            int cy = desktopImage.getHeight() - patternSized.getHeight();
            for (int y = 0; y <= cy; y++) {
                for (int x = 0; x <= cx; x++) {
                    // int average = integralLookup.total(x, y,
                    // patternSized.getWidth() - 1 + x, patternSized.getHeight()
                    // - 1 + y);
                    // float color = compare(average, averagePattern);
                    // if (color > 0.94f) {
                    if (averageLookup.pass(averagePattern, x, y, 0.99f)) {
                        float res = averagePattern.compare(averageLookup, x, y);
                        if (res >= 0.95f) {
                            System.out.println("x " + x + " y " + y + " f " + res + " scale " + scale);

                            averageLookup.debug(averagePattern, x, y, 0.99f);

                            Capture capture = new Capture();
                            capture.save(patternSized, "/Users/axet/Desktop/test1.png");
                            capture.save(desktopImage, "/Users/axet/Desktop/test2.png");
                        }
                    }
                }
            }
        }

        return null;
    }
}
