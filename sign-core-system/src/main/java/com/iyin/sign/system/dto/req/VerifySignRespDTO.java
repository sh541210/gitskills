package com.iyin.sign.system.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: VerifySignRespDTO
 * @Description: 验真
 * @Author: wdf
 * @CreateDate: 2019/7/24  下午 3:03
 * @UpdateDate: 2019/7/24  下午 3:03
 * @Version: 0.0.1
 */
@Data
public class VerifySignRespDTO implements Serializable {

    private static final long serialVersionUID = 6619474212927970128L;

    private SignInfoRespDTO signInfoRespDTO;

    private CerInfoRespDTO cerInfoRespDTO;
}
