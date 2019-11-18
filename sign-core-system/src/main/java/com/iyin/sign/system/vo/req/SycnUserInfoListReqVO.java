package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @ClassName SycnUserInfoListReqVO
 * SycnUserInfoListReqVO
 * @Author wdf
 * @Date 2019/10/16 10:39
 * @throws
 * @Version 1.0
 **/
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@ApiModel(value = "同步用户信息请求VO-白鹤")
public class SycnUserInfoListReqVO {

    @ApiModelProperty("用户列表")
    @NotEmpty(message = "用户列表不能为空")
    private List<String> userList;
}
