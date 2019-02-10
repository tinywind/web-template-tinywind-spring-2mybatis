package org.tinywind.server.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class UserEntity {
    private Long id;
    private String loginId;
    private String password;
    private String creator;
    private Timestamp createdAt;
}