package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SealFileInfoListRespVO
 * FileResourceInfoVO
 * @Author wdf
 * @Date 2019/7/24 11:13
 * @throws
 * @Version 1.0
 **/
@Data
public class SealFileInfoListRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 文件页数
     */
    @ApiModelProperty(value = "文件页数")
    private Long pageTotal;

    @ApiModelProperty(value = "文件资源编码UUID")
    private String fileCode;

    @ApiModelProperty(value = "文件资源名称")
    private String fileName;

}
