package com.maxgarfinkel.treeStore.configuration;

import com.maxgarfinkel.treeStore.infrastructure.TreeSqlRepository;
import com.maxgarfinkel.treeStore.model.TreeRepository;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class RealTreeConfig {

    @Bean
    public TreeRepository treeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new TreeSqlRepository(jdbcTemplate);
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/treeDB");
        dataSource.setUsername("treeUser");
        dataSource.setPassword("1q2w3e4r");
        return dataSource;
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    webServerFactoryCustomizer() {
        return factory -> factory.setContextPath("/realTree");
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource());
        txManager.setDefaultTimeout(2);
        return txManager;
    }
}
