package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName StampRecordApplyVO
 * StampRecordApplyVO
 * @Author wdf
 * @Date 2019/10/25 9:53
 * @throws
 * @Version 1.0
 **/
@NoArgsConstructor
@Data
public class StampRecordApplyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "单据ID")
    private String id;
    @ApiModelProperty(value = "申请人ID")
    private String applyUser;
    @ApiModelProperty(value = "申请人名称")
    private String applyUserName;
    @ApiModelProperty(value = "申请人头像")
    private String headPortrait;
    @ApiModelProperty(value = "签名")
    private String autoGraph;
    @ApiModelProperty(value = "公司ID")
    private String companyId;
    @ApiModelProperty(value = "部门名称")
    private String orgStructureName;
    @ApiModelProperty(value = "印章ID")
    private String sealId;
    @ApiModelProperty(value = "印章MAC")
    private String mac;
    @ApiModelProperty(value = "印章名称")
    private String sealName;
    @ApiModelProperty(value = "申请事由")
    private String applyCause;
    @ApiModelProperty(value = "文件类型")
    private String fileType;
    @ApiModelProperty(value = "文件份数")
    private Integer fileNumber;
    @ApiModelProperty(value = "用印过期时间")
    private Long expireTime;
    @ApiModelProperty(value = "申请次数")
    private Integer applyCount;
    @ApiModelProperty(value = "申请时间")
    private Long applyTime;
    @ApiModelProperty(value = "单据状态，1：已审批，5：已关闭")
    private Integer approveStatus;
    @ApiModelProperty(value = "最近审批时间")
    private Long lastApproveTime;
    @ApiModelProperty(value = "剩余盖章次数")
    private Integer availableCount;
    @ApiModelProperty(value = "盖章次数")
    private Integer stampCount;
    @ApiModelProperty(value = "盖章图片数量")
    private Integer photoCount;
    @ApiModelProperty(value = "申请文件名称")
    private String applyPdf;
    @ApiModelProperty(value = "盖章文件名称")
    private String stampPdf;
    @ApiModelProperty(value = "盖章记录 PDF 文件名称")
    private String stampRecordPdf;
    @ApiModelProperty(value = "最近盖章时间")
    private Long lastStampTime;
    @ApiModelProperty(value = "最近盖章地址")
    private String lastStampAddress;
    @ApiModelProperty(value = "盖章图片名称列表")
    private List<String> stampRecordImgList;
}
