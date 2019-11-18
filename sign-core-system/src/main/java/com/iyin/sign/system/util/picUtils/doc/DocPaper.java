/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */

import com.iyin.sign.system.util.picUtils.util.To;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSize.ISO;
import javax.print.attribute.standard.MediaSizeName;
import java.awt.*;
import java.util.HashMap;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class DocPaper
/*     */ {
/*     */   public String name;
/*     */   public int width;
/*     */   public int height;
/*     */   private int topMargin;
/*     */   private int leftMargin;
/*     */   private int rightMargin;
/*     */   private int bottomMargin;
/*     */   private XDoc xdoc;
/*  66 */   public static int DEFAULT_MARGIN = 96;
/*  67 */   public static int DEFAULT_WIDTH = (int)(ISO.A4.getX(25400) * 96.0F);
/*  68 */   public static int DEFAULT_HEIGHT = (int)(ISO.A4.getY(25400) * 96.0F);
/*     */ 
/* 124 */   private static HashMap paperMap = new HashMap();
/*     */ 
/* 125 */   static { MediaSizeName[] mss = { MediaSizeName.ISO_A0,
/* 126 */       MediaSizeName.ISO_A1,
/* 127 */       MediaSizeName.ISO_A2,
/* 128 */       MediaSizeName.ISO_A3,
/* 129 */       MediaSizeName.ISO_A4,
/* 130 */       MediaSizeName.ISO_A5,
/* 131 */       MediaSizeName.ISO_A6,
/* 132 */       MediaSizeName.ISO_A7,
/* 133 */       MediaSizeName.ISO_A8,
/* 134 */       MediaSizeName.ISO_A9,
/* 135 */       MediaSizeName.ISO_A10,
/* 136 */       MediaSizeName.ISO_B0,
/* 137 */       MediaSizeName.ISO_B1,
/* 138 */       MediaSizeName.ISO_B2,
/* 139 */       MediaSizeName.ISO_B3,
/* 140 */       MediaSizeName.ISO_B4,
/* 141 */       MediaSizeName.ISO_B5,
/* 142 */       MediaSizeName.ISO_B6,
/* 143 */       MediaSizeName.ISO_B7,
/* 144 */       MediaSizeName.ISO_B8,
/* 145 */       MediaSizeName.ISO_B9,
/* 146 */       MediaSizeName.ISO_B10,
/* 147 */       MediaSizeName.ISO_C0,
/* 148 */       MediaSizeName.ISO_C1,
/* 149 */       MediaSizeName.ISO_C2,
/* 150 */       MediaSizeName.ISO_C3,
/* 151 */       MediaSizeName.ISO_C4,
/* 152 */       MediaSizeName.ISO_C5,
/* 153 */       MediaSizeName.ISO_C6 };
/*     */ 
/* 155 */     for (int i = 0; i < mss.length; i++) {
/* 156 */       MediaSize ms = MediaSize.getMediaSizeForName(mss[i]);
/* 157 */       if (ms != null)
/* 158 */         paperMap.put(ms.getMediaSizeName().toString().toUpperCase(), 
/* 159 */           new Dimension((int)(ms.getX(25400) * 96.0F),
/* 159 */           (int)(ms.getY(25400) * 96.0F)));
/*     */     }
/*     */   }
/*     */ 
/*     */   public int viewWidth()
/*     */   {
/*  40 */     return this.xdoc.intScale(this.width);
/*     */   }
/*     */   public int viewHeight() {
/*  43 */     return this.xdoc.intScale(this.height);
/*     */   }
/*     */ 
/*     */   public int getContentWidth()
/*     */   {
/*  50 */     return Math.max(this.width - getLeftMargin() - getRightMargin(), XFont.defaultFontSize);
/*     */   }
/*     */ 
/*     */   public int getContentHeight()
/*     */   {
/*  57 */     return Math.max(this.height - getTopMargin() - getBottomMargin(), XFont.defaultFontSize);
/*     */   }
/*     */ 
/*     */   public Dimension getContentSize()
/*     */   {
/*  64 */     return new Dimension(getContentWidth(), getContentHeight());
/*     */   }
/*     */ 
/*     */   public DocPaper(XDoc xdoc)
/*     */   {
/*  70 */     this.xdoc = xdoc;
/*  71 */     this.width = DEFAULT_WIDTH;
/*  72 */     this.height = DEFAULT_HEIGHT;
/*  73 */     setMargin(DEFAULT_MARGIN);
/*     */   }
/*     */   public DocPaper(XDoc xdoc, String paper) {
/*  76 */     this.xdoc = xdoc;
/*  77 */     String[] strs = paper.split(",");
/*  78 */     strs[0] = strs[0].toUpperCase();
/*  79 */     if (strs[0].indexOf("*") >= 0) {
/*  80 */       int pos = strs[0].indexOf("*");
/*  81 */       this.width = To.toInt(strs[0].substring(0, pos));
/*  82 */       this.height = To.toInt(strs[0].substring(pos + 1));
/*  83 */       if ((this.width == 0) && (this.height == 0)) {
/*  84 */         this.width = DEFAULT_WIDTH;
/*  85 */         this.height = DEFAULT_HEIGHT;
/*  86 */         xdoc.bodyRect.sizeType = "autosize";
/*  87 */       } else if (this.width == 0) {
/*  88 */         this.width = DEFAULT_WIDTH;
/*  89 */         xdoc.bodyRect.sizeType = "autowidth";
/*  90 */       } else if (this.height == 0) {
/*  91 */         this.height = DEFAULT_HEIGHT;
/*  92 */         xdoc.bodyRect.sizeType = "autoheight";
/*     */       } else {
/*  94 */         xdoc.bodyRect.sizeType = "normal";
/*     */       }
/*  96 */       if (strs.length > 1)
/*  97 */         setMargin(To.toInt(strs[1]));
/*     */     }
/*     */     else {
/* 100 */       if (paperMap.get(strs[0]) != null) {
/* 101 */         Dimension d = (Dimension)paperMap.get(strs[0]);
/* 102 */         this.width = d.width;
/* 103 */         this.height = d.height;
/* 104 */       } else if (paperMap.get("ISO-" + strs[0]) != null) {
/* 105 */         Dimension d = (Dimension)paperMap.get("ISO-" + strs[0]);
/* 106 */         this.width = d.width;
/* 107 */         this.height = d.height;
/*     */       }
/* 109 */       if ((strs.length > 1) && 
/* 110 */         (this.width < this.height) && (
/* 111 */         (strs[1].toUpperCase().equals("H")) || 
/* 112 */         (strs[1].toUpperCase().equals("L")))) {
/* 113 */         int n = this.width;
/* 114 */         this.width = this.height;
/* 115 */         this.height = n;
/*     */       }
/* 117 */       if (strs.length > 2)
/* 118 */         setMargin(To.toInt(strs[2]));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set(DocPaper paper)
/*     */   {
/* 164 */     this.name = paper.name;
/* 165 */     this.width = paper.width;
/* 166 */     this.height = paper.height;
/* 167 */     this.topMargin = paper.topMargin;
/* 168 */     this.bottomMargin = paper.bottomMargin;
/* 169 */     this.leftMargin = paper.leftMargin;
/* 170 */     this.rightMargin = paper.rightMargin;
/*     */   }
/*     */   public int getBottomMargin() {
/* 173 */     return this.bottomMargin;
/*     */   }
/*     */   public int getLeftMargin() {
/* 176 */     return this.leftMargin;
/*     */   }
/*     */   public int getRightMargin() {
/* 179 */     return this.rightMargin;
/*     */   }
/*     */   public int getTopMargin() {
/* 182 */     return this.topMargin;
/*     */   }
/*     */   public void setBottomMargin(int bottomMargin) {
/* 185 */     this.bottomMargin = bottomMargin;
/*     */   }
/*     */   public void setLeftMargin(int leftMargin) {
/* 188 */     this.leftMargin = leftMargin;
/*     */   }
/*     */   public void setRightMargin(int rightMargin) {
/* 191 */     this.rightMargin = rightMargin;
/*     */   }
/*     */   public void setTopMargin(int topMargin) {
/* 194 */     this.topMargin = topMargin;
/*     */   }
/*     */   public Element toEle() {
/* 197 */     Element paper = DocumentHelper.createElement("paper");
/* 198 */     if (this.width != (int)(ISO.A4.getX(25400) * 96.0F)) {
/* 199 */       paper.addAttribute("width", String.valueOf(this.width));
/*     */     }
/* 201 */     if (this.height != (int)(ISO.A4.getY(25400) * 96.0F)) {
/* 202 */       paper.addAttribute("height", String.valueOf(this.height));
/*     */     }
/* 204 */     if (this.topMargin != 96) {
/* 205 */       paper.addAttribute("topMargin", String.valueOf(this.topMargin));
/*     */     }
/* 207 */     if (this.leftMargin != 96) {
/* 208 */       paper.addAttribute("leftMargin", String.valueOf(this.leftMargin));
/*     */     }
/* 210 */     if (this.rightMargin != 96) {
/* 211 */       paper.addAttribute("rightMargin", String.valueOf(this.rightMargin));
/*     */     }
/* 213 */     if (this.bottomMargin != 96) {
/* 214 */       paper.addAttribute("bottomMargin", String.valueOf(this.bottomMargin));
/*     */     }
/* 216 */     if (paper.attributeCount() > 0) {
/* 217 */       return paper;
/*     */     }
/* 219 */     return null;
/*     */   }
/*     */ 
/*     */   public void setMargin(int margin) {
/* 223 */     this.topMargin = margin;
/* 224 */     this.bottomMargin = margin;
/* 225 */     this.leftMargin = margin;
/* 226 */     this.rightMargin = margin;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.DocPaper
 * JD-Core Version:    0.6.0
 */