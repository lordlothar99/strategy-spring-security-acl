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
package com.github.lothar.security.acl.elasticsearch;

import java.io.IOException;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FilterBuilder;

import com.github.lothar.security.acl.bean.NamedBean;

public class FilterBuilderBean extends NamedBean implements FilterBuilder {

  private FilterBuilder filterBuilder;

  public FilterBuilderBean(FilterBuilder filterBuilder) {
    super();
    this.filterBuilder = filterBuilder;
  }

  @Override
  public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
    return filterBuilder.toXContent(builder, params);
  }

  @Override
  public BytesReference buildAsBytes() throws ElasticsearchException {
    return filterBuilder.buildAsBytes();
  }

  @Override
  public BytesReference buildAsBytes(XContentType contentType) throws ElasticsearchException {
    return filterBuilder.buildAsBytes(contentType);
  }

}
