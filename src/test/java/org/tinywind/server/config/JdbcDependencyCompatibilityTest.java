package org.tinywind.server.config;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JdbcDependencyCompatibilityTest {
    @Test
    public void initializesMysqlProtocolMessagesWithTheManagedProtobufRuntime() throws Exception {
        Class<?> message = Class.forName("com.mysql.cj.x.protobuf.MysqlxDatatypes$Any");
        assertNotNull(message.getMethod("getDefaultInstance").invoke(null));
    }
}
