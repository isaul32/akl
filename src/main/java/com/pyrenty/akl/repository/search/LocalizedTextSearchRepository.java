package com.pyrenty.akl.repository.search;

import com.pyrenty.akl.domain.LocalizedText;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LocalizedText entity.
 */
public interface LocalizedTextSearchRepository extends ElasticsearchRepository<LocalizedText, Long> {
}
