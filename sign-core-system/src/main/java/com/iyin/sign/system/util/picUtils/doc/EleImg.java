//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;


import com.iyin.sign.system.util.picUtils.util.MapUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class EleImg extends EleShape {
    public String src;
    public String drawType;
    public String strShape;
    public static String DRAW_TYPE_ZOOM = "zoom";
    public static String DRAW_TYPE_REPEAT = "repeat";
    public static String DRAW_TYPE_CENTER = "center";
    public static String DRAW_TYPE_FACT = "fact";
    public static String DRAW_TYPE_ADJUST = "adjust";
    public static String DRAW_TYPE_9GRID = "9grid";
    public static String DRAW_TYPE_4CORNER = "4corner";
    public BufferedImage img = null;

    protected EleBase copyEle(XDoc xdoc) {
        return new EleImg(xdoc, this.getAttMap());
    }

    public EleImg(XDoc xdoc, HashMap attMap) {
        super(xdoc, attMap);
    }

    public EleImg(XDoc xdoc) {
        super(xdoc);
    }

    protected void init() {
        super.init();
        this.typeName = "img";
        this.drawType = DRAW_TYPE_ZOOM;
        this.strShape = "";
        this.src = "";
        this.color = null;
        this.fillColor = Color.WHITE;
        this.width = 0;
        this.height = 0;
    }

    public void setAttMap(HashMap map) {
        super.setAttMap(map);
        if(map.containsKey("fillColor") || map.containsKey("fillImg") || map.containsKey("src") && !map.get("src").equals(this.src)) {
            this.img = null;
        }

        this.src = MapUtil.getString(map, "src", this.src);
        this.drawType = MapUtil.getString(map, "drawType", this.drawType);
        this.strShape = MapUtil.getString(map, "shape", this.strShape);
    }

    public HashMap getAttMap() {
        HashMap map = super.getAttMap();
        map.put("src", this.src);
        map.put("drawType", this.drawType);
        map.put("shape", this.strShape);
        return map;
    }

    public Object clone() {
        return new EleImg(this.xdoc, this.getAttMap());
    }

    protected Shape getShape() {
        Shape sha;
        if(this.strShape.length() > 0) {
            sha = ShapeUtil.strToShape(this.strShape);
            Rectangle2D bound = sha.getBounds2D();
            AffineTransform af = new AffineTransform();
            af.scale((double)this.width / bound.getWidth(), (double)this.height / bound.getHeight());
            sha = af.createTransformedShape(sha);
            bound = sha.getBounds2D();
            af = new AffineTransform();
            af.translate(-bound.getX(), -bound.getY());
            sha = af.createTransformedShape(sha);
        } else {
            sha = super.getShape();
        }

        return sha;
    }

    public BufferedImage getImg() {
        if(this.img == null && this.src.length() > 0) {
            if(this.src.startsWith("$")) {
                this.src = this.src.substring(1);
            }

            this.img = ImgUtil.loadImg(this.xdoc, this.src, this.fillColor, this.fillColor2, !Color.WHITE.equals(this.fillColor) || this.fillImg.length() > 0);
            if(this.img != null && this.width == 0 && this.height == 0) {
                this.width = this.img.getWidth();
                this.height = this.img.getHeight();
            }
        }

        return this.img;
    }

    protected void drawOther(Graphics2D g, Shape shape) {
        this.img = this.getImg();
        if(this.img != null) {
            this.autoSize();
            if(this.width > 0 && this.height > 0) {
                TexturePaint paint = ImgUtil.toPaint(this.img, this.drawType, this.width, this.height, this.margin, ImgUtil.isStretch(this.src));
                paint = PaintUtil.checkPaint(g, paint);
                g.setPaint(paint);
                if(this.margin != 0) {
                    new AffineTransform();
                    AffineTransform af = new AffineTransform();
                    af.translate((double)(-this.margin), (double)(-this.margin));
                    shape = af.createTransformedShape(shape);
                }

                g.fill(shape);
            }
        }

    }

    public void autoSize() {
        if(this.width == 0 && this.height == 0) {
            this.getImg();
        }

        boolean b = false;

        for(int i = 0; i < this.eleList.size(); ++i) {
            if(this.eleList.get(i) instanceof EleRect && ((EleRect)this.eleList.get(i)).dock.length() == 0) {
                b = true;
                break;
            }
        }

        if(!b && (this.sizeType.equals("autosize") || this.sizeType.equals("autowidth") || this.sizeType.equals("autoheight")) && !hasTxt(this)) {
            this.img = this.getImg();
            if(this.img != null) {
                if(this.sizeType.equals("autosize") || this.sizeType.equals("autowidth")) {
                    this.width = this.img.getWidth();
                }

                if(this.sizeType.equals("autosize") || this.sizeType.equals("autoheight")) {
                    this.height = this.img.getHeight();
                }

                this.fixSize();
            } else {
                this.width = this.height;
            }
        } else {
            super.autoSize();
        }

    }

    protected void bindValue(String val) {
        this.src = val;
    }

    public void fixSize() {
        DocPaper p = this.xdoc.getPaper();
        fixSize(this, p.getContentWidth(), p.getContentHeight());
    }
}
