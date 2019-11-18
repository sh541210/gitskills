/*    */ package com.iyin.sign.system.util.picUtils.xdoc;
/*    */ 
/*    */ public class Meta extends Component
/*    */ {
/*    */   public static final String VIEW_TEXT = "text";
/*    */   public static final String VIEW_TABLE = "table";
/*    */   public static final String VIEW_SLIDE = "slide";
/*    */   public static final String VIEW_WEB = "web";
/*    */ 
/*    */   public Meta()
/*    */   {
/* 26 */     super("meta");
/*    */   }
/*    */ 
/*    */   public void setTitle(String title)
/*    */   {
/* 33 */     setAttribute("title", title);
/*    */   }
/*    */ 
/*    */   public void setAuthor(String author)
/*    */   {
/* 40 */     setAttribute("author", author);
/*    */   }
/*    */ 
/*    */   public void setOrg(String org)
/*    */   {
/* 47 */     setAttribute("org", org);
/*    */   }
/*    */ 
/*    */   public void setDesc(String desc)
/*    */   {
/* 54 */     setAttribute("desc", desc);
/*    */   }
/*    */ 
/*    */   public void setthumb(String thumb)
/*    */   {
/* 61 */     setAttribute("thumb", thumb);
/*    */   }
/*    */ 
/*    */   public void setView(String view)
/*    */   {
/* 84 */     setAttribute("view", view);
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Meta
 * JD-Core Version:    0.6.0
 */