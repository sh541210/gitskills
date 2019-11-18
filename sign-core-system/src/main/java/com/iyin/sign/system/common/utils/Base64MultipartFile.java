package com.iyin.sign.system.common.utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * @param :
 * @author:barley
 * @detail:
 * @Version:0.01
 * @return:
 */
@Slf4j
public class Base64MultipartFile implements MultipartFile{

    private final byte[] imgContent;
    private final String header;

    public Base64MultipartFile(byte[] imgContent, String header){
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public String getOriginalFilename() {
        String msword = "msword";
        String document = "document";
        String docSuffix = "doc";
        String suffixSeperator = ".";
        String suffix = header.split("/")[1];
        suffix = suffix.substring(suffix.lastIndexOf(suffixSeperator) + 1);
        if (msword.equalsIgnoreCase(suffix) || document.equalsIgnoreCase(suffix)) {
            suffix = docSuffix;
        }
        return String.format("%s.%s", "file", suffix);
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(dest)){
            fileOutputStream.write(imgContent);
        }
    }


    public static MultipartFile base64ToMultipart(String base64){
        String [] baseStrs = base64.split(",");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b;
        try {
            b = decoder.decodeBuffer(baseStrs[1]);
            for(int i = 0;i< b.length;i++){
                if(b[i] <0){
                    b[i] += 256;
                }
            }
            return new Base64MultipartFile(b,baseStrs[0]);
        } catch (IOException e) {
            log.error("{}",e);
        }
        return null;
    }

    public static MultipartFile base64ToMultipart2(String base64){
        String [] baseStrs = base64.split(",");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b;
        try {
            if(baseStrs.length>1) {
                b = decoder.decodeBuffer(baseStrs[1]);
                log.info("base64ToMultipart2 baseStrs[1]:"+baseStrs[1].length());
            }else{
                b = decoder.decodeBuffer(baseStrs[0]);
                log.info("base64ToMultipart2 baseStrs[1]:"+baseStrs[0].length());
            }
            log.info("base64ToMultipart2 s b:"+b.length);
            for(int i = 0;i< b.length;i++){
                if(b[i] <0){
                    b[i] += 256;
                }
            }
            log.info("base64ToMultipart2 b:"+b.length);
            return new Base64MultipartFile(b,baseStrs[0]);
        } catch (IOException e) {
            log.error("{}",e);
        }
        return null;
    }

   public static String fileToBase64(String filePath){
        String base64 = null;
       File file = new File(filePath);
       try(InputStream in=new FileInputStream(file)) {
           byte[] bytes = new byte[(int) file.length()];
           int read = in.read(bytes);
           if(read > 0){
               base64 = Base64.encodeBase64String(bytes);
           }
       }catch (IOException e) {
           log.error("{}",e);
       }
       return  base64;
   }
}