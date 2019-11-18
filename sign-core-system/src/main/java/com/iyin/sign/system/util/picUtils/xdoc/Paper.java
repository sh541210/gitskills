/*     */ package com.iyin.sign.system.util.picUtils.xdoc;
/*     */ 
/*     */ public class Paper extends Component
/*     */ {
/*     */   public Paper()
/*     */   {
/*  26 */     super("paper");
/*  27 */     setSize(793, 1122);
/*     */   }
/*     */ 
/*     */   public void setSize(String size)
/*     */   {
/*  34 */     setAttribute("size", size);
/*     */   }
/*     */ 
/*     */   public String getSize(String size)
/*     */   {
/*  40 */     return getAttribute("size");
/*     */   }
/*     */ 
/*     */   public void setSize(int width, int height)
/*     */   {
/*  48 */     setWidth(width);
/*  49 */     setHeight(height);
/*     */   }
/*     */ 
/*     */   public void setWidth(int width)
/*     */   {
/*  56 */     setAttribute("width", width);
/*     */   }
/*     */ 
/*     */   public void setHeight(int height)
/*     */   {
/*  63 */     setAttribute("height", height);
/*     */   }
/*     */ 
/*     */   public void setMargin(int margin)
/*     */   {
/*  70 */     setLeftMargin(margin);
/*  71 */     setRightMargin(margin);
/*  72 */     setTopMargin(margin);
/*  73 */     setBottonMargin(margin);
/*     */   }
/*     */ 
/*     */   public void setLeftMargin(int margin)
/*     */   {
/*  80 */     setAttribute("leftMargin", margin);
/*     */   }
/*     */ 
/*     */   public void setRightMargin(int margin)
/*     */   {
/*  87 */     setAttribute("rightMargin", margin);
/*     */   }
/*     */ 
/*     */   public void setTopMargin(int margin)
/*     */   {
/*  94 */     setAttribute("topMargin", margin);
/*     */   }
/*     */ 
/*     */   public void setBottonMargin(int margin)
/*     */   {
/* 101 */     setAttribute("bottonMargin", margin);
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 108 */     return getAttribute("width", 24);
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 115 */     return getAttribute("height", 96);
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Paper
 * JD-Core Version:    0.6.0
 */