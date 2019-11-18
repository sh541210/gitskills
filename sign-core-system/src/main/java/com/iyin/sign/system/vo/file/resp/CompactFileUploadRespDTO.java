package com.iyin.sign.system.vo.file.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: CompactFileUploadRespVO
 * @Description: 合同文件上传响应VO
 * @Author: 唐德繁
 * @CreateDate: 2018/9/12 0012 下午 3:16
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/9/12 0012 下午 3:16
 * @Version: 0.0.1
 */
@Data
public class CompactFileUploadRespDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同文件编码")
    private String fileCode;

    @ApiModelProperty(value = "合同文件名称")
    private String fileName;

    @ApiModelProperty(value = "合同文件首页图片路径")
    private String homePageImagePath;

    @ApiModelProperty(value = "合同文件页数")
    private Integer filePage;

}
