package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.FileResource;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.file.resp.FileManageQueryRespDTO;
import com.iyin.sign.system.vo.req.FileResourceListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件微服务中文件资源表 Mapper 接口
 * </p>
 *
 * @author xiaofanzhou
 * @since 2018-09-14
 */

@Mapper
public interface FileResourceMapper extends BaseMapper<FileResource> {

    /**
     * 修改文件资源
     * @param fileResource
     * @return
     */
    int updateFile(FileResource fileResource);

    /**
     * 获取文件资源集合
     * @param page
     * @param fileResourceDto
     * @return
     */
    List<FileResourceDto> getPageList(IyinPage<FileResourceDto> page, @Param("fileResourceDto") FileResourceDto fileResourceDto);

    /**
     * 查询文
     *
     * @Author: yml
     * @CreateDate: 2019/7/10
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/10
     * @Version: 0.0.1
     * @param iyinPage
     * @param fileResourceListVO
     * @return : java.util.List<com.iyin.sign.system.vo.file.FileResourceDto>
     */
    List<FileResourceDto> queryByName(IyinPage<FileResourceDto> iyinPage,@Param("fileResourceListVO") FileResourceListVO fileResourceListVO);

    /**
     * 验证码验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/7/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/25
     * @Version: 0.0.1
     * @param verificationCode
     * @param enterpriseId
     * @return : com.iyin.sign.system.entity.FileResource
     */
    FileResource queryDocumetnByValityCode(@Param("verificationCode") String verificationCode,@Param("enterpriseId") String enterpriseId);

    /**
     * 更新处理后的雾化脱密文件
     *
     * @Author: yml
     * @CreateDate: 2019/7/30
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/30
     * @Version: 0.0.1
     * @param path
     * @param fileCode
     * @return : int
     */
    int updateOtherFile(@Param("path") String path,@Param("fileCode") String fileCode);

    /**
     * 查询文件管理列表
     *
     * @Author: yml
     * @CreateDate: 2019/7/10
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/10
     * @Version: 0.0.1
     * @param iyinPage
     * @param fileName
     * @param userId
     * @param type
     * @param orgId
     * @return : java.util.List<com.iyin.sign.system.vo.file.FileResourceDto>
     */
    List<FileManageQueryRespDTO> queryFileManageByName(IyinPage<FileManageQueryRespDTO> iyinPage, @Param("fileName") String fileName, @Param("userId") String userId, @Param("type") String type,@Param("orgId") String orgId);

    /**
     * 获取添加时间
     * @Author: wdf
     * @CreateDate: 2019/2/28 17:15
     * @UpdateUser: wdf
     * @UpdateDate: 2019/2/28 17:15
     * @Version: 0.0.1
     * @param
     * @return object
     */
    Object getAddDate();
}
