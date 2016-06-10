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
package com.github.lothar.security.acl.jpa.spec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.jdk8.BiFunction;

public class AclJpaSpecifications {

  private AclJpaSpecifications() {}

  public static <T, ID extends Serializable> Specification<T> idsIn(final Iterable<ID> ids) {
    BiFunction<Root<T>, CriteriaBuilder, Predicate> function = new BiFunction<Root<T>, CriteriaBuilder, Predicate>() {
      @Override
      public Predicate apply(Root<T> root, CriteriaBuilder cb) {
        return root.get("ids").in(collection(ids));
      }
    };
    return new BiFunctionSpecification<>(function);
  }

  public static <T, ID extends Serializable> Specification<T> idEqualTo(final ID id) {
    BiFunction<Root<T>, CriteriaBuilder, Predicate> function = new BiFunction<Root<T>, CriteriaBuilder, Predicate>() {
      @Override
      public Predicate apply(Root<T> root, CriteriaBuilder cb) {
        return cb.equal(root.get("id"), id);
      }
    };
    return new BiFunctionSpecification<>(function);
  }

  private static <T> Collection<T> collection(Iterable<T> iterable) {
    if (iterable instanceof Collection) {
      return (Collection<T>) iterable;
    } else {
      List<T> list = new ArrayList<>();
      for (T element : iterable) {
        list.add(element);
      }
      return list;
    }
  }
}
