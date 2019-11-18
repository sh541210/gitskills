package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * @ClassName: UpdateNodeReqVO
 * @Description: 编辑节点
 * @Author: luwenxiong
 * @CreateDate: 2019/7/16 12:01
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/16 12:01
 * @Version: 0.0.1
 */
@Data
public class UpdateNodeReqVO implements Serializable {
    private static final long serialVersionUID = 1874019151571732848L;

    @ApiModelProperty(value = "节点Id不能为空")
    private String nodeId;

    @ApiModelProperty(value = "节点名称不能为空")
    private String nodeName;
}
