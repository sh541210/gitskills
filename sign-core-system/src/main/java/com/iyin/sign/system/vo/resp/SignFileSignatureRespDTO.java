package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignFileSignatureRespDTO
 * @Description: 加签文件签章信息响应DTO
 * @Author: 唐德繁
 * @CreateDate: 2018/12/13 0013 下午 6:30
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/12/13 0013 下午 6:30
 * @Version: 0.0.1
 */
@Data
public class SignFileSignatureRespDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否签名认证通过")
    private Boolean isSignatureCertificate;

    @ApiModelProperty(value = "是否时间戳认证通过")
    private Boolean isTimestampCertificate;

    @ApiModelProperty(value = "签约人")
    private String signer;

    @ApiModelProperty(value = "签约时间")
    private Date signDate;

    @ApiModelProperty(value = "证书颁发机构")
    private String certificateAuthority;

    @ApiModelProperty(value = "时间戳")
    private Date timestamp;

    @ApiModelProperty(value = "印章编码")
    private String sealCode;

}
