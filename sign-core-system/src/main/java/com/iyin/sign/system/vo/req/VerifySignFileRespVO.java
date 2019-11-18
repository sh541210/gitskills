package com.iyin.sign.system.vo.req;

import com.iyin.sign.system.dto.req.VerifySignRespDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: VerifySignFileRespVO
 * @Description: 加签文件验真响应DTO
 * @Author: 唐德繁
 * @CreateDate: 2018/12/13 0013 下午 2:27
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/12/13 0013 下午 2:27
 * @Version: 0.0.1
 */
@Data
public class VerifySignFileRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "验真加签文件签章信息集合")
    private List<VerifySignRespDTO> signFileSignatureList;
}
