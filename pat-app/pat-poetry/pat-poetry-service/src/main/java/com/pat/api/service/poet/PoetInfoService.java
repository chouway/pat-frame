package com.pat.api.service.poet;

import com.alibaba.fastjson.JSON;
import com.pat.api.bo.PoetInfoBO;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PoetInfoMapper;
import com.pat.starter.cache.constant.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * PoetInfoService
 *
 * @author chouway
 * @date 2022.03.14
 */
@Slf4j
@Service
public class PoetInfoService implements IPoetInfoService {

    @Autowired
    private PoetInfoMapper poetInfoMapper;

    @Override
    @Cacheable(value = CacheConstant.DAY,key = "'pi_'+#id")
    public PoetInfoBO getBoById(Long id) {
        try{
            if(id == null){
                throw new BusinessException("id为空");
            }
            log.info("getBoById-->id={}", id);
            return poetInfoMapper.getPoetInfoBO(id);
        }catch (BusinessException e){
            log.error("busi error:{}-->[id]={}",e.getMessage(), JSON.toJSONString(new Object[]{id}),e);
            throw e;
        }catch (Exception e){
            log.error("error:{}-->[id]={}",e.getMessage(),JSON.toJSONString(new Object[]{id}),e);
            throw new BusinessException("查询失败");
        }
    }
}
