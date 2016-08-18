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

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.elasticsearch.index.query.QueryBuilders.andQuery;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.idsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
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
    SearchQuery query = new NativeSearchQueryBuilder() //
        .withQuery(and(idsQuery().ids(stringIdRepresentation(id)), aclFilter())) //
        .build();
    List<T> list = elasticsearchOperations.queryForList(query, getEntityClass());
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    SearchQuery query = new NativeSearchQueryBuilder() //
        .withQuery(and(matchAllQuery(), aclFilter())) //
        .withPageable(pageable) //
        .build();
    return elasticsearchOperations.queryForPage(query, getEntityClass());
  }

  @Override
  public Iterable<T> findAll(Sort sort) {
    int itemCount = (int) this.count();
    if (itemCount == 0) {
      return new PageImpl<T>(Collections.<T>emptyList());
    }
    SearchQuery query = new NativeSearchQueryBuilder() //
        .withQuery(and(matchAllQuery(), aclFilter())) //
        .withPageable(new PageRequest(0, itemCount, sort)) //
        .build();
    return elasticsearchOperations.queryForPage(query, getEntityClass());
  }

  @Override
  public Iterable<T> findAll(Iterable<ID> ids) {
    Assert.notNull(ids, "ids can't be null.");

    SearchQuery query = new NativeSearchQueryBuilder() //
        .withQuery(and(idsQuery().ids(idsArray(ids)), aclFilter())) //
        .build();
    return elasticsearchOperations.queryForList(query, getEntityClass());
  }

  @Override
  public Iterable<T> search(QueryBuilder query) {
    return super.search(and(query, aclFilter()));
  }

  @Override
  public Page<T> search(QueryBuilder query, Pageable pageable) {
    return super.search(and(query, aclFilter()), pageable);
  }

  @Override
  public Page<T> search(SearchQuery query) {
    Assert.isInstanceOf(NativeSearchQuery.class, query, "NativeSearchQuery only are supported :(");
    NativeSearchQuery searchQuery = new NativeSearchQuery(and(query.getQuery(), aclFilter()));
    BeanUtils.copyProperties(query, searchQuery, "query");
    return elasticsearchOperations.queryForPage(searchQuery, getEntityClass());
  }

  @Override
  public Page<T> searchSimilar(T entity, String[] fields, Pageable pageable) {
    return super.searchSimilar(entity, fields, pageable);
  }

  @Override
  public long count() {
    SearchQuery query = new NativeSearchQueryBuilder() //
        .withQuery(and(matchAllQuery(), aclFilter())) //
        .build();
    return elasticsearchOperations.count(query, getEntityClass());
  }

  private QueryBuilder aclFilter() {
    QueryBuilder filterBuilder = filterProvider.filterFor(entityClass);
    logger.debug("Using ACL filter for objects {}: {}", getEntityClass().getSimpleName(),
        filterBuilder);
    return filterBuilder;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "<" + getEntityClass().getSimpleName() + ">";
  }

  private String[] idsArray(Iterable<ID> ids) {
    List<String> idsString = stringIdsRepresentation(ids);
    return idsString.toArray(new String[idsString.size()]);
  }

  private List<String> stringIdsRepresentation(Iterable<ID> ids) {
    Assert.notNull(ids, "ids can't be null.");
    return stream(ids.spliterator(), false) //
        .map(id -> stringIdRepresentation(id)) //
        .collect(toList());
  }

  @SuppressWarnings("deprecation")
  private QueryBuilder and(QueryBuilder... queryBuilders) {
      return boolQuery().must(andQuery(queryBuilders));
  }
}
