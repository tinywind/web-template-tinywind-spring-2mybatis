package org.tinywind.server.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FileEntity {
    private Long id;
    private Long size;
    private String originalName;
    private String path;
    private Timestamp createdAt;
}
