package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: EnterpriseBatchImportRespVO
 * @Description: 单位批量导入结果返回
 * @Author: luwenxiong
 * @CreateDate: 2019/7/17 17:15
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/17 17:15
 * @Version: 0.0.1
 */
@Data
public class EnterpriseBatchImportRespVO implements Serializable {

    private static final long serialVersionUID = -8906834001628443372L;

    @ApiModelProperty(value = "成功导入数量")
    private Integer successNum;

    @ApiModelProperty(value = "失败数量")
    private Integer failNum;

    @ApiModelProperty(value = "错误信息提示")
    private List<String> errMsgs;
}
