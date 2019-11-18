/*    */ package com.iyin.sign.system.util.picUtils.xdoc;
/*    */ 
/*    */ public class Polygon extends Rect
/*    */ {
/*    */   public Polygon(int n)
/*    */   {
/* 27 */     super("polygon");
/* 28 */     setPoints(n);
/*    */   }
/*    */ 
/*    */   public void setPoints(int points)
/*    */   {
/* 35 */     setAttribute("points", points);
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Polygon
 * JD-Core Version:    0.6.0
 */