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
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lambico.datatest.annotation.JpaTest;
import org.lambico.datatest.annotation.TestData;
import org.lambico.datatest.junit.Dataset;
import org.lambico.datatest.junit.JpaContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestData(resources = "dataset/dataset.json")
@JpaTest(loadEntities = {Person.class},
        useMerge = true)
/**
 * Porpoise of this test is just to validate the spring data test configuration.
 * It will be dismissed as soon as meaningful tests will be committed.
 */
public class PersonRepositoryTest {
    @Resource
    private PersonRepository personRepository;
    @ClassRule
    public static Dataset dataset = Dataset.builder().build();

    @Rule
    public JpaContext jpaContext = new JpaContext(dataset.getEntityManagerFactory());


    @Test
    @Transactional
    @Ignore
    public void should_findAll() {
        final Iterable<Person> all = personRepository.findAll();
        assertThat(all).hasSize(1);
    }
}
