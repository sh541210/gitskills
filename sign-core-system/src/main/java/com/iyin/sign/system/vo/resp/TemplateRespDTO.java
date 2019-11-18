package com.iyin.sign.system.vo.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.iyin.sign.system.vo.req.AddTemplateReqVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: TemplateRespDTO
 * @Description: 模板详情返回
 * @Author: yml
 * @CreateDate: 2019/9/26
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/26
 * @Version: 1.0.0
 */
@Data
@ApiModel("模板详情返回")
public class TemplateRespDTO implements Serializable {

    @ApiModelProperty("模板控件")
    @Valid
    private List<AddTemplateReqVo.TemplateFileds> templateFiledsList;

    /**
     * 模板编号
     */
    @ApiModelProperty(value = "模板编号")
    private String id;

    /**
     * 模板名称
     */
    @TableField("temp_name")
    @ApiModelProperty(value = "模板名称")
    private String tempName;

    /**
     * 模板用途
     */
    @TableField("temp_purpose")
    @ApiModelProperty(value = "模板用途")
    private String tempPurpose;

    /**
     * 模板状态
     */
    @TableField("temp_status")
    @ApiModelProperty(value = "模板状态")
    private Integer tempStatus;

    /**
     * 模板内容
     */
    @TableField("temp_html")
    @ApiModelProperty(value = "模板内容")
    private String tempHtml;

    /**
     * 模板类型
     */
    @TableField("temp_type")
    @ApiModelProperty(value = "模板类型")
    private String tempType;
}
