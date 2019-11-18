package com.iyin.sign.system.model.sign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: CoordinateParam
 * @Description: 坐标参数
 * @Author: 唐德繁
 * @CreateDate: 2018/10/23 0023 下午 7:36
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/10/23 0023 下午 7:36
 * @Version: 0.0.1
 */
@Data
public class CoordinateParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("左下角x坐标")
    @NotNull(message = "左下角x坐标不能为空")
    private float llx;
    @ApiModelProperty("左下角y坐标")
    @NotNull(message = "左下角y坐标不能为空")
    private float lly;
    @ApiModelProperty("右上角x坐标")
    @NotNull(message = "右上角x坐标不能为空")
    private float urx;
    @ApiModelProperty("右上角y坐标")
    @NotNull(message = "右上角y坐标不能为空")
    private float ury;
}
