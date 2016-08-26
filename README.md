# strategy-spring-security-acl [![Build Status](https://travis-ci.org/lordlothar99/strategy-spring-security-acl.svg?branch=master)](https://travis-ci.org/lordlothar99/strategy-spring-security-acl) [![Coverage Status](https://coveralls.io/repos/github/lordlothar99/strategy-spring-security-acl/badge.svg?branch=master)](https://coveralls.io/github/lordlothar99/strategy-spring-security-acl?branch=master) [![GitHub tag](https://img.shields.io/github/tag/lordlothar99/strategy-spring-security-acl.svg?maxAge=3600&label=latest)](https://github.com/lordlothar99/strategy-spring-security-acl)

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

For read-like methods in JPA and ElasticSearch repositories, Strategy-spring-security-acl provides features able to inject ACL restriction criterias directly inside the repositories.

### Auto-configured

Strategy-security-acl is an extension of [Spring Security][] which will auto-configure thanks to [Spring Boot][] awesome magic

### Extensibility !!

Current bundled features are:
* Grant : implementation of PermissionEvaluator which delegates to adequate `GrantEvaluator` beans
* JPA : injects `JpaSpecification` in your repositories so they would retrieve only authorized objects from database ; thanks to [Spring Data JPA][]
* ElasticSearch : injects `FilterBuilder` in your repositories so they would retrieve only authorized objects from ElasticSearch ; thanks to [Spring Data ElasticSearch][]
You need more than existing features ? Create your own !! and share it ;).

### Strategy oriented

1. ACL strategies are Spring beans
2. You can create reusable strategies, and apply them to several domain objects, or associate 1 specific strategy for each domain object
3. Strategies are composite objects wrapping ACL filter implementations dedicated to each supported technology, which are called "feature" :
- GrantEvaluator, as a typed delegation of [Spring Security][]'s `PermissionEvaluator`
- Specification, implementation of [Spring Data JPA][]'s API
- FilterBuilder, implementation of [Spring Data ElasticSearch][]'s API

## <a name="Installation">Installation</a>

Add Github as a maven repository (yes, you can) :

	<repositories>
		<repository>
			<id>strategy-spring-security-acl-github-repo</id>
			<url>https://raw.github.com/lordlothar99/mvn-repo/master/</url>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
		</repository>
	</repositories>

### [Spring Boot][]

Configured beans are automatically loaded by [Spring Boot][]'s magic, as soon jars are in the path.
Add required dependencies to your pom (latest version is highest tag created on Github : ![GitHub tag](https://img.shields.io/github/tag/lordlothar99/strategy-spring-security-acl.svg?maxAge=3600&label=latest)) :

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

### Integration

Then you need very few Spring config:
* For Jpa feature :
```
	import com.github.lothar.security.acl.jpa.repository.AclJpaRepositoryFactoryBean;
...
	@EnableJpaRepositories(
		value = "<your jpa repositories package here>",
		repositoryFactoryBeanClass = AclJpaRepositoryFactoryBean.class
	)
```

* For ElasticSearch feature :
```
	import com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepositoryFactoryBean;
...
	@EnableElasticsearchRepositories(
		value = "<your elastic search repositories package here>",
		repositoryFactoryBeanClass = AclElasticsearchRepositoryFactoryBean.class
	)
```

* For GrantEvaluator feature (if you want to enable Pre/Post annotations) :
```
	@EnableGlobalMethodSecurity(prePostEnabled = true)
```

Now, let's say you have a `Customer` domain entity in your project, and you need to restrict readable customers, so that only those whose last name is "Smith" can be retrieved.
1. Define your strategy : let's create an `CustomerAclStrategy`, which will contain our different ACL features implementations (1 implementation by feature). `SimpleAclStrategy` implementation is recommended as a start. In your favorite `Configuration` bean, let's define :
```
  @Bean
  public SimpleAclStrategy customerStrategy() {
    return new SimpleAclStrategy();
  }
```
2. If you are using GrantEvaluator feature, create a `CustomerGrantEvaluator` bean, and install it inside the `CustomerStrategy`. Let's add a new bean into `Configuration` :
```
  @Bean
  public GrantEvaluator smithFamilyGrantEvaluator(CustomerRepository customerRepository,
      GrantEvaluatorFeature grantEvaluatorFeature) {
    GrantEvaluator smithFamilyGrantEvaluator = new CustomerGrantEvaluator(customerRepository);
    customerStrategy.install(grantEvaluatorFeature, smithFamilyGrantEvaluator);
    return smithFamilyGrantEvaluator;
  }
```
And create a dedicated `CustomerGrantEvaluator` class, it's close to Spring's `PermissionEvaluator` API :
```
import static com.github.lothar.security.acl.jpa.spec.AclJpaSpecifications.idEqualTo;
import org.springframework.security.core.Authentication;
import com.github.lothar.security.acl.sample.domain.Customer;
import com.github.lothar.security.acl.sample.jpa.CustomerRepository;

public class CustomerGrantEvaluator extends AbstractGrantEvaluator<Customer, String> {

  private CustomerRepository repository;

  public CustomerGrantEvaluator(CustomerRepository repository) {
    super();
    this.repository = repository;
  }

  @Override
  public boolean isGranted(Permission permission, Authentication authentication,
      Customer domainObject) {
    return "Smith".equals(domainObject.getLastName());
  }

  @Override
  public boolean isGranted(Permission permission, Authentication authentication, String targetId,
      Class<? extends Customer> targetType) {
    // thanks to JpaSpecFeature, repository will count only authorized customers !
    return repository.count(idEqualTo(targetId)) == 1;
  }
}
```
3. Add Pre/Post annotations on adequate methods :
```
  @PreAuthorize("hasPermission(#customer, 'SAVE')")
...
  @PreAuthorize("hasPermission(#customerId, 'com.github.lothar.security.acl.sample.domain.Customer', 'READ')")
```
4. If you are using Jpa feature, create a `Specification` bean, and install it inside the `CustomerStrategy`. Let's add this new bean into `Configuration` :
```
  @Bean
  public Specification<Customer> smithFamilySpec(JpaSpecFeature<Customer> jpaSpecFeature) {
    Specification<Customer> smithFamilySpec = new Specification<Customer>() {
      @Override
      public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query,
          CriteriaBuilder cb) {
        return cb.equal(root.get("lastName"), "Smith");
      }
    };
    customerStrategy.install(jpaSpecFeature, smithFamilySpec);
    return smithFamilySpec;
  }
```
5. If you are using ElasticSearch feature, create a `FilterBuilder` bean, and install it inside the `CustomerStrategy`. Let's add this new bean into `Configuration` :
```
  @Bean
  public TermFilterBuilder smithFamilyFilter(ElasticSearchFeature elasticSearchFeature) {
    TermFilterBuilder smithFamilyFilter = termFilter("lastName", "Smith");
    customerStrategy.install(elasticSearchFeature, smithFamilyFilter);
    return smithFamilyFilter;
  }
```

### Exclude a Spring Data JPA query-method to be filtered with ACL

You just have to put `@NoAcl` annotation on this method in order to avoid the ACL JPA Specification to be injected.

### Override strategies

It may be useful (for tests purpose for example) to disable all domain objects strategies, and use only one (which may be `allowAllStrategy`, so no restriction would be applied). Just add following property in your project's yml/properties file:

```
strategy-security-acl:
    override-strategy: allowAllStrategy
```

### Struggling with integration ?

Have a look at our samples !!

[Spring Boot]: http://projects.spring.io/spring-boot/
[Spring Data JPA]: http://projects.spring.io/spring-data-jpa/
[Spring Data ElasticSearch]: http://projects.spring.io/spring-data-elasticsearch/
[Spring Security]: http://projects.spring.io/spring-security/
[Spring Security ACL]: https://github.com/spring-projects/spring-security/tree/master/acl


### Using JDK 7 ?

Projects using JDK 7 can use artifacts version with suffix "jdk7"
