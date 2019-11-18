package com.iyin.sign.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.fastdfs.FastdfsClient;
import com.iyin.sign.system.common.interfaces.CacheConstants;
import com.iyin.sign.system.common.utils.Office2PdfUtil;
import com.iyin.sign.system.common.utils.PdfUtils;
import com.iyin.sign.system.entity.*;
import com.iyin.sign.system.mapper.FileResourceMapper;
import com.iyin.sign.system.mapper.ISignSysCompactFileMapper;
import com.iyin.sign.system.mapper.ISysSignatureLogMapper;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.sign.ImgInfoDTO;
import com.iyin.sign.system.model.sign.ImgInfoListDTO;
import com.iyin.sign.system.model.sign.ImgInfoVo;
import com.iyin.sign.system.service.FileResourceService;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.service.ISignSysSignConfigService;
import com.iyin.sign.system.service.feign.AddWatermarkService;
import com.iyin.sign.system.util.*;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO;
import com.iyin.sign.system.vo.req.AddGratingReqVO;
import com.iyin.sign.system.vo.req.AddWatermarkPdfReqVO;
import com.iyin.sign.system.vo.req.DocPackageReqDto;
import com.iyin.sign.system.vo.req.FileResourceInfoVO;
import com.iyin.sign.system.vo.resp.AddWatermarkRespVO;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName: FileServiceImpl
 * @Description: 文件处理
 * @Author: yml
 * @CreateDate: 2019/6/20
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/20
 * @Version: 1.0.0
 */
@Slf4j
@NoArgsConstructor
@Configuration
public class FileServiceImpl implements IFileService {

    private static final int INITIAL_CAPACITY = 16;
    private static final int IMGDPI = 72;
    private static final int DPI = 144;
    private static final String CHARSET_UTF8 = "UTF-8";
    public static final long MILLIS = 3000L;
    private static String fileContentType = "contentType";
    private static final String FILENAME = "fileName";

    private static final String FILETYPE_PNG = "png";
    private static final String SUFFIX_SEPERATOR = ".";
    private static final String PDFNAME = ".pdf";
    private static final String IMAGE = "image";
    private String pdfType = "application/pdf";
    private static final String FILESIZE = "fileSize";
    private static final String FILEEXT = "fileExt";
    private static final String FILETYPE = "application/octet-stream";

    private static final String API_TOKEN = "api_token";
    private static final String SESSION_TOKEN = "session_token";

    @Value("${file-path}")
    private String filePath;
    @Value("${fileTempPath}")
    private String fileTempPath;

    @Value("${fastdfs.fastdfs_head}")
    private String fastdfsHead;

    @Value("${fastdfs.fastdfs_ip_port}")
    private String fastdfsIpPort;

    @Autowired
    private FastdfsClient client;
    @Autowired
    private FileResourceService fileService;
    @Autowired
    private PdfUtil pdfUtil;
    @Resource
    private FileResourceMapper fileResourceMapper;
    @Autowired
    private Office2PdfUtil office2PdfUtil;
    @Autowired
    private ISignSysSignConfigService signSysSignConfigService;
    @Resource
    private ISysSignatureLogMapper signatureLogMapper;
    @Resource
    private ISignSysCompactFileMapper signSysCompactFileMapper;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private IAuthTokenService authTokenService;
    @Autowired
    private AddWatermarkService addWatermarkService;

    @Override
    public FileUploadRespDto upload(MultipartFile file, String userId) {
        try {
            log.info("file size:{}", file.getSize());
            String extension = FileUtil.getFileExtensionName(file.getOriginalFilename());
            String fileName = FileUtil.getFileName(file.getOriginalFilename());
            // 提取fdfs需要的参数
            Map<String, String> map = new HashMap<>(INITIAL_CAPACITY);
            map.put(FILENAME, fileName);
            map.put("name", file.getName());
            map.put(fileContentType, file.getContentType());

            map.put(FILESIZE, String.valueOf(file.getSize()));
            map.put(FILEEXT, extension);
            byte[] bytes = file.getBytes();
            // fdfs调用
            log.info("fastdfs upload begin: {}", fileName);
            String fileId = client.upload(bytes, fileName, map);
            log.info("fastdfs upload result: {}", fileId);

            FileResource resource = new FileResource();
            packData(file, userId, fileName, fileId, resource, 0L);
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(SUFFIX_SEPERATOR));
            if(PDFNAME.equalsIgnoreCase(fileType)){
                try (PDDocument pdfDocument = PDDocument
                        .load(file.getInputStream(), MemoryUsageSetting.setupTempFileOnly())) {
                    resource.setPageTotal(new Long(pdfDocument.getNumberOfPages()));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
                }
            }else{
                resource.setPageTotal(0L);
            }

            fileService.save(resource);
            FileResourceDto dto = new FileResourceDto();
            BeanUtils.copyProperties(resource, dto);
            FileUploadRespDto resp = new FileUploadRespDto();
            resp.setFile(dto);
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    @Override
    public FileUploadRespDto uploadSignInit(MultipartFile file, String userId) {
        try {
            log.info("uploadSignInit file size:{}", file.getSize());
            String extension = FileUtil.getFileExtensionName(file.getOriginalFilename());
            String fileName = FileUtil.getFileName(file.getOriginalFilename());
            // 提取fdfs需要的参数
            Map<String, String> map = new HashMap<>(INITIAL_CAPACITY);
            map.put(FILENAME, fileName);
            map.put("name", file.getName());
            map.put(fileContentType, file.getContentType());

            map.put(FILESIZE, String.valueOf(file.getSize()));
            map.put(FILEEXT, extension);
            byte[] bytes = file.getBytes();
            // fdfs调用
            log.info("fastdfs upload begin: {}",fileName);
            String fileId = client.upload(bytes, fileName, map);
            log.info("fastdfs uploadSignInit result: {}", fileId);

            FileResource resource = new FileResource();
            packData(file, userId, fileName, fileId, resource, 1L);
            SignSysSignConfig signSysSignConfig=signSysSignConfigService.getSignConfig();

            if(null!=signSysSignConfig){
                resource.setVerificationCode("0");
                if(signSysSignConfig.getVerificationCode()==1){
                    resource.setVerificationCode(MD5Util.md5HashCode32(file.getInputStream()));
                }
                resource.setQrCode(signSysSignConfig.getQrCode());
                resource.setGmtVerification("0");
                if(0!=signSysSignConfig.getGmtVerification()){
                    int num=signSysSignConfig.getGmtVerification();
                    resource.setGmtVerification(0==num?"0": LocalDate.now().plusDays((num-1)).toString());
                }
            }
            resource.setGmtModified(LocalDateTime.now());
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(SUFFIX_SEPERATOR));
            if(PDFNAME.equalsIgnoreCase(fileType)) {
                try (PDDocument pdfDocument = PDDocument
                        .load(file.getInputStream(), MemoryUsageSetting.setupTempFileOnly())) {
                    resource.setPageTotal(new Long(pdfDocument.getNumberOfPages()));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
                }
            }else{
                resource.setPageTotal(0L);
            }
            fileService.save(resource);
            FileResourceDto dto = new FileResourceDto();
            BeanUtils.copyProperties(resource, dto);
            FileUploadRespDto resp = new FileUploadRespDto();
            resp.setFile(dto);
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    private void packData(MultipartFile file, String userId, String fileName, String fileId, FileResource resource, long l) throws IOException {
        LocalDateTime date = LocalDateTime.now();
        resource.setId(String.valueOf(SnowflakeIdWorker.getId()));
        resource.setFileName(fileName);
        resource.setFilePath(fileId);
        resource.setUserId(userId);
        resource.setFileCode(UUID.randomUUID().toString());
        resource.setFileSize(file.getSize() + "");
        resource.setFileHash(MD5Util.md5HashCode32(file));
        resource.setFileType(file.getContentType());
        resource.setIsDeleted(0);
        resource.setPrintNum(0L);
        resource.setSignCount(l);
        resource.setDownCount(0L);
        resource.setGmtCreate(date);
    }

    @Override
    public FileUploadRespDto updateFile(MultipartFile file, String userId, String fileCode) {
        FileResource res = fileService.findByFileCode(fileCode);
        if (StringUtils.isEmpty(res) || (!fileCode.equals(res.getFileCode())) || 0 != res.getIsDeleted()) {
            throw new BusinessException(FileResponseCode.DATA_FILE_CODE_NULL);
        }
        String extension = FileUtil.getFileExtensionName(file.getOriginalFilename());
        String fileName = FileUtil.getFileName(file.getOriginalFilename());
        // 提取fdfs需要的参数
        Map<String, String> map = new HashMap<>(INITIAL_CAPACITY);
        map.put(FILENAME, fileName);
        map.put("name", file.getName());
        map.put(fileContentType, file.getContentType());

        map.put(FILESIZE, String.valueOf(file.getSize()));
        map.put(FILEEXT, extension);
        try {
            byte[] bytes = file.getBytes();
            // fdfs调用
            String fileId = client.upload(bytes, fileName, map);
            log.info("fastdfs updateFile result: {}", fileId);
            LocalDateTime date = LocalDateTime.now();
            FileResource resource = new FileResource();
            resource.setFilePath(fileId);
            resource.setFileCode(fileCode);
            resource.setFileSize(file.getSize() + "");
            resource.setFileHash(MD5Util.md5HashCode32(file));
            resource.setFileType(file.getContentType());
            resource.setSignCount(res.getSignCount() + 1);
            resource.setGmtModified(date);
            resource.setGmtMod(date);
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(SUFFIX_SEPERATOR));
            if(PDFNAME.equalsIgnoreCase(fileType)) {
                try (PDDocument pdfDocument = PDDocument
                        .load(file.getInputStream(), MemoryUsageSetting.setupTempFileOnly())) {
                    resource.setPageTotal(new Long(pdfDocument.getNumberOfPages()));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
                }
            }else{
                resource.setPageTotal(0L);
            }
            fileResourceMapper.updateFile(resource);

            FileResource res2 = fileService.findByFileCode(fileCode);
            FileResourceDto dto = new FileResourceDto();
            BeanUtils.copyProperties(res2, dto);
            FileUploadRespDto resp = new FileUploadRespDto();
            resp.setFile(dto);

            //移除历史数据
            client.delete(res.getFilePath());
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION.getCode(), e.getLocalizedMessage());
        }
    }

    @Override
    public FileUploadRespDto updateFileInfo(MultipartFile file, FileResourceInfoVO fileResourceInfoVO) {
        FileResource res = fileService.findByFileCode(fileResourceInfoVO.getFileCode());
        if (StringUtils.isEmpty(res) || (!fileResourceInfoVO.getFileCode().equals(res.getFileCode())) || 0 != res.getIsDeleted()) {
            throw new BusinessException(FileResponseCode.DATA_FILE_CODE_NULL);
        }
        String extension = FileUtil.getFileExtensionName(file.getOriginalFilename());
        String fileName = FileUtil.getFileName(file.getOriginalFilename());
        // 提取fdfs需要的参数
        Map<String, String> map = new HashMap<>(INITIAL_CAPACITY);
        map.put(FILENAME, fileName);
        map.put("name", file.getName());
        map.put(fileContentType, file.getContentType());

        map.put(FILESIZE, String.valueOf(file.getSize()));
        map.put(FILEEXT, extension);
        try {
            byte[] bytes = file.getBytes();
            // fdfs调用
            String fileId = client.upload(bytes, fileName, map);
            log.info("fastdfs updateFileInfo result: {}", fileId);
            LocalDateTime date = LocalDateTime.now();
            FileResource resource = new FileResource();
            BeanUtils.copyProperties(fileResourceInfoVO,resource);
            resource.setFilePath(fileId);
            resource.setFileCode(fileResourceInfoVO.getFileCode());
            resource.setFileSize(file.getSize() + "");
            resource.setFileHash(MD5Util.md5HashCode32(file));
            resource.setFileType(file.getContentType());
            resource.setSignCount(res.getSignCount() + 1);
            resource.setGmtModified(date);
            resource.setGmtMod(date);
            //签章配置
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(SUFFIX_SEPERATOR));
            if(PDFNAME.equalsIgnoreCase(fileType)) {
                try (PDDocument pdfDocument = PDDocument
                        .load(file.getInputStream(), MemoryUsageSetting.setupTempFileOnly())) {
                    resource.setPageTotal(new Long(pdfDocument.getNumberOfPages()));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
                }
            }else {
                resource.setPageTotal(0L);
            }
            fileResourceMapper.updateFile(resource);

            FileResource res2 = fileService.findByFileCode(fileResourceInfoVO.getFileCode());
            FileResourceDto dto = new FileResourceDto();
            BeanUtils.copyProperties(res2, dto);
            FileUploadRespDto resp = new FileUploadRespDto();
            resp.setFile(dto);
            //移除历史数据
            client.delete(res.getFilePath());
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION.getCode(), e.getLocalizedMessage());
        }
    }

    @Override
    public FileUploadRespDto baseUpload(MultipartFile file, String userId) {
        try {
            String extension = FileUtil.getFileExtensionName(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + ".pdf";
            // 提取fdfs需要的参数
            Map<String, String> map = new HashMap<>(INITIAL_CAPACITY);
            map.put(FILENAME, fileName);
            map.put("name", file.getName());
            map.put(fileContentType, pdfType);
            map.put(FILESIZE, String.valueOf(file.getSize()));
            map.put(FILEEXT, extension);
            byte[] bytes = file.getBytes();
            // fdfs调用
            String fileId = client.upload(bytes, fileName, map);
            log.info("fastdfs upload result: {}", fileId);
            LocalDateTime date = LocalDateTime.now();
            FileResource resource = new FileResource();
            resource.setId(String.valueOf(SnowflakeIdWorker.getId()));
            resource.setFileName(fileName);
            resource.setFilePath(fileId);
            resource.setUserId(userId);
            resource.setFileCode(UUID.randomUUID().toString());
            resource.setFileSize(file.getSize() + "");
            resource.setFileType(pdfType);
            resource.setIsDeleted(0);
            resource.setPrintNum(0L);
            resource.setSignCount(0L);
            resource.setDownCount(0L);
            resource.setFileHash(MD5Util.md5HashCode32(file));
            resource.setGmtCreate(date);
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(SUFFIX_SEPERATOR));
            if(PDFNAME.equalsIgnoreCase(fileType)) {
                try (PDDocument pdfDocument = PDDocument
                        .load(file.getInputStream(), MemoryUsageSetting.setupTempFileOnly())) {
                    resource.setPageTotal(new Long(pdfDocument.getNumberOfPages()));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
                }
            }else{
                resource.setPageTotal(0L);
            }
            fileService.save(resource);
            FileResourceDto dto = new FileResourceDto();
            BeanUtils.copyProperties(resource, dto);
            FileUploadRespDto resp = new FileUploadRespDto();
            resp.setFile(dto);
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    @Override
    public InMemoryMultipartFile fetch(String fileCode) {
        try {
            FileResource resource = fileService.findByFileCode(fileCode);
            if (null == resource) {
                throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
            }
            String fileId = resource.getFilePath();
            log.info("fetch resource: {}", fileId);
            Map<String, String> map = client.getMeta(fileId);
            log.info("fetch meta: {}", map);
            byte[] bytes = client.download(fileId);
            String fileName = map.get(FILENAME);
            String contentType = map.get(fileContentType);
            if (ObjectUtils.isEmpty(fileName)) {
                fileName = fileCode;
            }
            if (ObjectUtils.isEmpty(contentType)) {
                contentType = FILETYPE;
            }
            return new InMemoryMultipartFile("file", fileName, contentType, bytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    public byte[] fetchFileid(String fileId) {
        try {
            return client.download(fileId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    InMemoryMultipartFile fetchWithC(String fileCode, boolean grating, boolean watermark, boolean origin) {
        try {
            if (origin) {
                SignSysCompactFile signSysCompactFile = signSysCompactFileMapper.selectOne(
                        Wrappers.<SignSysCompactFile>lambdaQuery().eq(SignSysCompactFile::getFileCode, fileCode));
                fileCode = signSysCompactFile.getFileCodeOrigin();
            }
            FileResource resource = fileService.findByFileCode(fileCode);
            if (null == resource) {
                throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
            }
            String fileId = resource.getFilePath();
            log.info("原始文件ID" + fileId);
            if(grating){
                /*光栅*/
                AddGratingReqVO addGratingReqVO = new AddGratingReqVO();
                addGratingReqVO.setBussinessID("01");
                addGratingReqVO.setReqType("gratingPdf");
                addGratingReqVO.setSrcFileID(fileId);
                addGratingReqVO.setText("ANYINKEJI");
                addGratingReqVO.setX(200);
                addGratingReqVO.setY(350);
                addGratingReqVO.setImageDpi(150);
                String relt = addWatermarkService.pdfGrating(addGratingReqVO);
                AddWatermarkRespVO respVO =  JSON.parseObject(relt,AddWatermarkRespVO.class);
                fileId = respVO.getFileID();
                log.info("光栅文件ID" + fileId);
            }
            if(watermark){
                /*水印*/
                AddWatermarkPdfReqVO addWatermarkPdfReqVO = new AddWatermarkPdfReqVO();
                addWatermarkPdfReqVO.setBussinessID("01");
                addWatermarkPdfReqVO.setReqType("pdfAddWatermark");
                addWatermarkPdfReqVO.setSrcFileID(fileId);
                addWatermarkPdfReqVO.setText("ANYINKEJI");
                addWatermarkPdfReqVO.setType(1);
                addWatermarkPdfReqVO.setImageDpi(150);
                String relt = addWatermarkService.pdfWatermark(addWatermarkPdfReqVO);
                AddWatermarkRespVO respVO =  JSON.parseObject(relt,AddWatermarkRespVO.class);
                fileId = respVO.getFileID();
                log.info("水印文件ID" + fileId);
            }
            log.info("fetch resource: {}", fileId);
            Map<String, String> map = client.getMeta(fileId);
            log.info("fetch meta: {}", map);
            byte[] bytes = client.download(fileId);
            String fileName = null != map ? map.get(FILENAME) : "anonymous";
            String contentType = null != map ? map.get(fileContentType) : PDFNAME;
            if (ObjectUtils.isEmpty(fileName)) {
                fileName = fileCode;
            }
            if (ObjectUtils.isEmpty(contentType)) {
                contentType = FILETYPE;
            }
            return new InMemoryMultipartFile("file", fileName, contentType, bytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    @Override
    public InMemoryMultipartFile downloadSignDoc(String fileCode) {
        try {
            FileResource resource = fileService.findByFileCode(fileCode);
            if (null == resource) {
                throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
            }
            String fileId = resource.getFilePath();
            log.info("downloadSignDoc resource: {}", fileId);
            Map<String, String> map = client.getMeta(fileId);
            log.info("downloadSignDoc meta: {}", map);
            byte[] bytes = client.download(fileId);
            String fileName = map.get(FILENAME);
            String contentType = map.get(fileContentType);
            if (ObjectUtils.isEmpty(fileName)) {
                fileName = fileCode;
            }
            if (ObjectUtils.isEmpty(contentType)) {
                contentType = FILETYPE;
            }
            //update downCount
            FileResource fr = new FileResource();
            fr.setFileCode(fileCode);
            fr.setDownCount(resource.getDownCount() + 1);
            fileResourceMapper.updateFile(fr);
            return new InMemoryMultipartFile("file", fileName, contentType, bytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    @Override
    public InMemoryMultipartFile downloadDocZip(DocPackageReqDto reqDto) {
        Collection<FileResource> list = new ArrayList<>();
        List<String> fileCodeList = reqDto.getResourceIds();
        for (String fileCode : fileCodeList) {
            FileResource fileResource = fileService.findByFileCode(fileCode);
            list.add(fileResource);
        }
        if (list.isEmpty()) {
            throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
        }
        HashSet<String> fileNames = new HashSet<>();
        InMemoryMultipartFile inMemoryMultipartFile;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            docPackage(list, baos, fileNames);
            inMemoryMultipartFile = new InMemoryMultipartFile("file", reqDto.getUserId() + UUID.randomUUID() + ".zip", FILETYPE, baos.toByteArray());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
        return inMemoryMultipartFile;
    }

    @Override
    public ImgInfoListDTO uploadCovertPic(MultipartFile file, String userId) {
        ImgInfoListDTO imgInfoListDTO;
        try {
            imgInfoListDTO = pdfToimg(file, fileTempPath, userId);
        } catch (IOException e) {
            log.error("uploadCovertPic file IOException :", e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
        return imgInfoListDTO;
    }

    @Override
    public ImgInfoListDTO uploadCovertPic(String fileCode,HttpServletRequest request) {
        ImgInfoListDTO imgInfoListDTO;
        InMemoryMultipartFile in = fetch(fileCode);
        try {
            imgInfoListDTO = pdfToimg(in, fileTempPath, getUser(request));
        } catch (IOException e) {
            log.error("uploadCovertPic fileCode IOException :", e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
        return imgInfoListDTO;
    }

    @Override
    public ImgInfoDTO uploadCovertPicSize(String fileCode) {
        ImgInfoDTO imgInfoDTO;
        InMemoryMultipartFile in = fetch(fileCode);
        try {
            imgInfoDTO = pdfToimgSize(in, fileTempPath);
        } catch (IOException e) {
            log.error("uploadCovertPic IOException :", e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
        return imgInfoDTO;
    }

    @Override
    public String uploadCovertPdf(MultipartFile file) {
        String base64str;
        try {
            File outFile = fileToPdf(file, fileTempPath);
            base64str = FileConvertToBase64Util.fileToBase64(outFile);
            deleteFile(outFile);
        } catch (IOException e) {
            log.error("uploadCovertPdf IOException :", e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
        return base64str;
    }

    @Override
    public InMemoryMultipartFile downloadCovertPdf(MultipartFile file) {
        InMemoryMultipartFile in;
        try {
            File outFile = fileToPdf(file, fileTempPath);
            in = new InMemoryMultipartFile(outFile);
            deleteFile(outFile);
        } catch (IOException e) {
            log.error("downloadCovertPdf IOException :", e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
        return in;
    }

    @Override
    public FileUploadRespDto uploadCovertPdfData(MultipartFile file, HttpServletRequest request) {
        FileUploadRespDto fileUploadRespDto;
        try {
            File outFile = fileToPdf(file, fileTempPath);
            //upload outFile
            String fileName = FileUtil.getFielNameNoExt(file.getOriginalFilename());
            fileUploadRespDto = upload(new InMemoryMultipartFile(outFile,fileName+".pdf"), getUser(request));
            deleteFile(outFile);
        } catch (IOException e) {
            log.error("uploadCovertPdf IOException :", e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
        return fileUploadRespDto;
    }

    @Override
    public Boolean del(String fileCode) {
        FileResource resource = fileService.findByFileCode(fileCode);
        if (null == resource) {
            throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
        }
        //移除历史数据
        Boolean delete;
        try {
            delete = client.delete(resource.getFilePath());
            if (delete) {
                fileResourceMapper.deleteById(resource.getId());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
        return delete;
    }

    @Override
    public InMemoryMultipartFile fetchByPage(String fileCode, int pageNo) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            log.info(fileCode);
            FileResource resource = fileService.findByFileCode(fileCode);
            if(null==resource){
                throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
            }
            String fileId = resource.getFilePath();
            // M00/00/00/wKgz5lubBuiAEklbAAAKBAckW4E5952565
            log.info("fetchByPage resource: {}", fileId);
            Map<String, String> map = client.getMeta(fileId);
            log.info("fetchByPage meta: {}", map);
            byte[] bytes = client.download(fileId);
            String fileName = map.get(FILENAME);
            String contentType = map.get(fileContentType);
            if (ObjectUtils.isEmpty(fileName)) {
                fileName = fileCode;
            }
            String pdf = ".pdf";
            if (!pdfType.equals(contentType) && (!resource.getFilePath().contains(pdf))) {
                throw new BusinessException(FileResponseCode.DATA_INVALID_DOC_EXCEPTION);
            }
            String imageType = "jpg";
            pdfUtil.getPdfImageByPage(bytes, imageType, pageNo, DPI, output);
            contentType = "image/jpg";
            bytes = output.toByteArray();
            InMemoryMultipartFile resp = new InMemoryMultipartFile("file", fileName, contentType, bytes);
            log.info("file size: {}", resp.getSize());
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
    }

    @Override
    public int pageTotal(String fileCode) {
        MultipartFile file = fetch(fileCode);
        int total;
        try {
            total = PdfUtil.getPdfNumberOfPages(file.getBytes());
            log.info("" + total);
            return total;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
    }

    /**
     * 获取雾化文件
     *
     * @param fileCode
     * @return : com.iyin.sign.system.model.InMemoryMultipartFile
     * @Author: yml
     * @CreateDate: 2019/7/30
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/30
     * @Version: 0.0.1
     */
    @Override
    public InMemoryMultipartFile fetch2(String fileCode) {
        try {
            FileResource resource = fileService.findByFileCode(fileCode);
            if (null == resource) {
                throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
            }
            String fileId = resource.getAtomizationFilePath();
            if(org.apache.commons.lang.StringUtils.isBlank(fileId)){
                fileId = resource.getFilePath();
            }
            log.info("fetch resource: {}", fileId);
            Map<String, String> map = client.getMeta(fileId);
            log.info("fetch meta: {}", map);
            byte[] bytes = client.download(fileId);
            String fileName = map.get(FILENAME);
            String contentType = map.get(fileContentType);
            if (ObjectUtils.isEmpty(fileName)) {
                fileName = fileCode;
            }
            if (ObjectUtils.isEmpty(contentType)) {
                contentType = FILETYPE;
            }
            return new InMemoryMultipartFile("file", fileName, contentType, bytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    /**
     * 按页浏览pdf，以图片的方式返回jpeg
     *
     * @param fileCode
     * @param pageNo
     * @return : com.iyin.sign.system.model.InMemoryMultipartFile
     * @Author: yml
     * @CreateDate: 2019/7/31
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/31
     * @Version: 0.0.1
     */
    @Override
    @Cacheable(value = CacheConstants.PDF_SCAN,key = "#fileCode + '_' + #pageNo")
    public InMemoryMultipartFile fetchByPage2(String fileCode, int pageNo, HttpServletRequest request) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            log.info(fileCode);
            FileResource resource = fileService.findByFileCode(fileCode);
            if (null == resource) {
                throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
            }
            String fileId = resource.getFilePath();
            log.info("fetchByPage resource: {}", fileId);
            Map<String, String> map = client.getMeta(fileId);
            log.info("fetchByPage meta: {}", map);
            byte[] bytes = client.download(fileId);
            String fileName = map.get(FILENAME);
            String contentType = map.get(fileContentType);
            if (ObjectUtils.isEmpty(fileName)) {
                fileName = fileCode;
            }
            String pdf = ".pdf";
            if (!pdfType.equals(contentType) && (!resource.getFilePath().contains(pdf))) {
                throw new BusinessException(FileResponseCode.DATA_INVALID_DOC_EXCEPTION);
            }
            List<SysSignatureLog> signatureLogs = signatureLogMapper.selectList(Wrappers.<SysSignatureLog>lambdaQuery().eq(SysSignatureLog::getFileCode, fileCode).orderByDesc(SysSignatureLog::getCreateDate));
            PdfReadUtil.manipulatePdfNew(bytes,  pageNo - 1, output, signatureLogs);
            contentType = "image/jpg";
            bytes = output.toByteArray();
            InMemoryMultipartFile resp = new InMemoryMultipartFile("file", fileName, contentType, bytes);
            log.info("file size: {}", resp.getSize());
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
    }

    private String ckToken(HttpServletRequest request) {
        log.info("FileServiceImpl ckToken");
        String userId="";
        String token = request.getHeader(API_TOKEN);
        String sessionToken = request.getHeader(SESSION_TOKEN);
        if (org.apache.commons.lang.StringUtils.isBlank(token) && org.apache.commons.lang.StringUtils.isBlank(sessionToken)) {
            throw new BusinessException(ErrorCode.REQUEST_40434);
        }
        if(org.apache.commons.lang.StringUtils.isNotBlank(sessionToken)){
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get("userId"));
        }else{
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(token);
            userId = sysSignApplication.getUserId();
        }
        return userId;
    }

    @Override
    public String upload(File imageLocalFile) {
        String fileId;
        try {
            fileId = client.upload(imageLocalFile);
        } catch (Exception e) {
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
        return fileId;
    }

    @Override
    public String getFullUrlByFileId(String fileId) throws IOException{
        if (fileId.startsWith("/")) {
            throw new IOException("文件Id格式错误！");
        }
        StringBuilder sb = new StringBuilder(fastdfsHead);
        //生产环境下，上传和下载的ip不一致
        sb.append(fastdfsIpPort);
        sb.append(fileId);
        return sb.toString();
    }

    File fileToPdf(MultipartFile file, String destImgFolder) throws IOException {
        log.info("fileToPdf :pdfFile.getName:{},destImgFolder:{} ,getOriginalFilename:{}", file.getName(), destImgFolder,file.getOriginalFilename());
        File tempDir = new File(destImgFolder);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(SUFFIX_SEPERATOR));
        File outFile;
        //先处理图片类型
        if (StringUtils.hasText(file.getContentType()) && file.getContentType().toLowerCase().startsWith(IMAGE)) {
            outFile = new File(destImgFolder, UUID.randomUUID().toString() + PDFNAME);
            FileUtils.writeByteArrayToFile(outFile, img2pdf(file));
            log.info("outFile:" + outFile.getName());
        } else if (!PDFNAME.equals(fileType)) {
            log.info("fileToPdf no pdf fileType:{}",pdfType);
            outFile = convert2pdf(file, destImgFolder);
        } else {
            outFile = new File(destImgFolder, UUID.randomUUID().toString() + PDFNAME);
            try (OutputStream outputStream = new FileOutputStream(outFile)) {
                FileUtil.copy(file.getInputStream(), outputStream);
                log.info("fileToPdf :outFile.exists:{}", outFile.exists());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
            }
        }
        return outFile;
    }

    private byte[] img2pdf(MultipartFile file) {
        try {
            return pdfUtil.img2pdf(file.getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
    }

    /**
     * 文件转图片并上传到文件服务器
     * 将指定的pdf文件转换为指定路径的图片
     *
     * @param pdfFile 原文件路径，例如d:/test/test.pdf
     * @param destImgFolder 图片生成路径，例如 d:/test/  /usr/local/document/
     * <p>
     * dpi越大转换后越清晰，相对转换速度越慢
     */
    private ImgInfoListDTO pdfToimg(MultipartFile pdfFile, String destImgFolder, String userId) throws IOException {
        log.info("pdfToimg :pdfFile.getName:{},destImgFolder:{} ", pdfFile.getName(), destImgFolder);
        File tempDir = new File(destImgFolder);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        ImgInfoListDTO imgInfoListDTO=new ImgInfoListDTO();
        String fileType=pdfFile.getOriginalFilename().substring(pdfFile.getOriginalFilename().lastIndexOf(SUFFIX_SEPERATOR));
        File outFile=null;
        InMemoryMultipartFile inFile=null;
        //先处理图片类型
        if (StringUtils.hasText(pdfFile.getContentType()) && pdfFile.getContentType().toLowerCase().startsWith(IMAGE)) {
            inFile = new InMemoryMultipartFile(UUID.randomUUID() + ".png", img2pdf(pdfFile));
            log.info("pdfFile:" + pdfFile.getName());
        } else if (!PDFNAME.equals(fileType)) {
            outFile = convert2pdf(pdfFile, destImgFolder);
        } else {
            outFile = new File(destImgFolder, UUID.randomUUID().toString() + PDFNAME);
            log.info("初始化 outFile");
            try (OutputStream outputStream = new FileOutputStream(outFile)) {
                log.info("输出到 outFile");
                FileUtil.copy(pdfFile.getInputStream(), outputStream);
                log.info("PdfUtils pdfToimg :outFile.exists:{}", outFile.exists());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
            }
        }
        if (null == inFile) {
            if (null == outFile) {
                throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
            }
            inFile = new InMemoryMultipartFile(outFile);
        }
        try (PDDocument pdfDocument = PDDocument.load(inFile.getInputStream())) {
            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            List<ImgInfoVo> list = new ArrayList<>();
            int pages = pdfDocument.getNumberOfPages();
            int h = 0;
            int w = 0;
            for (int i = 0; i < pages; i++) {
                ImgInfoVo imgvo = new ImgInfoVo();
                //图片文件目录处理 imgPath dstFile
                String imgPath = destImgFolder + (i + 1) + SUFFIX_SEPERATOR + FILETYPE_PNG;
                File dstFile = new File(imgPath);
                BufferedImage image = renderer.renderImageWithDPI(i, IMGDPI);
                ImageIO.write(image, FILETYPE_PNG, dstFile);
                h = image.getHeight();
                w = image.getWidth();
                //读取图片文件
                InMemoryMultipartFile inMemoryMultipartFile = new InMemoryMultipartFile(dstFile);
                //文件上传
                //yunqiantuozhuai FileUploadRespDto
                FileUploadRespDto fileUploadRespDto = upload(inMemoryMultipartFile, userId);
                imgvo.setPageNo(i + 1);

                imgvo.setFileCode(fileUploadRespDto.getFile().getFileCode());
                list.add(imgvo);
                PdfUtils.deleteFile(imgPath);
            }
            //总页码
            imgInfoListDTO.setTotalPageNo(pages);
            imgInfoListDTO.setHeight(h);
            imgInfoListDTO.setWidth(w);
            imgInfoListDTO.setImgInfoList(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
        deleteFile(outFile);
        return imgInfoListDTO;
    }

    /**
     * 文件转图片并上传到文件服务器
     * 将指定的pdf文件转换为指定路径的图片
     *
     * @param pdfFile 原文件路径，例如d:/test/test.pdf
     * @param destImgFolder 图片生成路径，例如 d:/test/  /usr/local/document/
     * <p>
     * dpi越大转换后越清晰，相对转换速度越慢
     */
    private ImgInfoDTO pdfToimgSize(MultipartFile pdfFile, String destImgFolder) throws IOException {
        log.info("pdfToimgSize :pdfFile.getName:{},destImgFolder:{} ", pdfFile.getName(), destImgFolder);
        File tempDir = new File(destImgFolder);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        ImgInfoDTO imgInfoDTO = new ImgInfoDTO();
        String fileType = pdfFile.getOriginalFilename().substring(pdfFile.getOriginalFilename().lastIndexOf(SUFFIX_SEPERATOR));
        File outFile = null;
        InMemoryMultipartFile inFile = null;
        //先处理图片类型
        if (StringUtils.hasText(pdfFile.getContentType()) && pdfFile.getContentType().toLowerCase().startsWith(IMAGE)) {
            inFile = new InMemoryMultipartFile(UUID.randomUUID() + ".png", img2pdf(pdfFile));
            log.info("pdfToimgSize pdfFile:" + pdfFile.getName());
        } else if (!PDFNAME.equals(fileType)) {
            outFile = convert2pdf(pdfFile, destImgFolder);
        } else {
            outFile = new File(destImgFolder, UUID.randomUUID().toString() + PDFNAME);
            log.info(" pdfToimgSize 初始化 outFile");
            try (OutputStream outputStream = new FileOutputStream(outFile)) {
                log.info("输出到 outFile");
                FileUtil.copy(pdfFile.getInputStream(), outputStream);
                log.info("pdfToimgSize PdfUtils pdfToimg :outFile.exists:{}", outFile.exists());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
            }
        }
        if (null == inFile) {
            inFile = new InMemoryMultipartFile(outFile);
        }
        try (PDDocument pdfDocument = PDDocument.load(inFile.getInputStream(), MemoryUsageSetting.setupTempFileOnly())) {
            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            int pages = pdfDocument.getNumberOfPages();
            int h,w;
            //图片文件目录处理 imgPath dstFile
            String imgPath = destImgFolder + 1 + SUFFIX_SEPERATOR + FILETYPE_PNG;
            File dstFile = new File(imgPath);
            BufferedImage image = renderer.renderImageWithDPI(0, IMGDPI);
            ImageIO.write(image, FILETYPE_PNG, dstFile);
            h = image.getHeight();
            w = image.getWidth();
            PdfUtils.deleteFile(imgPath);
            //总页码
            imgInfoDTO.setTotalPageNo(pages);
            imgInfoDTO.setHeight(h);
            imgInfoDTO.setWidth(w);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
        deleteFile(outFile);
        return imgInfoDTO;
    }

    /**
     * 文件转pdf
     *
     * @param file, destImgFolder
     * @return java.io.InputStream
     * @Author: wdf
     * @CreateDate: 2019/3/6 11:56
     * @UpdateUser: wdf
     * @UpdateDate: 2019/3/6 11:56
     * @Version: 0.0.1
     */
    private File convert2pdf(MultipartFile file, String destImgFolder) {
        log.info("convert2pdf :{}", destImgFolder);
        File tempDir = new File(destImgFolder);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        String tempFileName = UUID.randomUUID().toString() + "." + FileUtil.getFileExtensionName(file.getOriginalFilename());
        File inFile = new File(tempDir, tempFileName);
        File outFile = new File(tempDir, UUID.randomUUID().toString() + PDFNAME);
        log.info("PdfUtils convert2pdf :inFile.getName:{}，outFile.getName:{}", inFile.getName(), outFile.getName());
        try (OutputStream outputStream = new FileOutputStream(inFile)) {
            FileUtil.copy(file.getInputStream(), outputStream);
            log.info("PdfUtils convert2pdf :outFile.exists:{}", outFile.exists());
            office2PdfUtil.convert(inFile, outFile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        } finally {
            log.info("PdfUtils convert2pdf :inFile.getName:{}，outFile.exists:{}", inFile.exists(), outFile.exists());
            deleteFile(inFile);
        }
        return outFile;
    }

    private void docPackage(Collection<FileResource> list, ByteArrayOutputStream baos, HashSet<String> fileNames) {
        StringBuilder fileName;
        byte[] bytes;
        try (
                // 检查输出流,采用CRC32算法，保证文件的一致性
                CheckedOutputStream cos = new CheckedOutputStream(baos, new CRC32());
                // 创建zip文件的输出流
                ZipOutputStream zos = new ZipOutputStream(cos, Charset.forName(CHARSET_UTF8))) {
            // 遍历合同文件列表
            for (FileResource resource : list) {
                fileName=new StringBuilder();
                if (fileNames.contains(resource.getFileName())) {
                    // 避免文件名重复
                    fileName.append(resource.getId()).append(" - ").append(resource.getFileName());
                }else{
                    fileName.append(resource.getFileName());
                }
                fileNames.add(fileName.toString());
                // 创建压缩文件
                zos.putNextEntry(new ZipEntry(fileName.toString()));
                bytes = client.download(resource.getFilePath());
                log.info("zip add file: {}/{}", bytes.length, fileName);
                // 写入压缩文件
                zos.write(bytes);
                zos.closeEntry();
            }
            zos.finish();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    /**
     * 删除指定文件
     *
     * @param file
     * @return void
     * @Author: wdf
     * @CreateDate: 2019/3/6 14:55
     * @UpdateUser: wdf
     * @UpdateDate: 2019/3/6 14:55
     * @Version: 0.0.1
     */
    private void deleteFile(File file) {
        if (!ObjectUtils.isEmpty(file) && file.exists()) {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                log.error("del file error:{}", e.getLocalizedMessage());
                throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
            }
        }
    }

    private String getUser(HttpServletRequest request) {
        String sessionToken = request.getHeader("session_token");
        log.info("=======sessionToken:{}=========", sessionToken);
        String user = "";
        if (!org.apache.commons.lang.StringUtils.isEmpty(sessionToken)) {
            Claims claims = jwtUtils.parseJWT(sessionToken);
            user = String.valueOf(claims.get("userId"));
        }
        if(org.apache.commons.lang.StringUtils.isBlank(user)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1011);
        }
        return user;
    }
}
