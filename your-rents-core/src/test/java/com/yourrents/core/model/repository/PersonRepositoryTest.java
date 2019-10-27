package com.yourrents.core.model.repository;
/*-
 * #%L
 * Your Rents Core
 * %%
 * Copyright (C) 2019 Your Rents Team
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

import com.yourrents.core.model.Gender;
import com.yourrents.core.model.Person;
import com.yourrents.core.test.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
/**
 * Porpoise of this test is just to validate the spring data test configuration.
 * It will be dismissed as soon as meaningful tests will be committed.
 */
public class PersonRepositoryTest {
    @Resource
    private PersonRepository personRepository;

    @Test
    @Sql({"/sql/cleanup.sql", "/sql/person-data.sql"})
    @Transactional
    public void should_addPerson() {
        //given
        List<Person> all = personRepository.findAll();
        assertThat(all).hasSize(2);
        //when
        final Person jd = Person.builder()
                .id(3L)
                .birthDate(LocalDate.of(1980, Month.APRIL, 14))
                .email("a@b.com")
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.MALE)
                .build();
        personRepository.save(jd);
        //then
        all = personRepository.findAll();
        assertThat(all).hasSize(3);
    }

    @Test
    @Sql({"/sql/cleanup.sql", "/sql/person-data.sql"})
    @Transactional
    public void should_findWomen() {
        final List<Person> women = personRepository.findByGender(Gender.FEMALE);
        assertThat(women).hasSize(1);
    }
}
