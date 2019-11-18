
package com.iyin.sign.system.util.picUtils.doc;

import com.iyin.sign.system.util.picUtils.util.MapUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class ElePath extends EleShape {
    public String drawType;
    public static String DRAW_TYPE_ZOOM = "zoom";
    public static String DRAW_TYPE_FACT = "fact";
    public static String DRAW_TYPE_ADJUST = "adjust";
    public int repeat;
    public String path;

    protected EleBase copyEle(XDoc xdoc) {
        return new ElePath(xdoc, this.getAttMap());
    }

    public ElePath(XDoc xdoc, HashMap attMap) {
        super(xdoc, attMap);
    }

    public ElePath(XDoc xdoc) {
        super(xdoc);
    }

    protected void init() {
        super.init();
        this.typeName = "path";
        this.path = "";
        this.drawType = DRAW_TYPE_ZOOM;
        this.repeat = 1;
    }

    protected boolean canFill() {
        return this.path.length() > 0;
    }

    public void setAttMap(HashMap map) {
        super.setAttMap(map);
        this.path = MapUtil.getString(map, "path", this.path);
        this.path = MapUtil.getString(map, "shape", this.path);
        this.drawType = MapUtil.getString(map, "drawType", this.drawType);
        if (MapUtil.getBool(map, "lockRatio", false)) {
            this.drawType = DRAW_TYPE_ADJUST;
        }

        this.repeat = MapUtil.getInt(map, "repeat", this.repeat);
    }

    public HashMap getAttMap() {
        HashMap map = super.getAttMap();
        map.put("shape", this.path);
        map.put("drawType", this.drawType);
        map.put("repeat", String.valueOf(this.repeat));
        return map;
    }

    public Object clone() {
        return new ElePath(this.xdoc, this.getAttMap());
    }

    protected Shape getShape() {
        if (this.repeat <= 1) {
            return this.getActShape();
        } else {
            Shape shape = this.getActShape();
            Rectangle2D bound = shape.getBounds2D();
            AffineTransform af = new AffineTransform();
            double n = 1000.0D;
            af.scale(n * 0.9D / bound.getWidth(), n * 0.9D / bound.getHeight());
            shape = af.createTransformedShape(shape);
            bound = shape.getBounds2D();
            af = new AffineTransform();
            af.translate(-bound.getX() + n * 0.05D, -bound.getY() + n * 0.05D);
            shape = af.createTransformedShape(shape);
            GeneralPath path;
            int i;
            if (this.width > this.height) {
                path = new GeneralPath();
                af = new AffineTransform();

                for (i = 0; i < this.repeat; ++i) {
                    path.append(af.createTransformedShape(shape), false);
                    af.translate(n, 0.0D);
                }

                af = new AffineTransform();
                af.scale(1.0D / (double) this.repeat * (double) this.width / n, (double) this.height / n);
                return af.createTransformedShape(path);
            } else if (this.width == this.height) {
                double var10 = 6.283185307179586D / (double) this.repeat;
                GeneralPath path1 = new GeneralPath();
                af = new AffineTransform();
                af.translate(n * 2.0D, 0.0D);
                shape = af.createTransformedShape(shape);
                af = new AffineTransform();

                for (int i1 = 0; i1 < this.repeat; ++i1) {
                    path1.append(af.createTransformedShape(shape), false);
                    af.rotate(var10, n * 2.5D, n * 2.5D);
                }

                af = new AffineTransform();
                af.scale(0.2D * (double) this.width / n, 0.2D * (double) this.width / n);
                return af.createTransformedShape(path1);
            } else {
                path = new GeneralPath();
                af = new AffineTransform();

                for (i = 0; i < this.repeat; ++i) {
                    path.append(af.createTransformedShape(shape), false);
                    af.translate(0.0D, n);
                }

                af = new AffineTransform();
                af.scale((double) this.width / n, 1.0D / (double) this.repeat * (double) this.height / n);
                return af.createTransformedShape(path);
            }
        }
    }

    private Shape getActShape() {
        if (this.path.length() > 0) {
            Shape shape = ShapeUtil.strToShape(this.path);
            if (this.drawType.equals(DRAW_TYPE_FACT)) {
                return shape;
            } else {
                if (this.drawType.startsWith(DRAW_TYPE_ADJUST)) {
                    Rectangle2D bounds = shape.getBounds2D();
                    double bound;
                    if (bounds.getWidth() / bounds.getHeight() > (double) this.width / (double) this.height) {
                        bound = (double) this.width / bounds.getWidth();
                    } else {
                        bound = (double) this.height / bounds.getHeight();
                    }

                    double scaley = bound;
                    if (this.width != this.height && this.margin > 0) {
                        if (this.width > this.height) {
                            bound += ((double) this.margin * 2.0D * (double) this.height / (double) this.width - (double) (this.margin * 2)) / (double) this.width;
                        } else {
                            scaley = bound + ((double) this.margin * 2.0D * (double) this.width / (double) this.height - (double) (this.margin * 2)) / (double) this.height;
                        }
                    }

                    int offx = (this.width - (int) (bounds.getWidth() * bound)) / 2;
                    int offy = (this.height - (int) (bounds.getHeight() * scaley)) / 2;
                    if (this.drawType.endsWith("left")) {
                        offx = 0;
                    } else if (this.drawType.endsWith("top")) {
                        offy = 0;
                    } else if (this.drawType.endsWith("right")) {
                        offx *= 2;
                    } else if (this.drawType.endsWith("bottom")) {
                        offy *= 2;
                    }

                    bounds = shape.getBounds2D();
                    AffineTransform af1 = new AffineTransform();
                    af1.scale(bound, scaley);
                    shape = af1.createTransformedShape(shape);
                    bounds = shape.getBounds2D();
                    af1 = new AffineTransform();
                    af1.translate(-bounds.getX() + (double) offx, -bounds.getY() + (double) offy);
                    shape = af1.createTransformedShape(shape);
                } else {
                    Rectangle2D bound1 = shape.getBounds2D();
                    AffineTransform af = new AffineTransform();
                    af.scale((double) this.width / bound1.getWidth(), (double) this.height / bound1.getHeight());
                    shape = af.createTransformedShape(shape);
                    bound1 = shape.getBounds2D();
                    af = new AffineTransform();
                    af.translate(-bound1.getX(), -bound1.getY());
                    shape = af.createTransformedShape(shape);
                }

                return shape;
            }
        } else {
            return super.getShape();
        }
    }

    protected void bindValue(String val) {
        this.path = val;
    }
}
