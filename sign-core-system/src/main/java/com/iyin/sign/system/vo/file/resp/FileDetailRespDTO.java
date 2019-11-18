package com.iyin.sign.system.vo.file.resp;

import com.iyin.sign.system.entity.FileResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName: FileDetailRespDTO
 * @Description: 文件管理详情
 * @Author: yml
 * @CreateDate: 2019/8/8
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/8
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("查看文件")
public class FileDetailRespDTO extends FileResource implements Serializable {

    @ApiModelProperty(value = "文件状态（01：草稿；02：已撤销；03：已拒签；04：签署中；05：签署完成；）")
    private String compactStatus;

    @ApiModelProperty(value = "文件ID")
    private String compactId;

    @ApiModelProperty(value = "签署状态（01：待我签署；02：待他人签；03：签署通过；04签署不通过）")
    private String signStatus;

    @ApiModelProperty(value = "抄送人")
    private String carbonCopy;

    @ApiModelProperty(value = "合同发起人")
    private String sponsor;

    @ApiModelProperty(value = "签署人ID")
    private String signatoryId;
}
