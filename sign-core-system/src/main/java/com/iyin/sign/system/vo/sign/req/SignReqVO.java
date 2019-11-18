package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @ClassName: SingleReqVO
 * @Description: 签章请求
 * @Author: yml
 * @CreateDate: 2019/10/29
 * @UpdateUser: yml
 * @UpdateDate: 2019/10/29
 * @Version: 1.0.0
 */
@ApiModel("签章请求")
@Data
public class SignReqVO {

    @ApiModelProperty(value = "签章类型:single单页,batch多页,perforation骑缝", allowableValues = "single,batch,perforation")
    @NotBlank(message = "签章类型不能为空")
    private String reqType;

    @ApiModelProperty("签章文件")
    @NotBlank(message = "签章文件不能为空")
    private String pdfFileID;

    @ApiModelProperty("签章章模")
    @NotBlank(message = "签章章模不能为空")
    private String sealFileID;

    @ApiModelProperty("签章证书")
    @NotBlank(message = "签章证书不能为空")
    private String pfxFileID;

    @ApiModelProperty("证书密码")
    private String pfxPassword;

    @ApiModelProperty("签章页数/起始页")
    @NotBlank(message = "签章页数/起始页不能为空")
    private String page;

    @ApiModelProperty("签章结束页数")
    private String pageEnd;

    @ApiModelProperty("x位移矢量")
    private String x;

    @ApiModelProperty("y位移矢量")
    private String y;

    @ApiModelProperty(value = "签章方向（0：表示左；1：表示右；）",allowableValues = "0,1")
    private String signatureDirection;

    @ApiModelProperty(value = "覆盖页面")
    private String pageSize;
}
