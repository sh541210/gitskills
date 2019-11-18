package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SealInfoRespVO
 * @Description: 印章信息详情
 * @Author: luwenxiong
 * @CreateDate: 2019/6/24 11:01
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/24 11:01
 * @Version: 0.0.1
 */
@Data
public class SealInfoRespVO implements Serializable {
    private static final long serialVersionUID = 4383680077462062949L;

    @ApiModelProperty(value = "印章ID")
    private String id;


    @ApiModelProperty(value = "印章编码")
    private String pictureCode;

    @ApiModelProperty(value = "印章名称")
    private String pictureName;

    @ApiModelProperty(value = " 章模业务类型：单位：01 行政章、02 财务章等  03  业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    private String pictureBusinessType;

    @ApiModelProperty(value = "01： 章模  02：手写签名")
    private String pictureType;

    private String pictureWidth;

    private String pictureHeight;

    @ApiModelProperty(value = "证书来源 01 单位证书库关联  02 在线软证书 03 本地生成证书")
    private String certificateSource;

    @ApiModelProperty(value = "云印章对应的证书ID")
    private String certificateId;

    @ApiModelProperty(value = "印章所属个人还是单位  01：单位 02 个人")
    @NotBlank(message = "pictureUserType不能为空")
    private String pictureUserType;

    @NotBlank(message = "企业或个人id不能为空。")
    private String enterpriseOrPersonalId;

    private String picturePath;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date gmtCreate;

    private Integer isDeleted;

    @ApiModelProperty(value = "章模状态：01正常，02禁用")
    private String pictureStatus;

    @ApiModelProperty(value = "介质类型 01 云印章 02 ukey印章")
    private String mediumType;

    /**
     * 如果是ukey印章，需展示ukey印章中的证书信息
     */

    @ApiModelProperty(value = "ukey印章对应的证书ID")
    private String ukCertId;

    @ApiModelProperty(value = "数字证书厂商")
    private String issuer;

    @ApiModelProperty(value = "证书标识")
    private String oid;

    @ApiModelProperty(value = "信任服务号")
    private String trustId;

    @ApiModelProperty(value = "证书有效期开始")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date validStart;

    @ApiModelProperty(value = "证书有效期结束")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date validEnd;

}
