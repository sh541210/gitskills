package com.iyin.sign.system.util.sign;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author:liushuqiao
 * @title: PdfReaderUtil
 * @description:Pdf阅读工具类
 * @date: 14:12 2017/11/6
 * @version: v1.0.0
 */
@Slf4j
public class PdfReaderUtil {

    private PdfReaderUtil() {
        throw new IllegalStateException("PdfReaderUtil class");
    }

    /**
     * 获取Pdf文件中关键坐标，包括页数，横纵坐标，返回一个数组
     *
     * @param keyWords
     * @param fileAbsPath
     * @return
     * @throws IOException
     */
    private static int i = 1;
    private static List<Object[]> arrays2 = null;
    private static Rectangle2D.Float boundingRectange;
    private static StringBuilder content = new StringBuilder();
    private static Object[] objects;

    private static void handlePdf(final String keyWord, int pageNum, PdfReaderContentParser pdfReaderContentParser) throws IOException {
        //以页为单位循环读取!
        for (i = 1; i <=pageNum; i++) {
            pdfReaderContentParser.processContent(i, new RenderListener() {
                @Override
                public void renderText(TextRenderInfo textRenderInfo) {
                    // 此处不会读整页内容，每次读一个字块，可以几个汉字或者一个
                    String text = textRenderInfo.getText();
                    //这一页的内容每个字段地往里面加，直到包括了关键字段为
                    content.append(text);
                    boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
                    if (content.toString().contains(keyWord)) {
                        objects = new Object[4];
                        objects[0] = i;
                        objects[1] = boundingRectange.x;
                        objects[2] = boundingRectange.y;
                        objects[3] = content;

                        arrays2.add(objects);
                        //包含了关键字以后清空重新开始
                        content.delete(0,content.length());
                    }
                }

                @Override
                public void renderImage(ImageRenderInfo arg0) {
                    log.info("handlePdf renderImage");
                }

                @Override
                public void endTextBlock() {
                    log.info("handlePdf endTextBlock");
                }

                @Override
                public void beginTextBlock() {
                    log.info("handlePdf beginTextBlock");
                }
            });
        }
    }

    public static List<Object[]> getKeyWord(final String keyWord, PdfReader pdfReader) throws IOException {
        int pageNum = pdfReader.getNumberOfPages();
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
        arrays2 = new ArrayList<>();
        handlePdf(keyWord, pageNum, pdfReaderContentParser);
        return arrays2;
    }

    /**
     * 获取关键字坐标（文档包含表单）
     * @Author: 唐德繁
     * @CreateDate: 2019/03/20 下午 3:54
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2019/03/20 下午 3:54
     * @Version: 0.0.1
     * @param pdfReader
     * @param keyword
     * @return
     */
    public static List<IyinTextLocationExtractionStrategy.TextRectangle> getNewKeyword(final String keyword, PdfReader pdfReader) {
        List<IyinTextLocationExtractionStrategy.TextRectangle> locations = new ArrayList<>();
        int pageNum = pdfReader.getNumberOfPages();
        PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
        try {
            Pattern pattern=Pattern.compile(keyword);
            for (i = 1; i <= pageNum; i++) {
                IyinTextLocationExtractionStrategy strategy = new IyinTextLocationExtractionStrategy(pattern);
                parser.processContent(i, strategy, Collections.emptyMap()).getResultantText();
                locations.addAll(strategy.getLocations(null, i));
            }
            if(locations.isEmpty()){
                // 获取关键字坐标
                List<Object[]> keyWordListEsign = getKeyWord(keyword, pdfReader);
                int pageNo;
                float coordinateX;
                float coordinateY;
                Object[] objects;
                for (int j = 0; j < keyWordListEsign.size(); j++) {
                    objects = keyWordListEsign.get(i);
                    pageNo = Integer.parseInt(objects[0].toString());
                    coordinateX = Float.parseFloat(objects[1].toString());
                    coordinateY = Float.parseFloat(objects[2].toString());
                    locations.add(i,new IyinTextLocationExtractionStrategy.TextRectangle(keyword,coordinateX,coordinateY,pageNo));
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return locations;
    }

}
