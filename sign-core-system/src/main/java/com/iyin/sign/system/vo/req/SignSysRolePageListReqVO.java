package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: SignSysRolePageListReqVO
 * @Description: 分页查询角色列表
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 17:08
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 17:08
 * @Version: 0.0.1
 */
@Data
public class SignSysRolePageListReqVO implements Serializable {

    @ApiModelProperty(value = "当前页")
    @NotNull(message = "当前页不能为空")
    private Integer currentPage;

    @ApiModelProperty(value = "每页展示的条数")
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;


}
