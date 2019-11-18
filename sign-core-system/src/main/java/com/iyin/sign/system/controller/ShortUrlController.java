package com.iyin.sign.system.controller;

import com.iyin.sign.system.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: ShortUrlController.java
 * @Description:
 * @Author: yml
 * @CreateDate: 2019/10/9
 * @UpdateUser: yml
 * @UpdateDate: 2019/10/9
 * @Version: 1.0.0
 */
@RestController
@Slf4j
@Api(tags = "短链接解析")
public class ShortUrlController {

    @Autowired
    RedisService redisService;

    @Value("${contract.base_url}")
    private String shortlinkOriBase;

    @ApiOperation(value = "短链转换")
    @GetMapping({"/short/{url}", "/short"})
    @ResponseBody
    public void shortResolve(
            @ApiParam(name = "url", value = "短链值") @PathVariable(name = "url", required = false)
                    String url, HttpServletResponse response) {
        try {
            if (StringUtils.isNotBlank(url)) {
                String longUrl = redisService.get(url);
                if (StringUtils.isNotBlank(longUrl)) {
                    response.sendRedirect(longUrl);
                }
            }
            log.warn("longUrl or url is Null by :" + url);
            response.sendRedirect(shortlinkOriBase);
        } catch (IOException e1) {
            log.error("com.iyin.sign.system.controller.ShortUrlController，exception:{}",e1.getLocalizedMessage());
        }
    }
}



