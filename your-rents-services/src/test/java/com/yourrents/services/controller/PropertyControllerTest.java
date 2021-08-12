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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourrents.core.YRNotFoundException;
import com.yourrents.core.dto.Property;
import com.yourrents.core.service.PropertyService;
import com.yourrents.services.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {PropertyController.class, SecurityConfig.class})
class PropertyControllerTest {

    private static final UUID uUID_1 = UUID.randomUUID();
    private static final UUID uUID_2 = UUID.randomUUID();

    public static final String URL = "/v1/property";

    @Resource
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void list() throws Exception {
        //given
        when(propertyService.list(any(Pageable.class)))
                .thenReturn(new PageImpl<>(buildList()));

        //when-then
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].external_id").value(uUID_1.toString()))
                .andExpect(jsonPath("$.content[0].name").value("flat-A"))
                .andExpect(jsonPath("$.content[0].description").value("flat at Jesolo"))
                .andExpect(jsonPath("$.content[1].external_id").value(uUID_2.toString()))
                .andExpect(jsonPath("$.content[1].name").value("flat-B"))
                .andExpect(jsonPath("$.content[1].description").value("flat at Livorno"));
        verify(propertyService, times(1)).list(any(Pageable.class));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getProperty() throws Exception {

        //given
        when(propertyService.get(uUID_1)).thenReturn(buildList().get(0));
        //when-then
        this.mockMvc.perform(get(URL + "/" + uUID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(propertyService, times(1)).get(uUID_1);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getProperty_404() throws Exception {

        //given
        when(propertyService.get(uUID_1)).thenThrow(YRNotFoundException.class);
        //when-then
        this.mockMvc.perform(get(URL + "/" + uUID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(propertyService, times(1)).get(uUID_1);
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void add() throws Exception {
        //given
        Property property = Property.builder()
                .name("new-name")
                .description("new-description")
                .build();
        when(propertyService.add(property)).thenReturn(uUID_1);

        //when-then
        this.mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(property)))
                .andExpect(status().isCreated());
        verify(propertyService, times(1)).add(property);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void update() throws Exception {
        //given
        Property property = Property.builder()
                .external_id(uUID_1)
                .build();
        when(propertyService.update(property)).thenReturn(1);

        //when-then
        this.mockMvc.perform(put(URL + "/" + uUID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(property)))
                .andExpect(status().isOk());
        verify(propertyService, times(1)).update(property);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void updateNotFound() throws Exception {
        //given
        Property property = Property.builder()
                .external_id(uUID_1)
                .build();
        when(propertyService.update(property)).thenReturn(0);

        //when-then
        this.mockMvc.perform(put(URL + "/" + uUID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(property)))
                .andExpect(status().isNotFound());
        verify(propertyService, times(1)).update(property);
    }


    private List<Property> buildList() {
        return List.of(Property.builder()
                        .external_id(uUID_1)
                        .name("flat-A")
                        .description("flat at Jesolo")
                        .build(),
                Property.builder()
                        .external_id(uUID_2)
                        .name("flat-B")
                        .description("flat at Livorno")
                        .build());
    }

}
