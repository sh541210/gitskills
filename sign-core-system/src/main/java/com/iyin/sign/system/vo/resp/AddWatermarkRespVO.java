package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: AddWatermarkRespVO
 * @Description: 增加水印调用结果返回
 * @Author: luwenxiong
 * @CreateDate: 2019/9/2 17:17
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/9/2 17:17
 * @Version: 0.0.1
 */
@Data
public class AddWatermarkRespVO implements Serializable {
    private static final long serialVersionUID = 983429192214437516L;

    @ApiModelProperty(value = "错误码 0-成功")
    private Integer code;

    @ApiModelProperty(value = "FastDFS上的文件路径")
    private String fileID;

    @ApiModelProperty(value = "业务流水号")
    private String bussinessID;

    @ApiModelProperty(value = "结果描述")
    private String msg;
}
