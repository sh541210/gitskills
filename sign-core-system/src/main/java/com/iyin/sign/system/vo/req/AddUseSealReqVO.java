package com.iyin.sign.system.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: UploadReqVO
 * @Description: 用印申请单据
 * @Author: wdf
 * @CreateDate: 2019/10/9
 * @UpdateUser: wdf
 * @UpdateDate: 2019/10/9
 * @Version: 1.0.0
 */
@Data
@ApiModel("用印申请单据")
public class AddUseSealReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申请事由
     */
    @ApiModelProperty(value = "申请事由")
    @NotBlank(message = "申请事由不能为空")
    @Length(max = 300,message = "申请事由最长为300个字")
    private String applyCause;

    /**
     * 印章ID
     */
    @ApiModelProperty(value = "印章ID")
    @NotBlank(message = "印章ID称不能为空")
    private String sealId;

    /**
     * 申请次数
     */
    @ApiModelProperty(value = "申请次数")
    @NotNull(message = "申请次数不能为空")
    @Min(1)
    private Integer applyCount;

    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    @NotNull(message = "失效时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    @Future
    private Date expireTime;


    @ApiModelProperty("文件code列表，多个逗号分隔")
    @NotBlank(message = "文件code列表不能为空")
    private String fileCode;

}
