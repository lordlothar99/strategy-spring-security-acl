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

  public static <T> Collection<T> collection(Iterable<T> iterable) {
    if (iterable instanceof Collection) {
      return (Collection<T>) iterable;
    } else {
      return stream(iterable.spliterator(), false).collect(toList());
    }
  }
}
