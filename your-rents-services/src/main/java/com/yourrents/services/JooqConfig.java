package com.yourrents.services;

/*-
 * #%L
 * Your Rents Services
 * %%
 * Copyright (C) 2019 - 2021 Your Rents Team
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.yourrents.core.util.JOOQToSpringExceptionTransformer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class JooqConfig {

    @Autowired
    private Environment env;
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        hikariConfig.setUsername(env.getRequiredProperty("spring.datasource.username"));
        hikariConfig.setPassword(env.getRequiredProperty("spring.datasource.password"));
        hikariConfig.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LazyConnectionDataSourceProxy lazyConnectionDataSource(final DataSource dataSource) {
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource(LazyConnectionDataSourceProxy lazyConnectionDataSource) {
        return new TransactionAwareDataSourceProxy(lazyConnectionDataSource);
    }

    @Bean
    public DataSourceTransactionManager transactionManager(LazyConnectionDataSourceProxy lazyConnectionDataSource) {
        return new DataSourceTransactionManager(lazyConnectionDataSource);
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider(TransactionAwareDataSourceProxy transactionAwareDataSource) {
        return new DataSourceConnectionProvider(transactionAwareDataSource);
    }

    @Bean
    public JOOQToSpringExceptionTransformer jooqToSpringExceptionTransformer() {
        return new JOOQToSpringExceptionTransformer();
    }

    @Bean
    public DefaultConfiguration configuration(DataSourceConnectionProvider connectionProvider) {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();

        jooqConfiguration.set(connectionProvider);
        jooqConfiguration.set(new DefaultExecuteListenerProvider(jooqToSpringExceptionTransformer()));
        jooqConfiguration.set(SQLDialect.POSTGRES);

        return jooqConfiguration;
    }

    @Bean
    public DefaultDSLContext dsl(DefaultConfiguration configuration) {
        return new DefaultDSLContext(configuration);
    }

}
