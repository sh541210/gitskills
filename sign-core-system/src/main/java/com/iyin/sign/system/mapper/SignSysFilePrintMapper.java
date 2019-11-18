package com.iyin.sign.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.dto.req.SignSysFilePrintDTO;
import com.iyin.sign.system.entity.SignSysFilePrint;
import com.iyin.sign.system.model.IyinPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件打印记录表 Mapper 接口
 * </p>
 *
 * @author wdf
 * @since 2019-07-19
 */
public interface SignSysFilePrintMapper extends BaseMapper<SignSysFilePrint> {

    /**
    　　* @description: 获取签章系统文件打印集合
    　　* @param page
     　 * @param signSysFilePrintDTO
    　　* @return List<SignSysFilePrintDTO>
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 16:47
    　　*/
    List<SignSysFilePrintDTO> getPageList(IyinPage<SignSysFilePrintDTO> page, @Param("signSysFilePrintDTO") SignSysFilePrintDTO signSysFilePrintDTO);
}
