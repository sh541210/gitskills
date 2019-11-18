package com.iyin.sign.system.common.utils;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.OpenOfficeService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.ConnectException;

/**
 * @ClassName Office2PdfUtil
 * Office2PdfUtil
 * @Author wdf
 * @Date 2019/7/29 17:52
 * @throws
 * @Version 1.0
 **/
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Configuration
public class Office2PdfUtil {
	@Autowired
	OpenOfficeService openOfficeService;

	public void convert(File src, File dest) {
		if (! src.exists() || ! src.isFile()) {
			throw new IllegalArgumentException("source file is not exist or not a file");
		}
		log.info("Office2PdfUtil convert File exists");
        if (! dest.exists()) {
            File parent = dest.getParentFile();
            parent.mkdirs();
        }
        // 1 get connection
		OpenOfficeConnection connection = openOfficeService.getConnection();
		// 2 open connection
        try {
			connection.connect();
			log.info("Office2PdfUtil convert connect()");
	        // 3 convert
	        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
	        converter.convert(src, dest);
		} catch (ConnectException e) {
			log.error(e.getMessage(), e);
			throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
		} finally {
	        // 4 close the connection
			openOfficeService.releaseConnection(connection);
		}
	}

}
