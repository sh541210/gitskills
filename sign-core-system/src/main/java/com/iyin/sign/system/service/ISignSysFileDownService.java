package com.iyin.sign.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysFileDown;
import com.iyin.sign.system.model.InMemoryMultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 文件下载记录表 服务类
 * </p>
 *
 * @author wdf
 * @since 2019-08-08
 */
public interface ISignSysFileDownService extends IService<SignSysFileDown> {

    InMemoryMultipartFile addFileDown(String fileCode, HttpServletRequest request);

}
