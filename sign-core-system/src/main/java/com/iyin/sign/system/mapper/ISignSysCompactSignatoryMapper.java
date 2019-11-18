package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysCompactSignatory;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: ISignSysCompactSignatoryMapper.java
 * @Description: 签署人
 * @Author: yml
 * @CreateDate: 2019/8/7
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface ISignSysCompactSignatoryMapper extends BaseMapper<SignSysCompactSignatory> {

    /**
     * 更新签署状态
     *
     * @Author: yml
     * @CreateDate: 2019/8/9
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/9
     * @Version: 0.0.1
     * @param compactId
     * @param code
     * @return : int
     */
    int updateByCompactId(@Param("compactId") String compactId,@Param("code") String code);
}