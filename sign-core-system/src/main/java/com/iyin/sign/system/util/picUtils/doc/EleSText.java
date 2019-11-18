//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;
import com.iyin.sign.system.util.picUtils.util.MapUtil;
import com.iyin.sign.system.util.picUtils.util.StrUtil;
import com.iyin.sign.system.util.picUtils.util.To;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class EleSText extends EleShape {
    public String fontName;
    public String text;
    public boolean bold;
    public boolean italic;
    public String spacing;
    public String format;

    protected EleBase copyEle(XDoc xdoc) {
        return new EleSText(xdoc, this.getAttMap());
    }

    public EleSText(XDoc xdoc, HashMap attMap) {
        super(xdoc, attMap);
    }

    public EleSText(XDoc xdoc) {
        super(xdoc);
    }

    protected void init() {
        super.init();
        this.typeName = "stext";
        this.spacing = "0";
        this.fontName = XFont.defaultFontName;
        this.format = "";
        this.width = 200;
        this.height = 100;
    }

    public void setAttMap(HashMap map) {
        super.setAttMap(map);
        this.fontName = MapUtil.getString(map, "fontName", this.fontName);
        this.text = MapUtil.getString(map, "text", this.text);
        this.bold = MapUtil.getBool(map, "bold", this.bold);
        this.italic = MapUtil.getBool(map, "italic", this.italic);
        this.spacing = MapUtil.getString(map, "spacing", this.spacing);
        this.format = MapUtil.getString(map, "format", this.format);
    }

    public HashMap getAttMap() {
        HashMap map = super.getAttMap();
        map.put("fontName", this.fontName);
        map.put("text", this.text);
        map.put("bold", String.valueOf(this.bold));
        map.put("italic", String.valueOf(this.italic));
        map.put("spacing", this.spacing);
        map.put("format", this.format);
        return map;
    }

    public Object clone() {
        return new EleSText(this.xdoc, this.getAttMap());
    }

    protected boolean canFill() {
        return this.text.length() > 0;
    }

    protected Shape getShape() {
        if(this.text.length() > 0) {
            byte style = 0;
            int var10 = style | (this.bold?1:0);
            if(this.italic) {
                var10 |= 2;
            }

            String txt = DocUtil.printEval(this.text, this.xdoc);
            if(this.format.length() > 0) {
                txt = StrUtil.format(txt, this.format);
            }

            Font f = XFont.createFont(this.fontName, var10, 100, txt);
            int tmpSpacing = 0;
            if(this.spacing.equals("auto")) {
                if(txt.length() > 1) {
                    int bound = txt.length() - 1;
                    if(this.layoutFlow.equals("v")) {
                        tmpSpacing = (int)((double)(100 * this.height) / (double)this.width - (double)this.height) / bound / 2;
                    } else {
                        tmpSpacing = (int)((double)(100 * this.width) / (double)this.height - (double)this.width) / bound / 2;
                    }

                    if(tmpSpacing < 0) {
                        tmpSpacing = 0;
                    }
                }
            } else {
                tmpSpacing = To.toInt(this.spacing);
            }

            Shape shape;
            AffineTransform af;
            int i;
            Object var11;
            GeneralPath var12;
            if(this.layoutFlow.equals("v")) {
                var12 = new GeneralPath();
                i = 0;

                for(int i1 = 0; i1 < txt.length(); ++i1) {
                    shape = PaintUtil.getOutline(String.valueOf(txt.charAt(i1)), f);
                    af = new AffineTransform();
                    af.translate(0.0D, (double)i);
                    i = (int)((double)i + shape.getBounds().getHeight() + 6.0D + (double)tmpSpacing);
                    shape = af.createTransformedShape(shape);
                    var12.append(shape, false);
                }

                var11 = var12;
            } else if(tmpSpacing == 0) {
                var11 = PaintUtil.getOutline(txt, f);
            } else {
                var12 = new GeneralPath();

                for(i = 0; i < txt.length(); ++i) {
                    var12.append(PaintUtil.getOutline(String.valueOf(txt.charAt(i)), f, (100 + tmpSpacing) * i, 0), false);
                }

                var11 = var12;
            }

            Rectangle2D var13 = ((Shape)var11).getBounds2D();
            af = new AffineTransform();
            af.scale((double)this.width / var13.getWidth(), (double)this.height / var13.getHeight());
            shape = af.createTransformedShape((Shape)var11);
            var13 = shape.getBounds2D();
            af = new AffineTransform();
            af.translate(-var13.getX(), -var13.getY());
            shape = af.createTransformedShape(shape);
            return shape;
        } else {
            return super.getShape();
        }
    }

    public void autoSize() {
        if(isBlank(this)) {
            if(this.layoutFlow.equals("v")) {
                if(!this.sizeType.equals("autoheight") && !this.sizeType.equals("autosize")) {
                    super.autoSize();
                } else {
                    this.height = (int)(this.txtBound(this.width).getWidth() * (double)(1 + To.toInt(this.spacing) / 100));
                }
            } else if(!this.sizeType.equals("autowidth") && !this.sizeType.equals("autosize")) {
                super.autoSize();
            } else {
                this.width = (int)(this.txtBound(this.height).getWidth() * (double)(1 + To.toInt(this.spacing) / 100));
            }
        } else {
            super.autoSize();
        }

    }

    private Rectangle2D txtBound(int fontSize) {
        byte style = 0;
        int var6 = style | (this.bold?1:0);
        if(this.italic) {
            var6 |= 2;
        }

        String txt = DocUtil.printEval(this.text, this.xdoc);
        Font f = XFont.createFont(this.fontName, var6, (float)fontSize);

        for(int rect = 0; rect < txt.length(); ++rect) {
            if(!Character.isSpaceChar(txt.charAt(rect)) && !f.canDisplay(txt.charAt(rect))) {
                f = XFont.createFont("", f.getStyle(), (float)f.getSize());
                break;
            }
        }

        Rectangle2D var7 = f.getStringBounds(txt, DocConst.g.getFontRenderContext());
        if(var7.getHeight() < (double)f.getSize()) {
            var7.setFrame(var7.getX(), var7.getY(), var7.getWidth(), (double)(f.getSize() + 1));
        }

        return var7;
    }

    public EleText toText() {
        EleText txt = new EleText(this.xdoc);
        txt.bBold = this.bold;
        txt.bItalic = this.italic;
        txt.fontName = this.fontName;
        txt.fontSize = this.height;
        txt.text = this.text;
        txt.format = this.format;
        txt.fontColor = this.fillColor;
        txt.href = this.href;
        return txt;
    }

    protected void bindValue(String val) {
        this.text = val;
    }
}
