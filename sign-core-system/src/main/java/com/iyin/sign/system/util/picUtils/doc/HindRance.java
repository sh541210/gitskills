/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */

import java.awt.*;
import java.util.ArrayList;

/*    */

/*    */
/*    */ public class HindRance extends Rectangle
/*    */ {
/*    */   public HindRance(Rectangle rect)
/*    */   {
/* 23 */     super(rect);
/*    */   }
/*    */   public static boolean intersects(ArrayList hrList, Rectangle r) {
/* 26 */     return false;
/*    */   }
/*    */   public static HindRance next(ArrayList hrList, int x, int y, int h) {
/* 29 */     HindRance hr = null;
/* 30 */     for (int i = 0; i < hrList.size(); i++) {
/* 31 */       HindRance hr2 = (HindRance)hrList.get(i);
/* 32 */       if ((hr2.x >= x) && (hr2.y < y + h) && (hr2.y + hr2.height > y) && ((hr == null) || (hr.x > hr2.x))) {
/* 33 */         hr = hr2;
/*    */       }
/* 35 */       if (hr2.y + hr2.height < y) {
/* 36 */         hrList.remove(i);
/* 37 */         i--;
/*    */       }
/*    */     }
/* 40 */     return hr;
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.HindRance
 * JD-Core Version:    0.6.0
 */