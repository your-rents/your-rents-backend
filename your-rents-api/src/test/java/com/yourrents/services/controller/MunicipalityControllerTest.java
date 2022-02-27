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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {MunicipalityController.class})
class MunicipalityControllerTest {
    public static final String URL = "/v1/istat/municipality";

    @Resource
    private MockMvc mockMvc;

    @MockBean
    private MunicipalityService municipalityService;

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void list() throws Exception {
        //given
        PageImpl<Municipality> page = new PageImpl<>(buildList());
        when(municipalityService.list(any(Pageable.class), any(String.class)))
                .thenReturn(page);

        //when-then
        mockMvc.perform(get(URL).param("name", "a"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].code").value("1"))
                .andExpect(jsonPath("$.content[0].name").value("Spinea"))
                .andExpect(jsonPath("$.content[0].province").value("Venezia"))
                .andExpect(jsonPath("$.content[0].region").value("Veneto"))
                .andExpect(jsonPath("$.content[1].code").value("2"))
                .andExpect(jsonPath("$.content[1].name").value("San Bonifacio"))
                .andExpect(jsonPath("$.content[1].province").value("Verona"))
                .andExpect(jsonPath("$.content[1].region").value("Veneto"));
        verify(municipalityService).list(any(Pageable.class), any(String.class));
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void findByCode() throws Exception {
        //given
        when(municipalityService.findByCode("1"))
                .thenReturn(Municipality.builder()
                        .code("1")
                        .name("Spinea")
                        .build());


        //when-then
        mockMvc.perform(get(URL+"/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        verify(municipalityService).findByCode("1");
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void findByCode_404() throws Exception {
        //given
        when(municipalityService.findByCode("xxx"))
                .thenThrow(new IllegalArgumentException("Invalid code"));


        //when-then
        mockMvc.perform(get(URL+"/xxx"))
                .andExpect(status().is4xxClientError());
        verify(municipalityService).findByCode("xxx");
    }

    private static List<Municipality> buildList() {
        return List.of(Municipality.builder()
                        .code("1")
                        .name("Spinea")
                        .province("Venezia")
                        .region("Veneto")
                        .build(),
                Municipality.builder()
                        .code("2")
                        .name("San Bonifacio")
                        .province("Verona")
                        .region("Veneto")
                        .build()
        );
    }



}
