package com.iyin.sign.system.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @Description 文件基本信息DTO
 * @Author: wdf
 * @CreateDate: 2019/1/28 17:29
 * @UpdateUser: wdf
 * @UpdateDate: 2019/1/28 17:29
 * @Version: 0.0.1
 * @param
 * @return
 */
@ApiModel("base64文件上传")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileBaseDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户id")
	@NotBlank(message = "用户id不能为空")
	private String userId;

	@ApiModelProperty("文件base64")
	@NotBlank(message = "文件base64不能为空")
	private String baseFile;

}
