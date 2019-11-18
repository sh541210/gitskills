package com.iyin.sign.system.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * @ClassName: PdfUtil.java
 * @Description: pdf文件操作
 * @Author: yml
 * @CreateDate: 2019/7/4
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/4
 * @Version: 1.0.0
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Configuration
public class PdfUtil {
	/**
	 * 功能描述 获取文件页码
	 * @author wdf
	 * @date 2019/3/5
	 * @param  * @param bytes
	 * @param imageType
	 * @param page
	 * @param dpi
	 * @param output
	 * @return void
	*/
	public void getPdfImageByPage(byte[] bytes, String imageType, int page, int dpi, OutputStream output) {
		try (PDDocument document = PDDocument.load(bytes)){
			int pages = document.getNumberOfPages();
			if (page <= 0 || page > pages) {
				throw new IllegalArgumentException("out of range of pages");
			}
			PDFRenderer renderer = new PDFRenderer(document);
			BufferedImage image = renderer.renderImageWithDPI(page - 1, dpi);
			ImageIO.write(image, imageType, output);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
		}
	}
	public static int getPdfNumberOfPages(byte[] bytes) {
		try (PDDocument document = PDDocument.load(bytes)){
			return document.getNumberOfPages();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return 0;
	}

	public byte[] img2pdf(byte[] image) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();){
	        // 第一步：创建一个document对象。[默认A4]
			Document document = new Document();
	        document.setMargins(0, 0, 0, 0);
	        // 第二步：创建一个PdfWriter实例，
	        PdfWriter.getInstance(document, baos);
	        // 第三步：打开文档。
	        document.open();
	        // 第四步：在文档中增加图片。
            Image img = Image.getInstance(image);
            img.setAlignment(Image.ALIGN_CENTER);
            img.scaleToFit(PageSize.A4);
            // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
            document.newPage();
            document.add(img);
            // 第五步：关闭文档。
            document.close();
	        return baos.toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BusinessException(FileResponseCode.DATA_PIC_COVERT_EXCEPTION);
		}
	}
}
