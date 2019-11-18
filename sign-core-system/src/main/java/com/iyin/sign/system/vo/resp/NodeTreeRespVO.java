package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: NodeTreeRespVO
 * @Description: 节点数结构
 * @Author: luwenxiong
 * @CreateDate: 2019/7/16 16:37
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/16 16:37
 * @Version: 0.0.1
 */
@Data
public class NodeTreeRespVO implements Serializable {

    private static final long serialVersionUID = -2423717353148211235L;

    @ApiModelProperty(value = "根节点")
    private NodeRootInfoRespVO rootNode;

    @ApiModelProperty(value = "子节点")
    private List<NodeInfoRespVO>  sonNodeList;
}
