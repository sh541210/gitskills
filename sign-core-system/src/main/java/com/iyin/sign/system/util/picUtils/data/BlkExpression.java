/*     */ package com.iyin.sign.system.util.picUtils.data;
/*     */ 
/*     */


import com.iyin.sign.system.util.picUtils.util.HgException;

import java.util.ArrayList;
import java.util.HashMap;

/*     */
/*     */

/*     */
/*     */ public class BlkExpression extends Expression
/*     */ {
/*     */   public String name;
/*     */   public HashMap varMap;
/*     */   public ArrayList expList;
/*     */   protected ArrayList exceptionExpList;
/*     */ 
/*     */   public BlkExpression(BlkExpression blkExp)
/*     */   {
/*  24 */     super(blkExp, "");
/*  25 */     this.expList = new ArrayList();
/*  26 */     this.varMap = new HashMap();
/*     */   }
/*     */ 
/*     */   public void eval(Conn conn)
/*     */     throws HgException
/*     */   {
/*  42 */     super.eval(conn);
/*     */     try {
/*  44 */       evalExpList(conn, this.expList);
/*     */     } catch (Exception e) {
/*  46 */       if ((this.blkExp != null) || (!(e instanceof HgException)) || (
/*  47 */         (!e.getMessage().equals("$SYS_SQL_RETURN$")) && 
/*  48 */         (!e.getMessage().equals("$SYS_SQL_EXIT$")))) {
/*  49 */         if ((this.blkExp != null) && ((e instanceof HgException)) && (
/*  50 */           (e.getMessage().equals("$SYS_SQL_RETURN$")) || 
/*  51 */           (e.getMessage().equals("$SYS_SQL_EXIT$"))))
/*  52 */           throw ((HgException)e);
/*  53 */         if (this.exceptionExpList != null) {
/*  54 */           this.varMap.put("ERROR", e.getMessage());
/*  55 */           evalExpList(conn, this.exceptionExpList);
/*     */         } else {
/*  57 */           if ((e instanceof HgException)) {
/*  58 */             if (this.name != null) {
/*  59 */               ((HgException)e).stackList.add(this.name);
/*     */             }
/*  61 */             throw ((HgException)e);
/*     */           }
/*  63 */           throw new HgException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void evalExpList(Conn conn, ArrayList list) throws HgException {
/*  70 */     for (int i = 0; i < list.size(); i++) {
/*  71 */       Expression exp = (Expression)list.get(i);
/*  72 */       exp.result = this.result;
/*  73 */       exp.resultDataStyle = this.resultDataStyle;
/*  74 */       exp.resultDataType = this.resultDataType;
/*  75 */       exp.eval(conn);
/*  76 */       this.result = exp.result;
/*  77 */       this.resultDataStyle = exp.resultDataStyle;
/*  78 */       this.resultDataType = exp.resultDataType;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean setVar(Conn conn, String name, Object value)
/*     */     throws HgException
/*     */   {
/*  89 */     boolean b = false;
/*  90 */     if (this.varMap.containsKey(name)) {
/*  91 */       this.varMap.put(name, value);
/*  92 */       b = true;
/*  93 */     } else if (name.indexOf('.') > 0) {
/*  94 */       String rowName = name.substring(0, name.indexOf('.'));
/*  95 */       if (this.varMap.containsKey(rowName)) {
/*  96 */         Object obj = this.varMap.get(rowName);
/*  97 */         if ((obj instanceof Row)) {
/*  98 */           String key = name.substring(name.indexOf('.') + 1);
/*  99 */           int index = ((Row)obj).fieldIndex(key);
/* 100 */           if (index >= 0) {
/* 101 */             ((Row)obj).set(index, value);
/* 102 */             b = true;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 111 */     if ((!b) && (this.blkExp != null)) {
/* 112 */       b = this.blkExp.setVar(conn, name, value);
/*     */     }
/* 114 */     return b;
/*     */   }
/*     */ 
/*     */   public void parse() throws HgException {
/* 118 */     for (int i = 0; i < this.expList.size(); i++) {
/* 119 */       Expression exp = (Expression)this.expList.get(i);
/* 120 */       exp.parse();
/*     */     }
/* 122 */     if (this.exceptionExpList != null) {
/* 123 */       for (int i = 0; i < this.exceptionExpList.size(); i++) {
/* 124 */         Expression exp = (Expression)this.exceptionExpList.get(i);
/* 125 */         exp.parse();
/*     */       }
/*     */     }
/* 128 */     this.parsed = true;
/*     */   }
/*     */   public Object getValue(String str) {
/* 131 */     Object obj = null;
/* 132 */     if (str.equals("RESULT")) {
/* 133 */       obj = this.result;
/* 134 */     } else if (this.varMap != null) {
/* 135 */       obj = this.varMap.get(str);
/* 136 */       if (obj == null) {
/* 137 */         if (str.indexOf('.') > 0) {
/* 138 */           String varName = str.substring(0, str.indexOf('.'));
/* 139 */           obj = this.varMap.get(varName);
/* 140 */           if ((obj != null) && ((obj instanceof Row))) {
/* 141 */             obj = ((Row)obj).get(str.substring(str.indexOf('.') + 1));
/*     */           }
/* 143 */           else if (this.blkExp != null) {
/* 144 */             obj = this.blkExp.getValue(str);
/*     */           }
/*     */ 
/*     */         }
/* 148 */         else if (this.blkExp != null) {
/* 149 */           obj = this.blkExp.getValue(str);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 154 */     return obj;
/*     */   }
/*     */ 
/*     */   protected void rtnResult(Object result, int resultDataStyle, int resultDataType)
/*     */   {
/* 163 */     this.result = result;
/* 164 */     this.resultDataStyle = resultDataStyle;
/* 165 */     this.resultDataType = resultDataType;
/* 166 */     if (this.blkExp != null)
/* 167 */       this.blkExp.rtnResult(result, resultDataStyle, resultDataType);
/*     */   }
/*     */ 
/*     */   public ArrayList getChildExpList(Conn conn) throws HgException {
/* 171 */     ArrayList list = new ArrayList();
/* 172 */     if (!this.parsed) parse();
/* 173 */     list.addAll(this.expList);
/* 174 */     if (this.exceptionExpList != null) {
/* 175 */       list.add(new Expression(null, "<<EXCEPTION>>"));
/* 176 */       list.addAll(this.exceptionExpList);
/*     */     }
/* 178 */     return list;
/*     */   }
/*     */   public String getExpStr() {
/* 181 */     return "BEGIN";
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.data.BlkExpression
 * JD-Core Version:    0.6.0
 */