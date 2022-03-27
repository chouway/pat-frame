package com.pat.api.service.poet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pat.api.bo.PoetBaikeBO;
import com.pat.api.bo.PoetInfoBO;
import com.pat.api.bo.PoetPropertyBO;
import com.pat.api.constant.PatConstant;
import com.pat.api.constant.PoetCharConstant;
import com.pat.api.constant.PoetIndexConstant;
import com.pat.api.constant.PoetRelConstant;
import com.pat.api.entity.*;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.*;
import com.pat.starter.cache.constant.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    private PoetSetMapper poetSetMapper;

    @Autowired
    private PoetChapterMapper poetChapterMapper;

    @Autowired
    private PoetSectionMapper poetSectionMapper;

    @Autowired
    private PoetInfoMapper poetInfoMapper;

    @Autowired
    private PoetContentMapper poetContentMapper;

    @Autowired
    private PoetBaikeMapper poetBaikeMapper;

    @Autowired
    private PoetPropertyMapper poetPropertyMapper;


    @Override
    @Cacheable(value = CacheConstant.DAY, key = "'pi_'+#id")
    public PoetInfoBO getInfoById(Long id) {
        try {
            if (id == null) {
                throw new BusinessException("id为空");
            }
            log.info("getInfoById-->id={}", id);
            PoetInfoBO poetInfoBO = poetInfoMapper.getPoetInfoBO(id);
            List<PoetContent> poetContents = poetContentMapper.createLambdaQuery().andEq(PoetContent::getInfoId, id).asc(PoetContent::getIndex).select();
            List<String> paragraphs = new ArrayList<String>();
            for (PoetContent poetContent : poetContents) {
                paragraphs.add(poetContent.getParagraph());
            }
            poetInfoBO.setParagraphs(paragraphs);
            return poetInfoBO;
        } catch (BusinessException e) {
            log.error("busi error:{}-->[id]={}", e.getMessage(), JSON.toJSONString(new Object[]{id}), e);
            throw e;
        } catch (Exception e) {
            log.error("error:{}-->[id]={}", e.getMessage(), JSON.toJSONString(new Object[]{id}), e);
            throw new BusinessException("查询失败");
        }
    }

    @Override
    @Cacheable(value = CacheConstant.DAY, key = "'bk_'+#infoId")
    public PoetBaikeBO getBaikeById(Long infoId) {
        try {
            if (infoId == null ) {
                throw new BusinessException("id为空");
            }
            PoetInfo poetInfo = poetInfoMapper.single(infoId);
            PoetBaike poetBaike = poetBaikeMapper.single(poetInfo.getBaikeId());

            PoetBaikeBO poetBaikeBO = new PoetBaikeBO();
            poetBaikeBO.setId(poetBaike.getId());
            poetBaikeBO.setBaikeUrl(poetBaike.getBaikeUrl());
            poetBaikeBO.setBaikeTitle(poetBaike.getBaikeTitle());
            String baikeDesc = poetBaike.getBaikeDesc();
            this.setBaikeDescs(poetBaike, poetBaikeBO, baikeDesc);

            poetBaikeBO.setPoetTitle(poetInfo.getTitle());
            Long setId = poetInfo.getSetId();
            PoetSet poetSet = poetSetMapper.single(setId);
            poetBaikeBO.setPoetSet(poetSet.getNameCn());
            Long chapterId = poetInfo.getChapterId();
            if(chapterId!=null){
                PoetChapter poetChapter = poetChapterMapper.single(chapterId);
                poetBaikeBO.setPoetChapter(poetChapter.getChapter());
            }
            Long sectionId = poetInfo.getSectionId();
            if(sectionId!=null){
                PoetSection poetSection = poetSectionMapper.single(chapterId);
                poetBaikeBO.setPoetSection(poetSection.getSection());
            }
            setPropertyBOs(poetBaike, poetBaikeBO);
            return poetBaikeBO;
        } catch (Exception e) {
            log.error("error:-->[infoId]={}", JSON.toJSONString(new Object[]{infoId}), e);
            throw new BusinessException("获取百科失败");
        }
    }
    /* -----private method spilt----- */
    private void setPropertyBOs(PoetBaike poetBaike, PoetBaikeBO poetBaikeBO) {
        List<PoetProperty> properties = poetPropertyMapper.createLambdaQuery().andEq(PoetProperty::getRelType, poetBaike.getRelType()).andEq(PoetProperty::getRelId, poetBaike.getRelId()).andEq(PoetProperty::getStatus, PatConstant.SUCCESS).asc(PoetProperty::getIndex).select();
        if(!CollectionUtils.isEmpty(properties)){
            List<PoetPropertyBO> propertyBOs  = new ArrayList<PoetPropertyBO>();
            for (PoetProperty property : properties) {
                PoetPropertyBO poetPropertyBO = new PoetPropertyBO();
                poetPropertyBO.setKey(property.getKey());
                poetPropertyBO.setValue(property.getValue());
                propertyBOs.add(poetPropertyBO);
            }
            poetBaikeBO.setPropertyBOs(propertyBOs);
        }
    }

    private void setBaikeDescs(PoetBaike poetBaike, PoetBaikeBO poetBaikeBO, String baikeDesc) {
        if(StringUtils.hasText(baikeDesc)){
            List<String> baikeDescs = new ArrayList<String>();
            String baikeDescParas = poetBaike.getBaikeDescParas();
            if(StringUtils.hasText(baikeDescParas)){
                String[] paras = baikeDescParas.split(PoetCharConstant.CHAR_COMMA);
                int index = 0;
                int start = 0;
                int end = 0;
                for (String para : paras) {
                    start = index;
                    end = Integer.parseInt(para);
                    baikeDescs.add(baikeDesc.substring(start,end));
                    index = end;
                }
            }else{
                baikeDescs.add(baikeDesc);
            }
            poetBaikeBO.setBaikeDescs(baikeDescs);
        }
    }
}
