package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysAdminUserBaseInfoRespVO
 * @Description: 管理员基本信息
 * @Author: luwenxiong
 * @CreateDate: 2019/8/9 9:41
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/9 9:41
 * @Version: 0.0.1
 */
@Data
public class SignSysAdminUserBaseInfoRespVO implements Serializable {

    private static final long serialVersionUID = -7226266995200924344L;

    @ApiModelProperty(value = "管理员用户ID")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
