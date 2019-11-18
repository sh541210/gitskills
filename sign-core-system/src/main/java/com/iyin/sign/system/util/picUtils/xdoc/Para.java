/*     */ package com.iyin.sign.system.util.picUtils.xdoc;
/*     */ 
/*     */ public class Para extends Container
/*     */ {
/*     */   public static final String ALIGN_LEFT = "left";
/*     */   public static final String ALIGN_RIGHT = "right";
/*     */   public static final String ALIGN_CENTER = "center";
/*     */   public static final String PREFIX_CIRCLE = "●";
/*     */   public static final String PREFIX_CIRCLE_BLANK = "●";
/*     */   public static final String PREFIX_RECT = "■";
/*     */   public static final String PREFIX_RECT_BLANK = "□";
/*     */ 
/*     */   public Para()
/*     */   {
/*  26 */     super("para");
/*     */   }
/*     */ 
/*     */   public Para(Component comp)
/*     */   {
/*  33 */     this();
/*  34 */     add(comp);
/*     */   }
/*     */ 
/*     */   public Para(String text)
/*     */   {
/*  41 */     this(new Text(text));
/*     */   }
/*     */ 
/*     */   public Para(String text, int heading)
/*     */   {
/*  48 */     this();
/*  49 */     Text txt = new Text(text);
/*  50 */     txt.setBold(heading > 0);
/*  51 */     txt.setFontSize((heading == 0) || (heading > 7) ? 14 : 28 - heading * 2);
/*  52 */     int spacing = heading > 7 ? 4 : 20 - heading * 2;
/*  53 */     setHeading(heading);
/*  54 */     if (heading == 0) spacing = 0;
/*  55 */     setLineSpacing(spacing);
/*  56 */     add(txt);
/*     */   }
/*     */ 
/*     */   public void setAlign(String align)
/*     */   {
/*  75 */     setAttribute("align", align);
/*     */   }
/*     */ 
/*     */   public void setBreakPage(boolean breakPage)
/*     */   {
/*  82 */     setAttribute("breakPage", breakPage);
/*     */   }
/*     */ 
/*     */   public void setLineSpacing(int lineSpacing)
/*     */   {
/*  89 */     setAttribute("lineSpacing", lineSpacing);
/*     */   }
/*     */ 
/*     */   public void setHeading(int heading)
/*     */   {
/*  96 */     setAttribute("heading", heading);
/*     */   }
/*     */ 
/*     */   public void setBackColor(Color color)
/*     */   {
/* 103 */     setAttribute("backColor", Color.toString(color));
/*     */   }
/*     */ 
/*     */   public void setIndent(int indent)
/*     */   {
/* 110 */     setAttribute("indent", indent);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix)
/*     */   {
/* 133 */     setAttribute("prefix", prefix);
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Para
 * JD-Core Version:    0.6.0
 */