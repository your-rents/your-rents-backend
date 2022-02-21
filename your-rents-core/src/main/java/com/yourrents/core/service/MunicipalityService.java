package com.yourrents.core.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MunicipalityService {
    private final List<Municipality> municipalities;

    public Municipality findByCode(String code) {
        return municipalities.stream()
                .filter(municipality -> municipality.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No municipality found having code: " + code));
    }

    /**
     * Pagination service.
     * Thanks to  @see <a href="https://careydevelopment.us/blog/java-streams-how-to-implement-pagination-with-skip-and-limit">careydevelopment</a>
     * @param pageable
     * @return
     */
    public Page<Municipality> list(Pageable pageable, String... name) {
        int skipCount = (pageable.getPageNumber()) * pageable.getPageSize();
        Stream<Municipality> stream = municipalities
                .stream();
        if (name.length > 0 && name[0] != null) {
            stream = stream.filter(municipality -> municipality.getName().contains(name[0]));
        }
        List<Municipality> list = stream
                .skip(skipCount)
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, municipalities.size());
    }
}
