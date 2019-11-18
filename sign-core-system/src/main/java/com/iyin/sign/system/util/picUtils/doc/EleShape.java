/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */ import java.util.HashMap;

/*    */
/*    */ public class EleShape extends EleRect
/*    */ {
/*    */   public EleShape(XDoc xdoc)
/*    */   {
/* 21 */     super(xdoc);
/*    */   }
/*    */   public EleShape(XDoc xdoc, HashMap attMap) {
/* 24 */     super(xdoc, attMap);
/*    */   }
/*    */   protected void init() {
/* 27 */     super.init();
/* 28 */     this.line = "00000000";
/* 29 */     this.width = 120;
/* 30 */     this.height = 120;
/*    */   }
/*    */   protected boolean isLineShape() {
/* 33 */     return !this.line.equals("00000000");
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.EleShape
 * JD-Core Version:    0.6.0
 */