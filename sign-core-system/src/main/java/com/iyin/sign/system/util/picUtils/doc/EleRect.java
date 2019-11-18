//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;

import com.iyin.sign.system.util.picUtils.data.BlkExpression;
import com.iyin.sign.system.util.picUtils.data.Conn;
import com.iyin.sign.system.util.picUtils.data.LogicExpression;
import com.iyin.sign.system.util.picUtils.data.Parser;
import com.iyin.sign.system.util.picUtils.util.ColorUtil;
import com.iyin.sign.system.util.picUtils.util.MapUtil;
import com.iyin.sign.system.util.picUtils.util.StrUtil;
import com.iyin.sign.system.util.picUtils.util.To;

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Line2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;

public class EleRect extends EleBase {
    public static final String SIZE_NORMAL = "normal";
    public static final String SIZE_AUTOWIDTH = "autowidth";
    public static final String SIZE_AUTOHEIGHT = "autoheight";
    public static final String SIZE_AUTOSIZE = "autosize";
    public static final String SIZE_AUTOFONT = "autofont";
    public static final String SIZE_AUTOTIP = "autotip";
    public static final String SIZE_AUTOOUT = "autoout";
    public static final String LINE_NONE = "00000000";
    public static final String LINE_RECT = "11110000";
    public static final String LINE_BOTTOM = "00010000";
    public int left;
    public int top;
    public int width;
    public int height;
    public String arc;
    public Color fillColor;
    public String fillImg;
    public Color fillColor2;
    public String sizeType;
    public int rotate;
    public int srotate;
    public String gradual;
    public String href;
    public String line;
    public String filter;
    public String filterTarget;
    public String filterParam;
    public int column;
    public int txtPadding;
    public int margin;
    public String layoutFlow;
    public boolean layoutLine;
    public String toolTip;
    public int row;
    public int col;
    public int rowSpan;
    public int colSpan;
    public String fillRatio;
    public String scale;
    public Color color;
    public String align;
    public String valign;
    public String zPosition;
    public double strokeWidth;
    public String strokeStyle;
    public String strokeImg;
    public String visible;
    public String distort;
    public String dock;
    public String comment;
    public ArrayList lineList;
    private boolean hasPrintExp = false;
    protected BufferedImage imgFill;
    public static final int DEF_HEIGHT = 24;
    public static final int DEF_WIDTH = 96;
    private static final int minFontSize = 4;
    private static int maxImgHeight = 1200;
    private static int maxImgWidth = 1200;

    protected EleBase copyEle(XDoc xdoc) {
        return new EleRect(xdoc, this.getAttMap());
    }

    public Object clone() {
        return new EleRect(this.xdoc, this.getAttMap());
    }

    public EleRect(XDoc xdoc, HashMap attMap) {
        super(xdoc, attMap);
    }

    public EleRect(XDoc xdoc) {
        super(xdoc);
    }

    protected void init() {
        super.init();
        this.typeName = "rect";
        this.fillColor = null;
        this.fillImg = "";
        this.align = "top";
        this.sizeType = "normal";
        this.left = 0;
        this.arc = "0";
        this.top = 0;
        this.width = 96;
        this.height = 24;
        this.rotate = 0;
        this.txtPadding = 2;
        this.layoutFlow = "h";
        this.href = "";
        this.toolTip = "";
        this.line = "11110000";
        this.column = 1;
        this.gradual = "";
        this.filter = "";
        this.filterTarget = "all";
        this.filterParam = "";
        this.margin = 0;
        this.colSpan = 1;
        this.rowSpan = 1;
        this.layoutLine = false;
        this.fillRatio = "";
        this.scale = "";
        this.color = Color.BLACK;
        this.strokeWidth = 1.0D;
        this.strokeStyle = "0";
        this.align = "top";
        this.strokeImg = "";
        this.zPosition = "top";
        this.valign = "bottom";
        this.distort = "";
        this.visible = "true";
        this.dock = "";
        this.comment = "";
    }

    public HashMap getAttMap() {
        HashMap map = super.getAttMap();
        map.put("left", String.valueOf(this.left));
        map.put("top", String.valueOf(this.top));
        map.put("sizeType", this.sizeType);
        map.put("width", String.valueOf(this.width));
        map.put("height", String.valueOf(this.height));
        map.put("fillColor", this.fillColor == null?"": ColorUtil.colorToStr(this.fillColor));
        map.put("fillColor2", this.fillColor2 == null?"":ColorUtil.colorToStr(this.fillColor2));
        map.put("fillImg", this.fillImg);
        map.put("line", this.line);
        if(this.gradual.equals("0")) {
            this.gradual = "";
        }

        map.put("gradual", this.gradual);
        map.put("arc", this.arc);
        map.put("href", this.href);
        map.put("filter", this.filter);
        map.put("filterTarget", this.filterTarget);
        map.put("filterParam", this.filterParam);
        map.put("column", String.valueOf(this.column));
        map.put("rotate", String.valueOf(this.rotate));
        map.put("srotate", String.valueOf(this.srotate));
        map.put("padding", String.valueOf(this.txtPadding));
        map.put("margin", String.valueOf(this.margin));
        map.put("toolTip", this.toolTip);
        map.put("layoutFlow", this.layoutFlow);
        map.put("row", String.valueOf(this.row + 1));
        map.put("rowSpan", String.valueOf(this.rowSpan));
        map.put("col", String.valueOf(this.col + 1));
        map.put("colSpan", String.valueOf(this.colSpan));
        map.put("layoutLine", String.valueOf(this.layoutLine));
        map.put("fillRatio", this.fillRatio);
        map.put("scale", this.scale);
        map.put("color", this.color == null?"":ColorUtil.colorToStr(this.color));
        map.put("strokeWidth", String.valueOf(this.strokeWidth));
        map.put("strokeStyle", this.strokeStyle);
        map.put("strokeImg", this.strokeImg);
        map.put("align", this.align);
        map.put("zPosition", this.zPosition);
        map.put("valign", this.valign);
        map.put("visible", this.visible);
        map.put("distort", this.distort);
        map.put("dock", this.dock);
        map.put("comment", this.comment);
        return map;
    }

    public void setAttMap(HashMap map) {
        super.setAttMap(map);
        this.left = MapUtil.getInt(map, "left", this.left);
        this.top = MapUtil.getInt(map, "top", this.top);
        this.sizeType = MapUtil.getString(map, "sizeType", this.sizeType).toLowerCase();
        this.width = MapUtil.getInt(map, "width", this.width);
        this.height = MapUtil.getInt(map, "height", this.height);
        this.arc = MapUtil.getString(map, "arc", this.arc);
        if(map.containsKey("fillColor")) {
            this.fillColor = ColorUtil.strToColor(MapUtil.getString(map, "fillColor", ""), (Color)null);
        }

        if(map.containsKey("fillColor2")) {
            this.fillColor2 = ColorUtil.strToColor(MapUtil.getString(map, "fillColor2", ""), (Color)null);
        }

        this.gradual = MapUtil.getString(map, "gradual", this.gradual);
        this.fillImg = MapUtil.getString(map, "fillImg", this.fillImg);
        this.line = MapUtil.getString(map, "line", this.line);
        this.href = MapUtil.getString(map, "href", this.href);
        this.filter = MapUtil.getString(map, "filter", this.filter);
        this.filterTarget = MapUtil.getString(map, "filterTarget", this.filterTarget);
        this.filterParam = MapUtil.getString(map, "filterParam", this.filterParam);
        this.column = MapUtil.getInt(map, "column", this.column);
        this.rotate = MapUtil.getInt(map, "rotate", this.rotate);
        this.srotate = MapUtil.getInt(map, "srotate", this.srotate);
        this.txtPadding = MapUtil.getInt(map, "padding", this.txtPadding);
        this.margin = MapUtil.getInt(map, "margin", this.margin);
        this.layoutFlow = MapUtil.getString(map, "layoutFlow", this.layoutFlow);
        this.toolTip = MapUtil.getString(map, "toolTip", this.toolTip);
        this.row = MapUtil.getInt(map, "row", this.row + 1) - 1;
        this.rowSpan = MapUtil.getInt(map, "rowSpan", this.rowSpan);
        this.col = MapUtil.getInt(map, "col", this.col + 1) - 1;
        this.colSpan = MapUtil.getInt(map, "colSpan", this.colSpan);
        this.layoutLine = MapUtil.getBool(map, "layoutLine", this.layoutLine);
        this.fillRatio = MapUtil.getString(map, "fillRatio", this.fillRatio);
        this.scale = MapUtil.getString(map, "scale", this.scale);
        if(map.containsKey("color")) {
            this.color = ColorUtil.strToColor(MapUtil.getString(map, "color", ""), (Color)null);
        }

        this.strokeWidth = MapUtil.getDouble(map, "strokeWidth", this.strokeWidth);
        if(map.containsKey("weight")) {
            this.strokeWidth = MapUtil.getDouble(map, "weight", this.strokeWidth);
        }

        this.strokeStyle = MapUtil.getString(map, "strokeStyle", this.strokeStyle);
        if(map.containsKey("dashStyle")) {
            this.strokeStyle = MapUtil.getString(map, "dashStyle", this.strokeStyle);
        }

        this.strokeImg = MapUtil.getString(map, "strokeImg", this.strokeImg);
        this.align = MapUtil.getString(map, "align", this.align);
        this.zPosition = MapUtil.getString(map, "zPosition", this.zPosition);
        this.valign = MapUtil.getString(map, "valign", this.valign);
        this.visible = MapUtil.getString(map, "visible", this.visible);
        this.distort = MapUtil.getString(map, "distort", this.distort);
        this.dock = MapUtil.getString(map, "dock", this.dock);
        this.comment = MapUtil.getString(map, "comment", this.comment);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < this.eleList.size(); ++i) {
            if(i > 0) {
                sb.append("\n");
            }

            sb.append(this.eleList.get(i));
        }

        return sb.toString();
    }

    public void setText(String text) {
        setText(this, text);
    }

    public static void setText(EleRect rect, String str) {
        ElePara para = null;
        EleText txt = null;

        for(int i = 0; i < rect.eleList.size(); ++i) {
            Object obj = rect.eleList.get(i);
            if(obj instanceof ElePara) {
                para = (ElePara)obj;
                if(para.eleList.size() > 0 && para.eleList.get(0) instanceof EleText) {
                    txt = (EleText)para.eleList.get(0);
                } else {
                    txt = new EleText(rect.xdoc);
                    para.eleList.add(0, txt);
                }
                break;
            }
        }

        if(txt == null) {
            para = new ElePara(rect.xdoc);
            txt = new EleText(rect.xdoc);
            para.eleList.add(txt);
            rect.eleList.add(para);
        }

        txt.setText(str);
        rect.relayout();
    }

    public void print(Graphics2D g) {
        if(this.isVisible()) {
            if(this.xdoc.print != null) {
                Shape fshape = null;
                if(this.href.length() > 0) {
                    if(fshape == null) {
                        fshape = this.getFillShape();
                    }

                    this.xdoc.print.hrefs.add(new NameShape(DocUtil.fixHref(this.href, this.name), g.getTransform().createTransformedShape(fshape)));
                }

                if(this.toolTip.length() > 0) {
                    if(fshape == null) {
                        fshape = this.getFillShape();
                    }

                    this.xdoc.print.toolTips.add(new NameShape(this.toolTip, g.getTransform().createTransformedShape(fshape)));
                }
            }

            this.actPrint(g);
        }

    }

    protected void actPrint(Graphics2D g) {
        if(this.scale.length() > 0) {
            String[] scales = this.scale.split(",");
            double xscale = 1.0D;
            double yscale = 1.0D;
            if(scales.length > 0) {
                xscale = To.toDouble(scales[0], 1.0D);
                if(xscale > 1.0D) {
                    xscale -= (double)((int)xscale);
                }

                yscale = xscale;
            }

            if(scales.length > 1) {
                yscale = To.toDouble(scales[1], yscale);
                if(yscale > 1.0D) {
                    yscale -= (double)((int)yscale);
                }
            }

            String sb = "c";
            if(scales.length > 2) {
                sb = scales[2].toLowerCase();
            }

            if(xscale <= 0.0D || yscale <= 0.0D) {
                return;
            }

            double w = (double)this.width * xscale;
            double h = (double)this.height * yscale;
            double x;
            double y;
            if(sb.equals("nw")) {
                x = 0.0D;
                y = 0.0D;
            } else if(sb.equals("n")) {
                x = 0.0D;
                y = 0.0D;
            } else if(sb.equals("ne")) {
                x = (double)this.width - w;
                y = 0.0D;
            } else if(sb.equals("w")) {
                x = 0.0D;
                y = ((double)this.height - h) / 2.0D;
            } else if(sb.equals("e")) {
                x = (double)this.width - w;
                y = ((double)this.height - h) / 2.0D;
            } else if(sb.equals("sw")) {
                x = 0.0D;
                y = (double)this.height - h;
            } else if(sb.equals("s")) {
                x = ((double)this.width - w) / 2.0D;
                y = (double)this.height - h;
            } else if(sb.equals("se")) {
                x = (double)this.width - w;
                y = (double)this.height - h;
            } else {
                x = ((double)this.width - w) / 2.0D;
                y = ((double)this.height - h) / 2.0D;
            }

            g = (Graphics2D)g.create((int) Math.round(x), (int) Math.round(y), (int) Math.round(w) + 1, (int) Math.round(h) + 1);
            g.scale(xscale, yscale);
        }

        this.printSelf(g);
        this.printContent(g);
    }

    protected void printSelf(Graphics2D g) {
        this.drawShape(g, this.fillShape(g));
    }

    protected void printContent(Graphics2D g) {
        this.printRect(g, true);
        this.printText(g);
        this.printRect(g, false);
    }

    protected Rectangle getBound() {
        return new Rectangle(this.left, this.top, this.width, this.height);
    }

    protected void printText(Graphics2D g) {
        ArrayList lineList = this.getLineList();
        int lineTop = 0;
        int lineHeight = 0;
        int txtLeft = this.getTextLeft();
        boolean txtWidth = false;
        boolean vflow = this.layoutFlow.equals("v");
        int var10;
        if(vflow) {
            var10 = this.getTextHeight();
        } else {
            var10 = this.getTextWidth();
        }

        for(int i = 0; i < lineList.size(); ++i) {
            DocPageLine line = (DocPageLine)lineList.get(i);
            if(this.layoutLine && this.color != null) {
                this.setStroke(g);
                lineHeight = line.height;
                if(vflow) {
                    lineTop = line.top - lineHeight;
                    g.draw(new Double((double)lineTop, (double)txtLeft, (double)lineTop, (double)(txtLeft + var10)));
                } else {
                    lineTop = line.top + lineHeight;
                    g.draw(new Double((double)txtLeft, (double)(lineTop - 1), (double)(txtLeft + var10), (double)(lineTop - 1)));
                }

                PaintUtil.resetStroke(g);
            }

            line.print(g, this.getTextLeft());
        }

        if(this.layoutLine && this.color != null) {
            this.setStroke(g);
            if(vflow) {
                for(lineTop -= lineHeight; lineTop > txtLeft; lineTop -= lineHeight) {
                    g.draw(new Double((double)(lineTop + 1), (double)txtLeft, (double)(lineTop + 1), (double)(txtLeft + var10)));
                }
            } else {
                for(lineTop += lineHeight; lineTop < this.height - txtLeft; lineTop += lineHeight) {
                    g.draw(new Double((double)txtLeft, (double)(lineTop - 1), (double)(txtLeft + var10), (double)(lineTop - 1)));
                }
            }

            PaintUtil.resetStroke(g);
        }

    }

    protected void textAutosize() {
        boolean vertical = this.layoutFlow.equals("v");
        if(vertical && this.sizeType.equals("autowidth") || !vertical && this.sizeType.equals("autoheight")) {
            DocPageLine line = null;
            int top = this.getTextTop();
            ArrayList hrList = new ArrayList();

            int w;
            for(w = 0; w < this.eleList.size(); ++w) {
                if(this.eleList.get(w) instanceof EleRect) {
                    EleRect i = (EleRect)this.eleList.get(w);
                    if(i.zPosition.equals("around")) {
                        HindRance j = new HindRance(new Rectangle(i.left, i.top, i.width, i.height));
                        hrList.add(j);
                    }
                }
            }

            ArrayList list;
            int var9;
            if(this.column <= 1) {
                if(vertical) {
                    top = this.width - this.getTextTop();
                }

                for(w = 0; w < this.eleList.size(); ++w) {
                    if(this.eleList.get(w) instanceof ElePara) {
                        if(this.layoutFlow.equals("v")) {
                            list = ((ElePara)this.eleList.get(w)).toLineList(top, this.getTextHeight(), hrList, false);
                        } else {
                            list = ((ElePara)this.eleList.get(w)).toLineList(top, this.getTextWidth(), hrList, true);
                        }

                        for(var9 = 0; var9 < list.size(); ++var9) {
                            line = (DocPageLine)list.get(var9);
                            if(vertical) {
                                top -= line.height;
                            } else {
                                top += line.height;
                            }
                        }
                    }
                }

                if(vertical && this.sizeType.equals("autowidth")) {
                    this.width -= top - this.getTextTop();
                } else if(!vertical && this.sizeType.equals("autoheight")) {
                    this.height = top + this.getTextTop();
                }
            } else {
                w = (this.width - this.txtPadding() * 2 * this.column) / this.column;

                for(var9 = 0; var9 < this.eleList.size(); ++var9) {
                    if(this.eleList.get(var9) instanceof ElePara) {
                        list = ((ElePara)this.eleList.get(var9)).toLineList(top, w, hrList, true);

                        for(int var10 = 0; var10 < list.size(); ++var10) {
                            line = (DocPageLine)list.get(var10);
                            top += line.height;
                        }
                    }
                }

                this.height = top + this.getTextTop() + (line != null?line.height:0) / this.column;
            }
        }

    }

    public int txtPadding() {
        return this.txtPadding + this.margin;
    }

    protected int getTextTop() {
        return this.txtPadding() + (int)this.strokeWidth;
    }

    protected int getTextLeft() {
        return this.getTextTop();
    }

    protected int getTextHeight() {
        return Math.abs(this.height - this.txtPadding() * 2 - (int)this.strokeWidth - (int)this.strokeWidth);
    }

    protected int getTextWidth() {
        return Math.abs(this.width - this.txtPadding() * 2 - (int)this.strokeWidth - (int)this.strokeWidth);
    }

    public void setStroke(Graphics2D g2) {
        BufferedImage imgStroke = ImgUtil.loadImg(this.xdoc, this.strokeImg, this.color, (Color)null, false);
        TexturePaint b;
        if(imgStroke != null) {
            b = new TexturePaint(imgStroke, new Rectangle(0, 0, imgStroke.getWidth(), imgStroke.getHeight()));
            b = PaintUtil.checkPaint(g2, b);
            g2.setPaint(b);
        } else {
            g2.setColor(this.color);
        }

        b = null;
        double sw = this.strokeWidth;
        Object var14;
        if(this.strokeStyle.length() != 0 && !this.strokeStyle.equals("0")) {
            char c = this.strokeStyle.charAt(0);
            if(c != 126 && c != 64 && c != 35 && c != 36) {
                String[] var15 = this.strokeStyle.split(",");
                float[] var16 = new float[var15.length];

                for(int var17 = 0; var17 < var15.length; ++var17) {
                    try {
                        var16[var17] = (float)java.lang.Double.parseDouble(var15[var17]);
                        if(var16[var17] < 1.0F) {
                            var16[var17] = 1.0F;
                        }
                    } catch (Exception var13) {
                        var16[var17] = 0.0F;
                    }
                }

                try {
                    var14 = new BasicStroke((float)sw, 0, 0, 20.0F, var16, 1.0F);
                } catch (Exception var12) {
                    var14 = new BasicStroke((float)sw, 0, 0, 20.0F);
                }
            } else if(sw > 1.0D) {
                if(c == 126) {
                    var14 = TextStrokeUtil.createStroke(ShapeUtil.strToShape(this.strokeStyle.substring(1)), (float)sw);
                } else {
                    boolean str = c == 36;
                    boolean dash = c == 64;
                    String e = this.strokeStyle.substring(1);
                    String e1 = XFont.defaultFontName;
                    if(e.startsWith("(") && e.indexOf(")") > 0) {
                        e1 = e.substring(1, e.indexOf(")"));
                        e = e.substring(e.indexOf(")") + 1);
                    }

                    var14 = TextStrokeUtil.createStroke(e, XFont.createFont(e1, 0, (int) Math.ceil(sw), e), str, dash);
                }
            } else {
                var14 = new BasicStroke((float)sw, 0, 0, 20.0F);
            }
        } else {
            var14 = new BasicStroke((float)sw, 1, 0, 20.0F);
        }

        try {
            g2.setStroke((Stroke)var14);
        } catch (Exception var11) {
            ;
        }

    }

    public ArrayList getParaList() {
        ArrayList paras = new ArrayList();

        for(int i = 0; i < this.eleList.size(); ++i) {
            if(this.eleList.get(i) instanceof ElePara) {
                paras.add(this.eleList.get(i));
            }
        }

        return paras;
    }

    public ArrayList getRectList() {
        ArrayList rects = new ArrayList();

        for(int i = 0; i < this.eleList.size(); ++i) {
            if(!(this.eleList.get(i) instanceof ElePara)) {
                rects.add(this.eleList.get(i));
            }
        }

        return rects;
    }

    public ArrayList getLineList() {
        if(this.lineList == null || this.hasPrintExp) {
            this.genLineList((ArrayList)null, 0);
        }

        return this.lineList;
    }

    public static boolean isBlank(EleRect rect) {
        return rect.eleList.size() == 0 || rect.eleList.size() == 1 && rect.eleList.get(0) instanceof ElePara && (((ElePara)rect.eleList.get(0)).eleList.size() == 0 || ((ElePara)rect.eleList.get(0)).eleList.size() == 1 && ((ElePara)rect.eleList.get(0)).eleList.get(0) instanceof EleText && ((EleText)((ElePara)rect.eleList.get(0)).eleList.get(0)).text.length() == 0);
    }

    public static boolean hasTxt(EleRect shape) {
        boolean has = false;
        int npara = 0;

        for(int i = 0; i < shape.eleList.size(); ++i) {
            if(shape.eleList.get(i) instanceof ElePara) {
                ++npara;
                if(npara > 1) {
                    has = true;
                    break;
                }

                if(((ElePara)shape.eleList.get(i)).eleList.size() > 1 || ((ElePara)shape.eleList.get(i)).eleList.size() == 1 && (!(((ElePara)shape.eleList.get(i)).eleList.get(i) instanceof EleText) || ((ElePara)shape.eleList.get(i)).eleList.get(i) instanceof EleText && ((EleText)((ElePara)shape.eleList.get(i)).eleList.get(i)).text.length() > 0)) {
                    has = true;
                    break;
                }
            }
        }

        return has;
    }

    public static boolean hasOneTxt(EleRect rect) {
        return rect.eleList.size() == 1 && rect.eleList.get(0) instanceof ElePara && ((ElePara)rect.eleList.get(0)).eleList.size() == 1 && ((ElePara)rect.eleList.get(0)).eleList.get(0) instanceof EleText;
    }

    protected int getArc() {
        boolean a = false;
        int a1;
        if(this.arc.indexOf(",") > 0) {
            a1 = To.toInt(this.arc.substring(0, this.arc.indexOf(",")));
        } else {
            a1 = To.toInt(this.arc);
        }

        return a1;
    }

    public Point viewSize() {
        if(this.rotate == 0) {
            return new Point(this.width, this.height);
        } else {
            double r = (double)this.rotate / 180.0D * 3.141592653589793D;
            return new Point((int) Math.abs(Math.ceil((double)this.width * Math.cos(r) + (double)this.height * Math.sin(r))), (int) Math.abs(Math.ceil((double)this.width * Math.sin(r) + (double)this.height * Math.cos(r))));
        }
    }

    protected void printRect(Graphics2D g, boolean bottom) {
        for(int i = 0; i < this.eleList.size(); ++i) {
            if(this.eleList.get(i) instanceof EleRect) {
                EleRect eleRect = (EleRect)this.eleList.get(i);
                eleRect.autoSize();
                this.dock(eleRect);
                if(bottom && eleRect.zPosition.equals("bottom") || !bottom && !eleRect.zPosition.equals("bottom")) {
                    if(eleRect.rotate == 0) {
                        if(eleRect.left < this.width && eleRect.top < this.height) {
                            Graphics2D rectangle = (Graphics2D)g.create(eleRect.left, eleRect.top, eleRect.width + 1, eleRect.height + 1);
                            eleRect.print(rectangle);
                            rectangle.dispose();
                        }
                    } else {
                        int var9 = (int) Math.ceil(Math.pow(Math.pow((double)eleRect.width, 2.0D) + Math.pow((double)eleRect.height, 2.0D), 0.5D));
                        if(eleRect.left - (var9 - eleRect.width) / 2 < this.width && eleRect.top - (var9 - eleRect.height) / 2 < this.height) {
                            Graphics2D cg = (Graphics2D)g.create(eleRect.left - (var9 - eleRect.width) / 2, eleRect.top - (var9 - eleRect.height) / 2, var9, var9);
                            AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians((double)eleRect.rotate), (double)(var9 / 2), (double)(var9 / 2));
                            cg.transform(at);
                            cg.translate((var9 - eleRect.width) / 2, (var9 - eleRect.height) / 2);
                            eleRect.print(cg);
                            cg.dispose();
                        }
                    }
                }

                if(this.eleList.get(i) instanceof EleLine) {
                    EleLine eleLine = (EleLine)this.eleList.get(i);
                    if(bottom && eleLine.zPosition.equals("bottom") || !bottom && !eleLine.zPosition.equals("bottom")) {
                        Rectangle var10 = EleLine.getViewBounds(eleLine);
                        eleLine.print(g, var10.x, var10.y);
                    }
                }
            }
        }

    }

    public void dock(EleRect rect) {
        if(rect.dock.length() > 0) {
            String[] strs = rect.dock.split(",");
            int padding = 0;
            String rectDock = strs[0];
            if(strs.length > 1) {
                padding = To.toInt(strs[1]);
            }

            if(rectDock.indexOf("center") >= 0) {
                rect.left = (this.width - rect.width) / 2;
                rect.top = (this.height - rect.height) / 2;
            }

            if(rectDock.indexOf("top") >= 0) {
                rect.top = this.margin + padding;
            }

            if(rectDock.indexOf("bottom") >= 0) {
                rect.top = this.height - rect.height - this.margin - padding;
            }

            if(rectDock.indexOf("left") >= 0) {
                rect.left = this.margin + padding;
            }

            if(rectDock.indexOf("right") >= 0) {
                rect.left = this.width - rect.width - this.margin - padding;
            }
        }

    }

    public Shape getOutline() {
        Shape shape = this.getFillShape();
        if(this.isLineShape()) {
            shape = this.getLineShape(this.strokeWidth);
        }

        return shape;
    }

    public Shape fillShape(Graphics2D g) {
        Shape shape = this.getFillShape();
        if(this.canFill()) {
            Object af = shape;
            if(this.fillRatio.length() > 0) {
                Area area = new Area(shape);
                String strfr = this.fillRatio;
                if(strfr.indexOf("$[") >= 0) {
                    strfr = DocUtil.printEval(strfr, this.xdoc);
                }

                double actFillRatio = To.toDouble(strfr, 1.0D);
                if(actFillRatio > 1.0D) {
                    actFillRatio -= (double)((int)actFillRatio);
                }

                if(actFillRatio != 1.0D) {
                    Rectangle bounds = shape.getBounds();
                    Area interArea;
                    int w1;
                    if(actFillRatio > 0.0D) {
                        if(this.width > this.height) {
                            interArea = new Area(new Rectangle(bounds.x, bounds.y, (int)((double)bounds.width * actFillRatio), bounds.height));
                        } else if(this.width == this.height) {
                            double w = Math.pow(Math.pow((double)bounds.width, 2.0D) * 2.0D, 0.5D);
                            interArea = new Area(new Arc2D.Double((double)bounds.x + ((double)bounds.width - w) / 2.0D, (double)bounds.y + ((double)bounds.width - w) / 2.0D, w, w, 90.0D, -actFillRatio * 360.0D, 2));
                        } else {
                            w1 = (int)((double)bounds.height * actFillRatio);
                            interArea = new Area(new Rectangle(bounds.x, bounds.y + bounds.height - w1, bounds.width, w1));
                        }
                    } else {
                        actFillRatio = -actFillRatio;
                        if(this.width > this.height) {
                            w1 = (int)((double)bounds.width * actFillRatio);
                            interArea = new Area(new Rectangle(bounds.x + bounds.width - w1, bounds.y, w1, bounds.height));
                        } else if(this.width == this.height) {
                            interArea = new Area(new Arc2D.Double((double)bounds.x, (double)bounds.y, (double)bounds.width, (double)bounds.height, 90.0D, actFillRatio * 360.0D, 2));
                        } else {
                            interArea = new Area(new Rectangle(bounds.x, bounds.y, bounds.width, (int)((double)bounds.height * actFillRatio)));
                        }
                    }

                    area.intersect(interArea);
                    af = area;
                }
            }

            PaintUtil.fill(g, this.xdoc, (Shape)af, this.fillColor, this.fillImg, this.gradual, this.fillColor2);
        }

        if(this.margin != 0) {
            g = (Graphics2D)g.create(this.margin, this.margin, this.width - this.margin * 2, this.height - this.margin * 2);
        }

        try {
            this.drawOther(g, shape);
        } finally {
            if(this.margin != 0) {
                g.dispose();
            }

        }

        if(this.isLineShape()) {
            shape = this.getLineShape(this.strokeWidth);
            AffineTransform af1 = new AffineTransform();
            af1.translate(0.5D, 0.5D);
            shape = af1.createTransformedShape(shape);
        }

        return shape;
    }

    protected boolean canFill() {
        return true;
    }

    protected boolean isLineShape() {
        return !this.line.equals("11110000");
    }

    private Shape getLineShape(double sw) {
        String tline = StrUtil.rpad(this.line, 8, "0");
        if(tline.equals("11110000")) {
            return this.getRectShape();
        } else {
            Rectangle rect = new Rectangle(0, 0, this.width, this.height);
            GeneralPath path = new GeneralPath();
            byte n = 0;
            int var9 = n + 1;
            if(tline.charAt(n) == 49) {
                path.moveTo(0.0F, (float)rect.getHeight());
                path.lineTo(0.0F, 0.0F);
            }

            if(tline.charAt(var9++) == 49) {
                path.moveTo(0.0F, 0.0F);
                path.lineTo((float)rect.getWidth(), 0.0F);
            }

            if(tline.charAt(var9++) == 49) {
                path.moveTo((float)rect.getWidth(), 0.0F);
                path.lineTo((float)rect.getWidth(), (float)rect.getHeight());
            }

            if(tline.charAt(var9++) == 49) {
                path.moveTo((float)rect.getWidth(), (float)rect.getHeight());
                path.lineTo(0.0F, (float)rect.getHeight());
            }

            if(tline.charAt(var9++) == 49) {
                path.moveTo(0.0F, 0.0F);
                path.lineTo((float)rect.getWidth(), (float)rect.getHeight());
            }

            if(tline.charAt(var9++) == 49) {
                path.moveTo((float)rect.getWidth(), 0.0F);
                path.lineTo(0.0F, (float)rect.getHeight());
            }

            if(tline.charAt(var9++) == 49) {
                path.moveTo(0.0F, (float)rect.getHeight() / 2.0F);
                path.lineTo((float)rect.getWidth(), (float)rect.getHeight() / 2.0F);
            }

            if(tline.charAt(var9++) == 49) {
                path.moveTo((float)rect.getWidth() / 2.0F, 0.0F);
                path.lineTo((float)rect.getWidth() / 2.0F, (float)rect.getHeight());
            }

            Object shape = path;
            if(this.margin != 0 || sw > 1.0D) {
                AffineTransform af = new AffineTransform();
                af.scale(((double)this.width - sw - (double)(this.margin * 2)) / (double)this.width, ((double)this.height - sw - (double)(this.margin * 2)) / (double)this.height);
                Shape var10 = af.createTransformedShape(path);
                af = new AffineTransform();
                af.translate(sw / 2.0D + (double)this.margin, sw / 2.0D + (double)this.margin);
                shape = af.createTransformedShape(var10);
            }

            return (Shape)shape;
        }
    }

    public void drawShape(Graphics2D g, Shape shape) {
        if(this.strokeWidth > 0.0D) {
            if(this.strokeWidth >= 1.0D && this.strokeImg != null && this.strokeImg.length() > 0) {
                BufferedImage var7 = ImgUtil.loadImg(this.xdoc, this.strokeImg, this.color, (Color)null, false);
                if(var7 != null) {
                    var7 = ImgUtil.alpha(var7, Color.WHITE);
                    Shape var10 = this.getFillShape(1.0D);
                    if(this.isLineShape()) {
                        var10 = this.getLineShape(1.0D);
                    }

                    this.drawImageShape(g, var7, var10, (int)this.strokeWidth);
                }
            } else if(this.color != null) {
                if(this.strokeWidth <= 1.0D || !this.strokeStyle.startsWith("g") && !this.strokeStyle.startsWith("s") && !this.strokeStyle.startsWith("d")) {
                    this.setStroke(g);
                    g.draw(shape);
                    PaintUtil.resetStroke(g);
                } else {
                    String style = this.strokeStyle;
                    Color sw;
                    int ow;
                    int iw;
                    if(!style.equals("g1") && !style.equals("g2")) {
                        if(style.equals("g3") || style.equals("g4")) {
                            g.setStroke(new BasicStroke(2.0F));
                            sw = new Color(255, 255, 255, 0);
                            ow = (int)(this.strokeWidth * 2.0D);

                            for(iw = 1; iw < ow; ++iw) {
                                shape = this.getFillShape((double)(iw + 1));
                                if(this.isLineShape()) {
                                    shape = this.getLineShape((double)(iw + 1));
                                }

                                if(style.equals("g3")) {
                                    if(iw <= ow / 2) {
                                        g.setColor(ColorUtil.mix(this.color, sw, (double)((float)iw / ((float)ow / 2.0F))));
                                    } else {
                                        g.setColor(ColorUtil.mix(this.color, sw, (double)((float)(ow - iw) / ((float)ow / 2.0F))));
                                    }
                                } else if(style.equals("g4")) {
                                    if(iw <= ow / 2) {
                                        g.setColor(ColorUtil.mix(this.color, sw, (double)((float)(ow / 2 - iw) / ((float)ow / 2.0F))));
                                    } else {
                                        g.setColor(ColorUtil.mix(this.color, sw, (double)((float)(iw - ow / 2) / ((float)ow / 2.0F))));
                                    }
                                }

                                g.draw(shape);
                            }
                        } else if(!style.equals("s1") && !style.equals("s2") && !style.equals("s3") && !style.equals("s4")) {
                            if(!style.equals("d1") && !style.equals("d2") && !style.equals("d3") && !style.equals("d4")) {
                                g.setColor(this.color);
                                g.setStroke(new BasicStroke((float)this.strokeWidth));
                                g.draw(shape);
                            } else {
                                g.setColor(this.color);
                                float var9 = (float)(this.strokeWidth * 2.0D);
                                float var11;
                                float var12;
                                if(style.equals("d1")) {
                                    var11 = var9 / 16.0F;
                                    var12 = var9 / 16.0F;
                                } else if(style.equals("d2")) {
                                    var11 = var9 / 8.0F;
                                    var12 = var9 / 8.0F;
                                } else if(style.equals("d3")) {
                                    var11 = var9 / 8.0F;
                                    var12 = var9 / 16.0F;
                                } else {
                                    var11 = var9 / 16.0F;
                                    var12 = var9 / 8.0F;
                                }

                                g.setStroke(new BasicStroke(var11));
                                shape = this.getFillShape((double)(var9 / 4.0F));
                                if(this.isLineShape()) {
                                    shape = this.getLineShape((double)(var9 / 4.0F));
                                }

                                g.draw(shape);
                                g.setStroke(new BasicStroke(var12));
                                shape = this.getFillShape((double)(var9 * 3.0F / 4.0F));
                                if(this.isLineShape()) {
                                    shape = this.getLineShape((double)(var9 * 3.0F / 4.0F));
                                }

                                g.draw(shape);
                            }
                        } else {
                            AffineTransform var8 = new AffineTransform();
                            ow = Integer.parseInt(style.substring(1));
                            if(ow == 1) {
                                var8.translate(-this.strokeWidth / 3.0D, -this.strokeWidth / 3.0D);
                            } else if(ow == 2) {
                                var8.translate(this.strokeWidth / 3.0D, -this.strokeWidth / 3.0D);
                            } else if(ow == 3) {
                                var8.translate(-this.strokeWidth / 3.0D, this.strokeWidth / 3.0D);
                            } else if(ow == 4) {
                                var8.translate(this.strokeWidth / 3.0D, this.strokeWidth / 3.0D);
                            }

                            g.setStroke(new BasicStroke((float)this.strokeWidth / 2.0F));
                            g.setColor(ColorUtil.mix(this.color, new Color(255, 255, 255, 0), 0.5D));
                            g.draw(var8.createTransformedShape(shape));
                            g.setColor(this.color);
                            g.draw(shape);
                        }
                    } else {
                        g.setStroke(new BasicStroke(2.0F));
                        sw = new Color(255, 255, 255, 0);
                        ow = (int)(this.strokeWidth * 2.0D);

                        for(iw = 1; iw <= ow; ++iw) {
                            shape = this.getFillShape((double)(iw + 1));
                            if(this.isLineShape()) {
                                shape = this.getLineShape((double)(iw + 1));
                            }

                            if(style.equals("g1")) {
                                g.setColor(ColorUtil.mix(this.color, sw, (double)((float)iw / (float)ow)));
                            } else if(style.equals("g2")) {
                                g.setColor(ColorUtil.mix(this.color, sw, (double)((float)(ow - iw) / (float)ow)));
                            }

                            g.draw(shape);
                        }
                    }
                }
            }
        }

    }

    protected void drawImageShape(Graphics2D g, BufferedImage img, Shape shape, int strokeWidth) {
        if(strokeWidth >= 1) {
            int width = (int) Math.round((double)img.getWidth() / (double)img.getHeight() * (double)strokeWidth);
            if(width < 1) {
                width = 1;
            }

            BufferedImage timg = new BufferedImage(width, strokeWidth, 2);
            Graphics2D tg = (Graphics2D)timg.getGraphics();
            tg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            tg.drawImage(img, 0, 0, width, strokeWidth, (ImageObserver)null);
            tg.dispose();
            BufferedImage[] imgs = new BufferedImage[width];

            for(int bounds = 0; bounds < imgs.length; ++bounds) {
                imgs[bounds] = timg.getSubimage(bounds, 0, 1, strokeWidth);
            }

            Rectangle var29 = shape.getBounds();
            img = new BufferedImage(var29.x + var29.width + strokeWidth, var29.y + var29.height + strokeWidth, 2);
            tg = (Graphics2D)img.getGraphics();
            FlatteningPathIterator it = new FlatteningPathIterator(shape.getPathIterator((AffineTransform)null), 1.0D);
            float moveX = 0.0F;
            float moveY = 0.0F;
            float lastX = 0.0F;
            float lastY = 0.0F;
            float thisX = 0.0F;
            float thisY = 0.0F;
            float[] points = new float[6];
            boolean type = false;
            int imgInx = 0;

            for(AffineTransform af = new AffineTransform(); !it.isDone(); it.next()) {
                int var30 = it.currentSegment(points);
                float dx;
                float dy;
                float distance;
                float r;
                float angle;
                int i;
                switch(var30) {
                    case 0:
                        moveX = lastX = points[0];
                        moveY = lastY = points[1];
                    case 2:
                    case 3:
                    default:
                        continue;
                    case 4:
                        points[0] = moveX;
                        points[1] = moveY;
                    case 1:
                        thisX = points[0];
                        thisY = points[1];
                        dx = thisX - lastX;
                        dy = thisY - lastY;
                        distance = (float) Math.sqrt((double)(dx * dx + dy * dy));
                        r = 1.0F / distance;
                        angle = (float) Math.atan2((double)dy, (double)dx);
                        i = 0;
                }

                while((float)i < distance) {
                    float x = lastX + (float)i * dx * r;
                    float y = lastY + (float)i * dy * r;
                    af.setToTranslation((double)x, (double)y);
                    af.rotate((double)angle);
                    tg.drawImage(imgs[imgInx], af, (ImageObserver)null);
                    ++imgInx;
                    imgInx %= imgs.length;
                    ++i;
                }

                lastX = thisX;
                lastY = thisY;
            }

            tg.dispose();
            g.drawImage(img, (BufferedImageOp)null, 0, 0);
        }

    }

    public void drawShape(Graphics2D g) {
        this.drawShape(g, this.fillShape(g));
    }

    protected void drawOther(Graphics2D g, Shape shape) {
    }

    public void printRect(Graphics2D g) {
        this.drawShape(g);
        this.printRect(g, true);
        this.printRect(g, false);
    }

    public static ArrayList split(EleRect rect, int fh, int h, boolean firstOnly) {
        return rect instanceof EleTable?splitTable((EleTable)rect, fh, h, firstOnly):splitRect(rect, fh, h, firstOnly);
    }

    private static ArrayList splitRect(EleRect rect, int fh, int h, boolean firstOnly) {
        ArrayList pages = new ArrayList();
        int pageSize = 2147483647;
        if(rect.sizeType.equals("normal")) {
            int sbound = rect.height;
            EleRect pageno = (EleRect)rect.clone();
            pageno.height = fh;
            pages.add(pageno);
            sbound -= fh;

            for(pageSize = 1; sbound > 0; ++pageSize) {
                sbound -= h - pageno.txtPadding * 2;
                pageno = (EleRect)rect.clone();
                if(sbound > 0) {
                    pageno.height = h;
                } else {
                    pageno.height = sbound + h + pageno.txtPadding;
                }

                pages.add(pageno);
                if(firstOnly && pages.size() > 0) {
                    break;
                }
            }
        }

        boolean var17 = false;
        boolean pageno2 = false;

        for(int paras = 0; paras < rect.eleList.size(); ++paras) {
            if(rect.eleList.get(paras) instanceof EleRect) {
                EleRect shape = (EleRect)((EleRect)rect.eleList.get(paras)).clone();
                shape.eleList.addAll(((EleRect)rect.eleList.get(paras)).eleList);
                Rectangle var16 = shape.getBound();
                if(var16.x + var16.width >= 0 && var16.x <= rect.width) {
                    int var18;
                    if(var16.y < fh) {
                        var18 = 0;
                    } else {
                        var18 = (int) Math.ceil((double)((var16.y - fh) / h)) + 1;
                    }

                    int var19;
                    if(var16.y + var16.height <= fh) {
                        var19 = 0;
                    } else {
                        var19 = (int) Math.ceil((double)((var16.y + var16.height - fh) / h)) + 1;
                    }

                    if(!firstOnly || var18 <= 0) {
                        if(var18 != var19 && (shape instanceof EleRect && shape.getLineList().size() > 1 || shape instanceof EleTable && ((EleTable)shape).rows.length() > 1)) {
                            ArrayList var21 = split(shape, var18 == 0?fh - var16.y:h - (var16.y - fh) % h, h, firstOnly);

                            for(int trect = 0; trect < var21.size() && var18 + trect <= pageSize - 1; ++trect) {
                                if(trect > 0) {
                                    ((EleRect)var21.get(trect)).top = 0;
                                } else if(var18 > 0) {
                                    ((EleRect)var21.get(trect)).top = (var16.y - fh) % h;
                                }

                                if(trect == 0) {
                                    ((EleRect)var21.get(trect)).height = var18 == 0?fh - var16.y:h - (var16.y - fh) % h;
                                } else if(trect == var21.size() - 1) {
                                    ((EleRect)var21.get(trect)).height = (var16.y + var16.height - fh + ((EleRect)var21.get(trect)).getTextTop() * var21.size() * 2) % h;
                                } else {
                                    ((EleRect)var21.get(trect)).height = h;
                                }

                                addToPage(pages, rect, var21.get(trect), var18 + trect);
                            }
                        } else {
                            if(var18 > 0) {
                                if(shape instanceof EleLine) {
                                    EleLine n = (EleLine)shape;
                                    n.startY = (n.startY - fh) % h;
                                    n.endY = (n.endY - fh) % h;
                                } else {
                                    shape.top = (var16.y - fh) % h;
                                }
                            }

                            if(var18 < pageSize) {
                                addToPage(pages, rect, shape, var18);
                            }
                        }
                    }
                }
            }
        }

        ArrayList var20 = rect.getParaList();

        for(int var22 = 0; var20 != null && var20.size() > 0 && var22 < pageSize; ++var22) {
            EleRect var23;
            if(var22 > pages.size() - 1) {
                var23 = (EleRect)rect.clone();
                pages.add(var23);
            } else {
                var23 = (EleRect)pages.get(var22);
            }

            if(firstOnly && pages.size() > 1) {
                break;
            }

            if(!rect.sizeType.equals("normal")) {
                if(var22 == 0) {
                    var23.height = fh;
                } else {
                    var23.top = 0;
                    var23.height = h;
                }
            }

            var20 = var23.genLineList(var20, h);
            if(var20 == null && (rect.sizeType.equals("autoheight") || rect.sizeType.equals("autosize"))) {
                var23 = (EleRect)pages.get(pages.size() - 1);
                if(var23.lineList != null && var23.lineList.size() > 0) {
                    DocPageLine line = (DocPageLine)var23.lineList.get(var23.lineList.size() - 1);
                    var23.height = line.top + line.height + var23.txtPadding;

                    for(int i = 0; i < var23.eleList.size(); ++i) {
                        if(var23.eleList.get(i) instanceof EleRect) {
                            EleRect crect = (EleRect)var23.eleList.get(i);
                            if(var23.height < crect.top + crect.height) {
                                var23.height = crect.top + crect.height;
                            }
                        }
                    }
                }
            }
        }

        if(pages.size() == 0) {
            pages.add(rect);
        }

        return pages;
    }

    private static EleTable pageTable(EleTable tab, int[] rs, int fromRow, int toRow) {
        if(fromRow > toRow) {
            return new EleTable(tab.xdoc);
        } else {
            EleTable ttab = new EleTable(tab.xdoc, tab.getAttMap());
            ttab.header = 0;
            ttab.footer = 0;
            ttab.height = 0;
            StringBuffer sb = new StringBuffer();

            int cell;
            for(cell = 0; cell < tab.header; ++cell) {
                if(sb.length() > 0) {
                    sb.append(",");
                }

                sb.append(rs[cell]);
                ttab.height += rs[cell];
            }

            for(cell = fromRow; cell <= toRow; ++cell) {
                if(sb.length() > 0) {
                    sb.append(",");
                }

                sb.append(rs[cell]);
                ttab.height += rs[cell];
            }

            for(cell = rs.length - tab.footer; cell < rs.length; ++cell) {
                if(sb.length() > 0) {
                    sb.append(",");
                }

                sb.append(rs[cell]);
                ttab.height += rs[cell];
            }

            ttab.height += (int)ttab.strokeWidth * 2 + ttab.margin * 2;
            ttab.rows = sb.toString();

            for(int i = 0; i < tab.eleList.size(); ++i) {
                EleCell var9 = (EleCell)tab.eleList.get(i);
                EleCell cell2;
                if(var9.row >= 0 && var9.row < tab.header) {
                    cell2 = (EleCell)var9.clone();
                    cell2.eleList.addAll(var9.eleList);
                    ttab.eleList.add(cell2);
                }

                if(var9.row >= fromRow && var9.row <= toRow) {
                    cell2 = (EleCell)var9.clone();
                    cell2.row = tab.header + var9.row - fromRow;
                    cell2.eleList.addAll(var9.eleList);
                    ttab.eleList.add(cell2);
                }

                if(var9.row >= rs.length - tab.footer && var9.row < rs.length) {
                    cell2 = (EleCell)var9.clone();
                    cell2.row = tab.header + toRow - fromRow + 1 + (var9.row - (rs.length - tab.footer));
                    cell2.eleList.addAll(var9.eleList);
                    ttab.eleList.add(cell2);
                }
            }

            return ttab;
        }
    }

    private static ArrayList splitTable(EleTable table, int fh, int h, boolean firstOnly) {
        ArrayList pages = new ArrayList();
        String[] rowStrs = table.rows.split(",");
        int[] rs = new int[rowStrs.length];
        boolean firstPage = true;

        int startRow;
        for(startRow = 0; startRow < rs.length; ++startRow) {
            rs[startRow] = To.toInt(rowStrs[startRow], 24);
        }

        if(table.header < 0) {
            table.header = 0;
        }

        if(table.header > rs.length) {
            table.header = rs.length;
        }

        if(table.footer < 0) {
            table.footer = 0;
        }

        if(table.footer > rs.length - table.header) {
            table.footer = rs.length - table.header;
        }

        startRow = table.header;
        int hfcount = 0;

        int hcount;
        for(hcount = 0; hcount < table.header; ++hcount) {
            hfcount += rs[hcount];
        }

        for(hcount = rs.length - table.footer; hcount < rs.length; ++hcount) {
            hfcount += rs[hcount];
        }

        hcount = hfcount;

        int cw;
        for(cw = table.header; cw < rs.length - table.footer; ++cw) {
            if(firstPage) {
                if(hcount + rs[cw] <= fh) {
                    hcount += rs[cw];
                } else {
                    firstPage = false;
                    if(hcount != hfcount || fh != h) {
                        --cw;
                    }

                    pages.add(pageTable(table, rs, startRow, cw));
                    startRow = cw + 1;
                    hcount = hfcount;
                    if(firstOnly) {
                        return pages;
                    }
                }
            } else if(hcount + rs[cw] > h) {
                firstPage = false;
                if(hcount != hfcount) {
                    --cw;
                }

                pages.add(pageTable(table, rs, startRow, cw));
                startRow = cw + 1;
                hcount = hfcount;
            } else {
                hcount += rs[cw];
            }
        }

        if(startRow <= rs.length - table.footer - 1) {
            pages.add(pageTable(table, rs, startRow, rs.length - table.footer - 1));
        }

        if(pages.size() == 0) {
            pages.add(table);
        }

        if(table.xdoc.getMeta().getView().equals("table")) {
            cw = table.xdoc.getPaper().getContentWidth();

            for(int i = 0; i < pages.size(); ++i) {
                if(((EleTable)pages.get(i)).width > cw) {
                    table = (EleTable)pages.remove(i);
                    pages.addAll(i, splitTableW(table, cw));
                }
            }
        }

        return pages;
    }

    private static ArrayList splitTableW(EleTable table, int cw) {
        ArrayList pages = new ArrayList();
        String[] colStrs = table.cols.split(",");
        int[] cols = new int[colStrs.length];

        int start;
        for(start = 0; start < colStrs.length; ++start) {
            cols[start] = Integer.parseInt(colStrs[start]);
        }

        start = 0;
        int widthCount = 0;
        StringBuffer sbCol = new StringBuffer();

        EleCell cell;
        EleCell cell2;
        EleTable ttab;
        int j;
        for(j = 0; j < cols.length; ++j) {
            widthCount += cols[j];
            if(start < j && widthCount > cw) {
                widthCount -= cols[j];
                ttab = (EleTable)table.clone();
                pages.add(ttab);
                ttab.rows = table.rows;

                for(int j1 = 0; j1 < table.eleList.size(); ++j1) {
                    cell = (EleCell)table.eleList.get(j1);
                    if(cell.col >= start && cell.col < j) {
                        cell2 = (EleCell)cell.clone();
                        cell2.col -= start;
                        cell2.eleList.addAll(cell.eleList);
                        ttab.eleList.add(cell2);
                    }
                }

                ttab.width = widthCount;
                ttab.cols = sbCol.toString();
                start = j;
                widthCount = cols[j];
                sbCol.setLength(0);
            }

            if(sbCol.length() > 0) {
                sbCol.append(",");
            }

            sbCol.append(cols[j]);
        }

        if(widthCount > 0) {
            ttab = (EleTable)table.clone();
            pages.add(ttab);
            ttab.rows = table.rows;

            for(j = 0; j < table.eleList.size(); ++j) {
                cell = (EleCell)table.eleList.get(j);
                if(cell.col >= start) {
                    cell2 = (EleCell)cell.clone();
                    cell2.col -= start;
                    cell2.eleList.addAll(cell.eleList);
                    ttab.eleList.add(cell2);
                }
            }

            ttab.width = widthCount;
            ttab.cols = sbCol.toString();
        }

        return pages;
    }

    private static void addToPage(ArrayList pages, EleRect rect, Object shape, int pageno) {
        if(pages.size() <= pageno) {
            for(int i = pages.size(); i <= pageno; ++i) {
                pages.add(rect.clone());
            }
        }

        ((EleRect)pages.get(pageno)).eleList.add(shape);
    }

    private ArrayList hrList() {
        ArrayList hrList = new ArrayList();

        for(int i = 0; i < this.eleList.size(); ++i) {
            if(this.eleList.get(i) instanceof EleRect) {
                EleRect rect = (EleRect)this.eleList.get(i);
                if(rect.zPosition.equals("around")) {
                    HindRance hr = new HindRance(new Rectangle(rect.left, rect.top, rect.width, rect.height));
                    hrList.add(hr);
                }
            }
        }

        return hrList;
    }

    public void autoSize() {
        if(!this.sizeType.equals("normal") && !this.sizeType.equals("autotip")) {
            Point rsize;
            if(this.sizeType.equals("autofont")) {
                rsize = this.textSize();
                boolean tsize = false;

                for(Point tsize1 = new Point(this.width, this.height); !tsize && (rsize.x > tsize1.x || rsize.y > tsize1.y); rsize = this.textSize()) {
                    tsize = true;

                    for(int i = 0; i < this.eleList.size(); ++i) {
                        if(this.eleList.get(i) instanceof ElePara) {
                            ElePara para = (ElePara)this.eleList.get(i);

                            for(int j = 0; j < para.eleList.size(); ++j) {
                                if(para.eleList.get(j) instanceof EleText) {
                                    EleText txt = (EleText)para.eleList.get(j);
                                    if(txt.fontSize > 4) {
                                        --txt.fontSize;
                                        if(txt.fontSize > 4) {
                                            tsize = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                rsize = this.rectSize();
                Point var8 = this.textSize();
                this.width = Math.max(rsize.x, var8.x);
                this.height = Math.max(rsize.y, var8.y);
            }
        }

    }

    public Point rectSize() {
        if(this.lineList != null) {
            return new Point(this.getTextTop() * 2, this.getTextLeft() * 2);
        } else {
            Point p = new Point();
            if(this.sizeType.equals("autowidth")) {
                p.y = this.height;
            }

            if(this.sizeType.equals("autoheight")) {
                p.x = this.width;
            }

            int nw = 0;
            int nh = 0;
            boolean n = false;
            boolean b = true;

            for(int i = 0; i < this.eleList.size(); ++i) {
                EleBase ele = (EleBase)this.eleList.get(i);
                if(ele instanceof EleRect) {
                    EleRect rect = (EleRect)ele;
                    rect.autoSize();
                    if(rect.dock.length() > 0) {
                        rect.top = 0;
                        rect.left = 0;
                    }

                    int var9 = rect.top + rect.height;
                    if(var9 > nh) {
                        nh = var9;
                    }

                    var9 = rect.left + rect.width;
                    if(var9 > nw) {
                        nw = var9;
                    }

                    b = false;
                }
            }

            if(!b) {
                if(this.sizeType.equals("autowidth") || this.sizeType.equals("autosize")) {
                    p.x = nw + this.txtPadding * 2;
                }

                if(this.sizeType.equals("autoheight") || this.sizeType.equals("autosize")) {
                    p.y = nh + this.txtPadding * 2;
                }
            }

            return p;
        }
    }

    public Point textSize() {
        if(isBlank(this)) {
            return new Point(this.txtPadding * 2, this.txtPadding * 2);
        } else {
            Point p = new Point();
            ArrayList paras = this.getParaList();
            boolean vertical = this.layoutFlow.equals("v");
            if(this.sizeType.equals("autowidth")) {
                p.y = this.height;
            }

            if(this.sizeType.equals("autoheight")) {
                p.x = this.width;
            }

            int top;
            if(vertical) {
                top = this.width - this.getTextTop();
            } else {
                top = this.getTextTop();
            }

            ArrayList hrList = this.hrList();
            boolean textWidth = false;
            int var12;
            if(vertical && (this.sizeType.equals("autowidth") || this.sizeType.equals("autofont"))) {
                var12 = this.getTextHeight();
            } else if(vertical || !this.sizeType.equals("autoheight") && !this.sizeType.equals("autofont")) {
                var12 = 2147483647;
            } else {
                var12 = this.getTextWidth();
            }

            DocPageLine line = null;

            for(int i = 0; i < paras.size(); ++i) {
                ElePara para = (ElePara)paras.get(i);
                ArrayList list;
                int j;
                if(vertical) {
                    list = para.toLineList(top, var12, hrList, false);

                    for(j = 0; j < list.size(); ++j) {
                        line = (DocPageLine)list.get(j);
                        if(!this.sizeType.equals("autowidth") && p.y < line.width) {
                            p.y = line.width;
                        }

                        if(!this.sizeType.equals("autoheight")) {
                            p.x += line.height;
                        }
                    }
                } else {
                    list = para.toLineList(top, var12, hrList, true);

                    for(j = 0; j < list.size(); ++j) {
                        line = (DocPageLine)list.get(j);
                        if(!this.sizeType.equals("autoheight") && p.x < line.width) {
                            p.x = line.width;
                        }

                        if(!this.sizeType.equals("autowidth")) {
                            p.y += line.height;
                        }
                    }
                }
            }

            if(!this.sizeType.equals("autoheight")) {
                p.x += this.getTextTop() * 2 + 1;
            }

            if(!this.sizeType.equals("autowidth")) {
                p.y += this.getTextTop() * 2 + 1;
            }

            return p;
        }
    }

    protected ArrayList genLineList(ArrayList paras, int h) {
        int wColSplit = this.txtPadding() * 2;
        if(wColSplit <= 0) {
            wColSplit = 8;
        }

        ArrayList overParas = null;
        boolean ownPara = false;
        ElePara para;
        if(paras == null) {
            ownPara = true;
            paras = this.getParaList();
            ElePara.genHeadInx(paras);
            if(!this.hasPrintExp) {
                for(int list = 0; list < paras.size(); ++list) {
                    para = (ElePara)paras.get(list);

                    for(int line = 0; line < para.eleList.size(); ++line) {
                        if(para.eleList.get(line) instanceof EleText && ((EleText)para.eleList.get(line)).text.indexOf("#") >= 0) {
                            this.hasPrintExp = true;
                            break;
                        }
                    }
                }
            }
        }

        this.lineList = new ArrayList();
        DocPageLine var23 = null;
        boolean vertical = this.layoutFlow.equals("v");
        int top;
        if(vertical) {
            top = this.width - this.getTextTop();
        } else {
            top = this.getTextTop();
        }

        ArrayList hrList = this.hrList();
        if(this.column < 1) {
            this.column = 1;
        }

        boolean textWidth = false;
        int var24;
        if(vertical) {
            var24 = this.getTextHeight() / this.column - wColSplit * (this.column - 1);
        } else {
            var24 = this.getTextWidth() / this.column - wColSplit * (this.column - 1);
        }

        boolean stop = false;

        int topOffset;
        int i;
        int colCount;
        int var25;
        for(topOffset = 0; topOffset < paras.size(); ++topOffset) {
            para = (ElePara)paras.get(topOffset);
            ArrayList var22 = para.toLineList(top, var24, hrList, !vertical);

            for(i = 0; i < var22.size(); ++i) {
                var23 = (DocPageLine)var22.get(i);
                var23.index = this.lineList.size();
                var23.top = top;
                if(vertical) {
                    top -= var23.height;
                } else {
                    top += var23.height;
                }

                if(para instanceof EleParaLine) {
                    this.lineList.add(var23);
                } else {
                    if((topOffset > 0 || i > 0) && vertical && top < this.getTextLeft() - this.getTextWidth() * (this.column - 1) || (topOffset > 0 || i > 0) && !vertical && (top > this.height - this.getTextLeft() + this.getTextHeight() * (this.column - 1) || this.isTableWidthOver(var22, var23)) || para.breakPage && i == var22.size() - 1 && !this.name.equals("$_NO_BREAK_$")) {
                        if(ownPara) {
                            if(this.sizeType.equals("autotip") && (topOffset < paras.size() - 1 || i < var22.size() - 1) && this.lineList.size() > 0) {
                                var23 = (DocPageLine)this.lineList.get(this.lineList.size() - 1);
                                if(var23.eleList.size() > 0 && var23.eleList.get(var23.eleList.size() - 1) instanceof EleText) {
                                    EleText var28 = (EleText)var23.eleList.get(var23.eleList.size() - 1);
                                    if(var28.text.length() > 2) {
                                        var28 = new EleText(var28.xdoc, var28.getAttMap());
                                        var28.text = var28.text.substring(0, var28.text.length() - 2) + "...";
                                        var23.eleList.set(var23.eleList.size() - 1, var28);
                                    }
                                }

                                if(this.xdoc.print != null) {
                                    this.xdoc.print.toolTips.add(new NameShape(this.toString(), DocConst.g.getTransform().createTransformedShape(this.getFillShape())));
                                }
                            }
                        } else {
                            overParas = new ArrayList();
                            if(var22.size() == 1 && var23.eleList.size() == 1 && (var23.eleList.get(0) instanceof EleRect && ((EleRect)var23.eleList.get(0)).getLineList().size() > 1 || var23.eleList.get(0) instanceof EleTable && ((EleTable)var23.eleList.get(0)).rows.indexOf(",") > 0) && !para.breakPage) {
                                var25 = 0;
                                if(vertical && top < -this.getTextWidth() * (this.column - 1)) {
                                    var25 = var23.height + top - this.getTextWidth() * (this.column - 1);
                                } else if(!vertical && top > this.height + this.getTextHeight() * (this.column - 1)) {
                                    var25 = var23.height + this.height + this.getTextHeight() * (this.column - 1) - top;
                                }

                                if(this.isTableWidthOver(var22, var23)) {
                                    h = ((EleTable)var23.eleList.get(0)).height;
                                    var25 = h;
                                }

                                boolean var27 = false;
                                if(var25 <= 24 && h > 24) {
                                    var25 = h;
                                    var27 = true;
                                }

                                ArrayList i1 = split((EleRect)var23.eleList.get(0), var25, h, false);
                                HashMap atts = null;
                                if(i1.size() > 0) {
                                    atts = para.getAttMap();
                                }

                                ElePara rpara;
                                if(i1.size() != 1 && (i1.size() <= 0 || !var27)) {
                                    if(i1.size() > 1) {
                                        if(vertical) {
                                            top += var23.height;
                                        } else {
                                            top -= var23.height;
                                        }

                                        rpara = new ElePara(this.xdoc, atts);
                                        rpara.breakPage = true;
                                        rpara.eleList.add(i1.get(0));
                                        var23 = new DocPageLine(para, rpara.eleList, ((EleRect)i1.get(0)).height + rpara.lineSpacing, var24, vertical);
                                        var23.top = top;
                                        var23.align = para.align;
                                        var23.offset = ((DocPageLine)rpara.toLineList(top, this.width, hrList, !vertical).get(0)).offset;
                                        this.lineList.add(var23);
                                    }
                                } else {
                                    rpara = new ElePara(this.xdoc, atts);
                                    rpara.breakPage = true;
                                    rpara.eleList.add(i1.get(0));
                                    overParas.add(rpara);
                                }

                                for(int k = 1; k < i1.size(); ++k) {
                                    rpara = new ElePara(this.xdoc, atts);
                                    rpara.breakPage = true;
                                    rpara.eleList.add(i1.get(k));
                                    overParas.add(rpara);
                                }
                            } else if(i < var22.size()) {
                                if(para.breakPage && i == var22.size() - 1) {
                                    this.lineList.add(var23);
                                } else {
                                    ElePara hcount;
                                    if(var23.height > h) {
                                        this.lineList.add(var23);
                                        hcount = (ElePara)para.clone();

                                        for(colCount = i + 1; colCount < var22.size(); ++colCount) {
                                            var23 = (DocPageLine)var22.get(colCount);
                                            hcount.eleList.addAll(var23.eleList);
                                        }

                                        overParas.add(hcount);
                                    } else {
                                        hcount = (ElePara)para.clone();
                                        hcount.prefix = "";
                                        hcount.headInx = para.headInx;

                                        for(colCount = i; colCount < var22.size(); ++colCount) {
                                            var23 = (DocPageLine)var22.get(colCount);
                                            hcount.eleList.addAll(var23.eleList);
                                        }

                                        overParas.add(hcount);
                                    }
                                }
                            }

                            for(var25 = topOffset + 1; var25 < paras.size(); ++var25) {
                                overParas.add(paras.get(var25));
                            }
                        }

                        stop = true;
                        break;
                    }

                    this.lineList.add(var23);
                }
            }

            if(stop) {
                break;
            }
        }

        if(this.column > 1) {
            topOffset = 0;
            i = 0;
            var25 = 0;
            colCount = 0;

            for(int var26 = 0; var26 < this.lineList.size(); ++var26) {
                var23 = (DocPageLine)this.lineList.get(var26);
                if(colCount > 0 && var25 == 0) {
                    if(!vertical) {
                        topOffset = var23.top - this.getTextTop();
                    } else {
                        topOffset = var23.top - this.width + this.getTextTop();
                    }
                }

                var25 += var23.height;
                var23.top -= topOffset;
                var23.left += i;
                if(!vertical && var25 > this.height - this.txtPadding() * 2 || vertical && var25 > this.width - this.txtPadding() * 2) {
                    if(var25 != var23.height) {
                        --var26;
                        var23.top += topOffset;
                        var23.left -= i;
                    }

                    var25 = 0;
                    ++colCount;
                    i = var24 * colCount + wColSplit * colCount;
                    if(colCount >= this.column) {
                        break;
                    }
                }
            }
        }

        if(this.column == 1 && !this.align.equals("top")) {
            topOffset = 0;
            if(vertical && top > 0) {
                if(this.align.equals("center")) {
                    topOffset = top / 2;
                } else if(this.align.equals("bottom")) {
                    topOffset = top;
                }

                for(i = 0; i < this.lineList.size(); ++i) {
                    var23 = (DocPageLine)this.lineList.get(i);
                    if(vertical) {
                        var23.top -= topOffset;
                    } else {
                        var23.top += topOffset;
                    }
                }
            } else if(!vertical && top < this.height) {
                if(this.align.equals("center")) {
                    topOffset = (this.height - top) / 2 - this.getTextTop() / 2;
                } else if(this.align.equals("bottom")) {
                    topOffset = this.height - top - (int)this.strokeWidth;
                }

                for(i = 0; i < this.lineList.size(); ++i) {
                    var23 = (DocPageLine)this.lineList.get(i);
                    var23.top += topOffset;
                }
            }
        }

        return overParas;
    }

    private boolean isTableWidthOver(ArrayList list, DocPageLine line) {
        return this.xdoc.getMeta().getView().equals("table") && list.size() == 1 && line.eleList.size() == 1 && line.eleList.get(0) instanceof EleTable && ((EleTable)line.eleList.get(0)).cols.split(",").length > 1 && ((EleTable)line.eleList.get(0)).rows.split(",").length > 1 && ((EleTable)line.eleList.get(0)).width > this.getTextWidth() && ((EleTable)line.eleList.get(0)).height <= this.getTextHeight();
    }

    protected static void fixSize(EleRect rect, int w, int h) {
        if(rect.width > w || rect.height > h) {
            double scale;
            if((double)rect.width / (double)rect.height > (double)w / (double)h) {
                scale = (double)w / (double)rect.width;
            } else {
                scale = (double)h / (double)rect.height;
            }

            rect.width = (int)((double)rect.width * scale);
            rect.height = (int)((double)rect.height * scale);
            rect.sizeType = "normal";
        }

    }

    public void relayout() {
        this.lineList = null;
    }

    protected Shape getShape() {
        return this.getRectShape();
    }

    private Shape getRectShape() {
        return ShapeUtil.getRectShape(this.width, this.height, this.arc);
    }

    private Shape getFillShape() {
        return this.getFillShape(this.strokeWidth);
    }

    private Shape getFillShape(double sw) {
        Shape shape = this.getShape();
        AffineTransform af;
        if(this.srotate != 0) {
            af = new AffineTransform();
            Rectangle bound = shape.getBounds();
            af.rotate((double)this.srotate * 3.141592653589793D / 180.0D, bound.getCenterX(), bound.getCenterY());
            shape = af.createTransformedShape(shape);
            Rectangle2D bound1 = shape.getBounds2D();
            af = new AffineTransform();
            af.scale((double)this.width / bound1.getWidth(), (double)this.height / bound1.getHeight());
            shape = af.createTransformedShape(shape);
            bound1 = shape.getBounds2D();
            af = new AffineTransform();
            af.translate(-bound1.getX(), -bound1.getY());
            shape = af.createTransformedShape(shape);
        }

        if(this.color != null || this.fillImg != null && this.fillImg.length() > 0 || this.margin != 0) {
            if(sw > 1.0D || this.margin != 0) {
                af = new AffineTransform();
                af.scale(((double)this.width - sw - (double)(this.margin * 2)) / (double)this.width, ((double)this.height - sw - (double)(this.margin * 2)) / (double)this.height);
                shape = af.createTransformedShape(shape);
            }

            af = new AffineTransform();
            af.translate(sw / 2.0D + (double)this.margin, sw / 2.0D + (double)this.margin);
            shape = af.createTransformedShape(shape);
        }

        return shape;
    }

    public void setBounds(Rectangle rect) {
        this.left = rect.x;
        this.top = rect.y;
        this.width = rect.width;
        this.height = rect.height;
    }

    public static void concatText(EleRect rect) {
        EleText txt1 = null;

        for(int i = 0; i < rect.eleList.size(); ++i) {
            EleBase base = (EleBase)rect.eleList.get(i);
            if(base instanceof ElePara) {
                ElePara para = (ElePara)base;
                txt1 = null;

                for(int j = 0; j < para.eleList.size(); ++j) {
                    base = (EleBase)para.eleList.get(j);
                    if(base instanceof EleRect) {
                        txt1 = null;
                        concatText((EleRect)base);
                    } else if(base instanceof EleText) {
                        if(txt1 == null) {
                            txt1 = (EleText)base;
                        } else {
                            EleText txt2 = (EleText)base;
                            if(txt1.attEquals(txt2)) {
                                txt1.text = txt1.text + txt2.text;
                                para.eleList.remove(j);
                                --j;
                            } else {
                                txt1 = txt2;
                            }
                        }
                    }
                }
            } else if(base instanceof EleRect) {
                concatText((EleRect)base);
            }
        }

    }

    public void clearContent() {
        this.eleList.clear();
        this.lineList = null;
    }

    public boolean isVisible() {
        boolean visible = true;
        if(this.visible.length() > 0) {
            String vstr = this.visible;
            if(vstr.equalsIgnoreCase("false")) {
                visible = false;
            } else if(!vstr.equalsIgnoreCase("true")) {
                try {
                    BlkExpression e = new BlkExpression((BlkExpression)null);
                    e.varMap.put("PAGENO", new Long((long)this.xdoc.getViewPage()));
                    e.varMap.put("PAGECOUNT", new Long((long)this.xdoc.getViewPages()));
                    e.varMap.put("PAGE", new Long((long)this.xdoc.getViewPage()));
                    e.varMap.put("PAGES", new Long((long)this.xdoc.getViewPages()));
                    LogicExpression mathExp = new LogicExpression(e, Parser.pretreat(vstr)[0]);
                    visible = mathExp.isTrue((Conn)null);
                } catch (Exception var5) {
                    visible = true;
                }
            }
        }

        return visible;
    }

    public static BufferedImage toImg(EleRect shape) {
        return toImg(shape, false);
    }

    public static BufferedImage toImg(EleRect rect, boolean alpha) {
        BufferedImage bufImg;
        Graphics2D g;
        if(rect instanceof EleLine) {
            EleLine v = (EleLine)rect;
            Rectangle at = v.getBound();
            at.width = (int)((double)at.width + v.strokeWidth * 2.0D);
            at.height = (int)((double)at.height + v.strokeWidth * 2.0D);
            if(at.width > maxImgWidth) {
                at.width = maxImgWidth;
            }

            if(at.height > maxImgHeight) {
                at.height = maxImgHeight;
            }

            if(at.width == 0 || at.height == 0) {
                at = new Rectangle(0, 0, 1, 1);
            }

            bufImg = new BufferedImage(v.xdoc.intScale(at.width), v.xdoc.intScale(at.height), alpha?2:1);
            g = (Graphics2D)bufImg.getGraphics();
            ImgUtil.setRenderHint(g);
            if(!alpha) {
                g.setColor(Color.white);
                g.fillRect(0, 0, bufImg.getWidth(), bufImg.getHeight());
            }

            v.scalePrint(g, (int)v.strokeWidth, (int)v.strokeWidth);
            g.dispose();
        } else {
            rect.autoSize();
            if(rect.width > maxImgWidth) {
                rect.width = maxImgWidth;
            }

            if(rect.height > maxImgHeight) {
                rect.height = maxImgHeight;
            }

            if(rect.width <= 0) {
                rect.width = 1;
            }

            if(rect.height <= 0) {
                rect.height = 1;
            }

            if(rect.rotate == 0) {
                bufImg = new BufferedImage(rect.xdoc.intScale(rect.width), rect.xdoc.intScale(rect.height), alpha?2:1);
                g = (Graphics2D)bufImg.getGraphics();
                if(!alpha) {
                    g.setColor(Color.white);
                    g.fillRect(0, 0, bufImg.getWidth(), bufImg.getHeight());
                }
            } else {
                int v1 = (int) Math.ceil(Math.pow(Math.pow((double)rect.xdoc.intScale(rect.width), 2.0D) + Math.pow((double)rect.xdoc.intScale(rect.height), 2.0D), 0.5D));
                bufImg = new BufferedImage(v1, v1, alpha?2:1);
                g = (Graphics2D)bufImg.getGraphics();
                if(!alpha) {
                    g.setColor(Color.white);
                    g.fillRect(0, 0, bufImg.getWidth(), bufImg.getHeight());
                }

                AffineTransform at1 = AffineTransform.getRotateInstance(Math.toRadians((double)rect.rotate), (double)(v1 / 2), (double)(v1 / 2));
                g.transform(at1);
                g.translate((v1 - rect.width) / 2, (v1 - rect.height) / 2);
            }

            g.scale(rect.xdoc.scale, rect.xdoc.scale);
            ImgUtil.setImgRenderHint(g);
            String v2 = rect.visible;
            rect.visible = "true";

            try {
                rect.print(g);
            } finally {
                rect.visible = v2;
            }

            g.dispose();
        }

        return bufImg;
    }
}
