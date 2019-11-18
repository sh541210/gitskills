/*     */ package com.iyin.sign.system.util.picUtils.data;
/*     */ 
/*     */

import com.iyin.sign.system.util.picUtils.util.HgException;
import com.iyin.sign.system.util.picUtils.util.To;

import java.util.Date;

/*     */
/*     */

/*     */
/*     */ public class ObjectUtil
/*     */ {
/*     */   public static int compare(Object obj1, Object obj2, int dataType)
/*     */     throws HgException
/*     */   {
/*  37 */     if ((dataType == 4) || (dataType == -5) || (dataType == 8))
/*     */     {
/*  39 */       double val1 = To.toDouble(obj1);
/*  40 */       double val2 = To.toDouble(obj2);
/*  41 */       if (val1 > val2)
/*  42 */         return 1;
/*  43 */       if (val1 < val2) {
/*  44 */         return -1;
/*     */       }
/*  46 */       return 0;
/*     */     }
/*     */ 
/*  49 */     if (obj1 == null) obj1 = "";
/*  50 */     if (obj2 == null) obj2 = "";
/*  51 */     return To.objToString(obj1).compareTo(To.objToString(obj2));
/*     */   }
/*     */ 
/*     */   public static Object add(Object obj1, Object obj2, int dataType)
/*     */   {
/*  63 */     if ((dataType == 4) || (dataType == -5)) {
/*  64 */       return new Long(To.toLong(obj1) + To.toLong(obj2));
/*     */     }
/*  66 */     return new Double(To.toDouble(obj1) + To.toDouble(obj2));
/*     */   }
/*     */ 
/*     */   public static Object div(Object obj1, Object obj2)
/*     */     throws HgException
/*     */   {
/*  78 */     return new Double(To.toDouble(obj1) / To.toDouble(obj2));
/*     */   }
/*     */ 
/*     */   public static Object avg(Object obj1, Object obj2)
/*     */   {
/*  88 */     return new Double((To.toDouble(obj1) + To.toDouble(obj2)) / 2.0D);
/*     */   }
/*     */ 
/*     */   public static Object max(Object obj1, Object obj2, int dataType)
/*     */     throws HgException
/*     */   {
/*  99 */     if (compare(obj1, obj2, dataType) > 0) {
/* 100 */       return obj1;
/*     */     }
/* 102 */     return obj2;
/*     */   }
/*     */ 
/*     */   public static Object min(Object obj1, Object obj2, int dataType)
/*     */     throws HgException
/*     */   {
/* 114 */     if (compare(obj1, obj2, dataType) < 0) {
/* 115 */       return obj1;
/*     */     }
/* 117 */     return obj2;
/*     */   }
/*     */ 
/*     */   public static int type(Object obj)
/*     */   {
/* 126 */     if ((obj instanceof Boolean))
/* 127 */       return 16;
/* 128 */     if ((obj instanceof Integer))
/* 129 */       return 4;
/* 130 */     if ((obj instanceof Long))
/* 131 */       return -5;
/* 132 */     if ((obj instanceof Double))
/* 133 */       return 8;
/* 134 */     if ((obj instanceof Date))
/* 135 */       return 91;
/* 136 */     if ((obj instanceof Row))
/* 137 */       return 9000;
/* 138 */     if ((obj instanceof RowSet)) {
/* 139 */       return 9001;
/*     */     }
/* 141 */     return 12;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.data.ObjectUtil
 * JD-Core Version:    0.6.0
 */