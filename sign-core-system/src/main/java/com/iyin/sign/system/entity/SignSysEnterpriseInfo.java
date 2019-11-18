package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysEnterpriseInfo
 * @Description: 签章系统企业数据
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 18:01
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 18:01
 * @Version: 0.0.1
 */
@ApiModel(value = "签章系统企业数据")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysEnterpriseInfo extends Model<SignSysEnterpriseInfo> implements Serializable {

    private static final long serialVersionUID = 1802234546382874477L;

    private String id;

    private String chineseName;

    private String minorityName;

    private String englishName;

    private String creditCode;

    private String licenseCode;

    private String organizationCode;

    private String undeclaredCode;

    private String areaCode;

    private String status;

    private String type;

    private String nationality;

    private String province;

    private String city;

    private String area;

    private String phone;

    private String address;

    private Date startupDate;

    private String legalName;

    private String legalTelephone;

    private String legalCertificateType;

    private String legalCertificateNo;

    private String legalAddress;

    private String detailsInfo;

    private String organizationLogoPath;

    private String remark;

    private Integer isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String certificationLevels;

    private Integer unitType;

    private Date businessEndTime;

    private String businessScope;

    private String industry;

    private String companySize;

    private Date publishCertificationTime;

    private String registrationCompany;

    private Date businessStartTime;

    private String capitalType;

    private String capital;

    private String companyWebsiteUrl;

    private String companyEmail;

    private String fax;

    private int realNameAuthenticationFlag;

    private Date realNameAuthenticationTime;

    private String realNameAuthenticationFromBizSystemCode;

    private String businessLicensePic;

    private String legalPersonIdentityCardFrontPic;

    private String legalPersonIdentityCardBackPic;

    @ApiModelProperty("对应的节点ID")
    private String nodeId;

    /**
     * 自定义字段
     */
    private String extDefine;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
