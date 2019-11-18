package com.iyin.sign.system.vo.file.resp;

import com.iyin.sign.system.vo.file.req.InitiateContractReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: BatchImportRespDTO
 * @Description: 批量导入返回
 * @Author: yml
 * @CreateDate: 2019/8/23
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/23
 * @Version: 1.0.0
 */
@Data
public class BatchImportRespDTO implements Serializable {

    @ApiModelProperty("总共数据")
    private Integer total;

    @ApiModelProperty("数据")
    private Integer normal;

    @ApiModelProperty("异常数据")
    private Integer exception;

    @ApiModelProperty("异常数据下载链接")
    private String exceptionDownload;

    @ApiModelProperty("数据")
    private List<InitiateContractReqVO.SignInfo> signInfos;
}
