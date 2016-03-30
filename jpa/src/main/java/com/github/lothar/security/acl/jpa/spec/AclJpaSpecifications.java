package com.github.lothar.security.acl.jpa.spec;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class AclJpaSpecifications {

  private AclJpaSpecifications() {}

  public static <T, ID extends Serializable> Specification<T> idsIn(Iterable<ID> ids) {
    return new IdsInSpecification<>(collection(ids));
  }

  public static <T> Specification<T> allowAll() {
    return AllowAllSpecification.get();
  }

  public static class IdsInSpecification<T, ID extends Serializable> implements Specification<T> {

    private Collection<ID> ids;
    private String idField = "id";

    public IdsInSpecification(Collection<ID> ids) {
      this.ids = ids;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
      return root.get(idField).in(ids);
    }
  }

  public static <T, ID extends Serializable> Specification<T> idIs(ID id) {
    return new Specification<T>() {
      @Override
      public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("id"), id);
      }
    };
  }

  private static <T> Collection<T> collection(Iterable<T> iterable) {
    if (iterable instanceof Collection) {
      return (Collection<T>) iterable;
    } else {
      return stream(iterable.spliterator(), false).collect(toList());
    }
  }
}
