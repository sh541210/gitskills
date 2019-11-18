package com.iyin.sign.system.controller;

import com.itextpdf.text.pdf.PdfReader;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.service.VerifyService;
import com.iyin.sign.system.util.PdfReadUtil;
import com.iyin.sign.system.vo.req.VerifySignFileReqDTO;
import com.iyin.sign.system.vo.resp.VerifyByCodeRespDTO;
import com.iyin.sign.system.vo.resp.VerifySignFileListRespDTO;
import com.iyin.sign.system.vo.resp.VerifySignFileRespDTO;
import com.iyin.sign.system.vo.sign.req.JudgePositionReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @ClassName: VerifyController
 * @Description: 验真Controller
 * @Author: 唐德繁
 * @CreateDate: 2018/12/13 0013 下午 2:50
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/12/13 0013 下午 2:50
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("/verify")
@Slf4j
@Api(tags = "v1.1.0_验真")
public class VerifyController {

    @Autowired
    private VerifyService verifyService;
    @Autowired
    private IFileService fileService;

    /**
     * 验真加签文件信息
     * @Author: 唐德繁
     * @CreateDate: 2018/12/13 下午 2:55
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/13 下午 2:55
     * @Version: 0.0.1
     * @param verifySignFileReqDTO
     * @return
     */
    @PostMapping("/sign/file")
    @ApiOperation(value = "加签文件验真")
    public IyinResult<VerifySignFileRespDTO> setVerifySignFile(@RequestBody @Valid VerifySignFileReqDTO verifySignFileReqDTO) {
        return verifyService.setVerifySignFile(verifySignFileReqDTO);
    }

    /**
     * 判断签章处是否空白
     *
     * @Author: yml
     * @CreateDate: 2019/7/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/23
     * @Version: 0.0.1
     * @param judgePositionReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    @PostMapping("judgePosition")
    @ApiOperation(value = "v1.1.0_判断签章处是否空白")
    public IyinResult<Boolean> judgePosition(@RequestBody @Valid JudgePositionReqVO judgePositionReqVO) throws IOException {
        PdfReadUtil pdfReadUtil = new PdfReadUtil(judgePositionReqVO.getPage(), judgePositionReqVO.getXCoordinate(),
                judgePositionReqVO.getYCoordinate(), judgePositionReqVO.getWCoordinate(),
                judgePositionReqVO.getHCoordinate());
        InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(judgePositionReqVO.getFileCode());
        PdfReader pdfReader =  new PdfReader(inMemoryMultipartFile.getInputStream());
        pdfReadUtil.manipulatePdf(pdfReader);
        return IyinResult.success(pdfReadUtil.isNotBlank());
    }

    /**
     * 验真加签文件信息
     * @Author: wdf
     * @CreateDate: 2019/07/24 下午 2:27
     * @UpdateUser: wdf
     * @UpdateDate: 2019/07/24 下午 2:27
     * @Version: 0.0.1
     * @param fileCode
     * @return
     */
    @GetMapping("/sign/file/{fileCode}")
    @ApiOperation(value = "加签文件验真 V1.1.0")
    public IyinResult<VerifySignFileListRespDTO> setVerifySignFile(@PathVariable  String fileCode) {
        return verifyService.setVerifySignFile(fileCode);
    }

    /**
     * 验真加签文件信息
     * @Author: wdf
     * @CreateDate: 2019/07/24 下午 2:27
     * @UpdateUser: wdf
     * @UpdateDate: 2019/07/24 下午 2:27
     * @Version: 0.0.1
     * @param file
     * @return
     */
    @PostMapping("/sign/verifyFile")
    @ApiOperation(value = "加签文件验真")
    @ApiIgnore
    public IyinResult<VerifySignFileListRespDTO> setVerifySignFile(@RequestPart MultipartFile file) {
        return verifyService.setVerifySignFile(file);
    }

    /**
     * 电子文档验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/7/26
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/26
     * @Version: 0.0.1
     * @param file
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.VerifySignFileListRespDTO>
     */
    @PostMapping("/sign/verify/electronics/file")
    @ApiOperation(value = "v1.1.0_电子文档验证签章")
    public IyinResult<VerifySignFileListRespDTO> verifySignElectronicsFile(@RequestParam("file") MultipartFile file) {
        return verifyService.setVerifySignFile(file);
    }

    /**
     * 二维码验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/7/26
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/26
     * @Version: 0.0.1
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.VerifySignFileListRespDTO>
     */
    @PostMapping("/sign/verify/qrCode/{id}")
    @ApiOperation(value = "v1.2.1_二维码验证签章")
    public IyinResult<VerifySignFileListRespDTO> verifySignByQrCode(@PathVariable("id") String contractId) {
        return verifyService.verifySignByQrCode(contractId);
    }

    /**
     * 验证码验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/7/26
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/26
     * @Version: 0.0.1
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.VerifyByCodeRespDTO>
     */
    @PostMapping("/sign/verify/code/{code}")
    @ApiOperation(value = "v1.2.1_验证码验证签章")
    public IyinResult<VerifyByCodeRespDTO> verifySignByCode(@PathVariable("code") String code) {
        return verifyService.verifySignByCode(code);
    }
}
