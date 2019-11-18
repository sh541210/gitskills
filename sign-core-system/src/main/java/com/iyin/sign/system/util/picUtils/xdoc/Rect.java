/*     */ package com.iyin.sign.system.util.picUtils.xdoc;
/*     */ 
/*     */ public class Rect extends Container
/*     */ {
/*     */   public static final String SIZE_TYPE_NORMAL = "normal";
/*     */   public static final String SIZE_TYPE_AUTOWIDTH = "autowidth";
/*     */   public static final String SIZE_TYPE_AUTOHEIGHT = "autoheight";
/*     */   public static final String SIZE_TYPE_AUTOSIZE = "autosize";
/*     */   public static final String SIZE_TYPE_AUTOFONT = "autofont";
/*     */   public static final String SIZE_TYPE_AUTOTIP = "autotip";
/*     */   public static final String VALIGN_TOP = "top";
/*     */   public static final String VALIGN_CENTER = "center";
/*     */   public static final String VALIGN_BOTTOM = "bottom";
/*     */   public static final String DOCK_LEFT = "left";
/*     */   public static final String DOCK_TOP = "top";
/*     */   public static final String DOCK_RIGHT = "right";
/*     */   public static final String DOCK_BOTTOM = "bottom";
/*     */   public static final String DOCK_LEFT_TOP = "lefttop";
/*     */   public static final String DOCK_LEFT_BOTTOM = "leftbottom";
/*     */   public static final String DOCK_RIGHT_TOP = "righttop";
/*     */   public static final String DOCK_RIGHT_BOTTOM = "leftbottom";
/*     */ 
/*     */   protected Rect(String tagName)
/*     */   {
/*  27 */     super(tagName);
/*     */   }
/*     */ 
/*     */   public Rect()
/*     */   {
/*  33 */     super("rect");
/*     */   }
/*     */ 
/*     */   public Rect(int width, int height)
/*     */   {
/*  41 */     this();
/*  42 */     setSize(width, height);
/*     */   }
/*     */ 
/*     */   public Rect(int left, int top, int width, int height)
/*     */   {
/*  52 */     this();
/*  53 */     setBounds(left, top, width, height);
/*     */   }
/*     */ 
/*     */   public void setLeft(int left)
/*     */   {
/*  60 */     setAttribute("left", left);
/*     */   }
/*     */ 
/*     */   public void setTop(int top)
/*     */   {
/*  67 */     setAttribute("top", top);
/*     */   }
/*     */ 
/*     */   public void setLocation(int left, int top)
/*     */   {
/*  75 */     setLeft(left);
/*  76 */     setTop(top);
/*     */   }
/*     */ 
/*     */   public void setWidth(int width)
/*     */   {
/*  83 */     setAttribute("width", width);
/*     */   }
/*     */ 
/*     */   public void setHeight(int height)
/*     */   {
/*  90 */     setAttribute("height", height);
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/*  97 */     return getAttribute("width", 96);
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 104 */     return getAttribute("height", 24);
/*     */   }
/*     */ 
/*     */   public void setSize(int width, int height)
/*     */   {
/* 112 */     setWidth(width);
/* 113 */     setHeight(height);
/*     */   }
/*     */ 
/*     */   public void setBounds(int left, int top, int width, int height)
/*     */   {
/* 119 */     setLocation(left, top);
/* 120 */     setSize(width, height);
/*     */   }
/*     */ 
/*     */   public void setStrokeWidth(int width)
/*     */   {
/* 127 */     setAttribute("strokeWidth", width);
/*     */   }
/*     */ 
/*     */   public void setStrokeStyle(String strokeStyle)
/*     */   {
/* 134 */     setAttribute("strokeStyle", strokeStyle);
/*     */   }
/*     */ 
/*     */   public void setStrokeImg(String strokeImg)
/*     */   {
/* 141 */     setAttribute("strokeImg", strokeImg);
/*     */   }
/*     */ 
/*     */   public void setLine(String line)
/*     */   {
/* 148 */     setAttribute("line", line);
/*     */   }
/*     */ 
/*     */   public void setArc(int arc)
/*     */   {
/* 155 */     setAttribute("arc", arc);
/*     */   }
/*     */ 
/*     */   public void setRotate(int rotate)
/*     */   {
/* 162 */     setAttribute("rotate", rotate);
/*     */   }
/*     */ 
/*     */   public void setSRotate(int srotate)
/*     */   {
/* 169 */     setAttribute("srotate", srotate);
/*     */   }
/*     */ 
/*     */   public void setColor(Color color)
/*     */   {
/* 176 */     setAttribute("color", Color.toString(color));
/*     */   }
/*     */ 
/*     */   public void setFillColor(Color color)
/*     */   {
/* 183 */     setAttribute("fillColor", Color.toString(color));
/*     */   }
/*     */ 
/*     */   public void setGradual(int type)
/*     */   {
/* 190 */     setAttribute("gradual", type);
/*     */   }
/*     */ 
/*     */   public void setGradual(int type, Color color)
/*     */   {
/* 198 */     setGradual(type);
/* 199 */     setAttribute("fillColor2", Color.toString(color));
/*     */   }
/*     */ 
/*     */   public void setFillImg(String img)
/*     */   {
/* 206 */     setAttribute("fillImg", img);
/*     */   }
/*     */ 
/*     */   public void setFilter(String filter)
/*     */   {
/* 213 */     setAttribute("filter", filter);
/*     */   }
/*     */ 
/*     */   public void setFilter(String filter, String param)
/*     */   {
/* 221 */     setFilter(filter);
/* 222 */     setAttribute("filterParam", param);
/*     */   }
/*     */ 
/*     */   public void setToolTip(String toolTip)
/*     */   {
/* 229 */     setAttribute("toolTip", toolTip);
/*     */   }
/*     */ 
/*     */   public void setHref(String href)
/*     */   {
/* 236 */     setAttribute("href", href);
/*     */   }
/*     */ 
/*     */   public void setColumn(int column)
/*     */   {
/* 243 */     setAttribute("column", column);
/*     */   }
/*     */ 
/*     */   public void setVisible(boolean visible)
/*     */   {
/* 250 */     setAttribute("visible", visible);
/*     */   }
/*     */ 
/*     */   public void setSizeType(String sizeType)
/*     */   {
/* 281 */     setAttribute("sizeType", sizeType);
/*     */   }
/*     */ 
/*     */   public void setVAlign(String vAlign)
/*     */   {
/* 300 */     setAttribute("valign", vAlign);
/*     */   }
/*     */ 
/*     */   public void setDock(String dock)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.xdoc.Rect
 * JD-Core Version:    0.6.0
 */