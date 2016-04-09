/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.github.lothar.security.acl.elasticsearch.repository;

import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.io.Serializable;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.MoreLikeThisQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.support.AbstractElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.util.Assert;

import com.github.lothar.security.acl.elasticsearch.AclFilterProvider;

public class AclElasticsearchRepository<T, ID extends Serializable>
    extends AbstractElasticsearchRepository<T, ID> {

  private AclFilterProvider filterProvider;
  private Logger logger = LoggerFactory.getLogger(getClass());

  // reflection invocation by
  // com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepositoryFactoryBean.Factory.getTargetRepository(RepositoryInformation)
  public AclElasticsearchRepository(ElasticsearchEntityInformation<T, ID> metadata,
      ElasticsearchOperations elasticsearchOperations, AclFilterProvider filterProvider) {
    super(metadata, elasticsearchOperations);
    this.filterProvider = filterProvider;
  }

  public AclElasticsearchRepository(ElasticsearchOperations elasticsearchOperations,
      AclFilterProvider filterProvider) {
    super(elasticsearchOperations);
    this.filterProvider = filterProvider;
  }

  @Override
  protected String stringIdRepresentation(ID id) {
    return String.valueOf(id);
  }

  @Override
  public T findOne(ID id) {
    // TODO apply filter
    return super.findOne(id);
  }

  @Override
  public Iterable<T> findAll() {
    // redirects to #findAll(Pageable)
    return super.findAll();
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    SearchQuery query = new NativeSearchQueryBuilder() //
        .withQuery(filteredQuery(matchAllQuery(), filter())) //
        .withPageable(pageable) //
        .build();
    return elasticsearchOperations.queryForPage(query, getEntityClass());
}

  @Override
  public Iterable<T> findAll(Sort sort) {
    // TODO apply filter
    return super.findAll(sort);
  }

  @Override
  public Iterable<T> findAll(Iterable<ID> ids) {
    // TODO apply filter
    return super.findAll(ids);
  }

  @Override
  public boolean exists(ID id) {
    // TODO apply filter
    return super.exists(id);
  }

  @Override
  public Iterable<T> search(QueryBuilder query) {
    return super.search(filteredQuery(query, filter()));
  }

  @Override
  public FacetedPage<T> search(QueryBuilder query, Pageable pageable) {
    return super.search(filteredQuery(query, filter()), pageable);
  }

  @Override
  public FacetedPage<T> search(SearchQuery query) {
//    // TODO apply filter
//     SearchQuery searchQuery = new NativeSearchQueryBuilder() //
//     .withFilter(filter()) //
//     .withQuery(query) //
//     .build();
    return elasticsearchOperations.queryForPage(query, getEntityClass());
  }

  @Override
  public Page<T> searchSimilar(T entity, String[] fields, Pageable pageable) {
    // TODO apply filter
    Assert.notNull(entity, "Cannot search similar records for 'null'.");
    Assert.notNull(pageable, "'pageable' cannot be 'null'");
    MoreLikeThisQuery query = new MoreLikeThisQuery();
    query.setId(stringIdRepresentation(extractIdFromBean(entity)));
    query.setPageable(pageable);
    if (fields != null) {
      query.addFields(fields);
    }
    return elasticsearchOperations.moreLikeThis(query, getEntityClass());
  }

  @Override
  public long count() {
    SearchQuery query = new NativeSearchQueryBuilder() //
         .withQuery(filteredQuery(matchAllQuery(), filter())) //
        .build();
    return elasticsearchOperations.count(query, getEntityClass());
  }

  private FilterBuilder filter() {
    FilterBuilder filterBuilder = filterProvider.filterFor(entityClass);
    logger.debug("Using ACL filter for objects {}: {}", getEntityClass().getSimpleName(),
        filterBuilder);
    return filterBuilder;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "<" + getEntityClass().getSimpleName() + ">";
  }

}
