package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: SysSignatureLog.java
 * @Description: 签章记录表
 * @Author: yml
 * @CreateDate: 2019/6/27
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/27
 * @Version: 1.0.0
 */
@Data
@Api("签章记录表")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sign_sys_signature_log")
public class SysSignatureLog {
    /**
     * snowflake
     */
    private String id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 印章名称
     */
    private String sealName;

    /**
     * 印章编码
     */
    private String sealCode;

    /**
     * 章模类型：行政章，人事章
     */
    private String sealType;

    /**
     * 介质类型：01 云印章，02云签名，03 ukey印章，04ukey签名
     */
    private String mediumType;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件编码
     */
    private String fileCode;

    /**
     * 签章方案(01:平台签，02:快捷签)
     */
    private String signatureType;

    /**
     * 签章模式(01:关键字签章，02:坐标签章)
     */
    private String signatureModel;

    /**
     * 签章方式(01:单页签章，02:多页签章，03:骑缝签章)
     */
    private String signatureName;

    /**
     * 签章结果(01:成功,02:失败)
     */
    private String signatureResult;

    /**
     * 用户信息
     */
    private String userId;

    /**
     * 签后文档HASH
     */
    private String signFileHash;

    /**
     * 签后文档ID
     */
    private String signFileCode;

    /**
     * 文件页数
     */
    private Long page;

    /**
     * 是否删除(0:否，1:是)
     */
    @TableLogic
    private Byte isDeleted;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 更新者
     */
    private String updateUser;

    /**
     * 签章参数
     */
    private String multiParam;

    /**
     * 签章ip
     */
    private String ipAddress;

    /**
     * 签章mac
     */
    private String macAddress;

    /**
     * 签章设备
     */
    private String deviceName;

}