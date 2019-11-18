/*    */ package com.iyin.sign.system.util.picUtils.data;
/*    */ 
/*    */ public class Field
/*    */ {
/* 51 */   public String name = "";
/*    */ 
/* 55 */   public int type = 12;
/*    */ 
/* 59 */   public int length = 0;
/*    */ 
/* 63 */   public int decimal = 0;
/*    */ 
/* 67 */   public boolean notnull = false;
/* 68 */   public String seq = "";
/*    */ 
/*    */   public Field()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Field(String name)
/*    */   {
/* 37 */     this(name, 12);
/*    */   }
/*    */ 
/*    */   public Field(String name, int type)
/*    */   {
/* 45 */     this.name = name;
/* 46 */     this.type = type;
/*    */   }
/*    */ 
/*    */   public String getShortName()
/*    */   {
/* 70 */     String sName = this.name;
/* 71 */     if (this.name.indexOf(".") >= 0) {
/* 72 */       sName = this.name.substring(this.name.lastIndexOf(".") + 1);
/*    */     }
/* 74 */     return sName;
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.data.Field
 * JD-Core Version:    0.6.0
 */