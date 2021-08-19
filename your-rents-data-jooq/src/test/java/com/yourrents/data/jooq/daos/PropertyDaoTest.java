package com.yourrents.data.jooq.daos;

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

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import com.zaxxer.hikari.HikariDataSource;
import com.yourrents.data.jooq.tables.records.PropertyRecord;
import com.yourrents.data.util.TestUtils;

import static com.yourrents.data.jooq.Tables.PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PropertyDaoTest {
    private static final UUID EXISTING_UUID = UUID.fromString("a444c18e-01ad-4aef-9365-6f42752faa62");
    private static final UUID NOT_EXISTING_UUID = UUID.fromString("e5c1ec0b-95d4-4e9f-b5d1-12c55812dbe7");

    private static HikariDataSource sharedTcDs;
    private static DSLContext dsl;
    private static PropertyDao propertyDao;

    @BeforeAll
    public static void initTestClass() throws SQLException, IOException {
        sharedTcDs = TestUtils.getTestContainersDataSource();
        dsl = DSL.using(sharedTcDs.getConnection());
        propertyDao = new PropertyDao(dsl);
    }

    @AfterAll
    public static void cleanTestClass() {
        sharedTcDs.close();
    }

    @BeforeEach
    public void initTest() {
        dsl.deleteFrom(PROPERTY).execute();
    }

    @Test
    void insert() {
        //given
        Result<Record> result = dsl.select().from(PROPERTY).fetch();
        assertThat(result.isEmpty()).isTrue();

        //when
        propertyDao.insert("flat-A", "short description");

        //then
        result = dsl.select().from(PROPERTY).fetch();
        assertThat(result).hasSize(1);
    }

    @Test
    void updateByExternalId() throws IOException {
        //given
        populateDB();

        //when
        int update = propertyDao.updateByExternalId(EXISTING_UUID, "flat-A", "short description");

        //then
        assertThat(update).isEqualTo(1);
    }

    @Test
    void findByExternalId() throws IOException {
        //given
        populateDB();

        //when
        Optional<PropertyRecord> property = propertyDao.findByExternalId(EXISTING_UUID);
        //then
        assertThat(property.isPresent()).isTrue();
        assertThat(property.get().getName()).isEqualTo("First property");
        assertThat(property.get().getDescription()).isEqualTo("The first property added to the database");
        assertThat(property.get().getExternalId()).isEqualTo(EXISTING_UUID);
    }

    @Test
    void get_notFound() throws IOException {
        //given
        populateDB();

        //when
        Optional<PropertyRecord> property = propertyDao.findByExternalId(NOT_EXISTING_UUID);
        //then
        assertThat(property.isPresent()).isFalse();
    }

    private void populateDB() throws IOException {
        InputStream is = PropertyDaoTest.class.getClassLoader().getResourceAsStream("com/yourrents/data/jooq/testdata/property.json");
        dsl.loadInto(PROPERTY).loadJSON(is, StandardCharsets.UTF_8).fieldsCorresponding().execute();
    }
}
