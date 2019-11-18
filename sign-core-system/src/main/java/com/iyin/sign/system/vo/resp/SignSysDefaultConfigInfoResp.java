package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SignSysDefaultConfigInfoResp
 * @Description: 默认设置信息
 * @Author: luwenxiong
 * @CreateDate: 2019/8/9 11:23
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/9 11:23
 * @Version: 0.0.1
 */
@Data
public class SignSysDefaultConfigInfoResp implements Serializable {

    private static final long serialVersionUID = -6937395771578665818L;

    @ApiModelProperty(value = "系统名称")
    private String sysName;

    @ApiModelProperty(value = "时间戳服务器地址")
    private String timeStamp;

    @ApiModelProperty(value = "系统logo地址")
    private String logoUrl;
}
