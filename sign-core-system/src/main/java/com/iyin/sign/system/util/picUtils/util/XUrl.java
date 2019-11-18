//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.util;

import java.io.*;
import java.net.*;

public class XUrl {
    public String urlStr;

    public XUrl(String urlStr) {
        this.urlStr = urlStr;
    }

    public InputStream getInputStream() throws HgException, FileNotFoundException {
        try {
            if(isFileUrl(this.urlStr)) {
                return new FileInputStream(this.urlStr);
            } else if(this.urlStr.indexOf(":") <= 0) {
                return new ByteArrayInputStream(new byte[0]);
            } else {
                String e2 = this.urlStr.substring(0, this.urlStr.indexOf(":")).toUpperCase();
                String prefix;
                if(e2.equals("CLASS")) {
                    String var9 = this.urlStr.substring(8, this.urlStr.indexOf("/", 8));
                    prefix = this.urlStr.substring(this.urlStr.indexOf("/", 8) + 1);
                    InputStream var10 = Class.forName(var9).getResourceAsStream(prefix);
                    if(var10 != null) {
                        return var10;
                    } else {
                        throw new FileNotFoundException(this.urlStr);
                    }
                } else if(!e2.equals("DATA")) {
                    return this.openInputStream(this.getURLConnection(this.urlStr));
                } else {
                    int pos = this.urlStr.indexOf(44);
                    if(pos <= 0) {
                        return new ByteArrayInputStream(new byte[0]);
                    } else {
                        prefix = this.urlStr.substring(5, pos);
                        if(prefix.endsWith(";base64")) {
                            return new ByteArrayInputStream(StrUtil.fromBase64(this.urlStr.substring(pos + 1)));
                        } else {
                            String[] strs = prefix.split(";");
                            String charset = "utf-8";

                            for(int data = 1; data < strs.length; ++data) {
                                if(strs[data].startsWith("charset=")) {
                                    charset = strs[data].substring(8);
                                    break;
                                }
                            }

                            String var11 = URLDecoder.decode(this.urlStr.substring(pos + 1), charset);
                            return new ByteArrayInputStream(var11.getBytes("utf-8"));
                        }
                    }
                }
            }
        } catch (FileNotFoundException var7) {
            throw var7;
        } catch (Exception var8) {
            throw new HgException(var8);
        }
    }

    public OutputStream getOutputStream() throws HgException {
        try {
            URLConnection e = this.getURLConnection(this.urlStr);
            if(e instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection)e;
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("Content-Type", "application/octet-stream");
                httpConn.setDoOutput(true);
            }

            return e.getOutputStream();
        } catch (Exception var3) {
            throw new HgException(var3);
        }
    }

    private InputStream openInputStream(URLConnection urlConn) throws IOException {
        return urlConn.getInputStream();
    }

    private static String encodeURI(String str) {
        char[] cs = str.toCharArray();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < cs.length; ++i) {
            if((cs[i] < 97 || cs[i] > 122) && (cs[i] < 65 || cs[i] > 90) && (cs[i] < 48 || cs[i] > 57) && "-_.!~*\'();/?:@&=+$,#".indexOf(cs[i]) < 0) {
                if(cs[i] == 32) {
                    sb.append('+');
                } else {
                    try {
                        sb.append(URLEncoder.encode(String.valueOf(cs[i]), "UTF-8"));
                    } catch (UnsupportedEncodingException var5) {
                        sb.append(cs[i]);
                    }
                }
            } else {
                sb.append(cs[i]);
            }
        }

        return sb.toString();
    }

    private URLConnection getURLConnection(String urlStr) throws HgException {
        try {
            String e = "";
            int propPos = urlStr.indexOf("{");
            if(propPos >= 0 && urlStr.indexOf("}", propPos) > 0) {
                e = urlStr.substring(propPos + 1, urlStr.indexOf("}", propPos)).trim();
                urlStr = urlStr.substring(0, propPos) + urlStr.substring(urlStr.indexOf("}", propPos) + 1);
            }

            URL url = new URL(encodeURI(urlStr));
            URLConnection urlConn = url.openConnection();
            if(e.length() > 0) {
                String[] httpConn = e.split(";");

                for(int i = 0; i < httpConn.length; ++i) {
                    if(url.getProtocol().toLowerCase().equals("http") && httpConn[i].substring(0, httpConn[i].indexOf("=")).equals("loginUrl")) {
                        HttpURLConnection.setFollowRedirects(false);
                        String cookie = "";
                        URL loginUrl = new URL(httpConn[i].substring(httpConn[i].indexOf("=") + 1));
                        HttpURLConnection httpConn1 = (HttpURLConnection)loginUrl.openConnection();
                        httpConn1.setInstanceFollowRedirects(false);
                        cookie = httpConn1.getHeaderField("Set-Cookie");
                        urlConn.addRequestProperty("Cookie", cookie);
                    } else {
                        urlConn.addRequestProperty(httpConn[i].substring(0, httpConn[i].indexOf("=")), httpConn[i].substring(httpConn[i].indexOf("=") + 1));
                    }
                }
            }

            if(url.getProtocol().equals("http")) {
                HttpURLConnection var12 = (HttpURLConnection)urlConn;
                var12.setRequestMethod("GET");
                setAgent(var12);
            }

            return urlConn;
        } catch (Exception var11) {
            throw new HgException(var11);
        }
    }

    public static String fixUrl(String urlStr) {
        if(urlStr.length() > 0 && !urlStr.endsWith("/") && !urlStr.endsWith("\\")) {
            urlStr = urlStr + "/";
        }

        urlStr = urlStr.replace('\\', '/');
        return urlStr;
    }

    public String getName() {
        String name = this.urlStr;
        if(name.indexOf(63) >= 0) {
            name = name.substring(0, name.indexOf(63));
        }

        if(name.indexOf(47) >= 0) {
            name = name.substring(name.lastIndexOf(47) + 1);
        }

        if(name.indexOf(92) >= 0) {
            name = name.substring(name.lastIndexOf(92) + 1);
        }

        return name;
    }

    public static void setAgent(HttpURLConnection conn) {
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
    }

    private static boolean isFileUrl(String str) {
        return str.indexOf(":") < 2 || str.startsWith("/");
    }
}
