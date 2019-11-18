package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: VerifySignFileRespDTO
 * @Description: 验真加签文件响应DTO
 * @Author: 唐德繁
 * @CreateDate: 2018/12/13 0013 下午 3:07
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/12/13 0013 下午 3:07
 * @Version: 0.0.1
 */
@Data
public class VerifySignFileRespDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "验真加签文件签章信息集合")
    private List<SignFileSignatureRespDTO> signFileSignatureList;

}
