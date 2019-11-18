package com.iyin.sign.system.vo.file.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FileManageInfoReqVO
 * @Description: 发起请求参数
 * @Author: yml
 * @CreateDate: 2019/8/7
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
@Data
public class FileManageInfoReqVO implements Serializable{

    @ApiModelProperty(value = "主题")
    @NotBlank(message = "主题不能为空")
    private String compactTheme;

    @ApiModelProperty(value = "所属组织Id")
    @NotBlank(message = "所属组织ID不能为空")
    private String orgId;

    @ApiModelProperty(value = "合同文件编码")
    @NotBlank(message = "文件编码不能为空")
    private String fileCode;

    @ApiModelProperty(value = "合同文件页数")
    @NotNull(message = "文件页数不能为空")
    private Integer filePage;

    @ApiModelProperty(value = "签署信息")
    @NotEmpty(message = "签署信息不能为空")
    @Valid
    private List<SignInfo> signInfos;

    @ApiModelProperty(value = "签署方式（01：无序签；02：顺序签署）")
    @NotBlank(message = "签署方式不能为空")
    @Length(message = "签署方式值不对", min = 2, max = 2)
    private String signWay;

    @ApiModelProperty(value = "签署人方式（01：我需要签署；02：需要其他人签署；）")
    @NotBlank(message = "签署人方式不能为空")
    @Length(message = "签署人方式值不对", min = 2, max = 2)
    private String signatoryWay;

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty(value = "签署人(自签)")
    private String signName;

    @ApiModelProperty(value = "签署人的联系方式联系方式(自签)")
    private String signContact;

    @ApiModelProperty(value = "类型（01:文件管理，02:合同）")
    @NotBlank(message = "类型不能为空")
    @Length(message = "类型值不对", min = 2, max = 2)
    private String type;

    @Data
    public static class SignInfo implements Serializable {
        @ApiModelProperty(value = "签署人的联系方式(手机/邮箱)")
        @NotBlank(message = "签署人的联系方式不能为空")
        private String signContact;

        @ApiModelProperty(value = "签署人名称")
        @NotBlank(message = "签署人名称不能为空")
        private String signName;

        @ApiModelProperty(value = "签署人Id（sign_sys_user_info表中Id）")
        @NotBlank(message = "签署人Id不能为空")
        private String signatoryId;

        @ApiModelProperty(value = "签署方式：01:签署；02：抄送")
        @NotBlank(message = "签署方式不能为空")
        @Length(message = "签署方式值不对", min = 2, max = 2)
        private String type;

    }
}
