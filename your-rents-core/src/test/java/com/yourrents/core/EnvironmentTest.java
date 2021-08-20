package com.yourrents.core;

/*-
 * #%L
 * Your Rents Core
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

import static com.yourrents.data.jooq.Tables.PROPERTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;

import com.yourrents.core.test.TestConfig;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Some simple tests for checking the module environment. Other tests should mock jooq dependencies.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class EnvironmentTest {

    @Autowired
    private DSLContext dsl;

    @BeforeEach
    public void initTestClass() throws SQLException, IOException {
        dsl.deleteFrom(PROPERTY).execute();
        InputStream is = EnvironmentTest.class.getClassLoader().getResourceAsStream("com/yourrents/core/testdata/property.json");
        dsl.loadInto(PROPERTY).loadJSON(is, Charset.forName("UTF-8")).fieldsCorresponding().execute();        
    }

    @Test
    public void testTestEnvironment() {
        assertTrue(true);
    }

    @Test
    public void testFindAllProperties() throws SQLException {
        Result<Record> result = dsl.select().from(PROPERTY).fetch();
        assertEquals(2, result.size());
    }
    
}
