package com.yourrents.core.config;

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
import com.yourrents.core.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IstatDataFactory {

    private static final String PATH = "istat/Elenco-comuni-italiani.csv";
    private final Mapper mapper;

    @Bean()
    @Qualifier("municipalities")
    public List<Municipality> list() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(PATH);
        Reader reader = new InputStreamReader(is);
        List<Municipality> records = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                Municipality municipality = mapper.stringToMunicipality(line);
                records.add(municipality);
            }
        }
        records.remove(0);
        records.sort((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
        log.info("List of municipalities loaded from the file: {}", PATH);
        return records;
    }

}
