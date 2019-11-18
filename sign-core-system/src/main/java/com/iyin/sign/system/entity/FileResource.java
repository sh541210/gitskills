package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件微服务中文件资源表
 * </p>
 *
 * @author xiaofanzhou
 * @since 2018-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "文件中心")
@TableName("sign_sys_file_resource")
public class FileResource  extends Model<FileResource> {

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

    @ApiModelProperty(value = "雾化文件资源路径")
    private String atomizationFilePath;

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

    @ApiModelProperty(value = "最后一次打印分配次数")
    private Long printDisNum;

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

    @ApiModelProperty(value = "文件页数")
    private Long pageTotal;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "FileResource{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", fileCode='" + fileCode + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", atomizationFilePath='" + atomizationFilePath + '\'' +
                ", printNum=" + printNum +
                ", fileSize='" + fileSize + '\'' +
                ", fileHash='" + fileHash + '\'' +
                ", fileType='" + fileType + '\'' +
                ", signCount=" + signCount +
                ", downCount=" + downCount +
                ", verificationCode='" + verificationCode + '\'' +
                ", gmtVerification='" + gmtVerification + '\'' +
                ", qrCode=" + qrCode +
                ", printDisNum=" + printDisNum +
                ", isDeleted=" + isDeleted +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", gmtMod=" + gmtMod +
                ", pageTotal=" + pageTotal +
                '}';
    }
}
