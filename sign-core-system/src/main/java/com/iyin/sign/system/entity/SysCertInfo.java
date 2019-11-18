package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SysCertInfo.java
 * @Description: 证书
 * @Author: yml
 * @CreateDate: 2019/6/26
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/26
 * @Version: 1.0.0
 */
@Data
@Api(value = "证书")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sign_sys_cert_info")
public class SysCertInfo extends Model<SysCertInfo> implements Serializable{
    /**
     * 证书ID
     */
    @ApiModelProperty("证书ID")
    private String certId;

    /**
     * 机构ID
     */
    @ApiModelProperty("机构ID")
    private String orgId;

    /**
     * 证书编码
     */
    @ApiModelProperty("证书编码")
    private String certCode;

    /**
     * 证书密码
     */
    @ApiModelProperty("证书密码")
    private String certPassword;

    /**
     * 证书名称
     */
    @ApiModelProperty("证书密码")
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
    private Date validityFrom;

    /**
     * 有效期到
     */
    @ApiModelProperty("有效期到")
    private Date validityExp;

    /**
     * 是否删除(0:否，1:是)
     */
    @ApiModelProperty("是否删除(0:否，1:是)")
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createDate;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private String createUser;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateDate;

    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.certId;
    }
}