package com.iyin.sign.system.vo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@ApiModel("文件上传输出")
@Getter
@Setter
@NoArgsConstructor
public class FileUploadRespDto implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("文件资源实体")
	private FileResourceDto file;

}
