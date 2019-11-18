package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysSealUser
 * @Description: 印章权限分配
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 11:12
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 11:12
 * @Version: 0.0.1
 */
@Data
public class SignSysSealUser extends Model<SignSysSealUser> implements Serializable {

    private static final long serialVersionUID = -8300871693097146289L;
    /**
     * 主键
     */
    private String id;

    /**
     * 印章ID(印章表主键)
     */
    private String sealId;

    /**
     * 用户ID(用户表主键)
     */
    private String userId;

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



    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
