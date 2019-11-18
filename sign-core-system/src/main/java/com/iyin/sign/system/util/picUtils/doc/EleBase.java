/*     */ package com.iyin.sign.system.util.picUtils.doc;

import com.iyin.sign.system.util.picUtils.util.HgException;
import com.iyin.sign.system.util.picUtils.util.MapUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class EleBase
/*     */   implements Cloneable
/*     */ {
/*     */   public static final String ELEMENT_TEXT = "TEXT";
/*     */   public XDoc xdoc;
/*     */   public String name;
/*  42 */   public ArrayList eleList = new ArrayList();
/*     */   public String typeName;
/*     */   protected EleBase resEle;
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  44 */     return new EleBase(this.xdoc, getAttMap());
/*     */   }
/*     */ 
/*     */   public EleBase(XDoc xdoc)
/*     */   {
/*  51 */     this.xdoc = xdoc;
/*  52 */     init();
/*     */   }
/*     */   public EleBase(XDoc xdoc, HashMap attMap) {
/*  55 */     this(xdoc);
/*  56 */     setAttMap(attMap);
/*     */   }
/*     */   protected void init() {
/*  59 */     this.name = "";
/*     */   }
/*     */   public void setXDoc(XDoc xdoc) {
/*  62 */     this.xdoc = xdoc;
/*  63 */     for (int i = 0; i < this.eleList.size(); i++)
/*  64 */       ((EleBase)this.eleList.get(i)).setXDoc(xdoc);
/*     */   }
/*     */ 
/*     */   protected EleBase copyEle(XDoc xdoc) {
/*  68 */     return new EleBase(xdoc, getAttMap());
/*     */   }
/*     */ 
/*     */   public void setAttribute(String key, Object object)
/*     */   {
/*  76 */     HashMap map = new HashMap();
/*  77 */     map.put(key, object);
/*  78 */     setAttMap(map);
/*     */   }
/*     */ 
/*     */   public HashMap getAttMap()
/*     */   {
/*  85 */     HashMap map = new HashMap();
/*  86 */     map.put("name", this.name);
/*  87 */     return map;
/*     */   }
/*     */ 
/*     */   public void setAttMap(HashMap map)
/*     */   {
/*  94 */     this.name = MapUtil.getString(map, "name", this.name);
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String key)
/*     */   {
/* 102 */     return getAttMap().get(key);
/*     */   }
/*     */ 
/*     */   protected Object getAttribute(String key, Object defValue)
/*     */   {
/* 110 */     Map map = getAttMap();
/* 111 */     Object obj = map.get(MapUtil.igKey(map, key));
/* 112 */     return obj != null ? obj : defValue;
/*     */   }
/*     */   public String toString() {
/* 115 */     return this.name;
/*     */   }
/*     */   public static EleBase deepClone(XDoc doc, EleBase ele) throws HgException {
/*     */     try {
/* 119 */       Constructor cons = ele.getClass().getConstructor(
/* 120 */         new Class[] { doc.getClass(), HashMap.class });
/* 121 */       EleBase cloneEle = (EleBase)cons.newInstance(new Object[] { doc,
/* 122 */         ele.getAttMap() });
/* 123 */       for (int i = 0; i < ele.eleList.size(); i++) {
/* 124 */         cloneEle.eleList.add(deepClone(doc, 
/* 125 */           (EleBase)ele.eleList.get(i)));
/*     */       }
/* 127 */       return cloneEle;
    } catch (Exception e) {
/* 129 */
        throw new HgException(e);
    }
/*     */   }
/*     */ 
/*     */   public void setText(String text)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.doc.EleBase
 * JD-Core Version:    0.6.0
 */