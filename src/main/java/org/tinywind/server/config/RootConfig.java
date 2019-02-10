package org.tinywind.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.tinywind.server.controller.ApiControllerAspect;
import org.tinywind.server.service.storage.BasicSessionStorage;
import org.tinywind.server.service.storage.SessionStorage;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author tinywind
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = Constants.BASE_PACKAGE, excludeFilters = {@ComponentScan.Filter(Configuration.class)})
public class RootConfig {
    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        return objectMapper;
    }

    @Bean
    public SerializationConfig serializationConfig() {
        return objectMapper().getSerializationConfig();
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MessageSource messageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n.messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        // messageSource.setCacheSeconds(1);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    @Bean
    public ApiControllerAspect apiControllerAspect() {
        return new ApiControllerAspect();
    }

    @Bean
    public SessionStorage sessionStorage() {
        return new BasicSessionStorage();
    }
}
