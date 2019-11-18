//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.doc;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class XDocXMLWriter extends XMLWriter {
    private StringBuffer escapeBuffer = new StringBuffer();

    public XDocXMLWriter(OutputStream out, OutputFormat format) throws UnsupportedEncodingException {
        super(out, format);
    }

    public XDocXMLWriter(OutputStream out) throws UnsupportedEncodingException {
        super(out);
    }

    public XDocXMLWriter(Writer out, OutputFormat format) {
        super(out, format);
    }

    public XDocXMLWriter(Writer out) {
        super(out);
    }

    protected String escapeElementEntities(String text) {
        char[] block = null;
        int last = 0;
        int size = text.length();

        int i;
        String answer;
        for(i = 0; i < size; ++i) {
            answer = null;
            char c = text.charAt(i);
            switch(c) {
                case '\t':
                case '\n':
                case '\r':
                    break;
                case '&':
                    answer = "&amp;";
                    break;
                case '<':
                    answer = "&lt;";
                    break;
                case '>':
                    answer = "&gt;";
                    break;
                default:
                    if(c < 32 || this.shouldEncodeChar(c)) {
                        answer = "&#" + c + ";";
                    }
            }

            if(answer != null) {
                if(block == null) {
                    block = text.toCharArray();
                }

                this.escapeBuffer.append(block, last, i - last);
                this.escapeBuffer.append(answer);
                last = i + 1;
            }
        }

        if(last == 0) {
            return text;
        } else {
            if(last < size) {
                if(block == null) {
                    block = text.toCharArray();
                }

                this.escapeBuffer.append(block, last, i - last);
            }

            answer = this.escapeBuffer.toString();
            this.escapeBuffer.setLength(0);
            return answer;
        }
    }

    protected String escapeAttributeEntities(String text) {
        char quote = this.getOutputFormat().getAttributeQuoteCharacter();
        char[] block = null;
        int last = 0;
        int size = text.length();

        int i;
        String answer;
        for(i = 0; i < size; ++i) {
            answer = null;
            char c = text.charAt(i);
            switch(c) {
                case '\"':
                    if(quote == 34) {
                        answer = "&quot;";
                    }
                    break;
                case '&':
                    answer = "&amp;";
                    break;
                case '\'':
                    if(quote == 39) {
                        answer = "&apos;";
                    }
                    break;
                case '<':
                    answer = "&lt;";
                    break;
                case '>':
                    answer = "&gt;";
                    break;
                default:
                    if(c < 32 || this.shouldEncodeChar(c)) {
                        answer = "&#" + c + ";";
                    }
            }

            if(answer != null) {
                if(block == null) {
                    block = text.toCharArray();
                }

                this.escapeBuffer.append(block, last, i - last);
                this.escapeBuffer.append(answer);
                last = i + 1;
            }
        }

        if(last == 0) {
            return text;
        } else {
            if(last < size) {
                if(block == null) {
                    block = text.toCharArray();
                }

                this.escapeBuffer.append(block, last, i - last);
            }

            answer = this.escapeBuffer.toString();
            this.escapeBuffer.setLength(0);
            return answer;
        }
    }
}
