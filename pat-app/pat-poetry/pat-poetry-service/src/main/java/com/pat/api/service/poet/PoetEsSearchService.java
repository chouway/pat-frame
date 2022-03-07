package com.pat.api.service.poet;

import com.alibaba.fastjson.JSON;
import com.pat.api.bo.EsPropBO;
import com.pat.api.bo.EsSearchBO;
import com.pat.api.constant.PoetIndexConstant;
import com.pat.api.constant.PoetSearchTempConstant;
import com.pat.api.exception.BusinessException;
import com.pat.api.service.mo.PoetAggsInfoMO;
import com.pat.api.service.mo.PoetSearchPageMO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/**
 * PoetEsSearchService
 *
 * @author chouway
 * @date 2022.03.03
 */
@Slf4j
@Service
public class PoetEsSearchService implements IPoetEsSearchService {

    private Integer DEFAUTE_SIZE = 32;

    private Integer DEFAUTE_PAGE_NUM = 1;

    private Integer MAX_NUM = 100;

    @Autowired
    private IPoetEsSearchTempService poetEsSearchTempService;

    @Override
    public String search(EsSearchBO esSearchBO) {
        try{
            PoetSearchPageMO poetSearchPageMO = new PoetSearchPageMO();
            poetSearchPageMO.setFrom(0);
            poetSearchPageMO.setSize(DEFAUTE_SIZE);
            List<PoetAggsInfoMO> aggsInfos = new ArrayList<PoetAggsInfoMO>();
            PoetAggsInfoMO poetAggsInfoMO = new PoetAggsInfoMO();
            poetAggsInfoMO.setAggsName("num_perKey");
            poetAggsInfoMO.setField("propKeys");
            poetAggsInfoMO.setSize(DEFAUTE_SIZE);
            poetAggsInfoMO.setEnd(PoetSearchTempConstant.CHAR_EMPTY);
            aggsInfos.add(poetAggsInfoMO);
            poetSearchPageMO.setAggsInfos(aggsInfos);
            poetSearchPageMO.setNoSources(true);
            poetSearchPageMO.setNeedHighLight(true);
            this.putMO(esSearchBO,poetSearchPageMO);
            return poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_INFO,poetSearchPageMO,PoetSearchTempConstant.POET_SEARCH_PAGE);
        }catch(Exception e){
            log.error("error:-->[esSearchBO]={}", JSON.toJSONString(new Object[]{esSearchBO}),e);
           throw new BusinessException("搜索失败");
        }
    }

    @Override
    public String aggs(EsSearchBO esSearchBO) {
        try{
            List<String> props = esSearchBO.getAggsPropKeys();
            if(CollectionUtils.isEmpty(props)){
                return null;
            }
            PoetSearchPageMO poetSearchPageMO = new PoetSearchPageMO();
            this.putMO(esSearchBO,poetSearchPageMO);
            poetSearchPageMO.setFrom(0);
            poetSearchPageMO.setSize(0);
            List<String> limitSources = new ArrayList<String>();
            limitSources.add("");
            poetSearchPageMO.setNoSources(true);
            List<PoetAggsInfoMO> aggsInfos = new ArrayList<PoetAggsInfoMO>();
            for (int i = 0; i < props.size(); i++) {
                String aggsPropKey = props.get(i);
                PoetAggsInfoMO poetAggsInfoMO = new PoetAggsInfoMO();
                poetAggsInfoMO.setAggsName(String.format("vals_perkey_%s",i));
                poetAggsInfoMO.setField(String.format("properties.%s.keyword",aggsPropKey));
                poetAggsInfoMO.setSize(32);
                aggsInfos.add(poetAggsInfoMO);
            }
            aggsInfos.get(aggsInfos.size()-1).setEnd(PoetSearchTempConstant.CHAR_EMPTY);
            poetSearchPageMO.setAggsInfos(aggsInfos);
            return poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_INFO,poetSearchPageMO,PoetSearchTempConstant.POET_SEARCH_PAGE);
        }catch(Exception e){
            log.error("error:-->[esSearchBO]={}",JSON.toJSONString(new Object[]{esSearchBO}),e);
           throw new BusinessException("筛选失败");
        }
    }

    @Override
    public String get(Long id) {
        return poetEsSearchTempService.get(PoetIndexConstant.POET_INFO,id);
    }

    /**
     * 转换出模版请求MO
     * @param esSearchBO
     * @return
     */
    private PoetSearchPageMO getPoetSearchPageMO(EsSearchBO esSearchBO) {
        PoetSearchPageMO poetSearchPageMO = new PoetSearchPageMO();
        poetSearchPageMO.setFrom(0);
        poetSearchPageMO.setSize(DEFAUTE_SIZE);
        List<PoetAggsInfoMO> aggsInfos = new ArrayList<PoetAggsInfoMO>();
        PoetAggsInfoMO poetAggsInfoMO = new PoetAggsInfoMO();
        poetAggsInfoMO.setAggsName("num_per_propKey");
        poetAggsInfoMO.setField("propKeys");
        poetAggsInfoMO.setSize(DEFAUTE_SIZE);
        poetAggsInfoMO.setEnd(PoetSearchTempConstant.CHAR_EMPTY);
        aggsInfos.add(poetAggsInfoMO);
        poetSearchPageMO.setAggsInfos(aggsInfos);
        if(esSearchBO != null){
            putMO(esSearchBO, poetSearchPageMO);
        }
        return poetSearchPageMO;
    }

    private void putMO(EsSearchBO esSearchBO, PoetSearchPageMO poetSearchPageMO) {
        Integer size = esSearchBO.getSize();
        if(esSearchBO.getSize() == null){
            esSearchBO.setSize(DEFAUTE_SIZE);
        }
        if(esSearchBO.getSize()>MAX_NUM){
            esSearchBO.setSize(MAX_NUM);
        }

        if(esSearchBO.getPageNum()==null){
            esSearchBO.setPageNum(DEFAUTE_PAGE_NUM);
        }

        if(esSearchBO.getPageNum()>MAX_NUM){
            esSearchBO.setPageNum(MAX_NUM);
        }
        poetSearchPageMO.setSize(esSearchBO.getSize());
        poetSearchPageMO.setFrom(esSearchBO.getFrom());
        String key = esSearchBO.getKey();
        boolean hasKey = false;
        if(StringUtils.hasText(key)){
            hasKey = true;
            poetSearchPageMO.setHasKey(hasKey);
            poetSearchPageMO.setKey(key);
        }
        List<EsPropBO> props = esSearchBO.getProps();
        boolean hasProps = false;
        if(!CollectionUtils.isEmpty(props)){
            hasProps = true;
            poetSearchPageMO.setHasProps(hasProps);
            props.get(props.size()-1).setEnd(PoetSearchTempConstant.CHAR_EMPTY);
            List<String> propKeys = new ArrayList<String>();
            for (EsPropBO prop : props) {
                propKeys.add(prop.getPropKey());
            }
            poetSearchPageMO.setPropKeys(propKeys);
            poetSearchPageMO.setProps(props);
        }
        if(hasProps&&hasKey){
            poetSearchPageMO.setHasJoin(true);
        }
    }
}
