package com.iyin.sign.system.vo.file.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: CompactFieldInfoReqVO.java
 * @Description: 签名域
 * @Author: yml
 * @CreateDate: 2019/8/8
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/8
 * @Version: 1.0.0
 */
@Data
public class CompactFieldInfoReqVO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "签署人Id")
    @NotBlank(message = "签署人Id不能为空")
    private String signatoryId;

    @ApiModelProperty(value = "签署人名称")
    @NotBlank(message = "签署人名称不能为空")
    private String signName;

    @ApiModelProperty(value = "位置类型(01:印章；02:签名；03:日期)")
    @NotBlank(message = "位置类型不能为空")
    private String signType;

    @ApiModelProperty(value = "合同文件编码")
    @NotBlank(message = "合同文件编码不能为空")
    private String compactFileCode;

    @ApiModelProperty(value = "签章方式(01：单页签章；02：多页签章；03：骑缝签章；04:连页签章)")
    @NotBlank(message = "签章方式不能为空")
    @Length(message = "签章方式值不对", min = 2, max = 2)
    private String signatureMethod;

    @ApiModelProperty(value = "签署开始页数")
    @NotNull(message = "签署开始页数不能为空")
    private Integer signatureStartPage;

    @ApiModelProperty(value = "签署结束页数")
    private Integer signatureEndPage;

    @ApiModelProperty(value = "骑缝签时每枚章的覆盖页数")
    private Integer coverPageNum;

    @ApiModelProperty(value = "签署坐标X轴")
    @NotNull(message = "签署坐标X轴不能为空")
    private BigDecimal signatureCoordinateX;

    @ApiModelProperty(value = "签署坐标Y轴")
    @NotNull(message = "签署坐标Y轴不能为空")
    private BigDecimal signatureCoordinateY;

    @ApiModelProperty(value = "覆盖页面")
    private String pageSize;
}
