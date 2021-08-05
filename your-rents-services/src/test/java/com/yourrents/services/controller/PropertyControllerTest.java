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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {PropertyController.class})
class PropertyControllerTest {

    @Resource
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        when(propertyService.list(any(Pageable.class)))
                .thenReturn(new PageImpl<>(buildList()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void list() throws Exception {
        mockMvc.perform(get("/property"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("flat-A"))
                .andExpect(jsonPath("$.content[0].description").value("flat at Jesolo"))
                .andExpect(jsonPath("$.content[1].name").value("flat-B"))
                .andExpect(jsonPath("$.content[1].description").value("flat at Livorno"));
        verify(propertyService, times(1)).list(any(Pageable.class));
    }

    @Test
    void listNotAuthorized() throws Exception {
        mockMvc.perform(get("/property"))
                .andExpect(status().isUnauthorized());
    }

    private List<Property> buildList() {
        return List.of(Property.builder()
                        .name("flat-A")
                        .description("flat at Jesolo")
                        .build(),
                Property.builder()
                        .name("flat-B")
                        .description("flat at Livorno")
                        .build());
    }
}
