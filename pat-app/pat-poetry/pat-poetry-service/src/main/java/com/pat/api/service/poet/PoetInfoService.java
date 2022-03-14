package com.pat.api.service.poet;

import com.alibaba.fastjson.JSON;
import com.pat.api.bo.PoetInfoBO;
import com.pat.api.entity.PoetContent;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PoetContentMapper;
import com.pat.api.mapper.PoetInfoMapper;
import com.pat.api.mapper.PoetPropertyMapper;
import com.pat.starter.cache.constant.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    
    @Autowired
    private PoetContentMapper poetContentMapper;


    @Override
    @Cacheable(value = CacheConstant.DAY,key = "'pi_'+#id")
    public PoetInfoBO getBoById(Long id) {
        try{
            if(id == null){
                throw new BusinessException("id为空");
            }
            log.info("getBoById-->id={}", id);
            PoetInfoBO poetInfoBO = poetInfoMapper.getPoetInfoBO(id);
            List<PoetContent> poetContents = poetContentMapper.createLambdaQuery().andEq(PoetContent::getInfoId, id).asc(PoetContent::getIndex).select();
            List<String> paragraphs = new ArrayList<String>();
            for (PoetContent poetContent : poetContents) {
                paragraphs.add(poetContent.getParagraph());
            }
            poetInfoBO.setParagraphs(paragraphs);
            return poetInfoBO;
        }catch (BusinessException e){
            log.error("busi error:{}-->[id]={}",e.getMessage(), JSON.toJSONString(new Object[]{id}),e);
            throw e;
        }catch (Exception e){
            log.error("error:{}-->[id]={}",e.getMessage(),JSON.toJSONString(new Object[]{id}),e);
            throw new BusinessException("查询失败");
        }
    }
}
