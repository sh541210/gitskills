package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import javafx.scene.chart.ValueAxis;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: UserBaseInfoRespVO
 * @Description: 用户基本信息
 * @Author: luwenxiong
 * @CreateDate: 2019/6/24 17:48
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/24 17:48
 * @Version: 0.0.1
 */
@Data
public class UserBaseInfoRespVO implements Serializable {
    private static final long serialVersionUID = 5153186314143285320L;

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @ApiModelProperty(value = "登录账号")
    private String userName;

    @ApiModelProperty(value = "所属节点ID")
    private String nodeId;

    @ApiModelProperty(value = "01 正常 02 禁用")
    private Integer isForbid;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date gmtCreate;
}
