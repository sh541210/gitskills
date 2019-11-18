package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.xmlbeans.impl.jam.mutable.MElement;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: UpdateUkSealReqVO
 * @Description: 编辑ukey印章
 * @Author: luwenxiong
 * @CreateDate: 2019/7/22 16:11
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/22 16:11
 * @Version: 0.0.1
 */
@Data
public class UpdateUkSealReqVO implements Serializable {

    @ApiModelProperty(value = "印章ID")
    @NotBlank(message = "印章ID不能为空")
    private String sealId;

    @ApiModelProperty(value = "印章名称")
    @NotBlank(message = "印章名称不能为空")
    private String pictureName;

    @ApiModelProperty(value = " 章模业务类型：单位：01 行政章、02 财务章等  03  业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    @NotBlank(message = "印章业务类型不能为空")
    private String pictureBusinessType;
}
