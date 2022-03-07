package com.yourrents.services.controller;

/*-
 * #%L
 * Your Rents REST API
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
import com.yourrents.core.service.MunicipalityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/istat/municipality")
@RequiredArgsConstructor
@Slf4j
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    @RequestMapping(
            method = {RequestMethod.GET},
            produces = "application/json"
    )
    public ResponseEntity<Page<Municipality>> list(Pageable pageable, @RequestParam(name = "name", required = false) String name) {
        Page<Municipality> list = municipalityService.list(pageable, name);
        return ResponseEntity.ok(list);
    }

    @RequestMapping(
            method = {RequestMethod.GET},
            produces = "application/json",
            value = "/{code}"
    )
    public ResponseEntity<Municipality> get(@PathVariable String code) {
        Municipality municipality;
        try {
            municipality = municipalityService.findByCode(code);
        } catch (IllegalArgumentException e) {
            log.warn(e.toString(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(municipality, HttpStatus.OK);
    }

}

