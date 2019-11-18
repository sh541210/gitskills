/*     */ package com.iyin.sign.system.util.picUtils.xdoc;
/*     */ 
/*     */

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class Component
/*     */ {
/*     */   private String tagName;
/*     */   private String text;
/*  93 */   private Map atts = new HashMap();
/*     */ 
/*     */   protected Component(String tagName)
/*     */   {
/*  38 */     this.tagName = tagName;
/*     */   }
/*     */ 
/*     */   public String getAttribute(String name)
/*     */   {
/*  46 */     String val = (String)this.atts.get(name);
/*  47 */     return val != null ? val : "";
/*     */   }
/*     */ 
/*     */   public int getAttribute(String name, int n)
/*     */   {
/*     */     try
/*     */     {
/*  56 */       return Integer.parseInt(getAttribute(name)); } catch (Exception e) {
/*     */     }
/*  58 */     return n;
/*     */   }
/*     */ 
/*     */   public boolean getAttribute(String name, boolean b)
/*     */   {
/*     */     try
/*     */     {
/*  68 */       return Boolean.valueOf(getAttribute(name)).booleanValue(); } catch (Exception e) {
/*     */     }
/*  70 */     return b;
/*     */   }
/*     */ 
/*     */   public Color getAttribute(String name, Color c)
/*     */   {
/*  79 */     if (this.atts.containsKey(name)) {
/*  80 */       return new Color(getAttribute(name));
/*     */     }
/*  82 */     return c;
/*     */   }
/*     */ 
/*     */   public void setText(String text)
/*     */   {
/*  91 */     this.text = text;
/*     */   }
/*     */ 
/*     */   public void setAttribute(String name, String value)
/*     */   {
/* 100 */     this.atts.put(name, value);
/*     */   }
/*     */ 
/*     */   public void setAttribute(String name, boolean value)
/*     */   {
/* 108 */     setAttribute(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   public void setAttribute(String name, int value)
/*     */   {
/* 116 */     setAttribute(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   public Element toElement(Document doc)
/*     */   {
/* 123 */     Element ele = doc.createElement(this.tagName);
/* 124 */     Iterator it = this.atts.keySet().iterator();
/*     */ 
/* 126 */     while (it.hasNext()) {
/* 127 */       String key = (String)it.next();
/* 128 */       ele.setAttribute(key, (String)this.atts.get(key));
/*     */     }
/* 130 */     if (this.text != null) {
/* 131 */       ele.setTextContent(this.text);
/*     */     }
/* 133 */     return ele;
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Component
 * JD-Core Version:    0.6.0
 */