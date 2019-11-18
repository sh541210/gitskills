package com.iyin.sign.system.vo.file.req;

import com.iyin.sign.system.vo.sign.req.UKeySignReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: SignContractReqVO
 * @Description: 签署合同请求信息
 * @Author: yml
 * @CreateDate: 2019/8/15
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/15
 * @Version: 1.0.0
 */
@Data
public class SignContractReqVO {

    @Valid
    @ApiModelProperty("签署信息")
    private List<FileManageSignReqVO> signReqVOS;

    @NotBlank(message = "合同ID不能为空")
    @ApiModelProperty("合同ID")
    private String contractId;

    @ApiModelProperty(value = "用户ID", hidden = true)
    private String userId;

    @ApiModelProperty("合同文件Ukey签署参数")
    @Valid
    private List<UKeySignReqVO> uKeySignReqVOS;

}
