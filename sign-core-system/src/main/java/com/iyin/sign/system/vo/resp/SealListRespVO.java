package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName FileResourceInfoVO
 * FileResourceInfoVO
 * @Author wdf
 * @Date 2019/7/24 11:13
 * @throws
 * @Version 1.0
 **/
@Data
public class SealListRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 印章个数
     */
    @ApiModelProperty(value = "印章个数")
    private Integer count;

    @ApiModelProperty(value = "印章列表")
    private List<SealPrivateRespVO> sealList;

}
