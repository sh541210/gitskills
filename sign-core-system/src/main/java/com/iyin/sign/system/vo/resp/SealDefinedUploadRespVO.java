package com.iyin.sign.system.vo.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SealDefinedUploadRespVO
 * @Description: 章模上传响应VO
 * @Author: 唐德繁
 * @CreateDate: 2018/8/15 0015 上午 11:26
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/8/15 0015 上午 11:26
 * @Version: 0.0.1
 */
@Data
public class SealDefinedUploadRespVO implements Serializable{

    private static final Long serialVersionUID = 1L;

    private Integer pictureWidth;

    private Integer pictureHeight;

    private String picturePath;
    /**章模图片数据临时表代理主键*/
    private String pictureDataTmpId;
}
