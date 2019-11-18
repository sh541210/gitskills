package com.iyin.sign.system.util;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.entity.SysSignatureLog;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.sign.MultiParam;
import com.iyin.sign.system.util.sign.ImgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * @ClassName: PdfReadUtil.java
 * @Description: PDF文件操作类
 * @Author: yml
 * @CreateDate: 2019/6/22
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/22
 * @Version: 1.0.0
 */
@Slf4j
public class PdfReadUtil {

    private static final int DPI = 145;
    private static final int INT = 4096;
    private static final int HALF = 2;
    private static final int RADIUS = 3;
    private int page;
    private float xCoordinate;
    private float yCoordinate;
    private float wCoordinate;
    private float hCoordinate;
    private boolean isNotBlank;

    private static final int WORD_HEIGHT = 8;

    public PdfReadUtil() {
    }

    public PdfReadUtil(int page, float xCoordinate, float yCoordinate, float wCoordinate, float hCoordinate) {
        this.page = page;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.wCoordinate = wCoordinate;
        this.hCoordinate = hCoordinate;
    }

    public boolean isNotBlank() {
        return isNotBlank;
    }

    public void manipulatePdf(PdfReader reader) throws IOException {
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(reader);
        pdfReaderContentParser.processContent(page, new RenderListener() {
            @Override
            public void beginTextBlock() {

            }

            @Override
            public void renderText(TextRenderInfo textRenderInfo) {
                String text = textRenderInfo.getText();
                if (!isNotBlank && StringUtils.isNotBlank(text)) {
                    Rectangle2D.Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
                    float x = boundingRectange.x;
                    float y = boundingRectange.y;
                    float width = boundingRectange.width;
                    boolean xJudge = !(xCoordinate > x + width || x > xCoordinate + wCoordinate);
                    boolean yJudge = !(y > yCoordinate + hCoordinate || yCoordinate > y + WORD_HEIGHT);
                    if (xJudge && yJudge) {
                        isNotBlank = true;
                    }
                }
            }

            @Override
            public void endTextBlock() {

            }

            @Override
            public void renderImage(ImageRenderInfo renderInfo) {

            }
        });
        reader.close();
    }

    public byte[] img2pdf(byte[] image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
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
            throw new BusinessException(ErrorCode.DATA_PIC_COVERT_EXCEPTION);
        }
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[INT];
            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, n);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DATA_IO_EXCEPTION);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                log.error("copy IOException:{0}", e);
            }
        }
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            log.error("del file error:" + e);
        }
    }

    /**
     * 整个文档脱密雾化
     *
     * @param fileBase64, multiParam
     * @return java.lang.String
     * @Author: wdf
     * @CreateDate: 2019/8/6 19:59
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/6 19:59
     * @Version: 0.0.1
     */
    public static String manipulatePdfNew(String fileBase64, MultiParam multiParam) throws Exception {
        byte[] bytes = Base64Util.decode(fileBase64);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PDDocument pdDocument = PDDocument.load(bytes)) {
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pageTotal = pdDocument.getNumberOfPages();
            Document document = new Document();
            document.setMargins(0, 0, 0, 0);
            // 第二步：创建一个PdfWriter实例，
            PdfWriter.getInstance(document, baos);
            // 第三步：打开文档。
            document.open();
            ByteArrayOutputStream baosi = new ByteArrayOutputStream();
            for (int i = 0; i < pageTotal; i++) {
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 150);
                float proportion = bufferedImage.getWidth() / multiParam.getPdfWidth();
                if (multiParam.getPageNum() - 1 == i || multiParam.getPageNumEnd() - 1 >= i) {
                    int height = (int) (multiParam.getUry() * proportion);
                    int width = (int) (multiParam.getUrx() * proportion);
                    int x = (int) (multiParam.getLlx() * proportion);
                    int y = (int) (multiParam.getLly() * proportion);
                    x = (x - width / 2) < 0 ? 0 : (x - width / 2);
                    y = bufferedImage.getHeight() - y - height / 2;
                    if (multiParam.isGrey()) {
                        ImgUtil.grayScaleDeal(bufferedImage, x, y, width, height);
                    }
                    if (multiParam.isFoggy()) {
                        int[] values = getPixArray(bufferedImage, x, y, width, height);
                        values = ImgUtil.doBlur(values, width, height, 3);
                        bufferedImage.setRGB(x, y, width, height, values, 0, width);
                    }
                }

                ImageIO.write(bufferedImage, "png", baosi);
                Image img = Image.getInstance(baosi.toByteArray());
                img.setAlignment(Image.ALIGN_CENTER);
                img.scaleToFit(PageSize.A4);
                // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
                document.newPage();
                document.add(img);
                baosi.reset();
            }
            // 第五步：关闭文档。
            document.close();
            return Base64Util.encode(baos.toByteArray());
        }
    }

    public static byte[] manipulatePdfNew(byte[] bytes, MultiParam multiParam) throws Exception {
        log.info("manipulatePdfNew 脱密 雾化处理 start");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PDDocument pdDocument = PDDocument.load(bytes);
             ByteArrayOutputStream baosi = new ByteArrayOutputStream()) {
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pageTotal = pdDocument.getNumberOfPages();
            Document document = new Document();
            document.setMargins(0, 0, 0, 0);
            // 第二步：创建一个PdfWriter实例，
            PdfWriter.getInstance(document, baos);
            // 第三步：打开文档。
            document.open();
            float proportion = renderer.renderImageWithDPI(0, DPI).getWidth() / multiParam.getPdfWidth();
            for (int i = 0; i < pageTotal; i++) {
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, DPI);
                boolean b2 = multiParam.getPageNum() - 1 == i ||
                        (multiParam.getPageNumEnd() - 1 >= i && multiParam.getPageNum() - 1 <= i);
                if (b2) {
                    log.info("manipulatePdfNew 脱密 雾化处理 for pageTotal:{}, i:{}",pageTotal,i);
                    float height = multiParam.getUry() * proportion;
                    float width = multiParam.getUrx() * proportion;
                    float x = (multiParam.getLlx() * proportion);
                    float y = (multiParam.getLly() * proportion);
                    int xi = Math.round((x - width / HALF) < 0 ? 0 : (x - width / HALF));
                    int yi = Math.round(bufferedImage.getHeight() - y - height / HALF);
                    int widthi = Math.round(width);
                    int heighti = Math.round(height);
                    if (multiParam.isGrey()) {
                        ImgUtil.grayScaleDeal(bufferedImage, xi, yi, widthi, heighti);
                    }
                    if (multiParam.isFoggy()) {
                        int[] values = getPixArray(bufferedImage, xi, yi, widthi, heighti);
                        ImgUtil.doBlur(values, widthi, heighti, RADIUS);
                        bufferedImage.setRGB(xi, yi, widthi, heighti, values, 0, widthi);
                    }
                }

                ImageIO.write(bufferedImage, "png", baosi);
                Image img = Image.getInstance(baosi.toByteArray());
                img.setAlignment(Image.ALIGN_CENTER);
                img.scaleToFit(PageSize.A4);
                // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
                document.newPage();
                document.add(img);
                baosi.reset();
            }
            // 第五步：关闭文档。
            document.close();
            log.info("manipulatePdfNew 脱密 雾化处理 end");
            return baos.toByteArray();
        }
    }

    public static void manipulatePdfNew(byte[] bytes, int page, ByteArrayOutputStream output, List<SysSignatureLog> signatureLogs) throws IOException, AWTException, InterruptedException {
        try (PDDocument pdDocument = PDDocument.load(bytes)) {
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            BufferedImage bufferedImage = renderer.renderImageWithDPI(page, DPI);
            for (SysSignatureLog sysSignatureLog : signatureLogs) {
                String multiParamStr = sysSignatureLog.getMultiParam();
                getOutStream(page, renderer, bufferedImage, multiParamStr);
            }
            ImageIO.write(bufferedImage, "jpg", output);
        }
    }

    private static void getOutStream(int page, PDFRenderer renderer, BufferedImage bufferedImage, String multiParamStr) throws IOException, InterruptedException, AWTException {
        if (StringUtils.isNotBlank(multiParamStr)) {
            MultiParam multiParam = JSONObject.parseObject(multiParamStr, MultiParam.class);
            if (null != multiParam) {
                boolean b1 = multiParam.isGrey() || multiParam.isFoggy();
                boolean b2 = multiParam.getPageNum() - 1 == page ||
                        (multiParam.getPageNumEnd() - 1 >= page && multiParam.getPageNum() - 1 <= page);
                boolean b = b2 && b1;
                getBufferedImage(renderer, bufferedImage, multiParam, b);
            }
        }
    }

    private static void getBufferedImage(PDFRenderer renderer, BufferedImage bufferedImage, MultiParam multiParam, boolean b) throws IOException, InterruptedException, AWTException {
        if (b) {
            float proportion = renderer.renderImageWithDPI(0, DPI).getWidth() / multiParam.getPdfWidth();
            float height = multiParam.getUry() * proportion;
            float width = multiParam.getUrx() * proportion;
            float x = (multiParam.getLlx() * proportion);
            float y = (multiParam.getLly() * proportion);
            int xi = Math.round((x - width / HALF) < 0 ? 0 : (x - width / HALF));
            int yi = Math.round(bufferedImage.getHeight() - y - height / HALF);
            int widthi = Math.round(width);
            int heighti = Math.round(height);
            if (multiParam.isGrey()) {
                ImgUtil.grayScaleDeal(bufferedImage, xi, yi, widthi, heighti);
            }
            if (multiParam.isFoggy()) {
                int[] values = getPixArray(bufferedImage, xi, yi, widthi, heighti);
                ImgUtil.doBlur(values, widthi, heighti, RADIUS);
                bufferedImage.setRGB(xi, yi, widthi, heighti, values, 0, widthi);
            }
        }
    }

    private static int[] getPixArray(java.awt.Image im, int x, int y, int w, int h) throws InterruptedException, AWTException {
        int[] pix = new int[w * h];
        PixelGrabber pg = new PixelGrabber(im, x, y, w, h, pix, 0, w);
        if (!pg.grabPixels()) {
            throw new AWTException("pg error" + pg.status());
        }
        return pix;
    }

}