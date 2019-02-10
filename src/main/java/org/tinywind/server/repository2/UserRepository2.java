package org.tinywind.server.repository2;

import org.springframework.stereotype.Repository;
import org.tinywind.server.model.UserEntity;
import org.tinywind.server.model.form.LoginForm;

@Repository
public interface UserRepository2 {
    void insert(UserEntity user);
}
