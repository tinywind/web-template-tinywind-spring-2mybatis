package org.tinywind.server.service;

import org.tinywind.server.config.PersistenceConfig1;
import org.tinywind.server.repository1.FileRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig1.class})
public class SampleTest {
    private static final Logger logger = LoggerFactory.getLogger(SampleTest.class);

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private FileRepository repository;

    @Test
    public void dummy() {
        assert String.format("%.03f", 1.0).equals("1.000");
        assert String.format("%.03f", 1.01).equals("1.010");
        assert String.format("%.03f", 1.2001).equals("1.200");
    }
}
