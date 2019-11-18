package com.iyin.sign.system.service.feign;

import com.iyin.sign.system.vo.req.AddGratingReqVO;
import com.iyin.sign.system.vo.req.AddWatermarkPdfReqVO;
import com.iyin.sign.system.vo.req.AddWatermarkReqVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name="c#service", url = "http://192.168.51.218:6355")
public interface AddWatermarkService {

    @ApiOperation("增加水印")
    @PostMapping(value = "/antiCounterfeiting")
    String testFench(@RequestBody @Valid AddWatermarkReqVO addWatermarkReqVO);

    /**
     * PDF增加水印
     *
     * @Author: yml
     * @CreateDate: 2019/9/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/23
     * @Version: 0.0.1
     * @param addWatermarkPdfReqVO
     * @return : java.lang.String
     */
    @ApiOperation("PDF增加水印")
    @PostMapping(value = "/antiCounterfeiting")
    String pdfWatermark(@RequestBody @Valid AddWatermarkPdfReqVO addWatermarkPdfReqVO);

    /**
     * PDF增加光栅
     *
     * @Author: yml
     * @CreateDate: 2019/9/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/23
     * @Version: 0.0.1
     * @param addGratingReqVO
     * @return : java.lang.String
     */
    @ApiOperation("PDF增加光栅")
    @PostMapping(value = "/antiCounterfeiting")
    String pdfGrating(@RequestBody @Valid AddGratingReqVO addGratingReqVO);
}
