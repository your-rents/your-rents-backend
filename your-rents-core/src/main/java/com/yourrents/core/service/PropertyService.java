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

import java.util.List;
import java.util.UUID;

import com.yourrents.core.NotFoundException;
import com.yourrents.core.dto.Property;
import com.yourrents.core.dto.PropertyConverter;
import com.yourrents.core.util.SortToSortFieldsTransformer;
import com.yourrents.data.jooq.daos.PropertyDao;
import com.yourrents.data.jooq.tables.records.PropertyRecord;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyDao propertyDao;
    private final PropertyConverter propertyConverter;
    private final SortToSortFieldsTransformer sortToSortFieldsTransformer;

    @Transactional(readOnly = true)
    public Page<Property> list(Pageable pageable) {
        List<PropertyRecord> queryResults = propertyDao.findAll(pageable.getPageSize(), pageable.getOffset(),
                sortToSortFieldsTransformer.transform(pageable.getSort()));
        List<Property> propertyEntries = propertyConverter.convert(queryResults);
        long totalCount = propertyDao.countAll();
        return new PageImpl<>(propertyEntries, pageable, totalCount);
    }

    @Transactional
    public UUID add(Property property) {
        return propertyDao.insert(property.getName(), property.getDescription());
    }

    @Transactional(readOnly = true)
    public Property get(UUID uuid) {
        return propertyDao.findByExternalId(uuid)
                .map((propertyRecord) -> propertyConverter.convert(propertyRecord))
                .orElseThrow(() -> new NotFoundException("No Property found having external_id " + uuid));
    }

    @Transactional
    public int update(Property property) {
        return propertyDao.updateByExternalId(property.getExternal_id(), property.getName(), property.getDescription());
    }

}
