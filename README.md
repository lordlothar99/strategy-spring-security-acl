# strategy-spring-security-acl [![Build Status](https://travis-ci.org/lordlothar99/strategy-spring-security-acl.svg?branch=master)](https://travis-ci.org/lordlothar99/strategy-spring-security-acl)

Extensible strategy-based Spring Security ACL implementation ; available modules are : PermissionEvaluator, JPA Specification and ElasticSearch Filter

How to install : have a look here : [Installation](#Installation)

## Why ??

### Default [Spring Security ACL][] implementation is database-oriented

[Spring Security ACL][] default implementation uses a persistent model in order to evaluate every permission for any object and SID. Aside performance issues due to a huge ternary table in this model (1 row is 3-tuple { sid ; object weak reference ; permission }), developpers would rather use a more programmatic and object-oriented implementation, without any duplication.

### Access Control List is not only a PermissionEvaluator concern

Dealing with Access Control List is not a PermissionEvaluator-only concern. Let's take a CRUD-like standard application :
- Create, Update and Delete methods should be annotated with `@PreAuthorize("hasPermission(<object>, <permission>)")`, and so would reject unauthorized executions, that's great !
- But Read methods would use `@PostAuthorize("hasPermission(<object>, <permission>)")` instead, and therefore try to filter objects after they've been retrieved from underlying layers (often database) ; this leads to two frequent issues:
- performance : some useless objects are retrieved but evicted
- pagination : let's say user ask for a 10 items page ; repository finds those, but 4 of them are evicted... either developper implemented some retry-til-page-is-complete behavior (ugly pattern, and downgraded performance expected...), either client will get only 6 objects, and therefore ask himself where the 4 others went !! too bad...

## Ok, what's the proposal then ??

Strategy-spring-security-acl relies on several principles :

### Easy to plug extension of [Spring Security][]

Propose an alternative to [Spring Security Acl][]

### Easy to configure

Thanks to [Spring Boot][] auto-configure awesome feature

### Extensibility !!

You need more than bundled modules ? Create your own !! and share it ;).
Current bundled modules are:
* Grant : implementation of PermissionEvaluator which delegates to adequate `GrantEvaluator` beans
* JPA : injects `JpaSpecification` in your repositories so they would retrieve only authorized objects from database ; thanks to [Spring Data JPA][]
* ElasticSearch : injects `FilterBuilder` in your repositories so they would retrieve only authorized objects from ElasticSearch ; thanks to [Spring Data ElasticSearch][]

### Strategy oriented

1. Create ACL strategies as Spring beans. Strategies are composite objects wrapping ACL filter components  
2. Install for each business object, and you

## <a name="Installation">Installation</a>

Add Github as a maven repository (yes, you can) :

	<repositories>
		<repository>
			<id>strategy-spring-security-acl-github-repo</id>
			<url>https://raw.github.com/lordlothar99/strategy-spring-security-acl/mvn-repo/</url>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
	</repositories>

### I know Spring-boot / I like Spring-boot / I want Spring-boot / I'm a Spring-boot addict / I dream of Spring-boot every single night / ...

That's great !!! So, you just have to add necessary dependencies to your pom.xml :

<< coming soon >>

### Spring-boot... what's that ?? / I don't know Spring-boot

Have a look [here][Spring Boot] (you really should). Ok, no time for that ?
Add required dependencies :

	<dependency>
		<groupId>com.github.lothar.security.acl</groupId>
		<artifactId>strategy-spring-security-acl-elasticsearch</artifactId>
		<version>LATEST</version>
	</dependency>

	<dependency>
		<groupId>com.github.lothar.security.acl</groupId>
		<artifactId>strategy-spring-security-acl-grant</artifactId>
		<version>LATEST</version>
	</dependency>

	<dependency>
		<groupId>com.github.lothar.security.acl</groupId>
		<artifactId>strategy-spring-security-acl-jpa</artifactId>
		<version>LATEST</version>
	</dependency>

Then you need to tell Spring to load some beans definitions:
* With Jpa module :

	@Import( com.github.lothar.security.acl.jpa.config.JpaSpecAclConfiguration.class )
	@EnableJpaRepositories(
		value = "<your jpa repositories package here>",
		repositoryFactoryBeanClass = com.github.lothar.security.acl.jpa.repository.AclJpaRepositoryFactoryBean.class
	)

* With ElasticSearch module :

	@Import( com.github.lothar.security.acl.elasticsearch.config.ElasticSearchAclConfiguration.class )
	@EnableElasticsearchRepositories(
		value = "<your elastic search repositories package here>",
		repositoryFactoryBeanClass = com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepositoryFactoryBean.class
	)

### Struggling with integration ?

Have a look at our samples !!

[Spring Boot]: http://projects.spring.io/spring-boot/
[Spring Data JPA]: http://projects.spring.io/spring-data-jpa/
[Spring Data ElasticSearch]: http://projects.spring.io/spring-data-elasticsearch/
[Spring Security]: http://projects.spring.io/spring-security/
[Spring Security ACL]: https://github.com/spring-projects/spring-security/tree/master/acl