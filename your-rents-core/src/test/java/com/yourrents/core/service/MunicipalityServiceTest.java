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

import com.yourrents.core.config.IstatDataFactory;
import com.yourrents.core.dto.Municipality;
import com.yourrents.core.test.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class MunicipalityServiceTest {

    @Autowired
    private MunicipalityService municipalityService;


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
        Municipality spinea = municipalityService.findByCode("027038");
        //then
        assertThat(spinea).isNotNull();
        assertThat(spinea.getName()).isEqualTo("Spinea");
    }

    @Test
    void findByCode_NotFound() {
        assertThatThrownBy(() -> municipalityService.findByCode("xxx")).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("xxx");
    }

}
