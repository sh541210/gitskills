/*    */ package com.iyin.sign.system.util.picUtils.doc;
/*    */ 
/*    */


import com.iyin.sign.system.util.picUtils.util.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;

/*    */
/*    */

/*    */
/*    */ public class EleParaLine extends ElePara
/*    */ {
/*    */   public int width;
/*    */   public int height;
/*    */   public int offset;
/*    */   public boolean vertical;
/*    */ 
/*    */   public EleParaLine(XDoc xdoc, HashMap attMap)
/*    */   {
/* 28 */     super(xdoc, attMap);
/*    */   }
/*    */   public EleParaLine(DocPageLine line) {
/* 31 */     super(line.para.xdoc, line.para.getAttMap());
/* 32 */     this.width = line.width;
/* 33 */     this.height = line.height;
/* 34 */     this.offset = line.offset;
/* 35 */     this.vertical = line.vertical;
/* 36 */     this.eleList = line.eleList;
/*    */   }
/*    */   protected void init() {
/* 39 */     super.init();
/* 40 */     this.typeName = "pline";
/* 41 */     this.width = 0;
/* 42 */     this.height = XFont.defaultFontSize;
/* 43 */     this.offset = 0;
/* 44 */     this.vertical = false;
/*    */   }
/*    */   public HashMap getAttMap() {
/* 47 */     HashMap map = super.getAttMap();
/* 48 */     map.put("width", String.valueOf(this.width));
/* 49 */     map.put("height", String.valueOf(this.height));
/* 50 */     map.put("offset", String.valueOf(this.offset));
/* 51 */     map.put("vertical", String.valueOf(this.vertical));
/* 52 */     map.put("lineSpacing", "0");
/* 53 */     map.put("heading", "0");
/* 54 */     map.put("align", "left");
/* 55 */     map.put("prefix", "");
/* 56 */     map.put("breakPage", "false");
/* 57 */     return map;
/*    */   }
/*    */   public void setAttMap(HashMap map) {
/* 60 */     super.setAttMap(map);
/* 61 */     this.width = MapUtil.getInt(map, "width", this.width);
/* 62 */     this.height = MapUtil.getInt(map, "height", this.height);
/* 63 */     this.offset = MapUtil.getInt(map, "offset", this.offset);
/* 64 */     this.vertical = MapUtil.getBool(map, "vertical", this.vertical);
/*    */   }
/*    */   public ArrayList toLineList(int top, int width, ArrayList hrList, boolean h) {
/* 67 */     this.layWidth = (width - this.indentLeft);
/* 68 */     ArrayList lineList = new ArrayList();
/* 69 */     DocPageLine line = new DocPageLine(this, this.eleList, this.height, this.width, this.vertical);
/* 70 */     line.offset = this.offset;
/* 71 */     lineList.add(line);
/* 72 */     return lineList;
/*    */   }
/*    */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.EleParaLine
 * JD-Core Version:    0.6.0
 */