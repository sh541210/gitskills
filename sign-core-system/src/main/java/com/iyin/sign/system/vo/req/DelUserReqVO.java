package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName: DelUserReqVO
 * @Description: DelUserReqVO
 * @Author: luwenxiong
 * @CreateDate: 2019/9/4 16:57
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/9/4 16:57
 * @Version: 0.0.1
 */
@Data
public class DelUserReqVO implements Serializable {

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

}
