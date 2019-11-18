package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: AddWatermarkReqVO
 * @Description: 增加水印参数
 * @Author: luwenxiong
 * @CreateDate: 2019/9/2 17:11
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/9/2 17:11
 * @Version: 0.0.1
 */
@Data
public class AddWatermarkReqVO implements Serializable {

    private static final long serialVersionUID = 4995412316605282181L;

    @ApiModelProperty(value = "业务流水号")
    @NotBlank(message = "业务流水号不能为空")
    private String bussinessID;

    @ApiModelProperty(value = "请求类型 固定参数,为addWatermark")
    @NotBlank(message = "请求类型不能为空")
    private String reqType;

    @ApiModelProperty(value = "FastDFS上的文件路径")
    @NotBlank(message = "文件ID不能为空")
    private String srcFileID;

    @ApiModelProperty(value = "需要添加水印的文字，不支持中文")
    @NotBlank(message = "需要添加水印的文字不能为空")
    private String text;

    @ApiModelProperty(value = "水印算法类型 0 -- 表示嵌入打印扫描可提取的水印 1 -- 表示嵌入手机拍照可提取的水印")
    @NotBlank(message = "水印算法不能为空")
    private String algFlag;
}
