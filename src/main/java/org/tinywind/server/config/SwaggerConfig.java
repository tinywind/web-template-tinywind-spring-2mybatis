package org.tinywind.server.config;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.or;

@EnableSwagger2
@PropertySource("classpath:swagger.properties")
public class SwaggerConfig {

    @Value("${contract.name}")
    private String name;
    @Value("${contract.url}")
    private String url;
    @Value("${contract.email}")
    private String email;
    @Value("${api.path:/}")
    private String apiPath;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(paths())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact(name, url, email))
                .version("0.1")
                .build();
    }

    private Predicate<String> paths() {
        return or(containsPattern(("/" + apiPath + "/*").replaceAll("[/]+", "/")));
    }
}