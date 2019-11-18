/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;

/*    */
/*    */

/*    */
/*    */ public class DocConst
/*    */ {
/* 25 */   public static Graphics2D g = (Graphics2D)new BufferedImage(10, 10, 1).getGraphics();
/*    */   public static FontRenderContext frc;
/*    */   public static final String ALIGN_LEFT = "left";
/*    */   public static final String ALIGN_RIGHT = "right";
/*    */   public static final String ALIGN_CENTER = "center";
/*    */   public static final String ALIGN_TOP = "top";
/*    */   public static final String ALIGN_BOTTOM = "bottom";
/*    */   public static final String ALIGN_AROUND = "around";
/*    */   public static final String ALIGN_DISTRIBUTE = "distribute";
/*    */   public static final String ALIGN_FLOAT = "float";
/*    */   public static final String BLANK_RECT = "__blank";
/*    */   public static final String BLANK_RECT_PREFIX = "__blank:";
/*    */   public static final String MAIL = "xdoc@xdocserver.com";
/*    */   public static final String NO_PARA_BREAK = "$_NO_BREAK_$";
/*    */   public static final String NO_VALUE = "$null$";
/*    */   public static final String PRINTMARK_PRE = "$[";
/*    */   public static final String PRINTMARK_POST = "]";
/*    */   public static final String BLANK_TEMPLATE = "{}";
/*    */ 
/*    */   static
/*    */   {
/* 26 */     ImgUtil.setRenderHint(g);
/* 27 */     frc = g.getFontRenderContext();
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.DocConst
 * JD-Core Version:    0.6.0
 */