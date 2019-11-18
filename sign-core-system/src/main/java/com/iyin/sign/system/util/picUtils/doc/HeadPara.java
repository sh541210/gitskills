/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */

import java.util.ArrayList;
import java.util.List;

/*    */

/*    */
/*    */ public class HeadPara
/*    */ {
/*    */   public ElePara para;
/* 22 */   public ArrayList paras = new ArrayList();
/*    */ 
/* 24 */   public HeadPara(ElePara elePara) { this.para = elePara; }
/*    */ 
/*    */   public static HeadPara getHeader(List headParas, int[] inx) {
/* 27 */     return getHeader(headParas, inx, 0);
/*    */   }
/*    */   private static HeadPara getHeader(List headParas, int[] inx, int level) {
/* 30 */     HeadPara headPara = (HeadPara)headParas.get(inx[level]);
/* 31 */     if (level + 1 < inx.length) {
/* 32 */       level++;
/* 33 */       return getHeader(headPara.paras, inx, level);
/*    */     }
/* 35 */     return headPara;
/*    */   }
/*    */ 
/*    */   public static ArrayList toHeadParas(XDoc doc) {
/* 39 */     ArrayList headParas = new ArrayList();
/* 40 */     ArrayList paras = new ArrayList();
/*    */ 
/* 42 */     for (int i = 0; i < doc.paraList.size(); i++) {
/* 43 */       ElePara elePara = (ElePara)doc.paraList.get(i);
/* 44 */       if (elePara.heading != 0) {
/* 45 */         addHeadPara(headParas, paras, new HeadPara(elePara));
/*    */       }
/*    */     }
/* 48 */     return headParas;
/*    */   }
/*    */ 
/*    */   private static void addHeadPara(ArrayList headParas, ArrayList paras, HeadPara para) {
/* 52 */     boolean find = false;
/* 53 */     for (int i = paras.size() - 1; i >= 0; i--) {
/* 54 */       HeadPara tmpPara = (HeadPara)paras.get(i);
/* 55 */       if (tmpPara.para.heading < para.para.heading) {
/* 56 */         tmpPara.paras.add(para);
/* 57 */         find = true;
/* 58 */         break;
/*    */       }
/*    */     }
/* 61 */     if (!find) {
/* 62 */       headParas.add(para);
/*    */     }
/* 64 */     paras.add(para);
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.HeadPara
 * JD-Core Version:    0.6.0
 */