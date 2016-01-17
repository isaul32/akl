package com.pyrenty.akl.repository.search;

import com.pyrenty.akl.domain.GameServer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GameServer entity.
 */
public interface GameServerSearchRepository extends ElasticsearchRepository<GameServer, Long> {
}
