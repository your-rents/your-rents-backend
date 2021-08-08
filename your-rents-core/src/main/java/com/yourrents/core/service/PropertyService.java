package com.yourrents.core.service;

/*-
 * #%L
 * Your Rents Services
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

import com.yourrents.core.dto.Property;
import com.yourrents.data.jooq.tables.records.PropertyRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.TableField;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.yourrents.data.jooq.Tables.PROPERTY;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final DSLContext dsl;

    @Transactional(readOnly = true)
    public Page<Property> list(Pageable pageable) {
        List<PropertyRecord> queryResults = dsl.selectFrom(PROPERTY)
                .orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize()).offset(pageable.getOffset())
                .fetchInto(PropertyRecord.class);
        List<Property> propertyEntries = convertQueryResultsToModelObjects(queryResults);
        long totalCount = dsl.fetchCount(PROPERTY);
        return new PageImpl<>(propertyEntries, pageable, totalCount);
    }

    @Transactional
    public int add(Property property) {
        return dsl.insertInto(PROPERTY,
                PROPERTY.NAME, PROPERTY.DESCRIPTION)
                .values(property.getName(), property.getDescription())
                .execute();
    }

    @SuppressWarnings("rawtypes")
    private Collection<SortField<?>> getSortFields(Sort sortSpecification) {
        Collection<SortField<?>> querySortFields = new ArrayList<>();
        if (sortSpecification == null) {
            return querySortFields;
        }
        Iterator<Sort.Order> specifiedFields = sortSpecification.iterator();
        while (specifiedFields.hasNext()) {
            Sort.Order specifiedField = specifiedFields.next();
            String sortFieldName = specifiedField.getProperty();
            Sort.Direction sortDirection = specifiedField.getDirection();
            TableField tableField = getTableField(sortFieldName);
            SortField<?> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }
        return querySortFields;
    }

    @SuppressWarnings("rawtypes")
    private TableField getTableField(String sortFieldName) {
        TableField sortField = null;
        try {
            Field tableField = PROPERTY.getClass().getField(sortFieldName);
            sortField = (TableField) tableField.get(PROPERTY);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            String errorMessage = String.format("Could not find table field: {}", sortFieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }
        return sortField;
    }

    @SuppressWarnings("rawtypes")
    private SortField<?> convertTableFieldToSortField(TableField tableField, Sort.Direction sortDirection) {
        if (sortDirection == Sort.Direction.ASC) {
            return tableField.asc();
        } else {
            return tableField.desc();
        }
    }

    private List<Property> convertQueryResultsToModelObjects(List<PropertyRecord> queryResults) {
        List<Property> propertyEntries = new ArrayList<>();
        for (PropertyRecord queryResult : queryResults) {
            Property propertyEntry = convertQueryResultToModelObject(queryResult);
            propertyEntries.add(propertyEntry);
        }
        return propertyEntries;
    }

    private Property convertQueryResultToModelObject(PropertyRecord queryResult) {
        return Property.builder().name(queryResult.getName()).description(queryResult.getDescription()).build();
    }
}
