package com.yourrents.core.util;

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

import com.yourrents.core.dto.Municipality;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    private static final String FIELD_SEPARATOR = ";";

    private static final int CODE_POS = 4;
    private static final int NAME_POS = 5;
    private static final int REGION_POS = 10;
    private static final int PROVINCE_POS = 11;

    public Municipality stringToMunicipality(String line) {
        String[] fields = line.split(FIELD_SEPARATOR);
        return Municipality.builder()
                .code(fields[CODE_POS])
                .name(fields[NAME_POS])
                .province(fields[PROVINCE_POS])
                .region(fields[REGION_POS])
                .build();
    }
}
