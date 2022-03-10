package com.pat.app.poetry.synch.service.es;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.index.AliasData;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * PoetEsAliasService
 *
 * @author chouway
 * @date 2022.03.10
 */
@Slf4j
@Service
public class PoetEsAliasService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    /**
     * 获取索引对应的别名
     */
    public Set<String> getAlias(String index) {

        if(!StringUtils.hasText(index)){
            throw new RuntimeException("index不为空");
        }
        final IndexOperations indexOps = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(index));
        final Map<String, Set<AliasData>> aliases = indexOps.getAliasesForIndex(index);
        final Set<AliasData> dataSet = aliases.get(index);
        Set<String> set = new HashSet<>(dataSet.size());
        dataSet.forEach(aliasData -> set.add(aliasData.getAlias()));
        log.info("addAlias-->index={},alias={}", index,dataSet);
        return set;
    }

    /**
     * 为索引添加别名
     *
     * @param index 真实索引
     * @param alias 别名
     */
    public boolean addAlias(String index, String... alias) {

        if(!StringUtils.hasText(index)){
            throw new RuntimeException("index不为空");
        }
        if(alias == null||alias.length<1){
            throw new RuntimeException("alias为空");
        }

        final IndexOperations indexOps = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(index));
        AliasActions aliasActions = new AliasActions(new AliasAction.Add(
                AliasActionParameters.builder().withIndices(index).withAliases(alias).build()
        ));
        boolean aliasResult = indexOps.alias(aliasActions);
        log.info("addAlias-->index={},alias={},aliasResult={}", index,alias,aliasResult);
        return aliasResult;
    }


    /**
     * 为索引删除
     *
     * @param index 真实索引
     * @param alias 别名
     */
    public boolean delAlias(String index, String... alias) {
        if(!StringUtils.hasText(index)){
            throw new RuntimeException("index不为空");
        }
        if(alias == null||alias.length<1){
            throw new RuntimeException("alias为空");
        }
        final IndexOperations indexOps = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(index));
        AliasActions aliasActions = new AliasActions(new AliasAction.Remove(
                AliasActionParameters.builder().withIndices(index).withAliases(alias).build()
        ));
        boolean aliasResult = indexOps.alias(aliasActions);
        log.info("delAlias-->index={},alias={},aliasResult={}", index,alias,aliasResult);
        return aliasResult;
    }


    /**
     * 为索引更换别名 旧的换为新的 不会判断旧的是否存在
     *
     * @param index    真实索引
     * @param oldAlias 要删除的别名
     * @param newAlias 要新增的别名
     */
    public boolean replaceAlias(String index, String oldAlias, String newAlias) {
        if(!StringUtils.hasText(index)){
            throw new RuntimeException("index不为空");
        }
        if(!StringUtils.hasText(oldAlias)){
            throw new RuntimeException("oldAlias为空");
        }
        if(!StringUtils.hasText(newAlias)){
            throw new RuntimeException("newAlias为空");
        }
        final IndexOperations indexOps = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(index));
        final AliasAction.Add add = new AliasAction.Add(AliasActionParameters.builder().withIndices(index).withAliases(newAlias).build());
        final AliasAction.Remove remove = new AliasAction.Remove(AliasActionParameters.builder().withIndices(index).withAliases(oldAlias).build());
        AliasActions aliasActions = new AliasActions(add, remove);
        boolean aliasResult = indexOps.alias(aliasActions);
        log.info("replaceAlias-->index={},oldAlias={},newAlias={},aliasResult={}", index,oldAlias,newAlias,aliasResult);
        return aliasResult;
    }
}
