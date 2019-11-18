//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;
import com.iyin.sign.system.util.picUtils.util.HgException;
import com.iyin.sign.system.util.picUtils.util.StrUtil;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PdfPrint {
    public PdfPrint() {
    }

    public static void write(XDoc doc, OutputStream out) throws HgException {
        double oldScale = doc.scale;

        try {
            doc.scale = 0.75D;
            DocPrint e = new DocPrint(doc);
            doc.print = e;
            DocPaper docPaper = doc.getPaper();
            int paperWidth = docPaper.viewWidth();
            int paperHeight = docPaper.viewHeight();
            Document pdf = new Document(new Rectangle(0.0F, 0.0F, (float)paperWidth, (float)paperHeight), 0.0F, 0.0F, 0.0F, 0.0F);
            PdfWriter pdfwriter = PdfWriter.getInstance(pdf, out);
            pdfwriter.setCloseStream(false);
            pdf.addAuthor(doc.getMeta().getAuthor());
            pdf.addSubject(doc.getMeta().getDesc());
            pdf.addTitle(doc.getMeta().getTitle());
            pdf.addCreator("XDOC A.3.6");
            pdf.addKeywords(XDocXml.toZipDataURI(doc));
            pdf.open();
            PdfContentByte pcb = pdfwriter.getDirectContent();
            doc.heads.clear();
            byte[] bimg = null;
            Graphics2D g2;
            if(doc.pages <= 0) {
                pdf.newPage();
                g2 = pcb.createGraphicsShapes((float)paperWidth, (float)paperHeight);
                g2.dispose();
            } else {
                XFontMapper xfm = new XFontMapper();

                for(int i = 0; i < doc.pages; ++i) {
                    pdf.newPage();
                    g2 = pcb.createGraphics((float)paperWidth, (float)paperHeight, xfm);
                    ImgUtil.setRenderHint(g2);
                    e.hrefs.clear();
                    e.toolTips.clear();
                    e.inputs.clear();
                    e.print(g2, (PageFormat)null, i);
                    clearOverlapTips(e);

                    Image img;
                    NameShape ns;
                    java.awt.Rectangle rect;
                    int j;
                    for(j = 0; j < e.hrefs.size(); ++j) {
                        ns = (NameShape)e.hrefs.get(j);
                        rect = ns.shape.getBounds();
                        if(ns.name.startsWith("@:")) {
                            if(bimg == null) {
                                bimg = getAnnotationImg();
                            }

                            img = Image.getInstance(bimg);
                            Chunk chunk = new Chunk(img, (float)rect.x, (float)(paperHeight - rect.y - rect.height));
                            chunk.setLocalDestination(ns.name.substring("@:".length()));
                            pdf.add(chunk);
                        } else {
                            PdfAction action;
                            if(ns.name.startsWith("#")) {
                                action = PdfAction.gotoLocalPage(ns.name.substring(1), false);
                            } else {
                                if(ns.name.indexOf(10) > 0) {
                                    ns.name = ns.name.split("\n")[0];
                                }

                                action = new PdfAction(ns.name, false);
                            }

                            pdfwriter.addAnnotation(new PdfAnnotation(pdfwriter, (float)rect.x, (float)(paperHeight - rect.y - rect.height), (float)(rect.x + rect.width), (float)(paperHeight - rect.y), action));
                        }
                    }

                    for(j = 0; j < e.toolTips.size(); ++j) {
                        ns = (NameShape)e.toolTips.get(j);
                        rect = ns.shape.getBounds();
                        if(bimg == null) {
                            bimg = getAnnotationImg();
                        }

                        img = Image.getInstance(bimg);
                        img.setAnnotation(new Annotation(0.0F, 0.0F, (float)rect.width, (float)rect.height, "Tip:" + StrUtil.URLEncode(ns.name)));
                        pcb.addImage(img, (float)rect.width, 0.0F, 0.0F, (float)rect.height, (float)rect.x, (float)(paperHeight - rect.y - rect.height));
                    }

                    g2.dispose();
                }
            }

            if(doc.heads != null && doc.heads.size() > 0) {
                DocUtil.fixHeads(doc);
                writePdfHeads(pcb.getRootOutline(), doc.heads, pdfwriter, paperHeight);
            }

            pdf.close();
        } catch (Exception var24) {
            throw new HgException(var24);
        } finally {
            doc.scale = oldScale;
        }

    }

    private static void clearOverlapTips(DocPrint docPrint) {
        for(int i = 0; i < docPrint.hrefs.size(); ++i) {
            NameShape ns = (NameShape)docPrint.hrefs.get(i);

            for(int j = 0; j < docPrint.toolTips.size(); ++j) {
                if(ns.shape.getBounds().equals(((NameShape)docPrint.hrefs.get(j)).shape.getBounds())) {
                    docPrint.toolTips.remove(j);
                    --j;
                }
            }
        }

    }

    private static byte[] getAnnotationImg() throws IOException {
        BufferedImage img2 = new BufferedImage(1, 1, 2);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ImageIO.write(img2, "png", bout);
        return bout.toByteArray();
    }

    private static void writePdfHeads(PdfOutline parent, ArrayList cheads, PdfWriter writer, int h) {
        for(int i = 0; i < cheads.size(); ++i) {
            Heading heading = (Heading)cheads.get(i);
            PdfOutline child = new PdfOutline(parent, PdfAction.gotoLocalPage(heading.page + 1, new PdfDestination(0, (float)heading.x, (float)(h - heading.y), 0.0F), writer), heading.name());
            if(heading.cheads != null) {
                writePdfHeads(child, heading.cheads, writer, h);
            }
        }

    }
}
