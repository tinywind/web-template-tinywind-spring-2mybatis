package org.tinywind.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tinywind.server.model.UserEntity;
import org.tinywind.server.repository1.UserRepository;
import org.tinywind.server.repository2.UserRepository2;

/**
 * @author tinywind
 * @since 2018-01-14
 */
@Service
@PropertySource("classpath:application.properties")
public class MultipleTransactionTest {
    private static final Logger logger = LoggerFactory.getLogger(MultipleTransactionTest.class);

    @Autowired
    private UserRepository repository1;
    @Autowired
    private UserRepository2 repository2;

    @Transactional("transactionManager1")
    public void forceError1() {
        final UserEntity user = new UserEntity();
        user.setLoginId("1234");
        user.setPassword("1234");
        repository1.insert(user);

        throw new RuntimeException();
    }

    @Transactional("transactionManager2")
    public void forceError2() {
        final UserEntity user = new UserEntity();
        user.setLoginId("1234");
        user.setPassword("1234");
        repository2.insert(user);

        throw new RuntimeException();
    }
}
