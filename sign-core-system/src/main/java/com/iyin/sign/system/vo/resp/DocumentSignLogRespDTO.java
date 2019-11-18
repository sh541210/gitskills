package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: DocumentSignLogRespDTO
 * @Description: 文档签署日志
 * @Author: yml
 * @CreateDate: 2019/7/5
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/5
 * @Version: 1.0.0
 */
@Data
public class DocumentSignLogRespDTO implements Serializable {
    @ApiModelProperty("日志ID")
    private String logId;

    @ApiModelProperty("签章人用户名")
    private String userName;

    @ApiModelProperty("所属单位")
    private String orgName;

    @ApiModelProperty("所属个人")
    private String personalName;

    @ApiModelProperty("印章名称")
    private String sealName;

    @ApiModelProperty("章模业务类型：01 行政章、02 财务章等 03 业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    private String sealType;

    @ApiModelProperty("印章编码")
    private String sealCode;

    @ApiModelProperty("介质类型 01 云印章  02ukey印章")
    private String mediumType;

    @ApiModelProperty("签署文件 hash值")
    private String signFileHash;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm" ,timezone = "GMT+8")
    @ApiModelProperty("签章时间")
    private Date createDate;

}
