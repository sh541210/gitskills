/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.*;

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
/*     */ public class TextStroke
/*     */   implements Stroke
/*     */ {
/*     */   public String text;
/*     */   public Font font;
/*  32 */   public boolean stretchToFit = false;
/*  33 */   public boolean repeat = false;
/*  34 */   private AffineTransform t = new AffineTransform();
/*     */   private static final float FLATNESS = 1.0F;
/*     */   public float advance;
/*  37 */   public Shape strokeShape = null;
/*  38 */   public int shapeInx = -1;
/*     */ 
/*  40 */   public TextStroke(int shapeInx, float strokeWidth) { this.advance = strokeWidth;
/*  41 */     this.shapeInx = shapeInx; }
/*     */ 
/*     */   public TextStroke(Shape shape, float strokeWidth) {
/*  44 */     this.advance = strokeWidth;
/*  45 */     if (shape != null) {
/*  46 */       Rectangle2D bounds = shape.getBounds2D();
/*  47 */       double scale = strokeWidth / bounds.getHeight();
/*  48 */       this.advance = (float)(bounds.getWidth() * scale);
/*  49 */       if (this.advance <= 0.0F) {
/*  50 */         this.advance = 1.0F;
/*     */       }
/*  52 */       this.t.scale(scale, scale);
/*  53 */       this.t.translate(-bounds.getX(), -bounds.getY() - bounds.getHeight() / 2.0D);
/*  54 */       this.strokeShape = this.t.createTransformedShape(shape);
/*  55 */       this.t = new AffineTransform();
/*     */     }
/*     */   }
/*     */ 
/*     */   public TextStroke(String text, Font font, boolean stretchToFit, boolean repeat) {
/*  59 */     this.text = text;
/*  60 */     this.font = font;
/*  61 */     this.stretchToFit = stretchToFit;
/*  62 */     this.repeat = repeat;
/*     */   }
/*     */   public Shape createStrokedShape(Shape shape) {
/*  65 */     GeneralPath result = new GeneralPath();
/*  66 */     PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), 1.0D);
/*  67 */     float[] points = new float[6];
/*  68 */     float moveX = 0.0F; float moveY = 0.0F;
/*  69 */     float lastX = 0.0F; float lastY = 0.0F;
/*  70 */     float thisX = 0.0F; float thisY = 0.0F;
/*  71 */     int type = 0;
/*  72 */     float next = 0.0F;
/*  73 */     if (this.strokeShape == null) {
/*  74 */       FontRenderContext frc = new FontRenderContext(null, true, true);
/*  75 */       GlyphVector glyphVector = this.font.createGlyphVector(frc, this.text);
/*  76 */       int currentChar = 0;
/*  77 */       int length = glyphVector.getNumGlyphs();
/*  78 */       if (length == 0) return result;
/*  79 */       float factor = this.stretchToFit ? measurePathLength(shape) / (float)glyphVector.getLogicalBounds().getWidth() : 1.0F;
/*  80 */       float nextAdvance = 0.0F;
/*     */       do {
/*  82 */         type = it.currentSegment(points);
/*  83 */         switch (type) {
/*     */         case 0:
/*  85 */           moveX = lastX = points[0];
/*  86 */           moveY = lastY = points[1];
/*  87 */           result.moveTo(moveX, moveY);
/*  88 */           nextAdvance = glyphVector.getGlyphMetrics(currentChar).getAdvance() * 0.5F;
/*  89 */           next = nextAdvance + this.font.getSize2D() * factor / 4.0F;
/*  90 */           break;
/*     */         case 4:
/*  92 */           points[0] = moveX;
/*  93 */           points[1] = moveY;
/*     */         case 1:
/*  95 */           thisX = points[0];
/*  96 */           thisY = points[1];
/*  97 */           float dx = thisX - lastX;
/*  98 */           float dy = thisY - lastY;
/*  99 */           float distance = (float) Math.sqrt(dx * dx + dy * dy);
/* 100 */           if (distance >= next) {
/* 101 */             float r = 1.0F / distance;
/* 102 */             float angle = (float) Math.atan2(dy, dx);
/* 103 */             while ((currentChar < length) && (distance >= next)) {
/* 104 */               Shape glyph = glyphVector.getGlyphOutline(currentChar);
/* 105 */               Point2D p = glyphVector.getGlyphPosition(currentChar);
/* 106 */               float px = (float)p.getX();
/* 107 */               float py = (float)p.getY();
/* 108 */               float x = lastX + next * dx * r;
/* 109 */               float y = lastY + next * dy * r;
/* 110 */               float advance = nextAdvance;
/* 111 */               nextAdvance = currentChar < length - 1 ? glyphVector.getGlyphMetrics(currentChar + 1).getAdvance() * 0.5F : advance;
/* 112 */               this.t.setToTranslation(x, y);
/* 113 */               this.t.rotate(angle);
/* 114 */               this.t.translate(-px - advance, -py + this.font.getSize() * 0.5D);
/* 115 */               result.append(this.t.createTransformedShape(glyph), false);
/* 116 */               next += (advance + nextAdvance) * factor;
/* 117 */               currentChar++;
/* 118 */               if (!this.repeat) continue; currentChar %= length;
/*     */             }
/*     */           }
/* 121 */           next -= distance;
/* 122 */           lastX = thisX;
/* 123 */           lastY = thisY;
/*     */         case 2:
/*     */         case 3:
/* 126 */         }it.next();
/*     */ 
/*  81 */         if (currentChar >= length) break; 
/*  81 */       }while (!it.isDone());
/*     */     }
/*     */     else
/*     */     {
/*     */       do
/*     */       {
/* 130 */         type = it.currentSegment(points);
/* 131 */         switch (type) {
/*     */         case 0:
/* 133 */           moveX = lastX = points[0];
/* 134 */           moveY = lastY = points[1];
/* 135 */           result.moveTo(moveX, moveY);
/* 136 */           next = 0.0F;
/* 137 */           break;
/*     */         case 4:
/* 140 */           points[0] = moveX;
/* 141 */           points[1] = moveY;
/*     */         case 1:
/* 143 */           thisX = points[0];
/* 144 */           thisY = points[1];
/* 145 */           float dx = thisX - lastX;
/* 146 */           float dy = thisY - lastY;
/* 147 */           float distance = (float) Math.sqrt(dx * dx + dy * dy);
/* 148 */           if (distance >= next) {
/* 149 */             float r = 1.0F / distance;
/* 150 */             float angle = (float) Math.atan2(dy, dx);
/* 151 */             while (distance >= next) {
/* 152 */               float x = lastX + next * dx * r;
/* 153 */               float y = lastY + next * dy * r;
/* 154 */               this.t.setToTranslation(x, y);
/* 155 */               this.t.rotate(angle);
/* 156 */               result.append(this.t.createTransformedShape(this.strokeShape), false);
/* 157 */               next += this.advance;
/*     */             }
/*     */           }
/* 160 */           next -= distance;
/* 161 */           lastX = thisX;
/* 162 */           lastY = thisY;
/*     */         case 2:
/*     */         case 3:
/* 165 */         }it.next();
/*     */       }
/* 129 */       while (!it.isDone());
/*     */     }
/*     */ 
/* 168 */     return result;
/*     */   }
/*     */   public float measurePathLength(Shape shape) {
/* 171 */     PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), 1.0D);
/* 172 */     float[] points = new float[6];
/* 173 */     float moveX = 0.0F; float moveY = 0.0F;
/* 174 */     float lastX = 0.0F; float lastY = 0.0F;
/* 175 */     float thisX = 0.0F; float thisY = 0.0F;
/* 176 */     int type = 0;
/* 177 */     float total = 0.0F;
/* 178 */     while (!it.isDone()) {
/* 179 */       type = it.currentSegment(points);
/* 180 */       switch (type) {
/*     */       case 0:
/* 182 */         moveX = lastX = points[0];
/* 183 */         moveY = lastY = points[1];
/* 184 */         break;
/*     */       case 4:
/* 186 */         points[0] = moveX;
/* 187 */         points[1] = moveY;
/*     */       case 1:
/* 189 */         thisX = points[0];
/* 190 */         thisY = points[1];
/* 191 */         float dx = thisX - lastX;
/* 192 */         float dy = thisY - lastY;
/* 193 */         total += (float) Math.sqrt(dx * dx + dy * dy);
/* 194 */         lastX = thisX;
/* 195 */         lastY = thisY;
/*     */       case 2:
/*     */       case 3:
/* 198 */       }it.next();
/*     */     }
/* 200 */     return total;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.TextStroke
 * JD-Core Version:    0.6.0
 */