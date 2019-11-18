/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */

import java.awt.*;

/*    */
/*    */ public class NameShape
/*    */ {
/*    */   public Shape shape;
/*    */   public int page;
/*    */   public String name;
/*    */ 
/*    */   public NameShape(String name, Shape shape)
/*    */   {
/* 24 */     this(name, shape, 0);
/*    */   }
/*    */   public NameShape(String name, Shape shape, int page) {
/* 27 */     this.shape = shape;
/* 28 */     this.page = page;
/* 29 */     this.name = name;
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.NameShape
 * JD-Core Version:    0.6.0
 */