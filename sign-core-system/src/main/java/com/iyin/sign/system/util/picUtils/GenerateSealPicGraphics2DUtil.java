package com.iyin.sign.system.util.picUtils;


import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.util.UnicodeToStringUtil;
import com.iyin.sign.system.util.picUtils.doc.XFont;
import com.iyin.sign.system.util.picUtils.xdoc.XDoc;
import com.iyin.sign.system.vo.DefineSeal.CircularSealPicDTO;
import com.iyin.sign.system.vo.DefineSeal.OvalSealPicDTO;
import com.iyin.sign.system.vo.req.GeneratePicReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class GenerateSealPicGraphics2DUtil {

    private static Logger logger = LoggerFactory.getLogger(GenerateSealPicGraphics2DUtil.class);

    /**
     * （样章）
     */
    private static final String SEAL_BOTTOM_SUFFIX = "";

    /**
     * 五角星
     */
    private static final int FIVE_POINTED_STAR = 0;

    /**
     * 单线五角星
     */
    private static final int SINGLE_PENTACLE = 1;

    /**
     * 党徽
     */
    private static final int PARTY_EMBLEM = 2;



    /**
     * 取圆形章模 所有的 固定的 属性
     *
     * @return
     */
    public static CircularSealPicDTO getPropCircle() {
        CircularSealPicDTO circularSealPicDTO = new CircularSealPicDTO();
        circularSealPicDTO.setWidthPX(Integer.valueOf(SealPropConfigUtil.getPropValue("circle.width.PX")));
        circularSealPicDTO.setHeightPX(Integer.valueOf(SealPropConfigUtil.getPropValue("circle.height.PX")));
        circularSealPicDTO.setWidthReal(Integer.valueOf(SealPropConfigUtil.getPropValue("circle.width.real")));
        circularSealPicDTO.setHeightReal(Integer.valueOf(SealPropConfigUtil.getPropValue("circle.height.real")));
        circularSealPicDTO.setPlyCircle(Float.valueOf(SealPropConfigUtil.getPropValue("circle.ply")));
        //设置印章图案及字体的描绘颜色
        circularSealPicDTO.setColorRgb(SealPropConfigUtil.getPropValue("circle.colorRGB"));
        circularSealPicDTO.setSizeTop(Integer.valueOf(SealPropConfigUtil.getPropValue("circle.size.top")));
        circularSealPicDTO.setSizeCenter(Integer.valueOf(SealPropConfigUtil.getPropValue("circle.size.center")));
        circularSealPicDTO.setSizeBottom(Integer.valueOf(SealPropConfigUtil.getPropValue("circle.size.bottom")));
        circularSealPicDTO.setFontTop(SealPropConfigUtil.getPropValue("circle.font.top"));
        circularSealPicDTO.setFontBottom(SealPropConfigUtil.getPropValue("circle.font.bottom"));
        circularSealPicDTO.setDpi(Integer.valueOf(SealPropConfigUtil.getPropValue("circle.DPI")));
        circularSealPicDTO.setYear(Integer.valueOf(SealPropConfigUtil.getPropValue("validity.year.seal")));
        //文件保存的根目录和相对路径
        circularSealPicDTO.setDirPath1(SealPropConfigUtil.getPropValue("resources.file.dir"));//默认本地为：D:/esign
        circularSealPicDTO.setDirPath2(SealPropConfigUtil.getPropValue("dir.path.real.seal"));//默认保存在/eseal/ 目录下
        //wzz 1027 追加圆形边距属性
        circularSealPicDTO.setSpaceBetweenPicAndPly(Float.valueOf(SealPropConfigUtil.getPropValue("circle.spaceBetweenPicAndPly")));
        circularSealPicDTO.setSpaceBetweenPlyAndWord(Float.valueOf(SealPropConfigUtil.getPropValue("circle.spaceBetweenPlyAndWord")));
        return circularSealPicDTO;
    }


    public static OvalSealPicDTO getPropOval() {
        OvalSealPicDTO ov = new OvalSealPicDTO();

        //设置dpi和年限
        ov.setDpi(Integer.valueOf(SealPropConfigUtil.getPropValue("oval.DPI")));
        ov.setYear(Integer.valueOf(SealPropConfigUtil.getPropValue("validity.year.seal")));

        //文件保存的根目录和相对路径
        ov.setDirPath1(SealPropConfigUtil.getPropValue("resources.file.dir"));//默认本地为：D:/esign
        ov.setDirPath2(SealPropConfigUtil.getPropValue("dir.path.real.seal"));//默认保存在/eseal/ 目录下

        //设置doc文件和字体样式文件
        /*map.put("filePathXdoc1", SealPropConfigUtil.getFilePathOfXdocForOval1());
        map.put("filePathXdoc2", SealPropConfigUtil.getFilePathOfXdocForOval2());
        map.put("dirPathFont", SealPropConfigUtil.getDirPathOfFont());*/

        return ov;
    }


    /**
     * 配置生成圆形印模样章的方法
     *
     * @param generatePicReqVO
     * @return
     */
    public static CircularSealPicDTO configurationCircularSealPic(GeneratePicReqVO generatePicReqVO) {
        CircularSealPicDTO cir = getPropCircle();
        cir.setTopCircle(generatePicReqVO.getEnterpriseName());
        cir.setBottomCircle(generatePicReqVO.getSealName() + SEAL_BOTTOM_SUFFIX);
        //设置章模内部图案
        if (generatePicReqVO.getCenterDesign() == FIVE_POINTED_STAR) {
            cir.setCenterCircle("★");
        } else if (generatePicReqVO.getCenterDesign() == SINGLE_PENTACLE) {
            cir.setCenterCircle("☆");
        } else if (generatePicReqVO.getCenterDesign() == PARTY_EMBLEM) {
            String s= UnicodeToStringUtil.unicodeToString("\u262D");
            log.info("configurationCircularSealPic s:{}",s);
            cir.setCenterCircle(s);
        } else {
            cir.setCenterCircle("");
        }
        cir.setFileName(generatePicReqVO.getEnterpriseName() + generatePicReqVO.getSealName() + ".png");
        return cir;
    }

    /**
     * 入参转换，配置需生成的椭圆章
     *
     * @param enterpriseName
     * @param enterpriseCode
     * @param sealCode
     * @param sealName
     * @return
     */
    public static OvalSealPicDTO configurationOvalPic(String enterpriseName, String enterpriseCode, String sealCode, String sealName) {
        OvalSealPicDTO ov = getPropOval();
        ov.setEnterpriseCode(enterpriseCode);
        ov.setSealCode(sealCode);
        ov.setTopWords(enterpriseName);
        ov.setBottomWords(sealName + SEAL_BOTTOM_SUFFIX);
        //Xdox文件
        if (sealName != null && !"".equals(sealName.trim())) {
            ov.setXdocPath(new File(GenerateSealPicGraphics2DUtil.class.getClassLoader().getResource("config/oval_2.xdoc").getFile()).getAbsolutePath());
        } else {
            ov.setXdocPath(new File(GenerateSealPicGraphics2DUtil.class.getClassLoader().getResource("config/oval_1.xdoc").getFile()).getAbsolutePath());
        }
        //样式
        ov.setFontPath(new File(GenerateSealPicGraphics2DUtil.class.getClassLoader().getResource("config/fonts").getFile()).getAbsolutePath());
        ov.setFileName(enterpriseName + sealName + ".png");
        String dir = ov.getDirPath1() + "/" + ov.getDirPath2() + "/" + enterpriseCode + "/" + sealCode;
        File fileDir = new File(dir);
        if (!fileDir.exists())
            fileDir.mkdirs();
        ov.setAbsPath(dir + "/" + ov.getFileName());
        return ov;
    }

    /**
     * 生成圆形印章图片
     *
     * @param circularSealPicDTO
     * @return
     */
    public static BufferedImage generateBufferedImageCircle(CircularSealPicDTO circularSealPicDTO) {
        //----->取图片的用户输入信息:图片加入的方字
        String topCircle = circularSealPicDTO.getTopCircle();
        String centerCircle = circularSealPicDTO.getCenterCircle();
        String bottomCircle = circularSealPicDTO.getBottomCircle();
        //----->取图片固定信息
        int widthPX = circularSealPicDTO.getWidthPX();
        int heightPX = circularSealPicDTO.getHeightPX();
        float plyCircle = circularSealPicDTO.getPlyCircle();
        String str_RGB = circularSealPicDTO.getColorRgb();
        String arr_RGB[] = str_RGB.split(" ");
        int R = Integer.parseInt(arr_RGB[0]);
        int G = Integer.parseInt(arr_RGB[1]);
        int B = Integer.parseInt(arr_RGB[2]);
        int sizeTop = circularSealPicDTO.getSizeTop();
        int sizeCenter = circularSealPicDTO.getSizeCenter();
        int sizeBottom = circularSealPicDTO.getSizeBottom();
        String fontTop = circularSealPicDTO.getFontTop();
        String fontBottom = circularSealPicDTO.getFontBottom();
        //wzz 1027 修正圆形，追加夹距
        //图与外圆边框夹距
        float spaceBetweenPicAndPly = circularSealPicDTO.getSpaceBetweenPicAndPly();
        //文字与外圆半径夹距
        float spaceBetweenPlyAndWord = circularSealPicDTO.getSpaceBetweenPlyAndWord();
        BufferedImage buffImg;
        //wzz 1101 追加linux环境下异常处理
        try {
            //开始绘画
            //创建一个8位深的图片，宽高用px指定
            buffImg = new BufferedImage(widthPX, heightPX, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffImg.createGraphics();
            g.setBackground(Color.WHITE);
            //清除指定矩形背景并用当前绘图颜色填充
            g.clearRect(0, 0, widthPX, heightPX);
            //1.----------画圆形----------
            g.setColor(new Color(R, G, B));
            //wzz 0922 设置圆形边框厚度
            g.setStroke(new BasicStroke(plyCircle, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            //绘制圆 以左上角为原点，右、下 皆为正
            //wzz 0922 这个半径是以   圆 点   到   边框厚度中心  计算的
            int radius = heightPX / 2 - (int) spaceBetweenPicAndPly - (int) (plyCircle / 2);
            //原点X坐标
            int CENTERX = widthPX / 2;
            //原点Y坐标
            int CENTERY = heightPX / 2;
            Ellipse2D circle = new Ellipse2D.Double();
            circle.setFrameFromCenter(CENTERX, CENTERY, CENTERX + radius, CENTERY + radius);
            g.draw(circle);
            //2.----------填充内部----------
            //----->2.1添加中间的五角星 原来的★现在的★
            Font font_center = new Font("", Font.PLAIN, sizeCenter);
            FontRenderContext context_center = g.getFontRenderContext();
            Rectangle2D bounds_center = font_center.getStringBounds(centerCircle, context_center);
            g.setFont(font_center);
            //以图形左下角 为画点。Y往上偏移了
            g.drawString(centerCircle, (float) (CENTERX - bounds_center.getCenterX()), CENTERY + (sizeCenter / 3));
            //----->2.2添加下部的文字
            //1.设置 规格的字体属性
            //wzz 1112 使用自定义字体
            Font font_bottom = CustomFontUtil.getCustomFont(fontBottom);
            if (font_bottom == null) {
                return null;
            }
            font_bottom = font_bottom.deriveFont(Font.BOLD);
            font_bottom = font_bottom.deriveFont((float) sizeBottom);
            //wzz 1208 自动调节
            AffineTransform transformBottom = AffineTransform.getScaleInstance(1, 1.6);
            font_bottom = font_bottom.deriveFont(transformBottom);
            FontRenderContext context_bottom = g.getFontRenderContext();
            Rectangle2D bounds_bottom = font_bottom.getStringBounds(("".equals(bottomCircle)) ? "" :
                    (bottomCircle.substring(0, 1)), context_bottom);
            g.setFont(font_bottom);

            //wzz 1202 取固定区域数据
            float widthOfFixedAreaInBottom = (float) (radius * Math.sin(Math.toRadians(50)) * 2);
            int lengthOfTextInBottom = bottomCircle.length();
            //平局布局下，每个文字x增量
            float incrementOfFixedAreaInBottom = widthOfFixedAreaInBottom / (lengthOfTextInBottom + 1);
            //起点
            String[] messagesTempOfTextInBottom = bottomCircle.split("", 0);
            String[] messagesOfTextInBottom = new String[messagesTempOfTextInBottom.length];
            System.arraycopy(messagesTempOfTextInBottom, 0, messagesOfTextInBottom, 0, messagesTempOfTextInBottom.length);
            float xStartOfTextInBottom = CENTERX - widthOfFixedAreaInBottom / 2;
            for (int i = 0; i < lengthOfTextInBottom; i++) {
                //单个文字
                String singleOfTextInBottom = messagesOfTextInBottom[i];
                //单个文字x坐标  第i个竖线位置 - 文字绘制偏移量
                float x_singleOfTextInBottom = (float) ((xStartOfTextInBottom + (i + 1) * incrementOfFixedAreaInBottom) - bounds_bottom.getCenterX());
                float y_singleOfTextInBottom = (float) (CENTERY + (sizeCenter / 3) - (bounds_bottom.getCenterY() * 2.8 * 1.6));
                g.drawString(singleOfTextInBottom, x_singleOfTextInBottom, y_singleOfTextInBottom);
            }
            //----->2.3添加上部文字，通过角度计算坐标，计算字体格式
            //--->1.计算字符宽度
            //根据输入字符串得到字符数组
            String[] messages2 = topCircle.split("", 0);
            String[] messages = new String[messages2.length];
            System.arraycopy(messages2, 0, messages, 0, messages2.length);
            //输入的字数
            int length_top_circle = messages.length;
            //设置字体属性
            //wzz 1112 使用自定义字体
            Font font_top = CustomFontUtil.getCustomFont(fontTop);
            if (font_top == null) {
                return null;
            }
            font_top = font_top.deriveFont(Font.BOLD);
            font_top = font_top.deriveFont((float) sizeTop);
            AffineTransform transformTop = new AffineTransform();
            transformTop.scale(1, 2);
            font_top = font_top.deriveFont(transformTop);
            FontRenderContext context_top = g.getFontRenderContext();
            Rectangle2D bounds_top = font_top.getStringBounds(topCircle, context_top);
            //每个字符宽度＝字符串长度(矩形总宽度)/字符数
            double char_interval = (bounds_top.getWidth() / length_top_circle);
            //--->2.取奇偶长度下，分左右，取文字在数组中下标
            //文字高度
            double ascent = -bounds_top.getY();
            int first = 0, second = 0;
            //奇偶字数 odd奇数
            boolean odd = false;
            if (length_top_circle % 2 == 1) {
                //奇数  左右对半  中间有一个字  这个first刚好是数组中的 那个中间字 的 下标
                first = (length_top_circle - 1) / 2;
                odd = true;
            } else {
                //偶数  左右对半  中间无字
                first = (length_top_circle) / 2 - 1;
                second = (length_top_circle) / 2;
                odd = false;
            }
            //--->3.取参考坐标长度 和 单个文字的弧度
            //内圆半径  外圆半径 减去 文字高度
            double radius_in = radius - plyCircle / 2 - spaceBetweenPlyAndWord - ascent;
            //圆心X
            double x0 = CENTERX;
            //内外圆之间的Y  Y固定 （height - 内圆半径）
            double y0 = CENTERY - radius_in;
            //旋转角度   字符底边/2  除  内圆半径 得 角度/2，乘 2 得 一个字的旋转角度
            double angle_one = 2 * Math.asin(char_interval / (2 * radius_in));
            //wzz 1102 根据字数，自动修正字体间距 最多18个
            //long timeStart = System.currentTimeMillis();
            if (length_top_circle >= 16) {
                angle_one = angle_one - (length_top_circle - 15) * 0.01;
            } else if (length_top_circle >= 13) {
                angle_one = angle_one + 0.01 + 0.02 * (15 - length_top_circle);
            } else if (length_top_circle >= 11) {
                angle_one = angle_one + 0.08 + 0.04 * (12 - length_top_circle);
            } else if (length_top_circle >= 8) {
                angle_one = angle_one + 0.16 + 0.05 * (10 - length_top_circle);
            } else if (length_top_circle == 7) {
                angle_one = angle_one + 0.35;
            } else if (length_top_circle == 6) {
                angle_one = angle_one + 0.47;
            } else if (length_top_circle <= 5) {
                angle_one = angle_one + 0.64 + 0.3 * (5 - length_top_circle);
            }
            //--->4.分奇偶，分左右，计算字 弧度、坐标、字体，并画出
            if (odd) {
                //奇数
                //画中间那个字 x坐标（原点 - （每个字宽度/2））  y坐标
                g.setFont(font_top);
                g.drawString(messages[first], (float) (x0 - char_interval / 2), (float) y0);

                //中心点的右边
                for (int i = first + 1; i < length_top_circle; i++) {
                    //右边第 n 个，角度偏移 n * ∠
                    double angle_n = (i - first) * angle_one;
                    //计算x坐标
                    //1.圆心竖线 到 n个字中线的偏移X
                    double x_offset = radius_in * Math.sin(angle_n);
                    //2.修正的半个字的x
                    double x_char_half = (char_interval / 2) * Math.cos(angle_n);
                    //3.得出真实x
                    double x_n = x0 + x_offset - x_char_half;
                    //计算y坐标
                    //1.计算n个字中线偏移y
                    double y_offset = radius_in - radius_in * Math.cos(angle_n);
                    //2.修正中线y 到 字左下角的距离
                    double y_char_half = (char_interval / 2) * Math.sin(angle_n);
                    //3.追加内圆外圆部分，得出真实y
                    double y_n = y0 + y_offset - y_char_half;
                    //通过旋转角度计算字体格式
                    transformTop.setToRotation(angle_n);
                    transformTop.scale(1, 2);
                    Font font_right = font_top.deriveFont(transformTop);
                    g.setFont(font_right);
                    //画出
                    g.drawString(messages[i], (float) (x_n), (float) (y_n));
                }
                //中心点的左边
                for (int i = first - 1; i > -1; i--) {
                    double angle_n = (first - i) * angle_one;
                    //计算x坐标
                    //1.圆心竖线 到 n个字中线的偏移X
                    double x_offset = radius_in * Math.sin(angle_n);
                    //2.修正的半个字的x
                    double x_char_half = (char_interval / 2) * Math.cos(angle_n);
                    //3.得出真实x
                    double x_n = x0 - x_offset - x_char_half;
                    //计算y坐标
                    //1.计算n个字中线偏移y
                    double y_offset = radius_in - radius_in * Math.cos(angle_n);
                    //2.修正中线y 到 字左下角的距离
                    double y_char_half = (char_interval / 2) * Math.sin(angle_n);
                    //3.追加内圆外圆部分，得出真实y
                    double y_n = y0 + y_offset + y_char_half;
                    transformTop.setToRotation(-angle_n);
                    transformTop.scale(1, 2);
                    Font font_left = font_top.deriveFont(transformTop);
                    g.setFont(font_left);
                    g.drawString(messages[i], (float) (x_n), (float) (y_n));
                }
            } else {
                //偶数  6;  first下标 = 2;second下标 = 3
                //中心点的右边
                for (int i = second; i < length_top_circle; i++) {
                    //圆心竖线 到 第n个左下角的角度
                    double angle_n = (i - second + 0.5) * angle_one;
                    //计算x坐标
                    //1.圆心竖线 到 n个字中线的偏移X
                    double x_offset = radius_in * Math.sin(angle_n);
                    //2.修正的半个字的x
                    double x_char_half = (char_interval / 2) * Math.cos(angle_n);
                    //3.得出真实x
                    double x_n = x0 + x_offset - x_char_half;
                    //计算y坐标
                    //1.计算n个字中线偏移y
                    double y_offset = radius_in - radius_in * Math.cos(angle_n);
                    //2.修正中线y 到 字左下角的距离
                    double y_char_half = (char_interval / 2) * Math.sin(angle_n);
                    //3.追加内圆外圆部分，得出真实y
                    double y_n = y0 + y_offset - y_char_half;
                    transformTop.setToRotation(angle_n);
                    transformTop.scale(1, 2);
                    Font font_right = font_top.deriveFont(transformTop);
                    g.setFont(font_right);
                    g.drawString(messages[i], (float) (x_n), (float) (y_n));
                }
                //中心点的左边
                for (int i = first; i > -1; i--) {
                    double angle_n = (first - i + 0.5) * angle_one;
                    //计算x坐标
                    //1.圆心竖线 到 n个字中线的偏移X
                    double x_offset = radius_in * Math.sin(angle_n);
                    //2.修正的半个字的x
                    double x_char_half = (char_interval / 2) * Math.cos(angle_n);
                    //3.得出真实x
                    double x_n = x0 - x_offset - x_char_half;
                    //计算y坐标
                    //1.计算n个字中线偏移y
                    double y_offset = radius_in - radius_in * Math.cos(angle_n);
                    //2.修正中线y 到 字左下角的距离
                    double y_char_half = (char_interval / 2) * Math.sin(angle_n);
                    //3.追加内圆外圆部分，得出真实y
                    double y_n = y0 + y_offset + y_char_half;
                    transformTop.setToRotation(-angle_n);
                    transformTop.scale(1, 2);
                    Font font_left = font_top.deriveFont(transformTop);
                    g.setFont(font_left);
                    g.drawString(messages[i], (float) (x_n), (float) (y_n));
                }
            }
            //wzz 1208 图形立即释放资源。比GC好一点
            //Java 程序运行时，可以在一个短时间帧内创建大量的 Graphics 对象。
            // 尽管垃圾回收器的终止进程也能够进行相同的系统资源释放，
            // 但是最好通过调用此方法来手工释放相关资源，而不是依靠终止进程，
            // 因为终止进程需要很长时间才能结束运行。
            g.dispose();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            return null;
        }
        //wzz 0924 2016年9月24日 10:52:28
        return buffImg;
    }


    /**
     * 生成椭圆图片的方法
     *
     * @param ovalSealPicDTO
     * @return
     */
    public static BufferedImage generateBufferedImageOval1(OvalSealPicDTO ovalSealPicDTO) {
        BufferedImage bufferedImage = null;
        try {
            String contentTemplate = FileUtils.readFileToString(new File(ovalSealPicDTO.getXdocPath()), "UTF-8").replace("topOval", ovalSealPicDTO.getTopWords()).replace("bottomOval", ovalSealPicDTO.getBottomWords() != null ? ovalSealPicDTO.getBottomWords() : "");
            XFont.init(ovalSealPicDTO.getFontPath());//字体样式初始化
            //写XDoc文件
            XDoc.write(contentTemplate, new FileOutputStream(new File(ovalSealPicDTO.getAbsPath())), "png");
            bufferedImage = ImageIO.read(new File(ovalSealPicDTO.getAbsPath()));
        } catch (Exception e) {
            System.out.println("生成椭圆章图片失败，失败原因为：" + e.getLocalizedMessage());
        }
        return bufferedImage;
    }

    /**
     * 保存格珊图片到本地
     *
     * @param signPic
     * @param DPI
     * @param filePath
     * @return
     */
    public static boolean saveGridImage(BufferedImage signPic, Integer DPI, String filePath) {
        File file = new File(filePath);
        //wzz 0922 原png
        final String file_ext = "png";

        for (Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName(file_ext); iterator.hasNext(); ) {
            ImageWriter writer = iterator.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                continue;
            }
            ImageOutputStream stream = null;
            try {
                setDPI(metadata, DPI);
                stream = ImageIO.createImageOutputStream(file);
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(signPic, null, metadata), writeParam);
            } catch (Exception e) {
                e.printStackTrace();
                if (file.exists()) {
                    file.delete();
                }
                return false;
            } finally {
                try {
                    if(stream!=null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
        return true;
    }

    /**
     * 设置dpi
     *
     * @param metadata
     * @param DPI
     * @throws IIOInvalidTreeException
     */
    private static void setDPI(IIOMetadata metadata, Integer DPI) throws IIOInvalidTreeException {
        //double dotsPerMilli = 1.0 * DPI / 10 / INCH_2_CM;
        //DPI就是你要设置的值，java中默认是72，如果需要打印图片，可能就要设置为300
        //INCH_2_CM表示1英寸等于多少厘米，这里就是2.54
        double dotsPerMilli = 1.0 * DPI / 10 / 2.54;
        //wzz 0924 水平像素
        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMilli));
        //wzz 0924 垂直像素
        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMilli));
        //wzz 0924 维度
        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);
        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);
        metadata.mergeTree("javax_imageio_1.0", root);
    }

    public static void main(String[] args) {
        GeneratePicReqVO generatePicReqVO=new GeneratePicReqVO();
        generatePicReqVO.setCenterDesign(2);
        generatePicReqVO.setEnterpriseName("深圳安印科技有限公司");
        generatePicReqVO.setSealName("安印科技");
        //1、组装生成图片的参数
        CircularSealPicDTO circularSealPicDTO = GenerateSealPicGraphics2DUtil.configurationCircularSealPic(generatePicReqVO);
        //2、绘图，成生bufferedImage
        BufferedImage signPic = GenerateSealPicGraphics2DUtil.generateBufferedImageCircle(circularSealPicDTO);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             FileOutputStream fos=new FileOutputStream(new File("D://"+ SnowflakeIdWorker.getIdToStr() +".png"))){
            ImageIO.write(signPic, "png", out);
            out.writeTo(fos);
            out.flush();
        } catch (IOException e) {
            log.error("BufferedImage转ByteArrayOutputStream异常：{}", e.getMessage());
        }
    }


//    /**
//     * 生成印章样章的统一方法
//     *
//     * @param picType
//     * @param enterpriseName
//     * @param enterpriseCode
//     * @param sealCode
//     * @param sealName
//     * @param centerDesign   中心设计标识，0为内部为五角星，1为三角形，2为矩形，3为十字形
//     * @return
//     * @date 2018-01-16
//     * @author liushuqiao
//     */
//    public static byte[] generateSealPic(Integer picType, String enterpriseName, String enterpriseCode, String sealCode, String sealName, Integer centerDesign) {
//
//        if (sealCode == null || StringUtils.isEmpty(enterpriseName)
//                || StringUtils.isEmpty(enterpriseCode) || StringUtils.isEmpty(sealCode) || StringUtils.isEmpty(sealName)) {
//            return null;
//        }
//        boolean fg = Boolean.FALSE;
//        File file = null;
//        if (picType == 1) {
//            CircularSealPicDTO circularSealPicDTO = configurationCircularSealPic(enterpriseName, enterpriseCode, sealCode, sealName, centerDesign);
//            fg = saveGridImage(generateBufferedImageCircle(circularSealPicDTO), circularSealPicDTO.getDpi(), circularSealPicDTO.getAbsPath());
//            if (fg) {
//                file = new File(circularSealPicDTO.getAbsPath());
//            }
//        } else if (picType == 2) {
//            OvalSealPicDTO ovalSealPicDTO = configurationOvalPic(enterpriseName, enterpriseCode, sealCode, sealName);
//            fg = saveGridImage(generateBufferedImageOval1(ovalSealPicDTO), ovalSealPicDTO.getDpi(), ovalSealPicDTO.getAbsPath());
//            if (fg) {
//                file = new File(ovalSealPicDTO.getAbsPath());
//            }
//        } else {
//
//        }
//        if (file != null) {//输出流
//            byte[] buffer = null;
//            try(FileInputStream fis = new FileInputStream(file);
//                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {
//                byte[] b = new byte[1024];
//                int n;
//                while ((n = fis.read(b)) != -1) {
//                    bos.write(b, 0, n);
//                }
//                buffer = bos.toByteArray();
//                return buffer;
//            } catch (FileNotFoundException e) {
//                logger.error("输入Bytes发生异常："+e.getLocalizedMessage());
//            } catch (IOException e) {
//                logger.error("输入Bytes发生异常："+e.getLocalizedMessage());
//            }
//        }
//        return null;
//    }


}