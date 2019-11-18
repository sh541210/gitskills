/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

/*    */
/*    */

/*    */
/*    */ public class EleCharRect extends EleRect
/*    */ {
/*    */   public EleChar eleChar;
/*    */ 
/*    */   protected EleBase copyEle(XDoc xdoc)
/*    */   {
/* 23 */     return new EleCharRect(xdoc, getAttMap());
/*    */   }
/*    */ 
/*    */   public EleCharRect(XDoc xdoc, HashMap attMap) {
/* 27 */     this(xdoc);
/* 28 */     setAttMap(attMap);
/*    */   }
/*    */ 
/*    */   public EleCharRect(XDoc xdoc) {
/* 32 */     super(xdoc);
/* 33 */     this.eleChar = new EleChar(xdoc);
/*    */   }
/*    */   protected void init() {
/* 36 */     super.init();
/* 37 */     this.typeName = "char";
/*    */   }
/*    */   public void setAttMap(HashMap map) {
/* 40 */     super.setAttMap(map);
/* 41 */     this.eleChar.setAttMap(map);
/* 42 */     Rectangle2D bounds = this.eleChar.getBounds();
/* 43 */     this.width = (int)bounds.getWidth();
/* 44 */     this.height = (int)bounds.getHeight();
/*    */   }
/*    */ 
/*    */   public HashMap getAttMap() {
/* 48 */     HashMap map = super.getAttMap();
/* 49 */     map.putAll(this.eleChar.getAttMap());
/* 50 */     return map;
/*    */   }
/*    */   public Object clone() {
/* 53 */     return new EleCharRect(this.xdoc, getAttMap());
/*    */   }
/*    */   public void print(Graphics2D g) {
/* 56 */     this.eleChar.print(g, 0, 0, this.eleChar.fontSize, 0);
/*    */   }
/*    */   public String toString() {
/* 59 */     return this.eleChar.toString();
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.EleCharRect
 * JD-Core Version:    0.6.0
 */