package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @ClassName: SignSysDefaultConfig
 * @Description: 默认设置
 * @Author: luwenxiong
 * @CreateDate: 2019/8/5 17:57
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/5 17:57
 * @Version: 0.0.1
 */
@ApiModel(value = "签章系统默认配置信息")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SignSysDefaultConfig extends Model<SignSysDefaultConfig> {

    private static final long serialVersionUID = -2912755748187546650L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "系统名称")
    @Length(max = 30,message ="最大支持15个字符" )
    private String sysName;

    @ApiModelProperty(value = "时间戳服务器地址")
    private String timeStamp;

    @ApiModelProperty(value = "安印云签API令牌")
    private String apiToken;

    @ApiModelProperty(value = "系统logo绝对路径")
    private String logoUrl;

    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;


}
