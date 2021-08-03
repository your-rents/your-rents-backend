package com.yourrents.data;

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

import com.yourrents.data.util.TestUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseDataTest {

    private static HikariDataSource sharedTcDs;

    @BeforeAll
    public static void initTestClass() {
        sharedTcDs = TestUtils.getTestContainersDataSource();
    }

    @AfterAll
    public static void cleanTestClass() {
        sharedTcDs.close();
    }

    @Test
    public void testCurrentDatabaseVersion() throws SQLException {
        try (Connection conn = sharedTcDs.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "select version, success from flyway_schema_history order by installed_rank desc");
                ResultSet rs = stmt.executeQuery();) {
            if (rs.next()) {
                assertEquals(1, rs.getInt("version"));
                assertTrue(rs.getBoolean("success"));
            }
        }
    }

    @Test
    public void isolatedSimpleTest() throws SQLException {
        try (HikariDataSource ds = TestUtils.getTestContainersDataSource()) {
            try (Connection conn = ds.getConnection();
                    PreparedStatement stmt = conn.prepareStatement("select 1");
                    ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    assertEquals(1, rs.getInt(1));
                }
            }
        }
    }

}
