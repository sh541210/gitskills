//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;
import com.iyin.sign.system.util.picUtils.util.*;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class XDocXml {
    private static HashMap stdAttsMap = new HashMap();

    public XDocXml() {
    }

    public static XDoc read(String urlStr) throws HgException {
        try {
            XDoc e = read((new XUrl(urlStr)).getInputStream());
            e.url = urlStr;
            return e;
        } catch (Exception var2) {
            throw new HgException(var2);
        }
    }

    public static XDoc parseXmlDoc(Element root) throws HgException {
        XDoc xdoc = new XDoc();
        String cause;
        if(root.getName().equals("xdoc")) {
            Iterator msg = root.elementIterator();
            HashMap styles = null;

            while(true) {
                while(msg.hasNext()) {
                    Element ele = (Element)msg.next();
                    cause = ele.getName();
                    if(cause.equals("body")) {
                        attMap(ele, styles);
                        EleBase docEle = toDocEle(xdoc, ele, styles);

                        for(int var18 = 0; var18 < docEle.eleList.size(); ++var18) {
                            EleBase var16 = (EleBase)docEle.eleList.get(var18);
                            if(var16 instanceof ElePara) {
                                if(var16.eleList.size() == 0) {
                                    var16.eleList.add(new EleText(xdoc));
                                }

                                xdoc.paraList.add(var16);
                            } else if(var16 instanceof EleText) {
                                ElePara var19 = new ElePara(xdoc);
                                var19.eleList.add(var16);
                                xdoc.paraList.add(var19);
                            } else {
                                xdoc.rectList.add(var16);
                            }
                        }

                        xdoc.bodyRect = (EleRect)docEle;
                        xdoc.bodyRect.eleList.clear();
                    } else if(cause.equals("back")) {
                        xdoc.backRect = (EleRect)toDocEle(xdoc, ele, styles);
                        xdoc.backRect.width = xdoc.getPaper().width;
                        xdoc.backRect.height = xdoc.getPaper().height;
                    } else if(cause.equals("front")) {
                        xdoc.frontRect = (EleRect)toDocEle(xdoc, ele, styles);
                        xdoc.frontRect.width = xdoc.getPaper().width;
                        xdoc.frontRect.height = xdoc.getPaper().height;
                    } else if(cause.equals("meta")) {
                        xdoc.metaMap.putAll(attMap(ele, styles));
                        xdoc.metaMap.remove("TEXT");
                        if("slide".equals(xdoc.getMeta("view"))) {
                            xdoc.metaMap.put("view", "page");
                        }

                        xdoc.scale = To.toDouble(xdoc.getMeta("scale", "1"), 1.0D);
                    } else if(cause.equals("paper")) {
                        DocPaper var15 = xdoc.getPaper();
                        HashMap attMap = attMap(ele, styles);
                        var15.setMargin(MapUtil.getInt(attMap, "margin", DocPaper.DEFAULT_MARGIN));
                        if(attMap.containsKey("size")) {
                            DocPaper var17 = new DocPaper(xdoc, MapUtil.getString(attMap, "size", ""));
                            var15.width = var17.width;
                            var15.height = var17.height;
                            var15.setMargin(MapUtil.getInt(attMap, "margin", var15.getTopMargin()));
                        }

                        var15.width = MapUtil.getInt(attMap, "width", var15.width);
                        var15.height = MapUtil.getInt(attMap, "height", var15.height);
                        var15.setTopMargin(MapUtil.getInt(attMap, "topMargin", var15.getTopMargin()));
                        var15.setLeftMargin(MapUtil.getInt(attMap, "leftMargin", var15.getLeftMargin()));
                        var15.setRightMargin(MapUtil.getInt(attMap, "rightMargin", var15.getRightMargin()));
                        var15.setBottomMargin(MapUtil.getInt(attMap, "bottomMargin", var15.getBottomMargin()));
                    } else if(cause.equals("styles")) {
                        styles = new HashMap();
                        Iterator its = ele.elementIterator();

                        while(its.hasNext()) {
                            ele = (Element)its.next();
                            HashMap map = attMap(ele, styles);
                            String[] styleName = MapUtil.getString(map, "name", "").split(",");

                            for(int i = 0; i < styleName.length; ++i) {
                                if(styleName[i].length() > 0) {
                                    styles.put(styleName[i], map);
                                    map.remove("name");
                                }
                            }
                        }
                    }
                }

                return xdoc;
            }
        } else if(!root.getName().equals("response")) {
            Object var12 = toDocEle(xdoc, root, (Map)null);
            if(var12 instanceof EleRect) {
                if(DocUtil.isBlank((EleRect)var12)) {
                    xdoc.paraList.addAll(((EleRect)var12).getParaList());
                    xdoc.rectList.addAll(((EleRect)var12).getRectList());
                } else {
                    xdoc.rectList.add((EleRect)var12);
                }

                xdoc.getPaper().setMargin(0);
                xdoc.bodyRect.sizeType = "autosize";
            } else {
                if(var12 instanceof EleText) {
                    ElePara var13 = new ElePara(xdoc);
                    var13.eleList.add(var12);
                    var12 = var13;
                }

                if(var12 instanceof ElePara) {
                    xdoc.paraList.add(var12);
                }
            }
        } else if("false".equals(root.attributeValue("success"))) {
            String var14 = "";
            cause = "";
            if(root.element("error") != null) {
                var14 = root.element("error").getText();
            }

            if(root.element("cause") != null) {
                cause = root.element("cause").getText();
            }

            throw new HgException(var14, new Exception(cause));
        }

        return xdoc;
    }

    public static XDoc parseXml(String xml) throws HgException {
        Document doc = null;

        try {
            doc = XmlUtil.parseText(xml);
        } catch (DocumentException var3) {
            throw new HgException(var3);
        }

        return parseXmlDoc(doc.getRootElement());
    }

    public static XDoc read(InputStream in) throws HgException {
        try {
            return parseXmlDoc(XmlUtil.createSAXReader().read(in).getRootElement());
        } catch (Exception var2) {
            throw new HgException(var2);
        }
    }

    public static XDoc read(Reader reader) throws HgException {
        try {
            return parseXmlDoc(XmlUtil.createSAXReader().read(reader).getRootElement());
        } catch (Exception var2) {
            throw new HgException(var2);
        }
    }

    public static String toXml(XDoc xdoc) {
        return toXml(xdoc, false);
    }

    public static String toXml(XDoc xdoc, boolean pretty) {
        return XmlUtil.toXml(toXmlDoc(xdoc), pretty);
    }

    public static void write(XDoc xdoc) throws HgException {
        write(xdoc, xdoc.url);
    }

    public static void write(XDoc xdoc, String urlStr) throws HgException {
        OutputStream out = getOutputStream(urlStr);

        try {
            write(xdoc, out);
        } catch (HgException var11) {
            throw var11;
        } finally {
            try {
                out.close();
            } catch (IOException var10) {
                ;
            }

        }

    }

    public static void write(XDoc xdoc, OutputStream out) throws HgException {
        try {
            XDocXMLWriter e;
            if(DocUtil.prettyFormat) {
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setTrimText(false);
                e = new XDocXMLWriter(out, format);
            } else {
                e = new XDocXMLWriter(out);
            }

            e.write(toXmlDoc(xdoc));
            e.flush();
            out.flush();
        } catch (Exception var4) {
            throw new HgException(var4);
        }
    }

    public static void write(XDoc xdoc, Writer writer) throws HgException {
        try {
            OutputFormat e = OutputFormat.createPrettyPrint();
            e.setTrimText(false);
            XDocXMLWriter xmlwriter = new XDocXMLWriter(writer, e);
            xmlwriter.write(toXmlDoc(xdoc));
            xmlwriter.flush();
            writer.flush();
        } catch (Exception var4) {
            throw new HgException(var4);
        }
    }

    public static void writeZip(XDoc doc, OutputStream out) throws HgException {
        try {
            ZipOutputStream e = new ZipOutputStream(out);
            e.putNextEntry(new ZipEntry("xdoc.xdoc"));
            write(doc, (OutputStream)e);
            e.close();
        } catch (Exception var3) {
            throw new HgException(var3);
        }
    }

    public static String toZipDataURI(XDoc xdoc) throws HgException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        writeZip(xdoc, bout);
        return "data:application/zip;base64," + StrUtil.toBase64(bout.toByteArray());
    }

    public static Document toXmlDoc(XDoc xdoc) {
        Document doc = DocumentHelper.createDocument();
        toXmlDoc(xdoc, doc);
        return doc;
    }

    private static void toXmlDoc(XDoc xdoc, Branch doc) {
        Element root = doc.addElement("xdoc");
        if(root.isRootElement()) {
            root.addAttribute("version", "A.3.6");
        }

        Element ele = root.addElement("meta");
        if(xdoc.metaMap.containsKey("createDate")) {
            xdoc.metaMap.put("modifyDate", DateUtil.getDateTimeString());
        } else {
            xdoc.metaMap.put("createDate", DateUtil.getDateTimeString());
        }

        Iterator it = xdoc.metaMap.keySet().iterator();

        while(true) {
            String key;
            do {
                do {
                    do {
                        if(!it.hasNext()) {
                            Element paper = xdoc.getPaper().toEle();
                            if(paper != null) {
                                root.add(paper);
                            }

                            toBFBEle(root, xdoc.frontRect, "front");
                            toBFBEle(root, xdoc.backRect, "back");
                            Element body = toBFBEle(root, xdoc.bodyRect, "body");

                            int i;
                            for(i = 0; i < xdoc.paraList.size(); ++i) {
                                body.add(toXmlEle((EleBase)xdoc.paraList.get(i)));
                            }

                            for(i = 0; i < xdoc.rectList.size(); ++i) {
                                body.add(toXmlEle((EleBase)xdoc.rectList.get(i)));
                            }

                            return;
                        }

                        key = (String)it.next();
                    } while(key.equals("TEXT"));
                } while(((String)xdoc.metaMap.get(key)).length() == 0);
            } while(key.equals("scale") && To.toDouble(xdoc.getMeta(key)) == 1.0D);

            addAtt(ele, key, (String)xdoc.metaMap.get(key));
        }
    }

    private static Element toBFBEle(Element root, EleRect rect, String name) {
        Element bfb = toXmlEle(rect);
        bfb.setName(name);
        XmlUtil.removeAtts(bfb, "name,top,left,width,height,color");
        if(rect.color != null) {
            bfb.addAttribute("color", ColorUtil.colorToStr(rect.color));
        }

        if(name.equals("body")) {
            if(rect.txtPadding == 0) {
                XmlUtil.removeAtt(bfb, "padding");
            }

            if(rect.strokeWidth == 0.0D) {
                XmlUtil.removeAtt(bfb, "strokeWidth");
            }
        }

        if(name.equals("body") || bfb.attributeCount() > 0 || bfb.elements().size() > 0) {
            root.add(bfb);
        }

        return bfb;
    }

    public static Element toXmlEle(EleBase docEle) {
        if(docEle instanceof EleCharRect) {
            docEle = ((EleCharRect)docEle).eleChar;
        }

        Element ele = DocumentHelper.createElement(((EleBase)docEle).typeName);
        HashMap attMap = ((EleBase)docEle).getAttMap();
        if(docEle instanceof EleCell) {
            docEle = ((EleCell)docEle).getRect();
            attMap.remove("left");
            attMap.remove("top");
            attMap.remove("width");
            attMap.remove("height");
        } else {
            attMap.remove("row");
            attMap.remove("col");
        }

        HashMap stdMap = getStdAtts((EleBase)docEle);
        Iterator it = attMap.keySet().iterator();

        while(it.hasNext()) {
            String key = (String)it.next();
            if(attMap.get(key) != null && !attMap.get(key).equals(stdMap.get(key)) && attMap.get(key) instanceof String) {
                addAtt(ele, key, (String)attMap.get(key));
            }
        }

        if(ele.getName().equals("ext") && ele.attributeValue("type", "").length() > 0) {
            ele.setName(ele.attributeValue("type", ""));
            ele.remove(ele.attribute("type"));
        }

        if(docEle instanceof EleTable) {
            for(int pe = 0; pe < ((EleBase)docEle).eleList.size(); ++pe) {
                if(((EleBase)docEle).eleList.get(pe) instanceof EleCell) {
                    EleCell paraSize = (EleCell)((EleBase)docEle).eleList.get(pe);
                    if(paraSize.belong == null) {
                        ele.add(toXmlEle((EleBase)((EleBase)docEle).eleList.get(pe)));
                    }
                }
            }
        } else {
            int var10 = 0;
            Element var11 = null;

            for(int i = 0; i < ((EleBase)docEle).eleList.size(); ++i) {
                Element cele = toXmlEle((EleBase)((EleBase)docEle).eleList.get(i));
                if(cele.getName().equals("para")) {
                    ++var10;
                    var11 = cele;
                }

                ele.add(cele);
            }

            if(var10 == 1 && var11.nodeCount() == 0) {
                ele.remove(var11);
            }
        }

        return ele;
    }

    public static EleBase toDocEle(XDoc doc, Element ele) {
        return toDocEle(doc, ele, (Map)null);
    }

    private static EleBase toDocEle(XDoc doc, Element ele, Map styles) {
        HashMap attMap = attMap(ele, styles);
        String eleName = ele.getName();
        Object docEle = null;
        if(eleName.equals("para")) {
            docEle = new ElePara(doc, attMap);
        } else if(eleName.equals("pline")) {
            docEle = new EleParaLine(doc, attMap);
        } else {
            if(eleName.equals("text")) {
                return new EleText(doc, attMap);
            }

            if(eleName.equals("img")) {
                if("normal".equals(attMap.get("sizeType"))) {
                    if("0".equals(attMap.get("width")) && "0".equals(attMap.get("height"))) {
                        attMap.put("sizeType", "autosize");
                    } else if("0".equals(attMap.get("width"))) {
                        attMap.put("sizeType", "autowidth");
                    } else if("0".equals(attMap.get("height"))) {
                        attMap.put("sizeType", "autoheight");
                    }
                }

                docEle = new EleImg(doc, attMap);
            } else {
                if(eleName.equals("line")) {
                    return new EleLine(doc, attMap);
                }

                if(eleName.equals("arc")) {
                    docEle = new EleArc(doc, attMap);
                } else if(eleName.equals("polygon")) {
                    docEle = new ElePolygon(doc, attMap);
                } else if(eleName.equals("path")) {
                    docEle = new ElePath(doc, attMap);
                } else if(eleName.equals("stext")) {
                    docEle = new EleSText(doc, attMap);
                } else if(eleName.equals("table")) {
                    docEle = new EleTable(doc, attMap);
                } else if(eleName.equals("cell")) {
                    docEle = new EleCell(doc, attMap);
                } else if(eleName.equals("group")) {
                    docEle = new EleGroup(doc, attMap);
                } else if(eleName.equals("space")) {
                    docEle = new EleSpace(doc, attMap);
                } else if(!eleName.equals("rect") && !eleName.equals("body") && !eleName.equals("back") && !eleName.equals("front")) {
                    if(eleName.equals("char")) {
                        docEle = new EleCharRect(doc, attMap);
                    } else {
                        docEle = new EleRect(doc, attMap);
                    }
                } else {
                    docEle = new EleRect(doc, attMap);
                    if((eleName.equals("back") || eleName.equals("front") || eleName.equals("body")) && !attMap.containsKey("color")) {
                        ((EleRect)docEle).color = null;
                    }

                    if(eleName.equals("body")) {
                        if(!attMap.containsKey("padding")) {
                            ((EleRect)docEle).txtPadding = 0;
                        }

                        if(!attMap.containsKey("strokeWidth")) {
                            ((EleRect)docEle).strokeWidth = 0.0D;
                        }

                        ((EleRect)docEle).name = "__blank";
                    }
                }
            }
        }

        Iterator str = ele.elementIterator();

        while(true) {
            while(str.hasNext()) {
                Element cEle = (Element)str.next();
                EleBase cDocEle = toDocEle(doc, cEle, styles);
                if(docEle instanceof EleTable) {
                    if(cDocEle instanceof EleRect) {
                        if(cDocEle instanceof EleCell) {
                            ((EleBase)docEle).eleList.add(cDocEle);
                        } else {
                            ((EleBase)docEle).eleList.add(new EleCell((EleRect)cDocEle));
                        }
                    }
                } else if(cDocEle instanceof EleText && !(docEle instanceof ElePara)) {
                    ElePara n = new ElePara(doc);
                    n.eleList.add(cDocEle);
                    ((EleBase)docEle).eleList.add(n);
                } else if(docEle instanceof ElePara && cDocEle instanceof ElePara) {
                    ((EleBase)docEle).eleList.addAll(cDocEle.eleList);
                } else {
                    ((EleBase)docEle).eleList.add(cDocEle);
                }
            }

            if(eleName.equals("table")) {
                EleTable var12 = (EleTable)docEle;
                String[] i;
                int i1;
                int var14;
                int var15;
                if(!attMap.containsKey("width")) {
                    var14 = 0;
                    if(var12.cols.length() > 0) {
                        i = var12.cols.split(",");

                        for(i1 = 0; i1 < i.length; ++i1) {
                            var14 += To.toInt(i[i1], 96);
                        }
                    } else {
                        var14 = 0;

                        for(var15 = 0; var15 < var12.eleList.size(); ++var15) {
                            if(((EleCell)var12.eleList.get(var15)).col > var14) {
                                var14 = ((EleCell)var12.eleList.get(var15)).col;
                            }
                        }

                        var14 = (var14 + 1) * 96;
                    }

                    var12.width = var14;
                }

                if(!attMap.containsKey("height")) {
                    var14 = 0;
                    if(var12.rows.length() > 0) {
                        i = var12.rows.split(",");

                        for(i1 = 0; i1 < i.length; ++i1) {
                            var14 += To.toInt(i[i1], 24);
                        }
                    } else {
                        var14 = 0;

                        for(var15 = 0; var15 < var12.eleList.size(); ++var15) {
                            if(((EleCell)var12.eleList.get(var15)).row > var14) {
                                var14 = ((EleCell)var12.eleList.get(var15)).row;
                            }
                        }

                        var14 = (var14 + 1) * 24;
                    }

                    var12.height = var14;
                }
            }

            if(((EleBase)docEle).eleList.size() == 0 && attMap.containsKey("TEXT") && !(docEle instanceof EleText)) {
                String var13 = (String)attMap.get("TEXT");
                if(var13.length() > 0) {
                    ((EleBase)docEle).setText(var13);
                }
            }

            return (EleBase)docEle;
        }
    }

    private static boolean isInvalidChar(char c) {
        return c > '�' || c < 32 && c != 10 && c != 13 && c != 9;
    }

    private static void addAtt(Element ele, String name, String value) {
        if(name.equals("TEXT")) {
            boolean valid = true;

            for(int c = 0; c < value.length(); ++c) {
                if(isInvalidChar(value.charAt(c))) {
                    valid = false;
                    break;
                }
            }

            if(!valid) {
                StringBuffer sb = new StringBuffer();

                for(int i = 0; i < value.length(); ++i) {
                    char var7 = value.charAt(i);
                    if(isInvalidChar(var7)) {
                        var7 = 32;
                    }

                    sb.append(var7);
                }

                value = sb.toString();
            }

            ele.setText(value);
        } else {
            ele.addAttribute(name, value);
        }

    }

    private static HashMap attMap(Element ele, Map styles) {
        HashMap attMap = new HashMap();
        if(styles != null && styles.containsKey(ele.getName())) {
            attMap.putAll((Map)styles.get(ele.getName()));
        }

        Iterator items = ele.attributeIterator();

        while(items.hasNext()) {
            Attribute att = (Attribute)items.next();
            attMap.put(att.getName(), att.getValue());
        }

        if(ele.elements().size() == 0) {
            attMap.put("TEXT", ele.getText());
        }

        if(attMap.containsKey("style")) {
            String[] var7 = MapUtil.getString(attMap, "style", "").split(";");

            for(int i = 0; i < var7.length; ++i) {
                int pos = var7[i].indexOf(":");
                if(pos > 0) {
                    attMap.put(var7[i].substring(0, pos), var7[i].substring(pos + 1));
                } else if(styles != null && styles.containsKey(var7[i])) {
                    attMap.putAll((Map)styles.get(var7[i]));
                }
            }

            attMap.remove("style");
        }

        return attMap;
    }

    private static OutputStream getOutputStream(String urlStr) throws HgException {
        return (new XUrl(urlStr)).getOutputStream();
    }

    public static HashMap getStdAtts(EleBase docEle) {
        HashMap map = (HashMap)stdAttsMap.get(docEle.getClass());
        if(map == null) {
            map = new HashMap();
            map.put("name", "");
            if(docEle instanceof EleRect) {
                map.put("color", "#000000");
                map.put("strokeWidth", "1.0");
                map.put("strokeStyle", "0");
                map.put("strokeImg", "");
                map.put("align", "top");
                map.put("zPosition", "top");
                map.put("valign", "bottom");
                map.put("visible", "true");
                map.put("dock", "");
                map.put("left", "0");
                map.put("top", "0");
                map.put("sizeType", "normal");
                map.put("fillColor", "");
                map.put("fillColor2", "");
                map.put("fillImg", "");
                map.put("line", docEle instanceof EleShape?"00000000":"11110000");
                map.put("gradual", "");
                map.put("arc", "0");
                map.put("href", "");
                map.put("filter", "");
                map.put("filterTarget", "all");
                map.put("filterParam", "");
                map.put("scale", "");
                map.put("column", "1");
                map.put("rotate", "0");
                map.put("srotate", "0");
                map.put("distort", "");
                map.put("padding", "2");
                map.put("margin", "0");
                map.put("toolTip", "");
                map.put("layoutFlow", "h");
                map.put("layoutLine", "false");
                map.put("fillRatio", "");
                map.put("colSpan", "1");
                map.put("rowSpan", "1");
                map.put("comment", "");
                if(docEle instanceof EleSText) {
                    map.put("width", "200");
                    map.put("height", "100");
                } else if(docEle instanceof EleShape) {
                    map.put("width", "120");
                    map.put("height", "120");
                } else if(docEle instanceof EleImg) {
                    map.put("width", "0");
                    map.put("height", "0");
                } else {
                    map.put("width", String.valueOf(96));
                    map.put("height", String.valueOf(24));
                }

                if(docEle instanceof EleArc) {
                    map.put("type", "open");
                    map.put("start", "0");
                    map.put("extent", "360");
                } else if(docEle instanceof EleSText) {
                    map.put("fontName", XFont.defaultFontName);
                    map.put("text", "");
                    map.put("italic", "false");
                    map.put("bold", "false");
                    map.put("spacing", "0");
                    map.put("format", "");
                } else if(docEle instanceof EleImg) {
                    map.put("src", "");
                    map.put("drawType", "zoom");
                    map.put("shape", "");
                    map.put("color", "");
                    map.put("fillColor", "#ffffff");
                } else if(docEle instanceof EleCell) {
                    map.put("conn", "");
                    map.put("sql", "");
                    map.put("data", "");
                    map.put("rowset", "");
                    map.put("rowCount", "0");
                    map.put("direction", "v");
                } else if(docEle instanceof EleLine) {
                    map.put("startX", "0");
                    map.put("startY", "0");
                    map.put("endX", "20");
                    map.put("endY", "20");
                    map.put("startArrow", "");
                    map.put("endArrow", "");
                    map.put("lineStyle", "line");
                } else if(docEle instanceof EleGroup) {
                    map.put("text", "");
                    map.put("color", "");
                    map.put("drawType", "zoom");
                } else if(docEle instanceof ElePath) {
                    map.put("shape", "");
                    map.put("drawType", "zoom");
                    map.put("repeat", "1");
                } else if(docEle instanceof EleTable) {
                    map.put("color", "");
                    map.put("width", "0");
                    map.put("height", "0");
                    map.put("input", "false");
                    map.put("header", "0");
                    map.put("footer", "0");
                    map.put("data", "");
                    map.put("tarType", "table");
                    map.put("rowCount", "0");
                    map.put("sizeType", "autosize");
                }
            } else if(docEle instanceof ElePara) {
                map.put("lineSpacing", "0");
                map.put("indent", "0");
                map.put("heading", "0");
                map.put("align", "left");
                map.put("prefix", "");
                map.put("breakPage", "false");
                map.put("backColor", "");
                map.put("backImg", "");
                if(docEle instanceof EleParaLine) {
                    map.put("width", "0");
                    map.put("height", String.valueOf(XFont.defaultFontSize));
                    map.put("offset", "0");
                    map.put("vertical", "false");
                }
            } else if(docEle instanceof EleText) {
                map.put("fontColor", "#000000");
                map.put("backColor", "");
                map.put("backImg", "");
                map.put("TEXT", "");
                map.put("fontName", XFont.defaultFontName);
                map.put("fontSize", String.valueOf(XFont.defaultFontSize));
                map.put("valign", "bottom");
                map.put("href", "");
                map.put("toolTip", "");
                map.put("fontStyle", "");
                map.put("format", "");
                map.put("spacing", "0");
                if(docEle instanceof EleChar) {
                    map.put("img", "");
                    map.put("shape", "");
                }
            }

            stdAttsMap.put(docEle.getClass(), map);
        }

        return (HashMap)map.clone();
    }

    public static Map attMap(Element ele) {
        return attMap(ele, (Map)null);
    }

    public static String toXml(EleBase ele) {
        return XmlUtil.toXml(toXmlEle(ele));
    }
}
