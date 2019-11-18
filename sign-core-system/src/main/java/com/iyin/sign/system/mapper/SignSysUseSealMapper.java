package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.dto.req.SignSysUseSealDTO;
import com.iyin.sign.system.entity.SignSysUseSeal;
import com.iyin.sign.system.model.IyinPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
	* SignSysUseSealMapper
	* @Author: wdf 
    * @CreateDate: 2019/10/24 14:44
	* @UpdateUser: wdf
    * @UpdateDate: 2019/10/24 14:44
	* @Version: 0.0.1
    * @param 
    * @return 
    */
public interface SignSysUseSealMapper extends BaseMapper<SignSysUseSeal> {

    /**
    	* 申请用印记录
    	* @Author: wdf 
        * @CreateDate: 2019/10/24 14:43
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/24 14:43
    	* @Version: 0.0.1
        * @param page, signSysUseSealDTO
        * @return java.util.List<com.iyin.sign.system.dto.req.SignSysUseSealDTO>
        */
    List<SignSysUseSealDTO> getPageList(IyinPage<SignSysUseSealDTO> page, @Param("signSysUseSealDTO") SignSysUseSealDTO signSysUseSealDTO);
}