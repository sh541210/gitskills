package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: SignLogListReqVO
 * @Description: 分页查询单位签章日志
 * @Author: luwenxiong
 * @CreateDate: 2019/6/26 17:47
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/26 17:47
 * @Version: 0.0.1
 */
@Data
public class SignLogListReqVO implements Serializable {

    private static final long serialVersionUID = 4626491093284361842L;

    @ApiModelProperty(value = "文档名称")
    private String fileName;

    @ApiModelProperty(value = "印章编码")
    private String sealCode;

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;

    @NotNull(message = "currentPage不能为空")
    private Integer currentPage;

    @NotBlank(message = "单位ID不能为空")
    private String enterpriseId;

}
