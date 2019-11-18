package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignLogRespVO
 * @Description: 签章日志详情
 * @Author: luwenxiong
 * @CreateDate: 2019/6/26 16:51
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/26 16:51
 * @Version: 0.0.1
 */
@Data
public class SignLogRespVO implements Serializable {

    private static final long serialVersionUID = 6838419573515731532L;

    @ApiModelProperty(value = "签章人姓名")
    private String signerName;

    @ApiModelProperty(value = "文档名称")
    private String fileName;

    @ApiModelProperty(value = "印章名称")
    private String sealName;

    @ApiModelProperty(value = "印章业务类型")
    private String sealBusinessType;

    @ApiModelProperty(value = "印章编码")
    private String sealCode;

    @ApiModelProperty(value = "介质类型")
    private String sealMedia;

    @ApiModelProperty(value = "签后HASH")
    private String signHash;

    @ApiModelProperty(value = "签章时间")
    private Date signDate;
}
