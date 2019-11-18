package com.iyin.sign.system.vo.req;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: SettingSealUserLinkReqVO
 * @Description: 设置印章与用户的关联关系
 * @Author: luwenxiong
 * @CreateDate: 2019/6/24 17:20
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/24 17:20
 * @Version: 0.0.1
 */
@Data
public class BindUserSealReqVO implements Serializable {

    private static final long serialVersionUID = -8750036778820520083L;

    @NotEmpty(message = "印章编码不能为空")
    private String sealId;

    @NotEmpty(message = "用户ID不能为空")
    private List<String> userIds;

}
