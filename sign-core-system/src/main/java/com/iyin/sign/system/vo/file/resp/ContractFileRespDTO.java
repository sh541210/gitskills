package com.iyin.sign.system.vo.file.resp;

import com.iyin.sign.system.entity.SignSysCompactFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ContractFileRespDTO
 * @Description: 合同文件信息
 * @Author: yml
 * @CreateDate: 2019/8/16
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/16
 * @Version: 1.0.0
 */
@Data
public class ContractFileRespDTO implements Serializable {

    @ApiModelProperty("合同ID")
    private String contractId;

    @ApiModelProperty("合同主题")
    private String contractTheme;

    @ApiModelProperty("合同文件")
    private List<SignSysCompactFile> compactFiles;

    @ApiModelProperty("合同附件")
    private List<SignSysCompactFile> compactAttachments;
}
