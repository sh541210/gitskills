package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

@ApiModel("文件打包下载请求参数")
@Getter
@Setter
@NoArgsConstructor
public class DocPackageReqDto implements Serializable {
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
	@NotEmpty(message="用户id不允许为空")
	private String userId;
    @NotEmpty(message="资源id集合不允许为空")
    @ApiModelProperty(value = "资源id集合")
	private List<String> resourceIds;
}
