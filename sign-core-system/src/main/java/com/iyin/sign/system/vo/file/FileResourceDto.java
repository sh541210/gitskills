package com.iyin.sign.system.vo.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("文件资源实体")
@Getter
@Setter
@NoArgsConstructor
public class FileResourceDto implements Serializable {
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件资源id")
    private String id;

    @ApiModelProperty(value = "上传资源用户Id（user_info表中Id）")
    private String userId;

    @ApiModelProperty(value = "文件资源编码UUID")
    private String fileCode;

    @ApiModelProperty(value = "文件资源名称")
    private String fileName;

    @ApiModelProperty(value = "文件资源路径")
    private String filePath;

    @ApiModelProperty(value = "打印次数")
    private Long printNum;

    @ApiModelProperty(value = "文件大小(字节)")
    private String fileSize;

    @ApiModelProperty(value = "文件HASH")
    private String fileHash;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "签署次数")
    private Long signCount;

    @ApiModelProperty(value = "下载次数")
    private Long downCount;

    @ApiModelProperty(value = "验证码")
    private String verificationCode;

    @ApiModelProperty(value = "验证有效时间 0为永久有效")
    private String gmtVerification;

    @ApiModelProperty(value = "二维码")
    private Integer qrCode;

    @ApiModelProperty(value = "是否删除（0：未删除；1：删除）")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "签署时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime gmtMod;

    /**
     * 添加用户名称
     */
    @ApiModelProperty(value = "添加用户名称")
    private String createUserName;

    @ApiModelProperty(value = "文件页数")
    private Long pageTotal;

}
