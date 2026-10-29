package org.tinywind.server.config;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;

public class WebAssetCompatibilityTest {
    @Test
    public void resolvesEveryWebJarReferencedByThePageTags() throws Exception {
        Pattern references = Pattern.compile("/webjars/([^\"]+)");
        for (String tag : new String[]{"js.tag", "css.tag"}) {
            String source = Files.readString(Path.of("src/main/webapp/WEB-INF/tags", tag));
            var matches = references.matcher(source);
            while (matches.find()) {
                String resource = "META-INF/resources/webjars/" + matches.group(1);
                assertNotNull(resource, getClass().getClassLoader().getResource(resource));
            }
        }
    }
}
