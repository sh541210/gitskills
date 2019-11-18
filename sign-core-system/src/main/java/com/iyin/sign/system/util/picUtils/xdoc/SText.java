/*    */ package com.iyin.sign.system.util.picUtils.xdoc;
/*    */ 
/*    */ public class SText extends Rect
/*    */ {
/*    */   public SText(String text)
/*    */   {
/* 27 */     super("stext");
/* 28 */     setText(text);
/*    */   }
/*    */ 
/*    */   public void setText(String text)
/*    */   {
/* 35 */     setAttribute("text", text);
/*    */   }
/*    */ 
/*    */   public void setFontName(String name)
/*    */   {
/* 42 */     setAttribute("fontName", name);
/*    */   }
/*    */ 
/*    */   public void setBold(boolean bold)
/*    */   {
/* 49 */     setAttribute("bold", bold);
/*    */   }
/*    */ 
/*    */   public void setItalic(boolean italic)
/*    */   {
/* 56 */     setAttribute("italic", italic);
/*    */   }
/*    */ 
/*    */   public void setSpacing(int spacing)
/*    */   {
/* 63 */     setAttribute("spacing", spacing);
/*    */   }
/*    */ 
/*    */   public void setFormat(String format)
/*    */   {
/* 70 */     setAttribute("format", format);
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.SText
 * JD-Core Version:    0.6.0
 */