package com.iyin.sign.system.vo.file.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: SignInfo
 * @Description: 签署信息
 * @Author: yml
 * @CreateDate: 2019/8/8
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/8
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileManageSignInfoReqVO extends FileManageInfoReqVO {

    @ApiModelProperty("签名域信息集")
    @NotNull(message = "签名域信息集不为null")
    @Valid
    private List<CompactFieldInfoReqVO> compactFieldInfoReqVOList;
}
