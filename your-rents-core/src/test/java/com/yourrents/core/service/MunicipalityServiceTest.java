package com.yourrents.core.service;

/*-
 * #%L
 * Your Rents Core
 * %%
 * Copyright (C) 2019 - 2022 Your Rents Team
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

import com.yourrents.core.dto.Municipality;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class MunicipalityServiceTest {
    private MunicipalityService municipalityService;

    @BeforeEach
    void setUp() {
        this.municipalityService = new MunicipalityService(buildList());
    }

    @Test
    void findAll() {
        //when
        Page<Municipality> list = municipalityService.list(Pageable.ofSize(2));
        //then
        assertThat(list).isNotNull();
        assertThat(list).hasSize(2);
    }

    @Test
    void findByCode() {
        //when
        Municipality spinea = municipalityService.findByCode("1");
        //then
        assertThat(spinea).isNotNull();
        assertThat(spinea.getName()).isEqualTo("Spinea");
    }

    @Test
    void findByCode_NotFound() {
        assertThatThrownBy(() -> municipalityService.findByCode("xxx")).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("xxx");
    }

    public static List<Municipality> buildList() {
        return List.of(Municipality.builder()
                        .code("1")
                        .name("Spinea")
                        .province("Venezia")
                        .region("Veneto")
                        .build(),
                Municipality.builder()
                        .code("2")
                        .name("San Bonifacio")
                        .province("Verona")
                        .region("Veneto")
                        .build()
        );
    }

}
