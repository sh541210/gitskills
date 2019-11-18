package com.iyin.sign.system.vo.file.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: ContractUserRespDTO
 * @Description: 合同签署和抄送人
 * @Author: yml
 * @CreateDate: 2019/8/20
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/20
 * @Version: 1.0.0
 */
@Data
public class ContractUserRespDTO implements Serializable {

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户名称")
    private String userNickName;
}
