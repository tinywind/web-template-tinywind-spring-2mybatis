package org.tinywind.server.config;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.*;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.tinywind.server.util.ClassUtils;
import org.tinywind.server.util.mybatis.ClobHandler;
import org.tinywind.server.util.mybatis.CodeHasable;
import org.tinywind.server.util.mybatis.CodeHasableHandler;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tinywind
 * @since 2017-11-30
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:application.properties")
// @MapperScan(Constants.REPOSITORY_PACKAGE)
public class PersistenceConfig2 {

    @Value("${jdbc2.driverClass}")
    private String driverClass;
    @Value("${jdbc2.url}")
    private String url;
    @Value("${jdbc2.user}")
    private String user;
    @Value("${jdbc2.password}")
    private String password;

    @Bean("dataSource2")
    public DataSource dataSource() {
        final DriverManagerDataSource dataSourceSpied = new DriverManagerDataSource();
        dataSourceSpied.setDriverClassName(driverClass);
        dataSourceSpied.setUrl(url);
        dataSourceSpied.setUsername(user);
        dataSourceSpied.setPassword(password);

        final Log4JdbcCustomFormatter logFormatter = new Log4JdbcCustomFormatter();
        logFormatter.setLoggingType(LoggingType.MULTI_LINE);

        final Log4jdbcProxyDataSource dataSource = new Log4jdbcProxyDataSource(dataSourceSpied);
        dataSource.setLogFormatter(logFormatter);
        return dataSource;
    }

    @Bean("sqlSessionFactoryBean2")
    public SqlSessionFactoryBean sqlSessionFactoryBean(Environment env) throws Exception {
        driverClass = env.getProperty("jdbc2.driverClass");
        url = env.getProperty("jdbc2.url");
        user = env.getProperty("jdbc2.user");
        password = env.getProperty("jdbc2.password");

        final SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        final org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();

        final org.apache.ibatis.mapping.Environment environment = new org.apache.ibatis.mapping.Environment("mybatis", new JdbcTransactionFactory(), dataSource());
        config.setEnvironment(environment);
        config.setUseGeneratedKeys(true);
        config.setDefaultExecutorType(ExecutorType.REUSE);
        config.setCacheEnabled(true);
        config.setLazyLoadingEnabled(true);
        config.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.WARNING);
        config.setDefaultStatementTimeout(400);
        config.setDefaultFetchSize(100);
        config.setMapUnderscoreToCamelCase(true);
        config.setLocalCacheScope(LocalCacheScope.STATEMENT);
        config.setJdbcTypeForNull(JdbcType.NULL);

        config.getTypeHandlerRegistry().register(BooleanTypeHandler.class);
        config.getTypeHandlerRegistry().register(DateTypeHandler.class);
        config.getTypeHandlerRegistry().register(DateOnlyTypeHandler.class);
        config.getTypeHandlerRegistry().register(SqlDateTypeHandler.class);
        config.getTypeHandlerRegistry().register(TimeOnlyTypeHandler.class);
        config.getTypeHandlerRegistry().register(SqlTimestampTypeHandler.class);
        config.getTypeHandlerRegistry().register(SqlTimeTypeHandler.class);

        config.getTypeHandlerRegistry().register(java.sql.Timestamp.class, DateTypeHandler.class);
        config.getTypeHandlerRegistry().register(java.sql.Timestamp.class, SqlTimestampTypeHandler.class);
        config.getTypeHandlerRegistry().register(java.sql.Time.class, DateTypeHandler.class);
        config.getTypeHandlerRegistry().register(java.sql.Time.class, SqlTimeTypeHandler.class);
        config.getTypeHandlerRegistry().register(java.sql.Date.class, DateTypeHandler.class);
        config.getTypeHandlerRegistry().register(java.sql.Date.class, SqlDateTypeHandler.class);
        config.getTypeHandlerRegistry().register(java.util.Date.class, DateTypeHandler.class);

        config.getTypeHandlerRegistry().register(String.class, JdbcType.LONGVARCHAR, ClobHandler.class);

        factoryBean.setConfiguration(config);
        factoryBean.setDataSource(dataSource());

        Constants.MYBATIS_MODEL_PACKAGES.forEach(aPackage -> config.getTypeAliasRegistry().registerAliases(aPackage));

        List<TypeHandler> handlers = ClassUtils.getClasses(Constants.BASE_PACKAGE, CodeHasable.class).stream()
                .filter(e -> !e.isInterface())
                .distinct()
                .map(e -> new CodeHasableHandler(e))
                .collect(Collectors.toList());

        factoryBean.setTypeHandlers(handlers.toArray(new TypeHandler[handlers.size()]));

        handlers = ClassUtils.getClasses().stream()
                .filter(e -> e.isEnum() && !ClassUtils.isExpands(e, CodeHasable.class))
                .distinct()
                .map(e -> new EnumTypeHandler(e))
                .collect(Collectors.toList());

        factoryBean.setTypeHandlers(handlers.toArray(new TypeHandler[handlers.size()]));

        return factoryBean;
    }

    @Bean("transactionManager2")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean("transactionAwareDataSource2")
    public DelegatingDataSource transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource());
    }

    @Bean("sqlSessionTemplate2")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryBean2") SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
    }

    @Bean("mapperScannerConfigurer2")
    public MapperScannerConfigurer mapperScannerConfigurer(@Qualifier("sqlSessionTemplate2") SqlSessionTemplate sqlSessionTemplate) {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage(Constants.REPOSITORY_PACKAGE2);
        configurer.setAnnotationClass(Repository.class);
        configurer.setSqlSessionTemplateBeanName("sqlSessionTemplate2");

        return configurer;
    }
}