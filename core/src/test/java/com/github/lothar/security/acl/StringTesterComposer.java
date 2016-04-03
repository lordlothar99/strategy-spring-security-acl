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
package com.github.lothar.security.acl;

import java.util.function.Function;

import com.github.lothar.security.acl.compound.AclComposer;

public class StringTesterComposer implements AclComposer<Function<String, Boolean>> {

  public Function<String, Boolean> and(Function<String, Boolean> lhs,
      Function<String, Boolean> rhs) {
    return (s) -> lhs.apply(s) && rhs.apply(s);
  }

  @Override
  public Function<String, Boolean> or(Function<String, Boolean> lhs,
      Function<String, Boolean> rhs) {
    return (s) -> lhs.apply(s) || rhs.apply(s);
  }
}
