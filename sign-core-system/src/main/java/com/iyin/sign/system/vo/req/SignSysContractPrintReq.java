package com.iyin.sign.system.vo.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: SignSysContractPrintReq.java
 * @Description: 合同文件打印记录表
 * @Author: yml
 * @CreateDate: 2019/9/3
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/3
 * @Version: 1.0.0
 */
@Data
@Accessors(chain = true)
@ApiModel(value ="合同文件打印记录表")
public class SignSysContractPrintReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 1后台用户2前台用户
     */
    @ApiModelProperty(value = "1后台用户2前台用户")
    @JsonIgnore
    private Integer userChannel;

    /**
     * 文件code
     */
    @ApiModelProperty(value = "文件code")
    @NotBlank(message = "文件code不能为空")
    private String fileCode;

    @ApiModelProperty(value = "合同ID")
    @NotBlank(message = "合同ID不能为空")
    private String contractId;
}
