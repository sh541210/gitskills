package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysPersonalInfo
 * @Description: 签章系统个人用户信息
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 18:11
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 18:11
 * @Version: 0.0.1
 */
@ApiModel(value = "签章系统个人用户信息")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysPersonalInfo extends Model<SignSysPersonalInfo> implements Serializable {

    private static final long serialVersionUID = 5241936555325500891L;

    private String id;

    private String personalName;

    private String personalPhone;

    private String personalLogoPath;

    private String personalEmail;

    private String personalCertificateType;

    private String personalCertificateNo;

    private String personalAddress;

    private String personalContactAddress;

    private String nationality;

    private String province;

    private String city;

    private String area;

    private String areaCode;

    private String gender;

    private Date birth;

    private String remark;

    private Integer isDeleted;

    private String identityCardFrontPic;

    private String identityCardBackPic;

    private int realNameAuthenticationFlag;

    private Date realNameAuthenticationTime;

    private String realNameAuthenticationFromBizSystemCode;

    private Date gmtCreate;

    private Date gmtModified;

    private String certificationLevels;

    private String qq;

    private Object bankInfoJson;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
