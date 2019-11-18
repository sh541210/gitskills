package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName: AddTemplateReqVo
 * @Description: 添加模板
 * @Author: yml
 * @CreateDate: 2019/9/16
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/16
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddTemplateReqVo extends SignSysTemplateVO {

    @ApiModelProperty("模板ID")
    private String templateId;

    @ApiModelProperty("模板控件")
    @Valid
    private List<TemplateFileds> templateFiledsList;

    @Data
    @ApiModel("模板控件")
    public static class TemplateFileds implements Serializable {
        /**
         * 名称
         */
        @ApiModelProperty(value = "名称")
        @Length(max = 30)
        private String signName;

        /**
         * 位置类型(01:印章；02:签名；)
         */
        @ApiModelProperty(value = "位置类型(01:印章；02:签名；)")
        private String signType;

        /**
         * 签章方式(01：单页签章；02：多页签章；03：骑缝签章；04:连页签章)
         */
        @ApiModelProperty(value = "签章方式(01：单页签章；02：多页签章；03：骑缝签章；04:连页签章)")
        private String signatureMethod;

        /**
         * 签章页或签章初始页
         */
        @ApiModelProperty(value = "签章页或签章初始页")
        private Integer signatureStartPage;

        /**
         * 签章结束页
         */
        @ApiModelProperty(value = "签章结束页")
        private Integer signatureEndPage;

        /**
         * 骑缝签时每枚章的覆盖页数
         */
        @ApiModelProperty(value = "骑缝签时每枚章的覆盖页数")
        private Integer coverPageNum;

        /**
         * 签章坐标X轴
         */
        @ApiModelProperty(value = "签章坐标X轴")
        private BigDecimal signatureCoordinateX;

        /**
         * 签章坐标Y轴
         */
        @ApiModelProperty(value = "签章坐标Y轴")
        private BigDecimal signatureCoordinateY;

        @ApiModelProperty(value = "骑缝签章方向0：表示左 1：表示右")
        private String signatureDirection;
    }
}
