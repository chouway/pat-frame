package com.pat.app.poetry.synch.repo;

import com.pat.app.poetry.synch.eo.PoetSuggestEO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * PoetInfoRepository
 *
 * @author chouway
 * @date 2022.03.01
 */
@Repository
public interface PoetSuggestRepository extends ElasticsearchRepository<PoetSuggestEO,Long> {
}
