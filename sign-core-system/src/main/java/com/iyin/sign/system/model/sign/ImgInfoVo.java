package com.iyin.sign.system.model.sign;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
	* @Description 文件转图片后 数据
	* @Author: wdf 
    * @CreateDate: 2019/3/5 16:53
	* @UpdateUser: wdf
    * @UpdateDate: 2019/3/5 16:53
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="文件转图片", description="文件转图片")
public class ImgInfoVo {
    @ApiModelProperty(name = "图片链接(code)")
    @JsonIgnore
    private String pcLink;
    @ApiModelProperty(name = "图片(code)")
    private String fileCode;
    @ApiModelProperty(name = "页码")
    private int pageNo;


}
