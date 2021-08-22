package com.yourrents.core.util;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.yourrents.data.jooq.Tables;

import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class SortToSortFieldsTransformer {

    /**
     * Convert a Spring sort specification to a collection of JOOQ SortField objects.
     * 
     * The property names in the Spring sort specification must be in the form "TABLE.FIELD",
     * all in uppercase.
     * 
     * @param sortSpecification The Spring sort specification
     * @return The JOOQ SortField objects
     */
    @SuppressWarnings("rawtypes")
    public Collection<SortField<?>> transform(Sort sortSpecification) {
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
        String[] tableAndField = sortFieldName.split("\\.");
        TableField sortField;
        try {
            Table table = (Table) Tables.class.getField(tableAndField[0]).get(null);
            Field tableField = table.getClass().getField(tableAndField[1]);
            sortField = (TableField) tableField.get(table);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            String errorMessage = String.format("Could not find table field: %s", sortFieldName);
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
    
}
