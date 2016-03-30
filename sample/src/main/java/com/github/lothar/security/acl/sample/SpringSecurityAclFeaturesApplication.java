package com.github.lothar.security.acl.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.test.ImportAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.github.lothar.security.acl.config.AclConfiguration;

@SpringBootApplication
@ComponentScan
@ImportAutoConfiguration({AclConfiguration.class})
public class SpringSecurityAclFeaturesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAclFeaturesApplication.class, args);
	}
}
