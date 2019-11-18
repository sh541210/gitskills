package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName SealRecordVO
 * SealRecordVO
 * @Author wdf
 * @Date 2019/10/25 14:16
 * @throws
 * @Version 1.0
 **/
@NoArgsConstructor
@Data
public class SealRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "盖章 id")
    private String id;
    @ApiModelProperty(value = "盖章流水号")
    private String flowNumber;
    @ApiModelProperty(value = "盖章时间")
    private Long stampTime;
    @ApiModelProperty(value = "经度")
    private Double longitude;
    @ApiModelProperty(value = "纬度")
    private Double latitude;
    @ApiModelProperty(value = "盖章地址")
    private String address;
}
