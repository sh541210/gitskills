package com.iyin.sign.system.vo.req;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: DeleteEnterpriseSealInfoReqVO
 * @Description: 删除单位下的印章
 * @Author: luwenxiong
 * @CreateDate: 2019/6/24 16:39
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/24 16:39
 * @Version: 0.0.1
 */
@Data
public class DeleteEnterpriseSealInfoReqVO implements Serializable {

    private static final long serialVersionUID = 8147158331390008463L;

    @NotBlank(message = "单位ID不能为空")
    private String enterpriseId;

    @NotEmpty(message = "印章编码不能为空")
    private List<String> pictureCodes;
}
