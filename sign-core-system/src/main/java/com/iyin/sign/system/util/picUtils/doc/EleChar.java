/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */


import com.iyin.sign.system.util.picUtils.util.MapUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
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
/*     */ public class EleChar extends EleText
/*     */ {
/*     */   public String strImg;
/* 142 */   private String strShape = "";
/* 143 */   private Shape shape = null;
/*     */   private BufferedImage img;
/*     */ 
/*     */   public EleChar(XDoc xdoc)
/*     */   {
/*  32 */     super(xdoc);
/*     */   }
/*     */   public EleChar(XDoc xdoc, HashMap attMap) {
/*  35 */     super(xdoc, attMap);
/*     */   }
/*     */   protected void init() {
/*  38 */     super.init();
/*  39 */     this.typeName = "char";
/*  40 */     this.strShape = "";
/*  41 */     this.strImg = "";
/*     */   }
/*     */   public boolean attEquals(EleText txt) {
/*  44 */     return false;
/*     */   }
/*     */   protected EleBase copyEle(XDoc xdoc) {
/*  47 */     return new EleChar(xdoc, getAttMap());
/*     */   }
/*     */   public Object clone() {
/*  50 */     return new EleChar(this.xdoc, getAttMap());
/*     */   }
/*     */   public HashMap getAttMap() {
/*  53 */     HashMap map = super.getAttMap();
/*  54 */     map.put("shape", this.strShape);
/*  55 */     map.put("img", this.strImg);
/*  56 */     return map;
/*     */   }
/*     */   protected void actDrawString(Graphics2D g, String str, int x, int y, boolean vertical) {
/*  59 */     int fontSize = g.getFont().getSize();
/*  60 */     int spacing = getSpacing(fontSize) / 2;
/*  61 */     if (this.shape != null) {
/*  62 */       g.fill(getShape(str, g.getFont(), x, y, vertical));
/*     */     }
/*  64 */     if (this.strImg.length() > 0) {
/*  65 */       if ((this.strImg.startsWith("<")) || (this.strImg.startsWith("{"))) {
/*  66 */         EleRect rect = DocUtil.getRect(this.xdoc, this.strImg);
/*  67 */         if (rect != null) {
/*  68 */           Graphics2D cg = (Graphics2D)g.create(x + spacing, (int)(y - fontSize * 0.8D),
/*  69 */             (int)(rect.width * (fontSize / rect.height)), fontSize);
/*  70 */           cg.scale(fontSize / rect.height, fontSize / rect.height);
/*  71 */           rect.print(cg);
/*  72 */           cg.dispose();
/*  73 */           return;
/*     */         }
/*     */       }
/*  76 */       BufferedImage bimg = getImg();
/*  77 */       if (bimg != null) {
/*  78 */         int w = (int)(bimg.getWidth() * (fontSize / bimg.getHeight()));
/*  79 */         g.drawImage(bimg, x + spacing, 
/*  80 */           (int)(y - fontSize * 0.8D), w, fontSize, null);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Shape getShape(String str, Font font, int x, int y, boolean vertical) {
/*  85 */     int fontSize = font.getSize();
/*  86 */     int spacing = getSpacing(font.getSize()) / 2;
/*  87 */     if (this.shape != null) {
/*  88 */       Shape sha = this.shape;
/*     */ 
/*  90 */       Rectangle2D bound = sha.getBounds2D();
/*  91 */       AffineTransform af = new AffineTransform();
/*  92 */       af.scale(fontSize / bound.getHeight() - fontSize * 0.02D / bound.getHeight(), fontSize * 0.9D / bound.getHeight());
/*  93 */       sha = af.createTransformedShape(sha);
/*     */ 
/*  95 */       bound = sha.getBounds2D();
/*  96 */       af = new AffineTransform();
/*  97 */       af.translate(-bound.getX() + x + fontSize * 0.02D + spacing, -bound.getY() + y - fontSize * 0.8D);
/*  98 */       sha = af.createTransformedShape(sha);
/*  99 */       return sha;
/* 100 */     }if (this.strImg.length() > 0) {
/* 101 */       BufferedImage bimg = getImg();
/* 102 */       if (bimg != null) {
/* 103 */         return new Rectangle(x + spacing, (int)(y - fontSize * 0.8D), (int)(bimg.getWidth() * (fontSize / bimg.getHeight())), fontSize);
/*     */       }
/*     */     }
/* 106 */     return new Rectangle(x + spacing, (int)(y - fontSize * 0.8D), fontSize, fontSize);
/*     */   }
/*     */   private BufferedImage getImg() {
/* 109 */     if ((this.img == null) && (this.strImg.length() > 0)) {
/* 110 */       this.img = ImgUtil.loadImg(this.xdoc, this.strImg, Color.black, Color.white, true);
/*     */     }
/* 112 */     return this.img;
/*     */   }
/*     */   public Rectangle2D getBounds() {
/* 115 */     return getBounds(this.fontSize);
/*     */   }
/*     */   private Rectangle2D getBounds(int fontSize) {
/* 118 */     int spacing = getSpacing(fontSize);
/* 119 */     if (this.shape != null) {
/* 120 */       Rectangle2D bounds = this.shape.getBounds2D();
/* 121 */       return new Rectangle(0, 0,
/* 122 */         (int)(fontSize * bounds.getWidth() / bounds.getHeight() + spacing), fontSize);
/* 123 */     }if (this.strImg.length() > 0) {
/* 124 */       BufferedImage bimg = getImg();
/* 125 */       if (bimg != null) {
/* 126 */         return new Rectangle(0, 0, (int)(bimg.getWidth() * (fontSize / bimg.getHeight()) + spacing), fontSize);
/*     */       }
/*     */     }
/* 129 */     return new Rectangle(0, 0, fontSize + spacing, fontSize);
/*     */   }
/*     */   public String viewText() {
/* 132 */     return "C";
/*     */   }
/*     */   public void setShape(String strShape) {
/* 135 */     this.strShape = strShape;
/* 136 */     if (strShape.length() > 0)
/* 137 */       this.shape = ShapeUtil.strToShape(this.strShape);
/*     */     else
/* 139 */       this.shape = null;
/*     */   }
/*     */ 
/*     */   public void setAttMap(HashMap map)
/*     */   {
/* 146 */     super.setAttMap(map);
/* 147 */     this.text = "C";
/* 148 */     if (map.containsKey("shape")) {
/* 149 */       setShape(MapUtil.getString(map, "shape", this.strShape));
/*     */     }
/* 151 */     this.strImg = MapUtil.getString(map, "img", this.strImg);
/* 152 */     if (map.containsKey("img"))
/* 153 */       this.img = null;
/*     */   }
/*     */ 
/*     */   public Rectangle2D getStrBounds(Font font, String str, boolean vertical) {
/* 157 */     Rectangle2D bounds = getBounds(font.getSize());
/* 158 */     bounds.setFrame(bounds.getX(), -bounds.getHeight() * 0.8D, 
/* 159 */       bounds.getWidth(), 
/* 160 */       bounds.getHeight());
/* 161 */     return bounds;
/*     */   }
/*     */   public String toString() {
/* 164 */     return this.text;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.EleChar
 * JD-Core Version:    0.6.0
 */