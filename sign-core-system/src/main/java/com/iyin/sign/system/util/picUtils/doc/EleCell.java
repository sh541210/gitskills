/*     */ package com.iyin.sign.system.util.picUtils.doc;



import com.iyin.sign.system.util.picUtils.data.RowSet;
import com.iyin.sign.system.util.picUtils.util.HgException;
import com.iyin.sign.system.util.picUtils.util.MapUtil;

import java.awt.*;
import java.util.HashMap;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class EleCell extends EleRect
/*     */ {
/*     */   private static final String INNER_RECT = "$innerRect$";
/*  26 */   private EleRect innerRect = null;
/*     */   public int rowCount;
/*     */   public String data;
/*     */   public String connName;
/*     */   public String sql;
/*     */   public int direction;
/*     */   public Object[][] resCells;
/* 173 */   public int i = 0;
/*     */   public RowSet[][] rowSets;
/* 184 */   public int rowIndex = 0;
/* 185 */   public int colIndex = 0;
/*     */   public boolean bSelect;
/*     */   public EleCell belong;
/*     */ 
/*     */   public EleCell(XDoc xdoc, HashMap attMap)
/*     */   {
/*  28 */     super(xdoc, attMap);
/*  29 */     if (attMap.containsKey("$innerRect$"))
/*     */       try {
/*  31 */         setRect((EleRect)EleBase.deepClone(xdoc, (EleRect)attMap.get("$innerRect$"))); } catch (Exception localException) {
/*     */       }
/*     */   }
/*     */ 
/*     */   public void autoSize() {
/*  36 */     if (this.innerRect == null) {
/*  37 */       super.autoSize();
/*     */     } else {
/*  39 */       this.innerRect.autoSize();
/*  40 */       this.width = this.innerRect.width;
/*  41 */       this.height = this.innerRect.height;
/*     */     }
/*     */   }
/*     */ 
/*     */   public EleCell(XDoc xdoc) {
/*  45 */     super(xdoc);
/*     */   }
/*     */   public EleCell(EleRect innerRect) {
/*  48 */     super(innerRect.xdoc);
/*  49 */     if ((innerRect instanceof EleCell)) {
/*  50 */       setAttMap(innerRect.getAttMap());
/*  51 */       this.eleList.addAll(innerRect.eleList);
/*     */     } else {
/*  53 */       this.innerRect = innerRect;
/*  54 */       this.typeName = innerRect.typeName;
/*  55 */       this.row = innerRect.row;
/*  56 */       this.rowSpan = innerRect.rowSpan;
/*  57 */       this.col = innerRect.col;
/*  58 */       this.colSpan = innerRect.colSpan;
/*  59 */       this.width = innerRect.width;
/*  60 */       this.height = innerRect.height;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRect(EleRect rect) {
/*  64 */     if (rect != this)
/*  65 */       if ((rect instanceof EleCell)) {
/*  66 */         this.typeName = "cell";
/*  67 */         this.innerRect = null;
/*  68 */         setAttMap(rect.getAttMap());
/*  69 */         this.eleList.clear();
/*  70 */         this.eleList.addAll(rect.eleList);
/*     */       } else {
/*  72 */         this.typeName = rect.typeName;
/*  73 */         this.innerRect = rect;
/*  74 */         this.eleList.clear();
/*  75 */         rect.left = 0;
/*  76 */         rect.top = 0;
/*  77 */         rect.width = this.width;
/*  78 */         rect.height = this.height;
/*  79 */         rect.col = this.col;
/*  80 */         rect.colSpan = this.colSpan;
/*  81 */         rect.row = this.row;
/*  82 */         rect.rowSpan = this.rowSpan;
/*     */       }
/*     */   }
/*     */ 
/*     */   public EleRect getRect() {
/*  87 */     if (this.innerRect == null) {
/*  88 */       return this;
/*     */     }
/*  90 */     this.innerRect.col = this.col;
/*  91 */     this.innerRect.row = this.row;
/*  92 */     this.innerRect.colSpan = this.colSpan;
/*  93 */     this.innerRect.rowSpan = this.rowSpan;
/*  94 */     return this.innerRect;
/*     */   }
/*     */ 
/*     */   public void setBounds(int x, int y, int w, int h) {
/*  98 */     this.left = x;
/*  99 */     this.top = y;
/* 100 */     this.width = w;
/* 101 */     this.height = h;
/* 102 */     if (this.innerRect != null) {
/* 103 */       this.innerRect.left = x;
/* 104 */       this.innerRect.top = y;
/* 105 */       this.innerRect.width = w;
/* 106 */       this.innerRect.height = h;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void init() {
/* 111 */     super.init();
/* 112 */     this.typeName = "cell";
/* 113 */     this.rowSpan = 1;
/* 114 */     this.colSpan = 1;
/* 115 */     this.sql = "";
/* 116 */     this.connName = "";
/* 117 */     this.data = "";
/* 118 */     this.rowCount = 0;
/*     */   }
/*     */ 
/*     */   public void setAttMap(HashMap map)
/*     */   {
/* 131 */     if (this.innerRect == null) {
/* 132 */       super.setAttMap(map);
/* 133 */       this.connName = MapUtil.getString(map, "conn", this.connName);
/* 134 */       this.sql = MapUtil.getString(map, "sql", this.sql);
/* 135 */       this.data = MapUtil.getString(map, "data", this.data);
/* 136 */       this.rowCount = MapUtil.getInt(map, "rowCount", this.rowCount);
/*     */     } else {
/* 138 */       this.innerRect.setAttMap(map);
/*     */     }
/*     */   }
/*     */ 
/*     */   public HashMap getAttMap()
/*     */   {
/*     */     HashMap map;
/* 144 */     if (this.innerRect == null) {
/* 145 */       map = super.getAttMap();
/* 146 */       map.put("conn", this.connName);
/* 147 */       map.put("sql", this.sql);
/* 148 */       map.put("data", this.data);
/* 149 */       map.put("rowCount", String.valueOf(this.rowCount));
/*     */     } else {
/* 151 */       this.innerRect.row = this.row;
/* 152 */       this.innerRect.rowSpan = this.rowSpan;
/* 153 */       this.innerRect.col = this.col;
/* 154 */       this.innerRect.colSpan = this.colSpan;
/* 155 */       map = this.innerRect.getAttMap();
/* 156 */       map.put("$innerRect$", this.innerRect);
/*     */     }
/* 158 */     return map;
/*     */   }
/*     */   public Object clone() {
/* 161 */     if (this.innerRect != null) {
/* 162 */       this.innerRect.row = this.row;
/* 163 */       this.innerRect.col = this.col;
/*     */       try {
/* 165 */         return new EleCell((EleRect)EleBase.deepClone(this.innerRect.xdoc, this.innerRect));
/*     */       } catch (HgException e) {
/* 167 */         return new EleCell((EleRect)this.innerRect.clone());
/*     */       }
/*     */     }
/* 170 */     return new EleCell(this.xdoc, getAttMap());
/*     */   }
/*     */ 
/*     */   public void relayout()
/*     */   {
/* 179 */     getRect().lineList = null;
/*     */   }
/*     */   protected EleBase copyEle(XDoc xdoc) {
/* 182 */     return new EleCell(xdoc, getAttMap());
/*     */   }
/*     */ 
/*     */   public void print(Graphics2D g)
/*     */   {
/* 189 */     if (this.innerRect == null) {
/* 190 */       super.print(g);
/*     */     } else {
/* 192 */       this.innerRect.width = this.width;
/* 193 */       this.innerRect.height = this.height;
/* 194 */       this.innerRect.print(g);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 198 */     return this.innerRect == null ? super.toString() : this.innerRect.toString();
/*     */   }
/*     */   public boolean isLoop() {
/* 201 */     return (this.sql.length() > 0) || (this.data.length() > 0);
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.EleCell
 * JD-Core Version:    0.6.0
 */