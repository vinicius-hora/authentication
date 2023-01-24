package com.rasmoo.client.financescontroll.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spi.service.contexts.SecurityContextBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableSwagger2
@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Value("${host.full.dns.auth.link}")
    private String authLink;

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rasmoo.client.financescontroll.v1.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.apiInfoBuilder().build())
                .securitySchemes(Collections.singletonList(this.securitySchema()))
                .securityContexts(Arrays.asList(this.securityContext()));
    }

    private ApiInfoBuilder apiInfoBuilder(){
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("Finances Controll");
        apiInfoBuilder.description("Api para controle de finanças");
        return apiInfoBuilder;
    }

    private OAuth securitySchema(){
        List<AuthorizationScope> authScope = new ArrayList<>();
        authScope.add(new AuthorizationScope("cw_logado", "Acesso area logada"));
        authScope.add(new AuthorizationScope("cw_naologado", "Acesso area nao logada"));

        List<GrantType> grantTypes = new ArrayList<>();
        grantTypes.add(new ResourceOwnerPasswordCredentialsGrant(this.authLink));
        return new OAuth("Auth2-schema", authScope, grantTypes);
    }

    private SecurityContext securityContext(){
        return SecurityContext.builder().securityReferences(this.defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth(){
        AuthorizationScope[] authScopes = new AuthorizationScope[2];
        authScopes[0] = new AuthorizationScope("cw_logado", "acesso area logada");
        authScopes[1] = new AuthorizationScope("cw_naologado", "Acesso area nao logada");
        return Collections.singletonList(new SecurityReference("Auth2-schema", authScopes));
    }
}
