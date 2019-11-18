package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName: UploadReqVO
 * @Description: 用印申请单据
 * @Author: wdf
 * @CreateDate: 2019/10/9
 * @UpdateUser: wdf
 * @UpdateDate: 2019/10/9
 * @Version: 1.0.0
 */
@Data
@ApiModel("用印申请单据")
public class UploadUseSealReqVO {

    @ApiModelProperty("申请单据 id")
    @NotBlank(message = "申请单据 id不能为空")
    private String applyId;

    @ApiModelProperty("申请单据编号")
    @NotBlank(message = "申请单据编号不能为空")
    private String applyCode;

    @ApiModelProperty("印章设备地址")
    @NotBlank(message = "印章设备地址不能为空")
    private String mac;

    @ApiModelProperty("申请次数")
    @NotNull(message = "申请次数不能为空")
    private Integer applyCount;

    @ApiModelProperty("失效时间")
    @NotNull(message = "失效时间不能为空")
    private Date expireTime;

    @ApiModelProperty("文件标题")
    private String fileTitle;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("盖章数据回传地址")
    private String postbackaddress;

}
