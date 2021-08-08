package com.yourrents.services.controller;

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
import com.yourrents.core.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/property")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    @RequestMapping(
            method = {RequestMethod.GET}
    )
    public ResponseEntity<Page<Property>> list(Pageable pageable) {
        return ResponseEntity.ok(propertyService.list(pageable));
    }

    @RequestMapping(
            method = {RequestMethod.PUT},
            produces = "application/json"
    )
    public ResponseEntity<Integer> add(@RequestBody Property property) {
        int id = propertyService.add(property);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

}
