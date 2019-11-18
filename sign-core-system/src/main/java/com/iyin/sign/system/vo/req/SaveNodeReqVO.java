package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: SaveNodeReqVO
 * @Description: 添加节点请求VO
 * @Author: luwenxiong
 * @CreateDate: 2019/7/16 11:22
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/16 11:22
 * @Version: 0.0.1
 */
@Data
public class SaveNodeReqVO implements Serializable {

    @ApiModelProperty(value = "父节点ID")
    @NotBlank(message = "父节点ID不能为空")
    private String parentNodeId;

    @ApiModelProperty(value = "节点名称")
    @NotBlank(message = "节点名称不能为空")
    private String nodeName;
}
