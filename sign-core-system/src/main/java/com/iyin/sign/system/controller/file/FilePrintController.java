package com.iyin.sign.system.controller.file;

/**
 * @ClassName FilePrintController
 * FilePrintController
 * @Author wdf
 * @Date 2019/7/19 14:16
 * @throws
 * @Version 1.0
 **/

import com.iyin.sign.system.common.SwaggerConstant;
import com.iyin.sign.system.dto.req.SignSysFilePrintDTO;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.ISignSysFilePrintService;
import com.iyin.sign.system.vo.req.PrintSummaryReqVO;
import com.iyin.sign.system.vo.req.SignSysContractPrintReq;
import com.iyin.sign.system.vo.req.SignSysFilePrintReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Api(value="文件打印服务 V1.1.0", tags= {SwaggerConstant.DOC_CENTER_PRINT})
@Slf4j
@RestController
@RequestMapping("/document/print")
public class FilePrintController {

    @Autowired
    private ISignSysFilePrintService signSysFilePrintService;

    private static final String OUT_FAIL="file output fail: ";
    private static final String CONTENT_DIS ="Content-Disposition";
    private static final String INLINE = "inline;filename=%s";

    /**
     * @Description 后台用户添加打印日志 并输出文件流
     * @Author: wdf
     * @CreateDate: 2018/12/3 19:15
     * @UpdateUser: wdf
     * @UpdateDate: 2018/12/3 19:15
     * @Version: 0.0.1
     * @param signSysFilePrintReq, response
     * @return void
     */
    @ApiOperation(value="文件打印 V1.1.0", tags= {SwaggerConstant.DOC_CENTER_PRINT})
    @PostMapping(value="/fetch")
    public void fetch(@RequestBody @Valid SignSysFilePrintReq signSysFilePrintReq, HttpServletRequest request, HttpServletResponse response) {
        InMemoryMultipartFile file = signSysFilePrintService.addFilePrint(signSysFilePrintReq,request);
        response.setContentType(file.getContentType());
        response.setHeader(CONTENT_DIS, String.format("inline;filename=%s", file.getOriginalFilename()));
        try {
            response.getOutputStream().write(file.getBytes());
        } catch (IOException e) {
            log.error(OUT_FAIL, e.getMessage());
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    /**
     * 合同打印日志 并输出文件流
     *
     * @Author: yml
     * @CreateDate: 2019/9/3
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/3
     * @Version: 0.0.1
     * @param sysContractPrintReq
     * @param request
     * @param response 
     * @return : void
     */
    @ApiOperation(value="文件打印 V1.2.0", tags= {SwaggerConstant.DOC_CENTER_PRINT})
    @PostMapping(value="/fetch2")
    public void fetch2(@RequestBody @Valid SignSysContractPrintReq sysContractPrintReq, HttpServletRequest request, HttpServletResponse response) {
        InMemoryMultipartFile file = signSysFilePrintService.addFilePrint2(sysContractPrintReq,request);
        response.setContentType(file.getContentType());
        response.setHeader(CONTENT_DIS, String.format("inline;filename=%s", file.getOriginalFilename()));
        try {
            response.getOutputStream().write(file.getBytes());
        } catch (IOException e) {
            log.error(OUT_FAIL, e.getMessage());
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    /**
    	* @Description 打印日志列表接口
    	* @Author: wdf
        * @CreateDate: 2019/7/22 11:39
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/22 11:39
    	* @Version: 0.0.1
        * @param currPage, pageSize, fileCode
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.dto.req.SignSysFilePrintDTO>>
        */
    @ApiOperation("打印日志列表接口  V1.1.0")
    @GetMapping("/getFilePrintList")
    public IyinResult<IyinPage<SignSysFilePrintDTO>> getFilePrintList(@RequestParam Integer currPage, @RequestParam Integer pageSize, @RequestParam(required = false) String fileCode, HttpServletRequest request) {
        return signSysFilePrintService.getFilePrintList(currPage,pageSize,fileCode,request);
    }


    /**
     * 打印摘要
     *
     * @Author: yml
     * @CreateDate: 2019/9/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/25
     * @Version: 0.0.1
     * @param printSummaryReqVO
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.InMemoryMultipartFile>
     */
    @ApiOperation("打印摘要 v1.2.1")
    @PostMapping("/printSummary")
    public void printSummary(
            @RequestBody @Valid PrintSummaryReqVO printSummaryReqVO, HttpServletRequest request,HttpServletResponse response) throws IOException {
        InMemoryMultipartFile inMemoryMultipartFile = signSysFilePrintService.printSummary(printSummaryReqVO, request);
        response.setContentType(inMemoryMultipartFile.getContentType());
        response.setHeader(CONTENT_DIS, String.format(INLINE, inMemoryMultipartFile.getOriginalFilename()));
        try {
            response.getOutputStream().write(inMemoryMultipartFile.getBytes());
        } catch (IOException e) {
            log.error(OUT_FAIL, e.getMessage());
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }
}
