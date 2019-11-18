package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: SignSysPrintInfoRespVO.java
 * @Description: 打印分配
 * @Author: yml
 * @CreateDate: 2019/9/3
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/3
 * @Version: 1.0.0
 */
@Data
@Accessors(chain = true)
@ApiModel(value ="打印分配")
public class SignSysPrintInfoRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("已分配对象")
    private List<SignSysPrintAuthUserRespVO> signSysPrintAuthUserRespVOS;

    @ApiModelProperty("打印次数")
    private String printNum;

    /**
     * 是否雾化 0否 1是
     */
    @ApiModelProperty(value = "是否雾化 0否 1是")
    private Integer isFoggy;

    /**
     * 是否脱密 0否 1是
     */
    @ApiModelProperty(value = "是否脱密 0否 1是")
    private Integer isGrey;
}
