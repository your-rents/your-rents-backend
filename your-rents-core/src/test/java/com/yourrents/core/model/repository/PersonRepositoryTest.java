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

import com.yourrents.core.model.Person;
import com.yourrents.core.test.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
    @Sql({"/sql/person-data.sql"})
    @Transactional
    public void should_findAll() {
        final Iterable<Person> all = personRepository.findAll();
        assertThat(all).hasSize(1);
    }
}
