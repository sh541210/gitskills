/*    */ package com.iyin.sign.system.util.picUtils.data;
/*    */ 
/*    */ public class IntArray extends Array
/*    */ {
/*    */   public IntArray()
/*    */   {
/* 23 */     super(4);
/*    */   }
/*    */   public void add(int n) {
/* 26 */     super.addObj(new Integer(n));
/*    */   }
/*    */   public void set(int index, int n) {
/* 29 */     super.setObj(index, new Integer(n));
/*    */   }
/*    */   public int get(int index) {
/* 32 */     return ((Integer)super.getObj(index)).intValue();
/*    */   }
/*    */   public void addAll(IntArray ary) {
/* 35 */     super.addAllObj(ary);
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.data.IntArray
 * JD-Core Version:    0.6.0
 */