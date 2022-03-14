package com.pat.api.service.poet;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.pat.api.bo.EsPropBO;
import com.pat.api.bo.EsSearchBO;
import com.pat.api.bo.EsSuggestBO;
import com.pat.api.constant.PoetIndexConstant;
import com.pat.api.constant.PoetSearchTempConstant;
import com.pat.api.entity.PoetSet;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PoetSetMapper;
import com.pat.api.service.mo.PoetAggsInfoMO;
import com.pat.api.service.mo.PoetSearchPageMO;
import com.pat.api.service.mo.PoetSuggestInfoMO;
import com.pat.api.service.mo.PoetSuggestPageMO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        try {
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
            this.putMO(esSearchBO, poetSearchPageMO);
            return poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_INFO, poetSearchPageMO, PoetSearchTempConstant.POET_SEARCH_PAGE);
        } catch (Exception e) {
            log.error("error:-->[esSearchBO]={}", JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("搜索失败");
        }
    }

    @Override
    public String aggs(EsSearchBO esSearchBO) {
        try {
            List<String> props = esSearchBO.getAggsPropKeys();
            if (CollectionUtils.isEmpty(props)) {
                return null;
            }
            PoetSearchPageMO poetSearchPageMO = new PoetSearchPageMO();
            this.putMO(esSearchBO, poetSearchPageMO);
            poetSearchPageMO.setFrom(0);
            poetSearchPageMO.setSize(0);
            poetSearchPageMO.setNoSources(true);
            List<PoetAggsInfoMO> aggsInfos = new ArrayList<PoetAggsInfoMO>();
            for (int i = 0; i < props.size(); i++) {
                String aggsPropKey = props.get(i);
                PoetAggsInfoMO poetAggsInfoMO = new PoetAggsInfoMO();
                poetAggsInfoMO.setAggsName(String.format("vals_perkey_%s", i));
                poetAggsInfoMO.setField(String.format("properties.%s.keyword", aggsPropKey));
                poetAggsInfoMO.setSize(32);
                aggsInfos.add(poetAggsInfoMO);
            }
            aggsInfos.get(aggsInfos.size() - 1).setEnd(PoetSearchTempConstant.CHAR_EMPTY);
            poetSearchPageMO.setAggsInfos(aggsInfos);
            return poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_INFO, poetSearchPageMO, PoetSearchTempConstant.POET_SEARCH_PAGE);
        } catch (Exception e) {
            log.error("error:-->[esSearchBO]={}", JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("筛选失败");
        }
    }

    @Override
    public List<EsSuggestBO> suggest(EsSuggestBO esSuggestBO) {
        try { if (esSuggestBO == null ) {
                esSuggestBO = new EsSuggestBO();
            }
            if(esSuggestBO.getKeyword()!=null){
                esSuggestBO.setKeyword(esSuggestBO.getKeyword().trim());
            }
            PoetSuggestPageMO poetSuggestPageMO = this.getPoetSuggestPageMO(esSuggestBO);
            String suggestStrs = poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_SUGGEST, poetSuggestPageMO, PoetSearchTempConstant.POET_SUGGEST_PAGE);
            return resolveSuggestResult(esSuggestBO, suggestStrs);
        } catch (Exception e) {
            log.error("error:-->[esSuggestBO]={}", JSON.toJSONString(new Object[]{esSuggestBO}), e);
            throw new BusinessException("推荐失败");
        }
    }

    private List<EsSuggestBO> resolveSuggestResult(EsSuggestBO esSuggestBO, String suggestStrs) {
        JSONObject jsonObject = JSON.parseObject(suggestStrs);
        JSONArray fullSuggests = (JSONArray) JSONPath.eval(jsonObject, "/suggest/fullSuggest/options");
        JSONArray prefixSuggests = (JSONArray) JSONPath.eval(jsonObject, "/suggest/prefixSuggest/options");
        JSONArray ikPreSuggests = (JSONArray) JSONPath.eval(jsonObject, "/suggest/ikPreSuggest/options");
        JSONArray hits = (JSONArray) JSONPath.eval(jsonObject, "/hits/hits");
        Map<Long, String> resultMap = new LinkedHashMap<Long, String>();
        if ("\\w+".matches(esSuggestBO.getKeyword())) {//纯字母数字
            this.putSuggests(ikPreSuggests, resultMap);
            this.putSuggests(fullSuggests, resultMap);
            this.putSuggests(prefixSuggests, resultMap);
            this.putSuggests(hits, resultMap);
        } else {
            this.putSuggests(ikPreSuggests, resultMap);
            this.putSuggests(hits, resultMap);
            this.putSuggests(fullSuggests, resultMap);
            this.putSuggests(prefixSuggests, resultMap);
        }
        List<EsSuggestBO> result = new ArrayList<EsSuggestBO>();
        for (Map.Entry<Long, String> entry : resultMap.entrySet()) {
            EsSuggestBO temBO = new EsSuggestBO();
            temBO.setId(entry.getKey());
            temBO.setKeyword(entry.getValue());
            result.add(temBO);
        }
        return result;
    }

    private List<EsSuggestBO> getPoetSetSuggest() {
//        poetSetMapper.createLambdaQuery().asc(PoetSet::)
        return null;
    };


    @Override
    public String get(Long id) {
        return poetEsSearchTempService.get(PoetIndexConstant.POET_INFO, id);
    }

    /**
     * 转换出模版请求MO
     *
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
        if (esSearchBO != null) {
            putMO(esSearchBO, poetSearchPageMO);
        }
        return poetSearchPageMO;
    }

    private void putMO(EsSearchBO esSearchBO, PoetSearchPageMO poetSearchPageMO) {
        Integer size = esSearchBO.getSize();
        if (esSearchBO.getSize() == null) {
            esSearchBO.setSize(DEFAUTE_SIZE);
        }
        if (esSearchBO.getSize() > MAX_NUM) {
            esSearchBO.setSize(MAX_NUM);
        }

        if (esSearchBO.getPageNum() == null) {
            esSearchBO.setPageNum(DEFAUTE_PAGE_NUM);
        }

        if (esSearchBO.getPageNum() > MAX_NUM) {
            esSearchBO.setPageNum(MAX_NUM);
        }
        poetSearchPageMO.setSize(esSearchBO.getSize());
        poetSearchPageMO.setFrom(esSearchBO.getFrom());
        String key = esSearchBO.getKey();
        boolean hasKey = false;
        if (StringUtils.hasText(key)) {
            hasKey = true;
            poetSearchPageMO.setHasKey(hasKey);
            poetSearchPageMO.setKey(key);
        }
        List<EsPropBO> props = esSearchBO.getProps();
        boolean hasProps = false;
        if (!CollectionUtils.isEmpty(props)) {
            hasProps = true;
            poetSearchPageMO.setHasProps(hasProps);
            props.get(props.size() - 1).setEnd(PoetSearchTempConstant.CHAR_EMPTY);
            List<String> propKeys = new ArrayList<String>();
            for (EsPropBO prop : props) {
                propKeys.add(prop.getPropKey());
            }
            poetSearchPageMO.setPropKeys(propKeys);
            poetSearchPageMO.setProps(props);
        }
        if (hasProps && hasKey) {
            poetSearchPageMO.setHasJoin(true);
        }
    }

    private PoetSuggestPageMO getPoetSuggestPageMO(EsSuggestBO esSuggestBO) {
        if (esSuggestBO.getSize() == null) {
            esSuggestBO.setSize(15);
        }
        if (esSuggestBO.getSize() > 32) {
            esSuggestBO.setSize(32);
        }
        Integer size = esSuggestBO.getSize();
        String keyword = esSuggestBO.getKeyword();
        PoetSuggestPageMO poetSuggestPageMO = new PoetSuggestPageMO();
        poetSuggestPageMO.setSize(size);
        poetSuggestPageMO.setKeyword(keyword);
        if(StringUtils.hasText(esSuggestBO.getKeyword())){
            poetSuggestPageMO.setHaveKeyword(true);
        }else{
            poetSuggestPageMO.setHaveKeyword(false);
            return poetSuggestPageMO;
        }

        List<PoetSuggestInfoMO> suggestInfoMOs = new ArrayList<PoetSuggestInfoMO>();

        PoetSuggestInfoMO ikPreSuggest = new PoetSuggestInfoMO();
        ikPreSuggest.setSuggestName("ikPreSuggest");
        ikPreSuggest.setField("suggestText");
        ikPreSuggest.setSize(size);
        ikPreSuggest.setKeyword(keyword);
        suggestInfoMOs.add(ikPreSuggest);

        PoetSuggestInfoMO fullSuggest = new PoetSuggestInfoMO();
        fullSuggest.setSuggestName("fullSuggest");
        fullSuggest.setField("fullPinyin");
        fullSuggest.setSize(size);
        fullSuggest.setKeyword(PinyinUtil.getPinyin(keyword, ""));
        suggestInfoMOs.add(fullSuggest);

        PoetSuggestInfoMO prefixSuggest = new PoetSuggestInfoMO();
        prefixSuggest.setSuggestName("prefixSuggest");
        prefixSuggest.setField("prefixPinyin");
        prefixSuggest.setSize(size);
        prefixSuggest.setKeyword(PinyinUtil.getFirstLetter(keyword, ""));
        prefixSuggest.setEnd(PoetSearchTempConstant.CHAR_EMPTY);
        suggestInfoMOs.add(prefixSuggest);

        poetSuggestPageMO.setSuggestInfos(suggestInfoMOs);
//      log.info("getPoetSuggestPageMO-->poetSuggestPageMO={}", JSON.toJSONString(poetSuggestPageMO));
        return poetSuggestPageMO;
    }

    private void putSuggests(JSONArray jsonArray, Map<Long, String> result) {
        if(jsonArray == null){
            return;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Long id = jsonObj.getLong("_id");
            if (result.containsKey(id)) {
                continue;
            }
            result.put(id, jsonObj.getJSONObject("_source").getString("suggestText"));
        }
    }
}
