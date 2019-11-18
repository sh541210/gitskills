package com.iyin.sign.system.vo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@ApiModel("base64文件上传")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileBaseVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户id")
	@NotBlank(message = "用户id不能为空")
	private String userId;

	@ApiModelProperty("文件base64")
	@NotBlank(message = "文件base64不能为空")
	private String baseFile;

}
