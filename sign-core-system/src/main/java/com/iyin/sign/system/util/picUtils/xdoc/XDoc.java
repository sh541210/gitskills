//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.xdoc;
import com.iyin.sign.system.util.picUtils.doc.ImgPrint;
import com.iyin.sign.system.util.picUtils.doc.PdfPrint;
import com.iyin.sign.system.util.picUtils.doc.XDocXml;
import com.iyin.sign.system.util.picUtils.util.HgException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.print.PrinterException;
import java.io.*;

public class XDoc extends Container {
    protected static final Logger logger = LoggerFactory.getLogger(XDoc.class);
    private static String utf8="UTF-8";

    private Meta meta = new Meta();
    private Paper paper = new Paper();
    private Rect front = new Rect("front");
    private Rect back = new Rect("back");
    private Rect body = new Rect("body");

    public XDoc() {
        super("xdoc");
        this.setAttribute("version", "A.3.6");
        this.add(this.meta);
        this.add(this.paper);
        this.add(this.front);
        this.add(this.back);
        this.add(this.body);
    }

    public Meta getMeta() {
        return this.meta;
    }

    public Paper getPaper() {
        return this.paper;
    }

    public Rect getBack() {
        this.back.setWidth(this.paper.getWidth());
        this.back.setHeight(this.paper.getHeight());
        return this.back;
    }

    public Rect getFront() {
        this.front.setWidth(this.paper.getWidth());
        this.front.setHeight(this.paper.getHeight());
        return this.front;
    }

    public Rect getBody() {
        return this.body;
    }

    public Document toDocument() throws ParserConfigurationException {
        DocumentBuilderFactory e = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = e.newDocumentBuilder();
        Document document = builder.newDocument();
        document.appendChild(this.toElement(document));
        return document;
    }

    public String toXml() throws ParserConfigurationException,IOException {
        ByteArrayOutputStream e = new ByteArrayOutputStream();
        this.write((OutputStream) e);
        return new String(e.toByteArray(), utf8);
    }

    public void write(OutputStream out) throws ParserConfigurationException,IOException {
        try {
            TransformerFactory e1 = TransformerFactory.newInstance();
            e1.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = e1.newTransformer();
            DOMSource source = new DOMSource(this.toDocument());
            transformer.setOutputProperty("encoding", utf8);
            transformer.setOutputProperty("indent", "yes");
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
            out.flush();
        } catch (TransformerException var6) {
            throw new IOException(var6);
        }
    }

    public void write(File file) throws Exception {
        write(this.toXml(), file);
    }

    public void write(OutputStream out, String format) throws Exception {
        if (format.equals("xdoc")) {
            this.write(out);
        } else {
            write(this.toXml(), out, format);
        }

    }

    public static void write(String xdoc, File file) throws Exception {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            write(xdoc, fout, getFormat(file.getName()));
            fout.flush();
        }catch (IOException ioe){
            logger.error("IOException",ioe);
        }finally {
            if (fout!=null)
                fout.close();
        }

    }

    public static void write(String xdoc, OutputStream out, String format) throws IOException, PrinterException,HgException {
        if (format.equals("pdf")) {
            PdfPrint.write(XDocXml.parseXml(xdoc), out);
        } else if (!format.equals("png") && !format.equals("jpeg")) {//若不是png或者jpeg文件，直接输出字符流
            out.write(xdoc.getBytes("UTF-8"));
        } else {//此处用来写png和jpeg的图片输出
            ImgPrint.write(XDocXml.parseXml(xdoc), out, format);
        }

    }

    private static String getFormat(String url) {
        String format = "xdoc";
        if(StringUtils.isNotBlank(url)) {
            int pos = url.lastIndexOf('.');
            if (pos > 0) {
                format = url.substring(pos + 1).toLowerCase();
            }
        }
        return format;
    }
}
