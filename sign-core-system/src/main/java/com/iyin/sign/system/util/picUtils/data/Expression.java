/*     */ package com.iyin.sign.system.util.picUtils.data;
/*     */ 
/*     */


import com.iyin.sign.system.util.picUtils.util.HgException;

import java.util.ArrayList;

/*     */

/*     */
/*     */ public class Expression
/*     */ {
/*     */   protected static final String INNER_SQL_TAG = "#";
/*     */   public Object data;
/*  37 */   public int dataStyle = 1;
/*     */ 
/*  41 */   public int resultDataType = 12;
/*     */ 
/*  45 */   public int resultDataStyle = 3;
/*     */   public Object result;
/*     */   public Object parseRes;
/*  57 */   protected boolean parsed = false;
/*     */ 
/*  61 */   protected String expStr = "";
/*     */   public BlkExpression blkExp;
/*  82 */   public boolean breaked = false;
/*     */ 
/*     */   public String getExpStr()
/*     */   {
/*  66 */     return this.expStr;
/*     */   }
/*     */ 
/*     */   public Expression(BlkExpression blkExp, String str)
/*     */   {
/*  72 */     this.blkExp = blkExp;
/*  73 */     this.expStr = str;
/*     */   }
/*     */ 
/*     */   public void parse()
/*     */     throws HgException, HgException
/*     */   {
/*  80 */     this.parsed = true;
/*     */   }
/*     */ 
/*     */   public ArrayList getChildExpList(Conn conn) throws HgException {
/*  84 */     return new ArrayList();
/*     */   }
/*     */   public String getExpPath(Conn conn, Expression exp) throws HgException {
/*  87 */     ArrayList list = getChildExpList(conn);
/*     */ 
/*  89 */     String path = null;
/*  90 */     for (int i = 0; i < list.size(); i++) {
/*  91 */       Expression curExp = (Expression)list.get(i);
/*  92 */       if (curExp.equals(exp)) {
/*  93 */         path = String.valueOf(i + 1);
/*  94 */         break;
/*     */       }
/*  96 */       path = curExp.getExpPath(conn, exp);
/*  97 */       if (path != null) {
/*  98 */         path = i + 1 + "," + path;
/*  99 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 103 */     return path;
/*     */   }
/*     */ 
/*     */   public void eval(Conn conn)
/*     */     throws HgException
/*     */   {
/* 109 */     if (!this.parsed) parse();
/*     */   }
/*     */ 
/*     */   public void fillVarList(ArrayList list)
/*     */     throws HgException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.data.Expression
 * JD-Core Version:    0.6.0
 */