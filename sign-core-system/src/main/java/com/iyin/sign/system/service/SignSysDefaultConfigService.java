package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysDefaultConfig;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.SignSysDefaultConfigReqVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: SignSysDefaultConfigService
 * @Description: 签章系统定义配置服务
 * @Author: wdf
 * @CreateDate: 2019/8/7
 * @UpdateUser: wdf
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface SignSysDefaultConfigService extends IService<SignSysDefaultConfig> {

    /**
     * 配置默认信息
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<Boolean> defaultConfig(SignSysDefaultConfigReqVO reqVO);

    /**
     * 上传logo
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<String> logoUpload(MultipartFile logo, String userId);
}
