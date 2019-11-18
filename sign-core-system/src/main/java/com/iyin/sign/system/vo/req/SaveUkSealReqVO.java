package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SaveUkSealReqVO
 * @Description: 保存uk印章
 * @Author: luwenxiong
 * @CreateDate: 2019/7/22 15:31
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/22 15:31
 * @Version: 0.0.1
 */
@Data
public class SaveUkSealReqVO implements Serializable {

    private static final long serialVersionUID = -7573350592765744101L;

    @ApiModelProperty(value = "印章所属个人还是单位  01：单位 02 个人")
    @NotBlank(message = "pictureUserType不能为空")
    private String pictureUserType;

    @ApiModelProperty(value = "企业表或个人表主键ID")
    @NotBlank(message = "企业或个人id不能为空。")
    private String enterpriseOrPersonalId;

    @ApiModelProperty(value = "从ukey读取到的印章编码")
    @NotBlank(message = "印章编码不能为空")
    private String sealCode;

    @ApiModelProperty(value = "数字证书厂商")
    @NotBlank(message = "数字证书厂商不能为空")
    private String issuer;

    @ApiModelProperty(value = "证书标识")
    @NotBlank(message = "证书标识不能为空")
    private String oid;

    @ApiModelProperty(value = "信任服务号")
//    @NotBlank(message = "信任服务号不能为空")
    private String trustId;

    @ApiModelProperty(value = "证书有效期开始")
    private Date validStart;

    @ApiModelProperty(value = "证书有效期结束")
    private Date validEnd;

    @ApiModelProperty(value = "印章名称")
    @NotBlank(message = "章模名称不能为空。")
    @Length(max = 30,message = "章模名称不能超过20个字符")
    private String pictureName;

    @ApiModelProperty(value = " 章模业务类型：单位：01 行政章、02 财务章等  03  业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    @NotBlank(message = "印章类型不能为空")
    private String pictureBusinessType;

    @ApiModelProperty(value = "印章图片base64")
    @NotBlank(message = "印章图片base64不能为空")
    private String pictureBase64;
}
