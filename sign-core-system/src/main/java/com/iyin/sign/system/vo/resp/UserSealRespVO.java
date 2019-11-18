package com.iyin.sign.system.vo.resp;

import com.itextpdf.text.pdf.PRAcroForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UserSealRespVO
 * @Description: 用户印章
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 16:40
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 16:40
 * @Version: 0.0.1
 */
@Data
public class UserSealRespVO implements Serializable {

    @ApiModelProperty(value = "印章ID")
    private String sealId;

    @ApiModelProperty(value = "印章名称")
    private String sealName;

    @ApiModelProperty(value = "章模业务类型：章模业务类型：单位：01 行政章、02 财务章等  03  业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    private String pictureBusinessType;

    @ApiModelProperty(value = "印章编码")
    private String sealCode;

    @ApiModelProperty(value = "介质类型 01 云印章 02 ukey印章")
    private String mediumType;
}
