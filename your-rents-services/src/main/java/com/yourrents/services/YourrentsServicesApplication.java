package com.yourrents.services;

/*-
 * #%L
 * Your Rents Services
 * %%
 * Copyright (C) 2019 Your Rents Team
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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(scanBasePackages = { "com.yourrents.services", "com.yourrents.core.service" },
 					   exclude = {R2dbcAutoConfiguration.class })
public class YourrentsServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(YourrentsServicesApplication.class, args);
	}

}
