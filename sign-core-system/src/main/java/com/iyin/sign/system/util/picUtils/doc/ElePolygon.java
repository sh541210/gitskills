package com.iyin.sign.system.util.picUtils.doc;

import com.iyin.sign.system.util.picUtils.util.MapUtil;
import com.iyin.sign.system.util.picUtils.util.To;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */

/*    */
/*    */ public class ElePolygon extends EleShape
/*    */ {
    /*    */   public String points;

    /*    */
/*    */
    protected EleBase copyEle(XDoc xdoc)
/*    */ {
/* 32 */
        return new ElePolygon(xdoc, getAttMap());
/*    */
    }

    /*    */
/*    */
    public ElePolygon(XDoc xdoc, HashMap attMap) {
/* 36 */
        super(xdoc, attMap);
/*    */
    }

    /*    */
    public ElePolygon(XDoc xdoc) {
/* 39 */
        super(xdoc);
/*    */
    }

    /*    */
    protected void init() {
/* 42 */
        super.init();
/* 43 */
        this.typeName = "polygon";
/* 44 */
        this.points = "";
/*    */
    }

    /*    */
/*    */
    public void setAttMap(HashMap map) {
/* 48 */
        super.setAttMap(map);
/* 49 */
        this.points = MapUtil.getString(map, "points", this.points);
/*    */
    }

    /*    */
/*    */
    public HashMap getAttMap() {
/* 53 */
        HashMap map = super.getAttMap();
/* 54 */
        map.put("points", this.points);
/* 55 */
        return map;
/*    */
    }

    /*    */
    public Object clone() {
/* 58 */
        return new ElePolygon(this.xdoc, getAttMap());
/*    */
    }

    /*    */
    protected Shape getShape() {
/* 61 */
        int n = To.toInt(this.points, 3);
/* 62 */
        if (n > 2) {
/* 63 */
            double d = -90.0D;
/* 64 */
            if (n % 2 == 0) {
/* 65 */
                d += 180 / n;
/*    */
            }
/* 67 */
            GeneralPath path = new GeneralPath();
/* 68 */
            path.moveTo((float) Math.cos(Math.toRadians(d)), (float) Math.sin(Math.toRadians(d)));
/* 69 */
            for (int i = 1; i <= n; i++) {
/* 70 */
                path.lineTo((float) Math.cos(Math.toRadians(360.0D / n * i + d)), (float) Math.sin(Math.toRadians(360.0D / n * i + d)));
/*    */
            }
/* 72 */
            path.closePath();
/* 73 */
            Shape shape = path;
/* 74 */
            AffineTransform af = new AffineTransform();
/* 75 */
            Rectangle2D bound = shape.getBounds2D();
/* 76 */
            af = new AffineTransform();
/* 77 */
            af.scale(this.width / bound.getWidth(), this.height / bound.getHeight());
/* 78 */
            shape = af.createTransformedShape(shape);
/* 79 */
            bound = shape.getBounds2D();
/* 80 */
            af = new AffineTransform();
/* 81 */
            af.translate(-bound.getX(), -bound.getY());
/* 82 */
            shape = af.createTransformedShape(shape);
/* 83 */
            return shape;
/*    */
        }
/* 85 */
        return super.getShape();
/*    */
    }

    /*    */
/*    */
    protected void bindValue(String val) {
/* 89 */
        this.points = val;
/*    */
    }
/*    */
}

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.ElePolygon
 * JD-Core Version:    0.6.0
 */