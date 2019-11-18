package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName StampRecordApplyResultRespVO
 * StampRecordResultRespVO
 * @Author wdf
 * @Date 2019/10/23 18:03
 * @throws
 * @Version 1.0
 **/
@Data
public class StampRecordApplyResultRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("盖章记录数据list")
    private List<StampRecordApplyVO> stampRecordApplyVOList;

    @ApiModelProperty("总数量")
    private Integer count;
}
