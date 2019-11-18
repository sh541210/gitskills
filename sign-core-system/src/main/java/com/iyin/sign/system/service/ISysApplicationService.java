package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.dto.req.SysApplicationDto;
import com.iyin.sign.system.dto.req.SysApplicationReq;
import com.iyin.sign.system.dto.req.SysSignApplicationDTO;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: ISysApplicationService
 * @Description: 系统应用服务
 * @Author: wdf
 * @CreateDate: 2019/8/7
 * @UpdateUser: wdf
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface ISysApplicationService extends IService<SysSignApplication> {

     IyinResult<IyinPage<SysSignApplicationDTO>> getApplicationList(Integer currPage, Integer pageSize, String applicationName,HttpServletRequest request);
     int updateApplication(SysSignApplication sysSignApplication);
     void deleteApplication(String id);
     void abledApp(String id,Integer type);
     int addApplication(SysApplicationReq sysApplicationReq, HttpServletRequest request);
     SysSignApplication addAppBackInfo(SysApplicationReq sysApplicationReq, HttpServletRequest request);
     SysSignApplication getApplicationById(SysApplicationDto sysApplicationDto);

}
