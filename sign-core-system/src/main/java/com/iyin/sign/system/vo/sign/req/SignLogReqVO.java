package com.iyin.sign.system.vo.sign.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iyin.sign.system.vo.req.PageBaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @ClassName: SignLogReqVO
 * @Description: 签章日志请求
 * @Author: yml
 * @CreateDate: 2019/9/16
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/16
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignLogReqVO extends PageBaseVo {

    @ApiModelProperty("签署状态 01成功02失败")
    private String signatureResult;

    @ApiModelProperty("章模业务类型：01 行政章、02 财务章等 03 业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    private String sealType;

    @ApiModelProperty("签章时间前")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date signDateBefore;

    @ApiModelProperty("签章时间后")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date signDateAfter;

    @ApiModelProperty("签章人/印章名称")
    private String word;

    @ApiModelProperty(value = "用户ID",hidden = true)
    private String orgId;
}
