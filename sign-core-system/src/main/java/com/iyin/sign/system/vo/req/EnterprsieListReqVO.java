package com.iyin.sign.system.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: EnterprsieListReqVO
 * @Description: EnterprsieListReqVO
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 14:39
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 14:39
 * @Version: 0.0.1
 */
@Data
public class EnterprsieListReqVO implements Serializable {
    private static final long serialVersionUID = -2539157865237604899L;

    /**
     * 根据单位名称模糊查询
     */
    private String chineseName;

    /**
     * 状态( 01 正常、 02禁用)
     */
    private String isDeleted;

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;

    @NotNull(message = "currentPage不能为空")
    private Integer currentPage;

}
