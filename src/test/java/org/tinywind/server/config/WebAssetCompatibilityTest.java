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
                var location = getClass().getClassLoader().getResource(resource);
                assertNotNull(resource, location);
                if (resource.contains("/font-awesome/") && resource.endsWith(".css")) {
                    String css;
                    try (var input = location.openStream()) {
                        css = new String(input.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                    }
                    var fonts = Pattern.compile("url\\(['\"]?(\\.\\./webfonts/[^'\"?#)]+)").matcher(css);
                    int count = 0;
                    while (fonts.find()) {
                        String font = java.net.URI.create(resource).resolve(fonts.group(1)).getPath();
                        assertNotNull(font, getClass().getClassLoader().getResource(font));
                        count++;
                    }
                    org.junit.Assert.assertTrue("Font Awesome CSS must resolve its web fonts", count > 0);
                }
            }
        }
    }
}
