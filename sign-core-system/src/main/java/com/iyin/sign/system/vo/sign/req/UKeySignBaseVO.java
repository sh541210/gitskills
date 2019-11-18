package com.iyin.sign.system.vo.sign.req;

import com.iyin.sign.system.model.sign.MultiParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName UKeySignBaseVO
 * UKeySignBaseVO
 * @Author wdf
 * @Date 2019/8/15 15:16
 * @throws
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class UKeySignBaseVO extends MultiParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("印章名称")
    @NotBlank(message = "印章名称不能为空")
    private String sealName;

    @ApiModelProperty("印章编码")
    @NotBlank(message = "印章编码不能为空")
    private String sealCode;
}
