package com.iyin.sign.system.vo.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 文件打印记录表
 * </p>
 *
 * @author wdf
 * @since 2019-07-19
 */
@Data
@Accessors(chain = true)
@ApiModel(value ="添加文件打印记录")
public class SignSysFilePrintReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 1后台用户2前台用户
     */
    @ApiModelProperty(value = "1后台用户2前台用户")
    @JsonIgnore
    private Integer userChannel;

    /**
     * 文件code
     */
    @ApiModelProperty(value = "文件code")
    @NotBlank(message = "文件code不能为空")
    private String fileCode;
}
