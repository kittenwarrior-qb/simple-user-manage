package com.example.management_demo.repository.cache;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheLimitRepository extends CrudRepository<CacheLimit, String> {
}
