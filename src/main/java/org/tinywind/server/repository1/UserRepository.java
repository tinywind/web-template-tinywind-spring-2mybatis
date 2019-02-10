package org.tinywind.server.repository1;

import org.springframework.stereotype.Repository;
import org.tinywind.server.model.UserEntity;
import org.tinywind.server.model.form.LoginForm;

@Repository
public interface UserRepository {
    UserEntity findOneById(String id);

    UserEntity findOneByIdAndPassword(LoginForm form);
}
