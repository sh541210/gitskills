package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
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
@NoArgsConstructor
@Data
public class StampRecordApplyListReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否分页")
    @NotNull(message = "是否分页不能为空")
    private Boolean hasPage;

    @ApiModelProperty("当前页码")
    @NotNull(message = "当前页码不能为空")
    private Integer curPage;

    @ApiModelProperty("页容量")
    @NotNull(message = "页容量不能为空")
    @Range(min = 1,max = 1000,message = "页容量超出范围")
    private Integer pageSize;

    @ApiModelProperty("过滤参数")
    @Valid
    private ParamBean param;

    @NoArgsConstructor
    @Data
    public static class ParamBean {

        @ApiModelProperty("申请人")
        private String applyUser;
        @ApiModelProperty("申请单结束时间")
        private Long endTime;
        @ApiModelProperty(value = "页容量,无需赋值true", hidden = true)
        private Boolean isAdmin;
        @ApiModelProperty("印章id")
        private String sealId;
        @ApiModelProperty("搜索内容")
        private String searchContent;
        @ApiModelProperty("搜索类型，1：按照申请事由，2：按照盖章流水号")
        @Range(min = 1,max = 2,message = "搜索类型超出范围")
        private Integer searchType;
        @ApiModelProperty(value = "类型，无需赋值0", hidden = true)
        private Integer stampType;
        @ApiModelProperty("申请单开始时间")
        private Long startTime;
    }
}
