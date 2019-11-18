package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.dto.req.TwoPressureCodeReqDTO;
import com.iyin.sign.system.entity.SignSysSealPictureDataTmp;
import com.iyin.sign.system.mapper.SignSysSealPictureDataTmpMapper;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.SignSysSealPictureDataTmpService;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName: SignSysSealPictureDataTmpServiceImpl
 * @Description: 签章系统章莫临时处理服务
 * @Author: luwenxiong
 * @CreateDate: 2019/6/22 17:02
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/22 17:02
 * @Version: 0.0.1
 */
@Slf4j
@Service
public class SignSysSealPictureDataTmpServiceImpl extends ServiceImpl<SignSysSealPictureDataTmpMapper,SignSysSealPictureDataTmp> implements SignSysSealPictureDataTmpService {

    @Autowired
    SignSysSealPictureDataTmpMapper signSysSealPictureDataTmpMapper;

    @Override
    public String savePictureDataPart(TwoPressureCodeReqDTO reqDTO) {

        //生成代理主键
        String id = SnowflakeIdWorker.getIdToStr();

        SignSysSealPictureDataTmp tmp = new SignSysSealPictureDataTmp();
        tmp.setId(id);
        tmp.setPictureData(reqDTO.getEyCode());
        tmp.setPictureData64(reqDTO.getBase64());
        tmp.setGmtCreate(new Date());
        /**上传的图片途径，0:章模，1:手绘签名*/
        tmp.setRemark(reqDTO.getPicType());

        int count = signSysSealPictureDataTmpMapper.insert(tmp);
        if(count==0){
            throw new BusinessException(7010,"电子印章章模数据入库失败");
        }
        return id;
    }
}
