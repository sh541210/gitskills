package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: PageBaseVo
 * @Description: 分页查询基类
 * @Author: yml
 * @CreateDate: 2019/7/8
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/8
 * @Version: 1.0.0
 */
@Data
public class PageBaseVo implements Serializable {

    @ApiModelProperty("页数")
    @NotNull(message = "页数不能为空")
    private Integer pageSize;

    @ApiModelProperty("页码")
    @NotNull(message = "页码不能为空")
    private Integer currentPage;
}
