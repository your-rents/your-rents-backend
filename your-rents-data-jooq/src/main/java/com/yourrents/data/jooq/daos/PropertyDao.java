package com.yourrents.data.jooq.daos;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.yourrents.data.jooq.tables.records.PropertyRecord;

import static com.yourrents.data.jooq.Tables.PROPERTY;

/*-
 * #%L
 * Your Rents Data JOOQ
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
import org.jooq.SortField;

public class PropertyDao {

    private DSLContext dsl;

    /**
     * Create a new PropertyDao with an attached configuration
     */
    public PropertyDao(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<PropertyRecord> findAll(int pageSize, long offset, Collection<SortField<?>> sortFields) {
        return dsl.selectFrom(PROPERTY).orderBy(sortFields).limit(pageSize).offset(offset)
                .fetchInto(PropertyRecord.class);
    }

    public long countAll() {
        return dsl.fetchCount(PROPERTY);
    }

    public Optional<PropertyRecord> findByExternalId(UUID uuid) {
        return Optional.ofNullable(dsl.selectFrom(PROPERTY).where(PROPERTY.EXTERNAL_ID.eq(uuid)).fetchOne());
    }

    public UUID insert(String name, String description) {
        return dsl.insertInto(PROPERTY, PROPERTY.NAME, PROPERTY.DESCRIPTION).values(name, description)
                .returningResult(PROPERTY.EXTERNAL_ID).fetchOne().component1();
    }

    public int updateByExternalId(UUID externalId, String name, String description) {
        return dsl.update(PROPERTY).set(PROPERTY.NAME, name).set(PROPERTY.DESCRIPTION, description)
                .where(PROPERTY.EXTERNAL_ID.eq(externalId)).execute();
    }

}
