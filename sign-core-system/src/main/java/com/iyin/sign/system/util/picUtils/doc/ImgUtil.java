//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;

import com.iyin.sign.system.util.picUtils.util.ColorUtil;
import com.iyin.sign.system.util.picUtils.util.StrUtil;
import com.iyin.sign.system.util.picUtils.util.XUrl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImgUtil {
    public static boolean useCache = false;
    private static final int maxImgSize = 1200;
    public static Object TEXT_ANTIALIASING = null;

    static {
        try {
            TEXT_ANTIALIASING = RenderingHints.class.getField("VALUE_TEXT_ANTIALIAS_LCD_HRGB").get(RenderingHints.class);
        } catch (Throwable var3) {
            ;
        }

    }

    public ImgUtil() {
    }

    public static BufferedImage loadInnerImg(String key) {
        BufferedImage img = ImgLib.getImg(key);
        if(img == null) {
            img = new BufferedImage(16, 16, 2);
        }

        return img;
    }

    public static void gray(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();

        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < h; ++j) {
                int rgb = img.getRGB(i, j);
                int gray = (rgb >> 16 & 255) * 77 + (rgb >> 8 & 255) * 151 + (rgb & 255) * 28 >> 8;
                img.setRGB(i, j, -16777216 | gray << 16 | gray << 8 | gray);
            }
        }

    }

    private static BufferedImage loadImg(String url) {
        BufferedImage img = null;

        try {
            return ImageIO.read((new XUrl(url)).getInputStream());
        } catch (Throwable var3) {
            img = new BufferedImage(1, 1, 2);
            return img;
        }
    }

    public static BufferedImage loadImg(XDoc xdoc, String url, Color color1, Color color2, boolean alpha) {
        if(url != null && url.length() != 0) {
            if(color1 == null) {
                color1 = Color.BLACK;
            }

            if(color2 == null) {
                color2 = Color.WHITE;
            }

            if(url.startsWith("#")) {
                url = url.substring(1);
            }

            boolean bin = false;
            if(url.startsWith("$")) {
                url = url.substring(1);
                bin = true;
            }

            BufferedImage img = null;
            if(!url.startsWith("@") && !url.startsWith("&") && !url.startsWith("?")) {
                if(!url.startsWith("<") && !url.startsWith("{")) {
                    if(url.startsWith("data:") || url.startsWith("BASE64:")) {
                        img = strToImg(url);
                    }
                } else {
                    if(xdoc == null) {
                        xdoc = new XDoc();
                    }

                    EleRect rect1 = DocUtil.getRect(xdoc, url);
                    if(rect1 != null) {
                        img = EleRect.toImg(rect1);
                    }
                }
            } else {
                BufferedImage rect = loadInnerImg(url.substring(1));
                if(rect != null) {
                    if(bin) {
                        rect = toBin(rect, color1, color2);
                    } else if(alpha) {
                        rect = alpha(rect, Color.WHITE);
                    } else {
                        rect = toRGB(rect);
                    }

                    return rect;
                }
            }

            if(img == null && url.length() > 0 && url.indexOf("{%") < 0 && url.indexOf("${") < 0) {
                try {
                    img = loadImg(url);
                } catch (Exception var8) {
                    ;
                }
            }

            if(img != null) {
                if(bin) {
                    img = toBin(img, color1, color2);
                } else if(alpha) {
                    img = alpha(img, Color.WHITE);
                } else {
                    img = toRGB(img);
                }
            }

            return img;
        } else {
            return null;
        }
    }

    private static BufferedImage toRGB(BufferedImage img) {
        if(img != null && img.getType() != 1 && img.getType() != 2) {
            BufferedImage timg = new BufferedImage(img.getWidth(), img.getHeight(), 1);
            Graphics g = timg.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.drawImage(img, 0, 0, (ImageObserver)null);
            img = timg;
        }

        return img;
    }

    public static BufferedImage toBin(BufferedImage img, Color color1, Color color2) {
        int w = img.getWidth();
        int h = img.getHeight();
        if(color1.equals(color2)) {
            color2 = ColorUtil.invert(color2);
        }

        boolean alpha = color1.getAlpha() != 255 || color2.getAlpha() != 255;
        BufferedImage img2 = new BufferedImage(w, h, alpha?2:1);

        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < h; ++j) {
                int rgb = img.getRGB(i, j);
                int r = rgb >> 16 & 255;
                int g = rgb >> 8 & 255;
                int b = rgb & 255;
                rgb = r * 77 + g * 151 + b * 28 >> 8;
                img2.setRGB(i, j, rgb < 128?color1.getRGB():color2.getRGB());
            }
        }

        return img2;
    }

    public static BufferedImage replace(BufferedImage img, Color c, Color c2) {
        return replace(img, c, c2, 0);
    }

    public static BufferedImage replace(BufferedImage img, Color c, Color c2, int range) {
        BufferedImage timg;
        if(img.getType() == 2) {
            timg = img;
        } else {
            timg = new BufferedImage(img.getWidth(), img.getHeight(), 2);
            timg.getGraphics().drawImage(img, 0, 0, (ImageObserver)null);
        }

        for(int i = 0; i < timg.getWidth(); ++i) {
            for(int j = 0; j < timg.getHeight(); ++j) {
                if(range == 0) {
                    if(timg.getRGB(i, j) == c.getRGB()) {
                        timg.setRGB(i, j, c2.getRGB());
                    }
                } else {
                    Color tc = new Color(timg.getRGB(i, j));
                    if(tc.getRed() > c.getRed() - range && tc.getRed() < c.getRed() + range && tc.getGreen() > c.getGreen() - range && tc.getGreen() < c.getGreen() + range && tc.getBlue() > c.getBlue() - range && tc.getBlue() < c.getBlue() + range) {
                        timg.setRGB(i, j, c2.getRGB());
                    }
                }
            }
        }

        return timg;
    }

    public static BufferedImage alpha(BufferedImage img, Color c) {
        return replace(img, c, new Color(255, 255, 255, 0), 48);
    }

    private static byte[][] toEdgePoints(BufferedImage img, int n) {
        int w = img.getWidth();
        int h = img.getHeight();
        byte[][] r1 = toBinPoints(img, n);
        byte[][] r2 = new byte[w][h];

        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < h; ++j) {
                if(r1[i][j] == 1) {
                    if(i != 0 && i != w - 1 && j != 0 && j != h - 1 && r1[i - 1][j - 1] != 0 && r1[i][j - 1] != 0 && r1[i + 1][j - 1] != 0 && r1[i - 1][j] != 0 && r1[i + 1][j] != 0 && r1[i - 1][j + 1] != 0 && r1[i][j + 1] != 0 && r1[i + 1][j + 1] != 0) {
                        r2[i][j] = 0;
                    } else {
                        r2[i][j] = 1;
                    }
                } else {
                    r2[i][j] = 0;
                }
            }
        }

        return r2;
    }

    private static byte[][] toBinPoints(BufferedImage img, int n) {
        int w = img.getWidth();
        int h = img.getHeight();
        byte[][] r1 = new byte[w][h];

        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < h; ++j) {
                int rgb = img.getRGB(i, j);
                int r = rgb >> 16 & 255;
                int g = rgb >> 8 & 255;
                int b = rgb & 255;
                rgb = r * 77 + g * 151 + b * 28 >> 8;
                if(rgb > n) {
                    r1[i][j] = 0;
                } else {
                    r1[i][j] = 1;
                }
            }
        }

        return r1;
    }

    private static BufferedImage toImg(byte[][] r, int w, int h, Color color, Color color2) {
        BufferedImage img2 = new BufferedImage(w, h, 1);

        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < h; ++j) {
                if(r[i][j] == 1) {
                    img2.setRGB(i, j, color.getRGB());
                } else {
                    img2.setRGB(i, j, color2.getRGB());
                }
            }
        }

        return img2;
    }

    public static BufferedImage toEdgeImg(BufferedImage img, int n, Color color, Color color2) {
        int w = img.getWidth();
        int h = img.getHeight();
        byte[][] r = toEdgePoints(img, n);
        return toImg(r, w, h, color, color2);
    }

    public static BufferedImage toBinImg(BufferedImage img, int n, Color color, Color color2) {
        int w = img.getWidth();
        int h = img.getHeight();
        byte[][] r = toBinPoints(img, n);
        return toImg(r, w, h, color, color2);
    }

    public static String imgToStr(Image img) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Object bufImg;
        if(img instanceof RenderedImage) {
            bufImg = (RenderedImage)img;
        } else {
            bufImg = new BufferedImage(img.getWidth((ImageObserver)null), img.getHeight((ImageObserver)null), 2);
            ((BufferedImage)bufImg).getGraphics().drawImage(img, 0, 0, (ImageObserver)null);
        }

        if(((RenderedImage)bufImg).getWidth() > 1200 || ((RenderedImage)bufImg).getHeight() > 1200) {
            if(!(bufImg instanceof BufferedImage)) {
                BufferedImage tmpImg = new BufferedImage(((RenderedImage)bufImg).getWidth(), ((RenderedImage)bufImg).getHeight(), 1);
                Graphics2D g = (Graphics2D)tmpImg.getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, ((RenderedImage)bufImg).getWidth(), ((RenderedImage)bufImg).getHeight());
                g.drawImage(img, new AffineTransform(), (ImageObserver)null);
                bufImg = tmpImg;
            }

            bufImg = zoomToSize((BufferedImage)bufImg, 1200);
        }

        try {
            ImageIO.write((RenderedImage)bufImg, "png", bout);
        } catch (Exception var5) {
            ;
        }

        return "data:image/png;base64," + StrUtil.toBase64(bout.toByteArray());
    }

    public static void setRenderHint(Graphics2D g2) {
        setRenderHint(g2, false);
    }

    public static void setImgRenderHint(Graphics2D g2) {
        setRenderHint(g2, true);
    }

    private static void setRenderHint(Graphics2D g2, boolean img) {
        if(!img && TEXT_ANTIALIASING != null && !PaintUtil.IS_OPEN_JDK) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, TEXT_ANTIALIASING);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public static BufferedImage strToImg(String str) {
        if(str.indexOf("base64,") > 0) {
            str = str.substring(str.indexOf("base64,") + 7);
        } else if(str.startsWith("BASE64:")) {
            str = str.substring(7);
        } else {
            str = "";
        }

        try {
            return ImageIO.read(new ByteArrayInputStream(StrUtil.fromBase64(str)));
        } catch (Exception var2) {
            return new BufferedImage(20, 20, 2);
        }
    }

    public static BufferedImage zoomToSize(BufferedImage img, int size) {
        if(img.getWidth() > size || img.getHeight() > size) {
            double scale;
            if(img.getWidth() > img.getHeight()) {
                scale = (double)size / (double)img.getWidth();
            } else {
                scale = (double)size / (double)img.getHeight();
            }

            BufferedImage timg = new BufferedImage((int)((double)img.getWidth() * scale), (int)((double)img.getHeight() * scale), 1);
            Graphics2D g2 = (Graphics2D)timg.getGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, timg.getWidth(), timg.getHeight());
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(img, 0, 0, timg.getWidth(), timg.getHeight(), (ImageObserver)null);
            g2.dispose();
            img = timg;
        }

        return img;
    }

    public static BufferedImage adjustImg(BufferedImage img, int w, int h) {
        BufferedImage timg = new BufferedImage(w, h, 2);
        Graphics2D g = (Graphics2D)timg.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        drawAdjustImg(g, img, w, h);
        g.dispose();
        return timg;
    }

    public static void drawAdjustImg(Graphics g, BufferedImage img, int w, int h) {
        double scale;
        if((double)img.getWidth() / (double)img.getHeight() > (double)w / (double)h) {
            scale = (double)w / (double)img.getWidth();
        } else {
            scale = (double)h / (double)img.getHeight();
        }

        g.drawImage(img, (w - (int)((double)img.getWidth() * scale)) / 2, (h - (int)((double)img.getHeight() * scale)) / 2, (int)((double)img.getWidth() * scale), (int)((double)img.getHeight() * scale), (ImageObserver)null);
    }

    public static Color getMainColor(BufferedImage img) {
        Color c = Color.white;
        int min = 256;
        int width = img.getWidth();
        int height = img.getHeight();

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                int rgb = img.getRGB(j, i);
                int r = rgb >> 16 & 255;
                int g = rgb >> 8 & 255;
                int b = rgb & 255;
                if(r * 77 + g * 151 + b * 28 >> 8 < min) {
                    min = r * 77 + g * 151 + b * 28 >> 8;
                    c = new Color(img.getRGB(j, i));
                }
            }
        }

        return c;
    }

    public static BufferedImage toBufImg(Image image) {
        if(image instanceof BufferedImage) {
            return (BufferedImage)image;
        } else {
            BufferedImage img = new BufferedImage(image.getWidth((ImageObserver)null), image.getHeight((ImageObserver)null), 1);
            Graphics g = img.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.drawImage(image, 0, 0, (ImageObserver)null);
            g.dispose();
            return img;
        }
    }

    public static BufferedImage nineGrid(BufferedImage img, int w, int h) {
        Rectangle grect = new Rectangle(0, 0, img.getWidth() / 3, img.getHeight() / 3);
        BufferedImage bimg = new BufferedImage((int)(Math.max(Math.ceil((double)w / (double)grect.width), 2.0D) * (double)grect.width), (int)(Math.max(Math.ceil((double)h / (double)grect.height), 2.0D) * (double)grect.height), img.getType());
        w = bimg.getWidth();
        h = bimg.getHeight();
        Graphics2D imgg = (Graphics2D)bimg.getGraphics();
        imgg.drawImage(img.getSubimage(img.getWidth() - grect.width, img.getHeight() - grect.height, grect.width, grect.height), (BufferedImageOp)null, w - grect.width, h - grect.height);
        Graphics2D cg;
        if(w - grect.width * 2 > 0) {
            cg = (Graphics2D)imgg.create(grect.width, h - grect.height, w - grect.width * 2, grect.height);
            cg.setPaint(new TexturePaint(img.getSubimage(grect.width, img.getHeight() - grect.height, grect.width, grect.height), grect));
            cg.fillRect(0, 0, w - grect.width * 2, grect.height);
        }

        imgg.drawImage(img.getSubimage(0, img.getHeight() - grect.height, grect.width, grect.height), (BufferedImageOp)null, 0, h - grect.height);
        if(h - grect.height * 2 > 0) {
            cg = (Graphics2D)imgg.create(w - grect.width, grect.height, grect.width, h - grect.height * 2);
            cg.setPaint(new TexturePaint(img.getSubimage(img.getWidth() - grect.width, grect.height, grect.width, grect.height), grect));
            cg.fillRect(0, 0, grect.width, h - grect.height * 2);
        }

        if(w - grect.width * 2 > 0 && h - grect.height * 2 > 0) {
            cg = (Graphics2D)imgg.create(grect.width, grect.height, w - grect.width * 2, h - grect.height * 2);
            cg.setPaint(new TexturePaint(img.getSubimage(grect.width, grect.height, grect.width, grect.height), grect));
            cg.fillRect(0, 0, w - grect.width * 2, h - grect.height * 2);
        }

        if(h - grect.height * 2 > 0) {
            cg = (Graphics2D)imgg.create(0, grect.height, grect.width, h - grect.height * 2);
            cg.setPaint(new TexturePaint(img.getSubimage(0, grect.height, grect.width, grect.height), grect));
            cg.fillRect(0, 0, grect.width, h - grect.height * 2);
        }

        imgg.drawImage(img.getSubimage(img.getWidth() - grect.width, 0, grect.width, grect.height), (BufferedImageOp)null, w - grect.width, 0);
        if(w - grect.width * 2 > 0) {
            cg = (Graphics2D)imgg.create(grect.width, 0, w - grect.width * 2, grect.height);
            cg.setPaint(new TexturePaint(img.getSubimage(grect.width, 0, grect.width, grect.height), grect));
            cg.fillRect(0, 0, w - grect.width * 2, grect.height);
        }

        imgg.drawImage(img.getSubimage(0, 0, grect.width, grect.height), (BufferedImageOp)null, 0, 0);
        return bimg;
    }

    public static BufferedImage fliph(BufferedImage img) {
        BufferedImage timg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        for(int i = 0; i < img.getWidth(); ++i) {
            for(int j = 0; j < img.getHeight(); ++j) {
                timg.setRGB(i, j, img.getRGB(img.getWidth() - i - 1, j));
            }
        }

        return timg;
    }

    public static BufferedImage flipv(BufferedImage img) {
        BufferedImage timg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        for(int i = 0; i < img.getWidth(); ++i) {
            for(int j = 0; j < img.getHeight(); ++j) {
                timg.setRGB(i, j, img.getRGB(i, img.getHeight() - j - 1));
            }
        }

        return timg;
    }

    public static BufferedImage fliphv(BufferedImage img) {
        return fliph(flipv(img));
    }

    public static boolean isStretch(String imgUrl) {
        return !imgUrl.startsWith("$@p") && !imgUrl.startsWith("@p") && !imgUrl.startsWith("@t") && !imgUrl.startsWith("$@t") && !imgUrl.startsWith("&p") && !imgUrl.startsWith("$&p") && !imgUrl.startsWith("&t") && !imgUrl.startsWith("$&t");
    }

    public static BufferedImage toXZoomImg(String imgUrl, String drawType, int width, int height) {
        BufferedImage img = loadImg((XDoc)null, imgUrl, (Color)null, (Color)null, false);
        TexturePaint paint = toPaint(img, drawType, width, height, 0, isStretch(imgUrl));
        img = new BufferedImage(width, height, 2);
        Graphics2D g2 = (Graphics2D)img.getGraphics();
        g2.setPaint(paint);
        g2.fillRect(0, 0, width, height);
        g2.dispose();
        return img;
    }

    public static TexturePaint toPaint(BufferedImage img, String drawType, int width, int height, int margin, boolean stretch) {
        BufferedImage bimg;
        if(drawType.equals(EleImg.DRAW_TYPE_4CORNER)) {
            bimg = new BufferedImage(img.getWidth() * 2, img.getHeight() * 2, img.getType());
            Graphics2D e = (Graphics2D)bimg.getGraphics();
            e.drawImage(img, (BufferedImageOp)null, 0, 0);
            e.drawImage(fliph(img), (BufferedImageOp)null, img.getWidth(), 0);
            e.drawImage(flipv(img), (BufferedImageOp)null, 0, img.getHeight());
            e.drawImage(fliphv(img), (BufferedImageOp)null, img.getWidth(), img.getHeight());
            e.dispose();
            img = bimg;
            drawType = EleImg.DRAW_TYPE_9GRID;
        }

        TexturePaint paint;
        int bimg1;
        int e1;
        if(drawType.equals(EleImg.DRAW_TYPE_REPEAT)) {
            if(!stretch) {
                paint = new TexturePaint(img, new Double(0.0D, 0.0D, (double)img.getWidth(), (double)img.getHeight()));
            } else {
                bimg1 = width - margin * 2;
                e1 = height - margin * 2;
                int n = (int) Math.round((double)bimg1 / (double)img.getWidth());
                if(n <= 0) {
                    n = 1;
                }

                double sw = (double)(n * img.getWidth()) / (double)bimg1;
                n = (int) Math.round((double)e1 / (double)img.getHeight());
                if(n <= 0) {
                    n = 1;
                }

                double sh = (double)(n * img.getHeight()) / (double)e1;
                paint = new TexturePaint(img, new Double(0.0D, 0.0D, (double)img.getWidth() / sw, (double)img.getHeight() / sh));
            }
        } else if(drawType.equals(EleImg.DRAW_TYPE_CENTER)) {
            if(img.getWidth() <= width - margin * 2 && img.getHeight() <= height - margin * 2) {
                bimg = new BufferedImage(width, height, 2);
                bimg.getGraphics().drawImage(img, (width - img.getWidth() - margin * 2) / 2, (height - img.getHeight() - margin * 2) / 2, (ImageObserver)null);
                paint = new TexturePaint(bimg, new Rectangle(0, 0, width, height));
            } else {
                paint = toAdjustPaint(img, drawType, width, height, margin);
            }
        } else if(drawType.equals(EleImg.DRAW_TYPE_FACT)) {
            bimg = new BufferedImage(width, height, 2);
            bimg.getGraphics().drawImage(img, 0, 0, (ImageObserver)null);
            paint = new TexturePaint(bimg, new Rectangle(0, 0, width, height));
        } else if(drawType.startsWith(EleImg.DRAW_TYPE_ADJUST)) {
            paint = toAdjustPaint(img, drawType, width, height, margin);
        } else if(drawType.equals(EleImg.DRAW_TYPE_9GRID)) {
            bimg1 = width - margin * 2;
            e1 = height - margin * 2;
            paint = new TexturePaint(nineGrid(img, bimg1, e1), new Rectangle(0, 0, bimg1, e1));
        } else if(width - margin * 2 < img.getWidth() && height - margin * 2 < img.getHeight()) {
            try {
                bimg = new BufferedImage(width - margin * 2, height - margin * 2, img.getType());
            } catch (Exception var14) {
                bimg = new BufferedImage(1, 1, img.getType());
            }

            bimg.getGraphics().drawImage(img, 0, 0, bimg.getWidth(), bimg.getHeight(), (ImageObserver)null);
            paint = new TexturePaint(bimg, new Rectangle(0, 0, bimg.getWidth(), bimg.getHeight()));
        } else {
            paint = new TexturePaint(img, new Rectangle(0, 0, width - margin * 2, height - margin * 2));
        }

        return paint;
    }

    private static TexturePaint toAdjustPaint(BufferedImage img, String drawType, int width, int height, int margin) {
        double scale;
        if((double)img.getWidth() / (double)img.getHeight() > (double)width / (double)height) {
            scale = (double)(width - margin * 2) / (double)img.getWidth();
        } else {
            scale = (double)(height - margin * 2) / (double)img.getHeight();
        }

        BufferedImage bimg = new BufferedImage(width - margin * 2, height - margin * 2, 2);
        int x = (width - margin * 2 - (int)((double)img.getWidth() * scale)) / 2;
        int y = (height - margin * 2 - (int)((double)img.getHeight() * scale)) / 2;
        if(drawType.endsWith("left")) {
            x = 0;
        } else if(drawType.endsWith("top")) {
            y = 0;
        } else if(drawType.endsWith("right")) {
            x *= 2;
        } else if(drawType.endsWith("bottom")) {
            y *= 2;
        }

        bimg.getGraphics().drawImage(img, x, y, (int)((double)img.getWidth() * scale), (int)((double)img.getHeight() * scale), (ImageObserver)null);
        return new TexturePaint(bimg, new Rectangle(0, 0, width - margin * 2, height - margin * 2));
    }
}
