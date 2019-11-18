package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 保存云印章
 * @author:barley
 * @detail:
 * @Version:0.01
 * @return:
 */
@Data
public class SaveCloudSealVO implements Serializable {

    private static final long serialVersionUID = -5439460537815225124L;

    @ApiModelProperty(value = "印章所属个人还是单位  01：单位 02 个人")
    @NotBlank(message = "pictureUserType不能为空")
    private String pictureUserType;

    @ApiModelProperty(value = "企业表或个人表主键ID")
    @NotBlank(message = "企业或个人id不能为空。")
    private String enterpriseOrPersonalId;

    //签名名称
    @ApiModelProperty(value = "印章名称")
    @NotBlank(message = "章模名称不能为空。")
    @Length(max = 20,message = "签名名称不能超过20个字符")
    private String pictureName;

    //上传签名文件URL
    @ApiModelProperty(value = "上传签名文件URL")
    @NotBlank(message = "上传签名文件URL不能为空。")
    private String picturePath;

    @ApiModelProperty(value = "章模类型 01：正规章模图片 02：手写签名")
    @NotBlank(message = "章模图片类型不能为空")
    private String pictureType;

    @ApiModelProperty(value = " 章模业务类型：单位：01 行政章、02 财务章等  03  业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    @NotBlank(message = "印章类型不能为空")
    private String pictureBusinessType;

    @ApiModelProperty(value = "章模宽度")
    private String pictureWidth;

    @ApiModelProperty(value = "章模高度")
    private String pictureHeight;

    @ApiModelProperty(value = "章模临时表Id不能为空。")
    @NotBlank(message = "章模临时表Id")
    private String pictureDataTmpId;

    @ApiModelProperty(value = "01 单位证书库关联  02 在线软证书 03 本地生成证书")
    @NotBlank(message = "证书来源不能为空 ")
    private String certificateSource;

    /**
     * 证书ID ,当certificateSource =01时不能为空
     */
    private String certificateId;


}
