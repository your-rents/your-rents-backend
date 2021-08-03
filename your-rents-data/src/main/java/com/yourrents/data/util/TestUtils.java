package com.yourrents.data.util;

/*-
 * #%L
 * Your Rents Data
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

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface TestUtils {
    static final Logger log = LoggerFactory.getLogger(TestUtils.class);

    static final String TEST_JDBC_URL = "jdbc:tc:postgresql:13-alpine://localhost:5432/yr?TC_TMPFS=/testtmpfs:rw&TC_INITFUNCTION=com.yourrents.data.util.TestUtils::initDatabase";
    static final String TEST_JDBC_USERNAME = "test";
    static final String TEST_JDBC_PASSWORD = "test";

    static HikariDataSource getTestContainersDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(TEST_JDBC_URL);
        hikariConfig.setUsername(TEST_JDBC_USERNAME);
        hikariConfig.setPassword(TEST_JDBC_PASSWORD);
        return new HikariDataSource(hikariConfig);
    }

    static void initDatabase(Connection conn) throws SQLException {
        log.info("Initializing Your Rents test database ({})", conn.getMetaData().getURL());
        Flyway flyway = Flyway.configure()
                .dataSource(conn.getMetaData().getURL(), TEST_JDBC_USERNAME, TEST_JDBC_PASSWORD).load();
        flyway.migrate();
    }

}
