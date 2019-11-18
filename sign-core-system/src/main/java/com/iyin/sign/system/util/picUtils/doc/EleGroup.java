//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;


import com.iyin.sign.system.util.picUtils.util.MapUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class EleGroup extends EleShape {
    public String drawType;
    public static String DRAW_TYPE_ZOOM = "zoom";
    public static String DRAW_TYPE_CENTER = "center";
    public static String DRAW_TYPE_FACT = "fact";
    public static String DRAW_TYPE_ADJUST = "adjust";
    public boolean lockRatio;
    public String text;

    protected EleBase copyEle(XDoc xdoc) {
        return new EleGroup(xdoc, this.getAttMap());
    }

    public Object clone() {
        return new EleGroup(this.xdoc, this.getAttMap());
    }

    public EleGroup(XDoc xdoc, HashMap attMap) {
        super(xdoc, attMap);
    }

    public EleGroup(XDoc xdoc) {
        super(xdoc);
    }

    protected void init() {
        super.init();
        this.typeName = "group";
        this.color = null;
        this.text = "";
        this.drawType = DRAW_TYPE_ZOOM;
    }

    public void setAttMap(HashMap map) {
        super.setAttMap(map);
        if(map.containsKey("text")) {
            this.text = MapUtil.getString(map, "text", this.text);
        } else if(map.containsKey("gname")) {
            this.text = MapUtil.getString(map, "gname", this.text);
        }

        this.drawType = MapUtil.getString(map, "drawType", this.drawType);
        if(MapUtil.getBool(map, "lockRatio", false)) {
            this.drawType = DRAW_TYPE_ADJUST;
        }

    }

    public HashMap getAttMap() {
        HashMap map = super.getAttMap();
        map.put("text", this.text);
        map.put("drawType", this.drawType);
        return map;
    }

    protected void drawOther(Graphics2D g, Shape shape) {
        EleRect rect = DocUtil.getRect(this.xdoc, this.text);
        if(rect != null) {
            rect.xdoc.scale = this.xdoc.scale;
            g = (Graphics2D)g.create(0, 0, this.width + 1, this.height + 1);
            if(this.drawType.startsWith(DRAW_TYPE_ADJUST)) {
                double scale;
                if((double)rect.width / (double)rect.height > (double)this.width / (double)this.height) {
                    scale = (double)(this.width - this.margin * 2) / (double)rect.width;
                } else {
                    scale = (double)(this.height - this.margin * 2) / (double)rect.height;
                }

                double offx = ((double)this.width - (double)rect.width * scale) / 2.0D;
                double offy = ((double)this.height - (double)rect.height * scale) / 2.0D;
                if(this.drawType.endsWith("left")) {
                    offx = 0.0D;
                } else if(this.drawType.endsWith("top")) {
                    offy = 0.0D;
                } else if(this.drawType.endsWith("right")) {
                    offx *= 2.0D;
                } else if(this.drawType.endsWith("bottom")) {
                    offy *= 2.0D;
                }

                AffineTransform af = AffineTransform.getScaleInstance(scale, scale);
                af.translate(offx / scale, offy / scale);
                g.transform(af);
            } else if(!this.drawType.equals(DRAW_TYPE_FACT)) {
                g.transform(AffineTransform.getScaleInstance((double)(this.width - this.margin * 2) / (double)rect.width, (double)(this.height - this.margin * 2) / (double)rect.height));
            }

            rect.print(g);
            g.dispose();
        }

    }
}
