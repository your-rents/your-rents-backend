package com.yourrents.core.util;

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

import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

/**
 * Exception transformer for JOOQ exceptions.
 * 
 * @see <a href=
 *      "https://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-configuration/">https://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-configuration/</a>
 */
public class JOOQToSpringExceptionTransformer extends DefaultExecuteListener {

    @Override
    public void exception(ExecuteContext ctx) {
        SQLDialect dialect = ctx.configuration().dialect();
        SQLExceptionTranslator translator = (dialect != null) ?
            new SQLErrorCodeSQLExceptionTranslator(dialect.name()):
            new SQLStateSQLExceptionTranslator();
        ctx.exception(translator.translate("jOOQ", ctx.sql(), ctx.sqlException()));
    }
}
