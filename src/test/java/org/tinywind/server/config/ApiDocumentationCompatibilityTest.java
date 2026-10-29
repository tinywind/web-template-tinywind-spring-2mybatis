package org.tinywind.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ApiDocumentationCompatibilityTest {
    @Test
    public void servesOpenApiAndSwaggerUiWithoutSpringBootApplication() throws Exception {
        try (var context = new AnnotationConfigWebApplicationContext()) {
            context.setServletContext(new MockServletContext());
            context.register(DocumentationConfiguration.class);
            context.refresh();
            var mvc = MockMvcBuilders.webAppContextSetup(context).build();
            var response = mvc.perform(get("/v3/api-docs")).andReturn().getResponse();
            assertEquals(200, response.getStatus());
            assertTrue(new ObjectMapper().readTree(response.getContentAsString()).path("openapi").asText().startsWith("3."));
            var ui = mvc.perform(get("/swagger-ui.html")).andReturn().getResponse();
            assertEquals(302, ui.getStatus());
            assertTrue(ui.getRedirectedUrl().contains("/swagger-ui/index.html"));
        }
    }

    @Test
    public void hidesSwaggerUiWhenDevelopmentResourcesAreDisabled() throws Exception {
        try (var context = new AnnotationConfigWebApplicationContext()) {
            context.setServletContext(new MockServletContext());
            context.getEnvironment().getPropertySources().addFirst(
                    new org.springframework.core.env.MapPropertySource("test", java.util.Map.of("application.devel", "false")));
            context.register(DocumentationConfiguration.class);
            context.refresh();
            var mvc = MockMvcBuilders.webAppContextSetup(context).build();
            assertEquals(404, mvc.perform(get("/swagger-ui.html")).andReturn().getResponse().getStatus());
        }
    }

    @Configuration
    @EnableWebMvc
    @Import(SwaggerConfig.class)
    public static class DocumentationConfiguration {
    }
}
