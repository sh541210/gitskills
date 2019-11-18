package com.iyin.sign.system.vo.sign.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignatureLogReqDTO
 * @Description:
 * @Author: pc
 * @CreateDate: 2018/12/19
 * @UpdateUser: pc
 * @UpdateDate: 2018/12/19
 * @Version: 1.0.0
 */
@Data
public class SignatureLogReqVO implements Serializable {
    @ApiModelProperty("应用ID")
    private String appId;
    @ApiModelProperty("印章名称")
    private String sealName;
    @ApiModelProperty("印章编码")
    private String sealCode;
    @ApiModelProperty("印章类型")
    private String sealType;
    @ApiModelProperty("介质类型：01 云印章，02云签名，03 ukey印章，04ukey签名")
    private String mediumType;
    @ApiModelProperty(value = "签章方案(01:平台签，02:快捷签)")
    @NotNull(message = "签章类型不能为空")
    private String signatureType;
    @ApiModelProperty("签章模式")
    @NotBlank(message = "签章模式不能为空")
    private String signatureModel;
    @ApiModelProperty(value = "签章方式")
    @NotBlank(message = "签章方式不能为空")
    private String signatureName;
    @ApiModelProperty(value = "文件编码")
    @NotBlank(message = "文件编码不能为空")
    private String fileCode;
    @ApiModelProperty("文件名称")
    private String fileName;
    @ApiModelProperty(value = "签章结果(02:失败，01:成功)")
    @NotNull(message = "签章结果不能为空")
    private String signatureResult;
    @ApiModelProperty(value = "用户信息")
    @NotBlank(message = "用户信息不能为空")
    private String userId;
    @ApiModelProperty("签后文档HASH")
    private String signFileHash;
    @ApiModelProperty("签后文档ID")
    private String signFileCode;
    @ApiModelProperty(value = "文件页数")
    @NotBlank(message = "文件页数不能为空")
    private Integer page;
    @ApiModelProperty(value = "创建时间")
    @NotNull(message = "创建时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    @ApiModelProperty(value = "创建者")
    @NotBlank(message = "创建者不能为空")
    private String createUser;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;
    @ApiModelProperty(value = "更新者")
    @NotBlank(message = "更新者不能为空")
    private String updateUser;
    @ApiModelProperty(value = "签章参数")
    @NotBlank(message = "签章参数不能为空")
    private String multiParam;
    @ApiModelProperty("签章ip")
    private String ipAddress;
    @ApiModelProperty("签章mac")
    private String macAddress;
    @ApiModelProperty("签署设备")
    private String deviceName;
}
