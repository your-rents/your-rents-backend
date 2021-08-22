package com.yourrents.core.dto;

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

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The generic interface for DTO converters.
 * 
 * @see <a href="https://stackoverflow.com/a/47133810/1398995">https://stackoverflow.com/a/47133810/1398995</a>
 */
public interface Converter<I, O> extends Function<I, O> {
    default O convert(final I input) {
        O output = null;
        if (input != null) {
            output = this.apply(input);
        }
        return output;
    }

    default List<O> convert(final List<I> input) {
        List<O> output = new ArrayList<O>();
        if (input != null) {
            output = input.stream().map(this::apply).collect(toList());
        }
        return output;
    }
}
