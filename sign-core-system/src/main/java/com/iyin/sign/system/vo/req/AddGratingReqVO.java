package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: AddGratingReqVO.java
 * @Description: 增加光栅参数
 * @Author: yml
 * @CreateDate: 2019/9/23
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/23
 * @Version: 1.0.0
 */
@Data
public class AddGratingReqVO implements Serializable {

    private static final long serialVersionUID = 4995412316605282181L;

    @ApiModelProperty(value = "业务流水号")
    @NotBlank(message = "业务流水号不能为空")
    private String bussinessID;

    @ApiModelProperty(value = "请求类型 固定参数,为gratingPdf")
    @NotBlank(message = "请求类型不能为空")
    private String reqType;

    @ApiModelProperty(value = "FastDFS上的文件路径")
    @NotBlank(message = "文件ID不能为空")
    private String srcFileID;

    @ApiModelProperty(value = "需要添加光栅的文字，不支持中文")
    @NotBlank(message = "需要添加光栅的文字不能为空")
    private String text;

    @ApiModelProperty(value = "光栅图片在pdf中的原点x坐标，坐标原点为PDF左上角")
    @NotBlank(message = "光栅图片在pdf中的原点x坐标不能为空")
    private Integer x;

    @ApiModelProperty(value = "光栅图片在pdf中的原点y坐标，坐标原点为PDF左上角")
    @NotBlank(message = "光栅图片在pdf中的原点y坐标不能为空")
    private Integer y;

    @ApiModelProperty(value = "生成pdf所使用的图片精度默认为150")
    private Integer imageDpi;
}
