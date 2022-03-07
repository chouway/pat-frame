package com.pat.app.poetry.synch.service.es;

import com.alibaba.fastjson.JSON;
import com.pat.api.constant.PatConstant;
import com.pat.api.constant.PoetRelConstant;
import com.pat.api.entity.*;
import com.pat.api.mapper.*;
import com.pat.app.poetry.synch.eo.PoetInfoEO;
import com.pat.app.poetry.synch.repo.PoetInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.page.PageResult;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * PoetEsService
 *
 * @author chouway
 * @date 2022.03.07
 */
@Slf4j
@Service
public class PoetEsPushService {

    @Autowired
    private PoetSetMapper poetSetMapper;

    @Autowired
    private PoetInfoMapper poetInfoMapper;

    @Autowired
    private PoetInfoRepository poetInfoRepository;

    @Autowired
    private PoetContentMapper poetContentMapper;

    @Autowired
    private PoetAuthorMapper poetAuthorMapper;

    @Autowired
    private PoetPropertyMapper poetPropertyMapper;

    @Autowired
    private PoetChapterMapper poetChapterMapper;

    @Autowired
    private PoetSectionMapper poetSectionMapper;

    /**
     * 同步所有待推送百科
     */
    public void synchPoetEs(){
        int pageNumber = 1;
        int pageSize = 500;
        PageResult<PoetInfo> page = null;
        do{
            page = poetInfoMapper.createLambdaQuery().andEq(PoetInfo::getEsStatus,PatConstant.INIT).page(pageNumber, pageSize, PoetInfo::getId);
            List<PoetInfo> list = page.getList();
            List<Long> infoIds = new ArrayList<Long>();
            for (PoetInfo poetInfo : list) {
                PoetInfoEO poetInfoEO = this.getPoetInfoEO(poetInfo);
                try{
                   poetInfoRepository.save(poetInfoEO);
                   this.saveEsStatus(poetInfo.getId(),PatConstant.TRUE);
                }catch(Exception e){
                    this.saveEsStatus(poetInfo.getId(),PatConstant.FAIL);
                    log.error("synchPoetEs error:-->[[]]={}", JSON.toJSONString(new Object[]{poetInfoEO}),e);
                    continue;
                }
            }
            ++pageNumber;
        }while(pageNumber<page.getTotalPage());
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveEsStatus(Long infoId,String esStatus){
        PoetInfo poetInfo = new PoetInfo();
        poetInfo.setId(infoId);
        poetInfo.setEsStatus(esStatus);
        poetInfo.setUpdateTs(new Date());
        poetInfoMapper.updateTemplateById(poetInfo);
    }



    private PoetInfoEO getPoetInfoEO(PoetInfo poetInfo) {

        IndexRequest indexRequest = new IndexRequest();
        PoetInfoEO poetInfoEO = new PoetInfoEO();
        poetInfoEO.setId(poetInfo.getId());
        poetInfoEO.setTitle(poetInfo.getTitle());
        poetInfoEO.setSubtitle(poetInfo.getSubtitle());
        poetInfoEO.setContent(poetContentMapper.getContent(poetInfo.getId()));
        poetInfoEO.setAuthor(poetAuthorMapper.unique(poetInfo.getAuthorId()).getName());
        poetInfoEO.setIndex(poetInfo.getIndex());
        List<PoetProperty> poetProperties = poetPropertyMapper.createLambdaQuery()
                .andEq(PoetProperty::getRelType, PoetRelConstant.REL_TYPE_INFO)
                .andEq(PoetProperty::getRelId, poetInfo.getId())
                .andEq(PoetProperty::getStatus, PatConstant.TRUE)
                .asc(PoetProperty::getIndex).select();

        Map<String, String> properties = new LinkedHashMap<String, String>();

        Long setId = poetInfo.getSetId();
        if(setId != null){
            PoetSet poetSet = poetSetMapper.unique(setId);
            properties.put("文集",poetSet.getNameCn());
        }
        Long chapterId = poetInfo.getChapterId();
        if(chapterId!=null){
            PoetChapter poetChapter = poetChapterMapper.unique(chapterId);
            properties.put("篇",poetChapter.getChapter());
        }

        Long sectionId = poetInfo.getSectionId();
        if(sectionId!=null){
            PoetSection poetSection = poetSectionMapper.unique(sectionId);
            properties.put("节",poetSection.getSection());
        }
        for (PoetProperty poetProperty : poetProperties) {
            String key = poetProperty.getKey();
            String value = poetProperty.getValue();
            if(StringUtils.hasText(key)&&StringUtils.hasText(value)){
                properties.put(key,value);
            }
        }
        poetInfoEO.setProperties(properties);
        poetInfoEO.setPropKeys(new ArrayList<String>(properties.keySet()));
        return poetInfoEO;
    }
}
