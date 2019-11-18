package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName FileResourceInfoVO
 * FileResourceInfoVO
 * @Author wdf
 * @Date 2019/7/24 11:13
 * @throws
 * @Version 1.0
 **/
@NoArgsConstructor
@Data
public class SealPrivateRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "人员Id")
    @JsonIgnore
    private String userId;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    @JsonIgnore
    private String departmentId;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用 true启用 false禁用")
    private Boolean enableFlag;

    @ApiModelProperty(value = "印章 id")
    private String id;
    @ApiModelProperty(value = "印章设备地址")
    private String mac;
    /**
     * 印章名称
     */
    @ApiModelProperty(value = "印章名称")
    private String name;
    @ApiModelProperty(value = "印章编号")
    private String sealNo;
    @ApiModelProperty(value = "使用范围")
    private String scope;
    @ApiModelProperty(value = "印模")
    private String sealPrint;
    @ApiModelProperty(value = "服务费过期时间，采用世纪秒")
    private long serviceTime;
    @ApiModelProperty(value = "是否过期")
    private Object hasExpired;
    @ApiModelProperty(value = "服务费类型，0：终身，1：按时间")
    private int serviceType;
    @ApiModelProperty(value = "通讯协议版本")
    private String dataProtocolVersion;
    @ApiModelProperty(value = "部门id")
    private String orgStructrueId;
    @ApiModelProperty(value = "是否允许跨部门申请")
    private boolean crossDepartmentApply;
    @ApiModelProperty(value = "是否启用电子围栏")
    private boolean enableEnclosure;
    @ApiModelProperty(value = "电子围栏信息")
    @JsonIgnore
    private SealPrivateRespVO.SealEnclosureBean sealEnclosure;
    @ApiModelProperty(value = "是否启用审批流")
    private boolean enableApprove;
    @ApiModelProperty(value = "审批流列表")
    @JsonIgnore
    private Object sealApproveFlowList;

    @NoArgsConstructor
    @Data
    public static class SealEnclosureBean {

        @ApiModelProperty(value = "电子围栏 id")
        private String id;
        @ApiModelProperty(value = "印章 id")
        private String sealId;
        @ApiModelProperty(value = "经度")
        private double longitude;
        @ApiModelProperty(value = "纬度")
        private double latitude;
        @ApiModelProperty(value = "方圆范围，单位：米")
        private int scope;
        @ApiModelProperty(value = "地址")
        private String address;
        @ApiModelProperty(value = "是否启用电子围栏")
        private Object enableFlag;
    }

}
