//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;



import com.itextpdf.awt.FontMapper;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.BaseFont;

import java.awt.*;

class XFontMapper implements FontMapper {
    XFontMapper() {
    }

    public BaseFont awtToPdf(Font font) {
        try {
            String e = XFont.getFontFilePath(font);
            if(e != null) {
                return BaseFont.createFont(e, "Identity-H", true);
            }
        } catch (Exception var4) {
            ;
        }

        try {
            return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
        } catch (Exception var3) {
            throw new ExceptionConverter(var3);
        }
    }

    public Font pdfToAwt(BaseFont bfont, int size) {
        return XFont.createFont(XFont.defaultFontName, 0, (float)size);
    }
}
