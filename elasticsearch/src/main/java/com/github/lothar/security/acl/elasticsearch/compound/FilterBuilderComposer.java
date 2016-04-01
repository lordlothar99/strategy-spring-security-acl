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

import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.orFilter;

import org.elasticsearch.index.query.FilterBuilder;

import com.github.lothar.security.acl.compound.AclComposer;

public class FilterBuilderComposer implements AclComposer<FilterBuilder> {

  @Override
  public FilterBuilder and(FilterBuilder lhs, FilterBuilder rhs) {
    return andFilter(lhs, rhs);
  }

  @Override
  public FilterBuilder or(FilterBuilder lhs, FilterBuilder rhs) {
    return orFilter(lhs, rhs);
  }

}
