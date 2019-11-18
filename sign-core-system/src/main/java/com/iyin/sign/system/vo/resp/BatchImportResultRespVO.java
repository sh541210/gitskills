package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: BatchImportResultRespVO
 * @Description: 批量导入单位的结构
 * @Author: luwenxiong
 * @CreateDate: 2019/7/26 10:53
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/26 10:53
 * @Version: 0.0.1
 */
@Data
public class BatchImportResultRespVO implements Serializable {

    @ApiModelProperty(value = "导入ID，方便后面下载用")
    private String importId;

    @ApiModelProperty(value = "成功条数")
    private   int success_num;

    @ApiModelProperty(value = "失败条数")
    private  int fail_num;

    @ApiModelProperty(value = "描述")
    private String err_msg;
}
