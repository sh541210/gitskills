package com.iyin.sign.system.vo.file.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: CompactQueryRespDTO
 * @Description: 合同管理查询返回
 * @Author: yml
 * @CreateDate: 2019/8/9
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/9
 * @Version: 1.0.0
 */
@ApiModel("合同管理查询返回")
@Data
public class CompactQueryRespDTO {

    @ApiModelProperty("合同ID")
    private String compactId;
    @ApiModelProperty("合同主题")
    private String compactTheme;
    @ApiModelProperty("发起方")
    private String initiator;
    @ApiModelProperty("签署人")
    private String signatory;
}
