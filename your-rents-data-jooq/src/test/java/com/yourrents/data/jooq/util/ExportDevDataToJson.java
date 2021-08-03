package com.yourrents.data.jooq.util;

/*-
 * #%L
 * Your Rents Data JOOQ
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

import static com.yourrents.data.jooq.Tables.*;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportDevDataToJson {
    static final Logger log = LoggerFactory.getLogger(ExportDevDataToJson.class);

    static final String DEV_JDBC_URL = "jdbc:postgresql://localhost:25432/your_rents";
    static final String DEV_JDBC_USERNAME = "your_rents";
    static final String DEV_JDBC_PASSWORD = "your_rents";

    static HikariDataSource getTestContainersDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(DEV_JDBC_URL);
        hikariConfig.setUsername(DEV_JDBC_USERNAME);
        hikariConfig.setPassword(DEV_JDBC_PASSWORD);
        return new HikariDataSource(hikariConfig);
    }

    public static void main(String[] args) throws SQLException {
        try (HikariDataSource ds = getTestContainersDataSource();
            Connection conn = ds.getConnection();
        ) {
            DSLContext dsl = DSL.using(conn);
            String json = dsl.select().from(PROPERTY).fetch().formatJSON();
            System.out.println(json);
        }
    }

}
