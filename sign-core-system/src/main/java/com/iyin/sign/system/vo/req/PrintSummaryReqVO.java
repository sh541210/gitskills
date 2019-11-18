package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: PrintSummaryReqVO
 * @Description: 打印摘要请求
 * @Author: yml
 * @CreateDate: 2019/9/23
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/23
 * @Version: 1.0.0
 */
@Data
@ApiModel("打印摘要")
public class PrintSummaryReqVO {

    @ApiModelProperty("打印摘要内容")
    private String context;

    @ApiModelProperty(value = "主题",hidden = true)
    private String theme;

    @ApiModelProperty("光栅")
    private Boolean grating;

    @ApiModelProperty("水印")
    private Boolean watermark;

    @ApiModelProperty("雾化")
    private Boolean atomization;

    @ApiModelProperty("脱密")
    private Boolean graying;

    @ApiModelProperty("原文")
    private Boolean origin;

    @ApiModelProperty("打印摘要文件编码")
    @NotEmpty(message = "打印摘要文件编码不能为空")
    private List<String> fileCodes;
}
