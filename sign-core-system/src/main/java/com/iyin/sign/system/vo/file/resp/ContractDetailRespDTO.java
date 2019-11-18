package com.iyin.sign.system.vo.file.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iyin.sign.system.entity.SignSysCompactSignatory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: ContractDetailRespDTO
 * @Description: 合同详情返回
 * @Author: yml
 * @CreateDate: 2019/8/15
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/15
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractDetailRespDTO extends ContractFileRespDTO implements Serializable {

    @ApiModelProperty("合同状态 01：草稿；02：已撤销；03：已拒签；0401：待我签署 0402 待他人签；05：签署完成；06:已过期")
    private String contractStatus;

    @ApiModelProperty("发起时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date compactStartDate;

    @ApiModelProperty("截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date signDeadline;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "撤销原因")
    private String revocationRemake;

    @ApiModelProperty(value = "拒签原因")
    private String refuseSignRemake;

    @ApiModelProperty("签署状态")
    private List<SignSysCompactSignatory> signStatusInfo;

    @ApiModelProperty("操作记录")
    private List<CompactLogRespDto> signSysCompactLogs;

    @ApiModelProperty("是否删除或撤销")
    private Boolean deleteOrRevoke;

    @ApiModelProperty("合同发起人ID")
    private String sponsorId;

    @ApiModelProperty(value = "验证码")
    private String verificationCode;

    @ApiModelProperty(value = "二维码")
    private String qrCode;

    @ApiModelProperty(value = "验证码有效期")
    private String verificationDate;

}
