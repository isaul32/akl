package com.pyrenty.akl.repository.search;

import com.pyrenty.akl.domain.Text;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Text entity.
 */
public interface TextSearchRepository extends ElasticsearchRepository<Text, Long> {
}
