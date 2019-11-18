/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */



import com.iyin.sign.system.util.picUtils.util.StrUtil;

import java.util.ArrayList;

/*    */

/*    */
/*    */ public class Heading
/*    */ {
/*    */   public ElePara para;
/*    */   public int page;
/*    */   public int x;
/*    */   public int y;
/*    */   public ArrayList cheads;
/*    */ 
/*    */   public Heading(ElePara para)
/*    */   {
/* 24 */     this.para = para;
/*    */   }
/*    */   public String name() {
/* 27 */     String str = this.para.toString().trim();
/* 28 */     if (str.indexOf('\n') >= 0) {
/* 29 */       str = StrUtil.replaceAll(str, "\n", "");
/*    */     }
/* 31 */     if (str.indexOf('\r') >= 0) {
/* 32 */       str = StrUtil.replaceAll(str, "\r", "");
/*    */     }
/* 34 */     return str;
/*    */   }
/*    */   public int level() {
/* 37 */     return this.para.heading;
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.Heading
 * JD-Core Version:    0.6.0
 */