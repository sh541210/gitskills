package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改云印章
 * @author:barley
 * @detail:
 * @Version:0.01
 * @return:
 */
@Data
public class UpdateCloudSealVO implements Serializable {

    private static final long serialVersionUID = -5439460537815225124L;

    @ApiModelProperty(value = "印章编码")
    @NotBlank(message = "印章编码不能为空")
    private String pictureCode;

    @ApiModelProperty(value = "印章名称")
    @NotBlank(message = "章模名称不能为空。")
    @Length(max = 20,message = "签名名称不能超过20个字符")
    private String pictureName;

    @ApiModelProperty(value = "创建方式：1手绘签名，2本地上传")
    @NotNull(message = "创建方式不能为空。")
    private Integer createMode;

    @NotBlank(message = "上传章模文件URL不能为空。")
    private String picturePath;

//    @ApiModelProperty(value = "章模类型：01 ukey印章，02云签名，03 云印章，04ukey签名")
//    @NotNull(message = "签名类型不能为空。")
//    private String pictureType;
    @ApiModelProperty(value = "介质类型 01：云印章 02：ukey印章")
    private String mediumType;

   @ApiModelProperty(value = "章模业务类型：PictureBusinessTypeEnum  01 行政章、02 财务章等 03 业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    private String pictureBusinessType;

    @ApiModelProperty(value = "章模宽度")
    @NotBlank(message = "章模宽度不能为空。")
    private String pictureWidth;

    @ApiModelProperty(value = "章模高度")
    @NotBlank(message = "章模高度不能为空。")
    private String pictureHeight;

    @ApiModelProperty(value = "章模临时表Id,如果重新上传了章莫，id不能为空")
    private String pictureDataTmpId;

    @NotBlank(message = "证书来源不能为空  01 单位证书库关联  02 在线软证书 03 本地生成证书")
    private String certificateSource;

    /**
     * 证书ID ,当certificateSource =01时不能为空
     */
    private String certificateId;


}
