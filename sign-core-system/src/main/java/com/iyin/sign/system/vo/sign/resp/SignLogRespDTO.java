package com.iyin.sign.system.vo.sign.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignLogRespDTO
 * @Description: 签章日志返回
 * @Author: yml
 * @CreateDate: 2019/9/16
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/16
 * @Version: 1.0.0
 */
@Data
public class SignLogRespDTO implements Serializable {

    @ApiModelProperty("签署人")
    private String signName;

    @ApiModelProperty("章模业务类型：01 行政章、02 财务章等 03 业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    private String sealType;

    @ApiModelProperty("印章名称")
    private String sealName;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("签署时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date signDate;

    @ApiModelProperty("签署状态 01成功02失败")
    private String signatureResult;

    @ApiModelProperty("文件编码")
    private String fileCode;
}
