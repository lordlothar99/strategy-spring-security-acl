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
package com.github.lothar.security.acl.config.web;

import javax.servlet.Servlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.lothar.security.acl.activation.AclSecurityActivator;
import com.github.lothar.security.acl.activation.web.AclActivatorFilter;

@Configuration
@ConditionalOnClass({Servlet.class})
public class AclWebConfiguration {

  @Bean
  public AclActivatorFilter aclActivatorFilter(AclSecurityActivator aclSecurityActivator) {
    return new AclActivatorFilter(aclSecurityActivator);
  }
}
