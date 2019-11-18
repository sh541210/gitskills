/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class DocPrint
/*     */   implements Printable
/*     */ {
/*     */   protected XDoc xdoc;
/*     */   public ArrayList pages;
/*  42 */   public List toolTips = new ArrayList();
/*  43 */   public List hrefs = new ArrayList();
/*  44 */   public List inputs = new ArrayList();
/*     */ 
/*     */   public DocPrint(XDoc xdoc)
/*     */   {
/*  28 */     this(xdoc, false);
/*     */   }
/*     */ 
/*     */   private static void getHeads(EleBase ele, ArrayList heads) {
/*  32 */     for (int i = 0; i < ele.eleList.size(); i++) {
/*  33 */       if ((ele.eleList.get(i) instanceof ElePara)) {
/*  34 */         ElePara para = (ElePara)ele.eleList.get(i);
/*  35 */         if (para.heading > 0) {
/*  36 */           heads.add(new Heading(para));
/*     */         }
/*     */       }
/*  39 */       getHeads((EleBase)ele.eleList.get(i), heads);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DocPrint(XDoc xdoc, boolean firstOnly)
/*     */   {
/*  46 */     this(xdoc, firstOnly, true);
/*     */   }
/*     */ 
/*     */   public DocPrint(XDoc xdoc, boolean firstOnly, boolean splitPage)
/*     */   {
/*  55 */     if (xdoc == null) return;
/*  56 */     this.xdoc = xdoc;
/*  57 */     EleRect rect = new EleRect(xdoc, xdoc.bodyRect.getAttMap());
/*  58 */     rect.align = "top";
/*  59 */     if ((rect.sizeType.equals("autowidth")) || 
/*  60 */       (rect.sizeType.equals("autosize")))
/*  61 */       rect.sizeType = "autosize";
/*     */     else {
/*  63 */       rect.sizeType = "autoheight";
/*     */     }
/*  65 */     DocPaper paper = xdoc.getPaper();
/*  66 */     xdoc.frontRect.left = 0;
/*  67 */     xdoc.frontRect.top = 0;
/*  68 */     xdoc.frontRect.width = paper.width;
/*  69 */     xdoc.frontRect.height = paper.height;
/*  70 */     xdoc.backRect.left = 0;
/*  71 */     xdoc.backRect.top = 0;
/*  72 */     xdoc.backRect.width = paper.width;
/*  73 */     xdoc.backRect.height = paper.height;
/*  74 */     rect.width = paper.getContentWidth();
/*  75 */     rect.height = 2147483647;
/*  76 */     if (this.xdoc.bodyRect.sizeType.equals("autofont")) {
/*  77 */       rect.sizeType = "autofont";
/*  78 */       rect.height = paper.getContentHeight();
/*     */     } else {
/*  80 */       rect.height = 2147483647;
/*     */     }
/*  82 */     ElePara.genHeadInx(xdoc.paraList);
/*  83 */     rect.eleList.addAll(xdoc.paraList);
/*  84 */     rect.eleList.addAll(xdoc.rectList);
/*  85 */     genHead(xdoc, rect);
/*  86 */     if ((this.xdoc.bodyRect.sizeType.equals("autowidth")) || 
/*  87 */       (this.xdoc.bodyRect.sizeType.equals("autoheight")) || 
/*  88 */       (this.xdoc.bodyRect.sizeType.equals("autosize")) || 
/*  89 */       (this.xdoc.bodyRect.sizeType.equals("autofont"))) {
/*  90 */       rect.autoSize();
/*  91 */       paper = this.xdoc.getPaper();
/*  92 */       if ((this.xdoc.bodyRect.sizeType.equals("autowidth")) || (this.xdoc.bodyRect.sizeType.equals("autosize"))) {
/*  93 */         paper.width = (rect.width + paper.getLeftMargin() + paper.getRightMargin());
/*     */       }
/*  95 */       if ((this.xdoc.bodyRect.sizeType.equals("autoheight")) || (this.xdoc.bodyRect.sizeType.equals("autosize"))) {
/*  96 */         paper.height = (rect.height + paper.getTopMargin() + paper.getBottomMargin());
/*     */       }
/*  98 */       xdoc.frontRect.width = paper.width;
/*  99 */       xdoc.frontRect.height = paper.height;
/* 100 */       xdoc.backRect.width = paper.width;
/* 101 */       xdoc.backRect.height = paper.height;
/* 102 */       rect.name = "$_NO_BREAK_$";
/*     */     }
/* 104 */     if (this.xdoc.metaMap.containsKey("page")) {
/* 105 */       rect.height = xdoc.getPaper().getContentHeight();
/*     */     }
/* 107 */     if ((this.xdoc.bodyRect.sizeType.equals("autoheight")) || 
/* 108 */       (this.xdoc.bodyRect.sizeType.equals("autosize")) || 
/* 109 */       (this.xdoc.metaMap.containsKey("page"))) {
/* 110 */       this.pages = new ArrayList();
/* 111 */       this.pages.add(rect);
/*     */     }
/* 113 */     else if (!splitPage) {
/* 114 */       this.pages = new ArrayList();
/* 115 */       this.pages.add(rect);
/*     */ 
/* 117 */       rect.rectSize();
/*     */     }
/*     */     else {
/* 120 */       if ((rect.eleList.size() > 0) && ((rect.eleList.get(0) instanceof ElePara))) {
/* 121 */         ElePara para = (ElePara)rect.eleList.get(0);
/* 122 */         if ((para.eleList.size() == 1) && ((para.eleList.get(0) instanceof EleTable)))
/*     */         {
/* 124 */           para = new ElePara(xdoc);
/* 125 */           EleText txt = para.addText();
/* 126 */           txt.text = " ";
/* 127 */           para.lineSpacing = -1;
/* 128 */           txt.fontSize = 1;
/* 129 */           rect.eleList.add(0, para);
/*     */         }
/*     */       }
/* 132 */       this.pages = EleRect.split(rect, xdoc.getPaper().getContentHeight(), xdoc.getPaper().getContentHeight(), firstOnly);
/* 133 */       if (this.pages.size() == 0) {
/* 134 */         this.pages = new ArrayList();
/* 135 */         this.pages.add(rect);
/*     */       }
/*     */       else {
/* 138 */         for (int i = 0; i < this.pages.size(); i++) {
/* 139 */           ((EleRect)this.pages.get(i)).rectSize();
/*     */         }
/*     */       }
/* 142 */       for (int i = 0; i < this.pages.size(); i++) {
/* 143 */         ((EleRect)this.pages.get(i)).height = xdoc.getPaper().getContentHeight();
/*     */       }
/*     */     }
/*     */ 
/* 147 */     xdoc.print = this;
/* 148 */     xdoc.pages = this.pages.size();
/*     */   }
/*     */   public static void genHead(XDoc xdoc) {
/* 151 */     EleRect rect = new EleRect(xdoc);
/* 152 */     rect.eleList.addAll(xdoc.paraList);
/* 153 */     rect.eleList.addAll(xdoc.rectList);
/* 154 */     genHead(xdoc, rect);
/*     */   }
/*     */   public static void genHead(XDoc xdoc, EleRect rect) {
/* 157 */     ArrayList tmpHeads = new ArrayList();
/*     */ 
/* 159 */     getHeads(rect, tmpHeads);
/*     */ 
/* 161 */     boolean find = false;
/*     */ 
/* 163 */     xdoc.heads = new ArrayList();
/* 164 */     for (int i = 0; i < tmpHeads.size(); i++) {
/* 165 */       Heading heading = (Heading)tmpHeads.get(i);
/* 166 */       find = false;
/* 167 */       for (int j = i - 1; j >= 0; j--) {
/* 168 */         Heading tmpHeading = (Heading)tmpHeads.get(j);
/* 169 */         if (tmpHeading.level() < heading.level()) {
/* 170 */           if (tmpHeading.cheads == null) {
/* 171 */             tmpHeading.cheads = new ArrayList();
/*     */           }
/* 173 */           tmpHeading.cheads.add(heading);
/* 174 */           find = true;
/* 175 */           break;
/*     */         }
/*     */       }
/* 178 */       if (!find) {
/* 179 */         xdoc.heads.add(heading);
/*     */       }
/*     */     }
/*     */ 
/* 183 */     genHeadInx(xdoc.heads, "");
/*     */   }
/*     */ 
/*     */   private static void genHeadInx(ArrayList heads, String inxStr) {
/* 187 */     if (heads != null) {
/* 188 */       int n = 0;
/* 189 */       for (int i = 0; i < heads.size(); i++)
/* 190 */         if (!((Heading)heads.get(i)).para.isBlank()) {
/* 191 */           ((Heading)heads.get(i)).para.headInx = (inxStr + (n + 1));
/* 192 */           genHeadInx(((Heading)heads.get(i)).cheads, inxStr + (n + 1) + ".");
/* 193 */           n++;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getPageCount() {
/* 199 */     return this.xdoc.pages;
/*     */   }
/*     */   public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
/* 202 */     this.xdoc.page = (pageIndex + 1);
/* 203 */     if (pageIndex < this.xdoc.pages) {
/* 204 */       if (pageIndex == 0) {
/* 205 */         this.xdoc.heading = "";
/*     */       }
/* 207 */       Graphics2D g2 = (Graphics2D)g;
/* 208 */       if (this.xdoc.scale != 1.0D) {
/* 209 */         g2.scale(this.xdoc.scale, this.xdoc.scale);
/*     */       }
/* 211 */       this.xdoc.backRect.print(g2);
/* 212 */       EleRect rect = (EleRect)this.pages.get(pageIndex);
/* 213 */       g2.translate(this.xdoc.getPaper().getLeftMargin(), this.xdoc.getPaper().getTopMargin());
/* 214 */       rect.print(g2);
/* 215 */       g2.translate(-this.xdoc.getPaper().getLeftMargin(), -this.xdoc.getPaper().getTopMargin());
/* 216 */       this.xdoc.frontRect.print(g2);
/* 217 */       return 0;
/*     */     }
/* 219 */     return 1;
/*     */   }
/*     */ 
/*     */   public XDoc getPageDoc(int inx) {
/* 223 */     XDoc pdoc = new XDoc();
/* 224 */     pdoc.setPaper(this.xdoc.getPaper());
/* 225 */     pdoc.bodyRect = this.xdoc.bodyRect;
/* 226 */     pdoc.backRect = this.xdoc.backRect;
/* 227 */     pdoc.bodyRect = new EleRect(pdoc, this.xdoc.bodyRect.getAttMap());
/* 228 */     pdoc.bodyRect.sizeType = "normal";
/* 229 */     pdoc.metaMap.putAll(this.xdoc.metaMap);
/* 230 */     pdoc.metaMap.put("page", String.valueOf(inx + 1));
/* 231 */     pdoc.metaMap.put("pages", String.valueOf(this.xdoc.pages));
/* 232 */     pdoc.metaMap.put("heading", String.valueOf(this.xdoc.heading));
/* 233 */     pdoc.frontRect = this.xdoc.frontRect;
/* 234 */     EleRect rect = (EleRect)this.pages.get(inx);
/* 235 */     for (int i = 0; i < rect.lineList.size(); i++) {
/* 236 */       pdoc.paraList.add(new EleParaLine((DocPageLine)rect.lineList.get(i)));
/*     */     }
/* 238 */     pdoc.rectList = rect.eleList;
/* 239 */     return pdoc;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.DocPrint
 * JD-Core Version:    0.6.0
 */