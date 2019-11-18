package com.iyin.sign.system.controller.sign;

import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.IFastSignService;
import com.iyin.sign.system.vo.sign.req.SignReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @ClassName: SignController
 * @Description: 签章
 * @Author: yml
 * @CreateDate: 2019/10/29
 * @UpdateUser: yml
 * @UpdateDate: 2019/10/29
 * @Version: 1.0.0
 */
@RestController
@Api(tags = "签章")
public class SignController {

    @Autowired
    private IFastSignService fastSignService;

    @ApiOperation("签章")
    @PostMapping("signO")
    public IyinResult<String> sign(@RequestBody SignReqVO signReqVO) throws IOException {
        return IyinResult.success(fastSignService.sign(signReqVO));
    }
}
