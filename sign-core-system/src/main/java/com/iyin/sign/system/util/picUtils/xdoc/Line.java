/*     */ package com.iyin.sign.system.util.picUtils.xdoc;
/*     */ 
/*     */ public class Line extends Rect
/*     */ {
/*     */   public static final String ARROW_LINE = "none";
/*     */   public static final String ARROW_BIAS = "bias";
/*     */   public static final String ARROW_TRIANGLE = "triangle";
/*     */   public static final String ARROW_CIRCLE = "circle";
/*     */   public static final String ARROW_RHOMBUS = "rhombus";
/*     */   public static final String ARROW_RECT = "rect";
/*  83 */   public static String LINE_STYLE_LINE = "line";
/*     */ 
/*  87 */   public static String LINE_STYLE_BROKEN = "broken";
/*     */ 
/*  91 */   public static String LINE_STYLE_ARC = "arc";
/*     */ 
/*  95 */   public static String LINE_STYLE_ARC2 = "arc2";
/*     */ 
/*     */   public Line(int startX, int startY, int endX, int endY)
/*     */   {
/*  30 */     super("line");
/*  31 */     setStart(startX, startY);
/*  32 */     setEnd(endX, endY);
/*     */   }
/*     */   private void setEnd(int endX, int endY) {
/*  35 */     setAttribute("endX", String.valueOf(endX));
/*  36 */     setAttribute("endY", String.valueOf(endY));
/*     */   }
/*     */   private void setStart(int startX, int startY) {
/*  39 */     setAttribute("startX", String.valueOf(startX));
/*  40 */     setAttribute("startY", String.valueOf(startY));
/*     */   }
/*     */ 
/*     */   public void setStartArrow(int arrow)
/*     */   {
/*  71 */     setAttribute("startArrow", arrow);
/*     */   }
/*     */ 
/*     */   public void setEndArrow(int arrow)
/*     */   {
/*  78 */     setAttribute("endArrow", arrow);
/*     */   }
/*     */ 
/*     */   public void setLineStyle(int style)
/*     */   {
/* 101 */     setAttribute("lineStyle", style);
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Line
 * JD-Core Version:    0.6.0
 */