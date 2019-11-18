package com.iyin.sign.system.model.sign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ImgInfoListDTO
 * ImgInfoListDTO
 * @Author wdf
 * @Date 2019/3/5 17:04
 * @throws
 * @Version 1.0
 **/
@ApiModel("文件上传输出")
@Data
@NoArgsConstructor
public class ImgInfoListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片信息集合")
    private List<ImgInfoVo> imgInfoList;

    @ApiModelProperty("文件总页码")
    private Integer totalPageNo;

    @ApiModelProperty("文件高度")
    private Integer height;

    @ApiModelProperty("文件宽度")
    private Integer width;
}
