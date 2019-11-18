package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: UserDetailRespVO
 * @Description: 用户详情
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 16:37
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 16:37
 * @Version: 0.0.1
 */
@Data
public class UserDetailRespVO implements Serializable {

    private static final long serialVersionUID = -8396406017525264373L;

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "成员名称")
    private String userNickName;

    @ApiModelProperty(value = "登录账号")
    private String userName;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "节点ID")
    private String nodeId;

    @ApiModelProperty(value = "状态 0:未禁用 1：启用")
    private Integer isForbid;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date gmtCreate;

    @ApiModelProperty(value = "数据权限 0：全部 1：本级  2：本级及子级")
    private String dataLimitType;

    /**
     * 白鹤第三方用户ID
     */
    @ApiModelProperty(value = "白鹤第三方用户ID")
    private String otherId;

    @ApiModelProperty(value = "角色集")
    private List<UserRoleRespVO> userRoleRespVOS;

    @ApiModelProperty(value = "用户印章集")
    private List<UserSealRespVO> userSealRespVOS;


}
