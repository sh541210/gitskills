package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iyin.sign.system.entity.SignSysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: UserLoginRespVO
 * @Description: 用户登录返回
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 17:59
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 17:59
 * @Version: 0.0.1
 */
@Data
public class UserLoginRespVO implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "用户名 如：张三、李四")
    private String userNickName;

    @ApiModelProperty(value = "登录账户 如：手机号或邮箱")
    private String userName;

   @ApiModelProperty(value = " 所属节点名称")
    private String nodeName;

    @ApiModelProperty(value = " 01 正常 02 禁用")
    private String isDeleted;

    /**
     * 白鹤第三方用户ID
     */
    @ApiModelProperty(value = "白鹤第三方用户ID")
    private String otherId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date gmtCreate;

    @ApiModelProperty("密码错误次数")
    private int passwordErrorTimes;

    @ApiModelProperty(value = "sessionToken信息")
    private String sessionToken;

    @ApiModelProperty(value = "系统配置信息")
    private SignSysDefaultConfigInfoResp configInfoResp;

    @ApiModelProperty(value = "Licence信息")
    private UserLicenceInfo userLicenceInfo;

    @ApiModelProperty(value = "用户所属单位ID")
    private String enterpriseId;

    @ApiModelProperty(value = "用户角色信息")
    private SignSysRole roleInfo;


}
