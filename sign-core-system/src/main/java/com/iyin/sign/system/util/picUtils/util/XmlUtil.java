

package com.iyin.sign.system.util.picUtils.util;

import com.iyin.sign.system.util.picUtils.data.Row;
import com.iyin.sign.system.util.picUtils.data.RowSet;
import com.iyin.sign.system.util.picUtils.doc.XDocXMLWriter;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class XmlUtil {
    public static Class clsXmlReader = null;

    public XmlUtil() {
    }

    public static void save(Document doc, String urlStr, String charset) throws HgException {
        try {
            XUrl e = new XUrl(urlStr);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setTrimText(false);
            if (charset.length() > 0) {
                format.setEncoding(charset);
            } else {
                format.setEncoding("UTF-8");
            }

            OutputStreamWriter writer;
            if (charset.length() > 0) {
                writer = new OutputStreamWriter(e.getOutputStream(), charset);
            } else {
                writer = new OutputStreamWriter(e.getOutputStream());
            }

            XMLWriter xmlwriter = new XMLWriter(writer, format);
            xmlwriter.write(doc);
            xmlwriter.close();
        } catch (Exception var7) {
            throw new HgException(var7);
        }
    }

    private static Element toEle(RowSet rowSet) throws HgException {
        Element ele = DocumentHelper.createElement("node");

        for (int j = 0; j < rowSet.size(); ++j) {
            Row row = rowSet.get(j);
            String name = row.get(0).toString();
            if (name.equals("$name")) {
                ele.setName(row.get(1).toString());
            } else if (name.equals("$text")) {
                ele.setText(row.get(1).toString());
            } else {
                ele.addAttribute(name, row.get(1).toString());
            }
        }

        return ele;
    }

    public static String as(RowSet rowSet) throws HgException {
        return toEle(rowSet).asXML();
    }

    public static String fixName(String name) {
        return fixName(name, "_");
    }

    public static String fixName(String name, String repStr) {
        boolean b = false;

        for (int sb = 0; sb < name.length(); ++sb) {
            if (!Character.isLetterOrDigit(name.charAt(sb))) {
                b = true;
                break;
            }
        }

        if (b) {
            StringBuffer var5 = new StringBuffer();

            for (int i = 0; i < name.length(); ++i) {
                if (!Character.isLetterOrDigit(name.charAt(i))) {
                    if (repStr.length() > 0) {
                        var5.append(repStr);
                    }
                } else {
                    var5.append(name.charAt(i));
                }
            }

            name = var5.toString();
            if (repStr.length() > 0) {
                while (name.endsWith(repStr)) {
                    name = name.substring(0, name.length() - repStr.length());
                }

                while (name.startsWith(repStr)) {
                    name = name.substring(repStr.length());
                }
            }
        }

        if (name.length() > 0 && Character.isDigit(name.charAt(0))) {
            name = "X" + name;
        }

        if (name.length() > 20) {
            name = name.substring(0, 20);
        }

        return name;
    }

    public static Document parseText(String text) throws DocumentException {
        Document result = null;
        SAXReader reader = createSAXReader();
        String encoding = getEncoding(text);
        InputSource source = new InputSource(new StringReader(text));
        source.setEncoding(encoding);
        result = reader.read(source);
        if (result.getXMLEncoding() == null) {
            result.setXMLEncoding(encoding);
        }

        return result;
    }

    private static String getEncoding(String text) {
        String result = null;
        String xml = text.trim();
        if (xml.startsWith("<?xml")) {
            int end = xml.indexOf("?>");
            String sub = xml.substring(0, end);
            StringTokenizer tokens = new StringTokenizer(sub, " =\"\'");

            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                if ("encoding".equals(token)) {
                    if (tokens.hasMoreTokens()) {
                        result = tokens.nextToken();
                    }
                    break;
                }
            }
        }

        return result;
    }

    public static Document fromCsv(String str) {
        List rows = StrUtil.csvList(str);
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("xdata");
        if (rows.size() > 1) {
            List cols = (List) rows.get(0);
            String[] names = new String[cols.size()];

            int i;
            for (i = 0; i < cols.size(); ++i) {
                names[i] = fixName((String) cols.get(i));
            }

            for (i = 1; i < rows.size(); ++i) {
                cols = (List) rows.get(i);
                Element eleRow = root.addElement("row");

                for (int j = 0; j < names.length && j < cols.size(); ++j) {
                    Element eleCell = eleRow.addElement(names[j]);
                    eleCell.setText((String) cols.get(j));
                }
            }
        }

        return doc;
    }

    public static SAXReader createSAXReader() {
        SAXReader reader = null;
        if (clsXmlReader != null) {
            try {
                reader = new SAXReader((XMLReader) clsXmlReader.newInstance());
            } catch (Exception var2) {
                ;
            }
        }

        if (reader == null) {
            reader = new SAXReader();
        }

        reader.setEntityResolver(NoOpEntityResolver.er);
        return reader;
    }

    public static String attEncode(String str) {
        if (str == null) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                if (c == 92) {
                    sb.append("\\\\");
                } else if (c == 10) {
                    sb.append("\\n");
                } else if (c == 9) {
                    sb.append("\\t");
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    public static String attDecode(String str) {
        if (str.indexOf(92) >= 0) {
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                if (c == 92 && i + 1 < str.length()) {
                    ++i;
                    c = str.charAt(i);
                    if (c == 110) {
                        c = 10;
                    } else if (c == 114) {
                        c = 13;
                    } else if (c == 116) {
                        c = 9;
                    }
                }

                sb.append(c);
            }

            str = sb.toString();
        }

        return str;
    }

    public static void removeAtt(Element ele, String attName) {
        if (ele.attribute(attName) != null) {
            ele.remove(ele.attribute(attName));
        }

    }

    public static void removeAtts(Element ele, String attNames) {
        String[] atts = attNames.split(",");

        for (int i = 0; i < atts.length; ++i) {
            removeAtt(ele, atts[i]);
        }

    }

    public static void trimText(Element ele) {
        Iterator it = ele.nodeIterator();
        ArrayList list = new ArrayList();

        while (it.hasNext()) {
            Node node = (Node) it.next();
            if (node instanceof Text && node.getText().trim().length() == 0) {
                list.add(node);
            }
        }

        for (int eles = 0; eles < list.size(); ++eles) {
            ele.remove((Text) list.get(eles));
        }

        List var6 = ele.elements();

        for (int i = 0; i < var6.size(); ++i) {
            trimText((Element) var6.get(i));
        }

    }

    public static String nodeValue(Node node) {
        if (node != null) {
            String val = node.getText();
            return val == null ? "" : val;
        } else {
            return "";
        }
    }

    public static String toXml(Node node) {
        return toXml(node, false);
    }

    public static String toXml(Node node, boolean pretty) {
        StringWriter sw = new StringWriter();
        XDocXMLWriter writer;
        if (pretty) {
            writer = new XDocXMLWriter(sw, OutputFormat.createPrettyPrint());
        } else {
            writer = new XDocXMLWriter(sw);
        }

        try {
            writer.write(node);
            writer.flush();
        } catch (IOException var5) {
            ;
        }

        return sw.toString();
    }

    public static Document toDoc(Throwable e) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("DocumentException");
        root.addAttribute("class", e.getClass().toString());
        root.addAttribute("message", e.getMessage());
        return doc;
    }

    public static void save(Document doc, String url) throws HgException {
        save(doc, url, "utf-8");
    }
}
