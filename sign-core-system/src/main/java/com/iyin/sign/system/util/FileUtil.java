package com.iyin.sign.system.util;

import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.file.Files;

/**
 * @ClassName: FileUtil.java
 * @Description: 文件处理
 * @Author: yml
 * @CreateDate: 2019/7/4
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/4
 * @Version: 1.0.0
 */
@Slf4j
public class FileUtil {
	private FileUtil() {
		throw new IllegalStateException("FileUtil class");
	}
    private static final int BLOCK_SIZE = 4096;

    public static String getFileExtensionName(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length() - 1))) { 
                return filename.substring(dot + 1); 
            } 
        } 
        throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
    }
    public static String getFielNameNoExt(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length() - 1))) { 
                return filename.substring(0, dot); 
            } 
        } 
        throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
    }
    public static String getFileName(String fileName) {
    	int dot = fileName.lastIndexOf('/');
    	if (dot == -1) {
    		dot = fileName.lastIndexOf('\\');
    	}
    	if (dot == -1) {
    		// 原样返回
    		return fileName;
    	}
    	return fileName.substring(dot + 1);
    }
	public static byte[] toBytesArray(InputStream is) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
	        byte[] b = new byte[BLOCK_SIZE];  
	        int n;  
	        while ((n = is.read(b)) != -1) {  
	            bos.write(b, 0, n);  
	        }  
	        return bos.toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
		}finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("toBytesArray IOException:{0}",e);
            }
        }
	}
	public static void copy(InputStream inputStream, OutputStream outputStream) {
		try {
	        byte[] buffer = new byte[BLOCK_SIZE];  
	        int n;  
	        while ((n = inputStream.read(buffer)) != -1) {  
	        	outputStream.write(buffer, 0, n);  
	        }  
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
		}finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                log.error("copy IOException:{0}",e);
            }
        }
	}

	/**
	 * 删除指定文件
	 * @Author: wdf
	 * @CreateDate: 2019/3/6 14:55
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/3/6 14:55
	 * @Version: 0.0.1
	 * @param file
	 * @return void
	 */
	public static void deleteFile(File file) {
		if (!ObjectUtils.isEmpty(file) && file.exists()) {
			try {
				Files.deleteIfExists(file.toPath());
			} catch (IOException e) {
				log.error("del file error:{}",e);
				throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
			}
		}
	}
}
