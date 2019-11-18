package com.iyin.sign.system.service;


import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.VerifySignFileReqDTO;
import com.iyin.sign.system.vo.resp.VerifyByCodeRespDTO;
import com.iyin.sign.system.vo.resp.VerifySignFileListRespDTO;
import com.iyin.sign.system.vo.resp.VerifySignFileRespDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: VerifyService
 * @Description: 验真服务接口类
 * @Author: 唐德繁
 * @CreateDate: 2018/12/13 0013 下午 3:11
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/12/13 0013 下午 3:11
 * @Version: 0.0.1
 */
public interface VerifyService {
    
    /**
     * 验真加签文件
     * @Author: 唐德繁
     * @CreateDate: 2018/12/13 下午 3:27
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/13 下午 3:27
     * @Version: 0.0.1
     * @param verifySignFileReqDTO
     * @return 
     */
    IyinResult<VerifySignFileRespDTO> setVerifySignFile(VerifySignFileReqDTO verifySignFileReqDTO);

    /**
     * 验真加签文件
     * @Author: wdf
     * @CreateDate: 2019/07/24 下午 2:27
     * @UpdateUser: wdf
     * @UpdateDate: 2019/07/24 下午 2:27
     * @Version: 0.0.1
     * @param fileCode
     * @return
     */
    IyinResult<VerifySignFileListRespDTO> setVerifySignFile(String fileCode);
    /**
     * 验真加签文件
     * @Author: wdf
     * @CreateDate: 2019/07/24 下午 2:27
     * @UpdateUser: wdf
     * @UpdateDate: 2019/07/24 下午 2:27
     * @Version: 0.0.1
     * @param file
     * @return
     */
    IyinResult<VerifySignFileListRespDTO> setVerifySignFile(MultipartFile file);

    /**
     * 二维码验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/9/3
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/3
     * @Version: 0.0.1
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.VerifySignFileListRespDTO>
     */
    IyinResult<VerifySignFileListRespDTO> verifySignByQrCode(String contractId);

    /**
     * 验证码验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/9/10
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/10
     * @Version: 0.0.1
     * @param code
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.VerifyByCodeRespDTO>
     */
    IyinResult<VerifyByCodeRespDTO> verifySignByCode(String code);
}
