package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: NodeInfoRespVO
 * @Description: 根节点信息
 * @Author: luwenxiong
 * @CreateDate: 2019/7/16 16:33
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/16 16:33
 * @Version: 0.0.1
 */
@Data
public class NodeRootInfoRespVO implements Serializable {

    private static final long serialVersionUID = -3603933703384479008L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "父节点ID")
    private String parentNodeId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;
}
