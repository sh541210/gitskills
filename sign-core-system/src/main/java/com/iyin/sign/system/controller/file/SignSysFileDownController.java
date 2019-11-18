package com.iyin.sign.system.controller.file;


import com.iyin.sign.system.common.SwaggerConstant;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.ISignSysFileDownService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 文件下载记录表 前端控制器
 * </p>
 *
 * @author wdf
 * @since 2019-08-08
 */
@Api(value="文件下载服务 V1.2.0", tags= {SwaggerConstant.DOC_CENTER_DOWN})
@Slf4j
@Controller
@RequestMapping("/fileDown")
public class SignSysFileDownController {

    private static final String OUT_FAIL="file output fail: ";
    private static final String CONTENT_DIS ="Content-Disposition";

    @Autowired
    private ISignSysFileDownService signSysFileDownService;

    /**
    	* 后台用户添加下载日志 并输出文件流
    	* @Author: wdf 
        * @CreateDate: 2019/8/8 9:57
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/8 9:57
    	* @Version: 0.0.1
        * @param fileCode, request, response
        * @return void
        */
    @ApiOperation(value="文件下载 V1.2.0", tags= {SwaggerConstant.DOC_CENTER_DOWN})
    @PostMapping(value="/fetch")
    public void fetch(@RequestParam String fileCode, HttpServletRequest request, HttpServletResponse response) {
        InMemoryMultipartFile file = signSysFileDownService.addFileDown(fileCode,request);
        response.setContentType(file.getContentType());
        response.setHeader(CONTENT_DIS, String.format("inline;filename=%s", file.getOriginalFilename()));
        try {
            response.getOutputStream().write(file.getBytes());
        } catch (IOException e) {
            log.error(OUT_FAIL, e.getMessage());
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }
}
