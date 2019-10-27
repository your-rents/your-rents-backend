package com.yourrents.core.model;

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

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.lambico.datatest.annotation.JpaTest;
import org.lambico.datatest.annotation.Property;
import org.lambico.datatest.annotation.TestData;
import org.lambico.datatest.junit.Dataset;
import org.lambico.datatest.junit.JpaContext;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@TestData(resources = "dataset/dataset.json")
@JpaTest(entities = {Person.class},
        properties = {@Property(name = "hibernate.show_sql", value = "true")})
public class PersonTest {

    @ClassRule
    public static Dataset dataset = Dataset.builder().build();

    @Rule
    public JpaContext jpaContext = new JpaContext(dataset.getEntityManagerFactory());

    @Test
    public void findObjects() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Person> query = em.createQuery("select p from Person p", Person.class);
        List<Person> results = query.getResultList();
        assertThat(results.size(), is(1));
    }
}
