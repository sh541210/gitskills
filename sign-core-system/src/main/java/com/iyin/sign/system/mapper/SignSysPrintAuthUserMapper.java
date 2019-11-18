package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysPrintAuthUser;
import com.iyin.sign.system.vo.resp.SignSysPrintAuthUserRespVO;
import feign.Param;

import java.util.List;

/**
 * <p>
 * 打印分配表 Mapper 接口
 * </p>
 *
 * @author wdf
 * @since 2019-08-06
 */
public interface SignSysPrintAuthUserMapper extends BaseMapper<SignSysPrintAuthUser> {

    /**
    　　* @description: 获取打印分配集合
    　　* @param fileCode
    　　* @return List</SignSysPrintAuthUserRespVo>
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 16:52
    　　*/
    List<SignSysPrintAuthUserRespVO> getPrintAuthList(@Param("fileCode") String fileCode);
}
