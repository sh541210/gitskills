package com.iyin.sign.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Swagger2
 * @Description: swagger配置类
 * @Author: 唐德繁
 * @CreateDate: 2018/9/12 0012 下午 12:16
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/9/12 0012 下午 12:16
 * @Version: 0.0.1
 */
@Configuration
@EnableSwagger2
public class Swagger2 {


    @Bean
    public Docket createRestApi() {
        ParameterBuilder sessionTokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        sessionTokenPar.name("session_token").description("前端web系统调用请求头 session_token").modelRef(new ModelRef("string")).parameterType("header").required(false);
        pars.add(sessionTokenPar.build());

        ParameterBuilder apiTokenPar = new ParameterBuilder();
        apiTokenPar.name("api_token").description("第三方系统调用请求头 api_token").modelRef(new ModelRef("string")).parameterType("header").required(false);
        pars.add(apiTokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.iyin.sign.system.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .enable(true);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("签章系统接口")
                .description("")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
