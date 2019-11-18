package com.iyin.sign.system.model;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @ClassName InMemoryMultipartFile
 * @Author wdf
 * @Date 2019/7/19 14:16
 * @throws
 * @Version 1.0
 **/
public class InMemoryMultipartFile implements MultipartFile,Serializable{

    private String name = "file";
    private String originalFileName;
    private String contentType = "application/octet-stream";
    private byte[] payload;

    public InMemoryMultipartFile(File file) throws IOException {
        this.originalFileName = file.getName();
        this.payload = FileCopyUtils.copyToByteArray(file);
    }

    public InMemoryMultipartFile(File file,String originalFileName) throws IOException {
        this.originalFileName = originalFileName;
        this.payload = FileCopyUtils.copyToByteArray(file);
    }

    public InMemoryMultipartFile(String originalFileName, byte[] payload) {
        this.originalFileName = originalFileName;
        this.payload = payload;
    }

    public InMemoryMultipartFile(String name, String originalFileName, String contentType, byte[] payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null.");
        }
        this.name = null == name ? this.name : name;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.payload = payload;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return payload.length == 0;
    }

    @Override
    public long getSize() {
        return payload.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return payload;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(payload);
    }

    @SuppressWarnings("resource")
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        if (null == payload || null == dest) {
            throw new IllegalStateException();
        }
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(payload);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
