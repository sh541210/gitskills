package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: EnterprsieBatchImportRespVO
 * @Description:  批量导入单位结构返回
 * @Author: luwenxiong
 * @CreateDate: 2019/7/26 11:03
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/26 11:03
 * @Version: 0.0.1
 */
@Data
public class EnterprsieBatchImportRespVO implements Serializable {

    private static final long serialVersionUID = -7499516449898229151L;

    @ApiModelProperty(value = "下载导入结果的ID")
    private String importId;

    @ApiModelProperty(value = "导入结果描述")
    private String desc;
}
