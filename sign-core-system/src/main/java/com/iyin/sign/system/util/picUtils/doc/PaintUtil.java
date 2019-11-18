//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;

import com.iyin.sign.system.util.picUtils.util.ColorUtil;
import com.iyin.sign.system.util.picUtils.util.To;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class PaintUtil {
    public static boolean IS_OPEN_JDK = false;
    public static boolean TEXT_AS_SHAPE = false;
    public static boolean PDF_EMBED_FONT = true;

    static {
        try {
            IS_OPEN_JDK = System.getProperty("java.runtime.name", "").startsWith("OpenJDK");
            if(IS_OPEN_JDK) {
                TEXT_AS_SHAPE = true;
            }
        } catch (Throwable var1) {
            ;
        }

    }

    public PaintUtil() {
    }

    public static void drawString(Graphics2D g, String str, int x, int y) {
        drawString(g, str, x, y, 0);
    }

    public static void drawString(Graphics2D g, String str, int x, int y, int spacing) {
        if(spacing != 0) {
            drawShapeString(g, str, x, y, spacing);
        } else if(isPdfGraphics(g)) {
            Font font = g.getFont();
            if(PDF_EMBED_FONT && !font.isBold() && XFont.getFontFilePath(font) != null) {
                g.drawString(str, x, y);
            } else {
                drawShapeString(g, str, x, y, 0);
            }
        } else if(TEXT_AS_SHAPE) {
            drawShapeString(g, str, x, y, 0);
        } else {
            g.drawString(str, x, y);
        }

    }

    private static boolean isPdfGraphics(Graphics2D g) {
        String strClass=g.getClass().getName();
        return strClass.equals("com.lowagie.text.pdf.PdfGraphics2D");
    }

    private static void drawShapeString(Graphics2D g, String str, int x, int y, int spacing) {
        g.fill(getOutline(str, g.getFont(), x, y, spacing));
    }

    public static Shape getOutline(String str, Font font) {
        return getOutline(str, font, 0, 0, 0);
    }

    public static Shape getOutline(String str, Font font, int x, int y) {
        return getOutline(str, font, x, y, 0);
    }

    public static Shape getOutline(String str, Font font, int x, int y, int spacing) {
        if(spacing == 0) {
            if(IS_OPEN_JDK) {
                TextLayout var11 = new TextLayout(str, font, DocConst.frc);
                if(x == 0 && y == 0) {
                    return var11.getOutline((AffineTransform)null);
                } else {
                    AffineTransform var13 = new AffineTransform();
                    var13.translate((double)x, (double)y);
                    return var11.getOutline(var13);
                }
            } else {
                return font.createGlyphVector(DocConst.frc, str).getOutline((float)x, (float)y);
            }
        } else if(IS_OPEN_JDK) {
            int var10 = str.length();
            GeneralPath var12 = new GeneralPath();
            AffineTransform var14 = new AffineTransform();
            var14.translate((double)(x + spacing / 2), (double)y);

            for(int i1 = 0; i1 < var10; ++i1) {
                Shape var15 = (new TextLayout(str.substring(i1, i1 + 1), font, DocConst.frc)).getOutline(var14);
                var12.append(var15, false);
                var14.translate((double)spacing + var15.getBounds2D().getWidth(), 0.0D);
            }

            return var12;
        } else {
            GlyphVector tl = font.createGlyphVector(DocConst.frc, str);
            int at = tl.getNumGlyphs();
            GeneralPath path = new GeneralPath();

            for(int i = 0; i < at; ++i) {
                path.append(tl.getGlyphOutline(i, (float)(x + i * spacing + spacing / 2), (float)y), false);
            }

            return path;
        }
    }

    public static void drawError(Graphics2D g, Throwable t) {
        drawError(g, String.valueOf(t.getMessage()));
        g.setColor(Color.RED);
        drawOutlineString(g, t.getClass().getName(), 2, g.getFont().getSize() * 2 + 2, Color.WHITE);
    }

    public static void drawError(Graphics2D g, String msg) {
        g.setFont(DocUtil.getDefultFont());
        g.setColor(Color.RED);
        drawOutlineString(g, msg, 2, g.getFont().getSize() + 2, Color.WHITE);
    }

    public static void drawOutlineString(Graphics2D g2, String value, int x, int y) {
        drawOutlineString(g2, value, x, y, (Color)null);
    }

    public static void drawOutlineString(Graphics2D g2, String value, int x, int y, int spacing) {
        drawOutlineString(g2, value, x, y, (Color)null, spacing);
    }

    public static void drawOutlineString(Graphics2D g2, String value, int x, int y, Color color) {
        drawOutlineString(g2, value, x, y, color, 0);
    }

    public static void drawOutlineString(Graphics2D g2, String value, int x, int y, Color color, int spacing) {
        Color c = g2.getColor();
        Font valueFont = g2.getFont();
        g2.setColor(color != null?color: ColorUtil.invert(c));
        g2.setStroke(new BasicStroke(valueFont.getSize2D() / 4.0F, 1, 1));
        g2.draw(getOutline(value, valueFont, x, y, spacing));
        g2.setStroke(new BasicStroke());
        g2.setColor(c);
        drawString(g2, value, x, y, spacing);
    }

    public static void drawOutlineShape(Graphics2D g, GeneralPath p) {
        Stroke s = g.getStroke();
        Color c = g.getColor();
        g.setColor(ColorUtil.invert(c));
        g.setStroke(new BasicStroke(8.0F, 1, 1));
        g.draw(p);
        g.setStroke(s);
        g.setColor(c);
        g.fill(p);
    }

    public static void setPaint(Graphics2D g, XDoc xdoc, Color color, String img) {
        BufferedImage imgFill = ImgUtil.loadImg(xdoc, img, color, Color.WHITE, false);
        if(imgFill != null) {
            TexturePaint paint = new TexturePaint(imgFill, new Rectangle(0, 0, imgFill.getWidth(), imgFill.getHeight()));
            paint = checkPaint(g, paint);
            g.setPaint(paint);
        } else if(color != null) {
            g.setPaint(color);
        }

    }

    public static void fill(Graphics2D g, XDoc xdoc, Shape shape, Color fillColor, String fillImg) {
        fill(g, xdoc, shape, fillColor, fillImg, "", (Color)null);
    }

    public static void fill(Graphics2D g, XDoc xdoc, Shape shape, Color fillColor, String fillImg, String gradual, Color fillColor2) {
        if(!fillImg.startsWith("<") && !fillImg.startsWith("[")) {
            boolean var21 = fillImg.startsWith("#");
            BufferedImage var22 = ImgUtil.loadImg(xdoc, fillImg, fillColor, fillColor2, false);
            if(var22 != null) {
                fillImg(g, var22, shape, var21, ImgUtil.isStretch(fillImg));
            } else if(fillColor != null) {
                if(gradual.length() > 0 && !gradual.equals("0")) {
                    if(fillColor2 == null) {
                        fillColor2 = Color.WHITE;
                    }

                    if(gradual.endsWith("%")) {
                        g.setColor(ColorUtil.mix(fillColor, fillColor2, To.toDouble(gradual.substring(0, gradual.length() - 1), 100.0D) / 100.0D));
                        g.fill(shape);
                    } else {
                        int var23 = To.toInt(gradual);
                        paintGradient(g, (var23 - 1) / 4, (var23 - 1) % 4, shape, fillColor, fillColor2);
                    }
                } else {
                    g.setColor(fillColor);
                    g.fill(shape);
                }
            }
        } else {
            EleRect nineGrid = DocUtil.getRect(xdoc, fillImg);
            if(nineGrid != null) {
                Rectangle imgFill = shape.getBounds();
                Graphics ng = g.create(imgFill.x, imgFill.y, imgFill.width, imgFill.height);
                AffineTransform af = new AffineTransform();
                af.translate((double)(-imgFill.x), (double)(-imgFill.y));
                Shape shape2 = af.createTransformedShape(shape);
                ng.setClip(shape2);
                int xn = (int) Math.round((double)imgFill.width / (double)nineGrid.width);
                int yn = (int) Math.round((double)imgFill.height / (double)nineGrid.height);
                if(xn <= 0) {
                    xn = 1;
                }

                if(yn <= 0) {
                    yn = 1;
                }

                double sw = (double)imgFill.width / ((double)xn * (double)nineGrid.width);
                double sh = (double)imgFill.height / ((double)yn * (double)nineGrid.height);

                for(int i = 0; i < xn; ++i) {
                    for(int j = 0; j < yn; ++j) {
                        Graphics2D cg2 = (Graphics2D)ng.create((int)((double)(i * nineGrid.width) * sw), (int)((double)(j * nineGrid.height) * sh), (int)((double)nineGrid.width * sw), (int)((double)nineGrid.height * sh));
                        cg2.scale(sw, sh);
                        nineGrid.print(cg2);
                        cg2.dispose();
                    }
                }

                ng.dispose();
            }
        }

    }

    public static void fillImg(Graphics2D g, BufferedImage img, Shape shape, boolean nineGrid, boolean stretch) {
        Rectangle rect = shape.getBounds();
        TexturePaint paint;
        if(!nineGrid) {
            if(!stretch) {
                paint = new TexturePaint(img, new Double((double)rect.x, (double)rect.y, (double)img.getWidth(), (double)img.getHeight()));
            } else {
                int n = (int) Math.round((double)rect.width / (double)img.getWidth());
                if(n <= 0) {
                    n = 1;
                }

                double sw = (double)(n * img.getWidth()) / (double)rect.width;
                n = (int) Math.round((double)rect.height / (double)img.getHeight());
                if(n <= 0) {
                    n = 1;
                }

                double sh = (double)(n * img.getHeight()) / (double)rect.height;
                paint = new TexturePaint(img, new Double((double)rect.x, (double)rect.y, (double)img.getWidth() / sw, (double)img.getHeight() / sh));
            }
        } else {
            paint = new TexturePaint(ImgUtil.nineGrid(img, rect.width, rect.height), rect);
        }

        paint = checkPaint(g, paint);
        g.setPaint(paint);
        g.fill(shape);
    }

    public static void paintGradient(Graphics2D g2, int type, int trans, Shape shape, Color c1, Color c2) {
        Rectangle rect = shape.getBounds();
        Graphics2D cg = (Graphics2D)g2.create(rect.x, rect.y, rect.width, rect.height);
        double x = (double)rect.x;
        double y = (double)rect.y;
        Object paint = Gradient.getPaint(rect, type, trans, c1, c2);
        if(paint != null) {
            if(type < 4 && (trans == 2 || trans == 3) && isPdfGraphics(g2)) {
                BufferedImage img = new BufferedImage(rect.width, rect.height, 2);
                Graphics2D imgg = (Graphics2D)img.getGraphics();
                imgg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                imgg.setPaint((Paint)paint);
                imgg.fillRect(0, 0, rect.width, rect.height);
                imgg.dispose();
                paint = new TexturePaint(img, rect);
            }

            cg.translate(-x, -y);
            cg.setPaint((Paint)paint);
            cg.fill(shape);
            cg.dispose();
        }

    }

    public static void resetStroke(Graphics2D g) {
        if(!(g.getStroke() instanceof BasicStroke)) {
            g.setStroke(new BasicStroke());
        }

    }

    public static TexturePaint checkPaint(Graphics2D g2, TexturePaint paint) {
        return checkPaint(g2, paint, false);
    }

    public static TexturePaint checkPaint(Graphics2D g2, TexturePaint paint, boolean force) {
        if(force) {
            byte minSize = 48;
            if(paint.getImage().getWidth() < minSize || paint.getImage().getHeight() < minSize) {
                int ws = (int) Math.ceil((double)minSize / (double)paint.getImage().getWidth());
                int hs = (int) Math.ceil((double)minSize / (double)paint.getImage().getHeight());
                BufferedImage img = new BufferedImage(paint.getImage().getWidth() * ws, paint.getImage().getHeight() * hs, 2);
                Graphics2D g = (Graphics2D)img.getGraphics();
                g.drawImage(paint.getImage(), 0, 0, img.getWidth(), img.getHeight(), (ImageObserver)null);
                g.dispose();
                paint = new TexturePaint(img, paint.getAnchorRect());
            }
        }

        return paint;
    }
}
