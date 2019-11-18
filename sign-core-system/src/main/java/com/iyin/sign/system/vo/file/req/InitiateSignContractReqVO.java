package com.iyin.sign.system.vo.file.req;

import com.iyin.sign.system.vo.sign.req.UKeySignReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: InitiateSignContractReqVO
 * @Description: 发起并签署
 * @Author: yml
 * @CreateDate: 2019/8/15
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/15
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InitiateSignContractReqVO extends InitiateContractReqVO {

    @ApiModelProperty("签章信息")
    @Valid
    private List<FileManageSignReqVO> fileManageSignReqVOS;

    @ApiModelProperty("合同文件Ukey签署参数")
    @Valid
    private List<UKeySignReqVO> uKeySignReqVOS;
}