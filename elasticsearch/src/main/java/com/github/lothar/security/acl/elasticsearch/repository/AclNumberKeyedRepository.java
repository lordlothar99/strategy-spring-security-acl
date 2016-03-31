package com.github.lothar.security.acl.elasticsearch.repository;

import java.util.Collections;
import java.util.function.Supplier;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.MoreLikeThisQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.NumberKeyedRepository;
import org.springframework.util.Assert;

public class AclNumberKeyedRepository<T, ID extends Number> extends NumberKeyedRepository<T, ID> {

  private Supplier<FilterBuilder> filterSupplier;
  private Logger logger = LoggerFactory.getLogger(getClass());

  // reflection invocation by
  // com.trackaflat.config.acl.AclElasticsearchRepositoryFactoryBean.AclElasticsearchRepositoryFactory.getTargetRepository(RepositoryInformation)
  public AclNumberKeyedRepository(ElasticsearchEntityInformation<T, ID> metadata,
      ElasticsearchOperations elasticsearchOperations, Supplier<FilterBuilder> filterSupplier) {
    super(metadata, elasticsearchOperations);
    this.filterSupplier = filterSupplier;
  }

  public AclNumberKeyedRepository(ElasticsearchOperations elasticsearchOperations, Supplier<FilterBuilder> filterSupplier) {
    super(elasticsearchOperations);
    this.filterSupplier = filterSupplier;
  }

  @Override
  public T findOne(ID id) {
    // TODO apply filter
    return super.findOne(id);
  }

  @Override
  public Iterable<T> findAll() {
    // TODO apply filter
    return super.findAll();
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    // TODO apply filter
    return super.findAll(pageable);
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
      SearchQuery searchQuery = new NativeSearchQueryBuilder() //
          .withFilter(filter()) //
          .withQuery(query) //
          .build();
      int count = (int) elasticsearchOperations.count(searchQuery, getEntityClass());
      if (count == 0) {
          return new PageImpl<T>(Collections.<T>emptyList());
      }
      searchQuery.setPageable(new PageRequest(0, count));
      return elasticsearchOperations.queryForPage(searchQuery, getEntityClass());
  }

  @Override
  public FacetedPage<T> search(QueryBuilder query, Pageable pageable) {
      SearchQuery searchQuery = new NativeSearchQueryBuilder() //
          .withFilter(filter()) //
          .withQuery(query) //
          .withPageable(pageable) //
          .build();
      return elasticsearchOperations.queryForPage(searchQuery, getEntityClass());
  }

  @Override
  public FacetedPage<T> search(SearchQuery query) {
    // TODO apply filter
//      SearchQuery searchQuery = new NativeSearchQueryBuilder() //
//        .withFilter(filter()) //
//        .withQuery(query) //
//        .build();
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
    // TODO apply filter
    return super.count();
  }

  private FilterBuilder filter() {
    FilterBuilder filterBuilder = filterSupplier.get();
    logger.debug("Using filter builder {} for objects {}", filterBuilder,
        getEntityClass().getSimpleName());
    return filterBuilder;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "<" + getEntityClass().getSimpleName() + ">";
  }

}
