package com.iyin.sign.system.vo.file.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: InitiateContractReqVO
 * @Description: 发起合同请求参数
 * @Author: yml
 * @CreateDate: 2019/8/14
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/14
 * @Version: 1.0.0
 */
@Data
public class InitiateContractReqVO implements Serializable{

    @ApiModelProperty(value = "用户ID",hidden = true)
    private String userId;

    @ApiModelProperty(value = "是否签署",hidden = true)
    private Boolean isStart;

    @ApiModelProperty(value = "是否单独签署二次",hidden = true)
    private Boolean isIndividually;

    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @ApiModelProperty(value = "是否删除合同ID(no:不删除，其他删除)",hidden = true)
    private String deleteFlag;

    @ApiModelProperty(value = "主题")
    @NotBlank(message = "主题不能为空")
    private String compactTheme;

    @ApiModelProperty(value = "合同有效期")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date validityEndDate;

    @ApiModelProperty(value = "签署截止日期")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date signDeadline;

    @ApiModelProperty(value = "是否指定签署位置（0：未指定签署位置；1：指定签署位置；）")
    @NotNull(message = "是否指定签署位置不能为空")
    private Byte isSiteSign;

    @ApiModelProperty(value = "所属组织Id")
    @NotBlank(message = "所属组织ID不能为空")
    private String orgId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("合同文件")
    @NotEmpty(message = "合同文件不能为空")
    @Valid
    private List<ContractFile> contractFileList;

    @ApiModelProperty(value = "签署信息")
    @NotEmpty(message = "签署信息不能为空")
    @Valid
    private List<InitiateContractReqVO.SignInfo> signInfos;

    @ApiModelProperty(value = "签署对象（01：我需要签署；02：仅需他人签署；）")
    @NotBlank(message = "签署对象不能为空")
    @Length(message = "签署对象值不对", min = 2, max = 2)
    private String signatoryWay;

    @ApiModelProperty(value = "签署方式（01：无序签；02：顺序签署；03：每人单独签署；）")
    @NotBlank(message = "签署方式不能为空")
    @Length(message = "签署方式值不对", min = 2, max = 2)
    private String signWay;

    @ApiModelProperty(value = "类型（01:文件管理，02:合同）")
    @NotBlank(message = "类型不能为空")
    @Length(message = "类型值不对", min = 2, max = 2)
    private String type;

    @ApiModelProperty("签名域信息集")
    @NotNull(message = "签名域信息集不为null")
    @Valid
    private List<CompactFieldInfoReqVO> compactFieldInfoReqVOList;

    @Data
    public static class SignInfo implements Serializable {
        @ApiModelProperty(value = "签署人的联系方式(手机/邮箱)")
        @NotBlank(message = "签署人的联系方式不能为空")
        private String signContact;

        @ApiModelProperty(value = "签署人名称")
        @NotBlank(message = "签署人名称不能为空")
        private String signName;

        @ApiModelProperty(value = "签署人Id（sign_sys_user_info表中Id）")
        private String signatoryId;

        @ApiModelProperty(value = "签署方式：01:签署；02：抄送")
        @NotBlank(message = "签署方式不能为空")
        @Length(message = "签署方式值不对", min = 2, max = 2)
        private String type;

    }

    @Data
    public static class ContractFile implements Serializable{
        @ApiModelProperty(value = "合同文件编码")
        @NotBlank(message = "文件编码不能为空")
        private String fileCode;

        @ApiModelProperty(value = "合同原文件编码",hidden = true)
        @JsonIgnore
        private String fileCodeOrigin;

        @ApiModelProperty(value = "文件名称")
        private String fileName;

        @ApiModelProperty(value = "合同文档类型（01：合同文件；02：合同附件；）")
        @NotBlank(message = "合同文档类型不能为空")
        private String fileType;

        @ApiModelProperty(value = "合同文件页数")
        @NotNull(message = "文件页数不能为空")
        private Integer pageTotal;
    }

}
