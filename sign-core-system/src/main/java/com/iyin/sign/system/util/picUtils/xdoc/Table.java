/*     */ package com.iyin.sign.system.util.picUtils.xdoc;
/*     */ 
/*     */ import java.util.ArrayList;

/*     */
/*     */ public class Table extends Rect
/*     */ {
/*     */   private static final int DEF_COL_WIDTH = 96;
/*     */   private static final int DEF_ROW_HEIGHT = 24;
/*  26 */   private ArrayList rowList = new ArrayList();
/*  27 */   private ArrayList colList = new ArrayList();
/*     */ 
/*     */   public Table(int rows, int cols)
/*     */   {
/*  32 */     super("table");
/*  33 */     ensureSize(this.rowList, rows, 24);
/*  34 */     ensureSize(this.colList, cols, 96);
/*  35 */     setAttribute("rows", join(this.rowList));
/*  36 */     calHeight();
/*  37 */     setAttribute("cols", join(this.colList));
/*  38 */     calWidth();
/*     */   }
/*     */ 
/*     */   public void setRowHeight(int row, int height)
/*     */   {
/*  45 */     ensureSize(this.rowList, row + 1, 24);
/*  46 */     this.rowList.set(row, String.valueOf(height));
/*  47 */     setAttribute("rows", join(this.rowList));
/*  48 */     calHeight();
/*     */   }
/*     */   private void ensureSize(ArrayList list, int size, int n) {
/*  51 */     if (list.size() < size)
/*  52 */       for (int i = list.size(); i < size; i++)
/*  53 */         list.add(String.valueOf(n));
/*     */   }
/*     */ 
/*     */   public void setColWidth(int col, int width)
/*     */   {
/*  62 */     ensureSize(this.colList, col + 1, 96);
/*  63 */     this.colList.set(col, String.valueOf(width));
/*  64 */     setAttribute("cols", join(this.colList));
/*  65 */     calWidth();
/*     */   }
/*     */ 
/*     */   public void setHeader(int header)
/*     */   {
/*  72 */     setAttribute("header", header);
/*     */   }
/*     */ 
/*     */   public void setFooter(int footer)
/*     */   {
/*  79 */     setAttribute("footer", footer);
/*     */   }
/*     */   private void calWidth() {
/*  82 */     int n = 0;
/*  83 */     for (int i = 0; i < this.colList.size(); i++) {
/*  84 */       n += toInt(String.valueOf(this.colList.get(i)), 96);
/*     */     }
/*  86 */     setWidth(n);
/*     */   }
/*     */   private int toInt(String str, int def) {
/*     */     try {
/*  90 */       return Integer.parseInt(str); } catch (Exception e) {
/*     */     }
/*  92 */     return def;
/*     */   }
/*     */ 
/*     */   private void calHeight() {
/*  96 */     int n = 0;
/*  97 */     for (int i = 0; i < this.rowList.size(); i++) {
/*  98 */       n += toInt(String.valueOf(this.rowList.get(i)), 24);
/*     */     }
/* 100 */     setHeight(n);
/*     */   }
/*     */   private String join(ArrayList ns) {
/* 103 */     StringBuffer sb = new StringBuffer();
/* 104 */     for (int i = 0; i < ns.size(); i++) {
/* 105 */       if (i > 0) {
/* 106 */         sb.append(",");
/*     */       }
/* 108 */       sb.append(ns.get(i));
/*     */     }
/* 110 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public void add(Component comp, int row, int col)
/*     */   {
/* 119 */     add(comp, row, col, 1, 1);
/*     */   }
/*     */ 
/*     */   public void add(Component comp, int row, int col, int rowSpan, int colSpan)
/*     */   {
/* 130 */     if (((comp instanceof Text)) || ((comp instanceof Para))) {
/* 131 */       Rect rect = new Rect();
/* 132 */       rect.add(comp);
/* 133 */       comp = rect;
/*     */     }
/* 135 */     comp.setAttribute("row", row + 1);
/* 136 */     comp.setAttribute("col", col + 1);
/* 137 */     comp.setAttribute("rowSpan", rowSpan);
/* 138 */     comp.setAttribute("colSpan", colSpan);
/* 139 */     super.add(comp);
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Table
 * JD-Core Version:    0.6.0
 */