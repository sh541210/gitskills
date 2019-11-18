/*     */ package com.iyin.sign.system.util.picUtils.xdoc;
/*     */ 
/*     */ public class Text extends Component
/*     */ {
/*     */   public static final String VALIGN_TOP = "top";
/*     */   public static final String VALIGN_CENTER = "center";
/*     */   public static final String VALIGN_BOTTOM = "bottom";
/*     */ 
/*     */   public Text()
/*     */   {
/*  26 */     super("text");
/*     */   }
/*     */ 
/*     */   public Text(String text)
/*     */   {
/*  33 */     this();
/*  34 */     setText(text);
/*     */   }
/*     */ 
/*     */   public void setFont(String name, int size)
/*     */   {
/*  42 */     setFontName(name);
/*  43 */     setFontSize(size);
/*     */   }
/*     */ 
/*     */   public void setFontName(String name)
/*     */   {
/*  50 */     setAttribute("fontName", name);
/*     */   }
/*     */ 
/*     */   public void setFontSize(int size)
/*     */   {
/*  57 */     setAttribute("fontSize", String.valueOf(size));
/*     */   }
/*     */ 
/*     */   public void setFontColor(Color color)
/*     */   {
/*  64 */     setAttribute("fontColor", Color.toString(color));
/*     */   }
/*     */ 
/*     */   public void setBackColor(Color color)
/*     */   {
/*  71 */     setAttribute("backColor", Color.toString(color));
/*     */   }
/*     */ 
/*     */   public void setFontStyle(String style)
/*     */   {
/*  78 */     setAttribute("fontStyle", style);
/*     */   }
/*     */   private void setStyle(String name, boolean value) {
/*  81 */     String style = getAttribute("fontStyle");
/*  82 */     String[] strs = style.split(",");
/*  83 */     boolean find = false;
/*  84 */     for (int i = 0; i < strs.length; i++) {
/*  85 */       if (strs[i].equals(name)) {
/*  86 */         if (value) {
/*  87 */           return;
/*     */         }
/*  89 */         find = true;
/*  90 */         strs[i] = null;
/*  91 */         break;
/*     */       }
/*     */     }
/*     */ 
/*  95 */     StringBuffer sb = new StringBuffer();
/*  96 */     for (int i = 0; i < strs.length; i++) {
/*  97 */       if (strs[i] != null) {
/*  98 */         if (sb.length() > 0) {
/*  99 */           sb.append(",");
/*     */         }
/* 101 */         sb.append(strs[i]);
/*     */       }
/*     */     }
/* 104 */     if ((!find) && (value)) {
/* 105 */       if (sb.length() > 0) {
/* 106 */         sb.append(",");
/*     */       }
/* 108 */       sb.append(name);
/*     */     }
/* 110 */     setFontStyle(sb.toString());
/*     */   }
/*     */ 
/*     */   public void setBold(boolean bold)
/*     */   {
/* 117 */     setStyle("bold", bold);
/*     */   }
/*     */ 
/*     */   public void setItalic(boolean italic)
/*     */   {
/* 124 */     setStyle("italic", italic);
/*     */   }
/*     */ 
/*     */   public void setThroughline(boolean throughline)
/*     */   {
/* 131 */     setStyle("throughline", throughline);
/*     */   }
/*     */ 
/*     */   public void setShadow(boolean shadow)
/*     */   {
/* 138 */     setStyle("shadow", shadow);
/*     */   }
/*     */ 
/*     */   public void setCircle(boolean circle)
/*     */   {
/* 145 */     setStyle("circle", circle);
/*     */   }
/*     */ 
/*     */   public void setStress(boolean stress)
/*     */   {
/* 152 */     setStyle("stress", stress);
/*     */   }
/*     */ 
/*     */   public void setOutline(boolean outline)
/*     */   {
/* 159 */     setStyle("outline", outline);
/*     */   }
/*     */   private void setUnderline(String underline) {
/* 162 */     String style = getAttribute("fontStyle");
/* 163 */     boolean find = false;
/* 164 */     String[] strs = style.split(",");
/* 165 */     for (int i = 0; i < strs.length; i++) {
/* 166 */       if (strs[i].startsWith("underline")) {
/* 167 */         if (underline.length() > 0)
/* 168 */           strs[i] = underline;
/*     */         else {
/* 170 */           strs[i] = null;
/*     */         }
/* 172 */         find = true;
/* 173 */         break;
/*     */       }
/*     */     }
/* 176 */     StringBuffer sb = new StringBuffer();
/* 177 */     for (int i = 0; i < strs.length; i++) {
/* 178 */       if (strs[i] != null) {
/* 179 */         if (sb.length() > 0) {
/* 180 */           sb.append(",");
/*     */         }
/* 182 */         sb.append(strs[i]);
/*     */       }
/*     */     }
/* 185 */     if ((!find) && (underline.length() > 0)) {
/* 186 */       if (sb.length() > 0) {
/* 187 */         sb.append(",");
/*     */       }
/* 189 */       sb.append(underline);
/*     */     }
/* 191 */     setFontStyle(sb.toString());
/*     */   }
/*     */ 
/*     */   public void setUnderline(boolean underline)
/*     */   {
/* 199 */     setUnderline(underline ? "underline" : "");
/*     */   }
/*     */ 
/*     */   public void setUnderline(int type)
/*     */   {
/* 206 */     setUnderline("underline" + type);
/*     */   }
/*     */ 
/*     */   public void setUnderline(int type, Color color)
/*     */   {
/* 214 */     setUnderline("underline" + type + "_" + color);
/*     */   }
/*     */ 
/*     */   public void setVAlign(String vAlign)
/*     */   {
/* 233 */     setAttribute("valign", vAlign);
/*     */   }
/*     */ 
/*     */   public void setHref(String href)
/*     */   {
/* 240 */     setAttribute("href", href);
/*     */   }
/*     */ 
/*     */   public void setFormat(String format)
/*     */   {
/* 247 */     setAttribute("format", format);
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Text
 * JD-Core Version:    0.6.0
 */