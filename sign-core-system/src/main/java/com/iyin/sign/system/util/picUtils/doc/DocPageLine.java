/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */




import com.iyin.sign.system.util.picUtils.util.StrUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class DocPageLine
/*     */ {
/*  27 */   public int index = 0;
/*  28 */   public int top = 0;
/*  29 */   public int left = 0;
/*  30 */   public int height = 20;
/*  31 */   public int width = 0;
/*  32 */   public boolean enter = false;
/*  33 */   public boolean vertical = false;
/*  34 */   public String align = "left";
/*  35 */   public ArrayList eleList = new ArrayList();
/*     */   public ElePara para;
/*  47 */   public int offset = 0;
/*     */ 
/*     */   public DocPageLine(ElePara para, ArrayList eleList, int height, int width, boolean vertical)
/*     */   {
/*  38 */     this.para = para;
/*  39 */     this.eleList = eleList;
/*  40 */     this.height = height;
/*  41 */     this.width = width;
/*  42 */     this.vertical = vertical;
/*     */   }
/*     */   public DocPageLine(ElePara para, ArrayList eleList, int height, int width) {
/*  45 */     this(para, eleList, height, width, false);
/*     */   }
/*     */ 
/*     */   public void print(Graphics2D g, int xStart) {
/*  49 */     print(g, xStart, this.top);
/*     */   }
/*     */   public String toString() {
/*  52 */     StringBuffer sb = new StringBuffer();
/*     */ 
/*  54 */     for (int i = 0; i < this.eleList.size(); i++) {
/*  55 */       Object ele = this.eleList.get(i);
/*  56 */       if ((ele instanceof EleText)) {
/*  57 */         sb.append(((EleText)ele).text);
/*     */       }
/*     */     }
/*  60 */     return sb.toString().trim();
/*     */   }
/*     */ 
/*     */   private void print(Graphics2D g, int xStart, int yStart)
/*     */   {
/*  76 */     if ((this.para.backColor != null) || (this.para.backImg.length() > 0)) {
/*  77 */       if (this.vertical)
/*  78 */         PaintUtil.fill(g, this.para.xdoc, 
/*  79 */           new Rectangle(yStart - this.height, xStart + this.para.indentLeft, this.height, this.para.layWidth),
/*  80 */           this.para.backColor, this.para.backImg);
/*     */       else {
/*  82 */         PaintUtil.fill(g, this.para.xdoc, 
/*  83 */           new Rectangle(this.para.indentLeft + xStart, yStart, this.para.layWidth, this.height),
/*  84 */           this.para.backColor, this.para.backImg);
/*     */       }
/*     */     }
/*  87 */     if (this.eleList.size() == 0) return;
/*  88 */     if (this.para.heading > 0) {
/*  89 */       ArrayList heads = this.para.xdoc.heads;
/*  90 */       boolean b = false;
/*  91 */       if ((heads.size() > 0) && 
/*  92 */         (this.para == ((Heading)heads.get(heads.size() - 1)).para)) {
/*  93 */         b = true;
/*     */       }
/*     */ 
/*  96 */       if (!b) {
/*  97 */         this.para.xdoc.heading = toString();
/*  98 */         Heading heading = new Heading(this.para);
/*  99 */         heading.page = (this.para.xdoc.page - 1);
/* 100 */         heading.x = ((int)g.getTransform().getTranslateX() + xStart);
/* 101 */         heading.y = ((int)g.getTransform().getTranslateY() + yStart);
/* 102 */         this.para.xdoc.heads.add(heading);
/*     */       }
/*     */     }
/* 105 */     if (this.vertical)
/* 106 */       printV(g, yStart, xStart);
/*     */     else
/* 108 */       printH(g, xStart, yStart);
/*     */   }
/*     */ 
/*     */   private void printV(Graphics2D g, int xStart, int yStart)
/*     */   {
/* 113 */     int y = this.offset + yStart + this.left;
/*     */ 
/* 117 */     int adjustHeight = 0;
/* 118 */     for (int i = 0; i < this.eleList.size(); i++) {
/* 119 */       Object ele = this.eleList.get(i);
/* 120 */       if ((ele instanceof EleCharRect)) {
/* 121 */         ele = ((EleCharRect)ele).eleChar;
/*     */       }
/* 123 */       if ((ele instanceof EleText)) {
/* 124 */         y = (int)(y + ((EleText)ele).printV(g, xStart - this.height, y, this.height, this.para.heading == 0 ? this.para.lineSpacing : 0));
/* 125 */       } else if ((ele instanceof EleRect)) {
/* 126 */         EleRect rect = (EleRect)ele;
/* 127 */         Point viewSize = rect.viewSize();
/* 128 */         if (rect.valign != null) {
/* 129 */           if (rect.valign.equals("top"))
/* 130 */             adjustHeight = this.height - viewSize.x - this.para.lineSpacing;
/* 131 */           else if (rect.valign.equals("center"))
/* 132 */             adjustHeight = (this.height - viewSize.x - this.para.lineSpacing) / 2;
/*     */           else {
/* 134 */             adjustHeight = 0;
/*     */           }
/*     */         }
/* 137 */         if (rect.rotate == 0) {
/* 138 */           if ((rect.valign.equals("around")) || 
/* 139 */             (rect.valign.equals("float"))) {
/* 140 */             Graphics2D cg = (Graphics2D)g.create(xStart + this.para.lineSpacing, y,
/* 141 */               rect.width + 1, rect.height + 1);
/* 142 */             rect.print(cg);
/* 143 */             cg.dispose();
/*     */           } else {
/* 145 */             Graphics2D cg = (Graphics2D)g.create(xStart - this.height + adjustHeight, y,
/* 146 */               rect.width + 1, rect.height + 1);
/* 147 */             rect.print(cg);
/* 148 */             cg.dispose();
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*     */           Point s;
/*     */
/* 152 */           if ((rect.valign.equals("around")) || (rect.valign.equals("float")))
/* 153 */             s = new Point(xStart + this.para.lineSpacing, y);
/*     */           else {
/* 155 */             s = new Point(xStart - viewSize.x + adjustHeight, y);
/*     */           }
/* 157 */           int d = (int) Math.ceil(Math.pow(Math.pow(rect.width, 2.0D) + Math.pow(rect.height, 2.0D), 0.5D));
/* 158 */           Graphics2D cg = (Graphics2D)g.create(s.x - (d - viewSize.x) / 2,
/* 159 */             s.y - (d - viewSize.y) / 2, 
/* 160 */             d, 
/* 161 */             d);
/* 162 */           AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rect.rotate), d / 2, d / 2);
/* 163 */           cg.transform(at);
/* 164 */           cg.translate((d - rect.width) / 2, (d - rect.height) / 2);
/* 165 */           rect.print(cg);
/* 166 */           cg.dispose();
/*     */         }
/* 168 */         if ((ele instanceof EleLine)) {
/* 169 */           EleLine line = (EleLine)ele;
/* 170 */           Rectangle rectBounds = EleLine.getViewBounds(line);
/* 171 */           if (line.valign != null) {
/* 172 */             if (line.valign.equals("top"))
/* 173 */               adjustHeight = this.height - rectBounds.height - this.para.lineSpacing;
/* 174 */             else if (line.valign.equals("center"))
/* 175 */               adjustHeight = (this.height - rectBounds.height - this.para.lineSpacing) / 2;
/*     */             else {
/* 177 */               adjustHeight = 0;
/*     */             }
/*     */           }
/* 180 */           line.print(g, xStart - rectBounds.width + adjustHeight, y);
/*     */         }
/* 182 */         y += viewSize.y;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printH(Graphics2D g, int xStart, int yStart) {
/* 188 */     int x = this.offset + xStart + this.left;
/*     */ 
/* 192 */     int adjustHeight = 0;
/* 193 */     for (int i = 0; i < this.eleList.size(); i++) {
/* 194 */       Object ele = this.eleList.get(i);
/* 195 */       if ((ele instanceof EleCharRect)) {
/* 196 */         ele = ((EleCharRect)ele).eleChar;
/*     */       }
/* 198 */       if ((ele instanceof EleText)) {
/* 199 */         if (((EleText)ele).valign.equals("around"))
/* 200 */           ((EleText)ele).print(g, x, yStart + (int)((EleText)ele).getBounds().getHeight() - this.height + this.para.lineSpacing, this.height, this.para.lineSpacing);
/*     */         else {
/* 202 */           ((EleText)ele).print(g, x, yStart, this.height, (this.para.heading == 0) && (this.eleList.size() > 1) ? this.para.lineSpacing : 0);
/*     */         }
/* 204 */         x = (int)(x + ((EleText)ele).getBounds().getWidth());
/* 205 */       } else if ((ele instanceof EleRect)) {
/* 206 */         if ((ele instanceof EleLine)) {
/* 207 */           ((EleLine)ele).printLine = false;
/*     */         }
/* 209 */         EleRect rect = (EleRect)ele;
/* 210 */         Point viewSize = rect.viewSize();
/* 211 */         if (rect.valign != null) {
/* 212 */           if (rect.valign.equals("top"))
/* 213 */             adjustHeight = this.height - viewSize.y;
/* 214 */           else if (rect.valign.equals("center"))
/* 215 */             adjustHeight = (this.height - viewSize.y) / 2;
/*     */           else {
/* 217 */             adjustHeight = 0;
/*     */           }
/*     */         }
/* 220 */         if (rect.rotate == 0) {
/* 221 */           if (rect.valign.equals("around")) {
/* 222 */             Graphics2D cg = (Graphics2D)g.create(x, yStart + this.para.lineSpacing,
/* 223 */               rect.width + 1, rect.height + 1);
/* 224 */             rect.print(cg);
/* 225 */             cg.dispose();
/* 226 */           } else if (rect.valign.equals("float")) {
/* 227 */             Graphics2D cg = (Graphics2D)g.create(x, yStart + this.para.lineSpacing - rect.height,
/* 228 */               rect.width + 1, rect.height + 1);
/* 229 */             rect.print(cg);
/* 230 */             cg.dispose();
/*     */           } else {
/* 232 */             Graphics2D cg = (Graphics2D)g.create(x, yStart + this.height - rect.height - adjustHeight,
/* 233 */               rect.width + 1, rect.height + 1);
/* 234 */             rect.print(cg);
/* 235 */             cg.dispose();
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*     */           Point s;
/*     */
/* 239 */           if (rect.valign.equals("around")) {
/* 240 */             s = new Point(x, yStart + this.para.lineSpacing);
/*     */           }
/*     */           else
/*     */           {
/*     */
/* 241 */             if (rect.valign.equals("float"))
/* 242 */               s = new Point(x, yStart + this.para.lineSpacing - rect.height);
/*     */             else
/* 244 */               s = new Point(x, yStart + this.height - viewSize.y - adjustHeight);
/*     */           }
/* 246 */           int d = (int) Math.ceil(Math.pow(Math.pow(rect.width, 2.0D) + Math.pow(rect.height, 2.0D), 0.5D));
/* 247 */           Graphics2D cg = (Graphics2D)g.create(s.x - (d - viewSize.x) / 2,
/* 248 */             s.y - (d - viewSize.y) / 2, 
/* 249 */             d, 
/* 250 */             d);
/* 251 */           AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rect.rotate), d / 2, d / 2);
/* 252 */           cg.transform(at);
/* 253 */           cg.translate((d - rect.width) / 2, (d - rect.height) / 2);
/* 254 */           rect.print(cg);
/* 255 */           cg.dispose();
/*     */         }
/* 257 */         if ((ele instanceof EleLine)) {
/* 258 */           EleLine line = (EleLine)ele;
/* 259 */           Rectangle rectBounds = EleLine.getViewBounds(line);
/* 260 */           if (line.valign != null) {
/* 261 */             if (line.valign.equals("top"))
/* 262 */               adjustHeight = this.height - rectBounds.height;
/* 263 */             else if (line.valign.equals("center"))
/* 264 */               adjustHeight = (this.height - rectBounds.height) / 2;
/*     */             else {
/* 266 */               adjustHeight = 0;
/*     */             }
/*     */           }
/* 269 */           line.print(g, x, yStart + this.height - rectBounds.height - adjustHeight - (int)line.strokeWidth / 2);
/*     */         }
/* 271 */         x += viewSize.x;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void distribute(int width, int from) {
/* 276 */     distribute(width, from, false);
/*     */   }
/*     */   private void distribute(int width, int from, boolean both) {
/* 279 */     if (this.eleList.size() > from) {
/* 280 */       if (!this.vertical)
/*     */       {
/* 282 */         ArrayList tarList = new ArrayList();
/* 283 */         if (from > 0) {
/* 284 */           for (int i = 0; i < from; i++) {
/* 285 */             tarList.add(this.eleList.get(i));
/*     */           }
/*     */         }
/* 288 */         for (int i = from; i < this.eleList.size(); i++) {
/* 289 */           if ((this.eleList.get(i) instanceof EleText)) {
/* 290 */             EleText txt = (EleText)this.eleList.get(i);
/* 291 */             if (txt.canSplit())
/* 292 */               distributeSplit(tarList, txt, both);
/*     */             else
/* 294 */               tarList.add(txt);
/*     */           }
/*     */           else {
/* 297 */             tarList.add(this.eleList.get(i));
/*     */           }
/*     */         }
/* 300 */         this.eleList = tarList;
/* 301 */         if ((this.eleList.size() == from + 1) && ((this.eleList.get(from) instanceof EleText))) {
/* 302 */           EleText txt = (EleText)this.eleList.get(from);
/* 303 */           if (txt.canSplit())
/*     */           {
/* 305 */             this.eleList.remove(from);
/* 306 */             distributeSplit(this.eleList, txt, false);
/*     */           }
/*     */         }
/* 309 */         double space = (width - this.width) / (this.eleList.size() - from - 1);
/* 310 */         if (space > this.height)
/*     */         {
/* 312 */           tarList = new ArrayList();
/* 313 */           if (from > 0) {
/* 314 */             for (int i = 0; i < from; i++) {
/* 315 */               tarList.add(this.eleList.get(i));
/*     */             }
/*     */           }
/* 318 */           for (int i = from; i < this.eleList.size(); i++) {
/* 319 */             if ((this.eleList.get(i) instanceof EleText)) {
/* 320 */               EleText txt = (EleText)this.eleList.get(i);
/* 321 */               if (txt.canSplit()) {
/* 322 */                 int pos = 0;
/* 323 */                 for (int j = 0; j < txt.text.length(); j++) {
/* 324 */                   if (txt.text.charAt(j) > 'ÿ') {
/* 325 */                     if (pos < j) {
/* 326 */                       tarList.add(new EleText(txt, txt.text.substring(pos, j)));
/*     */                     }
/* 328 */                     tarList.add(new EleText(txt, String.valueOf(txt.text.charAt(j))));
/* 329 */                     pos = j + 1;
/*     */                   }
/*     */                 }
/* 332 */                 if (pos < txt.text.length())
/* 333 */                   tarList.add(new EleText(txt, txt.text.substring(pos)));
/*     */               }
/*     */               else {
/* 336 */                 tarList.add(txt);
/*     */               }
/*     */             } else {
/* 339 */               tarList.add(this.eleList.get(i));
/*     */             }
/*     */           }
/* 342 */           this.eleList = tarList;
/*     */         }
/*     */       }
/* 345 */       if (this.eleList.size() > from + 1) {
/* 346 */         double space = (width - this.width) / (this.eleList.size() - from - 1);
/* 347 */         if (space > 0.0D) {
/* 348 */           EleSpace box = null;
/* 349 */           double count = 0.0D;
/* 350 */           for (int i = from; i < this.eleList.size() - 1; i += 2) {
/* 351 */             count += space - (int)space;
/* 352 */             if (((int)space > 0) || (count >= 1.0D)) {
/* 353 */               box = new EleSpace(this.para.xdoc, (int)space, 1);
/* 354 */               if (count >= 1.0D) {
/* 355 */                 box.width += (int)count;
/* 356 */                 count -= (int)count;
/*     */               }
/* 358 */               if (this.vertical) {
/* 359 */                 box.setSize(box.height, box.width);
/* 360 */                 if (box.height > 0)
/* 361 */                   this.eleList.add(i + 1, box);
/*     */               }
/*     */               else {
/* 364 */                 this.eleList.add(i + 1, box);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bothDistribute(int width, int from) {
/* 373 */     distribute(width, from, true);
/*     */   }
/*     */   public void calWidth() {
/* 376 */     int lineWidth = 0;
/*     */ 
/* 378 */     if (this.vertical)
/*     */     {
/* 381 */       for (int i = 0; i < this.eleList.size(); i++) {
/* 382 */         Object ele = this.eleList.get(i);
/* 383 */         if ((ele instanceof EleCharRect)) {
/* 384 */           ele = ((EleCharRect)ele).eleChar;
/*     */         }
/* 386 */         if ((ele instanceof EleText)) {
/* 387 */           EleText text = (EleText)ele;
/* 388 */           if (text.text.length() > 0) {
/* 389 */             Rectangle2D rectBounds = text.getBounds();
/* 390 */             if (EleText.isVRotate(text.text.charAt(0)))
/* 391 */               lineWidth = (int)(lineWidth + rectBounds.getWidth());
/*     */             else
/* 393 */               lineWidth = (int)(lineWidth + rectBounds.getHeight());
/*     */           }
/*     */         }
/* 396 */         else if ((ele instanceof EleRect)) {
/* 397 */           lineWidth += ((EleRect)ele).viewSize().y;
/*     */         }
/*     */       }
/* 400 */       this.width = lineWidth;
/*     */     } else {
/* 402 */       for (int i = 0; i < this.eleList.size(); i++) {
/* 403 */         Object ele = this.eleList.get(i);
/* 404 */         if ((ele instanceof EleCharRect)) {
/* 405 */           ele = ((EleCharRect)ele).eleChar;
/*     */         }
/* 407 */         if ((ele instanceof EleText))
/* 408 */           lineWidth = (int)(lineWidth + ((EleText)ele).getBounds().getWidth());
/* 409 */         else if ((ele instanceof EleRect)) {
/* 410 */           lineWidth += ((EleRect)ele).viewSize().x;
/*     */         }
/*     */       }
/* 413 */       this.width = lineWidth;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void distributeSplit(ArrayList list, EleText txt, boolean both)
/*     */   {
/* 422 */     if (both) {
/* 423 */       int pos = 0;
/* 424 */       boolean numBegin = false;
/* 425 */       boolean wordBegin = false;
/*     */ 
/* 427 */       for (int i = 0; i < txt.text.length(); i++) {
/* 428 */         char c = txt.text.charAt(i);
/* 429 */         if ((!wordBegin) && (!numBegin) && (StrUtil.isDigit(c))) {
/* 430 */           if (pos < i) {
/* 431 */             list.add(new EleText(txt, txt.text.substring(pos, i)));
/*     */           }
/* 433 */           pos = i;
/* 434 */           numBegin = true;
/* 435 */         } else if ((!wordBegin) && (StrUtil.isLetter(c))) {
/* 436 */           if (pos < i) {
/* 437 */             list.add(new EleText(txt, txt.text.substring(pos, i)));
/*     */           }
/* 439 */           pos = i;
/* 440 */           wordBegin = true;
/* 441 */         } else if ((numBegin) && (!StrUtil.isDigit(c)) && (c != '.') && (c != ',')) {
/* 442 */           if (pos < i) {
/* 443 */             list.add(new EleText(txt, txt.text.substring(pos, i)));
/*     */           }
/* 445 */           numBegin = false;
/* 446 */           pos = i;
/* 447 */         } else if ((wordBegin) && (!StrUtil.isLetter(c)) && (c != '_') && (!StrUtil.isDigit(c))) {
/* 448 */           if (pos < i) {
/* 449 */             list.add(new EleText(txt, txt.text.substring(pos, i)));
/*     */           }
/* 451 */           wordBegin = false;
/* 452 */           pos = i;
/* 453 */         } else if (ElePara.isPreSign(c)) {
/* 454 */           if (pos < i) {
/* 455 */             list.add(new EleText(txt, txt.text.substring(pos, i)));
/*     */           }
/* 457 */           pos = i;
/* 458 */         } else if ((!numBegin) && (ElePara.isPostSign(c))) {
/* 459 */           if (pos < i + 1) {
/* 460 */             list.add(new EleText(txt, txt.text.substring(pos, i + 1)));
/*     */           }
/* 462 */           pos = i + 1;
/*     */         }
/*     */       }
/* 465 */       if (pos < txt.text.length())
/* 466 */         list.add(new EleText(txt, txt.text.substring(pos)));
/*     */     }
/*     */     else {
/* 469 */       for (int i = 0; i < txt.text.length(); i++)
/* 470 */         list.add(new EleText(txt, String.valueOf(txt.text.charAt(i))));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.DocPageLine
 * JD-Core Version:    0.6.0
 */