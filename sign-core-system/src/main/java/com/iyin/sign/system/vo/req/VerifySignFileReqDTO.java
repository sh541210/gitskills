package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: VerifySignFileReqDTO
 * @Description: 验真加签文件请求DTO
 * @Author: 唐德繁
 * @CreateDate: 2018/12/13 0013 下午 3:06
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/12/13 0013 下午 3:06
 * @Version: 0.0.1
 */
@Data
public class VerifySignFileReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "加签文件Base64")
    @NotBlank(message = "加签文件Base64不能为空")
    private String signFileStr;
}
