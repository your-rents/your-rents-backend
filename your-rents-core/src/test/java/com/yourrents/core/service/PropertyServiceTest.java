package com.yourrents.core.service;

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

import com.yourrents.core.EnvironmentTest;
import com.yourrents.core.YRNotFoundException;
import com.yourrents.core.dto.Property;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.yourrents.data.jooq.Tables.PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class PropertyServiceTest {
    private static final UUID EXISTING_UUID = UUID.fromString("a444c18e-01ad-4aef-9365-6f42752faa62");
    private static final UUID NOT_EXISTING_UUID = UUID.fromString("e5c1ec0b-95d4-4e9f-b5d1-12c55812dbe7");

    @Autowired
    private DSLContext dsl;

    @Autowired
    private PropertyService propertyService;

    @BeforeEach
    public void initTestClass() {
        dsl.deleteFrom(PROPERTY).execute();
    }

    @Test
    void add() {
        //given
        Result<Record> result = dsl.select().from(PROPERTY).fetch();
        assertThat(result.isEmpty()).isTrue();

        //when
        propertyService.add(Property.builder()
                .name("flat-A")
                .description("short description")
                .build());

        //then
        result = dsl.select().from(PROPERTY).fetch();
        assertThat(result).hasSize(1);
    }

    @Test
    void update() throws IOException {
        //given
        populateDB();

        //when
        int update = propertyService.update(Property.builder()
                .external_id(EXISTING_UUID)
                .name("flat-A")
                .description("short description")
                .build());
        //then
        assertThat(update).isEqualTo(1);
    }

    @Test
    void get() throws IOException {
        //given
        populateDB();

        //when
        Property property = propertyService.get(EXISTING_UUID);
        //then
        assertThat(property).isNotNull();
        assertThat(property.getName()).isEqualTo("First property");
        assertThat(property.getDescription()).isEqualTo("The first property added to the database");
        assertThat(property.getExternal_id()).isEqualTo(EXISTING_UUID);
    }

    @Test
    void get_notFound() throws IOException {
        //given
        populateDB();

        //when-then
        assertThatExceptionOfType(YRNotFoundException.class)
                .isThrownBy(() -> propertyService.get(NOT_EXISTING_UUID)
                ).withMessageContaining(NOT_EXISTING_UUID.toString());
    }

    private void populateDB() throws IOException {
        InputStream is = EnvironmentTest.class.getClassLoader().getResourceAsStream("com/yourrents/core/testdata/property.json");
        dsl.loadInto(PROPERTY).loadJSON(is, StandardCharsets.UTF_8).fieldsCorresponding().execute();
    }
}
