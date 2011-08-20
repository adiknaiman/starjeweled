package com.github.axet.starjeweled.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.Kernel;
import java.awt.image.RGBImageFilter;
import java.lang.reflect.Array;
import java.util.Arrays;

import com.github.axet.starjeweled.common.RangeColor;

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

}
