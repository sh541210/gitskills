/*    */ package com.iyin.sign.system.util.picUtils.xdoc;
/*    */ 
/*    */ public class Arc extends Rect
/*    */ {
/* 31 */   public static String TYPE_OPEN = "open";
/*    */ 
/* 35 */   public static String TYPE_CHORD = "chord";
/*    */ 
/* 39 */   public static String TYPE_PIE = "pie";
/*    */ 
/* 43 */   public static String TYPE_RING = "ring";
/*    */ 
/*    */   public Arc()
/*    */   {
/* 26 */     super("arc");
/*    */   }
/*    */ 
/*    */   public void setType(String type)
/*    */   {
/* 49 */     setAttribute("type", type);
/*    */   }
/*    */ 
/*    */   public void setAngle(int start, int extent)
/*    */   {
/* 57 */     setAttribute("start", start);
/* 58 */     setAttribute("extent", extent);
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Arc
 * JD-Core Version:    0.6.0
 */