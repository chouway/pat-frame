package com.pat.starter.oauth.common.service;

import com.pat.api.entity.PatResource;
import com.pat.api.mapper.PatResourceMapper;
import com.pat.starter.cache.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PatResourceService
 *
 * @author chouway
 * @date 2022.03.29
 */
public class PatResourceService {

    @Autowired
    private PatResourceMapper patResourceMapper;

    @Cacheable(value = CacheConstant.WEEK, key = "'RS_'+#preKey")
    public List<PatResource> getAll(String preKey){
        List<PatResource> all = patResourceMapper.createLambdaQuery().andLike(PatResource::getUri, preKey+"%").desc(PatResource::getIndex).select();
        return all;
    }
}
