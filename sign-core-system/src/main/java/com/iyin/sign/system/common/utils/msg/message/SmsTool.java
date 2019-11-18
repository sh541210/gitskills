//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.common.utils.msg.message;

import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.model.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * @ClassName: SMSTool.java
 * @Description: 短信工具类
 * @Author: yml
 * @CreateDate: 2019/8/23
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/23
 * @Version: 1.0.0
 */
@Slf4j
public class SmsTool {
    private String serviceURL;
    private String sn;
    private String encrypt;
    /**
     * 位移位数
     */
    private static final int FOUR = 4;
    /**
     * 与运算数
     */
    private static final int FIFTEEN = 15;

    public SmsTool(String sn, String password, String url) throws UnsupportedEncodingException {
        this.sn = sn;
        this.encrypt = this.getMD5(sn + password);
        this.serviceURL = url;
    }

    public String getMD5(String sourceStr) throws UnsupportedEncodingException {
        StringBuilder resultStr = new StringBuilder();

        try {
            byte[] temp = sourceStr.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(temp);
            byte[] b = md5.digest();

            for(int i = 0; i < b.length; ++i) {
                char[] digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
                char[] ob = new char[]{digit[b[i] >>> FOUR & FIFTEEN], digit[b[i] & FIFTEEN]};
                resultStr.append(ob);
            }

            return resultStr.toString();
        } catch (NoSuchAlgorithmException var9) {
            log.error("NoSuchAlgorithmException:{}", var9);
            return null;
        }
    }

    public String mdsmssend(String mobile, String content, String ext, String stime, String rrid, String msgfmt) {
        String result = "";
        String soapAction = "http://entinfo.cn/mdsmssend";
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        xml = xml + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
        xml = xml + "<soap:Body>";
        xml = xml + "<mdsmssend  xmlns=\"http://entinfo.cn/\">";
        xml = xml + "<sn>" + this.sn + "</sn>";
        xml = xml + "<pwd>" + this.encrypt + "</pwd>";
        xml = xml + "<mobile>" + mobile + "</mobile>";
        xml = xml + "<content>" + content + "</content>";
        xml = xml + "<ext>" + ext + "</ext>";
        xml = xml + "<stime>" + stime + "</stime>";
        xml = xml + "<rrid>" + rrid + "</rrid>";
        xml = xml + "<msgfmt>" + msgfmt + "</msgfmt>";
        xml = xml + "</mdsmssend>";
        xml = xml + "</soap:Body>";
        xml = xml + "</soap:Envelope>";

        try {
            URL url = new URL(this.serviceURL);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpconn = (HttpURLConnection)connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(xml.getBytes());
            byte[] b = bout.toByteArray();
            httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
            httpconn.setRequestProperty("Content-Type", "text/xml; charset=gb2312");
            httpconn.setRequestProperty("SOAPAction", soapAction);
            httpconn.setRequestMethod("POST");
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            OutputStream out = httpconn.getOutputStream();
            out.write(b);
            out.close();
            InputStreamReader isr = new InputStreamReader(httpconn.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            String inputLine;
            while(null != (inputLine = in.readLine())) {
                Pattern pattern = compile("<mdsmssendResult>(.*)</mdsmssendResult>");

                for(Matcher matcher = pattern.matcher(inputLine); matcher.find();) {
                    result = matcher.group(1);
                }
            }


        } catch (Exception var21) {
            log.error("Exception:{}", var21);
            throw new BusinessException(ErrorCode.SMS_SEND_ERROR);
        }
        return result;
    }
}
