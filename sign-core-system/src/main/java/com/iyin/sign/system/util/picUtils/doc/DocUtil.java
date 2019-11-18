/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */

import com.iyin.sign.system.util.picUtils.data.BlkExpression;
import com.iyin.sign.system.util.picUtils.data.MathExpression;
import com.iyin.sign.system.util.picUtils.data.Parser;
import com.iyin.sign.system.util.picUtils.data.RowSet;
import com.iyin.sign.system.util.picUtils.util.*;
import org.dom4j.Document;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class DocUtil
/*     */ {
/*     */   public static final float dpi = 96.0F;
/*  97 */   public static final String[] noStyleAtt = { "name", "top", "left", "width", "height", "row", "rowSpan", "col", "colSpan", "line" };
/*     */ 
/* 192 */   private static int[] headingSize = { 29, 24, 21, 20, 18, 16 };
/*     */ 
/* 272 */   public static boolean withSource = false;
/* 273 */   public static boolean prettyFormat = false;
/*     */ 
/* 277 */   private static final String[] fullFormat = { "xdoc", "json", "zip", "pdf", "docx", "jar", "jpd", "fpd", "epub", "png" };
/*     */ 
/* 324 */   private static int maxLoop = -1;
/*     */   public static final String INNER_HREF_PREFIX = "@:";
/* 332 */   public static ThreadLocal tlUserDocPath = new ThreadLocal();
/* 333 */   public static String serverDocPath = "";
/*     */ 
/*     */   public static EleBase cloneEle(XDoc doc, EleBase ele)
/*     */   {
/*  44 */     return XDocXml.toDocEle(doc, XDocXml.toXmlEle(ele));
/*     */   }
/*     */   public static EleRect getRect(XDoc xdoc, String name) {
/*  47 */     EleRect rect = null;
/*  48 */     EleBase ele = null;
/*  49 */     if (name.startsWith("<"))
/*     */       try {
/*  51 */         ele = XDocXml.toDocEle(xdoc, XmlUtil.parseText(name).getRootElement());
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/*  55 */     if ((ele != null) && ((ele instanceof EleRect))) {
/*  56 */       rect = (EleRect)ele;
/*     */     }
/*  58 */     return rect;
/*     */   }
/*     */ 
/*     */   public static void fixHeads(XDoc doc)
/*     */   {
/*  63 */     ArrayList tmpHeads = doc.heads;
/*  64 */     doc.heads = new ArrayList();
/*     */ 
/*  66 */     boolean find = false;
/*  67 */     for (int i = 0; i < tmpHeads.size(); i++) {
/*  68 */       Heading heading = (Heading)tmpHeads.get(i);
/*  69 */       find = false;
/*  70 */       for (int j = i - 1; j >= 0; j--) {
/*  71 */         Heading tmpHeading = (Heading)tmpHeads.get(j);
/*  72 */         if (tmpHeading.level() < heading.level()) {
/*  73 */           if (tmpHeading.cheads == null) {
/*  74 */             tmpHeading.cheads = new ArrayList();
/*  75 */             tmpHeading.cheads.add(heading);
/*  76 */           } else if (!((Heading)tmpHeading.cheads.get(tmpHeading.cheads.size() - 1)).name().equals(heading.name())) {
/*  77 */             tmpHeading.cheads.add(heading);
/*     */           } else {
/*  79 */             tmpHeads.remove(i--);
/*     */           }
/*  81 */           find = true;
/*  82 */           break;
/*     */         }
/*     */       }
/*  85 */       if (!find)
/*  86 */         if ((doc.heads.size() == 0) || (!((Heading)doc.heads.get(doc.heads.size() - 1)).name().equals(heading.name())))
/*  87 */           doc.heads.add(heading);
/*  88 */         else if (doc.heads.size() > 0)
/*  89 */           tmpHeads.remove(i--);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isXDoc(String url)
/*     */   {
/*  99 */     if (url.endsWith(".xdoc"))
/* 100 */       return true;
/* 101 */     if (url.endsWith(".zip")) {
/*     */       try (ZipInputStream zin = new ZipInputStream(new XUrl(url).getInputStream())){
/* 104 */         ZipEntry en = zin.getNextEntry();
/* 105 */         zin.close();
/* 106 */         return en.getName().endsWith(".xdoc");
/*     */       } catch (Exception e) {
/* 108 */         return false;
/*     */       }
/*     */     }
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   public static void setCRectToView(EleRect rect)
/*     */   {
/* 116 */     Point p = new Point(2147483647, 2147483647);
/* 117 */     for (int i = 0; i < rect.eleList.size(); i++) {
/* 118 */       if ((rect.eleList.get(i) instanceof EleRect)) {
/* 119 */         EleRect crect = (EleRect)rect.eleList.get(i);
/* 120 */         if (crect.left < p.x) {
/* 121 */           p.x = crect.left;
/*     */         }
/* 123 */         if (crect.top < p.y) {
/* 124 */           p.y = crect.top;
/*     */         }
/*     */       }
/*     */     }
/* 128 */     for (int i = 0; i < rect.eleList.size(); i++)
/* 129 */       if ((rect.eleList.get(i) instanceof EleRect)) {
/* 130 */         EleRect crect = (EleRect)rect.eleList.get(i);
/* 131 */         crect.left -= p.x;
/* 132 */         crect.top -= p.y;
/*     */       }
/*     */   }
/*     */ 
/*     */   public static XDoc errDoc(Throwable t) {
/* 137 */     return strDoc(HgException.getStackTraceMsg(t));
/*     */   }
/*     */   public static XDoc strDoc(String str) {
/* 140 */     XDoc xdoc = new XDoc();
/* 141 */     ElePara para = new ElePara(xdoc);
/* 142 */     xdoc.paraList.add(para);
/* 143 */     EleText txt = new EleText(xdoc);
/* 144 */     txt.text = str;
/* 145 */     para.eleList.add(txt);
/* 146 */     return xdoc;
/*     */   }
/*     */   public static List toTabs(XDoc xdoc) {
/* 149 */     ArrayList tabs = new ArrayList();
/* 150 */     for (int i = 0; i < xdoc.rectList.size(); i++) {
/* 151 */       tabs.add(toTab((EleBase)xdoc.rectList.get(i)));
/*     */     }
/*     */ 
/* 154 */     for (int i = 0; i < xdoc.paraList.size(); i++) {
/* 155 */       ElePara para = (ElePara)xdoc.paraList.get(i);
/* 156 */       for (int j = 0; j < para.eleList.size(); j++) {
/* 157 */         if ((para.eleList.size() == 1) && 
/* 158 */           ((para.eleList.get(0) instanceof EleText)) && 
/* 159 */           (((EleText)para.eleList.get(0)).text.length() == 0)) {
/*     */           continue;
/*     */         }
/* 162 */         tabs.add(toTab((EleBase)para.eleList.get(j)));
/*     */       }
/*     */     }
/* 165 */     return tabs;
/*     */   }
/*     */ 
/*     */   private static EleTable toTab(EleBase ele)
/*     */   {
/*     */
/*     */     EleTable tab;
/* 169 */     if ((ele instanceof EleTable)) {
/* 170 */       tab = (EleTable)ele;
/* 171 */     } else if ((ele instanceof EleRect)) {
/* 172 */       EleRect rect = (EleRect)ele;
/* 173 */       tab = new EleTable(rect.xdoc);
/* 174 */       tab.rows = String.valueOf(rect.height);
/* 175 */       tab.cols = String.valueOf(rect.width);
/* 176 */       tab.eleList.add(new EleCell(rect));
/*     */     } else {
/* 178 */       tab = new EleTable(ele.xdoc);
/* 179 */       if ((ele instanceof EleText)) {
/* 180 */         EleRect rect = new EleRect(ele.xdoc);
/* 181 */         ElePara para = new ElePara(ele.xdoc);
/* 182 */         para.eleList.add(ele);
/* 183 */         rect.eleList.add(para);
/* 184 */         tab.eleList.add(new EleCell(rect));
/*     */       }
/*     */     }
/* 187 */     return tab;
/*     */   }
/*     */   public static EleBase copy(XDoc xdoc, EleBase ele) {
/* 190 */     return XDocXml.toDocEle(xdoc, XDocXml.toXmlEle(ele));
/*     */   }
/*     */ 
/*     */   public static int headingFontSize(int n) {
/* 194 */     return (n == 0) || (n > headingSize.length) ? XFont.defaultFontSize : headingSize[(n - 1)];
/*     */   }
/*     */   public static int headingSpacing(int n) {
/* 197 */     int spacing = 0;
/* 198 */     if (n > 0) {
/* 199 */       if (n < headingSize.length)
/* 200 */         spacing = headingSize[(n - 1)] / 2 * 2;
/* 201 */       else if ((n > 0) && (n < headingSize.length)) {
/* 202 */         spacing = 4;
/*     */       }
/*     */     }
/* 205 */     return spacing;
/*     */   }
/*     */   public static void setHeadingStyle(ElePara para, int n) {
/* 208 */     para.heading = n;
/* 209 */     if ((para.eleList.size() == 1) && ((para.eleList.get(0) instanceof EleText))) {
/* 210 */       EleText txt = (EleText)para.eleList.get(0);
/* 211 */       txt.fontName = (n != 0 ? XFont.titleFontName : XFont.defaultFontName);
/* 212 */       txt.fontSize = headingFontSize(n);
/* 213 */       txt.valign = (n == 0 ? "bottom" : "center");
/*     */     }
/* 215 */     para.lineSpacing = headingSpacing(n);
/*     */   }
/*     */   public static void autoSize(EleBase ele) {
/* 218 */     for (int i = 0; i < ele.eleList.size(); i++) {
/* 219 */       autoSize((EleBase)ele.eleList.get(i));
/*     */     }
/* 221 */     if ((ele instanceof EleRect))
/* 222 */       ((EleRect)ele).autoSize();
/*     */   }
/*     */ 
/*     */   public static boolean isFirstPage(Map paramMap) {
/* 226 */     return MapUtil.getBool(paramMap, "firstPage", false);
/*     */   }
/*     */ 
/*     */   public static String dataFormat(String urlStr)
/*     */     throws Exception
/*     */   {
/* 237 */     XUrl url = new XUrl(urlStr);
/* 238 */     InputStream in = url.getInputStream();
/* 239 */     String format = "txt";
/* 240 */     byte[] buf = new byte[256];
/* 241 */     in.read(buf);
/* 242 */     String str = new String(buf).toLowerCase();
/* 243 */     if (str.startsWith("{\\rtf1"))
/* 244 */       format = "rtf";
/* 245 */     else if (str.startsWith("pk"))
/* 246 */       format = "zip";
/* 247 */     else if ((str.startsWith("<html")) || (str.startsWith("<!doctype html")))
/* 248 */       format = "html";
/* 249 */     else if (str.startsWith("<?xml"))
/* 250 */       format = "xml";
/* 251 */     else if (str.startsWith("邢")) {
/* 252 */       format = "doc";
/*     */     }
/* 254 */     in.close();
/* 255 */     return format;
/*     */   }
/*     */   public static Font getDefultFont() {
/* 258 */     return XFont.createFont(XFont.defaultFontName, 0, XFont.defaultFontSize);
/*     */   }
/*     */ 
/*     */   public static String fixPath(String path, Document doc)
/*     */   {
/* 267 */     if ((!path.startsWith("/")) && (doc != null) && (doc.getRootElement() != null)) {
/* 268 */       path = "/" + doc.getRootElement().getName() + "/" + path;
/*     */     }
/* 270 */     return path;
/*     */   }
/*     */ 
/*     */   public static boolean isWithSource(Map params)
/*     */   {
/* 275 */     return MapUtil.getBool(params, "_source", withSource);
/*     */   }
/*     */ 
/*     */   public static boolean isFullFormat(String format) {
/* 279 */     for (int i = 0; i < fullFormat.length; i++) {
/* 280 */       if (fullFormat[i].equals(format)) {
/* 281 */         return true;
/*     */       }
/*     */     }
/* 284 */     return false;
/*     */   }
/*     */ 
/*     */   public static void setPTAtt(EleBase e, String fontName, int fontSizeChange, int lineSpacing, int indentLeft, int indentRight)
/*     */   {
/* 296 */     for (int i = 0; i < e.eleList.size(); i++) {
/* 297 */       EleBase ce = (EleBase)e.eleList.get(i);
/* 298 */       if ((ce instanceof EleText)) {
/* 299 */         EleText txt = (EleText)ce;
/* 300 */         if (fontName.length() > 0) {
/* 301 */           txt.fontName = fontName;
/*     */         }
/* 303 */         txt.fontSize += fontSizeChange;
/* 304 */         if (txt.fontSize < 6)
/* 305 */           txt.fontSize = 6;
/*     */       }
/* 307 */       else if ((ce instanceof ElePara)) {
/* 308 */         ElePara para = (ElePara)ce;
/* 309 */         para.indentLeft += indentLeft;
/* 310 */         para.indentLeft += indentRight;
/* 311 */         para.lineSpacing += lineSpacing;
/* 312 */         setPTAtt(ce, fontName, fontSizeChange, lineSpacing, indentLeft, indentRight);
/*     */       } else {
/* 314 */         setPTAtt(ce, fontName, fontSizeChange, lineSpacing, indentLeft, indentRight);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int getMaxLoop() {
/* 319 */     return maxLoop;
/*     */   }
/*     */ 
/*     */   public static String fixHref(String href, String name)
/*     */   {
/* 326 */     return href.equals("#") ? "@:" + name : href;
/*     */   }
/*     */ 
/*     */   public static String getUserDocPath()
/*     */   {
/* 335 */     String upath = (String)tlUserDocPath.get();
/* 336 */     return upath != null ? upath : serverDocPath;
/*     */   }
/*     */   public static boolean isDynamic(String url) {
/* 339 */     return (url.startsWith("{")) || 
/* 340 */       (url.startsWith("<")) || 
/* 341 */       (url.startsWith("text:")) || 
/* 342 */       (url.startsWith("data:"));
/*     */   }
/*     */   public static boolean isBlank(EleRect base) {
/* 345 */     return (base.name.equals("__blank")) || (base.name.startsWith("__blank:"));
/*     */   }
/*     */   public static String fixFileName(String name) {
/* 348 */     return StrUtil.replaceAll(name, "/", "_");
/*     */   }
/*     */   public static String fixStoreId(String id) {
/* 351 */     String format = "xdat";
/* 352 */     id = id.toLowerCase();
/* 353 */     int pos = id.lastIndexOf('.');
/* 354 */     if (pos >= 0) {
/* 355 */       format = id.substring(pos + 1);
/* 356 */       id = id.substring(0, pos);
/*     */     }
/* 358 */     StringBuffer sb = new StringBuffer("_");
/* 359 */     int i = 0;
/* 360 */     if (id.startsWith("a_")) {
/* 361 */       sb.append("a_");
/* 362 */       i = 2;
/*     */     }
/*     */ 
/* 365 */     for (; i < 32; i++) {
/* 366 */       if (i < id.length()) {
/* 367 */         char c = id.charAt(i);
/* 368 */         if (((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'z')))
/* 369 */           sb.append(c);
/*     */         else
/* 371 */           sb.append((char)(c % '\032' + 97));
/*     */       }
/*     */       else {
/* 374 */         sb.append('0');
/*     */       }
/*     */     }
/* 377 */     sb.append(".").append(format);
/* 378 */     return sb.toString();
/*     */   }
/*     */   public static String simplifyIdName(String name) {
/* 381 */     int pos = name.lastIndexOf(".");
/* 382 */     if ((pos > 0) && (name.startsWith("_"))) {
/* 383 */       for (int i = pos - 1; i >= 0; i--) {
/* 384 */         if (name.charAt(i) != '0') {
/* 385 */           name = name.substring(0, i + 1) + name.substring(pos);
/* 386 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 390 */     return name;
/*     */   }
/*     */ 
/*     */   public static String printEval(String text, XDoc xdoc)
/*     */   {
/* 399 */     if ((text.length() > 0) && (text.indexOf("$[") >= 0)) {
/* 400 */       String str = text;
/*     */ 
/* 402 */       StringBuffer sb = new StringBuffer();
/* 403 */       int pos = str.indexOf("$[");
/* 404 */       if (pos >= 0)
/*     */       {
/* 406 */         BlkExpression blkExp = new BlkExpression(null);
/* 407 */         blkExp.varMap.put("PAGE", new Long(xdoc.getViewPage()));
/* 408 */         blkExp.varMap.put("PAGES", new Long(xdoc.getViewPages()));
/* 409 */         blkExp.varMap.put("PAGENO", new Long(xdoc.getViewPage()));
/* 410 */         blkExp.varMap.put("PAGECOUNT", new Long(xdoc.getViewPages()));
/* 411 */         blkExp.varMap.put("HEADING", xdoc.getViewHeading());
/* 412 */         while (pos >= 0) {
/* 413 */           if (pos > 0) {
/* 414 */             sb.append(str.substring(0, pos));
/*     */           }
/* 416 */           str = str.substring(pos + "$[".length());
/* 417 */           pos = str.indexOf("]");
/* 418 */           if (pos > 0) {
/* 419 */             String expStr = str.substring(0, pos).trim();
/* 420 */             if (expStr.length() > 0) {
/* 421 */               if (expStr.charAt(0) == '\\')
/* 422 */                 sb.append("$[").append(str.substring(1, pos)).append("]");
/*     */               else {
/*     */                 try {
/* 425 */                   expStr = Parser.pretreat(expStr)[0];
/* 426 */                   MathExpression mathExp = new MathExpression(blkExp, expStr);
/* 427 */                   mathExp.eval(null);
/* 428 */                   if (mathExp.resultDataType == 91) {
/* 429 */                     sb.append(DateUtil.toDateTimeString((Date)mathExp.result));
/*     */                   }
/* 431 */                   else if ((mathExp.result instanceof RowSet)) {
/* 432 */                     RowSet rowSet = (RowSet)mathExp.result;
/* 433 */                     if ((rowSet.size() > 0) && (rowSet.fieldSize() > 0))
/* 434 */                       sb.append(rowSet.getCellValue(0, 0));
/*     */                   }
/*     */                   else {
/* 437 */                     sb.append(mathExp.result);
/*     */                   }
/*     */                 }
/*     */                 catch (Exception e) {
/* 441 */                   sb.append("$[").append(str.substring(0, pos)).append("/*").append(e.getMessage()).append("*/").append("]");
/*     */                 }
/*     */               }
/*     */             }
/* 445 */             str = str.substring(pos + "]".length());
/* 446 */           } else if (pos == 0) {
/* 447 */             str = str.substring(1);
/*     */           } else {
/* 449 */             str = "$[" + str;
/* 450 */             break;
/*     */           }
/* 452 */           pos = str.indexOf("$[");
/*     */         }
/*     */       }
/* 455 */       sb.append(str);
/* 456 */       return sb.toString();
/*     */     }
/* 458 */     return text;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.DocUtil
 * JD-Core Version:    0.6.0
 */