package com.yourrents.data.jooq;

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

import com.yourrents.data.util.TestUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;

import static com.yourrents.data.jooq.Tables.PROPERTY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertyTest {
    private static HikariDataSource sharedTcDs;
    private static DSLContext dsl;

    @BeforeAll
    public static void initTestClass() throws SQLException, IOException {
        sharedTcDs = TestUtils.getTestContainersDataSource();
        dsl = DSL.using(sharedTcDs.getConnection());

        InputStream is = PropertyTest.class.getClassLoader().getResourceAsStream("com/yourrents/data/jooq/testdata/property.json");
        dsl.loadInto(PROPERTY).loadJSON(is, Charset.forName("UTF-8")).fieldsCorresponding().execute();
    }

    @AfterAll
    public static void cleanTestClass() {
        sharedTcDs.close();
    }

    @Test
    public void testFindAll() throws SQLException {
        Result<Record> result = dsl.select().from(PROPERTY).fetch();
        assertEquals(2, result.size());
    }

}
