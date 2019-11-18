/*     */ package com.iyin.sign.system.util.picUtils.util;
/*     */ 
/*     */

import java.awt.*;
import java.util.HashMap;

/*     */

/*     */
/*     */ public class ColorUtil
/*     */ {
/*  62 */   public static HashMap colorMap = new HashMap();
/*     */ 
/*  64 */   static { colorMap.put("aliceblue", "#F0F8FF");
/*  65 */     colorMap.put("antiquewhite", "#FAEBD7");
/*  66 */     colorMap.put("aqua", "#00FFFF");
/*  67 */     colorMap.put("aquamarine", "#7FFFD4");
/*  68 */     colorMap.put("azure", "#F0FFFF");
/*  69 */     colorMap.put("beige", "#F5F5DC");
/*  70 */     colorMap.put("bisque", "#FFE4C4");
/*  71 */     colorMap.put("black", "#000000");
/*  72 */     colorMap.put("blanchedalmond", "#FFEBCD");
/*  73 */     colorMap.put("blue", "#0000FF");
/*  74 */     colorMap.put("blueviolet", "#8A2BE2");
/*  75 */     colorMap.put("brown", "#A52A2A");
/*  76 */     colorMap.put("burlywood", "#DEB887");
/*  77 */     colorMap.put("cadetblue", "#5F9EA0");
/*  78 */     colorMap.put("chartreuse", "#7FFF00");
/*  79 */     colorMap.put("chocolate", "#D2691E");
/*  80 */     colorMap.put("coral", "#FF7F50");
/*  81 */     colorMap.put("cornflowerblue", "#6495ED");
/*  82 */     colorMap.put("cornsilk", "#FFF8DC");
/*  83 */     colorMap.put("crimson", "#DC143C");
/*  84 */     colorMap.put("cyan", "#00FFFF");
/*  85 */     colorMap.put("darkblue", "#00008B");
/*  86 */     colorMap.put("darkcyan", "#008B8B");
/*  87 */     colorMap.put("darkgoldenrod", "#B8860B");
/*  88 */     colorMap.put("darkgray", "#A9A9A9");
/*  89 */     colorMap.put("darkgreen", "#006400");
/*  90 */     colorMap.put("darkkhaki", "#BDB76B");
/*  91 */     colorMap.put("darkmagenta", "#8B008B");
/*  92 */     colorMap.put("darkolivegreen", "#556B2F");
/*  93 */     colorMap.put("darkorange", "#FF8C00");
/*  94 */     colorMap.put("darkorchid", "#9932CC");
/*  95 */     colorMap.put("darkred", "#8B0000");
/*  96 */     colorMap.put("darksalmon", "#E9967A");
/*  97 */     colorMap.put("darkseagreen", "#8FBC8B");
/*  98 */     colorMap.put("darkslateblue", "#483D8B");
/*  99 */     colorMap.put("darkslategray", "#2F4F4F");
/* 100 */     colorMap.put("darkturquoise", "#00CED1");
/* 101 */     colorMap.put("darkviolet", "#9400D3");
/* 102 */     colorMap.put("deeppink", "#FF1493");
/* 103 */     colorMap.put("deepskyblue", "#00BFFF");
/* 104 */     colorMap.put("dimgray", "#696969");
/* 105 */     colorMap.put("dodgerblue", "#1E90FF");
/* 106 */     colorMap.put("firebrick", "#B22222");
/* 107 */     colorMap.put("floralwhite", "#FFFAF0");
/* 108 */     colorMap.put("forestgreen", "#228B22");
/* 109 */     colorMap.put("fuchsia", "#FF00FF");
/* 110 */     colorMap.put("gainsboro", "#DCDCDC");
/* 111 */     colorMap.put("ghostwhite", "#F8F8FF");
/* 112 */     colorMap.put("gold", "#FFD700");
/* 113 */     colorMap.put("goldenrod", "#DAA520");
/* 114 */     colorMap.put("gray", "#808080");
/* 115 */     colorMap.put("green", "#008000");
/* 116 */     colorMap.put("greenyellow", "#ADFF2F");
/* 117 */     colorMap.put("honeydew", "#F0FFF0");
/* 118 */     colorMap.put("hotpink", "#FF69B4");
/* 119 */     colorMap.put("indianred", "#CD5C5C");
/* 120 */     colorMap.put("indigo", "#4B0082");
/* 121 */     colorMap.put("ivory", "#FFFFF0");
/* 122 */     colorMap.put("khaki", "#F0E68C");
/* 123 */     colorMap.put("lavender", "#E6E6FA");
/* 124 */     colorMap.put("lavenderblush", "#FFF0F5");
/* 125 */     colorMap.put("lawngreen", "#7CFC00");
/* 126 */     colorMap.put("lemonchiffon", "#FFFACD");
/* 127 */     colorMap.put("lightblue", "#ADD8E6");
/* 128 */     colorMap.put("lightcoral", "#F08080");
/* 129 */     colorMap.put("lightcyan", "#E0FFFF");
/* 130 */     colorMap.put("lightgoldenrodyellow", "#FAFAD2");
/* 131 */     colorMap.put("lightgreen", "#90EE90");
/* 132 */     colorMap.put("lightgray", "#D3D3D3");
/* 133 */     colorMap.put("lightpink", "#FFB6C1");
/* 134 */     colorMap.put("lightsalmon", "#FFA07A");
/* 135 */     colorMap.put("lightseagreen", "#20B2AA");
/* 136 */     colorMap.put("lightskyblue", "#87CEFA");
/* 137 */     colorMap.put("lightslategray", "#778899");
/* 138 */     colorMap.put("lightsteelblue", "#B0C4DE");
/* 139 */     colorMap.put("lightyellow", "#FFFFE0");
/* 140 */     colorMap.put("lime", "#00FF00");
/* 141 */     colorMap.put("limegreen", "#32CD32");
/* 142 */     colorMap.put("linen", "#FAF0E6");
/* 143 */     colorMap.put("magenta", "#FF00FF");
/* 144 */     colorMap.put("maroon", "#800000");
/* 145 */     colorMap.put("mediumaquamarine", "#66CDAA");
/* 146 */     colorMap.put("mediumblue", "#0000CD");
/* 147 */     colorMap.put("mediumorchid", "#BA55D3");
/* 148 */     colorMap.put("mediumpurple", "#9370DB");
/* 149 */     colorMap.put("mediumseagreen", "#3CB371");
/* 150 */     colorMap.put("mediumslateblue", "#7B68EE");
/* 151 */     colorMap.put("mediumspringgreen", "#00FA9A");
/* 152 */     colorMap.put("mediumturquoise", "#48D1CC");
/* 153 */     colorMap.put("mediumvioletred", "#C71585");
/* 154 */     colorMap.put("midnightblue", "#191970");
/* 155 */     colorMap.put("mintcream", "#F5FFFA");
/* 156 */     colorMap.put("mistyrose", "#FFE4E1");
/* 157 */     colorMap.put("moccasin", "#FFE4B5");
/* 158 */     colorMap.put("navajowhite", "#FFDEAD");
/* 159 */     colorMap.put("navy", "#000080");
/* 160 */     colorMap.put("oldlace", "#FDF5E6");
/* 161 */     colorMap.put("olive", "#808000");
/* 162 */     colorMap.put("olivedrab", "#6B8E23");
/* 163 */     colorMap.put("orange", "#FFA500");
/* 164 */     colorMap.put("orangered", "#FF4500");
/* 165 */     colorMap.put("orchid", "#DA70D6");
/* 166 */     colorMap.put("palegoldenrod", "#EEE8AA");
/* 167 */     colorMap.put("palegreen", "#98FB98");
/* 168 */     colorMap.put("paleturquoise", "#AFEEEE");
/* 169 */     colorMap.put("palevioletred", "#DB7093");
/* 170 */     colorMap.put("papayawhip", "#FFEFD5");
/* 171 */     colorMap.put("peachpuff", "#FFDAB9");
/* 172 */     colorMap.put("peru", "#CD853F");
/* 173 */     colorMap.put("pink", "#FFC0CB");
/* 174 */     colorMap.put("plum", "#DDA0DD");
/* 175 */     colorMap.put("powderblue", "#B0E0E6");
/* 176 */     colorMap.put("purple", "#800080");
/* 177 */     colorMap.put("red", "#FF0000");
/* 178 */     colorMap.put("rosybrown", "#BC8F8F");
/* 179 */     colorMap.put("royalblue", "#4169E1");
/* 180 */     colorMap.put("saddlebrown", "#8B4513");
/* 181 */     colorMap.put("salmon", "#FA8072");
/* 182 */     colorMap.put("sandybrown", "#F4A460");
/* 183 */     colorMap.put("seagreen", "#2E8B57");
/* 184 */     colorMap.put("seashell", "#FFF5EE");
/* 185 */     colorMap.put("sienna", "#A0522D");
/* 186 */     colorMap.put("silver", "#C0C0C0");
/* 187 */     colorMap.put("skyblue", "#87CEEB");
/* 188 */     colorMap.put("slateblue", "#6A5ACD");
/* 189 */     colorMap.put("slategray", "#708090");
/* 190 */     colorMap.put("snow", "#FFFAFA");
/* 191 */     colorMap.put("springgreen", "#00FF7F");
/* 192 */     colorMap.put("steelblue", "#4682B4");
/* 193 */     colorMap.put("tan", "#D2B48C");
/* 194 */     colorMap.put("teal", "#008080");
/* 195 */     colorMap.put("thistle", "#D8BFD8");
/* 196 */     colorMap.put("tomato", "#FF6347");
/* 197 */     colorMap.put("turquoise", "#40E0D0");
/* 198 */     colorMap.put("violet", "#EE82EE");
/* 199 */     colorMap.put("wheat", "#F5DEB3");
/* 200 */     colorMap.put("white", "#FFFFFF");
/* 201 */     colorMap.put("whitesmoke", "#F5F5F5");
/* 202 */     colorMap.put("yellow", "#FFFF00");
/* 203 */     colorMap.put("yellowgreen", "#9ACD32");
/*     */   }
/*     */ 
/*     */   public static Color invert(Color c)
/*     */   {
/*  22 */     return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
/*     */   }
/*     */   public static String colorToStr(Color color, boolean withAlpha) {
/*  25 */     if (color != null) {
/*     */       try {
/*  27 */         StringBuffer sb = new StringBuffer("#");
/*  28 */         sb.append(StrUtil.lpad(Integer.toHexString(color.getRed()), 2, "0"));
/*  29 */         sb.append(StrUtil.lpad(Integer.toHexString(color.getGreen()), 2, "0"));
/*  30 */         sb.append(StrUtil.lpad(Integer.toHexString(color.getBlue()), 2, "0"));
/*  31 */         if ((withAlpha) && (color.getAlpha() < 255)) {
/*  32 */           sb.append(StrUtil.lpad(Integer.toHexString(color.getAlpha()), 2, "0"));
/*     */         }
/*  34 */         return sb.toString();
/*     */       } catch (Exception e) {
/*  36 */         return "#ffffff";
/*     */       }
/*     */     }
/*  39 */     return "";
/*     */   }
/*     */ 
/*     */   public static String colorToStr(Color color)
/*     */   {
/*  44 */     return colorToStr(color, true);
/*     */   }
/*     */   public static boolean isColor(String str) {
/*  47 */     if (!colorMap.containsKey(str)) {
/*  48 */       if (((str.length() == 7) || (str.length() == 9)) && (str.charAt(0) == '#')) {
/*  49 */         str = str.toLowerCase();
/*  50 */         for (int i = 1; i < str.length(); i++)
/*  51 */           if (((str.charAt(i) < '0') || (str.charAt(i) > '9')) && (
/*  52 */             (str.charAt(i) < 'a') || (str.charAt(i) > 'f')))
/*  53 */             return false;
/*     */       }
/*     */       else
/*     */       {
/*  57 */         return false;
/*     */       }
/*     */     }
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   public static Color strToColor(String str, Color defColor)
/*     */   {
/*     */     try
/*     */     {
/* 207 */       if (!str.startsWith("#")) {
/* 208 */         if (colorMap.containsKey(str.toLowerCase())) {
/* 209 */           str = (String)colorMap.get(str.toLowerCase()); } else {
/* 210 */           if ((str.startsWith("rgb(")) && (str.endsWith(")"))) {
/* 211 */             String[] strs = str.substring(4, str.length() - 1).split(",");
/* 212 */             int[] c = { 0, 0, 0, 255 };
/* 213 */             for (int i = 0; (i < strs.length) && (i < c.length); i++) {
/* 214 */               c[i] = (To.toInt(strs[i]) % 256);
/*     */             }
/* 216 */             return new Color(c[0], c[1], c[2], c[3]);
/*     */           }
/* 218 */           return defColor;
/*     */         }
/*     */       }
/* 221 */       int[] c = { 0, 0, 0, 255 };
/* 222 */       String[] strs = StrUtil.splitn(str.substring(1), 2);
/* 223 */       for (int i = 0; (i < strs.length) && (i < c.length); i++) {
/* 224 */         c[i] = Integer.decode("0x" + strs[i]).intValue();
/*     */       }
/* 226 */       return new Color(c[0], c[1], c[2], c[3]); } catch (Exception e) {
/*     */     }
/* 228 */     return defColor;
/*     */   }
/*     */ 
/*     */   public static Color mix(Color c1, Color c2, double weight) {
/* 232 */     return new Color(mix(c1.getRed(), c2.getRed(), weight),
/* 233 */       mix(c1.getGreen(), c2.getGreen(), weight), 
/* 234 */       mix(c1.getBlue(), c2.getBlue(), weight), 
/* 235 */       mix(c1.getAlpha(), c2.getAlpha(), weight));
/*     */   }
/*     */   private static int mix(int n1, int n2, double weight) {
/* 238 */     return (int) Math.round(n1 * (1.0D - weight) + n2 * weight);
/*     */   }
/*     */ }

/* Location:           C:\Users\admin\Desktop\xdoc.jar
 * Qualified Name:     com.hg.util.ColorUtil
 * JD-Core Version:    0.6.0
 */