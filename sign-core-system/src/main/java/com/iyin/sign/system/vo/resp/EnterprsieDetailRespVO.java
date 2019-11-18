package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: EnterprsieDetailRespVO
 * @Description: 单位详情
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 16:27
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 16:27
 * @Version: 0.0.1
 */
@Data
public class EnterprsieDetailRespVO implements Serializable {
    private static final long serialVersionUID = -7181202577785176168L;

    private String id;

    /**
     * 单位名称
     */
    private String chineseName;

    /**
     * 企业信用代码
     */
    private String creditCode;

    /**
     * 商户单位编码
     */
    private String organizationCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date gmtCreate;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 状态 01：正常  02 禁用
     */
    private Integer isDeleted;

    /**
     * 自定义字段
     */
    private String extDefine;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "法人名称")
    private String legalName;

    @ApiModelProperty(value = "节点ID")
    private String nodeId;

}
