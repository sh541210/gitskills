package com.iyin.sign.system.service;

import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.resp.CertInfoRespDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: ICertInfoService
 * @Description: 证书
 * @Author: yml
 * @CreateDate: 2019/6/26
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/26
 * @Version: 1.0.0
 */
public interface ICertInfoService {

    /**
     * 导入证书
     *
     * @Author: yml
     * @CreateDate: 2019/6/26
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/26
     * @Version: 0.0.1
     * @param file
     * @param certPassword
     * @param userId
     * @param orgId
     * @return : CertInfoRespDTO
     */
    CertInfoRespDTO importFile(MultipartFile file, String certPassword, String userId, String orgId);

    /**
     * 根据组织ID查找证书
     *
     * @Author: yml
     * @CreateDate: 2019/6/27
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/27
     * @Version: 0.0.1
     * @param orgId
     * @param pageNum
     * @param pageSize
     * @return : com.iyin.sign.system.service.ICertInfoService
     */
    IyinPage<CertInfoRespDTO> listByOrgId(String orgId, Integer pageNum, Integer pageSize);

    /**
     * 删除证书
     *
     * @Author: yml
     * @CreateDate: 2019/6/27
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/27
     * @Version: 0.0.1
     * @param certId
     * @return : boolean
     */
    boolean deleteById(String certId);
}
