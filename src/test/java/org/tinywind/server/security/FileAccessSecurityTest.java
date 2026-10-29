package org.tinywind.server.security;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.tinywind.server.controller.web.MainController;
import org.tinywind.server.service.FileService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FileAccessSecurityTest {
    @Rule
    public TemporaryFolder temporary = new TemporaryFolder(new File("target"));

    @Test
    public void downloadsAnOrdinaryUnicodeFile() throws Exception {
        Path root = temporary.newFolder("files").toPath();
        Files.write(root.resolve("report 한글.txt"), "stored-content".getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = download(root, "report 한글.txt");
        assertEquals(200, response.getStatus());
        assertEquals("stored-content", response.getContentAsString());
    }

    @Test
    public void rejectsTraversalAndAbsoluteDownloadNames() throws Exception {
        Path root = temporary.newFolder("files").toPath();
        Path outside = temporary.getRoot().toPath().toAbsolutePath().resolve("outside.txt");
        Files.write(outside, "private".getBytes(StandardCharsets.UTF_8));
        for (String name : new String[]{"../outside.txt", outside.toString(), "..\\outside.txt", ".", "bad\r\nname"}) {
            MockHttpServletResponse response = download(root, name);
            assertEquals(name, 400, response.getStatus());
            assertEquals(name, "", response.getContentAsString());
        }
    }

    @Test
    public void doesNotFollowAStoredSymbolicLink() throws Exception {
        Path root = temporary.newFolder("files").toPath();
        Path outside = temporary.getRoot().toPath().toAbsolutePath().resolve("outside.txt");
        Files.write(outside, "private".getBytes(StandardCharsets.UTF_8));
        Files.createSymbolicLink(root.resolve("linked.txt"), outside);
        MockHttpServletResponse response = download(root, "linked.txt");
        assertEquals(404, response.getStatus());
        assertEquals("", response.getContentAsString());
    }

    @Test
    public void rejectsUploadPathsBeforeCreatingFiles() throws Exception {
        Path root = temporary.newFolder("files").toPath();
        FileService service = new FileService();
        ReflectionTestUtils.setField(service, "fileLocation", root.toString());
        for (String name : new String[]{"../outside.txt", "/outside.txt", "..\\outside.txt", "bad\r\nname"}) {
            assertThrows(name, IllegalArgumentException.class, () -> service.save(name, new byte[]{1}));
        }
        try (java.util.stream.Stream<Path> files = Files.list(root)) {
            assertEquals(0, files.count());
        }
    }

    private MockHttpServletResponse download(Path root, String name) throws Exception {
        MainController controller = new MainController();
        ReflectionTestUtils.setField(controller, "fileLocation", root.toString());
        MockHttpServletResponse response = new MockHttpServletResponse();
        controller.downloadFile(response, name);
        return response;
    }
}
