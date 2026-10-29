package org.tinywind.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.constraints.NotNull;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WebStackCompatibilityTest {
    @Test
    public void preservesJodaJsonSerialization() throws Exception {
        ObjectMapper mapper = new RootConfig().objectMapper();
        DateTime date = new DateTime(2026, 10, 29, 0, 0, org.joda.time.DateTimeZone.UTC);
        assertEquals(date.getMillis(), mapper.readTree(mapper.writeValueAsString(date)).asLong());
    }

    @Test
    public void usesContainerMultipartParsing() {
        assertTrue(new WebMvcConfig().multipartResolver() instanceof StandardServletMultipartResolver);
    }

    @Test
    public void validatesJakartaConstraints() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            assertEquals(1, factory.getValidator().validate(new RequiredValue()).size());
        }
    }

    @Test
    public void keepsRawByteResponsesForOpenApiAlongsideConfiguredJson() {
        WebMvcConfig application = new WebMvcConfig();
        org.springframework.test.util.ReflectionTestUtils.setField(application, "objectMapper", new RootConfig().objectMapper());
        var converters = new org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport() {
            @Override
            protected void configureMessageConverters(java.util.List<org.springframework.http.converter.HttpMessageConverter<?>> values) {
                application.configureMessageConverters(values);
            }

            @Override
            protected void extendMessageConverters(java.util.List<org.springframework.http.converter.HttpMessageConverter<?>> values) {
                application.extendMessageConverters(values);
            }

            java.util.List<org.springframework.http.converter.HttpMessageConverter<?>> converters() {
                return getMessageConverters();
            }
        }.converters();
        var first = converters.stream()
                .filter(converter -> converter.canWrite(byte[].class, org.springframework.http.MediaType.APPLICATION_JSON))
                .findFirst().orElseThrow();
        assertTrue(first instanceof org.springframework.http.converter.ByteArrayHttpMessageConverter);
    }

    public static class RequiredValue {
        @NotNull
        private String value;
    }
}
