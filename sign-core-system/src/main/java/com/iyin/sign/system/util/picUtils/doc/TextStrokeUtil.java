/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */

import java.awt.*;

/*    */
/*    */

/*    */
/*    */ public class TextStrokeUtil
/*    */ {
/*    */   public static Stroke createStroke(String text, Font font, boolean stretchToFit, boolean repeat)
/*    */   {
/* 28 */     return new TextStroke(text, font, stretchToFit, repeat);
/*    */   }
/*    */   public static Stroke createStroke(Shape shape, float advance) {
/* 31 */     return new TextStroke(shape, advance);
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.TextStrokeUtil
 * JD-Core Version:    0.6.0
 */