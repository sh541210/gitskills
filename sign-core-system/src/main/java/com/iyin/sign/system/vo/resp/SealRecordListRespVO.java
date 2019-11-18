package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SealRecordListRespVO
 * SealRecordListRespVO
 * @Author wdf
 * @Date 2019/10/25 14:17
 * @throws
 * @Version 1.0
 **/
@NoArgsConstructor
@Data
public class SealRecordListRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("盖章记录数据list")
    private List<SealRecordVO> sealRecordVOList;

    @ApiModelProperty("总数量")
    private Integer count;

}
