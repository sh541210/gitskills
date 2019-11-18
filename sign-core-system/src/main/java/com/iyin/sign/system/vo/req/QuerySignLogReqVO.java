package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: QuerySignLogReqVO
 * @Description: 查询文档签署日志
 * @Author: yml
 * @CreateDate: 2019/7/8
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/8
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuerySignLogReqVO extends PageBaseVo implements Serializable {

    @ApiModelProperty("文档编码")
    @NotBlank(message = "文档编码不能为空")
    private String fileCode;
}
