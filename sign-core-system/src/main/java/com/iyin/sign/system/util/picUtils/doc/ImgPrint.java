
package com.iyin.sign.system.util.picUtils.doc;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

public class ImgPrint {

    public static void write(XDoc doc, OutputStream out, String format) throws PrinterException, IOException {

        DocPrint docPrint = new DocPrint(doc, !(out instanceof ZipOutputStream));
        DocPaper docPaper = doc.getPaper();

        int pageWidth = docPaper.viewWidth();
        int pageHeight = docPaper.viewHeight();
        if (pageWidth == 0) pageWidth = 1;

        if (pageHeight == 0) pageHeight = 1;

        doc.print = docPrint;

        docPrint.hrefs.clear();

        docPrint.toolTips.clear();

        docPrint.inputs.clear();

        BufferedImage img = new BufferedImage(pageWidth, pageHeight, 1);

        Graphics2D g2 = img.createGraphics();

        g2.setColor(Color.WHITE);

        g2.fillRect(0, 0, pageWidth, pageHeight);

        ImgUtil.setImgRenderHint(g2);

        docPrint.print(g2, null, 0);

        g2.dispose();

        ImageIO.write(img, format, out);

        out.close();

    }

}
