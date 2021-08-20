package com.yourrents.core.test;
/*-
 * #%L
 * Your Rents Core
 * %%
 * Copyright (C) 2019 Your Rents Team
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
import com.yourrents.data.jooq.daos.PropertyDao;
import com.yourrents.data.util.TestUtils;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Test configuration for using JOOQ in Spring.
 *
 * @see <a href=
 * "https://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-configuration/">https://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-configuration/</a>
 * @see <a href=
 * "https://www.jooq.org/doc/latest/manual/getting-started/tutorials/jooq-with-spring/">https://www.jooq.org/doc/latest/manual/getting-started/tutorials/jooq-with-spring/</a>
 */
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.yourrents.core", "com.yourrents.core.dto"})
@Configuration
public class TestConfig {

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return TestUtils.getTestContainersDataSource();
    }

    @Bean
    public LazyConnectionDataSourceProxy lazyConnectionDataSource() {
        return new LazyConnectionDataSourceProxy(dataSource());
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(lazyConnectionDataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(lazyConnectionDataSource());
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }

    @Bean
    public JOOQToSpringExceptionTransformer jooqToSpringExceptionTransformer() {
        return new JOOQToSpringExceptionTransformer();
    }

    @Bean
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();

        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(new DefaultExecuteListenerProvider(jooqToSpringExceptionTransformer()));
        jooqConfiguration.set(SQLDialect.POSTGRES);

        return jooqConfiguration;
    }

    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    @Bean
    @Primary
    public PropertyDao mockPropertyDao() {
        return Mockito.mock(PropertyDao.class);
    }
}
