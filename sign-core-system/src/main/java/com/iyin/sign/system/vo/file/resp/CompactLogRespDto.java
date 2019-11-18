package com.iyin.sign.system.vo.file.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: CompactLog
 * @Description:
 * @Author: yml
 * @CreateDate: 2019/8/20
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/20
 * @Version: 1.0.0
 */
@Data
public class CompactLogRespDto implements Serializable {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("联系方式")
    private String phoneOrMail;

    @ApiModelProperty("操作类型 （0查看 1签署 2打印 3下载 4拒签 5用印申请）")
    private String type;

    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operaDate;
}
