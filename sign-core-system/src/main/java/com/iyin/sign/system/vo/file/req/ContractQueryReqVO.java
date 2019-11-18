package com.iyin.sign.system.vo.file.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName: ContractQueryReqVO
 * @Description: 文件管理查询返回
 * @Author: yml
 * @CreateDate: 2019/8/13
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/13
 * @Version: 1.0.0
 */
@Data
public class ContractQueryReqVO {

    @ApiModelProperty("搜索词")
    private String content;

    @ApiModelProperty("当前页")
    @NotNull(message = "当前页不能为空")
    private long pageNum;

    @ApiModelProperty("页数")
    @NotNull(message = "页数不能为空")
    private long pageSize;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date timeBefore;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date timeAfter;

    @ApiModelProperty("合同状态 01：草稿；02：已撤销；03：已拒签；0401：待我签署 0402 待他人签；05：签署完成；06:已过期")
    private String status;

    @ApiModelProperty(value = "用户ID",hidden = true)
    private String userId;

    @ApiModelProperty(value = "所属组织Id")
    @NotBlank(message = "所属组织ID不能为空")
    private String orgId;
}