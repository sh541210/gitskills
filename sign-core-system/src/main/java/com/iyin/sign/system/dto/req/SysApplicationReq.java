package com.iyin.sign.system.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description 系统应用请求信息
 * @Author: wdf
 * @CreateDate: 2019/1/28 17:29
 * @UpdateUser: wdf
 * @UpdateDate: 2019/1/28 17:29
 * @Version: 0.0.1
 * @param
 * @return
 */
@Data
public class SysApplicationReq implements Serializable{

    @ApiModelProperty("应用名称")
    @NotBlank(message = "应用名称不能为空")
    @Size(max = 30)
    private String applicationName;
    @ApiModelProperty("应用描述")
    @Size(max = 300)
    private String applicationDesc;
}
