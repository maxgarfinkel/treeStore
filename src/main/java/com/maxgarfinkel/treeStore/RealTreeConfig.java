package com.maxgarfinkel.treeStore;

import com.maxgarfinkel.treeStore.repository.TreeSqlRepository;
import com.maxgarfinkel.treeStore.repository.TreeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class RealTreeConfig {

    @Bean
    public TreeRepository treeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new TreeSqlRepository(jdbcTemplate);
    }
}
