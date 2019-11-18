/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */

import com.iyin.sign.system.util.picUtils.util.MapUtil;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class EleLine extends EleShape
/*     */ {
/*     */   public int startX;
/*     */   public int startY;
/*     */   public int endX;
/*     */   public int endY;
/*     */   public static final String ARROW_TYPE_NONE = "";
/*     */   public static final String ARROW_TYPE_BIAS = "bias";
/*     */   public static final String ARROW_TYPE_TRIANGLE = "triangle";
/*     */   public static final String ARROW_TYPE_CIRCLE = "circle";
/*     */   public static final String ARROW_TYPE_RHOMBUS = "rhombus";
/*     */   public static final String ARROW_TYPE_RECT = "rect";
/*     */   public String startArrow;
/*     */   public String endArrow;
/* 113 */   public static String STYLE_LINE = "line";
/* 114 */   public static String STYLE_BROKEN = "broken";
/* 115 */   public static String STYLE_ARC = "arc";
/* 116 */   public static String STYLE_ARC2 = "arc2";
/*     */   public String lineStyle;
/*     */   private static final int nw_se = 0;
/*     */   private static final int se_nw = 1;
/*     */   private static final int sw_ne = 2;
/*     */   private static final int ne_sw = 3;
/* 128 */   public boolean printLine = true;
/*     */ 
/*     */   protected EleBase copyEle(XDoc xdoc)
/*     */   {
/*  38 */     return new EleLine(xdoc, getAttMap());
/*     */   }
/*     */   public EleLine(XDoc xdoc, HashMap attMap) {
/*  41 */     super(xdoc, attMap);
/*     */   }
/*     */   public EleLine(XDoc xdoc) {
/*  44 */     super(xdoc);
/*     */   }
/*     */   protected void init() {
/*  47 */     super.init();
/*  48 */     this.typeName = "line";
/*  49 */     this.startX = 0;
/*  50 */     this.startY = 0;
/*  51 */     this.endX = 20;
/*  52 */     this.endY = 20;
/*  53 */     this.startArrow = "";
/*  54 */     this.endArrow = "";
/*  55 */     this.lineStyle = STYLE_LINE;
/*     */   }
/*     */   public HashMap getAttMap() {
/*  58 */     HashMap map = super.getAttMap();
/*  59 */     map.put("startX", String.valueOf(this.startX));
/*  60 */     map.put("startY", String.valueOf(this.startY));
/*  61 */     map.put("endX", String.valueOf(this.endX));
/*  62 */     map.put("endY", String.valueOf(this.endY));
/*  63 */     map.put("startArrow", this.startArrow);
/*  64 */     map.put("endArrow", this.endArrow);
/*  65 */     map.put("lineStyle", this.lineStyle);
/*  66 */     map.remove("left");
/*  67 */     map.remove("top");
/*  68 */     map.remove("width");
/*  69 */     map.remove("height");
/*  70 */     return map;
/*     */   }
/*     */   protected void printSelf(Graphics2D g) {
/*  73 */     if (isLineShape())
/*  74 */       super.printSelf(g);
/*     */   }
/*     */ 
/*     */   public void setAttMap(HashMap map)
/*     */   {
/*  81 */     super.setAttMap(map);
/*  82 */     this.startX = MapUtil.getInt(map, "startX", this.startX);
/*  83 */     this.startY = MapUtil.getInt(map, "startY", this.startY);
/*  84 */     this.endX = MapUtil.getInt(map, "endX", this.endX);
/*  85 */     this.endY = MapUtil.getInt(map, "endY", this.endY);
/*  86 */     Rectangle rect = getViewBounds(this);
/*  87 */     this.width = rect.width;
/*  88 */     this.height = rect.height;
/*  89 */     this.left = rect.x;
/*  90 */     this.top = rect.y;
/*  91 */     this.startArrow = MapUtil.getString(map, "startArrow", this.startArrow);
/*  92 */     this.endArrow = MapUtil.getString(map, "endArrow", this.endArrow);
/*  93 */     this.lineStyle = MapUtil.getString(map, "lineStyle", this.lineStyle);
/*     */   }
/*     */   public Object clone() {
/*  96 */     return new EleLine(this.xdoc, getAttMap());
/*     */   }
/*     */   public static Rectangle getViewBounds(EleLine line) {
/*  99 */     return new Rectangle(Math.min(line.startX, line.endX), Math.min(line.startY, line.endY),
/* 100 */       Math.abs(line.startX - line.endX), Math.abs(line.startY - line.endY));
/*     */   }
/*     */   protected Rectangle getBound() {
/* 103 */     return getViewBounds(this);
/*     */   }
/*     */ 
/*     */   public void scalePrint(Graphics2D g, int offX, int offY)
/*     */   {
/* 119 */     Graphics2D g2 = (Graphics2D)g.create();
/* 120 */     g2.scale(this.xdoc.scale, this.xdoc.scale);
/* 121 */     print(g2, this.xdoc.unintScale(offX), this.xdoc.unintScale(offY));
/* 122 */     g2.dispose();
/*     */   }
/*     */ 
/*     */   protected void actPrint(Graphics2D g)
/*     */   {
/* 130 */     super.actPrint(g);
/* 131 */     if (this.printLine)
/* 132 */       print(g, 0, 0);
/*     */   }
/*     */ 
/*     */   public void print(Graphics2D g, int offX, int offY) {
/* 136 */     if (this.lineStyle.length() == 0) {
/* 137 */       this.lineStyle = STYLE_LINE;
/*     */     }
/* 139 */     if ((this.color != null) && (isVisible())) {
/* 140 */       Rectangle rect = getViewBounds(this);
/* 141 */       GeneralPath path = new GeneralPath();
/* 142 */       int direction = 0;
/* 143 */       if ((this.startX <= this.endX) && (this.startY <= this.endY))
/* 144 */         direction = 0;
/* 145 */       else if ((this.startX >= this.endX) && (this.startY >= this.endY))
/* 146 */         direction = 1;
/* 147 */       else if ((this.startX <= this.endX) && (this.startY >= this.endY))
/* 148 */         direction = 2;
/* 149 */       else if ((this.startX >= this.endX) && (this.startY <= this.endY)) {
/* 150 */         direction = 3;
/*     */       }
/*     */ 
/* 154 */       if ((direction == 0) || (direction == 1)) {
/* 155 */         path.moveTo(0.0F, 0.0F);
/* 156 */         if (this.lineStyle.equals(STYLE_ARC)) {
/* 157 */           if (direction == 0)
/* 158 */             path.quadTo(0.0F, rect.height, rect.width, rect.height);
/*     */           else
/* 160 */             path.quadTo(rect.width, 0.0F, rect.width, rect.height);
/*     */         }
/* 162 */         else if (this.lineStyle.equals(STYLE_ARC2)) {
/* 163 */           path.curveTo(rect.width / 2, 0.0F, rect.width / 2, rect.height, rect.width, rect.height);
/* 164 */         } else if (this.lineStyle.equals(STYLE_BROKEN)) {
/* 165 */           path.lineTo(rect.width / 2, 0.0F);
/* 166 */           path.lineTo(rect.width / 2, rect.height);
/* 167 */           path.lineTo(rect.width, rect.height);
/*     */         } else {
/* 169 */           path.lineTo(rect.width, rect.height);
/*     */         }
/*     */       } else {
/* 172 */         path.moveTo(0.0F, rect.height);
/* 173 */         if (this.lineStyle.equals(STYLE_ARC)) {
/* 174 */           if (direction == 2)
/* 175 */             path.quadTo(0.0F, 0.0F, rect.width, 0.0F);
/*     */           else
/* 177 */             path.quadTo(rect.width, rect.height, rect.width, 0.0F);
/*     */         }
/* 179 */         else if (this.lineStyle.equals(STYLE_ARC2)) {
/* 180 */           path.curveTo(rect.width / 2, rect.height, rect.width / 2, 0.0F, rect.width, 0.0F);
/* 181 */         } else if (this.lineStyle.equals(STYLE_BROKEN)) {
/* 182 */           path.lineTo(rect.width / 2, rect.height);
/* 183 */           path.lineTo(rect.width / 2, 0.0F);
/* 184 */           path.lineTo(rect.width, 0.0F);
/*     */         } else {
/* 186 */           path.lineTo(rect.width, 0.0F);
/*     */         }
/*     */       }
/* 189 */       AffineTransform af = new AffineTransform();
/* 190 */       af.translate(offX, offY);
/* 191 */       Shape shape = path;
/* 192 */       shape = af.createTransformedShape(shape);
/* 193 */       if (this.strokeWidth > 0.0D) {
/* 194 */         if ((this.strokeWidth >= 1.0D) && (this.strokeImg != null) && (this.strokeImg.length() > 0)) {
/* 195 */           BufferedImage imgStroke = ImgUtil.loadImg(this.xdoc, this.strokeImg, this.color, null, false);
/* 196 */           if (imgStroke != null) {
/* 197 */             imgStroke = ImgUtil.alpha(imgStroke, Color.WHITE);
/* 198 */             drawImageShape(g, imgStroke, shape, (int)this.strokeWidth);
/* 199 */             return;
/*     */           }
/*     */         }
/* 202 */         setStroke(g);
/* 203 */         g.draw(shape);
/* 204 */         PaintUtil.resetStroke(g);
/*     */       }
/*     */ 
/* 207 */       String tmpStartArrow = this.startArrow;
/* 208 */       String tmpEndArrow = this.endArrow;
/* 209 */       if ((direction == 1) || (direction == 3)) {
/* 210 */         tmpStartArrow = this.endArrow;
/* 211 */         tmpEndArrow = this.startArrow;
/*     */       }
/* 213 */       if (!tmpStartArrow.equals("")) {
/* 214 */         double rotate = 0.0D;
/* 215 */         if (this.lineStyle.equals(STYLE_ARC)) {
/* 216 */           if (((direction == 0) && (this.startY != this.endY)) || ((direction == 1) && (this.startX == this.endX)))
/* 217 */             rotate = -1.570796326794897D;
/* 218 */           else if ((direction == 2) && (this.startX != this.endX)) {
/* 219 */             rotate = 1.570796326794897D;
/*     */           }
/*     */         }
/* 222 */         shape = getSAShape(tmpStartArrow, direction, rotate);
/* 223 */         if (shape != null) {
/* 224 */           shape = af.createTransformedShape(shape);
/* 225 */           g.draw(shape);
/* 226 */           g.fill(shape);
/*     */         }
/*     */       }
/* 229 */       if ((!tmpEndArrow.equals("")) && (!tmpEndArrow.equals("0"))) {
/* 230 */         double rotate = 0.0D;
/* 231 */         if (this.lineStyle.equals(STYLE_ARC)) {
/* 232 */           if ((direction == 3) && (this.startX != this.endX))
/* 233 */             rotate = -1.570796326794897D;
/* 234 */           else if (((direction == 1) && (this.startY != this.endY)) || ((direction == 0) && (this.startX == this.endX))) {
/* 235 */             rotate = 1.570796326794897D;
/*     */           }
/*     */         }
/* 238 */         shape = getEAShape(tmpEndArrow, direction, rotate);
/* 239 */         if (shape != null) {
/* 240 */           shape = af.createTransformedShape(shape);
/* 241 */           g.draw(shape);
/* 242 */           g.fill(shape);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Shape baseArrow(String arrowType, double weight) {
/* 248 */     Shape shape = null;
/* 249 */     int len = (int) Math.round(8.0D * weight / 2.0D);
/* 250 */     if (len < 8) len = 8;
/* 251 */     if ((arrowType.equals("bias")) || (arrowType.equals("1"))) {
/* 252 */       GeneralPath path = new GeneralPath();
/* 253 */       path.append(new Line2D.Double(0.0D, 0.0D, len * Math.cos(0.5235987755982988D), len * Math.sin(0.5235987755982988D)), true);
/* 254 */       path.append(new Line2D.Double(0.0D, 0.0D, len * Math.cos(-0.5235987755982988D), len * Math.sin(-0.5235987755982988D)), true);
/* 255 */       path.closePath();
/* 256 */       shape = path;
/* 257 */     } else if ((arrowType.equals("triangle")) || (arrowType.equals("2"))) {
/* 258 */       shape = new Polygon(new int[] { 0, (int)(len * Math.cos(0.5235987755982988D)), (int)(len * Math.cos(-0.5235987755982988D)) },
/* 259 */         new int[] { 0, (int)(len * Math.sin(0.5235987755982988D)), (int)(len * Math.sin(-0.5235987755982988D)) }, 3);
/* 260 */     } else if ((arrowType.equals("circle")) || (arrowType.equals("3"))) {
/* 261 */       shape = new Ellipse2D.Double(-len / 4, -len / 4, len, len);
/* 262 */       AffineTransform af = new AffineTransform();
/* 263 */       af.rotate(-0.7853981633974483D, 0.0D, 0.0D);
/* 264 */       shape = af.createTransformedShape(shape);
/* 265 */     } else if ((arrowType.equals("rhombus")) || (arrowType.equals("4"))) {
/* 266 */       shape = new Rectangle2D.Double(0.0D, 0.0D, len, len);
/* 267 */       AffineTransform af = new AffineTransform();
/* 268 */       af.rotate(-0.7853981633974483D, 0.0D, 0.0D);
/* 269 */       shape = af.createTransformedShape(shape);
/* 270 */     } else if ((arrowType.equals("rect")) || (arrowType.equals("5"))) {
/* 271 */       shape = new Rectangle(0, -len / 2, len, len);
/*     */     }
/* 273 */     return shape;
/*     */   }
/*     */   private Shape getSAShape(String type, int direction, double rotate2) {
/* 276 */     Shape shape = baseArrow(type, this.strokeWidth);
/* 277 */     if (shape != null) {
/* 278 */       Rectangle rect = getViewBounds(this);
/* 279 */       boolean b = false;
/* 280 */       if ((direction == 0) || (direction == 1)) {
/* 281 */         b = true;
/*     */       }
/* 283 */       if (shape != null) {
/* 284 */         AffineTransform af = new AffineTransform();
/* 285 */         if (this.lineStyle.equals(STYLE_LINE)) {
/* 286 */           double rotate = Math.atan(rect.height / rect.width);
/* 287 */           if (b)
/* 288 */             af.rotate(rotate, 0.0D, 0.0D);
/*     */           else {
/* 290 */             af.rotate(-rotate, 0.0D, 0.0D);
/*     */           }
/* 292 */           shape = af.createTransformedShape(shape);
/*     */         }
/* 294 */         if (rotate2 != 0.0D) {
/* 295 */           af = new AffineTransform();
/* 296 */           af.rotate(-rotate2, 0.0D, 0.0D);
/* 297 */           shape = af.createTransformedShape(shape);
/*     */         }
/* 299 */         if (!b) {
/* 300 */           af = new AffineTransform();
/* 301 */           af.translate(0.0D, rect.height);
/* 302 */           shape = af.createTransformedShape(shape);
/*     */         }
/*     */       }
/*     */     }
/* 306 */     return shape;
/*     */   }
/*     */   private Shape getEAShape(String type, int direction, double rotate2) {
/* 309 */     Shape shape = baseArrow(type, this.strokeWidth);
/* 310 */     if (shape != null) {
/* 311 */       Rectangle rect = getViewBounds(this);
/* 312 */       boolean b = false;
/* 313 */       if ((direction == 0) || (direction == 1)) {
/* 314 */         b = true;
/*     */       }
/* 316 */       if (shape != null) {
/* 317 */         AffineTransform af = new AffineTransform();
/* 318 */         double rotate = 0.0D;
/* 319 */         if (this.lineStyle.equals(STYLE_LINE)) {
/* 320 */           rotate = Math.atan(rect.height / rect.width);
/*     */         }
/* 322 */         if (b)
/* 323 */           af.rotate(3.141592653589793D + rotate + rotate2, 0.0D, 0.0D);
/*     */         else {
/* 325 */           af.rotate(3.141592653589793D - rotate + rotate2, 0.0D, 0.0D);
/*     */         }
/* 327 */         shape = af.createTransformedShape(shape);
/* 328 */         af = new AffineTransform();
/* 329 */         if (b)
/* 330 */           af.translate(rect.width, rect.height);
/*     */         else {
/* 332 */           af.translate(rect.width, 0.0D);
/*     */         }
/* 334 */         shape = af.createTransformedShape(shape);
/*     */       }
/*     */     }
/* 337 */     return shape;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.EleLine
 * JD-Core Version:    0.6.0
 */