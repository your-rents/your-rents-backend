package com.yourrents.core.util;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MapperTest {
    static final String SPINEA = "05;227;027; 038 ;027038;Spinea;Spinea;;2;Nord-est;Veneto;Venezia;3;0;VE;27038;27038;27038;27038;I908;ITH;ITH3;ITH35;ITH;ITH3;ITH35";
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new Mapper();
    }

    @Test
    void stringToMunicipality() {
        //given-when
        Municipality spinea = mapper.stringToMunicipality(SPINEA);

        //then
        assertThat(spinea).isNotNull();
        assertThat(spinea.getName()).isEqualTo("Spinea");
        assertThat(spinea.getProvince()).isEqualTo("Venezia");
        assertThat(spinea.getRegion()).isEqualTo("Veneto");
    }
}
