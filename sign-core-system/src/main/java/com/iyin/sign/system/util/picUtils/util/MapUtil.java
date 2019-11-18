/*     */ package com.iyin.sign.system.util.picUtils.util;
/*     */ 
/*     */

import java.util.Iterator;
import java.util.Map;

/*     */
/*     */
/*     */

/*     */
/*     */ public class MapUtil
/*     */ {
/*     */   public static Object getObj(Map map, String key, Object defVal)
/*     */   {
/*  34 */     Object val = map.get(key);
/*  35 */     if (val != null) {
/*  36 */       return val;
/*     */     }
/*  38 */     return defVal;
/*     */   }
/*     */ 
/*     */   public static String getString(Map map, String key, String defVal)
/*     */   {
/*  49 */     return To.objToString(getObj(map, key, defVal));
/*     */   }
/*     */ 
/*     */   public static String getString(Map map, String key)
/*     */   {
/*  59 */     return To.objToString(getObj(map, key, ""));
/*     */   }
/*     */ 
/*     */   public static int getInt(Map map, String key, int defVal)
/*     */   {
/*  69 */     return To.toInt(getObj(map, key, new Integer(defVal)));
/*     */   }
/*     */ 
/*     */   public static double getDouble(Map map, String key, double defVal)
/*     */   {
/*  79 */     return To.toDouble(getObj(map, key, new Double(defVal)));
/*     */   }
/*     */ 
/*     */   public static boolean getBool(Map map, String key, boolean defVal)
/*     */   {
/*  89 */     return To.toBool(getObj(map, key, Boolean.valueOf(defVal)));
/*     */   }
/*     */ 
/*     */   public static String igKey(Map map, String key) {
/* 105 */     if (!map.containsKey(key)) {
/* 107 */       Iterator it = map.keySet().iterator();
/*     */ 
/* 109 */       while (it.hasNext()) {
/* 110 */         String mkey = it.next().toString();
/* 111 */         if (mkey.equalsIgnoreCase(key)) {
/* 112 */           key = mkey;
/* 113 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 118 */     return key;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.util.MapUtil
 * JD-Core Version:    0.6.0
 */