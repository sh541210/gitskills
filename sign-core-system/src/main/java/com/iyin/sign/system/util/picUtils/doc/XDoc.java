/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */
import com.iyin.sign.system.util.picUtils.util.MapUtil;
import com.iyin.sign.system.util.picUtils.util.StrUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class XDoc
/*     */ {
/*     */   public static final String VIEW_TEXT = "text";
/*     */   public static final String VIEW_TABLE = "table";
/*     */   public static final String VIEW_PAGE = "page";
/*     */   public static final String VIEW_WEB = "web";
/*     */   public DocPrint print;
/*     */   public String url;
/*  67 */   public double scale = 1.0D;
/*     */ 
/*  77 */   public HashMap metaMap = new HashMap();
/*     */ 
/*  81 */   public ArrayList paraList = new ArrayList();
/*     */ 
/*  85 */   public ArrayList rectList = new ArrayList();
/*     */   public EleRect backRect;
/*     */   public EleRect frontRect;
/*     */   public EleRect bodyRect;
/*     */   private DocPaper paper;
/* 103 */   public int pages = 1;
/* 104 */   public int page = 1;
/*     */   public String heading;
/* 106 */   public ArrayList heads = new ArrayList();
/*     */   public Date createTime;
/* 112 */   public boolean runHead = false;
/* 113 */   public boolean runInput = false;
/*     */ 
/* 120 */   private Meta meta = new Meta(this);
/*     */ 
/*     */   public XDoc()
/*     */   {
/*  30 */     this("");
/*     */   }
/*     */   public XDoc(String url) {
/*  33 */     this.url = url;
/*  34 */     this.paper = new DocPaper(this);
/*  35 */     this.backRect = new EleRect(this);
/*  36 */     this.backRect.color = null;
/*  37 */     this.frontRect = new EleRect(this);
/*  38 */     this.frontRect.color = null;
/*  39 */     this.bodyRect = new EleRect(this);
/*  40 */     this.bodyRect.color = null;
/*  41 */     this.bodyRect.txtPadding = 0;
/*  42 */     this.bodyRect.strokeWidth = 0.0D;
/*  43 */     this.metaMap.put("id", StrUtil.uuid());
/*     */   }
/*     */ 
/*     */   public String getView()
/*     */   {
/*  50 */     String view = getMeta("view");
/*  51 */     if (view.length() == 0) {
/*  52 */       view = "text";
/*     */     }
/*  54 */     return view;
/*     */   }
/*     */   public void setView(String view) {
/*  57 */     this.metaMap.put("view", view);
/*     */   }
/*     */ 
/*     */   public int intScale(int n)
/*     */   {
/*  69 */     return (int)(n * this.scale);
/*     */   }
/*     */   public int unintScale(int n) {
/*  72 */     return (int)(n / this.scale);
/*     */   }
/*     */ 
/*     */   public DocPaper getPaper()
/*     */   {
/*  91 */     return this.paper;
/*     */   }
/*     */   public void setPaper(DocPaper docPaper) {
/*  94 */     this.paper.set(docPaper);
/*  95 */     if ((docPaper.width == 0) && (docPaper.height == 0))
/*  96 */       this.bodyRect.sizeType = "autosize";
/*  97 */     else if (docPaper.width == 0)
/*  98 */       this.bodyRect.sizeType = "autowidth";
/*  99 */     else if (docPaper.height == 0)
/* 100 */       this.bodyRect.sizeType = "autoheight";
/*     */   }
/*     */ 
/*     */   public String getMeta(String name, String defValue)
/*     */   {
/* 115 */     return MapUtil.getString(this.metaMap, MapUtil.igKey(this.metaMap, name), defValue);
/*     */   }
/*     */   public String getMeta(String name) {
/* 118 */     return getMeta(name, "");
/*     */   }
/*     */ 
/*     */   public Meta getMeta() {
/* 122 */     return this.meta;
/*     */   }
/*     */   public int getViewPage() {
/* 125 */     return MapUtil.getInt(this.metaMap, "page", this.page);
/*     */   }
/*     */   public int getViewPages() {
/* 128 */     return MapUtil.getInt(this.metaMap, "pages", this.pages);
/*     */   }
/*     */   public String getViewHeading() {
/* 131 */     return MapUtil.getString(this.metaMap, "heading", this.heading);
/*     */   }
/*     */   public boolean isPrintBack() {
/* 134 */     return !getMeta("printBack").equals("false");
/*     */   }
/*     */   public String getName() {
/* 137 */     return getMeta("title");
/*     */   }
/*     */   public void genId() {
/* 140 */     this.metaMap.put("id", StrUtil.uuid());
/*     */   }
/*     */   public boolean hasContent() {
/* 143 */     if ((this.rectList.size() > 0) || (this.paraList.size() > 1)) {
/* 144 */       return true;
/*     */     }
/* 146 */     if (this.paraList.size() == 0) {
/* 147 */       return false;
/*     */     }
/* 149 */     ElePara para = (ElePara)this.paraList.get(0);
/* 150 */     if (para.eleList.size() > 1)
/* 151 */       return true;
/* 152 */     if (para.eleList.size() == 0)
/* 153 */       return false;
/* 154 */     if ((para.eleList.get(0) instanceof EleText)) {
/* 155 */       return ((EleText)para.eleList.get(0)).text.length() > 0;
/*     */     }
/* 157 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.XDoc
 * JD-Core Version:    0.6.0
 */