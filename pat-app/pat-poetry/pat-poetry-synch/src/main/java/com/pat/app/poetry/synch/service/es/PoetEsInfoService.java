package com.pat.app.poetry.synch.service.es;

import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pat.api.constant.EsConstant;
import com.pat.api.constant.PatConstant;
import com.pat.api.constant.PoetRelConstant;
import com.pat.api.entity.*;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.*;
import com.pat.app.poetry.synch.eo.PoetInfoEO;
import com.pat.app.poetry.synch.repo.PoetInfoRepository;
import com.pat.app.poetry.synch.repo.PoetSuggestRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.beetl.sql.core.page.PageResult;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
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
public class PoetEsInfoService {

    @Autowired
    private PoetSetMapper poetSetMapper;

    @Autowired
    private PoetInfoMapper poetInfoMapper;

    @Autowired
    private PoetInfoRepository poetInfoRepository;

    @Autowired
    private PoetSuggestRepository suggestRepository;

    @Autowired
    private PoetContentMapper poetContentMapper;

    @Autowired
    private PoetAuthorMapper poetAuthorMapper;

    @Autowired
    private PoetPropertyMapper poetPropertyMapper;

    @Autowired
    private PoetBaikeMapper poetBaikeMapper;

    @Autowired
    private PoetChapterMapper poetChapterMapper;

    @Autowired
    private PoetSectionMapper poetSectionMapper;

    @Autowired
    private RestClientBuilder restClientBuilder;

    /**
     * 同步所有待推送百科
     */
    public void synchPoetEs(){
        int total = 0;
        int pageNumber = 1;
        int pageSize = 500;
        PageResult<PoetInfo> page = null;
        do{
            page = poetInfoMapper.createLambdaQuery().andEq(PoetInfo::getEsStatus,PatConstant.INIT).andEq(PoetInfo::getEsCheck,PatConstant.TRUE).page(pageNumber, pageSize);
            List<PoetInfo> list = page.getList();
            List<Long> infoIds = new ArrayList<Long>();
            for (PoetInfo poetInfo : list) {
                PoetInfoEO poetInfoEO = this.getPoetInfoEO(poetInfo);
                try{
                   total++;
                   poetInfoRepository.save(poetInfoEO);
                   this.saveEsStatus(poetInfo.getId(),PatConstant.TRUE,poetInfo.getVersion());
                }catch(Exception e){
                    if(EsConstant.isOK(e.getMessage())){
                        this.saveEsStatus(poetInfo.getId(),PatConstant.TRUE,poetInfo.getVersion());
                    }else{
                        log.error("synchPoetEs error:-->[[]]={}", JSON.toJSONString(new Object[]{poetInfoEO}),e);
                        this.saveEsStatus(poetInfo.getId(),PatConstant.FALSE,poetInfo.getVersion());
                    }
                    continue;
                }
            }
            ++pageNumber;
        }while(pageNumber<page.getTotalPage());
        log.info("synchPoetEs-->total={}", total);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveEsStatus(Long infoId,String esStatus,Long version){
        PoetInfo poetInfo = new PoetInfo();
        poetInfo.setId(infoId);
        poetInfo.setEsStatus(esStatus);
        poetInfo.setUpdateTs(new Date());
        poetInfo.setVersion(version);
        poetInfoMapper.updateTemplateById(poetInfo);
    }


    /**
     * 获取分词结果
     * @param source
     * @return
     */
    public Vector<String> participle(String text,String analyzer){
        Request request = new Request(Method.GET.toString(), "_analyze");
        Map<String,Object> jsonEntity = new HashMap<String,Object>();
        jsonEntity.put("analyzer",analyzer);
        jsonEntity.put("text",text);
        request.setJsonEntity(JSON.toJSONString(jsonEntity));
        String result = reqES(request);
        JSONObject resultObj = JSON.parseObject(result);
        JSONArray tokens = resultObj.getJSONArray("tokens");
        Vector<String> parts = new Vector<String>() ;
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.getJSONObject(i).getString("token");
            parts.add(token);
        }
        return parts;
    }

    private PoetInfoEO getPoetInfoEO(PoetInfo poetInfo) {

        PoetInfoEO poetInfoEO = new PoetInfoEO();
        poetInfoEO.setId(poetInfo.getId());
        poetInfoEO.setTitle(poetInfo.getTitle());
        poetInfoEO.setSubtitle(poetInfo.getSubtitle());
        poetInfoEO.setContent(poetContentMapper.getContent(poetInfo.getId()));
        poetInfoEO.setAuthor(poetAuthorMapper.unique(poetInfo.getAuthorId()).getName());
        poetInfoEO.setIndex(poetInfo.getIndex());
        poetInfoEO.setCount(poetInfo.getCount());

        PoetBaike poetBaike = poetBaikeMapper.createLambdaQuery().andEq(PoetBaike::getRelType, PoetRelConstant.REL_TYPE_INFO).andEq(PoetBaike::getRelId, poetInfo.getId()).singleSimple();
        poetInfoEO.setBaikeDesc(poetBaike.getBaikeDesc());

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
                if(!properties.containsKey(key)){
                    properties.put(key,value);
                }
            }
        }
        poetInfoEO.setProperties(properties);
        poetInfoEO.setPropKeys(new ArrayList<String>(properties.keySet()));
        return poetInfoEO;
    }

    private String reqES(Request request) {
        try{
           RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
           Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
           return  EntityUtils.toString(response.getEntity());
        }catch(Exception e){
           throw new BusinessException("请求ES失败",e);
        }
    }
}
