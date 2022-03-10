package com.pat.app.poetry.synch.repo;

import com.pat.app.poetry.synch.eo.PoetInfoEO;
import com.pat.app.poetry.synch.eo.SuggestEO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * PoetInfoRepository
 *
 * @author chouway
 * @date 2022.03.01
 */
@Repository
public interface SuggestRepository extends ElasticsearchRepository<SuggestEO,Long> {
}
