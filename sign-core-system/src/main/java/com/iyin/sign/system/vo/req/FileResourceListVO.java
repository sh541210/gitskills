package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@ApiModel("文件资源实体列表")
@Getter
@Setter
@NoArgsConstructor
public class FileResourceListVO implements Serializable {
	private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "上传资源用户Id列表（user_info表中Id）")
    private List<String> userIds;

    @ApiModelProperty(value = "文件资源名称")
    private String fileName;
}
