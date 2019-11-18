package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: SycnUserListReqVO
 * @Description: 同步用户信息—白鹤
 * @Author: yml
 * @CreateDate: 2019/10/9
 * @UpdateUser: yml
 * @UpdateDate: 2019/10/9
 * @Version: 1.0.0
 */
@Data
@ApiModel("同步用户信息List—白鹤")
public class SycnUserListReqVO {

    @ApiModelProperty("用户列表")
    @NotEmpty(message = "用户列表不能为空")
    @Valid
    private List<SycnUserReqVO> userVoList;

}
