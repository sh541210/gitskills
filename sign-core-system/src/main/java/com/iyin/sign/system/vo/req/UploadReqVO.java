package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: UploadReqVO
 * @Description: 上传申请单据
 * @Author: yml
 * @CreateDate: 2019/10/9
 * @UpdateUser: yml
 * @UpdateDate: 2019/10/9
 * @Version: 1.0.0
 */
@Data
@ApiModel("上传申请单据")
public class UploadReqVO{

    @ApiModelProperty("申请事由")
    @NotBlank(message = "申请事由不能为空")
    private String applyCause;

    @ApiModelProperty("申请次数")
    @NotNull(message = "申请次数不能为空")
    private Integer applyCount;

    @ApiModelProperty(value = "申请文件，限制为 PDF，和 imgList 参数二选一")
    private String applyPdf;

    @ApiModelProperty("申请人id")
    @NotBlank(message = "申请人id不能为空")
    private String applyUser;

    @ApiModelProperty(value = "用印过期时间",example = "1542154315")
    @NotNull(message = "用印过期时间不能为空")
    private Long expireTime;

    @ApiModelProperty("文件份数")
    @NotNull(message = "文件份数不能为空")
    private Integer fileNumber;

    @ApiModelProperty("文件类型")
    @NotBlank(message = "文件类型不能为空")
    private String fileType;

    @ApiModelProperty("申请文件拍照图片，和 applyPdf 参数二选一")
    private List<String> imgList;

    @ApiModelProperty("印章id")
    @NotBlank(message = "印章id不能为空")
    private String sealId;
}
