package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName StampRecordRespVO
 * StampRecordRespVO
 * @Author wdf
 * @Date 2019/10/23 17:51
 * @throws
 * @Version 1.0
 **/
@NoArgsConstructor
@Data
public class StampRecordRespVO implements Serializable {

    private static final long serialVersionUID = 4927043305538625067L;

    @ApiModelProperty("盖章数据 id")
    private String id;
    @ApiModelProperty("盖章人 id")
    private String stampUser;
    @ApiModelProperty("盖章人名称")
    private String stampUserName;
    @ApiModelProperty("印章 id")
    private String sealId;
    @ApiModelProperty("印章名称")
    private String sealName;
    @ApiModelProperty("盖章流水号")
    private String flowNumber;
    @ApiModelProperty("盖章时间")
    private long stampTime;
    @ApiModelProperty("经度")
    private double longitude;
    @ApiModelProperty("纬度")
    private double latitude;
    @ApiModelProperty("盖章地址")
    private String address;
}
