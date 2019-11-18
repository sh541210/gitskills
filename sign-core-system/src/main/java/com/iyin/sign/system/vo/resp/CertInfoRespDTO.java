package com.iyin.sign.system.vo.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: CertInfoRespDTO
 * @Description:
 * @Author: yml
 * @CreateDate: 2019/6/26
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/26
 * @Version: 1.0.0
 */
@Data
public class CertInfoRespDTO implements Serializable {

    /**
     * 证书ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("证书ID")
    private String certId;

    /**
     * 证书名称
     */
    @ApiModelProperty("证书名称")
    private String certName;

    /**
     * 颁发者
     */
    @ApiModelProperty("颁发者")
    private String issuer;

    /**
     * 使用者
     */
    @ApiModelProperty("使用者")
    private String subject;

    /**
     * 序列号
     */
    @ApiModelProperty("序列号")
    private String serialNumber;

    /**
     * 有效期开始
     */
    @ApiModelProperty("有效期开始")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date validityFrom;

    /**
     * 有效期到
     */
    @ApiModelProperty("有效期到")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date validityExp;

}
