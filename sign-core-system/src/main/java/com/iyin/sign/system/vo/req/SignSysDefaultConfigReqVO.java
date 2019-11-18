package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @ClassName: SignSysDefaultConfigReqVO
 * @Description: 默认设置
 * @Author: luwenxiong
 * @CreateDate: 2019/8/5 17:49
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/5 17:49
 * @Version: 0.0.1
 */
@Data
public class SignSysDefaultConfigReqVO implements Serializable {

    private static final long serialVersionUID = 3187688596394091322L;

    @ApiModelProperty(value = "系统名称")
    @Length(max = 30,message ="最大支持15个字符" )
    private String sysName;

    @ApiModelProperty(value = "时间戳服务器地址")
    private String timeStamp;

    @ApiModelProperty(value = "安印云签API令牌")
    private String apiToken;

    @ApiModelProperty(value = "系统logo绝对路径")
    private String logoUrl;
}
