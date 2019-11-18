package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.dto.req.SignSysTemplateDTO;
import com.iyin.sign.system.entity.SignSysTemplate;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.req.SignSysTemplateListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 模板表 Mapper 接口
 * </p>
 *
 * @author wdf
 * @since 2019-07-15
 */
public interface SignSysTemplateMapper extends BaseMapper<SignSysTemplate> {

    /**
    　　* @description: 通过Id获取模板
    　　* @param signSysTemplateDTO
    　　* @return signSysTemplateDTO
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 17:02
    　　*/
    SignSysTemplate getTemplateById(SignSysTemplateDTO signSysTemplateDTO);

    /**
    　　* @description: 修改模板
    　　* @param signSysTemplate
    　　* @return int
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 17:03
    　　*/
    int updateTemplate(SignSysTemplate signSysTemplate);

    /**
    　　* @description: 获取模板集合
    　　* @param page
     　 * @param signSysTemplateDTO
    　　* @return List<SignSysTemplateDTO>
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 17:04
    　　*/
    List<SignSysTemplateDTO> getPageList(IyinPage<SignSysTemplateDTO> page, @Param("signSysTemplateListVO") SignSysTemplateListVO signSysTemplateListVO);
}
