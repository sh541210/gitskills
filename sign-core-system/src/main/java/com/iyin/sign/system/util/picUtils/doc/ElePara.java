/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */

import com.iyin.sign.system.util.picUtils.util.ColorUtil;
import com.iyin.sign.system.util.picUtils.util.MapUtil;
import com.iyin.sign.system.util.picUtils.util.StrUtil;
import com.iyin.sign.system.util.picUtils.util.To;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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
/*     */ public class ElePara extends EleBase
/*     */ {
/*     */   public static final String PREFIX_TAB = "{t}";
/*     */   public String headInx;
/*     */   public int lineSpacing;
/*     */   public String align;
/*     */   public boolean breakPage;
/*     */   public int heading;
/*     */   public String prefix;
/*     */   public Color backColor;
/*     */   public String backImg;
/*     */   public int indentLeft;
/*     */   public int indentRight;
/* 137 */   public int layWidth = 0;
/*     */   private static final String postSign = ",.!}])?%，、。．；：？！︰…‥′‵～﹐﹑﹒﹔﹕﹖﹗％‰）︶｝︸〕︺】︼》︾〉﹀」﹂』﹄﹚﹜﹞’”〞ˊ";
/*     */   private static final String preSign = "{[($￡￥＄（︵｛︷〔︹【︻《︽〈︿「﹁『﹃﹙﹛﹝‘“〝ˋ";
/*     */ 
/*     */   protected EleBase copyEle(XDoc xdoc)
/*     */   {
/*  38 */     return new ElePara(xdoc, getAttMap());
/*     */   }
/*     */ 
/*     */   public ElePara(XDoc xdoc, HashMap attMap)
/*     */   {
/*  51 */     super(xdoc, attMap);
/*     */   }
/*     */   public ElePara(XDoc xdoc) {
/*  54 */     super(xdoc);
/*     */   }
/*     */ 
/*     */   public ElePara(XDoc xdoc, ArrayList eleList)
/*     */   {
/*  61 */     this(xdoc);
/*  62 */     this.eleList = eleList;
/*     */   }
/*     */   protected void init() {
/*  65 */     super.init();
/*  66 */     this.name = "";
/*  67 */     this.typeName = "para";
/*  68 */     this.align = "left";
/*  69 */     this.lineSpacing = 0;
/*  70 */     this.indentLeft = 0;
/*  71 */     this.indentRight = 0;
/*  72 */     this.heading = 0;
/*  73 */     this.breakPage = false;
/*  74 */     this.prefix = "";
/*  75 */     this.backColor = null;
/*  76 */     this.backImg = "";
/*     */   }
/*     */   public String toString() {
/*  79 */     StringBuffer sb = new StringBuffer();
/*  80 */     if ((this.prefix.length() > 0) && 
/*  81 */       (!this.prefix.startsWith("<")) && 
/*  82 */       (!this.prefix.startsWith("img:")) && 
/*  83 */       (!this.prefix.startsWith("shape:"))) {
/*  84 */       sb.append(replaceInx(this.prefix));
/*     */     }
/*     */ 
/*  87 */     for (int i = 0; i < this.eleList.size(); i++) {
/*  88 */       Object obj = this.eleList.get(i);
/*  89 */       if ((obj instanceof EleText))
/*  90 */         sb.append(((EleText)obj).viewText());
/*     */       else {
/*  92 */         sb.append(obj.toString());
/*     */       }
/*     */     }
/*  95 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public void setAttMap(HashMap map)
/*     */   {
/* 101 */     super.setAttMap(map);
/* 102 */     this.lineSpacing = MapUtil.getInt(map, "lineSpacing", this.lineSpacing);
/* 103 */     if (map.containsKey("indent")) {
/* 104 */       String[] indent = MapUtil.getString(map, "indent", "").split(",");
/* 105 */       if (indent.length > 0) {
/* 106 */         this.indentLeft = To.toInt(indent[0]);
/*     */       }
/* 108 */       this.indentRight = (indent.length > 1 ? To.toInt(indent[1]) : 0);
/*     */     }
/* 110 */     this.heading = MapUtil.getInt(map, "heading", this.heading);
/* 111 */     this.align = MapUtil.getString(map, "align", this.align);
/* 112 */     this.breakPage = MapUtil.getBool(map, "breakPage", this.breakPage);
/* 113 */     this.prefix = MapUtil.getString(map, "prefix", this.prefix);
/* 114 */     if (map.containsKey("backColor")) {
/* 115 */       this.backColor = ColorUtil.strToColor(MapUtil.getString(map, "backColor", ""), null);
/*     */     }
/* 117 */     this.backImg = MapUtil.getString(map, "backImg", this.backImg);
/*     */   }
/*     */ 
/*     */   public HashMap getAttMap()
/*     */   {
/* 123 */     HashMap map = super.getAttMap();
/* 124 */     map.put("lineSpacing", String.valueOf(this.lineSpacing));
/* 125 */     map.put("indent", this.indentLeft + (this.indentRight != 0 ? "," + this.indentRight : ""));
/* 126 */     map.put("heading", String.valueOf(this.heading));
/* 127 */     map.put("align", this.align);
/* 128 */     map.put("prefix", this.prefix);
/* 129 */     map.put("breakPage", String.valueOf(this.breakPage));
/* 130 */     map.put("backColor", ColorUtil.colorToStr(this.backColor));
/* 131 */     map.put("backImg", this.backImg);
/* 132 */     return map;
/*     */   }
/*     */   public Object clone() {
/* 135 */     return new ElePara(this.xdoc, getAttMap());
/*     */   }
/*     */ 
/*     */   public ArrayList getPrefixEles() {
/* 139 */     ArrayList preEles = null;
/* 140 */     if (this.prefix.length() > 0) {
/* 141 */       preEles = new ArrayList();
/*     */ 
/* 143 */       int n = XFont.defaultFontSize;
/*     */       HashMap atts;
/* 144 */       if (this.eleList.size() > 0) {
/* 145 */         EleBase ele = (EleBase)this.eleList.get(0);
/* 146 */         if ((ele instanceof EleText))
/* 147 */           n = ((EleText)ele).fontSize;
/* 148 */         else if ((ele instanceof EleRect)) {
/* 149 */           n = ((EleRect)ele).height;
/*     */         }
/* 151 */         atts = ele.getAttMap();
/* 152 */         atts.remove("format");
/* 153 */         if (atts.containsKey("fontStyle"))
/* 154 */           atts.put("fontStyle", fixPrefixFontStyle((String)atts.get("fontStyle")));
/*     */       }
/*     */       else {
/* 157 */         atts = new HashMap();
/*     */       }
/* 159 */       String str = this.prefix;
/* 160 */       if (str.indexOf("{t}") >= 0) {
/* 161 */         str = StrUtil.replaceAll(str, "{t}", "　　");
/*     */       }
/*     */ 
/* 164 */       if (str.startsWith("img:<")) {
/* 165 */         str = str.substring(4);
/*     */       }
/* 167 */       if (str.startsWith("img:")) {
/* 168 */         EleImg img = new EleImg(this.xdoc);
/* 169 */         img.fillColor = null;
/* 170 */         img.src = str.substring(4);
/* 171 */         img.valign = MapUtil.getString(atts, "valign");
/* 172 */         img.sizeType = "autosize";
/* 173 */         img.autoSize();
/* 174 */         img.sizeType = "normal";
/* 175 */         double scale = img.height / img.width;
/* 176 */         img.height = n;
/* 177 */         img.width = (int)(n / scale);
/* 178 */         preEles.add(img);
/*     */       } else {
/* 180 */         str = replaceInx(str);
/* 181 */         if ((str.startsWith("<")) && (str.endsWith(">"))) {
/* 182 */           EleGroup gshape = new EleGroup(this.xdoc);
/* 183 */           gshape.text = str;
/* 184 */           gshape.color = null;
/* 185 */           gshape.valign = MapUtil.getString(atts, "valign");
/* 186 */           gshape.width = n;
/* 187 */           gshape.height = n;
/* 188 */           EleRect rect = DocUtil.getRect(this.xdoc, str);
/* 189 */           if (rect != null) {
/* 190 */             double scale = rect.height / rect.width;
/* 191 */             gshape.height = n;
/* 192 */             gshape.width = (int)(n / scale);
/*     */           }
/* 194 */           preEles.add(gshape);
/* 195 */         } else if (str.indexOf('{') >= 0) {
/* 196 */           int pos = 0;
/*     */ 
/* 198 */           while ((str.indexOf('{', pos) >= 0) && (str.indexOf('}', pos) > str.indexOf('{', pos))) {
/* 199 */             EleText text = new EleText(this.xdoc, atts);
/* 200 */             text.text = str.substring(pos, str.indexOf('{', pos));
/* 201 */             preEles.add(text);
/* 202 */             String tmpStr = str.substring(str.indexOf('{', pos) + 1, str.indexOf('}', pos));
/* 203 */             if (tmpStr.startsWith(":")) {
/* 204 */               EleImg img = new EleImg(this.xdoc, atts);
/* 205 */               img.color = null;
/* 206 */               img.width = n;
/* 207 */               img.height = n;
/* 208 */               img.src = tmpStr.substring(1);
/* 209 */               preEles.add(img);
/*     */             } else {
/* 211 */               text = new EleText(this.xdoc, atts);
/* 212 */               text.text = tmpStr;
/* 213 */               preEles.add(text);
/*     */             }
/* 215 */             pos = str.indexOf('}', pos) + 1;
/*     */           }
/* 217 */           if (str.substring(pos).length() > 0) {
/* 218 */             EleText text = new EleText(this.xdoc, atts);
/* 219 */             text.text = str.substring(pos);
/* 220 */             preEles.add(text);
/*     */           }
/*     */ 
/*     */         }
/* 225 */         else if (str.startsWith("shape:")) {
/* 226 */           atts.put("shape", str.substring(6));
/* 227 */           preEles.add(new EleCharRect(this.xdoc, atts));
/*     */         } else {
/* 229 */           EleText text = new EleText(this.xdoc, atts);
/* 230 */           text.text = str;
/* 231 */           fixPrefix(text);
/* 232 */           preEles.add(text);
/*     */         }
/*     */       }
/* 235 */       if (!this.prefix.equals("{t}")) {
/* 236 */         EleText text = new EleText(this.xdoc, atts);
/* 237 */         text.text = " ";
/* 238 */         preEles.add(text);
/*     */       }
/*     */     }
/* 241 */     return preEles;
/*     */   }
/*     */   public static String fixPrefixFontStyle(String style) {
/* 244 */     String[] styles = style.split(",");
/* 245 */     StringBuffer sb = new StringBuffer();
/* 246 */     for (int i = 0; i < styles.length; i++) {
/* 247 */       if ((!styles[i].equals("bold")) && 
/* 248 */         (!styles[i].equals("italic")) && 
/* 249 */         (!styles[i].equals("shadow")) && 
/* 250 */         (!styles[i].equals("outline"))) continue;
/* 251 */       if (sb.length() > 0) sb.append(",");
/* 252 */       sb.append(styles[i]);
/*     */     }
/*     */ 
/* 255 */     return sb.toString();
/*     */   }
/*     */   protected boolean isBlank() {
/* 258 */     return (this.eleList.size() == 0) || ((this.eleList.size() == 1) && ((this.eleList.get(0) instanceof EleText)) && (((EleText)this.eleList.get(0)).text.length() == 0));
/*     */   }
/*     */   public ArrayList toLineList(int top, int width, ArrayList hrList, boolean h) {
/* 261 */     this.layWidth = (width - this.indentLeft - this.indentRight);
/* 262 */     if (isBlank()) {
/* 263 */       ArrayList lineList = new ArrayList();
/* 264 */       lineList.add(new DocPageLine(this, new ArrayList(), this.lineSpacing + XFont.defaultFontSize, 0));
/* 265 */       return lineList;
/*     */     }
/* 267 */     ArrayList preEles = getPrefixEles();
/* 268 */     EleRect prefixRect = null;
/* 269 */     if (preEles != null) {
/* 270 */       prefixRect = new EleRect(this.xdoc);
/* 271 */       prefixRect.color = null;
/* 272 */       prefixRect.txtPadding = 0;
/* 273 */       prefixRect.sizeType = "autosize";
/* 274 */       if (preEles.size() > 0) {
/* 275 */         if ((preEles.get(0) instanceof EleText))
/* 276 */           prefixRect.valign = ((EleText)preEles.get(0)).valign;
/* 277 */         else if ((preEles.get(0) instanceof EleRect)) {
/* 278 */           prefixRect.valign = ((EleRect)preEles.get(0)).valign;
/*     */         }
/*     */       }
/* 281 */       prefixRect.eleList.add(new ElePara(this.xdoc, preEles));
/* 282 */       prefixRect.autoSize();
/*     */     }
/* 284 */     width -= this.indentRight;
/* 285 */     if (h) {
/* 286 */       return toLineListH(top, width, hrList, prefixRect);
/*     */     }
/* 288 */     return toLineListV(top, width, hrList, prefixRect);
/*     */   }
/*     */ 
/*     */   private String replaceInx(String prefix) {
/* 292 */     StringBuffer sb = new StringBuffer();
/* 293 */     int pos = 0;
/*     */ 
/* 295 */     while ((prefix.indexOf('{', pos) >= 0) && (prefix.indexOf('}', pos) > prefix.indexOf('{', pos))) {
/* 296 */       sb.append(prefix.substring(pos, prefix.indexOf('{', pos)));
/* 297 */       String str = prefix.substring(prefix.indexOf('{', pos) + 1, prefix.indexOf('}', pos));
/* 298 */       if (str.startsWith(":")) {
/* 299 */         sb.append("{").append(str).append("}");
/*     */       }
/* 301 */       else if ((this.headInx != null) && (this.headInx.length() > 0)) {
/* 302 */         String[] strs = this.headInx.split("\\.");
/* 303 */         if (str.length() == 1) {
/* 304 */           sb.append(StrUtil.toStrInt(str.charAt(0), Integer.parseInt(strs[(strs.length - 1)])));
/* 305 */         } else if (str.length() > 1) {
/* 306 */           String sp = str.substring(1);
/* 307 */           for (int i = 0; i < strs.length; i++) {
/* 308 */             if (i > 0) {
/* 309 */               sb.append(sp);
/*     */             }
/* 311 */             sb.append(StrUtil.toStrInt(str.charAt(0), Integer.parseInt(strs[i])));
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 316 */       pos = prefix.indexOf('}', pos) + 1;
/*     */     }
/* 318 */     sb.append(prefix.substring(pos));
/*     */ 
/* 322 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static boolean isPreSign(char c)
/*     */   {
/* 327 */     return "{[($￡￥＄（︵｛︷〔︹【︻《︽〈︿「﹁『﹃﹙﹛﹝‘“〝ˋ".indexOf(String.valueOf(c)) >= 0;
/*     */   }
/*     */   public static boolean isPostSign(char c) {
/* 330 */     return ",.!}])?%，、。．；：？！︰…‥′‵～﹐﹑﹒﹔﹕﹖﹗％‰）︶｝︸〕︺】︼》︾〉﹀」﹂』﹄﹚﹜﹞’”〞ˊ".indexOf(String.valueOf(c)) >= 0;
/*     */   }
/*     */   private ArrayList toLineListH(int top, int width, ArrayList hrList, EleRect prefixRect) {
/* 333 */     ArrayList lineList = new ArrayList();
/* 334 */     int prefixWidth = prefixRect != null ? prefixRect.width : 0;
/* 335 */     if (width <= prefixWidth) {
/* 336 */       return lineList;
/*     */     }
/* 338 */     Object ele = null;
/*     */ 
/* 340 */     int lineWidth = this.indentLeft + prefixWidth; int lineHeight = 0;
/* 341 */     if (isPrefixTab()) {
/* 342 */       prefixWidth = 0;
/*     */     }
/*     */ 
/* 345 */     ArrayList lineEleList = new ArrayList();
/*     */ 
/* 347 */     boolean eleProcessed = true;
/*     */ 
/* 349 */     HindRance hr = HindRance.next(hrList, this.indentLeft, top, 10);
/*     */ 
/* 351 */     String href = null;
/* 352 */     for (int i = 0; i < this.eleList.size(); i++) {
/* 353 */       if (eleProcessed) {
/* 354 */         ele = this.eleList.get(i);
/* 355 */         if (((ele instanceof EleText)) && (((EleText)ele).href.equals("#"))) {
/* 356 */           EleText txtEle = (EleText)ele;
/* 357 */           href = txtEle.name.length() > 0 ? txtEle.name : txtEle.text;
/*     */         } else {
/* 359 */           href = null;
/*     */         }
/*     */       }
/* 362 */       if ((ele instanceof EleText)) {
/* 363 */         EleText txtEle = (EleText)ele;
/*     */         String tmpText;
/*     */
/* 364 */         if (eleProcessed)
/* 365 */           tmpText = txtEle.viewText();
/*     */         else {
/* 367 */           tmpText = txtEle.text;
/*     */         }
/* 369 */         Font font = txtEle.getFont(tmpText);
/* 370 */         Rectangle2D bounds = txtEle.getStrBounds(font, tmpText);
/* 371 */         if ((!txtEle.valign.equals("around")) && (bounds.getHeight() + this.lineSpacing > lineHeight)) {
/* 372 */           lineHeight = (int)bounds.getHeight() + this.lineSpacing;
/*     */         }
/* 374 */         if (((lineWidth + (int)bounds.getWidth() > width) || 
/* 375 */           ((hr != null) && (lineWidth + bounds.getWidth() > hr.x)) || 
/* 376 */           (tmpText.indexOf('\n') >= 0)) && 
/* 377 */           (txtEle.canSplit()))
/*     */         {
/* 379 */           for (int j = 0; j < tmpText.length(); j++) {
/* 380 */             if (tmpText.charAt(j) != '\n') {
/* 381 */               bounds = txtEle.getStrBounds(font, String.valueOf(tmpText.charAt(j)));
/* 382 */               if ((hr != null) && (lineWidth + bounds.getWidth() > hr.x))
/*     */               {
/* 384 */                 EleText txtEle2 = new EleText(txtEle.xdoc, txtEle.getAttMap());
/* 385 */                 if (href != null) {
/* 386 */                   txtEle2.name = href;
/*     */                 }
/* 388 */                 txtEle2.text = tmpText.substring(0, j);
/* 389 */                 lineEleList.add(txtEle2);
/* 390 */                 if (((EleText)ele).valign.equals("around")) {
/* 391 */                   bounds = txtEle2.getBounds();
/* 392 */                   hrList.add(new HindRance(new Rectangle(lineWidth, top, (int)bounds.getWidth(), (int)bounds.getHeight())));
/*     */                 }
/*     */ 
/* 395 */                 lineEleList.add(new EleSpace(this.xdoc, hr.width + hr.x - lineWidth, 1));
/* 396 */                 lineWidth = this.indentLeft + prefixWidth + hr.x + hr.width;
/* 397 */                 hr = HindRance.next(hrList, lineWidth, top, lineHeight);
/*     */ 
/* 399 */                 txtEle2 = new EleText(txtEle.xdoc, txtEle.getAttMap());
/* 400 */                 if (href != null) {
/* 401 */                   txtEle2.name = href;
/*     */                 }
/* 403 */                 txtEle2.text = tmpText.substring(j);
/* 404 */                 eleProcessed = false;
/* 405 */                 i--;
/* 406 */                 ele = txtEle2;
/* 407 */                 break;
/*     */               }
/* 409 */               lineWidth = (int)(lineWidth + bounds.getWidth());
/*     */             }
/* 411 */             if ((lineWidth >= width) || (j == tmpText.length() - 1) || (tmpText.charAt(j) == '\n')) {
/* 412 */               if ((j > 1) && (isPostSign(tmpText.charAt(j)))) {
/* 413 */                 j--;
/*     */               }
/* 415 */               if (j > 0)
/*     */               {
/* 417 */                 if (StrUtil.isLetter(tmpText.charAt(j))) {
/* 418 */                   for (int m = j - 1; m >= 0; m--)
/* 419 */                     if (!StrUtil.isLetter(tmpText.charAt(m))) {
/* 420 */                       if (j - m > 18) break;
/* 421 */                       lineWidth -= (int)txtEle.getStrBounds(font, tmpText.substring(m, j)).getWidth();
/* 422 */                       j = m + 1;
/*     */ 
/* 424 */                       break;
/*     */                     }
/*     */                 }
/* 427 */                 else if ((StrUtil.isDigit(tmpText.charAt(j))) || (tmpText.charAt(j) == '.')) {
/* 428 */                   for (int m = j - 1; m >= 0; m--) {
/* 429 */                     if ((!StrUtil.isDigit(tmpText.charAt(m))) && (tmpText.charAt(m) != '.')) {
/* 430 */                       if (j - m > 18) break;
/* 431 */                       lineWidth -= (int)txtEle.getStrBounds(font, tmpText.substring(m, j)).getWidth();
/* 432 */                       j = m + 1;
/*     */ 
/* 434 */                       break;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/* 439 */               if ((j > 1) && (isPreSign(tmpText.charAt(j - 1)))) {
/* 440 */                 j--;
/*     */               }
/*     */ 
/* 443 */               EleText txtEle2 = new EleText(txtEle.xdoc, txtEle.getAttMap());
/* 444 */               if (href != null) {
/* 445 */                 txtEle2.name = href;
/*     */               }
/* 447 */               if (j == 0) {
/* 448 */                 j = 1;
/*     */               }
/* 450 */               txtEle2.text = tmpText.substring(0, j);
/* 451 */               if (txtEle2.text.endsWith(" ")) {
/* 452 */                 txtEle2.text = txtEle2.text.substring(0, txtEle2.text.length() - 1);
/*     */               }
/* 454 */               if ((j < tmpText.length()) && (tmpText.charAt(j) == ' ')) {
/* 455 */                 j++;
/*     */               }
/* 457 */               DocPageLine line = new DocPageLine(this, lineEleList, lineHeight, lineWidth);
/* 458 */               line.enter = ((j < tmpText.length()) && (tmpText.charAt(j) == '\n'));
/* 459 */               lineList.add(line);
/* 460 */               if ((j < tmpText.length()) && (tmpText.charAt(j) == '\n')) {
/* 461 */                 j++;
/*     */               }
/* 463 */               lineEleList.add(txtEle2);
/* 464 */               if (((EleText)ele).valign.equals("around")) {
/* 465 */                 bounds = txtEle2.getBounds();
/* 466 */                 hrList.add(new HindRance(new Rectangle(lineWidth, top, (int)bounds.getWidth(), (int)bounds.getHeight())));
/*     */               }
/* 468 */               lineWidth = this.indentLeft + prefixWidth;
/* 469 */               top += lineHeight;
/* 470 */               hr = HindRance.next(hrList, lineWidth, top, lineHeight);
/* 471 */               lineHeight = 0;
/*     */ 
/* 473 */               lineEleList = new ArrayList();
/* 474 */               if (j < tmpText.length()) {
/* 475 */                 txtEle2 = new EleText(txtEle.xdoc, txtEle.getAttMap());
/* 476 */                 if (href != null) {
/* 477 */                   txtEle2.name = href;
/*     */                 }
/* 479 */                 txtEle2.text = tmpText.substring(j);
/* 480 */                 eleProcessed = false;
/* 481 */                 i--;
/* 482 */                 ele = txtEle2;
/* 483 */                 break;
/* 484 */               }eleProcessed = true;
/*     */ 
/* 486 */               break;
/*     */             }
/*     */           }
/*     */         } else {
/* 490 */           if (((EleText)ele).valign.equals("around")) {
/* 491 */             hrList.add(new HindRance(new Rectangle(lineWidth, top, (int)bounds.getWidth(), (int)bounds.getHeight())));
/*     */           }
/* 493 */           lineWidth = (int)(lineWidth + bounds.getWidth());
/* 494 */           lineEleList.add(ele);
/* 495 */           eleProcessed = true;
/*     */         }
/*     */       } else {
/* 497 */         if (!(ele instanceof EleRect))
/*     */           continue;
/*     */         EleRect rect;
/* 499 */         if ((ele instanceof EleLine)) {
/* 500 */           rect = new EleRect(this.xdoc);
/* 501 */           EleLine eleLine = (EleLine)ele;
/* 502 */           Rectangle rectangle = new Rectangle(Math.min(eleLine.startX, eleLine.endX), Math.min(eleLine.startY, eleLine.endY),
/* 503 */             Math.abs(eleLine.startX - eleLine.endX), Math.abs(eleLine.startY - eleLine.endY));
/*     */           EleRect tmp1480_1478 = rect; tmp1480_1478.top = (int)(tmp1480_1478.top - eleLine.strokeWidth / 2.0D);
/* 505 */           rect.width = rectangle.width;
/* 506 */           rect.height = (rectangle.height + (int)eleLine.strokeWidth);
/* 507 */           rect.valign = eleLine.valign;
/*     */         } else {
/* 509 */           rect = (EleRect)ele;
/* 510 */           rect.autoSize();
/* 511 */           if ((rect instanceof EleImg)) {
/* 512 */             EleRect.fixSize(rect, width, 2147483647);
/*     */           }
/* 514 */           if (rect.rotate != 0) {
/* 515 */             rect = new EleRect(rect.xdoc, rect.getAttMap());
/* 516 */             Point size = rect.viewSize();
/* 517 */             rect.width = size.x;
/* 518 */             rect.height = size.y;
/*     */           }
/*     */         }
/* 521 */         if (rect.valign.equals("around")) {
/* 522 */           HindRance thr = new HindRance(
/* 523 */             new Rectangle(lineWidth, top,
/* 523 */             rect.width, rect.height));
/* 524 */           if (this.align.equals("center"))
/* 525 */             thr.x = ((width - thr.width) / 2);
/* 526 */           else if (this.align.equals("right"))
/* 527 */             thr.x = (width - thr.width);
/*     */           else {
/* 529 */             thr.x = 0;
/*     */           }
/* 531 */           rect.left = thr.x;
/* 532 */           hrList.add(thr);
/* 533 */         } else if (rect.valign.equals("float")) {
/* 534 */           if (this.align.equals("center"))
/* 535 */             rect.left = ((width - rect.width) / 2);
/* 536 */           else if (this.align.equals("right"))
/* 537 */             rect.left = (width - rect.width);
/*     */           else {
/* 539 */             rect.left = 0;
/*     */           }
/*     */         }
/* 542 */         if (lineWidth + rect.width > width) {
/* 543 */           if ((lineWidth == 0) && (rect.width >= width)) {
/* 544 */             lineEleList.add(ele);
/* 545 */             lineWidth += rect.width;
/* 546 */             if ((!rect.valign.equals("around")) && 
/* 547 */               (!rect.valign.equals("float")) && 
/* 548 */               (rect.height + this.lineSpacing > lineHeight)) {
/* 549 */               lineHeight = rect.height + this.lineSpacing;
/*     */             }
/* 551 */             lineList.add(new DocPageLine(this, lineEleList, lineHeight, lineWidth));
/* 552 */             lineEleList = new ArrayList();
/* 553 */             lineWidth = this.indentLeft;
/* 554 */             lineHeight = this.lineSpacing;
/*     */           } else {
/* 556 */             lineList.add(new DocPageLine(this, lineEleList, lineHeight, lineWidth));
/* 557 */             lineEleList = new ArrayList();
/* 558 */             lineEleList.add(ele);
/* 559 */             lineWidth = this.indentLeft + rect.width;
/* 560 */             lineHeight = this.lineSpacing + rect.height;
/*     */           }
/*     */         } else {
/* 563 */           if ((!rect.valign.equals("around")) && 
/* 564 */             (!rect.valign.equals("float")) && 
/* 565 */             (rect.height + this.lineSpacing > lineHeight)) {
/* 566 */             lineHeight = rect.height + this.lineSpacing;
/*     */           }
/* 568 */           lineWidth += rect.width;
/* 569 */           lineEleList.add(ele);
/*     */         }
/* 571 */         eleProcessed = true;
/*     */       }
/*     */     }
/* 574 */     if (lineEleList.size() > 0)
/*     */     {
/* 576 */       lineList.add(new DocPageLine(this, lineEleList, lineHeight, lineWidth));
/*     */     }
/* 578 */     DocPageLine preLine = null;
/* 579 */     EleRect spacePrefix = null;
/* 580 */     for (int i = 0; i < lineList.size(); i++) {
/* 581 */       DocPageLine line = (DocPageLine)lineList.get(i);
/* 582 */       if ((prefixRect != null) && (line.eleList.size() > 0) && 
/* 583 */         (this.prefix != null) && (lineList.size() > 0)) {
/* 584 */         if ((i == 0) || ((preLine != null) && (preLine.enter))) {
/* 585 */           line.eleList.add(0, prefixRect);
/* 586 */         } else if (!isPrefixTab()) {
/* 587 */           if (i == 1) {
/* 588 */             spacePrefix = new EleSpace(this.xdoc, prefixRect.width, prefixRect.height);
/*     */           }
/* 590 */           line.eleList.add(0, spacePrefix);
/*     */         }
/*     */       }
/*     */ 
/* 594 */       line.align = this.align;
/* 595 */       if (line.align.length() == 0) line.align = "left";
/* 596 */       line.calWidth();
/* 597 */       if (line.width < this.layWidth) {
/* 598 */         if ((i < lineList.size() - 1) && (!line.enter) && ((line.eleList.size() != 1) || (!(line.eleList.get(0) instanceof EleRect)))) {
/* 599 */           line.bothDistribute(this.layWidth, (prefixRect != null) && (!isPrefixTab()) ? 1 : 0);
/*     */         }
/* 601 */         else if (line.align.equals("center")) {
/* 602 */           line.offset = ((this.layWidth - line.width) / 2);
/* 603 */           line.offset -= line.offset % 4;
/* 604 */         } else if (line.align.equals("right")) {
/* 605 */           line.offset = (this.layWidth - line.width);
/* 606 */         } else if (line.align.equals("distribute")) {
/* 607 */           line.distribute(this.layWidth, (prefixRect != null) && (!isPrefixTab()) ? 1 : 0);
/*     */         }
/*     */       }
/*     */ 
/* 611 */       line.offset += this.indentLeft;
/* 612 */       preLine = line;
/*     */     }
/* 614 */     return lineList;
/*     */   }
/*     */   private ArrayList toLineListV(int top, int width, ArrayList hrList, EleRect prefixRect) {
/* 617 */     int prefixWidth = prefixRect != null ? prefixRect.width : 0;
/* 618 */     ArrayList lineList = new ArrayList();
/* 619 */     Object ele = null;
/*     */ 
/* 621 */     int lineWidth = this.indentLeft + prefixWidth; int lineHeight = this.lineSpacing;
/* 622 */     if (isPrefixTab()) {
/* 623 */       prefixWidth = 0;
/*     */     }
/*     */ 
/* 626 */     ArrayList lineEleList = new ArrayList();
/*     */ 
/* 628 */     double cWidth = 0.0D;
/*     */ 
/* 632 */     for (int i = 0; i < this.eleList.size(); i++) {
/* 633 */       ele = this.eleList.get(i);
/* 634 */       if ((ele instanceof EleText)) {
/* 635 */         EleText txtEle = (EleText)ele;
/*     */         String href;
/*     */
/* 636 */         if (txtEle.href.equals("#"))
/* 637 */           href = txtEle.name.length() > 0 ? txtEle.name : txtEle.text;
/*     */         else {
/* 639 */           href = null;
/*     */         }
/*     */ 
/* 642 */         String tmpText = txtEle.text;
/* 643 */         Font font = txtEle.getFont(tmpText);
/* 644 */         for (int j = 0; j < tmpText.length(); j++)
/* 645 */           if (tmpText.charAt(j) == '\n') {
/* 646 */             lineList.add(new DocPageLine(this, lineEleList, lineHeight, lineWidth, true));
/* 647 */             lineHeight = this.lineSpacing;
/* 648 */             lineWidth = this.indentLeft + prefixWidth;
/* 649 */             lineEleList = new ArrayList();
/*     */           } else {
/* 651 */             Rectangle2D bounds = txtEle.getStrBounds(font, String.valueOf(tmpText.charAt(j)), true);
/* 652 */             if (EleText.isVRotate(tmpText.charAt(j)))
/* 653 */               cWidth = bounds.getWidth();
/*     */             else {
/* 655 */               cWidth = bounds.getHeight();
/*     */             }
/* 657 */             lineWidth = (int)(lineWidth + cWidth);
/* 658 */             if (lineWidth >= width) {
/* 659 */               if ((lineWidth == width) || (cWidth >= width - this.indentLeft) || (isPostSign(tmpText.charAt(j)))) {
/* 660 */                 EleText txtEle2 = new EleText(txtEle.xdoc, txtEle.getAttMap());
/* 661 */                 if (href != null) {
/* 662 */                   txtEle2.name = href;
/*     */                 }
/* 664 */                 txtEle2.text = String.valueOf(tmpText.charAt(j));
/* 665 */                 lineEleList.add(txtEle2);
/* 666 */                 if (EleText.isVRotate(tmpText.charAt(j))) {
/* 667 */                   if (bounds.getHeight() + this.lineSpacing > lineHeight) {
/* 668 */                     lineHeight = (int)bounds.getHeight() + this.lineSpacing;
/*     */                   }
/*     */                 }
/* 671 */                 else if (bounds.getWidth() + this.lineSpacing > lineHeight) {
/* 672 */                   lineHeight = (int)bounds.getWidth() + this.lineSpacing;
/*     */                 }
/*     */ 
/* 675 */                 if ((j + 1 < tmpText.length()) && (tmpText.charAt(j + 1) == '\n'))
/* 676 */                   j++;
/*     */               }
/*     */               else {
/* 679 */                 j--;
/*     */               }
/* 681 */               lineList.add(new DocPageLine(this, lineEleList, lineHeight, lineWidth, true));
/* 682 */               lineHeight = this.lineSpacing;
/* 683 */               lineWidth = this.indentLeft + prefixWidth;
/* 684 */               lineEleList = new ArrayList();
/*     */             } else {
/* 686 */               EleText txtEle2 = new EleText(txtEle.xdoc, txtEle.getAttMap());
/* 687 */               if (href != null) {
/* 688 */                 txtEle2.name = href;
/*     */               }
/* 690 */               txtEle2.text = String.valueOf(tmpText.charAt(j));
/* 691 */               lineEleList.add(txtEle2);
/* 692 */               if (EleText.isVRotate(tmpText.charAt(j))) {
/* 693 */                 if (bounds.getHeight() + this.lineSpacing > lineHeight) {
/* 694 */                   lineHeight = (int)bounds.getHeight() + this.lineSpacing;
/*     */                 }
/*     */               }
/* 697 */               else if (bounds.getWidth() + this.lineSpacing > lineHeight)
/* 698 */                 lineHeight = (int)bounds.getWidth() + this.lineSpacing;
/*     */             }
/*     */           }
/*     */       }
/*     */       else
/*     */       {
/* 704 */         if (!(ele instanceof EleRect))
/*     */           continue;
/* 706 */         EleRect rect = (EleRect)ele;
/* 707 */         rect.autoSize();
/* 708 */         if (rect.rotate != 0) {
/* 709 */           rect = new EleRect(rect.xdoc, rect.getAttMap());
/* 710 */           Point size = rect.viewSize();
/* 711 */           rect.width = size.x;
/* 712 */           rect.height = size.y;
/*     */         }
/* 714 */         int rectWidth = (ele instanceof EleCharRect) ? rect.height : rect.width;
/* 715 */         int rectHeight = (ele instanceof EleCharRect) ? rect.width : rect.height;
/* 716 */         if (lineWidth + rectWidth > width) {
/* 717 */           if (rectWidth >= width) {
/* 718 */             lineEleList.add(ele);
/* 719 */             lineWidth += rectWidth;
/* 720 */             if ((!rect.valign.equals("around")) && 
/* 721 */               (!rect.valign.equals("float")) && 
/* 722 */               (rectWidth + this.lineSpacing > lineHeight)) {
/* 723 */               lineHeight = rectWidth + this.lineSpacing;
/*     */             }
/*     */           }
/* 726 */           lineList.add(new DocPageLine(this, lineEleList, lineHeight, lineWidth, true));
/* 727 */           lineEleList = new ArrayList();
/* 728 */           lineEleList.add(ele);
/* 729 */           lineWidth = this.indentLeft + prefixWidth + rectWidth;
/* 730 */           lineHeight = this.lineSpacing + rectHeight;
/* 731 */           if (rectWidth >= width) {
/* 732 */             lineList.add(new DocPageLine(this, lineEleList, lineHeight, lineWidth, true));
/* 733 */             lineEleList = new ArrayList();
/* 734 */             lineWidth = this.indentLeft;
/* 735 */             lineHeight = this.lineSpacing;
/*     */           }
/*     */         } else {
/* 738 */           if ((!rect.valign.equals("around")) && 
/* 739 */             (!rect.valign.equals("float")) && 
/* 740 */             (rectWidth + this.lineSpacing > lineHeight)) {
/* 741 */             lineHeight = rectWidth + this.lineSpacing;
/*     */           }
/* 743 */           lineWidth += rectWidth;
/* 744 */           lineEleList.add(ele);
/*     */         }
/*     */       }
/*     */     }
/* 748 */     if (lineEleList.size() > 0)
/*     */     {
/* 750 */       lineList.add(new DocPageLine(this, lineEleList, lineHeight, lineWidth, true));
/*     */     }
/* 752 */     DocPageLine preLine = null;
/* 753 */     EleRect spacePrefix = null;
/* 754 */     for (int i = 0; i < lineList.size(); i++) {
/* 755 */       DocPageLine line = (DocPageLine)lineList.get(i);
/* 756 */       if ((prefixRect != null) && (line.eleList.size() > 0) && 
/* 757 */         (this.prefix != null) && (lineList.size() > 0)) {
/* 758 */         if ((i == 0) || ((preLine != null) && (preLine.enter))) {
/* 759 */           line.eleList.add(0, prefixRect);
/* 760 */         } else if (!isPrefixTab()) {
/* 761 */           if (i == 1) {
/* 762 */             spacePrefix = new EleSpace(this.xdoc, prefixRect.width, prefixRect.height);
/*     */           }
/* 764 */           line.eleList.add(0, spacePrefix);
/*     */         }
/*     */       }
/*     */ 
/* 768 */       line.align = this.align;
/* 769 */       if (line.align.length() == 0) line.align = "left";
/* 770 */       if (line.width < width) {
/* 771 */         if ((i < lineList.size() - 1) && (!line.enter)) {
/* 772 */           line.bothDistribute(width, (prefixRect != null) && (!isPrefixTab()) ? 1 : 0);
/*     */         }
/* 774 */         else if (line.align.equals("center")) {
/* 775 */           line.offset = ((width - line.width) / 2);
/* 776 */           line.offset -= line.offset % 4;
/* 777 */         } else if (line.align.equals("right")) {
/* 778 */           line.offset = (width - line.width);
/* 779 */         } else if (line.align.equals("distribute")) {
/* 780 */           line.distribute(width, (prefixRect != null) && (!isPrefixTab()) ? 1 : 0);
/*     */         }
/*     */       }
/*     */ 
/* 784 */       line.offset += this.indentLeft;
/* 785 */       preLine = line;
/*     */     }
/* 787 */     return lineList;
/*     */   }
/*     */   public EleText addText() {
/* 790 */     EleText txt = new EleText(this.xdoc);
/* 791 */     this.eleList.add(txt);
/* 792 */     return txt;
/*     */   }
/*     */   public void setText(String text) {
/* 795 */     EleText txt = null;
/* 796 */     if ((this.eleList.size() == 0) || (!(this.eleList.get(this.eleList.size() - 1) instanceof EleText))) {
/* 797 */       txt = new EleText(this.xdoc);
/* 798 */       this.eleList.add(txt);
/*     */     } else {
/* 800 */       txt = (EleText)this.eleList.get(this.eleList.size() - 1);
/*     */     }
/* 802 */     txt.setText(text);
/*     */   }
/*     */   public static void fixPrefix(EleText text) {
/* 805 */     if ((text.text.length() == 1) && 
/* 806 */       ("●○■□◆◇".indexOf(text.text.charAt(0)) >= 0))
/* 807 */       text.fontSize = (text.fontSize * 4 / 5);
/*     */   }
/*     */ 
/*     */   public static void genHeadInx(ArrayList paras) {
/* 811 */     int n = 0;
/* 812 */     String prefix = "";
/*     */ 
/* 814 */     for (int i = 0; i < paras.size(); i++) {
/* 815 */       ElePara para = (ElePara)paras.get(i);
/* 816 */       if (para.heading == 0) {
/* 817 */         if (para.isBlank())
/* 818 */           n = -1;
/* 819 */         else if (!prefix.equals(para.prefix)) {
/* 820 */           n = 0;
/*     */         }
/* 822 */         n++; para.headInx = String.valueOf(n);
/* 823 */         prefix = para.prefix;
/*     */       } else {
/* 825 */         n = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isPrefixTab()
/*     */   {
/* 834 */     return this.prefix.equals("{t}");
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.ElePara
 * JD-Core Version:    0.6.0
 */