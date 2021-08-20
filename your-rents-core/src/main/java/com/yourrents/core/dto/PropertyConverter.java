package com.yourrents.core.dto;

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

import com.yourrents.data.jooq.tables.records.PropertyRecord;

import org.springframework.stereotype.Component;

@Component
public class PropertyConverter implements Converter<PropertyRecord, Property> {

    @Override
    public Property apply(PropertyRecord record) {
        return Property.builder()
            .external_id(record.getExternalId())
            .name(record.getName())
            .description(record.getDescription())
            .build();
    }
    
}