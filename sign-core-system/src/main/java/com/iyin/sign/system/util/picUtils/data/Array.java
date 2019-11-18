/*     */ package com.iyin.sign.system.util.picUtils.data;
/*     */ 
/*     */



import com.iyin.sign.system.util.picUtils.util.To;

import java.util.ArrayList;
import java.util.List;

/*     */
/*     */

/*     */
/*     */ public class Array
/*     */ {
/*  29 */   private int type = 12;
/*  30 */   private List data = new ArrayList();
/*     */ 
/*     */   public Array(int type)
/*     */   {
/*  36 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public Array()
/*     */   {
/*  43 */     this.type = 12;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  50 */     return this.type;
/*     */   }
/*     */   public Object getObj(int index) {
/*  53 */     return this.data.get(index);
/*     */   }
/*     */ 
/*     */   public Object addObj(Object obj)
/*     */   {
/*  62 */     return addObj(size(), obj);
/*     */   }
/*     */ 
/*     */   public Object addObj(int index, Object obj)
/*     */   {
/*  71 */     obj = validate(obj);
/*  72 */     this.data.add(index, obj);
/*  73 */     return obj;
/*     */   }
/*     */ 
/*     */   public void addAllObj(Array ary)
/*     */   {
/*  81 */     for (int i = 0; i < ary.size(); i++)
/*  82 */       addObj(ary.getObj(i));
/*     */   }
/*     */ 
/*     */   public void setObj(int index, Object obj) {
/*  86 */     this.data.set(index, validate(obj));
/*     */   }
/*     */ 
/*     */   private Object validate(Object obj)
/*     */   {
/*  94 */     return To.toObj(obj, this.type);
/*     */   }
/*     */ 
/*     */   public void remove(int index) {
/*  98 */     this.data.remove(index);
/*     */   }
/*     */   public void clear() {
/* 101 */     this.data.clear();
/*     */   }
/*     */   public int size() {
/* 104 */     return this.data.size();
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.data.Array
 * JD-Core Version:    0.6.0
 */