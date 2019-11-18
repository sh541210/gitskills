/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */

import java.awt.*;
import java.util.HashMap;

/*    */

/*    */
/*    */ public class EleSpace extends EleRect
/*    */ {
/*    */   public EleSpace(XDoc xdoc, int width, int height)
/*    */   {
/* 22 */     super(xdoc);
/* 23 */     this.width = width;
/* 24 */     this.height = height;
/*    */   }
/*    */ 
/*    */   public EleSpace(XDoc xdoc, HashMap attMap)
/*    */   {
/* 31 */     super(xdoc, attMap);
/*    */   }
/*    */   protected void init() {
/* 34 */     super.init();
/* 35 */     this.typeName = "space";
/* 36 */     this.name = "";
/* 37 */     this.color = null;
/* 38 */     this.fillColor = null;
/* 39 */     this.width = 1;
/* 40 */     this.height = 1;
/*    */   }
/*    */   public void print(Graphics2D g) {
/*    */   }
/* 44 */   public void setSize(int width, int height) { this.width = width;
/* 45 */     this.height = height;
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.EleSpace
 * JD-Core Version:    0.6.0
 */