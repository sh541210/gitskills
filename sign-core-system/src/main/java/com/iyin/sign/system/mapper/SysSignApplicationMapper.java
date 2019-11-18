package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.dto.req.SysApplicationDto;
import com.iyin.sign.system.dto.req.SysSignApplicationDTO;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.model.IyinPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 应用表 Mapper 接口
 * </p>
 *
 * @author wdf
 * @since 2019-07-05
 */
public interface SysSignApplicationMapper extends BaseMapper<SysSignApplication> {

    /**
    　　* @description: 通过Id获取应用集合
    　　* @param sysApplicationDto
    　　* @return SysSignApplication
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 17:10
    　　*/
    SysSignApplication getApplicationById(SysApplicationDto sysApplicationDto);

    /**
    　　* @description: 修改应用信息
    　　* @param sysSignApplication
    　　* @return updateApplication
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 17:11
    　　*/
    int updateApplication(SysSignApplication sysSignApplication);

    /**
    　　* @description: 删除应用信息
    　　* @param application
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 17:12
    　　*/
    void deleteApplication(SysSignApplication application);

    /**
    　　* @description: 获取应用集合
    　　* @param page
     　 * @param sysSignApplicationDTO
    　　* @return List<SysSignApplicationDTO>
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 17:12
    　　*/
    List<SysSignApplicationDTO> getPageList(IyinPage<SysSignApplicationDTO> page, @Param("sysSignApplicationDTO") SysSignApplicationDTO sysSignApplicationDTO);
}
