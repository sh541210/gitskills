/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */


import com.iyin.sign.system.util.picUtils.util.To;

/*    */
/*    */ 
/*    */ public class Meta
/*    */ {
/*    */   private XDoc xdoc;
/* 22 */   public static final String[] metaNames = { "Id", "Title", "Author", "Org", "Desc", "View", "Action", "PrintBack", "CreateDate", "ModifyDate" };
/* 23 */   public static final String[] metas = { "id", "title", "author", "org", "desc", "view", "action", "printBack", "createDate", "modifyDate" };
/*    */ 
/* 25 */   public Meta(XDoc xdoc) { this.xdoc = xdoc; }
/*    */ 
/*    */   public String getTitle() {
/* 28 */     return this.xdoc.getMeta("title");
/*    */   }
/*    */   public String getAuthor() {
/* 31 */     return this.xdoc.getMeta("author");
/*    */   }
/*    */   public String getRunTip() {
/* 34 */     return this.xdoc.getMeta("runTip");
/*    */   }
/*    */   public String getAction() {
/* 37 */     return this.xdoc.getMeta("action");
/*    */   }
/*    */   public String getDesc() {
/* 40 */     return this.xdoc.getMeta("desc");
/*    */   }
/*    */   public String getOrg() {
/* 43 */     return this.xdoc.getMeta("org");
/*    */   }
/*    */   public String getView() {
/* 46 */     return this.xdoc.getMeta("view");
/*    */   }
/*    */   public String getId() {
/* 49 */     return this.xdoc.getMeta("id");
/*    */   }
/*    */   public void setRunTip(String runTip) {
/* 52 */     this.xdoc.metaMap.put("runTip", runTip);
/*    */   }
/*    */   public double getScale() {
/* 55 */     return To.toDouble(this.xdoc.getMeta("scale", "1"), 1.0D);
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.Meta
 * JD-Core Version:    0.6.0
 */