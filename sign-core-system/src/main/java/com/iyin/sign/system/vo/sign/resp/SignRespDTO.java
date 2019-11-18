package com.iyin.sign.system.vo.sign.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SignRespDTO
 * @Description: 签章响应DTO
 * @Author: yml
 * @CreateDate: 2019/6/22
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/22
 * @Version: 1.0.0
 */
@Data
public class SignRespDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "签署完成文件Base64")
  @JsonIgnore
  private String signCompleteFileStr;

  @ApiModelProperty(value = "签章参数")
  @JsonIgnore
  private String multiParam;

  @ApiModelProperty(value = "文档code")
  private String fileCode;

  @ApiModelProperty(value = "签署时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss ",timezone = "GMT+8")
  private LocalDateTime signDate;

  @Override
  public String toString() {
    return "SignRespDTO{" +
            "fileCode='" + fileCode + '\'' + '}';
  }
}
