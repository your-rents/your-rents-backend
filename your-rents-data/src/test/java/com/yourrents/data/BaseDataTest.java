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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.yourrents.data.util.TestUtils;
import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.Test;

public class BaseDataTest {

    @Test
    public void testSimple() throws SQLException {
        try (HikariDataSource ds = TestUtils.getTestContainersDataSource()) {
            try (Connection conn = ((DataSource)ds).getConnection();
                    PreparedStatement stmt = conn.prepareStatement("select 1");
                    ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    assertEquals(1, rs.getInt(1));
                }
            }
        }
    }

}
