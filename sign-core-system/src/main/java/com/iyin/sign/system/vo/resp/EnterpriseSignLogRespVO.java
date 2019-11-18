package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: EnterpriseSignLogRespVO
 * @Description: 单位签章日志 列表展示
 * @Author: luwenxiong
 * @CreateDate: 2019/7/1 11:16
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/1 11:16
 * @Version: 0.0.1
 */
@Data
public class EnterpriseSignLogRespVO implements Serializable {

    private String logId;

    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value ="印章名称")
    private String sealName;


    @ApiModelProperty(value = "印章业务类型 单位：01 行政章、02 财务章等  03  业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    private String sealBusinessType;

    @ApiModelProperty(value = "印章编码")
    private String sealCode;

    /**
     * 介子类型  01  云印章  02 UKEY印章
     */
    @ApiModelProperty(value = "01  云印章  02 UKEY印章")
    private String mediumType;

    /**
     * 签署文件 hash值
     */
    private String signFileHash;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm" ,timezone = "GMT+8")
    private Date createDate;
}
