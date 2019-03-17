package org.tinywind.server.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class UserEntity {
    private Long id;
    private String loginId;
    private String password;
    private Long profileImage;
    private String creator;
    private Timestamp createdAt;

    private String profileImagePath;
}