//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;


import com.iyin.sign.system.util.picUtils.util.ColorUtil;
import com.iyin.sign.system.util.picUtils.util.MapUtil;
import com.iyin.sign.system.util.picUtils.util.StrUtil;
import com.iyin.sign.system.util.picUtils.util.To;

import java.awt.*;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Line2D.Float;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;

public class EleText extends EleBase {
    public Color fontColor;
    public Color backColor;
    public String backImg;
    public String fontName;
    public int fontSize;
    public int spacing;
    public String text;
    public int underline;
    public boolean bThroughline;
    public boolean bAboveline;
    public boolean bBold;
    public boolean bItalic;
    public boolean bCircle;
    public boolean bRect;
    public boolean bShadow;
    public boolean bOutline;
    public boolean bBlank;
    public boolean bStress;
    public String valign;
    public String href;
    public String toolTip;
    public String format;
    public Color underlineColor;

    protected EleBase copyEle(XDoc xdoc) {
        return new EleText(xdoc, this.getAttMap());
    }

    public EleText(XDoc xdoc, HashMap attMap) {
        super(xdoc, attMap);
    }

    public EleText(XDoc xdoc) {
        super(xdoc);
    }

    public EleText(EleText txt, String str) {
        this(txt.xdoc, txt.getAttMap());
        this.setText(str);
    }

    protected void init() {
        super.init();
        this.name = "";
        this.text = "";
        this.typeName = "text";
        this.fontName = XFont.defaultFontName;
        this.fontSize = XFont.defaultFontSize;
        this.fontColor = Color.BLACK;
        this.backColor = null;
        this.underline = 0;
        this.bBold = false;
        this.bItalic = false;
        this.bCircle = false;
        this.bRect = false;
        this.bShadow = false;
        this.bStress = false;
        this.bOutline = false;
        this.bAboveline = false;
        this.bBlank = false;
        this.valign = "bottom";
        this.href = "";
        this.toolTip = "";
        this.format = "";
        this.backImg = "";
        this.underlineColor = null;
        this.spacing = 0;
    }

    public void setAttMap(HashMap map) {
        super.setAttMap(map);
        this.fontColor = ColorUtil.strToColor(MapUtil.getString(map, "fontColor", ""), this.fontColor);
        if(map.containsKey("backColor")) {
            this.backColor = ColorUtil.strToColor(MapUtil.getString(map, "backColor", ""), (Color)null);
        }

        this.text = MapUtil.getString(map, "TEXT", this.text);
        this.text = MapUtil.getString(map, "text", this.text);
        this.spacing = MapUtil.getInt(map, "spacing", this.spacing);
        this.valign = MapUtil.getString(map, "valign", this.valign);
        this.href = MapUtil.getString(map, "href", this.href);
        this.toolTip = MapUtil.getString(map, "toolTip", this.toolTip);
        this.format = MapUtil.getString(map, "format", this.format);
        String strStyle = MapUtil.getString(map, "fontStyle", (String)this.getAttribute("fontStyle"));
        if(strStyle.indexOf("underline") >= 0) {
            int pos = strStyle.indexOf("underline") + 9;
            String ul;
            if(strStyle.indexOf(44, pos) < 0) {
                ul = strStyle.substring(pos);
            } else {
                ul = strStyle.substring(pos, strStyle.indexOf(44, pos));
            }

            if(ul.length() > 0) {
                String[] uls = ul.split("_");
                if(uls.length > 0) {
                    this.underline = To.toInt(uls[0]);
                }

                if(uls.length > 1) {
                    this.underlineColor = ColorUtil.strToColor(uls[1], (Color)null);
                }
            } else {
                this.underline = 1;
            }

            if(this.underline <= 0) {
                this.underline = 1;
            }
        } else {
            this.underline = 0;
        }

        this.bBold = strStyle.indexOf("bold") >= 0;
        this.bItalic = strStyle.indexOf("italic") >= 0;
        this.bThroughline = strStyle.indexOf("throughline") >= 0;
        this.bAboveline = strStyle.indexOf("aboveline") >= 0;
        this.bCircle = strStyle.indexOf("circle") >= 0;
        this.bRect = strStyle.indexOf("rect") >= 0;
        this.bShadow = strStyle.indexOf("shadow") >= 0;
        this.bStress = strStyle.indexOf("stress") >= 0;
        this.bOutline = strStyle.indexOf("outline") >= 0;
        this.bBlank = strStyle.indexOf("blank") >= 0;
        if(map.containsKey("fontSize")) {
            this.fontSize = XFont.parseFontSize(MapUtil.getString(map, "fontSize", ""));
        }

        this.fontName = MapUtil.getString(map, "fontName", this.fontName);
        this.backImg = MapUtil.getString(map, "backImg", this.backImg);
    }

    public HashMap getAttMap() {
        HashMap map = super.getAttMap();
        map.put("fontColor", ColorUtil.colorToStr(this.fontColor));
        map.put("backColor", ColorUtil.colorToStr(this.backColor));
        map.put("TEXT", this.text);
        map.put("fontName", this.fontName);
        map.put("fontSize", String.valueOf(this.fontSize));
        map.put("spacing", String.valueOf(this.spacing));
        map.put("valign", this.valign);
        map.put("href", this.href);
        map.put("toolTip", this.toolTip);
        StringBuffer style = new StringBuffer();
        if(this.bBold) {
            style.append("bold");
        }

        if(this.bItalic) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("italic");
        }

        if(this.underline > 0) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("underline");
            if(this.underline > 1) {
                style.append(this.underline);
            }

            if(this.underlineColor != null) {
                style.append("_").append(ColorUtil.colorToStr(this.underlineColor));
            }
        }

        if(this.bThroughline) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("throughline");
        }

        if(this.bAboveline) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("aboveline");
        }

        if(this.bCircle) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("circle");
        }

        if(this.bRect) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("rect");
        }

        if(this.bShadow) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("shadow");
        }

        if(this.bStress) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("stress");
        }

        if(this.bOutline) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("outline");
        }

        if(this.bBlank) {
            if(style.length() > 0) {
                style.append(",");
            }

            style.append("blank");
        }

        map.put("fontStyle", style.toString());
        map.put("format", this.format);
        map.put("backImg", this.backImg);
        return map;
    }

    public Object clone() {
        return new EleText(this.xdoc, this.getAttMap());
    }

    public boolean canSplit() {
        boolean b = true;
        if(this.text.length() > 1) {
            if(this.format.length() == 0) {
                int pos = this.text.indexOf("$[");
                if(pos >= 0 && this.text.indexOf("]") > pos) {
                    b = false;
                }
            } else {
                b = false;
            }
        }

        return b;
    }

    public String viewText() {
        String str = DocUtil.printEval(this.text, this.xdoc);
        if(str.length() > 0) {
            if(str.indexOf("\t") >= 0) {
                str = StrUtil.replaceAll(str, "\t", tabStr());
            }

            if(this.format.length() > 0) {
                str = StrUtil.format(str, this.format);
            }
        }

        return str;
    }

    public void print(Graphics2D g, int x, int pos, int lineHeight, int lineSpacing) {
        this.print(g, x, pos, lineHeight, lineSpacing, true);
    }

    private void print(Graphics2D g, int x, int pos, int lineHeight, int lineSpacing, boolean checkFont) {
        String str = this.viewText();
        if(str.length() > 0) {
            int stressWidth;
            if(checkFont) {
                Font boundsRect = XFont.createFont(this.fontName, this.getFontStyle(), (float)this.fontSize);
                if(!XFont.canDisplay(boundsRect, str)) {
                    StringBuffer var18 = new StringBuffer();
                    HashMap var20 = this.getAttMap();
                    boolean var21 = true;

                    EleText var19;
                    for(stressWidth = 0; stressWidth < str.length(); ++stressWidth) {
                        if(!XFont.canDisplay(boundsRect, String.valueOf(str.charAt(stressWidth)))) {
                            if(var18.length() > 0 && var21) {
                                var19 = new EleText(this.xdoc, var20);
                                var19.text = var18.toString();
                                var19.format = "";
                                var19.print(g, x, pos, lineHeight, lineSpacing, false);
                                x = (int)((double)x + var19.getBounds().getWidth());
                                var18.setLength(0);
                            }

                            var18.append(str.charAt(stressWidth));
                            var21 = false;
                        } else {
                            if(var18.length() > 0 && !var21) {
                                var19 = new EleText(this.xdoc, var20);
                                var19.text = var18.toString();
                                var19.format = "";
                                var19.print(g, x, pos, lineHeight, lineSpacing, false);
                                x = (int)((double)x + var19.getBounds().getWidth());
                                var18.setLength(0);
                            }

                            var18.append(str.charAt(stressWidth));
                            var21 = true;
                        }
                    }

                    if(var18.length() > 0) {
                        var19 = new EleText(this.xdoc, var20);
                        var19.text = var18.toString();
                        var19.format = "";
                        var19.print(g, x, pos, lineHeight, lineSpacing, false);
                    }

                    return;
                }
            }

            g.setFont(this.getFont());
            Rectangle2D var17 = this.getStrBounds(g.getFont(), str);
            if(!this.bOutline && !this.bBlank && !this.bShadow && (this.backColor != null || this.backImg.length() > 0)) {
                PaintUtil.fill(g, this.xdoc, new Rectangle(x, pos, (int)var17.getWidth(), lineHeight), this.backColor, this.backImg);
            }

            int descent = (int)(var17.getHeight() + var17.getY());
            int adjustHeight = 0;
            if(this.valign != null) {
                if(this.valign.equals("top")) {
                    adjustHeight = lineHeight - (int)var17.getHeight() - lineSpacing;
                } else if(this.valign.equals("center")) {
                    adjustHeight = (lineHeight - (int)var17.getHeight() - lineSpacing) / 2;
                }
            }

            int n = 0;
            int offy = pos + lineHeight - descent - adjustHeight;
            this.drawString(g, str, x, offy, false);
            if(this.bStress || this.bCircle || this.bRect) {
                stressWidth = g.getFont().getSize() / 5;
                Stroke stroke = this.getLineStroke();

                for(int i = 0; i < str.length(); ++i) {
                    Rectangle2D bounds = this.getStrBounds(g.getFont(), String.valueOf(str.charAt(i)));
                    if(str.charAt(i) != 32) {
                        if(this.bStress) {
                            this.drawShape(g, new Double((double)(x + n + (int)bounds.getWidth() / 2 - stressWidth / 2), (double)(offy + (int)(bounds.getHeight() + bounds.getY() - (double)this.getULWeight())), (double)stressWidth, (double)stressWidth), stroke, this.fontColor, true);
                        }

                        if(this.bCircle) {
                            this.drawShape(g, new Double((double)(x + n), (double)(offy + (int)bounds.getY()), (double)((int)bounds.getWidth()), (double)((int)bounds.getHeight())), stroke, this.fontColor);
                        }

                        if(this.bRect) {
                            this.drawShape(g, new Rectangle(x + n, offy + (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight()), stroke, this.fontColor);
                        }
                    }

                    n = (int)((double)n + bounds.getWidth());
                }
            }

            if(this.underline > 0) {
                this.drawLine(g, x, pos + lineHeight - adjustHeight - this.getULWeight(), x + (int)var17.getWidth(), getLineStroke(this.underline, (float)this.getLineWeight()), this.getULColor());
                PaintUtil.resetStroke(g);
            }

            if(this.bThroughline) {
                this.drawLine(g, x, pos + lineHeight - (int)var17.getHeight() / 2 - adjustHeight, x + (int)var17.getWidth(), this.getLineStroke(), this.getFontColor());
            }

            if(this.bAboveline) {
                this.drawLine(g, x, pos, x + (int)var17.getWidth(), this.getLineStroke(), this.getFontColor());
            }
        }

    }

    private int getULWeight() {
        return Math.round((float)(this.getLineWeight() * 2));
    }

    private Color getULColor() {
        return this.underlineColor != null?this.underlineColor:this.getFontColor();
    }

    private Color getFontColor() {
        return this.fontColor != null?this.fontColor: Color.BLACK;
    }

    public static boolean isVRotate(char c) {
        return c < 256 || "…～‖｜–︱—︳︴﹏（）｛｝〔〕【】《》〈〉「」『』﹙﹚﹛﹜﹝﹞".indexOf(c) >= 0;
    }

    public double printV(Graphics2D g, int x, int pos, int lineHeight, int lineSpacing) {
        double cHeight = 0.0D;
        String str = this.viewText();
        if(str.length() > 0) {
            boolean vrotate = isVRotate(str.charAt(0));
            g.setFont(this.getFont(str));
            Rectangle2D boundsRect = this.getStrBounds(g.getFont(), str, true);
            if(vrotate) {
                cHeight = boundsRect.getWidth();
            } else {
                cHeight = boundsRect.getHeight();
            }

            if(!this.bOutline && !this.bBlank && !this.bShadow && this.backColor != null) {
                PaintUtil.fill(g, this.xdoc, new Rectangle(x, pos, lineHeight, (int)cHeight), this.backColor, this.backImg);
            }

            int adjustHeight = 0;
            if(this.valign != null) {
                if(this.valign.equals("top")) {
                    adjustHeight = lineHeight - (int)boundsRect.getWidth() - lineSpacing;
                } else if(this.valign.equals("center")) {
                    adjustHeight = (lineHeight - (int)boundsRect.getWidth() - lineSpacing) / 2;
                }
            }

            int offx = x + adjustHeight;
            int offy = (int)((double)pos - boundsRect.getY());
            g.setColor(this.fontColor);
            int stressWidth = g.getFont().getSize() / 5;
            Stroke stroke;
            if(vrotate) {
                Graphics2D trans = (Graphics2D)g.create(offx, offy - g.getFont().getSize() * 5 / 6, (int)boundsRect.getHeight(), (int)boundsRect.getWidth());
                trans.rotate(1.5707963267948966D, 0.0D, 0.0D);
                this.drawString(trans, str, this.spacing / 2, -g.getFont().getSize() / 6, true);
                trans.dispose();
                if(str.charAt(0) != 32) {
                    stroke = this.getLineStroke();
                    if(this.bStress) {
                        this.drawShape(g, new Double((double)(offx - stressWidth), (double)((int)((double)offy + boundsRect.getY() + boundsRect.getWidth() / 2.0D)), (double)stressWidth, (double)stressWidth), stroke, this.fontColor, true);
                    }

                    if(this.bCircle) {
                        this.drawShape(g, new Double((double)offx, (double)(offy + (int)boundsRect.getY()), (double)((int)boundsRect.getHeight()), (double)((int)boundsRect.getWidth())), stroke, this.fontColor);
                    }

                    if(this.bRect) {
                        this.drawShape(g, new Rectangle(offx, offy + (int)boundsRect.getY(), (int)boundsRect.getHeight(), (int)boundsRect.getWidth()), stroke, this.fontColor);
                    }
                }
            } else {
                boolean trans1 = "，。；：、？！".indexOf(str.charAt(0)) >= 0;
                if(trans1) {
                    g.translate(boundsRect.getWidth() / 2.0D, -boundsRect.getHeight() / 4.0D);
                }

                this.drawString(g, str, offx, offy + this.spacing / 2, true);
                if(trans1) {
                    g.translate(-boundsRect.getWidth() / 2.0D, boundsRect.getHeight() / 4.0D);
                }

                stroke = this.getLineStroke();
                if(this.bStress) {
                    this.drawShape(g, new Double((double)(offx - stressWidth), (double)((int)((double)offy + boundsRect.getY() / 2.0D + (double)(stressWidth / 2))), (double)stressWidth, (double)stressWidth), stroke, this.fontColor, true);
                }

                if(this.bCircle) {
                    this.drawShape(g, new Double((double)offx, (double)(offy + (int)boundsRect.getY()), (double)((int)boundsRect.getWidth()), (double)((int)boundsRect.getHeight())), stroke, this.fontColor);
                }

                if(this.bRect) {
                    this.drawShape(g, new Rectangle(offx, offy + (int)boundsRect.getY(), (int)boundsRect.getWidth(), (int)boundsRect.getHeight()), stroke, this.fontColor);
                }
            }

            if(this.underline > 0) {
                this.drawLineV(g, offx, pos, pos + (int)(vrotate?boundsRect.getWidth():boundsRect.getHeight()), getLineStroke(this.underline, (float)this.getLineWeight()), this.getULColor());
                PaintUtil.resetStroke(g);
            }

            if(this.bThroughline) {
                this.drawLineV(g, offx + (int)boundsRect.getHeight() / 2 + adjustHeight, pos, pos + (int)(vrotate?boundsRect.getWidth():boundsRect.getHeight()), this.getLineStroke(), this.getFontColor());
            }

            if(this.bAboveline) {
                this.drawLineV(g, offx + (int)boundsRect.getHeight() + adjustHeight, pos, pos + (int)(vrotate?boundsRect.getWidth():boundsRect.getHeight()), this.getLineStroke(), this.getFontColor());
            }
        }

        return cHeight;
    }

    private Stroke getLineStroke() {
        return getLineStroke(1, (float)this.getLineWeight());
    }

    private void drawString(Graphics2D g, String str, int x, int y, boolean v) {
        if(this.xdoc.print != null) {
            Rectangle2D shape;
            if(this.href.length() > 0) {
                shape = g.getFont().getStringBounds(str, g.getFontRenderContext());
                shape.setFrame(shape.getX() + (double)x, shape.getY() + (double)y, shape.getWidth(), shape.getHeight());
                this.xdoc.print.hrefs.add(new NameShape(this.getHrefName(), g.getTransform().createTransformedShape(shape)));
            }

            if(this.toolTip.length() > 0) {
                shape = g.getFont().getStringBounds(str, g.getFontRenderContext());
                shape.setFrame(shape.getX() + (double)x, shape.getY() + (double)y, shape.getWidth(), shape.getHeight());
                this.xdoc.print.toolTips.add(new NameShape(this.toolTip, g.getTransform().createTransformedShape(shape)));
            }
        }

        if(this.bShadow) {
            PaintUtil.setPaint(g, this.xdoc, this.backColor != null?this.backColor: Color.LIGHT_GRAY, this.backImg);
            int shape1 = this.getShadowOffset();
            this.actDrawString(g, str, x + shape1, y + shape1, v);
        }

        if(this.bOutline || this.bBlank) {
            Shape shape2 = this.getShape(str, g.getFont(), x, y, v);
            if(this.bOutline) {
                PaintUtil.setPaint(g, this.xdoc, this.backColor != null?this.backColor: Color.YELLOW, this.backImg);
                g.setStroke(new BasicStroke(g.getFont().getSize2D() / 5.0F, 1, 1));
                g.draw(shape2);
                g.setStroke(new BasicStroke());
            }

            if(this.bBlank) {
                g.setStroke(new BasicStroke(this.getBlankWeight(), 1, 1));
                if(this.backColor != null || this.backImg.length() > 0) {
                    PaintUtil.fill(g, this.xdoc, shape2, this.backColor, this.backImg);
                }

                g.setColor(this.fontColor != null?this.fontColor: Color.BLACK);
                g.draw(shape2);
                g.setStroke(new BasicStroke());
            }
        }

        if(!this.bBlank) {
            g.setColor(this.fontColor != null?this.fontColor: Color.BLACK);
            this.actDrawString(g, str, x, y, v);
        }

    }

    private String getHrefName() {
        return DocUtil.fixHref(this.href, this.name.length() > 0?this.name:this.text);
    }

    protected Shape getShape(String str, Font font, int x, int y, boolean vertical) {
        return PaintUtil.getOutline(str, font, x, y, vertical?0:this.getSpacing(font.getSize()));
    }

    public Rectangle2D getBounds() {
        String str = this.viewText();
        return (Rectangle2D)(str.length() > 0?this.getStrBounds(this.getFont(str), str):new Rectangle2D.Double());
    }

    public static final String tabStr() {
        return tabStr((Font)null);
    }

    public static final String tabStr(Font font) {
        return "　　";
    }

    public Rectangle2D getStrBounds(Font font, String str) {
        return this.getStrBounds(font, str, false);
    }

    public Rectangle2D getStrBounds(Font font, String str, boolean vertical) {
        Rectangle2D rect = null;
        if(vertical && (str.length() != 1 || !isVRotate(str.charAt(0)))) {
            if(str.indexOf("\t") >= 0) {
                str = StrUtil.replaceAll(str, "\t", tabStr(font));
            }

            rect = font.getStringBounds(str, DocConst.frc);
            if(rect.getHeight() < (double)font.getSize()) {
                rect.setFrame(rect.getX(), rect.getY(), rect.getWidth(), (double)(font.getSize() + 1));
            }

            if(this.spacing != 0 && str.length() > 0) {
                rect.setFrame(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight() + (double)(str.length() * this.getSpacing(font.getSize())));
            }
        } else {
            if(str.indexOf("\t") >= 0) {
                str = StrUtil.replaceAll(str, "\t", tabStr(font));
            }

            rect = font.getStringBounds(str, DocConst.frc);
            if(rect.getHeight() < (double)font.getSize()) {
                rect.setFrame(rect.getX(), rect.getY(), rect.getWidth(), (double)(font.getSize() + 1));
            }

            if(this.spacing != 0 && str.length() > 0) {
                rect.setFrame(rect.getX(), rect.getY(), rect.getWidth() + (double)(str.length() * this.getSpacing(font.getSize())), rect.getHeight());
            }
        }

        return rect;
    }

    protected int getSpacing(int fontSize) {
        return (int)((double)this.spacing * ((double)fontSize / (double)this.fontSize));
    }

    public int getShadowOffset() {
        return this.getLineWeight() + 1;
    }

    public int getLineWeight() {
        int weight = this.fontSize / 14;
        if(weight == 0) {
            weight = 1;
        }

        return weight;
    }

    public static Stroke getLineStroke(int type, float weight) {
        Object stroke = null;
        if(type == 1) {
            stroke = new BasicStroke(weight, 0, 0, 20.0F);
        } else if(type == 2) {
            stroke = TextStrokeUtil.createStroke(ShapeUtil.strToShape("M 0 0 L 10 0 L 10 5 L 0 5 L 0 0 Z M 0 25 L 10 25 L 10 30 L 0 30 L 0 25 Z"), 4.0F * weight);
        } else if(type == 3) {
            stroke = new BasicStroke(weight * 2.0F);
        } else if(type == 4) {
            stroke = new BasicStroke(weight, 0, 0, 20.0F, new float[]{weight * 2.0F}, weight);
        } else if(type == 5) {
            stroke = new BasicStroke(weight, 0, 0, 20.0F, new float[]{5.0F * weight}, weight);
        } else if(type == 6) {
            stroke = new BasicStroke(weight, 0, 0, 20.0F, new float[]{10.0F * weight, weight * 2.0F, weight * 2.0F, weight * 2.0F}, weight);
        } else if(type == 7) {
            stroke = new BasicStroke(weight, 0, 0, 20.0F, new float[]{10.0F * weight, weight * 2.0F, weight * 2.0F, weight * 2.0F, weight * 2.0F, weight * 2.0F}, weight);
        } else if(type == 8) {
            stroke = TextStrokeUtil.createStroke(ShapeUtil.strToShape("M 0 0 L 15 20 L 30 0 L 30 10 L 15 30 L 0 10 Z"), 3.0F * weight);
        } else if(type == 9) {
            stroke = TextStrokeUtil.createStroke(ShapeUtil.strToShape("M 0 0 L 10 0 L 10 15 L 0 15 L 0 0 Z M 0 25 L 10 25 L 10 30 L 0 30 L 0 25 Z"), 4.0F * weight);
        } else if(type == 10) {
            stroke = TextStrokeUtil.createStroke(ShapeUtil.strToShape("M 0 0 L 10 0 L 10 5 L 0 5 L 0 0 Z M 0 15 L 10 15 L 10 30 L 0 30 L 0 15 Z"), 4.0F * weight);
        } else {
            stroke = new BasicStroke(weight, 0, 0, 20.0F);
        }

        return (Stroke)stroke;
    }

    private void drawLine(Graphics2D g, int x, int y, int x2, Stroke stroke, Color lineColor) {
        if(this.bOutline || this.bShadow) {
            if(this.bOutline) {
                PaintUtil.setPaint(g, this.xdoc, this.backColor != null?this.backColor: Color.YELLOW, this.backImg);
                g.setStroke(new BasicStroke(g.getFont().getSize2D() / 5.0F, 1, 1, 20.0F));
                g.draw(stroke.createStrokedShape(new Float((float)x, (float)y, (float)x2, (float)y)));
            }

            if(this.bShadow) {
                PaintUtil.setPaint(g, this.xdoc, this.backColor != null?this.backColor: Color.LIGHT_GRAY, this.backImg);
                g.setStroke(stroke);
                int offset = this.getShadowOffset();
                g.draw(new Float((float)(x + offset), (float)(y + offset), (float)(x2 + offset), (float)(y + offset)));
            }
        }

        g.setColor(lineColor);
        g.setStroke(stroke);
        g.draw(new Float((float)x, (float)y, (float)x2, (float)y));
    }

    private void drawLineV(Graphics2D g, int x, int y, int y2, Stroke stroke, Color lineColor) {
        if(this.bOutline || this.bShadow) {
            if(this.bOutline) {
                PaintUtil.setPaint(g, this.xdoc, this.backColor != null?this.backColor: Color.YELLOW, this.backImg);
                g.setStroke(new BasicStroke(g.getFont().getSize2D() / 5.0F, 1, 1, 20.0F));
                g.draw(new Float((float)x, (float)y, (float)x, (float)y2));
            }

            if(this.bShadow) {
                PaintUtil.setPaint(g, this.xdoc, this.backColor != null?this.backColor: Color.LIGHT_GRAY, this.backImg);
                g.setStroke(stroke);
                int offset = this.getShadowOffset();
                g.draw(new Float((float)(x + offset), (float)(y + offset), (float)(x + offset), (float)(y2 + offset)));
            }
        }

        g.setColor(lineColor);
        g.setStroke(stroke);
        g.draw(new Float((float)x, (float)y, (float)x, (float)y2));
    }

    private void drawShape(Graphics2D g, Shape shape, Stroke stroke, Color lineColor) {
        this.drawShape(g, shape, stroke, lineColor, false);
    }

    private void drawShape(Graphics2D g, Shape shape, Stroke stroke, Color lineColor, boolean fill) {
        if(this.bOutline || this.bShadow) {
            if(this.bOutline) {
                PaintUtil.setPaint(g, this.xdoc, this.backColor != null?this.backColor: Color.YELLOW, this.backImg);
                g.setStroke(new BasicStroke(g.getFont().getSize2D() / 5.0F, 1, 1, 20.0F));
                g.draw(shape);
            }

            if(this.bShadow) {
                PaintUtil.setPaint(g, this.xdoc, this.backColor != null?this.backColor: Color.LIGHT_GRAY, this.backImg);
                g.setStroke(stroke);
                int offset = this.getShadowOffset();
                g.translate(offset, offset);
                if(fill) {
                    g.fill(shape);
                } else {
                    g.draw(shape);
                }

                g.translate(-offset, -offset);
            }
        }

        g.setColor(lineColor);
        if(fill) {
            g.fill(shape);
        } else {
            g.setStroke(stroke);
            g.draw(shape);
        }

    }

    public String toString() {
        return this.viewText();
    }

    public boolean attEquals(EleText txt) {
        HashMap map1 = this.getAttMap();
        HashMap map2 = txt.getAttMap();
        map1.remove("TEXT");
        Iterator it = map1.keySet().iterator();

        while(it.hasNext()) {
            String key = (String)it.next();
            if(!map1.get(key).equals(map2.get(key))) {
                return false;
            }
        }

        return true;
    }

    public void setUnknownText(String str) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if(c != 13) {
                if(!Character.isDefined(c)) {
                    sb.append(' ');
                } else {
                    sb.append(c);
                }
            }
        }

        this.text = sb.toString();
    }

    public Stroke getUnderlineStroke(double scale) {
        return getLineStroke(this.underline, (float)((double)this.getLineWeight() * scale));
    }

    public void setText(String text) {
        this.setAttribute("TEXT", text);
    }

    public float getBlankWeight() {
        return Math.max((float)this.getLineWeight() / 4.0F, 0.5F);
    }

    public int getFontStyle() {
        int style = 0;
        if(this.bBold) {
            style |= 1;
        }

        if(this.bItalic) {
            style |= 2;
        }

        return style;
    }

    public Font getFont() {
        return this.getFont(this.text);
    }

    public Font getFont(String text) {
        return XFont.createFont(this.fontName, this.getFontStyle(), this.fontSize, text);
    }

    protected void actDrawString(Graphics2D g, String str, int x, int y) {
        this.actDrawString(g, str, x, y, false);
    }

    protected void actDrawString(Graphics2D g, String str, int x, int y, boolean vertical) {
        PaintUtil.drawString(g, str, x, y, vertical?0:this.getSpacing(g.getFont().getSize()));
    }

    public String getText() {
        return this.text;
    }
}
