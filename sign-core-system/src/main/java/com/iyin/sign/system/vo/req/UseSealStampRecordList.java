package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName UseSealStampRecordList
 * UseSealStampRecordList
 * @Author wdf
 * @Date 2019/10/23 17:46
 * @throws
 * @Version 1.0
 **/
@Data
public class UseSealStampRecordList implements Serializable {

    private static final long serialVersionUID = 4626491093284361842L;

    @ApiModelProperty("是否分页")
    @NotNull(message = "是否分页不能为空")
    private Boolean hasPage;

    @ApiModelProperty("当前页码")
    @NotNull(message = "当前页码不能为空")
    private Integer curPage;

    @ApiModelProperty("页容量")
    @NotNull(message = "页容量不能为空")
    private Integer pageSize;
}
