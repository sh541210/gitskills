package com.iyin.sign.system.vo.sign.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FileSignRespDTO
 * @Description: 签章响应DTO
 * @Author: wdf
 * @CreateDate: 2019/6/22
 * @UpdateUser: wdf
 * @UpdateDate: 2019/6/22
 * @Version: 1.0.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileSignRespDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "签署完成文件Base64")
  private String signCompleteFileStr;

  @ApiModelProperty(value = "签章参数")
  @JsonIgnore
  private String multiParam;

  @ApiModelProperty(value = "文档code")
  private String fileCode;

  @Override
  public String toString() {
    return "FileSignRespDTO{" +
            "signCompleteFileStr='" + signCompleteFileStr + '\'' +
            ", multiParam='" + multiParam + '\'' +
            ", fileCode='" + fileCode + '\'' +
            '}';
  }
}
