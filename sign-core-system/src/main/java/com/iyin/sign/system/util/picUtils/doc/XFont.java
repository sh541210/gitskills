//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;

import com.iyin.sign.system.util.picUtils.util.To;
import com.iyin.sign.system.util.picUtils.util.XUrl;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class XFont {
    private static final String[] INNER_FONT_FILES = new String[]{"songti.ttf", "biaosong.ttf", "fangsong.ttf", "heiti.ttf", "cuhei.ttf", "xihei.ttf", "kaiti.ttf", "xingkai.ttf", "lishu.ttf", "weibei.ttf","minijiancufangsong.ttf"};
    private static final String[] INNER_FONT_NAMES = new String[]{"宋体", "标宋", "仿宋", "黑体", "粗黑", "细黑", "楷体", "行楷", "隶书", "魏碑","minijiancufangsong"};
    private static final String[] INNER_FONT_NAMES_EN = new String[]{"SongTi", "BiaoSong", "FangSong", "HeiTi", "CuHei", "XiHei", "KaiTi", "XingKai", "LiShu", "WeiBei","minijiancufangsong"};
//    private static final String[] INNER_FONT_FILES = new String[]{"songti.ttf", "biaosong.ttf", "fangsong.ttf", "heiti.ttf", "cuhei.ttf", "xihei.ttf", "kaiti.ttf", "xingkai.ttf", "lishu.ttf", "weibei.ttf"};
//    private static final String[] INNER_FONT_NAMES = new String[]{"宋体", "标宋", "仿宋", "黑体", "粗黑", "细黑", "楷体", "行楷", "隶书", "魏碑"};
//    private static final String[] INNER_FONT_NAMES_EN = new String[]{"SongTi", "BiaoSong", "FangSong", "HeiTi", "CuHei", "XiHei", "KaiTi", "XingKai", "LiShu", "WeiBei"};
    private static String[] USER_FONT_FILES = new String[0];
    private static String[] USER_FONT_NAMES = new String[0];
    private static String[] USER_FONT_NAMES_EN = new String[0];
    private static Map fontMap = new HashMap();
    private static Map fontPathMap = new HashMap();
    private static Font superFont;
    public static String superFontName;
    public static String defaultFontName;
    public static String titleFontName;
    public static int defaultFontSize;
    private static Map sizeMap;
    public static final int[] FONT_SIZE;
    public static final String[] FONT_SIZE_CN;
    public static final String[] FONT_SIZE_EN;

    static {
        superFont = new Font("Dialog", 0, defaultFontSize);
        superFontName = "黑体";
        defaultFontName = "宋体";
        titleFontName = "标宋";
        defaultFontSize = 14;
        FONT_SIZE = new int[]{56, 48, 34, 32, 29, 24, 21, 20, 18, 16, 14, 12, 10, 8, 7, 6};
        FONT_SIZE_CN = new String[]{"初号", "小初", "一号", "小一", "二号", "小二", "三号", "小三", "四号", "小四", "五号", "小五", "六号", "小六", "七号", "八号"};
        FONT_SIZE_EN = new String[]{"42pt", "36pt", "26pt", "24pt", "22pt", "18pt", "16pt", "15pt", "14pt", "12pt", "10.5pt", "9pt", "7.5pt", "6.5pt", "5.5pt", "5pt"};
        sizeMap = new HashMap();

        for(int i = 0; i < FONT_SIZE_CN.length; ++i) {
            sizeMap.put(FONT_SIZE_CN[i], new Integer(FONT_SIZE[i]));
            sizeMap.put(FONT_SIZE_EN[i], new Integer(FONT_SIZE[i]));
        }

    }

    public XFont() {
    }

    public static void init(String fontPath) {
        init(fontPath, (String[])null);
    }

    public static void init(String fontPath, String[] userFonts) {
        fontMap.clear();
        fontPathMap.clear();
        fontPath = XUrl.fixUrl(fontPath);
        File file = new File(fontPath);
        int strs;
        int i;
        String[] var8;
        if(file.exists() && file.isDirectory()) {
            for(strs = 0; strs < INNER_FONT_NAMES.length; ++strs) {
                loadFont(fontPath, INNER_FONT_FILES[strs], INNER_FONT_NAMES[strs], INNER_FONT_NAMES_EN[strs]);
            }

            if(userFonts == null) {
                File[] var9 = file.listFiles();
                boolean var10 = false;
                ArrayList i1 = new ArrayList();
                int i2 = 0;

                label98:
                while(true) {
                    if(i2 >= var9.length) {
                        userFonts = new String[i1.size()];
                        Collections.sort(i1);
                        i2 = 0;

                        while(true) {
                            if(i2 >= i1.size()) {
                                break label98;
                            }

                            userFonts[i2] = i1.get(i2) + ":";
                            ++i2;
                        }
                    }

                    if(var9[i2].getName().toLowerCase().endsWith(".ttf")) {
                        var10 = false;

                        for(int j = 0; j < INNER_FONT_FILES.length; ++j) {
                            if(var9[i2].getName().equals(INNER_FONT_FILES[j])) {
                                var10 = true;
                                break;
                            }
                        }

                        if(!var10) {
                            i1.add(var9[i2].getName());
                        }
                    }

                    ++i2;
                }
            }

            USER_FONT_FILES = new String[userFonts.length];
            USER_FONT_NAMES = new String[userFonts.length];
            USER_FONT_NAMES_EN = new String[userFonts.length];

            for(i = 0; i < userFonts.length; ++i) {
                var8 = userFonts[i].split(":");
                USER_FONT_FILES[i] = var8[0];
                if(var8.length > 1) {
                    USER_FONT_NAMES[i] = var8[1];
                    USER_FONT_NAMES_EN[i] = var8[1];
                } else {
                    USER_FONT_NAMES[i] = "";
                    USER_FONT_NAMES_EN[i] = "";
                }
            }

            for(int var11 = 0; var11 < USER_FONT_NAMES.length; ++var11) {
                String[] var12 = loadFont(fontPath, USER_FONT_FILES[var11], USER_FONT_NAMES[var11], USER_FONT_NAMES_EN[var11]);
                USER_FONT_NAMES[var11] = var12[0];
                USER_FONT_NAMES_EN[var11] = var12[1];
            }
        } else {
            for(strs = 0; strs < INNER_FONT_NAMES.length; ++strs) {
                registFont(INNER_FONT_NAMES[strs], INNER_FONT_NAMES_EN[strs], new Font(toWinName(INNER_FONT_NAMES[strs]), 0, defaultFontSize));
            }

            if(userFonts != null) {
                USER_FONT_FILES = new String[userFonts.length];
                USER_FONT_NAMES = new String[userFonts.length];
                USER_FONT_NAMES_EN = new String[userFonts.length];

                for(i = 0; i < userFonts.length; ++i) {
                    var8 = userFonts[i].split(":");
                    USER_FONT_FILES[i] = var8[0];
                    if(var8.length > 1) {
                        USER_FONT_NAMES[i] = var8[1];
                        USER_FONT_NAMES_EN[i] = var8[1];
                    } else {
                        USER_FONT_NAMES[i] = "";
                        USER_FONT_NAMES_EN[i] = "";
                    }

                    registFont(USER_FONT_NAMES[i], USER_FONT_NAMES_EN[i], new Font(toWinName(USER_FONT_NAMES[i]), 0, defaultFontSize));
                }
            }
        }

        defaultFontName = "SongTi";
        superFontName = "HeiTi";
        titleFontName = "BiaoSong";
        if(fontMap.containsKey(superFontName)) {
            superFont = (Font)fontMap.get(superFontName);
        }

    }

    private static String[] loadFont(String fontPath, String fontFile, String fontName, String fontName2) {
        File file = new File(fontPath + fontFile);
        Font font;
        if(file.exists()) {
            font = createFont(file);
            if(font != null) {
                if(fontName.length() == 0) {
                    fontName = font.getFontName(Locale.CHINA);
                    fontName2 = font.getFontName(Locale.ENGLISH);
                }

                registFont(fontName, fontName2, font);
                fontPathMap.put(font.getFontName(Locale.ENGLISH), file.getAbsolutePath());
            }
        }

        if(fontName.length() == 0) {
            if(fontFile.toLowerCase().endsWith(".ttf")) {
                fontFile = fontFile.substring(0, fontFile.length() - 4);
            }

            fontName = fontFile;
            fontName2 = fontFile;
        }

        if(!fontMap.containsKey(fontName)) {
            font = new Font(toWinName(fontName), 0, defaultFontSize);
            registFont(fontName, fontName2, font);
        }

        return new String[]{fontName, fontName2};
    }

    private static void registFont(String logicName, String logicName2, Font font) {
        fontMap.put(logicName, font);
        if(logicName2 != null) {
            fontMap.put(logicName2, font);
        }

        fontMap.put(font.getFontName(Locale.ENGLISH), font);
        fontMap.put(font.getFontName(Locale.CHINA), font);
    }

    public static Font createFont(File file) {
        Font font = null;

        try {
            font = Font.createFont(0, file);
        } catch (NoSuchMethodError var5) {
            try {
                font = Font.createFont(0, new FileInputStream(file));
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return font;
    }

    public static String getFontFilePath(Font font) {
        return (String)fontPathMap.get(font.getFontName(Locale.ENGLISH));
    }

    public static Font createFont(String name, int style, float size) {
        if(name.length() == 0) {
            name = defaultFontName;
        }

        Font font = (Font)fontMap.get(name);
        if(font != null) {
            font = font.deriveFont(style, size);
        } else {
            font = new Font(name, style, (int)size);
        }

        return font;
    }

    public static Font createFont(String fontName, int style, int size, String str) {
        Font font = createFont(fontName, style, (float)size);
        if(!canDisplay(font, str)) {
            font = superFont.deriveFont(style, (float)size);
        }

        return font;
    }

    public static boolean canDisplay(Font font, String str) {
        if(str.length() > 0 && !font.getFontName(Locale.ENGLISH).startsWith("Source Han Sans CN")) {
            for(int i = 0; i < str.length(); ++i) {
                if(!font.canDisplay(str.charAt(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static String toInnerName(String name) {
        if(name != null && name.length() != 0) {
            if(!fontMap.containsKey(name)) {
                if(name.indexOf("宋") >= 0) {
                    if(name.indexOf("仿宋") >= 0) {
                        name = "仿宋";
                    } else if(name.indexOf("标宋") < 0 && name.indexOf("粗宋") < 0 && name.indexOf("中宋") < 0) {
                        name = "宋体";
                    } else {
                        name = "标宋";
                    }
                } else if(name.indexOf("黑") >= 0) {
                    if(name.indexOf("粗黑") >= 0) {
                        name = "粗黑";
                    } else if(name.indexOf("细黑") >= 0) {
                        name = "细黑";
                    } else {
                        name = "黑体";
                    }
                } else if(name.indexOf("楷") >= 0) {
                    if(name.indexOf("行楷") >= 0) {
                        name = "行楷";
                    } else {
                        name = "楷体";
                    }
                } else if(name.indexOf("隶") >= 0) {
                    name = "隶书";
                } else if(name.indexOf("魏") >= 0) {
                    name = "魏碑";
                } else {
                    name = defaultFontName;
                }
            }
        } else {
            name = defaultFontName;
        }

        return name;
    }

    public static String toWinName(String name) {
        if(name.equals("仿宋")) {
            name = "华文仿宋";
        } else if(name.equals("标宋")) {
            name = "华文中宋";
        } else if(name.equals("魏碑")) {
            name = "华文新魏";
        } else if(name.equals("行楷")) {
            name = "华文行楷";
        } else if(name.equals("楷体")) {
            name = "华文楷体";
        } else if(name.equals("细黑")) {
            name = "华文细黑";
        } else if(name.indexOf("黑") >= 0) {
            name = "黑体";
        }

        return name;
    }

    public static int parseFontSize(String strSize) {
        int fontSize = defaultFontSize;
        strSize = strSize.toLowerCase();
        if(sizeMap.containsKey(strSize)) {
            fontSize = ((Integer)sizeMap.get(strSize)).intValue();
        } else {
            fontSize = To.toInt(strSize, fontSize);
        }

        if(fontSize <= 0) {
            fontSize = defaultFontSize;
        }

        return fontSize;
    }

    public static Font createDefaultFont() {
        return createFont(defaultFontName, 0, (float)defaultFontSize);
    }

    public static Font createOSFont(String name, int style, int size) {
        return createOSFont(name, style, size, "");
    }

    public static Font createOSFont(String name, int style, int size, String text) {
        Font font = new Font(name, 0, size);

        for(int i = 0; i < text.length(); ++i) {
            if(!font.canDisplay(text.charAt(i))) {
                font = new Font("Dialog", 0, size);
            }
        }

        return font;
    }
}
