package org.tinywind.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:swagger.properties")
@EnableConfigurationProperties(SpringDocConfigProperties.class)
@Import({SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class})
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
    public OpenAPI api(SpringDocConfigProperties properties) {
        properties.setPathsToMatch(List.of(("/" + apiPath + "/**").replaceAll("[/]+", "/")));
        return new OpenAPI().info(new Info().version("0.1")
                .contact(new Contact().name(name).url(url).email(email)));
    }

    @Configuration
    @ConditionalOnProperty(name = "application.devel", havingValue = "true", matchIfMissing = true)
    @EnableConfigurationProperties({SwaggerUiConfigProperties.class, SwaggerUiOAuthProperties.class,
            WebProperties.class, WebMvcProperties.class})
    @ImportAutoConfiguration(org.springdoc.webmvc.ui.SwaggerConfig.class)
    public static class SwaggerUiConfiguration {
    }
}