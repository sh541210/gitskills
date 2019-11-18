package com.iyin.sign.system.vo.file.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: FileManageQueryRespDTO
 * @Description: 文件管理查询返回
 * @Author: yml
 * @CreateDate: 2019/8/8
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/8
 * @Version: 1.0.0
 */
@Data
@ApiModel("文件管理查询返回")
public class FileManageQueryRespDTO {

    @ApiModelProperty("名称")
    private String compactTheme;

    @ApiModelProperty("创建人")
    private String creatUser;

    @ApiModelProperty("签章次数")
    private String signCount;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;

    @ApiModelProperty("最后签署时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtModified;

    @ApiModelProperty("验证码")
    private String verificationCode;

    @ApiModelProperty("二维码")
    private String qrCode;

    @ApiModelProperty("有效期")
    private String gmtVerification;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("文件编码")
    private String fileCode;

}