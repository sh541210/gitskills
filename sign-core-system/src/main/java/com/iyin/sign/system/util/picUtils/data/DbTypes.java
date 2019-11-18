/*     */ package com.iyin.sign.system.util.picUtils.data;
/*     */ 
/*     */ import java.util.Date;

/*     */
/*     */ public class DbTypes
/*     */ {
/*     */   public static final int BIT = -7;
/*     */   public static final int TINYINT = -6;
/*     */   public static final int SMALLINT = 5;
/*     */   public static final int INTEGER = 4;
/*     */   public static final int BIGINT = -5;
/*     */   public static final int FLOAT = 6;
/*     */   public static final int REAL = 7;
/*     */   public static final int DOUBLE = 8;
/*     */   public static final int NUMERIC = 2;
/*     */   public static final int DECIMAL = 3;
/*     */   public static final int CHAR = 1;
/*     */   public static final int VARCHAR = 12;
/*     */   public static final int LONGVARCHAR = -1;
/*     */   public static final int DATE = 91;
/*     */   public static final int TIME = 92;
/*     */   public static final int TIMESTAMP = 93;
/*     */   public static final int BINARY = -2;
/*     */   public static final int VARBINARY = -3;
/*     */   public static final int LONGVARBINARY = -4;
/*     */   public static final int NULL = 0;
/*     */   public static final int OTHER = 1111;
/*     */   public static final int JAVA_OBJECT = 2000;
/*     */   public static final int DISTINCT = 2001;
/*     */   public static final int STRUCT = 2002;
/*     */   public static final int ARRAY = 2003;
/*     */   public static final int BLOB = 2004;
/*     */   public static final int CLOB = 2005;
/*     */   public static final int REF = 2006;
/*     */   public static final int DATALINK = 70;
/*     */   public static final int BOOLEAN = 16;
/*     */   public static final int ROW = 9000;
/*     */   public static final int ROWSET = 9001;
/*     */   public static final int INTARRAY = 9002;
/*     */ 
/*     */
/*     */   public static int getObjType(Object obj) {
/* 224 */     int type = 12;
/* 225 */     if ((obj instanceof String))
/* 226 */       type = 12;
/* 227 */     else if ((obj instanceof Date))
/* 228 */       type = 91;
/* 229 */     else if ((obj instanceof Long))
/* 230 */       type = -5;
/* 231 */     else if ((obj instanceof Integer))
/* 232 */       type = 4;
/* 233 */     else if ((obj instanceof Boolean))
/* 234 */       type = 16;
/* 235 */     else if ((obj instanceof RowSet))
/* 236 */       type = 9001;
/*     */     else {
/* 238 */       type = 1111;
/*     */     }
/* 240 */     return type;
/*     */   }
/*     */ }
