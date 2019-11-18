package com.iyin.sign.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;
import com.iyin.sign.system.common.enums.SignatureEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.exception.ServiceException;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.sign.CoordinateParam;
import com.iyin.sign.system.model.sign.MultiParam;
import com.iyin.sign.system.service.ISignService;
import com.iyin.sign.system.util.ConvertImageAlphaUtil;
import com.iyin.sign.system.util.sign.IyinExternalSignatureContainer;
import com.iyin.sign.system.util.sign.IyinMakeSignature;
import com.iyin.sign.system.vo.sign.req.*;
import com.iyin.sign.system.vo.sign.resp.SignRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @ClassName: FastSignServiceImpl
 * @Description: 签章
 * @Author: yml
 * @CreateDate: 2019/6/22
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/22
 * @Version: 1.0.0
 */
@Service
@Slf4j
public class SignServiceImpl implements ISignService {

    private static final float IMAGE_DPI = 72f;

    private static final float DPI = 400f;

    private static final String TIMESTAMP_URL = "http://api.i-yin.net/tspserver/tsp";

    private static final String IMAGE_FORMAT = "png";
    private static final String IMAGE_TIME = "章模透明处理时间：";
    private static final String SIGN_CODE_TIME = "签章核心API时间：";
    private static final String PERFORATION_CODE_TIME = "骑缝签章核心API时间";
    private static final String SIGN_IN_TIME = "签署内部总时间：";
    private static final String PDF_NAME = "IYIN_STAMP";
    private static final String PDF_STRING_SIGN = "@";

    /**
     * 签章错误码
     */
    private static final int SIGNATURE_ERROR = 80008;
    private static final char PDF_VERSION = '\0';
    private static final int INITIAL_CAPACITY = 16;
    private static final int INITIAL_CAPACITY2 = 2;
    private static final int HALF_WIDTH = 2;

    /**
     * 单页签章
     *
     * @param singleSignReqVO
     * @return IyinResult<SignRespDTO>
     * @Author: 唐德繁
     * @CreateDate: 2018/11/27 下午 7:36
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/11/27 下午 7:36
     * @Version: 0.0.1
     */
    @Override
    public EsealResult<SignRespDTO> setSingleSign(SingleSignReqVO singleSignReqVO) throws IOException {
        try(ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            long time = System.currentTimeMillis();
            singleSign(singleSignReqVO, output);
            SignRespDTO signRespDTO = new SignRespDTO();
            //签名后Base64
            byte[] bytes = output.toByteArray();
            String signCompleteStr = Base64.encodeBase64String(bytes);
            signRespDTO.setSignCompleteFileStr(signCompleteStr);

            signRespDTO.setMultiParam(singleSignReqVO.getSignOriginalFileStr());
            //返回响应
            EsealResult<SignRespDTO> result = EsealResult.esealResult();
            result.setData(signRespDTO);
            log.info(SIGN_IN_TIME + (System.currentTimeMillis() - time));
            return result;
        }
    }

    public byte[] singleSign(SingleSignReqVO singleSignReqVO) throws IOException {
        try(ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            singleSign(singleSignReqVO, output);
            return output.toByteArray();
        }
    }

    private void singleSign(SingleSignReqVO singleSignReqVO, ByteArrayOutputStream output) {
        try {
            //获取证书信息
            String certPassword = singleSignReqVO.getCertPassword();
            String pfxStr = singleSignReqVO.getCertStr();
            PrivateKey pk = getPrivateKey(certPassword, pfxStr);
            Certificate[] chain = getCertificateChain(certPassword, pfxStr);
            //处理签章合同文件
            InputStream signOriginalFileStream = baseToInputStream(singleSignReqVO.getSignOriginalFileStr());
            PdfReader pdfReader = new PdfReader(signOriginalFileStream);
            float pdfHeight = pdfReader.getPageSize(1).getHeight();
            float pdfWidth = pdfReader.getPageSize(1).getWidth();
            int pageRotation = pdfReader.getPageRotation(1);
            if(0 != pageRotation){
                pdfHeight = pdfReader.getPageSize(1).getWidth();
                pdfWidth = pdfReader.getPageSize(1).getHeight();
            }
            //定义相关初始化参数
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            //PDF签章文档处理
            PdfStamper stamper = PdfStamper.createSignature(pdfReader, output, PDF_VERSION, null, true);
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

            byte[]  b = ConvertImageAlphaUtil.convertByte(baseToInputStream(singleSignReqVO.getSealStr()));
            //获取图片相关信息值
            ImageInfo imageInfo = Sanselan.getImageInfo(b);
            float sealWidth = imageInfo.getWidth() * IMAGE_DPI / DPI;
            float sealHeight = imageInfo.getHeight() * IMAGE_DPI / DPI;
            Image img = Image.getInstance(b);
            appearance.setSignatureGraphic(img);
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
            String fieldName = getFieldName();
            //最大X坐标和最大Y坐标
            CoordinateParam coordinateParam = handleCoordinate(singleSignReqVO.getCoordinatex(),
                    singleSignReqVO.getCoordinatey(), pdfHeight, pdfWidth, sealWidth, sealHeight);
            int pageNumber = singleSignReqVO.getPageNumber();
            appearance.setVisibleSignature(new Rectangle(coordinateParam.getLlx(), coordinateParam.getLly(),
                    coordinateParam.getUrx(), coordinateParam.getUry()), pageNumber, fieldName);
            // 定义时间戳
            TSAClient tsaClient = new TSAClientBouncyCastle(TIMESTAMP_URL);
            //摘要算法
            ExternalDigest digest = new BouncyCastleDigest();
            //签名算法
            ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, provider.getName());
            long endSignatureTime = System.currentTimeMillis();
            String sealCode = singleSignReqVO.getSealCode();
            //签章核心
            signCode(appearance, digest, signature, chain, tsaClient, sealCode);
            log.info(SIGN_CODE_TIME + (System.currentTimeMillis() - endSignatureTime));
            stamper.close();
            pdfReader.close();
            //章模图片处理
            MultiParam multiParam = new MultiParam();
            multiParam.setLlx(singleSignReqVO.getCoordinatex());
            multiParam.setLly(singleSignReqVO.getCoordinatey());
            multiParam.setUrx(sealWidth);
            multiParam.setUry(sealHeight);
            multiParam.setFoggy(singleSignReqVO.isFoggy());
            multiParam.setGrey(singleSignReqVO.isGrey());
            multiParam.setPageNum(pageNumber);
            multiParam.setPdfWidth(pdfWidth);
            singleSignReqVO.setSignOriginalFileStr(JSONObject.toJSONString(multiParam));
        } catch (Exception e) {
            log.error("单页签章异常：{}", e.getMessage());
            throw new BusinessException(SIGNATURE_ERROR, e.getMessage());
        }
    }

    /**
     * 签章核心
     *
     * @param appearance
     * @param digest
     * @param signature
     * @param chain
     * @param tsaClient
     * @param sealCode
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2019/03/07 下午 5:38
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2019/03/07 下午 5:38
     * @Version: 0.0.1
     */
    private void signCode(PdfSignatureAppearance appearance, ExternalDigest digest, ExternalSignature signature,
                          Certificate[] chain, TSAClient tsaClient, String sealCode) {
        try {
            if (org.apache.commons.lang.StringUtils.isEmpty(sealCode)) {
                MakeSignature.signDetached(appearance, digest, signature, chain, null, null, tsaClient, 0,
                        MakeSignature.CryptoStandard.CMS);
            } else {
                //设置签名域字典
                PdfDictionary dic = new PdfDictionary();
                PdfName pdfName = new PdfName(PDF_NAME);
                PdfString pdfString = new PdfString(sealCode + PDF_STRING_SIGN);
                dic.put(pdfName, pdfString);
                ExternalSignatureContainer external = new IyinExternalSignatureContainer(dic);
                IyinMakeSignature.signDetached(appearance, digest, signature, chain, null, null, tsaClient, 0,
                        MakeSignature.CryptoStandard.CMS, external);
            }
        } catch (Exception e) {
            log.error("签章核心异常：{}", e.getMessage());
        }
    }

    /**
     * 多页签章
     *
     * @param batchSignReqVO
     * @return IyinResult<SignRespDTO>
     * @Author: 唐德繁
     * @CreateDate: 2018/11/27 下午 7:41
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/11/27 下午 7:41
     * @Version: 0.0.1
     */
    @Override
    public EsealResult<SignRespDTO> setBatchSign(BatchSignReqVO batchSignReqVO) throws IOException {
        try(ByteArrayOutputStream outputStream = batchSign(batchSignReqVO)){
            long time = System.currentTimeMillis();
            SignRespDTO signRespDTO = new SignRespDTO();
            //签名后Base64
            byte[] bytes = outputStream.toByteArray();
            String signCompleteStr = Base64.encodeBase64String(bytes);
            signRespDTO.setSignCompleteFileStr(signCompleteStr);
            signRespDTO.setMultiParam(batchSignReqVO.getSignOriginalFileStr());
            //返回响应
            EsealResult<SignRespDTO> result = EsealResult.esealResult();
            result.setData(signRespDTO);
            log.info(SIGN_IN_TIME + (System.currentTimeMillis() - time));
            return result;
        }
    }

    public byte[] batch(BatchSignReqVO batchSignReqVO) throws IOException {
        try (ByteArrayOutputStream outputStream = batchSign(batchSignReqVO)) {
            return outputStream.toByteArray();
        }
    }

    private ByteArrayOutputStream batchSign(BatchSignReqVO batchSignReqVO) {
        ByteArrayOutputStream outputStream;
        try (InputStream signOriginalFileStream = baseToInputStream(batchSignReqVO.getSignOriginalFileStr())){
            long convertTime = System.currentTimeMillis();
            //章模图片处理
            byte[] b = ConvertImageAlphaUtil.convertByte(baseToInputStream(batchSignReqVO.getSealStr()));
            log.info(IMAGE_TIME + (System.currentTimeMillis() - convertTime));
            //获取图片相关信息值
            ImageInfo imageInfo = Sanselan.getImageInfo(b);
            float sealWidth = imageInfo.getWidth() * IMAGE_DPI / DPI;
            float sealHeight = imageInfo.getHeight() * IMAGE_DPI / DPI;
            Image img = Image.getInstance(b);
            //设置签名域
            String fieldName = getFieldName();
            byte[] buffer = setFormField(fieldName, batchSignReqVO, sealWidth, sealHeight);
            //获取证书信息
            String certPassword = batchSignReqVO.getCertPassword();
            String pfxStr = batchSignReqVO.getCertStr();
            PrivateKey pk = getPrivateKey(certPassword, pfxStr);
            Certificate[] chain = getCertificateChain(certPassword, pfxStr);
            //定义相关初始化参数
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            //签署签名域
            outputStream = signatureField(batchSignReqVO.getSealCode(), fieldName, buffer, img, pk, chain);
            PdfReader pdfReader = new PdfReader(signOriginalFileStream);
            float pdfWidth = pdfReader.getPageSize(1).getWidth();
            MultiParam multiParam = new MultiParam();
            multiParam.setLlx(batchSignReqVO.getCoordinateX());
            multiParam.setLly(batchSignReqVO.getCoordinateY());
            multiParam.setUrx(sealWidth);
            multiParam.setUry(sealHeight);
            multiParam.setFoggy(batchSignReqVO.isFoggy());
            multiParam.setGrey(batchSignReqVO.isGrey());
            multiParam.setPdfWidth(pdfWidth);
            multiParam.setPageNum(batchSignReqVO.getStartPageNumber());
            multiParam.setPageNumEnd(batchSignReqVO.getEndPageNumber());
            batchSignReqVO.setSignOriginalFileStr(JSONObject.toJSONString(multiParam));
            pdfReader.close();
        } catch (Exception e) {
            log.error("多页签章异常：{}", e.getMessage());
            throw new BusinessException(SIGNATURE_ERROR, e.getMessage());
        }
        return outputStream;
    }

    /**
     * 签署签名域
     *
     * @param fieldName
     * @param pdfBytes
     * @param img
     * @param pk
     * @param chain
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/12/04 上午 10:12
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/04 上午 10:12
     * @Version: 0.0.1
     */
    private ByteArrayOutputStream signatureField(String sealCode, String fieldName, byte[] pdfBytes, Image img,
                                                 PrivateKey pk, Certificate[] chain) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            PdfReader pdfReader = new PdfReader(pdfBytes);
            PdfStamper pdfStamper = PdfStamper.createSignature(pdfReader, output, PDF_VERSION, null, true);
            PdfSignatureAppearance appearance = pdfStamper.getSignatureAppearance();
            // 章模图片处理
            appearance.setSignatureGraphic(img);
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
            // 设置签署位置  根据已经创建的field
            appearance.setVisibleSignature(fieldName);
            // 定义时间戳
            TSAClient tsaClient = new TSAClientBouncyCastle(TIMESTAMP_URL);
            // 摘要算法
            ExternalDigest digest = new BouncyCastleDigest();
            // 签名算法
            ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, BouncyCastleProvider.PROVIDER_NAME);
            long endSignatureTime = System.currentTimeMillis();
            //签章核心
            signCode(appearance, digest, signature, chain, tsaClient, sealCode);
            log.info(SIGN_CODE_TIME + (System.currentTimeMillis() - endSignatureTime));
            pdfStamper.close();
            pdfReader.close();
        } catch (Exception e) {
            log.error("多页签章签署异常：{}", e.getMessage());
        }
        return output;
    }

    /**
     * 设置签名域
     *
     * @param fieldName
     * @param batchSignReqDTO
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/12/04 上午 9:59
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/04 上午 9:59
     * @Version: 0.0.1
     */
    private byte[] setFormField(String fieldName, BatchSignReqVO batchSignReqDTO, float sealWidth, float sealHeight) throws ServiceException, IOException, DocumentException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            //处理签章合同文件
            InputStream signOriginalFileStream = baseToInputStream(batchSignReqDTO.getSignOriginalFileStr());
            PdfReader pdfReader = new PdfReader(signOriginalFileStream);
            int actualPage = pdfReader.getNumberOfPages();
            float pdfHeight = pdfReader.getPageSize(1).getHeight();
            float pdfWidth = pdfReader.getPageSize(1).getWidth();
            PdfStamper pdfStamper = new PdfStamper(pdfReader, out, PDF_VERSION, true);
            PdfFormField pdfFormField = PdfFormField.createSignature(pdfStamper.getWriter());
            //最大X坐标和最大Y坐标
            CoordinateParam coordinateParam = handleCoordinate(batchSignReqDTO.getCoordinateX(), batchSignReqDTO.getCoordinateY(), pdfHeight, pdfWidth, sealWidth, sealHeight);
            pdfFormField.setWidget(new Rectangle(coordinateParam.getLlx(), coordinateParam.getLly(), coordinateParam.getUrx(), coordinateParam.getUry()), null);
            pdfFormField.setFlags(PdfAnnotation.FLAGS_PRINT);
            pdfFormField.setFieldName(fieldName);
            int startPageNumber = batchSignReqDTO.getStartPageNumber();
            if (1 > startPageNumber) {
                startPageNumber = 1;
            }
            if (startPageNumber > actualPage) {
                throw new ServiceException(ErrorCode.REQUEST_40101);
            }
            int endPageNumber = batchSignReqDTO.getEndPageNumber();
            if (endPageNumber > actualPage) {
                endPageNumber = actualPage;
            }
            for (int i = startPageNumber; i <= endPageNumber; i++) {
                pdfFormField.setPage(i);
                pdfStamper.addAnnotation(pdfFormField, i);
            }
            pdfStamper.close();
            pdfReader.close();
            return out.toByteArray();
        }
    }

    private List<Object> setFormField(PdfReader pdfReader, CoordinateParam coordinateParam,
                                      int startPageNumber, int endPageNumber) {
        List<Object> results = new ArrayList<>(INITIAL_CAPACITY2);
        List<String> fieldNames = new ArrayList<>(INITIAL_CAPACITY);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            //处理签章合同文件
            PdfStamper pdfStamper = new PdfStamper(pdfReader, out, PDF_VERSION, true);
            String fieldName = getFieldName();
            for (int i = startPageNumber; i <= endPageNumber; i++) {
                fieldNames.add(fieldName + i);
                PdfFormField pdfFormField = PdfFormField.createSignature(pdfStamper.getWriter());
                pdfFormField.setWidget(new Rectangle(coordinateParam.getLlx(), coordinateParam.getLly(),
                        coordinateParam.getUrx(), coordinateParam.getUry()), null);
                pdfFormField.setFlags(PdfAnnotation.FLAGS_PRINT);
                pdfFormField.setFieldName(fieldName + i);
                pdfFormField.setPage(i);
                pdfStamper.addAnnotation(pdfFormField, i);
            }
            results.add(fieldNames);
            pdfStamper.close();
            results.add(out.toByteArray());
        } catch (Exception e) {
            log.error("多页签章设置签署域异常：{}", e.getMessage());
            throw new BusinessException(SIGNATURE_ERROR, e.getLocalizedMessage());
        }
        return results;
    }

    private List<Object> setHalfFormField(PdfReader pdfReader,int pageNumber, CoordinateParam coordinateParam) {
        List<Object> results = new ArrayList<>(INITIAL_CAPACITY2);
        List<String> fieldNames = new ArrayList<>(INITIAL_CAPACITY);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            //处理签章合同文件
            PdfStamper pdfStamper = new PdfStamper(pdfReader, out, PDF_VERSION, true);
            String fieldName = getFieldName();
            fieldNames.add(fieldName + pageNumber);
            PdfFormField pdfFormField = PdfFormField.createSignature(pdfStamper.getWriter());
            pdfFormField.setWidget(new Rectangle(coordinateParam.getLlx(), coordinateParam.getLly(),
                    coordinateParam.getUrx(), coordinateParam.getUry()), null);
            pdfFormField.setFlags(PdfAnnotation.FLAGS_PRINT);
            pdfFormField.setFieldName(fieldName + pageNumber);
            pdfFormField.setPage(pageNumber);
            pdfStamper.addAnnotation(pdfFormField, pageNumber);
            results.add(fieldNames);
            pdfStamper.close();
            results.add(out.toByteArray());
        } catch (Exception e) {
            log.error("多页签章设置签署域异常：{}", e.getMessage());
            throw new BusinessException(SIGNATURE_ERROR, e.getLocalizedMessage());
        }
        return results;
    }

    @Override
    public EsealResult<SignRespDTO> setPerforationSign(PerforationSignReqVO perforationSignReqVO) throws IOException {
        try (ByteArrayOutputStream output = getByteArrayOutputStream(perforationSignReqVO)) {
            long time = System.currentTimeMillis();
            log.info("setPerforationSign 骑缝签 start");
            SignRespDTO signRespDTO = new SignRespDTO();
            //签名后Base64
            byte[] bytes = output.toByteArray();
            String signCompleteStr = Base64.encodeBase64String(bytes);
            signRespDTO.setSignCompleteFileStr(signCompleteStr);
            signRespDTO.setMultiParam(perforationSignReqVO.getSignOriginalFileStr());
            //返回响应
            EsealResult<SignRespDTO> result = EsealResult.esealResult();
            result.setData(signRespDTO);
            log.info("setPerforationSign 骑缝签 end");
            log.info(SIGN_IN_TIME + (System.currentTimeMillis() - time));
            return result;
        }
    }
    
    public byte[] perforationSign(PerforationSignReqVO perforationSignReqVO) throws IOException {
        try (ByteArrayOutputStream output = getByteArrayOutputStream(perforationSignReqVO)) {
            return output.toByteArray();
        }
    }

    @Override
    public EsealResult<SignRespDTO> setKeyWordSign(SameKeyWordSignReqVO sameKeyWordSignReqVO) throws IOException {
        try (ByteArrayOutputStream output = getKeyWordByteArrayOutputStream(sameKeyWordSignReqVO)) {
            long time = System.currentTimeMillis();
            log.info("setKeyWordSign 骑缝签改造关键字单页签章 start");
            SignRespDTO signRespDTO = new SignRespDTO();
            //签名后Base64
            byte[] bytes = output.toByteArray();
            String signCompleteStr = Base64.encodeBase64String(bytes);
            signRespDTO.setSignCompleteFileStr(signCompleteStr);
            signRespDTO.setMultiParam(sameKeyWordSignReqVO.getSignOriginalFileStr());
            //返回响应
            EsealResult<SignRespDTO> result = EsealResult.esealResult();
            result.setData(signRespDTO);
            log.info("setKeyWordSign 骑缝签改造关键字单页签章 end");
            log.info(SIGN_IN_TIME + (System.currentTimeMillis() - time));
            return result;
        }
    }

    @Override
    public EsealResult<SignRespDTO> setPagePerforationSign(PerforationSignReqVO perforationSignReqVO) throws IOException {
        try (ByteArrayOutputStream output = getPageByteArrayOutputStream(perforationSignReqVO)) {
            long time = System.currentTimeMillis();
            log.info("setPerforationSign 骑缝签 start");
            SignRespDTO signRespDTO = new SignRespDTO();
            //签名后Base64
            byte[] bytes = output.toByteArray();
            String signCompleteStr = Base64.encodeBase64String(bytes);
            signRespDTO.setSignCompleteFileStr(signCompleteStr);
            signRespDTO.setMultiParam(perforationSignReqVO.getSignOriginalFileStr());
            //返回响应
            EsealResult<SignRespDTO> result = EsealResult.esealResult();
            result.setData(signRespDTO);
            log.info("setPerforationSign 骑缝签 end");
            log.info(SIGN_IN_TIME + (System.currentTimeMillis() - time));
            return result;
        }
    }

    private ByteArrayOutputStream getPageByteArrayOutputStream(PerforationSignReqVO perforationSignReqVO) {
        //获取证书信息
        String certPassword = perforationSignReqVO.getCertPassword();
        String pfxStr = perforationSignReqVO.getCertStr();
        PrivateKey pk = getPrivateKey(certPassword, pfxStr);
        Certificate[] chain = getCertificateChain(certPassword, pfxStr);
        InputStream signOriginalFileStream = baseToInputStream(perforationSignReqVO.getSignOriginalFileStr());
        PdfReader pdfReaderByBytes = null;
        PdfReader pdfReaderByStream = null;
        PdfStamper stamper = null;
        try(ByteArrayOutputStream output = new ByteArrayOutputStream();){
            //处理签章合同文件
            pdfReaderByStream = new PdfReader(signOriginalFileStream);
            //定义相关初始化参数
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            int fileActualPage = pdfReaderByStream.getNumberOfPages();
            int startPageNumber = perforationSignReqVO.getStartPageNumber();
            if (1 > startPageNumber) {
                startPageNumber = 1;
            }
            if (startPageNumber > fileActualPage) {
                throw new ServiceException(ErrorCode.REQUEST_40101);
            }
            int endPageNumber = perforationSignReqVO.getEndPageNumber();
            if (endPageNumber > fileActualPage) {
                endPageNumber = fileActualPage;
            }
            int pageSpan = endPageNumber-startPageNumber+1;
            //签署循环次数
            int signCount=1;
            if(StringUtils.isNotBlank(perforationSignReqVO.getPageSize())) {
                int pageSize=Integer.parseInt(perforationSignReqVO.getPageSize());
                if(pageSize>0){
                    signCount=endPageNumber/pageSize+(endPageNumber%pageSize>0?1:0);
                }
                if(pageSpan==pageSize){
                    signCount=1;
                }
            }

            long endSealStrTime = System.currentTimeMillis();
            //图片切割
            byte[] b = Base64.decodeBase64(perforationSignReqVO.getSealStr());
            log.info(IMAGE_TIME + (System.currentTimeMillis() - endSealStrTime));
            //获取图片相关信息值
            ImageInfo imageInfo = Sanselan.getImageInfo(b);
            float sealWidth = imageInfo.getWidth() * IMAGE_DPI / DPI;
            float sealHeight = imageInfo.getHeight() * IMAGE_DPI / DPI;

            for(int k=0;k<signCount;k++) {
                int nspn=startPageNumber;
                int nepn=endPageNumber;
                int pageSizes=0;
                if(signCount>1){
                    pageSizes=Integer.parseInt(perforationSignReqVO.getPageSize());
                    pageSpan=pageSizes;
                    if(k==0){
                        nepn=Integer.parseInt(perforationSignReqVO.getPageSize());
                    }else if(k+1==signCount&&(endPageNumber%pageSizes<pageSizes)){
                        nspn=k*Integer.parseInt(perforationSignReqVO.getPageSize())+1;
                        pageSpan=endPageNumber-pageSizes*(signCount-1);
                    }else{
                        nspn=k*Integer.parseInt(perforationSignReqVO.getPageSize())+1;
                        nepn=(k+1)*Integer.parseInt(perforationSignReqVO.getPageSize());
                    }
                }

                Image[] nImage = subImages(b, pageSpan);
                output.reset();
                //设置签署位置
                Rectangle pageSize = pdfReaderByStream.getPageSize(1);
                CoordinateParam coordinateParam = perforationSign(perforationSignReqVO, pageSize, sealWidth, sealHeight,
                        nImage.length);
                // 定义时间戳
                TSAClient tsaClient = new TSAClientBouncyCastle(TIMESTAMP_URL);
                //摘要算法
                ExternalDigest digest = new BouncyCastleDigest();
                //签名算法
                ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, provider.getName());
                long endSignatureTime = System.currentTimeMillis();
                //设置签名域
                List<Object> results = setFormField(pdfReaderByStream, coordinateParam, nspn, nepn);
                byte[] buffer = (byte[]) results.get(1);
                List<String> fieldNames = (List<String>) results.get(0);
                //PDF签章文档处理
                pdfReaderByBytes = new PdfReader(buffer);
                stamper = PdfStamper.createSignature(pdfReaderByBytes, output, PDF_VERSION, null, true);

                PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
                appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.MULTIPLE_GRAPHIC);
                appearance.setVisibleSignature(fieldNames);
                Rectangle rect = new Rectangle(coordinateParam.getLlx(), coordinateParam.getLly(),
                        coordinateParam.getUrx(), coordinateParam.getUry());
                final float margin = 0;
                Rectangle signatureRect = new Rectangle(margin, margin, rect.getWidth() - margin,
                        rect.getHeight() - margin);
                List<PdfTemplate> imageList = appearance.getSignatureGraphices();
                if (null == imageList) {
                    imageList = Lists.newArrayList();
                }
                for (int i = 0; i < pageSpan; i++) {
                    nImage[i].setAbsolutePosition(coordinateParam.getLlx(), coordinateParam.getLly());
                    nImage[i].scaleToFit(signatureRect.getWidth(), signatureRect.getHeight());
                    PdfTemplate t = PdfTemplate.createTemplate(stamper.getWriter(), signatureRect.getWidth(),
                            signatureRect.getHeight());
                    ColumnText ct2 = new ColumnText(t);
                    ct2.setRunDirection(appearance.getRunDirection());
                    ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(),
                            signatureRect.getTop(), 0, Element.ALIGN_RIGHT);

                    Paragraph p = new Paragraph(signatureRect.getHeight());
                    // must calculate the point to draw from to make image appear in middle of column
                    float x = (signatureRect.getWidth() - nImage[i].getScaledWidth()) / HALF_WIDTH;
                    float y = (signatureRect.getHeight() - nImage[i].getScaledHeight()) / HALF_WIDTH;
                    p.add(new Chunk(nImage[i], x, y, false));
                    ct2.addElement(p);
                    ct2.go();
                    int signatureDirection = Integer.parseInt(perforationSignReqVO.getSignatureDirection());
                    if(0==signatureDirection){
                        //如果是左骑缝，截取的章模反向取
                        imageList.add(0,t);
                    }else {
                        imageList.add(t);
                    }
                }
                appearance.setSignatureGraphices(imageList);
                //签章核心
                signCode(appearance, digest, signature, chain, tsaClient, perforationSignReqVO.getSealCode());
                log.info(PERFORATION_CODE_TIME + (System.currentTimeMillis() - endSignatureTime));
                MultiParam multiParam = new MultiParam();
                float pdfWidth = pdfReaderByStream.getPageSize(1).getWidth();
                multiParam.setLlx(getx(perforationSignReqVO.getSignatureDirection(), sealWidth / pageSpan, pdfWidth));
                multiParam.setLly(perforationSignReqVO.getCoordinateY());
                multiParam.setUrx(sealWidth / pageSpan);
                multiParam.setUry(sealHeight);
                multiParam.setPdfWidth(pdfWidth);
                multiParam.setFoggy(perforationSignReqVO.isFoggy());
                multiParam.setGrey(perforationSignReqVO.isGrey());
                multiParam.setPageNum(perforationSignReqVO.getStartPageNumber());
                multiParam.setPageNumEnd(perforationSignReqVO.getEndPageNumber());
                perforationSignReqVO.setSignOriginalFileStr(JSONObject.toJSONString(multiParam));
                pdfReaderByStream=new PdfReader(output.toByteArray());
            }
            return output;
        } catch (Exception e) {
            log.error("骑缝签章异常：{}", e.getLocalizedMessage());
            throw new BusinessException(SIGNATURE_ERROR, e.getLocalizedMessage());
        } finally {
            try {
                if (null != stamper) {
                    stamper.close();
                }
                if (null != pdfReaderByStream) {
                    pdfReaderByStream.close();
                }
                if (null != pdfReaderByBytes) {
                    pdfReaderByBytes.close();
                }
                if (null != signOriginalFileStream) {
                    signOriginalFileStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public EsealResult<SignRespDTO> setPerforationHalfSign(PerforationHalfSignReqVO perforationHalfSignReqVO) throws IOException {
        SignRespDTO signRespDTO = new SignRespDTO();
        log.info("setPerforationHalfSign 骑缝连页签 start");
        long time = System.currentTimeMillis();
        int startPageNo=perforationHalfSignReqVO.getStartPageNumber();
        int endPageNo=perforationHalfSignReqVO.getEndPageNumber();
        int pageNum=endPageNo-startPageNo;
        if(pageNum<=0){
            throw new BusinessException(ErrorCode.REQUEST_40101);
        }
        //同一个完整章 循环盖章次数 一个章分2等份
        int num=(pageNum+1)/2+(pageNum+1)%2;
        //保存首页盖章起始轴线
        String signatureDirection=perforationHalfSignReqVO.getSignatureDirection();
        String multi="";
        for (int i=0;i<num;i++) {
            if(!perforationHalfSignReqVO.getSignatureDirection().equals(signatureDirection)){
                perforationHalfSignReqVO.setSignatureDirection(signatureDirection);
            }
            if(i>0) {
                perforationHalfSignReqVO.setStartPageNumber(perforationHalfSignReqVO.getStartPageNumber() + 2);
                perforationHalfSignReqVO.setEndPageNumber(perforationHalfSignReqVO.getStartPageNumber() + 1);
            }
            //一个完整章只盖一页的情况
            if(i+1!=1&&i+1==num&&(pageNum+1)%2>0){
                perforationHalfSignReqVO.setEndPageNumber(perforationHalfSignReqVO.getStartPageNumber());
            }
            //循环次数
            try (ByteArrayOutputStream output = getHalfByteArrayOutputStream(perforationHalfSignReqVO)) {
                //签名后Base64
                byte[] bytes = output.toByteArray();
                String signCompleteStr = Base64.encodeBase64String(bytes);
                signRespDTO.setSignCompleteFileStr(signCompleteStr);
                if(i==0){
                    multi=perforationHalfSignReqVO.getSignOriginalFileStr();
                }
                if(i>0){
                    multi=multi + "XXX" + perforationHalfSignReqVO.getSignOriginalFileStr();
                }
                log.info("setPerforationHalfSign multi:{}",multi);
                if(num!=(i+1)) {
                    perforationHalfSignReqVO.setSignOriginalFileStr(signCompleteStr);
                }
            }
        }
        log.info("setPerforationHalfSign multi.size:{}",multi.split("XXX").length);
        log.info("setPerforationHalfSign multi all:{}",multi);
        signRespDTO.setMultiParam(multi);
        //返回响应
        EsealResult<SignRespDTO> result = EsealResult.esealResult();
        result.setData(signRespDTO);
        log.info("setPerforationHalfSign 骑缝连页签 end");
        log.info(SIGN_IN_TIME + (System.currentTimeMillis() - time));
        return result;
    }

    private ByteArrayOutputStream getByteArrayOutputStream(PerforationSignReqVO perforationSignReqVO) {
        //获取证书信息
        String certPassword = perforationSignReqVO.getCertPassword();
        String pfxStr = perforationSignReqVO.getCertStr();
        PrivateKey pk = getPrivateKey(certPassword, pfxStr);
        Certificate[] chain = getCertificateChain(certPassword, pfxStr);
        InputStream signOriginalFileStream = baseToInputStream(perforationSignReqVO.getSignOriginalFileStr());
        PdfReader pdfReaderByBytes = null;
        PdfReader pdfReaderByStream = null;
        PdfStamper stamper = null;
        try(ByteArrayOutputStream output = new ByteArrayOutputStream();){
            //处理签章合同文件
            pdfReaderByStream = new PdfReader(signOriginalFileStream);
            //定义相关初始化参数
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            int fileActualPage = pdfReaderByStream.getNumberOfPages();
            int startPageNumber = perforationSignReqVO.getStartPageNumber();
            if (1 > startPageNumber) {
                startPageNumber = 1;
            }
            if (startPageNumber > fileActualPage) {
                throw new ServiceException(ErrorCode.REQUEST_40101);
            }
            int endPageNumber = perforationSignReqVO.getEndPageNumber();
            if (endPageNumber > fileActualPage) {
                endPageNumber = fileActualPage;
            }
            int pageSpan = endPageNumber - startPageNumber + 1;
            long endSealStrTime = System.currentTimeMillis();
            //图片切割
            byte[] b = Base64.decodeBase64(perforationSignReqVO.getSealStr());
            log.info(IMAGE_TIME + (System.currentTimeMillis() - endSealStrTime));
            Image[] nImage = subImages(b, pageSpan);
            //获取图片相关信息值
            ImageInfo imageInfo = Sanselan.getImageInfo(b);
            float sealWidth = imageInfo.getWidth() * IMAGE_DPI / DPI;
            float sealHeight = imageInfo.getHeight() * IMAGE_DPI / DPI;
            //设置签署位置
            Rectangle pageSize = pdfReaderByStream.getPageSize(1);
            CoordinateParam coordinateParam = perforationSign(perforationSignReqVO, pageSize, sealWidth, sealHeight,
                    nImage.length);
            // 定义时间戳
            TSAClient tsaClient = new TSAClientBouncyCastle(TIMESTAMP_URL);
            //摘要算法
            ExternalDigest digest = new BouncyCastleDigest();
            //签名算法
            ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, provider.getName());
            long endSignatureTime = System.currentTimeMillis();
            //设置签名域
            List<Object> results = setFormField(pdfReaderByStream, coordinateParam, startPageNumber, endPageNumber);
            byte[] buffer = (byte[]) results.get(1);
            List<String> fieldNames = (List<String>) results.get(0);
            //PDF签章文档处理
            pdfReaderByBytes = new PdfReader(buffer);
            stamper = PdfStamper.createSignature(pdfReaderByBytes, output, PDF_VERSION, null, true);

            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.MULTIPLE_GRAPHIC);
            appearance.setVisibleSignature(fieldNames);
            Rectangle rect = new Rectangle(coordinateParam.getLlx(), coordinateParam.getLly(),
                    coordinateParam.getUrx(), coordinateParam.getUry());
            final float margin = 0;
            Rectangle signatureRect = new Rectangle(margin, margin, rect.getWidth() - margin,
                    rect.getHeight() - margin);
            List<PdfTemplate> imageList = appearance.getSignatureGraphices();
            if (null == imageList) {
                imageList = Lists.newArrayList();
            }
            for (int i = 0; i < pageSpan; i++) {
                nImage[i].setAbsolutePosition(coordinateParam.getLlx(), coordinateParam.getLly());
                nImage[i].scaleToFit(signatureRect.getWidth(), signatureRect.getHeight());
                PdfTemplate t = PdfTemplate.createTemplate(stamper.getWriter(), signatureRect.getWidth(),
                        signatureRect.getHeight());
                ColumnText ct2 = new ColumnText(t);
                ct2.setRunDirection(appearance.getRunDirection());
                ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(),
                        signatureRect.getTop(), 0, Element.ALIGN_RIGHT);

                Paragraph p = new Paragraph(signatureRect.getHeight());
                // must calculate the point to draw from to make image appear in middle of column
                float x = (signatureRect.getWidth() - nImage[i].getScaledWidth()) / HALF_WIDTH;
                float y = (signatureRect.getHeight() - nImage[i].getScaledHeight()) / HALF_WIDTH;
                p.add(new Chunk(nImage[i], x, y, false));
                ct2.addElement(p);
                ct2.go();
                imageList.add(t);
            }
            appearance.setSignatureGraphices(imageList);
            //签章核心
            signCode(appearance, digest, signature, chain, tsaClient, perforationSignReqVO.getSealCode());
            log.info(PERFORATION_CODE_TIME + (System.currentTimeMillis() - endSignatureTime));
            MultiParam multiParam = new MultiParam();
            float pdfWidth = pdfReaderByStream.getPageSize(1).getWidth();
            multiParam.setLlx(getx(perforationSignReqVO.getSignatureDirection(), sealWidth / pageSpan, pdfWidth));
            multiParam.setLly(perforationSignReqVO.getCoordinateY());
            multiParam.setUrx(sealWidth / pageSpan);
            multiParam.setUry(sealHeight);
            multiParam.setPdfWidth(pdfWidth);
            multiParam.setFoggy(perforationSignReqVO.isFoggy());
            multiParam.setGrey(perforationSignReqVO.isGrey());
            multiParam.setPageNum(perforationSignReqVO.getStartPageNumber());
            multiParam.setPageNumEnd(perforationSignReqVO.getEndPageNumber());
            perforationSignReqVO.setSignOriginalFileStr(JSONObject.toJSONString(multiParam));
            return output;
        } catch (Exception e) {
            log.error("骑缝签章异常：{}", e.getLocalizedMessage());
            throw new BusinessException(SIGNATURE_ERROR, e.getLocalizedMessage());
        } finally {
            try {
                if (null != stamper) {
                    stamper.close();
                }
                if (null != pdfReaderByStream) {
                    pdfReaderByStream.close();
                }
                if (null != pdfReaderByBytes) {
                    pdfReaderByBytes.close();
                }
                if (null != signOriginalFileStream) {
                    signOriginalFileStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private ByteArrayOutputStream getKeyWordByteArrayOutputStream(SameKeyWordSignReqVO sameKeyWordSignReqVO) {
        //获取证书信息
        String certPassword = sameKeyWordSignReqVO.getCertPassword();
        String pfxStr = sameKeyWordSignReqVO.getCertStr();
        PrivateKey pk = getPrivateKey(certPassword, pfxStr);
        Certificate[] chain = getCertificateChain(certPassword, pfxStr);
        InputStream signOriginalFileStream = baseToInputStream(sameKeyWordSignReqVO.getSignOriginalFileStr());
        PdfReader pdfReaderByBytes = null;
        PdfReader pdfReaderByStream = null;
        PdfStamper stamper = null;
        try(ByteArrayOutputStream output = new ByteArrayOutputStream();){
            //处理签章合同文件
            pdfReaderByStream = new PdfReader(signOriginalFileStream);
            //定义相关初始化参数
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            int fileActualPage = pdfReaderByStream.getNumberOfPages();
            int startPageNumber = sameKeyWordSignReqVO.getStartPageNumber();
            if (1 > startPageNumber) {
                startPageNumber = 1;
            }
            if (startPageNumber > fileActualPage) {
                throw new ServiceException(ErrorCode.REQUEST_40101);
            }
            int endPageNumber = sameKeyWordSignReqVO.getEndPageNumber();
            if (endPageNumber > fileActualPage) {
                endPageNumber = fileActualPage;
            }
            int pageSpan = endPageNumber - startPageNumber + 1;
            long endSealStrTime = System.currentTimeMillis();
            //图片切割
            byte[] b = Base64.decodeBase64(sameKeyWordSignReqVO.getSealStr());
            log.info(IMAGE_TIME + (System.currentTimeMillis() - endSealStrTime));
            Image[] nImage = subImages(b, pageSpan);
            //获取图片相关信息值
            ImageInfo imageInfo = Sanselan.getImageInfo(b);
            float sealWidth = imageInfo.getWidth() * IMAGE_DPI / DPI;
            float sealHeight = imageInfo.getHeight() * IMAGE_DPI / DPI;
            //设置签署位置
            Rectangle pageSize = pdfReaderByStream.getPageSize(1);
            CoordinateParam coordinateParam = sameKeyWordSign(sameKeyWordSignReqVO, pageSize, sealWidth, sealHeight,
                    nImage.length);
            // 定义时间戳
            TSAClient tsaClient = new TSAClientBouncyCastle(TIMESTAMP_URL);
            //摘要算法
            ExternalDigest digest = new BouncyCastleDigest();
            //签名算法
            ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, provider.getName());
            long endSignatureTime = System.currentTimeMillis();
            //设置签名域
            List<Object> results = setFormField(pdfReaderByStream, coordinateParam, startPageNumber, endPageNumber);
            byte[] buffer = (byte[]) results.get(1);
            List<String> fieldNames = (List<String>) results.get(0);
            //PDF签章文档处理
            pdfReaderByBytes = new PdfReader(buffer);
            stamper = PdfStamper.createSignature(pdfReaderByBytes, output, PDF_VERSION, null, true);

            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.MULTIPLE_GRAPHIC);
            appearance.setVisibleSignature(fieldNames);
            Rectangle rect = new Rectangle(coordinateParam.getLlx(), coordinateParam.getLly(),
                    coordinateParam.getUrx(), coordinateParam.getUry());
            final float margin = 0;
            Rectangle signatureRect = new Rectangle(margin, margin, rect.getWidth() - margin,
                    rect.getHeight() - margin);
            List<PdfTemplate> imageList = appearance.getSignatureGraphices();
            if (null == imageList) {
                imageList = Lists.newArrayList();
            }
            for (int i = 0; i < pageSpan; i++) {
                nImage[i].setAbsolutePosition(coordinateParam.getLlx(), coordinateParam.getLly());
                nImage[i].scaleToFit(signatureRect.getWidth(), signatureRect.getHeight());
                PdfTemplate t = PdfTemplate.createTemplate(stamper.getWriter(), signatureRect.getWidth(),
                        signatureRect.getHeight());
                ColumnText ct2 = new ColumnText(t);
                ct2.setRunDirection(appearance.getRunDirection());
                ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(),
                        signatureRect.getTop(), 0, Element.ALIGN_RIGHT);

                Paragraph p = new Paragraph(signatureRect.getHeight());
                // must calculate the point to draw from to make image appear in middle of column
                float x = (signatureRect.getWidth() - nImage[i].getScaledWidth()) / HALF_WIDTH;
                float y = (signatureRect.getHeight() - nImage[i].getScaledHeight()) / HALF_WIDTH;
                p.add(new Chunk(nImage[i], x, y, false));
                ct2.addElement(p);
                ct2.go();
                imageList.add(t);
            }
            appearance.setSignatureGraphices(imageList);
            //签章核心
            signCode(appearance, digest, signature, chain, tsaClient, sameKeyWordSignReqVO.getSealCode());
            log.info(PERFORATION_CODE_TIME + (System.currentTimeMillis() - endSignatureTime));
            MultiParam multiParam = new MultiParam();
            float pdfWidth = pdfReaderByStream.getPageSize(1).getWidth();
            multiParam.setLlx(getx("0", sealWidth / pageSpan, pdfWidth));
            multiParam.setLly(sameKeyWordSignReqVO.getCoordinateY());
            multiParam.setUrx(sealWidth / pageSpan);
            multiParam.setUry(sealHeight);
            multiParam.setPdfWidth(pdfWidth);
            multiParam.setFoggy(sameKeyWordSignReqVO.isFoggy());
            multiParam.setGrey(sameKeyWordSignReqVO.isGrey());
            multiParam.setPageNum(sameKeyWordSignReqVO.getStartPageNumber());
            multiParam.setPageNumEnd(sameKeyWordSignReqVO.getEndPageNumber());
            sameKeyWordSignReqVO.setSignOriginalFileStr(JSONObject.toJSONString(multiParam));
            return output;
        } catch (Exception e) {
            log.error("关键字单页签章异常：{}", e.getLocalizedMessage());
            throw new BusinessException(SIGNATURE_ERROR, e.getLocalizedMessage());
        } finally {
            try {
                if (null != stamper) {
                    stamper.close();
                }
                if (null != pdfReaderByStream) {
                    pdfReaderByStream.close();
                }
                if (null != pdfReaderByBytes) {
                    pdfReaderByBytes.close();
                }
                if (null != signOriginalFileStream) {
                    signOriginalFileStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private ByteArrayOutputStream getHalfByteArrayOutputStream(PerforationHalfSignReqVO perforationHalfSignReqVO) {
        //获取证书信息
        String certPassword = perforationHalfSignReqVO.getCertPassword();
        String pfxStr = perforationHalfSignReqVO.getCertStr();
        PrivateKey pk = getPrivateKey(certPassword, pfxStr);
        Certificate[] chain = getCertificateChain(certPassword, pfxStr);
        PdfReader pdfReaderByBytes = null;
        PdfReader pdfReaderByStream = null;
        PdfStamper stamper = null;
        try(InputStream signOriginalFileStream = baseToInputStream(perforationHalfSignReqVO.getSignOriginalFileStr());
            ByteArrayOutputStream output = new ByteArrayOutputStream()){
            //处理签章合同文件
            pdfReaderByStream = new PdfReader(signOriginalFileStream);
            //定义相关初始化参数
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            int fileActualPage = pdfReaderByStream.getNumberOfPages();
            int startPageNumber = perforationHalfSignReqVO.getStartPageNumber();
            if (1 > startPageNumber) {
                startPageNumber = 1;
            }
            if (startPageNumber > fileActualPage) {
                throw new ServiceException(ErrorCode.REQUEST_40101);
            }
            int endPageNumber = perforationHalfSignReqVO.getEndPageNumber();
            if (endPageNumber > fileActualPage) {
                endPageNumber = fileActualPage;
            }
            if(startPageNumber+1<endPageNumber){
                endPageNumber=startPageNumber+1;
            }
            int pageSpan = endPageNumber - startPageNumber + 1;
            //判断是否为偶数
            if((endPageNumber - startPageNumber)!=1){
                pageSpan=1;
            }
            long endSealStrTime = System.currentTimeMillis();
            //图片切割
            byte[] b = Base64.decodeBase64(perforationHalfSignReqVO.getSealStr());
            log.info(IMAGE_TIME + (System.currentTimeMillis() - endSealStrTime));
            Image[] nImage = subImages(b, pageSpan);
            //获取图片相关信息值
            ImageInfo imageInfo = Sanselan.getImageInfo(b);
            float sealWidth = imageInfo.getWidth() * IMAGE_DPI / DPI;
            float sealHeight = imageInfo.getHeight() * IMAGE_DPI / DPI;
            float pdfWidth = pdfReaderByStream.getPageSize(1).getWidth();
            //设置签署位置
            String mulit="";
            for(int k=0;k<pageSpan;k++) {
                output.reset();
                if(k==1&& SignatureEnum.SIGNATURE_PERFORATION_LEFT.getCode().equals(perforationHalfSignReqVO.getSignatureDirection())){
                    perforationHalfSignReqVO.setSignatureDirection(SignatureEnum.SIGNATURE_PERFORATION_RIGHT.getCode());
                }
                if(k==1&&SignatureEnum.SIGNATURE_PERFORATION_RIGHT.getCode().equals(perforationHalfSignReqVO.getSignatureDirection())){
                    perforationHalfSignReqVO.setSignatureDirection(SignatureEnum.SIGNATURE_PERFORATION_LEFT.getCode());
                }
                Rectangle pageSize = pdfReaderByStream.getPageSize(startPageNumber+k);
                CoordinateParam coordinateParam = perforationHalfSign(perforationHalfSignReqVO, pageSize, sealWidth, sealHeight,
                        nImage.length);
                // 定义时间戳
                TSAClient tsaClient = new TSAClientBouncyCastle(TIMESTAMP_URL);
                //摘要算法
                ExternalDigest digest = new BouncyCastleDigest();
                //签名算法
                ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, provider.getName());
                long endSignatureTime = System.currentTimeMillis();
                //设置签名域
                List<Object> results = setHalfFormField(pdfReaderByStream,startPageNumber+k, coordinateParam);
                byte[] buffer = (byte[]) results.get(1);
                List<String> fieldNames = (List<String>) results.get(0);
                //PDF签章文档处理
                pdfReaderByBytes = new PdfReader(buffer);
                stamper = PdfStamper.createSignature(pdfReaderByBytes, output, PDF_VERSION, null, true);

                PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
                appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.MULTIPLE_GRAPHIC);
                appearance.setVisibleSignature(fieldNames);
                Rectangle rect = new Rectangle(coordinateParam.getLlx(), coordinateParam.getLly(),
                        coordinateParam.getUrx(), coordinateParam.getUry());
                final float margin = 0;
                Rectangle signatureRect = new Rectangle(margin, margin, rect.getWidth() - margin,
                        rect.getHeight() - margin);
                List<PdfTemplate> imageList = appearance.getSignatureGraphices();
                if (null == imageList) {
                    imageList = Lists.newArrayList();
                }

                nImage[k].setAbsolutePosition(coordinateParam.getLlx(), coordinateParam.getLly());
                nImage[k].scaleToFit(signatureRect.getWidth(), signatureRect.getHeight());
                PdfTemplate t = PdfTemplate.createTemplate(stamper.getWriter(), signatureRect.getWidth(),
                        signatureRect.getHeight());
                ColumnText ct2 = new ColumnText(t);
                ct2.setRunDirection(appearance.getRunDirection());
                ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(),
                        signatureRect.getTop(), 0, Element.ALIGN_RIGHT);

                Paragraph p = new Paragraph(signatureRect.getHeight());
                // must calculate the point to draw from to make image appear in middle of column
                float x = (signatureRect.getWidth() - nImage[k].getScaledWidth()) / HALF_WIDTH;
                float y = (signatureRect.getHeight() - nImage[k].getScaledHeight()) / HALF_WIDTH;
                p.add(new Chunk(nImage[k], x, y, false));
                ct2.addElement(p);
                ct2.go();
                imageList.add(t);

                appearance.setSignatureGraphices(imageList);
                //签章核心
                signCode(appearance, digest, signature, chain, tsaClient, perforationHalfSignReqVO.getSealCode());
                log.info(PERFORATION_CODE_TIME + (System.currentTimeMillis() - endSignatureTime));
                MultiParam multiParam = new MultiParam();
                multiParam.setLlx(getx(perforationHalfSignReqVO.getSignatureDirection(), sealWidth / pageSpan, pdfWidth));
                multiParam.setLly(perforationHalfSignReqVO.getCoordinateY());
                multiParam.setUrx(sealWidth / pageSpan);
                multiParam.setUry(sealHeight);
                multiParam.setPdfWidth(pdfWidth);
                multiParam.setFoggy(perforationHalfSignReqVO.isFoggy());
                multiParam.setGrey(perforationHalfSignReqVO.isGrey());
                multiParam.setPageNum(perforationHalfSignReqVO.getStartPageNumber()+k);
                multiParam.setPageNumEnd(perforationHalfSignReqVO.getStartPageNumber()+k);
                if(k==0){
                    mulit=JSONObject.toJSONString(multiParam);
                }
                if(k>0){
                    mulit=mulit+"XXX"+JSONObject.toJSONString(multiParam);
                }
                pdfReaderByStream=new PdfReader(output.toByteArray());
            }
            perforationHalfSignReqVO.setSignOriginalFileStr(mulit);
            log.info("getHalfByteArrayOutputStream mulit: {}",mulit);
            return output;
        } catch (Exception e) {
            log.error("骑缝签章异常：{}", e.getLocalizedMessage());
            throw new BusinessException(SIGNATURE_ERROR, e.getLocalizedMessage());
        } finally {
            try {
                if (null != stamper) {
                    stamper.close();
                }
                if (null != pdfReaderByStream) {
                    pdfReaderByStream.close();
                }
                if (null != pdfReaderByBytes) {
                    pdfReaderByBytes.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private float getx(String signatureDirection, float sealWidth, float pdfWidth) {
        String left = "0";
        if(left.equals(signatureDirection)){
            return 0;
        }
        String right = "1";
        if(right.equals(signatureDirection)){
            return pdfWidth - sealWidth / HALF_WIDTH;
        }
        return 0;
    }

    /**
     * 骑缝签章坐标
     *
     * @param perforationSignReqVO
     * @param pageSize
     * @param sealWidth
     * @param sealHeight
     * @param sealSize
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2019/01/02 下午 12:03
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2019/01/02 下午 12:03
     * @Version: 0.0.1
     */
    private CoordinateParam perforationSign(PerforationSignReqVO perforationSignReqVO, Rectangle pageSize,
                                            float sealWidth, float sealHeight, int sealSize) {
        float lx;
        float rx;
        float widthPdf = pageSize.getWidth();
        float rx1 = sealWidth / sealSize;
        // 左侧
        int signatureDirection = Integer.parseInt(perforationSignReqVO.getSignatureDirection());
        if (signatureDirection == 0) {
            lx = 0;
            rx = rx1;
        } else {
            lx = widthPdf - rx1;
            rx = widthPdf;
        }
        float coordinatey = perforationSignReqVO.getCoordinateY()- (sealHeight/HALF_WIDTH);
        float heightPdf = pageSize.getHeight();
        if (coordinatey > heightPdf) {
            coordinatey = heightPdf;
        } else if (coordinatey < 0) {
            coordinatey = 0;
        }
        float ly = coordinatey;
        float ry = ly + sealHeight;
        //封装参数
        CoordinateParam coordinateParam = new CoordinateParam();
        coordinateParam.setUrx(rx);
        coordinateParam.setUry(ry);
        coordinateParam.setLlx(lx);
        coordinateParam.setLly(ly);
        return coordinateParam;
    }

    /**
     * 骑缝签章坐标
     *
     * @param sameKeyWordSignReqVO
     * @param pageSize
     * @param sealWidth
     * @param sealHeight
     * @param sealSize
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2019/01/02 下午 12:03
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2019/01/02 下午 12:03
     * @Version: 0.0.1
     */
    private CoordinateParam sameKeyWordSign(SameKeyWordSignReqVO sameKeyWordSignReqVO, Rectangle pageSize,
                                            float sealWidth, float sealHeight, int sealSize) {
        float lx;
        float rx;
        float widthPdf = pageSize.getWidth();
        float rx1 = sealWidth / sealSize;
        lx = 0;
        rx = rx1;
        float coordinatey = sameKeyWordSignReqVO.getCoordinateY()- (sealHeight/HALF_WIDTH);
        float heightPdf = pageSize.getHeight();
        if (coordinatey > heightPdf) {
            coordinatey = heightPdf;
        } else if (coordinatey < 0) {
            coordinatey = 0;
        }
        float ly = coordinatey;
        float ry = ly + sealHeight;
        //封装参数
        CoordinateParam coordinateParam = new CoordinateParam();
        coordinateParam.setUrx(rx);
        coordinateParam.setUry(ry);
        coordinateParam.setLlx(lx);
        coordinateParam.setLly(ly);
        return coordinateParam;
    }

    /**
     * 骑缝签章坐标
     *
     * @param perforationHalfSignReqVO
     * @param pageSize
     * @param sealWidth
     * @param sealHeight
     * @param sealSize
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2019/01/02 下午 12:03
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2019/01/02 下午 12:03
     * @Version: 0.0.1
     */
    private CoordinateParam perforationHalfSign(PerforationHalfSignReqVO perforationHalfSignReqVO, Rectangle pageSize,
                                            float sealWidth, float sealHeight, int sealSize) {
        float lx;
        float rx;
        float widthPdf = pageSize.getWidth();
        float rx1 = sealWidth / sealSize;
        // 左侧
        int signatureDirection = Integer.parseInt(perforationHalfSignReqVO.getSignatureDirection());
        if (signatureDirection == 0) {
            lx = 0;
            rx = rx1;
        } else {
            lx = widthPdf - rx1;
            rx = widthPdf;
        }
        float coordinatey = perforationHalfSignReqVO.getCoordinateY()- (sealHeight/HALF_WIDTH);
        float heightPdf = pageSize.getHeight();
        if (coordinatey > heightPdf) {
            coordinatey = heightPdf;
        } else if (coordinatey < 0) {
            coordinatey = 0;
        }
        float ly = coordinatey;
        float ry = ly + sealHeight;
        //封装参数
        CoordinateParam coordinateParam = new CoordinateParam();
        coordinateParam.setUrx(rx);
        coordinateParam.setUry(ry);
        coordinateParam.setLlx(lx);
        coordinateParam.setLly(ly);
        return coordinateParam;
    }

    /**
     * 图片切割
     *
     * @param
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/12/11 上午 11:08
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/11 上午 11:08
     * @Version: 0.0.1
     */
    private Image[] subImages(byte[] image, int span) {
        Image[] nImage = new Image[span];
        try (ByteArrayInputStream input = new ByteArrayInputStream(image);
             ByteArrayOutputStream out = new ByteArrayOutputStream()){
            BufferedImage buffer = ImageIO.read(input);
            BufferedImage img = ConvertImageAlphaUtil.convertBufferedImage(buffer);
            int w = img.getWidth();
            int h = img.getHeight();
            int sw = w / span;
            for (int i = 0; i < span; i++) {
                BufferedImage subImg;
                if (i + 1 == span) {
                    //最后剩余部分
                    subImg = img.getSubimage(i * sw, 0, w - i * sw, h);
                } else {
                    //前n-1块均匀切
                    subImg = img.getSubimage(i * sw, 0, sw, h);
                }
                ImageIO.write(subImg, IMAGE_FORMAT, out);
                nImage[i] = Image.getInstance(out.toByteArray());
                out.flush();
                out.reset();
            }
        } catch (Exception e) {
            log.error("骑缝签章切割章模异常：{}", e.getMessage());
        }
        return nImage;
    }

    /**
     * 处理签章位置
     *
     * @param coordinatex
     * @param coordinatey
     * @param pdfHeight
     * @param pdfWidth
     * @param sealHeight
     * @param sealWidth
     * @return
     */
    private CoordinateParam handleCoordinate(float coordinatex, float coordinatey, float pdfHeight, float pdfWidth,
                                             float sealWidth, float sealHeight) {
        float maxx = pdfWidth - sealWidth / HALF_WIDTH;
        float minx = sealWidth / HALF_WIDTH;
        float maxy = pdfHeight - sealHeight / HALF_WIDTH;
        float miny = sealHeight / HALF_WIDTH;
        if (coordinatex > maxx) {
            coordinatex = maxx;
        } else if (coordinatex < minx) {
            coordinatex = minx;
        }
        if (coordinatey > maxy) {
            coordinatey = maxy;
        } else if (coordinatey < miny) {
            coordinatey = miny;
        }
        CoordinateParam coordinateParam = new CoordinateParam();
        coordinateParam.setLlx(coordinatex - minx);
        coordinateParam.setLly(coordinatey - miny);
        coordinateParam.setUrx(coordinatex + minx);
        coordinateParam.setUry(coordinatey + miny);
        return coordinateParam;
    }

    /**
     * 系统定义签名域件名
     *
     * @Author: 唐德繁
     * @CreateDate: 2018/11/28 下午 2:48
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/11/28 下午 2:48
     * @Version: 0.0.1
     */
    public String getFieldName() {
        Date date = new Date(System.currentTimeMillis());
        String datetimeFormat = "yyyyMMddHHmmssSSS";
        DateFormat df = new SimpleDateFormat(datetimeFormat);
        return "IYINES_Signature_" + df.format(date);
    }

    /**
     * 获取证书链
     *
     * @param password
     * @param pfx
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/10/23 下午 7:12
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/10/23 下午 7:12
     * @Version: 0.0.1
     */
    private Certificate[] getCertificateChain(String password, String pfx) {
        Certificate[] chain = null;
        try {
            KeyStore ks = KeyStore.getInstance("pkcs12");
            InputStream certInputStream = baseToInputStream(pfx);
            ks.load(certInputStream, password.toCharArray());
            String alias = ks.aliases().nextElement();
            chain = ks.getCertificateChain(alias);
        } catch (Exception e) {
            log.error("获取证书链异常：{}", e.getMessage());
        }
        if (null == chain) {
            throw new BusinessException(ErrorCode.CERTIFICATION_PASSWORD_ERROR);
        }
        return chain;
    }

    /**
     * 获取私钥
     *
     * @param certPassword
     * @param pfx
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/10/23 下午 7:07
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/10/23 下午 7:07
     * @Version: 0.0.1
     */
    private PrivateKey getPrivateKey(String certPassword, String pfx) {
        PrivateKey pk = null;
        try {
            KeyStore ks = KeyStore.getInstance("pkcs12");
            InputStream certInputStream = baseToInputStream(pfx);
            ks.load(certInputStream, certPassword.toCharArray());
            String alias = ks.aliases().nextElement();
            pk = (PrivateKey) ks.getKey(alias, certPassword.toCharArray());
        } catch (Exception e) {
            log.error("获取证书私钥异常：{}", e.getMessage());
        }
        if (null == pk) {
            throw new BusinessException(ErrorCode.CERTIFICATION_PASSWORD_ERROR);
        }
        return pk;
    }

    /**
     * Base64字符串转InputStream流对象
     *
     * @param base64string
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/10/23 上午 11:14
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/10/23 上午 11:14
     * @Version: 0.0.1
     */
    public static InputStream baseToInputStream(String base64string) {
        long time = System.currentTimeMillis();
        ByteArrayInputStream stream = null;
        try {
            byte[] bytes = Base64.decodeBase64(base64string);
            stream = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            log.error("Base64字符串转InputStream流对象异常：{}", e.getMessage());
        }
        log.info("baseToInputStreamTime:" + (System.currentTimeMillis() - time));
        return stream;
    }

}
