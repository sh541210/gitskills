package com.iyin.sign.system.vo.file.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: ContractQueryRespDTO
 * @Description: 合同管理查询返回
 * @Author: yml
 * @CreateDate: 2019/8/8
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/8
 * @Version: 1.0.0
 */
@Data
@ApiModel("合同管理查询返回")
public class ContractQueryRespDTO implements Serializable {

    @ApiModelProperty("合同主题")
    private String compactTheme;

    @ApiModelProperty("合同主键")
    private String compactId;

    @ApiModelProperty("发起方")
    private String sponsor;

    @ApiModelProperty("发起方用户ID")
    private String sponsorId;

    @ApiModelProperty("签署人")
    private String signer;

    @ApiModelProperty("发起时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startingTime;

    @ApiModelProperty("合同状态 01：草稿；02：已撤销；03：已拒签；0401：待我签署 0402 待他人签；05：签署完成；06:已过期")
    private String contractStatus;

    @ApiModelProperty(value = "验证码")
    private String verificationCode;

    @ApiModelProperty(value = "二维码")
    private String qrCode;

    @ApiModelProperty(value = "验证码有效期")
    private String verificationDate;
}
