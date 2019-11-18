package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: UserListReqVO
 * @Description: 查询分组下的用户列表
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 16:49
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 16:49
 * @Version: 0.0.1
 */
@Data
public class UserListReqVO implements Serializable {


    @ApiModelProperty(value = "节点ID")
    @NotBlank(message = "分组ID不能为空")
    private String nodeId;


    @ApiModelProperty(value = "用户名")
    private String userNickName;

    @ApiModelProperty(value = "0:正常 1：禁用")
    private String isForbid;


    @ApiModelProperty(value = "登录账号")
    private String userName;

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;

    @NotNull(message = "currentPage不能为空")
    private Integer currentPage;
}
