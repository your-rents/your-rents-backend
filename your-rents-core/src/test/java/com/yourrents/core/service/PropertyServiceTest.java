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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.yourrents.core.NotFoundException;
import com.yourrents.core.dto.Property;
import com.yourrents.core.test.TestConfig;
import com.yourrents.data.jooq.daos.PropertyDao;
import com.yourrents.data.jooq.tables.records.PropertyRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class PropertyServiceTest {
    private static final UUID EXISTING_UUID = UUID.fromString("a444c18e-01ad-4aef-9365-6f42752faa62");
    private static final UUID NOT_EXISTING_UUID = UUID.fromString("e5c1ec0b-95d4-4e9f-b5d1-12c55812dbe7");

    @Autowired
    private PropertyDao mockPropertyDao;

    @Autowired
    private PropertyService propertyService;

    @BeforeEach
    public void initTest() {
        Mockito.reset(mockPropertyDao);
    }

    @Test
    public void findAll() {
        when(mockPropertyDao.findAll(100, 0, List.of()))
            .thenReturn(List.of(new PropertyRecord(), new PropertyRecord()));

        Page<Property> result = propertyService.list(Pageable.ofSize(100));
        assertThat(result.getNumberOfElements()).isEqualTo(2);
    }

    
    @Test
    void add() {
        //given

        //when
        propertyService.add(Property.builder()
            .name("flat-A")
            .description("short description")
            .build());

        //then
        verify(mockPropertyDao, times(1)).insert("flat-A", "short description");
    }

    @Test
    void update() throws IOException {
        // given
        when(mockPropertyDao.updateByExternalId(EXISTING_UUID, "flat-A", "short description")).thenReturn(1);

        // when
        int update = propertyService.update(
                Property.builder().external_id(EXISTING_UUID).name("flat-A").description("short description").build());
        // then
        assertThat(update).isEqualTo(1);
    }

    @Test
    void get() throws IOException {
        // given
        when(mockPropertyDao.findByExternalId(EXISTING_UUID))
            .thenReturn(Optional.of(
                new PropertyRecord(1, "First property", "The first property added to the database",
                    null, EXISTING_UUID)));

        // when
        Property property = propertyService.get(EXISTING_UUID);
        // then
        assertThat(property).isNotNull();
        assertThat(property.getName()).isEqualTo("First property");
        assertThat(property.getDescription()).isEqualTo("The first property added to the database");
        assertThat(property.getExternal_id()).isEqualTo(EXISTING_UUID);
    }

    @Test
    void get_notFound() throws IOException {
        // given
        when(mockPropertyDao.findByExternalId(NOT_EXISTING_UUID)).thenReturn(Optional.empty());

        // when-then
        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> propertyService.get(NOT_EXISTING_UUID))
                .withMessageContaining(NOT_EXISTING_UUID.toString());
    }

}
