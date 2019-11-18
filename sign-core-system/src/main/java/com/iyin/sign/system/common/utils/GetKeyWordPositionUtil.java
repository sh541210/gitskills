package com.iyin.sign.system.common.utils;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
	* @Description 获取关键字定位的工具类
	* @Author: wdf
    * @CreateDate: 2019/8/23 16:06
	* @UpdateUser: wdf
    * @UpdateDate: 2019/8/23 16:06
	* @Version: 0.0.1
    * @param
    * @return
    */
@Slf4j
public class GetKeyWordPositionUtil {

    public static void main(String[] args) throws IOException {
        //1.给定文件
        File pdfFile = new File("D://2.pdf");
//        File pdfFile = new File("C:\\Users\\Administrator\\Downloads\\download.pdf");

        //4.指定关键字 水67果
        String keyword = "盖章";
        List<float[]> positions;
        PdfReader reader;

        //3.IO流读取文件内容
        try (FileInputStream inputStream = new FileInputStream(pdfFile)){
//            PDFParser pdfParser=new PDFParser(new RandomAccessBufferedFileInputStream(pdfFile.getPath()));
//            pdfParser.parse();
//            PDDocument document=pdfParser.getPDDocument();
//            PDFTextStripper stripper= new PDFTextStripper();
//
//            String res=stripper.getText(document);
//            System.out.println("res:"+res);

            reader=new PdfReader(inputStream);
//            long sotime=System.currentTimeMillis();
//            List<IyinTextLocationExtractionStrategy.TextRectangle> keyWordList = PdfReaderUtil.getNewKeyword(keyword, reader);
//            log.info("keyWordList:{}",keyWordList.size());
//            long eotime=System.currentTimeMillis();
//            log.info("j s-e:{}",eotime-sotime);
            //5.调用方法，给定关键字和文件
            long stime=System.currentTimeMillis();
            positions = findKeywordPostions(reader, keyword);
            long etime=System.currentTimeMillis();
            log.info("n s-e:{} ms ，positions.size:{}",etime-stime,positions.size());
        } catch (IOException e) {
            throw e;
        }

        //6.返回值类型是  List<float[]> 每个list元素代表一个匹配的位置，分别为 float[0]所在页码  float[1]所在x轴 float[2]所在y轴
        log.info("total:{}" ,positions.size());
        if (!positions.isEmpty()) {
            for (float[] position : positions) {
                log.info("pageNum: {} , x: {} , y: {}" , (int) position[0], position[1],position[2]);
            }
        }
    }


    /**
     * findKeywordPostions
     *
     * @param reader IO流
     * @param keyword 关键字
     * @return List<float   [   ]>  : float[0]:pageNum float[1]:x float[2]:y
     * @throws IOException
     */
    public static List<float[]> findKeywordPostions(PdfReader reader, String keyword) throws IOException {
        List<float[]> result = new ArrayList<>();
        List<PdfPageContentPositions> pdfPageContentPositions = getPdfContentPostionsList(reader);

        for (PdfPageContentPositions pdfPageContentPosition : pdfPageContentPositions) {
            List<float[]> charPositions = findPositions(keyword, pdfPageContentPosition);
            if (charPositions.isEmpty()) {
                continue;
            }
            result.addAll(charPositions);
        }
        return result;
    }


    private static List<PdfPageContentPositions> getPdfContentPostionsList(PdfReader reader) throws IOException {
        List<PdfPageContentPositions> result = new ArrayList<>();
        int pages = reader.getNumberOfPages();
        for (int pageNum = 1; pageNum <= pages; pageNum++) {
            float width = reader.getPageSize(pageNum).getWidth();
            float height = reader.getPageSize(pageNum).getHeight();
            PdfRenderListener pdfRenderListener = new PdfRenderListener(pageNum, width, height);
            //解析pdf，定位位置
            PdfContentStreamProcessor processor = new PdfContentStreamProcessor(pdfRenderListener);
            PdfDictionary pageDic = reader.getPageN(pageNum);
            PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
            try {
                processor.processContent(ContentByteUtils.getContentBytesForPage(reader, pageNum), resourcesDic);
            } catch (IOException e) {
                reader.close();
                throw e;
            }
            String content = pdfRenderListener.getContent();
            List<CharPosition> charPositions = pdfRenderListener.getcharPositions();
            List<float[]> positionsList = new ArrayList<>();
            for (CharPosition charPosition : charPositions) {
                float[] positions = new float[]{charPosition.getPageNum(), charPosition.getX(), charPosition.getY()};
                positionsList.add(positions);
            }

            PdfPageContentPositions pdfPageContentPositions = new PdfPageContentPositions();
            pdfPageContentPositions.setContent(content);
            pdfPageContentPositions.setPostions(positionsList);
            result.add(pdfPageContentPositions);
        }
        reader.close();
        return result;
    }


    private static List<float[]> findPositions(String keyword, PdfPageContentPositions pdfPageContentPositions) {
        List<float[]> result = new ArrayList<>();

        String content = pdfPageContentPositions.getContent();
        List<float[]> charPositions = pdfPageContentPositions.getPositions();

        for (int pos = 0; pos < content.length(); ) {
            int positionIndex = content.indexOf(keyword, pos);
            if (positionIndex == -1) {
                break;
            }
            float[] postions = charPositions.get(positionIndex);
            result.add(postions);
            pos = positionIndex + 1;
        }
        return result;
    }

    private static class PdfPageContentPositions {
        private String content;
        private List<float[]> positions;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<float[]> getPositions() {
            return positions;
        }

        public void setPostions(List<float[]> positions) {
            this.positions = positions;
        }
    }

    private static class PdfRenderListener implements RenderListener {
        private int pageNum;
        private float pageWidth;
        private float pageHeight;
        private StringBuilder contentBuilder = new StringBuilder();
        private List<CharPosition> charPositions = new ArrayList<>();

        public PdfRenderListener(int pageNum, float pageWidth, float pageHeight) {
            this.pageNum = pageNum;
            this.pageWidth = pageWidth;
            this.pageHeight = pageHeight;
        }

        @Override
        public void beginTextBlock() {
            if(log.isDebugEnabled()){
                log.debug("beginTextBlock");
            }
        }

        @Override
        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> characterRenderInfos = renderInfo.getCharacterRenderInfos();
            renderInfo.getAscentLine();
            for (TextRenderInfo textRenderInfo : characterRenderInfos) {
                String word = textRenderInfo.getText();
                if (word.length() > 1) {
                    word = word.substring(word.length() - 1);
                }
                Float rectangle = textRenderInfo.getAscentLine().getBoundingRectange();

                float x = (float)rectangle.getX();
                float y = (float)rectangle.getY();

                CharPosition charPosition = new CharPosition(pageNum, x, y);
                charPositions.add(charPosition);
                contentBuilder.append(word);
            }
        }

        @Override
        public void endTextBlock() {
            if(log.isDebugEnabled()) {
                log.debug("endTextBlock");
            }
        }

        @Override
        public void renderImage(ImageRenderInfo renderInfo) {
            if(log.isDebugEnabled()) {
                log.debug("renderImage");
            }
        }


        public String getContent() {
            return contentBuilder.toString();
        }


        public List<CharPosition> getcharPositions() {
            return charPositions;
        }

        @Override
        public String toString() {
            return "PdfRenderListener{" +
                    "pageNum=" + pageNum +
                    ", pageWidth=" + pageWidth +
                    ", pageHeight=" + pageHeight +
                    '}';
        }
    }


    private static class CharPosition {
        private int pageNum = 0;
        private float x = 0;
        private float y = 0;

        public CharPosition(int pageNum, float x, float y) {
            this.pageNum = pageNum;
            this.x = x;
            this.y = y;
        }


        public int getPageNum() {
            return pageNum;
        }


        public float getX() {
            return x;
        }


        public float getY() {
            return y;
        }


        @Override
        public String toString() {
            return "[pageNum=" + this.pageNum + ",x=" + this.x + ",y=" + this.y + "]";
        }
    }
}