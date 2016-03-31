package com.github.lothar.security.acl.jpa.spec;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.data.jpa.domain.Specification;

public class AclJpaSpecifications {

  private AclJpaSpecifications() {}

  public static <T, ID extends Serializable> Specification<T> idsIn(Iterable<ID> ids) {
    return new BiFunctionSpecification<>((root, cb) -> root.get("ids").in(collection(ids)));
  }

  public static <T, ID extends Serializable> Specification<T> idEqualTo(ID id) {
    return new BiFunctionSpecification<>((root, cb) -> cb.equal(root.get("id"), id));
  }

  private static <T> Collection<T> collection(Iterable<T> iterable) {
    if (iterable instanceof Collection) {
      return (Collection<T>) iterable;
    } else {
      return stream(iterable.spliterator(), false).collect(toList());
    }
  }
}
