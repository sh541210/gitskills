package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: VerifyByCodeRespDTO.java
 * @Description: 验证码查询
 * @Author: yml
 * @CreateDate: 2019/9/10
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/10
 * @Version: 1.0.0
 */
@Data
public class VerifyByCodeRespDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "主题")
    private String theme;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ApiModelProperty(value = "最后签署时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signDate;

}
