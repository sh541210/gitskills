package com.iyin.sign.system.model.sign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: MultiParam
 * @Description:
 * @Author: yml
 * @CreateDate: 2019/5/20
 * @UpdateUser: yml
 * @UpdateDate: 2019/5/20
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultiParam extends CoordinateParam implements Serializable {
    @ApiModelProperty("开始页")
    @NotNull(message = "开始页不能为空")
    private int pageNum;
    @ApiModelProperty("结束页")
    private int pageNumEnd;
    @ApiModelProperty("pdf文件宽度")
    @NotNull(message = "pdf文件宽度不能为空")
    private float pdfWidth;
    @ApiModelProperty("是否雾化")
    @NotNull(message = "是否雾化不能为空")
    private boolean foggy;
    @ApiModelProperty("是否脱密")
    @NotNull(message = "是否脱密不能为空")
    private boolean grey;
}
