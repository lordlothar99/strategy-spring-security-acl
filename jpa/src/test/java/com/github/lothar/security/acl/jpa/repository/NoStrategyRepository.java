package com.github.lothar.security.acl.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.lothar.security.acl.jpa.domain.NoStrategyObject;

@Repository
public interface NoStrategyRepository extends JpaRepository<NoStrategyObject, Long> {

}
