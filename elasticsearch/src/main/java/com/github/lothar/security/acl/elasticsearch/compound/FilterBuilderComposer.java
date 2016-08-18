/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
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
 *******************************************************************************/
package com.github.lothar.security.acl.elasticsearch.compound;

import static org.elasticsearch.index.query.QueryBuilders.andQuery;
import static org.elasticsearch.index.query.QueryBuilders.orQuery;

import org.elasticsearch.index.query.QueryBuilder;

import com.github.lothar.security.acl.compound.AclComposer;

@SuppressWarnings("deprecation")
public class FilterBuilderComposer implements AclComposer<QueryBuilder> {

  @Override
  public QueryBuilder and(QueryBuilder lhs, QueryBuilder rhs) {
    return andQuery(lhs, rhs);
  }

  @Override
  public QueryBuilder or(QueryBuilder lhs, QueryBuilder rhs) {
    return orQuery(lhs, rhs);
  }

}
