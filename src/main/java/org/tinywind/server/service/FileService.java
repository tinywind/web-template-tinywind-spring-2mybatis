package org.tinywind.server.service;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tinywind.server.model.FileEntity;
import org.tinywind.server.model.form.FileUploadForm;
import org.tinywind.server.repository1.FileRepository;
import org.tinywind.server.util.UrlUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author tinywind
 * @since 2018-01-14
 */
@Service
@PropertySource("classpath:application.properties")
public class FileService {
    public static final String FILE_PATH = "files/download";
    public static final String FILE_REQUEST_PARAM_KEY = "file";
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private FileRepository fileRepository;

    @Value("${application.file.location}")
    private String fileLocation;

    public String url(Long fileId) {
        if (fileId == null)
            return null;

        return url(fileRepository.findOne(fileId));
    }

    public String url(FileEntity fileEntity) {
        if (fileEntity == null)
            return null;

        return url(fileEntity.getPath());
    }

    public String url(String path) {
        if (path == null)
            return null;

        return baseUrl() + "?" + UrlUtils.encodeQueryParams(FILE_REQUEST_PARAM_KEY, path);
    }

    public String baseUrl() {
        return ("/" + FILE_PATH + "/").replaceAll("[/]+", "/");
    }

    public Long save(String fileName, byte[] bytes) throws IOException {
        final String filePath = System.currentTimeMillis() + "_" + System.nanoTime() + "_" + fileName;
        final File file = new File(fileLocation, filePath);
        try (final FileOutputStream writer = new FileOutputStream(file)) {
            writer.write(bytes);
            writer.flush();

            final FileEntity fileEntity = new FileEntity();
            fileEntity.setOriginalName(fileName);
            fileEntity.setPath(filePath);
            fileEntity.setSize((long) bytes.length);
            return fileRepository.save(fileEntity);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            boolean deleted = file.delete();
            if (!deleted) logger.error("Failed: delete file: " + file.getAbsolutePath());

            throw e;
        }
    }

    public Long save(FileUploadForm form) throws IOException {
        return save(form.getFileName(), Base64.decodeBase64(form.getData()));
    }

    public Long save(MultipartFile file) throws IOException {
        return save(file.getOriginalFilename(), file.getBytes());
    }
}
