package com.iyin.sign.system.vo.file.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: CompactFieldInfoRespDTO.java
 * @Description: 名域
 * @Author: yml
 * @CreateDate: 2019/8/8
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/8
 * @Version: 1.0.0
 */
@Data
public class CompactFieldInfoRespDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "签署人Id")
    private String signatoryId;

    @ApiModelProperty(value = "签署人")
    private String signName;

    @ApiModelProperty(value = "合同文件编码")
    private String compactFileCode;

    @ApiModelProperty(value = "签章方式(01：单页签章；02：多页签章；03：骑缝签章；)")
    private String signatureMethod;

    @ApiModelProperty(value = "签署开始页数")
    private Integer signatureStartPage;

    @ApiModelProperty(value = "签署结束页数")
    private Integer signatureEndPage;

    @ApiModelProperty(value = "签署坐标X轴")
    private BigDecimal signatureCoordinateX;

    @ApiModelProperty(value = "签署坐标Y轴")
    private BigDecimal signatureCoordinateY;

}
