/*     */ package com.iyin.sign.system.util.picUtils.doc;
/*     */ 
/*     */


import com.iyin.sign.system.util.picUtils.util.MapUtil;
import com.iyin.sign.system.util.picUtils.util.To;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.HashMap;

/*     */
/*     */
/*     */
/*     */
/*     */
///*     */ import java.awt.geom.Line2D.Double;
/*     */
/*     */ 
/*     */ public class EleArc extends EleShape
/*     */ {
/*     */   public int angleStart;
/*     */   public int angleExtent;
/*     */   public String arcType;
/*  74 */   public static String ARC_TYPE_OPEN = "open";
/*  75 */   public static String ARC_TYPE_CHORD = "chord";
/*  76 */   public static String ARC_TYPE_PIE = "pie";
/*  77 */   public static String ARC_TYPE_RING = "ring";
/*  78 */   public static String ARC_TYPE_THIN_RING = "thinring";
/*  79 */   public static String ARC_TYPE_THICK_RING = "thickring";
/*     */ 
/*     */   protected EleBase copyEle(XDoc xdoc)
/*     */   {
/*  32 */     return new EleArc(xdoc, getAttMap());
/*     */   }
/*     */ 
/*     */   public EleArc(XDoc xdoc, HashMap attMap) {
/*  36 */     super(xdoc, attMap);
/*     */   }
/*     */   public EleArc(XDoc xdoc) {
/*  39 */     super(xdoc);
/*     */   }
/*     */   protected void init() {
/*  42 */     super.init();
/*  43 */     this.typeName = "arc";
/*  44 */     this.arcType = ARC_TYPE_OPEN;
/*  45 */     this.angleStart = 0;
/*  46 */     this.angleExtent = 360;
/*     */   }
/*     */ 
/*     */   protected void bindValue(String val)
/*     */   {
/*  51 */     this.angleExtent = To.toInt(val);
/*     */   }
/*     */ 
/*     */   public void setAttMap(HashMap map) {
/*  55 */     super.setAttMap(map);
/*  56 */     this.arcType = MapUtil.getString(map, "arcType", this.arcType);
/*  57 */     this.arcType = MapUtil.getString(map, "type", this.arcType).toLowerCase();
/*  58 */     this.angleStart = MapUtil.getInt(map, "angleStart", this.angleStart);
/*  59 */     this.angleStart = MapUtil.getInt(map, "start", this.angleStart);
/*  60 */     this.angleExtent = MapUtil.getInt(map, "angleExtent", this.angleExtent);
/*  61 */     this.angleExtent = MapUtil.getInt(map, "extent", this.angleExtent);
/*     */   }
/*     */ 
/*     */   public HashMap getAttMap() {
/*  65 */     HashMap map = super.getAttMap();
/*  66 */     map.put("type", this.arcType);
/*  67 */     map.put("start", String.valueOf(this.angleStart));
/*  68 */     map.put("extent", String.valueOf(this.angleExtent));
/*  69 */     return map;
/*     */   }
/*     */   public Object clone() {
/*  72 */     return new EleArc(this.xdoc, getAttMap());
/*     */   }
/*     */ 
/*     */   protected Shape getShape()
/*     */   {
/*  81 */     Arc2D arc = new Arc2D.Double();
/*  82 */     int extent = this.angleExtent;
/*  83 */     if (extent == 0) {
/*  84 */       extent = 360;
/*     */     }
/*  86 */     int start = -this.angleStart + 90;
/*  87 */     arc.setAngleStart(start);
/*  88 */     extent = -extent;
/*  89 */     setArcType(arc);
/*  90 */     arc.setAngleExtent(extent);
/*  91 */     arc.setFrame(0.0D, 0.0D, this.width, this.height);
/*  92 */     if (this.arcType.toLowerCase().endsWith("ring")) {
/*  93 */       Arc2D arc2 = new Arc2D.Double();
/*  94 */       arc2.setAngleStart(start + extent);
/*  95 */       setArcType(arc2);
/*  96 */       arc2.setAngleExtent(-extent);
/*  97 */       if (this.arcType.equalsIgnoreCase("thinring"))
/*  98 */         arc2.setFrame(this.width * 0.125D, this.height * 0.125D, this.width * 0.75D, this.height * 0.75D);
/*  99 */       else if (this.arcType.equalsIgnoreCase("thickring"))
/* 100 */         arc2.setFrame(this.width * 0.375D, this.height * 0.375D, this.width * 0.25D, this.height * 0.25D);
/*     */       else {
/* 102 */         arc2.setFrame(this.width * 0.25D, this.height * 0.25D, this.width * 0.5D, this.height * 0.5D);
/*     */       }
/* 104 */       GeneralPath path = new GeneralPath();
/* 105 */       path.append(arc, false);
/* 106 */       if (extent % 360 != 0) {
/* 107 */         path.append(new Line2D.Double(arc.getEndPoint(), arc2.getStartPoint()), true);
/*     */       }
/* 109 */       path.append(arc2, extent % 360 != 0);
/* 110 */       if (extent % 360 != 0) {
/* 111 */         path.append(new Line2D.Double(arc2.getEndPoint(), arc.getStartPoint()), true);
/*     */       }
/* 113 */       return path;
/*     */     }
/* 115 */     return arc;
/*     */   }
/*     */ 
/*     */   private void setArcType(Arc2D arc) {
/* 119 */     if (this.arcType != null)
/* 120 */       if (this.arcType.equals(ARC_TYPE_CHORD))
/* 121 */         arc.setArcType(1);
/* 122 */       else if ((this.arcType.equals(ARC_TYPE_PIE)) && (this.angleExtent % 360 != 0))
/* 123 */         arc.setArcType(2);
/*     */       else
/* 125 */         arc.setArcType(0);
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.EleArc
 * JD-Core Version:    0.6.0
 */