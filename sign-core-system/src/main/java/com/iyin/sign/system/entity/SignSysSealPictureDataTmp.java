package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysSealPictureDataTmp
 * @Description: 章莫临时表
 * @Author: luwenxiong
 * @CreateDate: 2019/6/22 16:56
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/22 16:56
 * @Version: 0.0.1
 */
@ApiModel(value = "章莫临时表")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysSealPictureDataTmp extends Model<SignSysSealPictureDataTmp> implements Serializable {

    private static final long serialVersionUID = 6297041720032200713L;
    private String id;

    private String pictureData;

    private String pictureData64;

    private String remark;

    private Integer isDeleted;

    private Date gmtCreate;

    private Date gmtModified;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
