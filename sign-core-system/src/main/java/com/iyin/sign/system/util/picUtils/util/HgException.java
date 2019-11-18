/*     */ package com.iyin.sign.system.util.picUtils.util;
/*     */ 
/*     */

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class HgException extends Exception
/*     */ {
/*     */   public static final String SYS_SQL_EXIT = "$SYS_SQL_EXIT$";
/*     */   public static final String SYS_SQL_RETURN = "$SYS_SQL_RETURN$";
/*  34 */   public ArrayList stackList = new ArrayList();
/*     */ 
/* 130 */   public static HashMap exceptMap = new HashMap();
/*     */ 
/*     */   public HgException(String message)
/*     */   {
/*  39 */     super(message);
/*     */   }
/*     */ 
/*     */   public HgException(String message, Throwable cause)
/*     */   {
/*  47 */     super(message, cause);
/*     */   }
/*     */ 
/*     */   public HgException(Throwable cause)
/*     */   {
/*  54 */     super((cause instanceof HgException) ? cause.getMessage() : null, (cause instanceof HgException) ? cause.getCause() : cause);
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/*  60 */     String str = super.getMessage();
/*  61 */     if (str == null) {
/*  62 */       Throwable throwable = super.getCause();
/*  63 */       if ((throwable instanceof UnknownHostException))
/*  64 */         str = "不能与服务器" + throwable.getMessage() + "连接";
/*  65 */       else if ((throwable instanceof ConnectException))
/*  66 */         str = "不能连接到服务器";
/*  67 */       else if ((throwable instanceof HgException))
/*  68 */         str = throwable.getMessage();
/*     */       else {
/*  70 */         str = throwable.toString();
/*     */       }
/*     */     }
/*  73 */     if (this.stackList.size() > 0) {
/*  74 */       for (int i = this.stackList.size(); i > 0; i--) {
/*  75 */         str = str + "\n" + this.stackList.get(i - 1);
/*     */       }
/*     */     }
/*  78 */     return str;
/*     */   }
/*     */ 
/*     */   public static String getMsg(String code, String[] params)
/*     */   {
/*  88 */     String msg = (String)exceptMap.get(code);
/*  89 */     if (msg != null) {
/*  90 */       if (msg.indexOf("&") >= 0) {
/*  91 */         String[] strs = msg.split("&");
/*  92 */         StringBuffer sb = new StringBuffer();
/*     */ 
/*  94 */         for (int i = 0; i < strs.length; i++) {
/*  95 */           if (i % 2 == 1) {
/*  96 */             int n = To.toInt(strs[i]);
/*  97 */             if (n < params.length)
/*  98 */               sb.append(params[n]);
/*     */           }
/*     */           else {
/* 101 */             sb.append(strs[i]);
/*     */           }
/*     */         }
/* 104 */         msg = sb.toString();
/*     */       }
/*     */     }
/* 107 */     else msg = code;
/*     */ 
/* 109 */     return msg;
/*     */   }
/*     */   public String getStackTraceMsg() {
/* 112 */     return getStackTraceMsg(this);
/*     */   }
/*     */   public static String getStackTraceMsg(Throwable e) {
/* 115 */     StringWriter sw = new StringWriter();
/* 116 */     e.printStackTrace(new PrintWriter(sw));
/* 117 */     return sw.toString();
/*     */   }
/*     */   public static String getStackMsg(Throwable e) {
/* 120 */     Throwable cause = e.getCause();
/* 121 */     String stackMsg = null;
/* 122 */     if (cause != null) {
/* 123 */       stackMsg = getStackMsg(cause);
/*     */     }
/* 125 */     return e.getMessage() + (stackMsg != null ? "\n" + stackMsg : "");
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.util.HgException
 * JD-Core Version:    0.6.0
 */