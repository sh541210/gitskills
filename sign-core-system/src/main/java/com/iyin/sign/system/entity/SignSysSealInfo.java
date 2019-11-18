package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysSealInfo
 * @Description: 印章实体
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 11:05
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 11:05
 * @Version: 0.0.1
 */
@Data
public class SignSysSealInfo extends Model<SignSysSealInfo> implements Serializable {

    private static final long serialVersionUID = -5734074098485909630L;
    /**
     * 主键
     */
    private String id;

    /**
     * 所属用户类型：01 单位，02 个人
     */
    private String pictureUserType;

    /**
     * 企业用户或个人用户ID
     */
    private String enterpriseOrPersonalId;

    /**
     * 章模类型：01 ukey印章，02云签名，03 云印章，04ukey签名
     */
    private String pictureType;

    /**
     * 章模业务类型：01 行政章、02 财务章等 03 业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章
     */
    private String pictureBusinessType;

    /**
     * 章模名称（如：电子行政章、电子行政章钢印）
     */
    private String pictureName;

    /**
     * 章模编码（电子印章唯一标识）
     */
    private String pictureCode;

    /**
     * 章模图片类型
     */
    private String picturePattern;

    /**
     * 章模图片宽度（mm）
     */
    private String pictureWidth;

    /**
     * 章模图片高度（mm）
     */
    private String pictureHeight;

    /**
     * 章模图片数据（ODC-数字信封 IYIN-二压码Base64）
     */
    private String pictureData;

    /**
     * 章模图片数据（Base64）
     */
    private String pictureData64;

    /**
     * 章模图片摘要
     */
    private String pictureDigest;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除（0：未删除；1：删除；）
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 章模来源
     */
    private String pictureOrigin;

    /**
     * 章模来源标记
     */
    private String pictureFlag;

    /**
     * 章模图片路径
     */
    private String picturePath;

    /**
     * 章模状态：01正常，02禁用
     */
    private String pictureStatus;

    /**
     * 证书来源（ 01 单位证书库关联  02 在线软证书 03 本地生成证书）'
     */
    private String certificateSource;

    /**
     * 证书ID
     * （当为云印章时：证书信息表主键,当证书来源为 01 时不能为空）
     * ukey印章时：sign_sys_uk_cert_info表主键
     */
    private String certificateId;

    /**
     * 介质类型 01 云印章  02ukey印章
     */
    private String mediumType;








































    @Override
    protected Serializable pkVal() {
        return null;
    }
}
