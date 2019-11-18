package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 打印分配
 * </p>
 *
 * @author wdf
 * @since 2019-08-06
 */
@Data
@Accessors(chain = true)
@ApiModel(value ="以分配返回列表数据")
public class SignSysPrintAuthUserListRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 打印分配列表数据
     */
    @ApiModelProperty(value = "打印分配列表数据")
    private List<SignSysPrintAuthUserRespVO> list;

    /**
     * 最后一次打印分配次数
     */
    @ApiModelProperty(value = "最后一次打印分配次数")
    private Long printDisNum;

}
