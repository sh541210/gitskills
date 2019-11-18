package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: UserSignLogListReqVO
 * @Description: 用户签署日志查询请求VO
 * @Author: luwenxiong
 * @CreateDate: 2019/7/12 17:21
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/12 17:21
 * @Version: 0.0.1
 */
@Data
public class UserSignLogListReqVO implements Serializable {

    private static final long serialVersionUID = 4626491093284361842L;

    @ApiModelProperty(value = "文档名称")
    private String fileName;

    @ApiModelProperty(value = "印章编码")
    private String sealCode;

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;

    @NotNull(message = "currentPage不能为空")
    private Integer currentPage;

    @NotBlank(message = "用户ID不能为空")
    private String userId;
}
