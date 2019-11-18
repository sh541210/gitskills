/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */

import java.awt.*;

/*    */
/*    */
/*    */

/*    */
/*    */ public class Gradient
/*    */ {
/*    */   public static Paint getPaint(Rectangle rect, int type, int trans, Color c1, Color c2)
/*    */   {
/* 24 */     Paint paint = null;
/* 25 */     if (type == 0) {
/* 26 */       if (trans == 0)
/* 27 */         paint = new GradientPaint(rect.y, rect.height + rect.y, c1, rect.width + rect.x, rect.height + rect.y, c2, true);
/* 28 */       else if (trans == 1)
/* 29 */         paint = new GradientPaint(rect.y, rect.height + rect.y, c2, rect.width + rect.x, rect.height + rect.y, c1, true);
/* 30 */       else if (trans == 2)
/* 31 */         paint = new GradientPaint(rect.y, rect.height / 2 + rect.y, c1, rect.width / 2 + rect.x, rect.height / 2 + rect.y, c2, true);
/* 32 */       else if (trans == 3)
/* 33 */         paint = new GradientPaint(rect.y, rect.height / 2 + rect.y, c2, rect.width / 2 + rect.x, rect.height / 2 + rect.y, c1, true);
/*    */     }
/* 35 */     else if (type == 1) {
/* 36 */       if (trans == 0)
/* 37 */         paint = new GradientPaint(rect.width / 2 + rect.x, rect.x, c1, rect.width / 2 + rect.x, rect.height + rect.y, c2, true);
/* 38 */       else if (trans == 1)
/* 39 */         paint = new GradientPaint(rect.width / 2 + rect.x, rect.x, c2, rect.width / 2 + rect.x, rect.height + rect.y, c1, true);
/* 40 */       else if (trans == 2)
/* 41 */         paint = new GradientPaint(rect.width / 2 + rect.x, rect.x, c1, rect.width / 2 + rect.x, rect.height / 2 + rect.y, c2, true);
/* 42 */       else if (trans == 3)
/* 43 */         paint = new GradientPaint(rect.width / 2 + rect.x, rect.x, c2, rect.width / 2 + rect.x, rect.height / 2 + rect.y, c1, true);
/*    */     }
/* 45 */     else if (type == 2) {
/* 46 */       if (trans == 0)
/* 47 */         paint = new GradientPaint(rect.x, rect.y, c1, rect.width + rect.x, rect.height + rect.y, c2, true);
/* 48 */       else if (trans == 1)
/* 49 */         paint = new GradientPaint(rect.x, rect.y, c2, rect.width + rect.x, rect.height + rect.y, c1, true);
/* 50 */       else if (trans == 2)
/* 51 */         paint = new GradientPaint(rect.x, rect.y, c1, rect.width / 2 + rect.x, rect.height / 2 + rect.y, c2, true);
/* 52 */       else if (trans == 3)
/* 53 */         paint = new GradientPaint(rect.x, rect.y, c2, rect.width / 2 + rect.x, rect.height / 2 + rect.y, c1, true);
/*    */     }
/* 55 */     else if (type == 3) {
/* 56 */       if (trans == 0)
/* 57 */         paint = new GradientPaint(rect.y, rect.height + rect.y, c1, rect.width + rect.x, 0.0F, c2, true);
/* 58 */       else if (trans == 1)
/* 59 */         paint = new GradientPaint(rect.y, rect.height + rect.y, c2, rect.width + rect.x, 0.0F, c1, true);
/* 60 */       else if (trans == 2)
/* 61 */         paint = new GradientPaint(rect.y, rect.height + rect.y, c1, rect.width / 2 + rect.x, rect.height / 2 + rect.y, c2, true);
/* 62 */       else if (trans == 3) {
/* 63 */         paint = new GradientPaint(rect.y, rect.height + rect.y, c2, rect.width / 2 + rect.x, rect.height / 2 + rect.y, c1, true);
/*    */       }
/*    */     }
/* 66 */     if (paint == null) {
/* 67 */       paint = c1;
/*    */     }
/* 69 */     return paint;
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.Gradient
 * JD-Core Version:    0.6.0
 */