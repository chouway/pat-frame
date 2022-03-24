package com.pat.app.poetry.synch.service.baike;

import com.alibaba.fastjson.JSON;
import com.pat.app.poetry.synch.PoetSynchTest;
import com.pat.starter.cache.constant.CacheConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PatBaikeServiceTest
 *
 * @author chouway
 * @date 2022.03.03
 */
public class PoetBaikeServiceTest extends PoetSynchTest {

    @Autowired
    private PoetBaikeService poetBaikeService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void synchPoetBaike(){
        poetBaikeService.synchPoetBaike();
    }

    @Test
    public void synchBaiduBaikeProps() {
        Long infoId = 9L;
        poetBaikeService.synchBaiduBaikeProps(infoId);
    }



    @Test
    public void getCahce(){
        String cacheKey = CacheConstant.DAY;
        Long infoId = 9L;
        Cache dayCache = cacheManager.getCache(cacheKey);
        String cacheVal = "bk_" + infoId;
        Cache.ValueWrapper valueWrapper = dayCache.get(cacheVal);
        log.info("getCahce-->cacheKey={},cacheVal={}", cacheKey,cacheVal);
        if(valueWrapper == null){
            log.info("getCahce-->valueWrapper is null");
        }else{
            log.info("getCahce-->valueWrapper={}", JSON.toJSONString(valueWrapper.get()));
            dayCache.evictIfPresent(cacheVal);
            log.info("getCahce-->evictIfPresent");
            valueWrapper = dayCache.get(cacheVal);
            log.info("getCahce-->cacheKey={},cacheVal={}", cacheKey,cacheVal);
            if(valueWrapper == null){
                log.info("getCahce-->valueWrapper is null");
            }else{
                log.info("getCahce-->valueWrapper={}", JSON.toJSONString(valueWrapper.get()));
            }
        }


    }
}