package com.pat.api.service.poet;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.pat.api.bo.*;
import com.pat.api.constant.PoetCharConstant;
import com.pat.api.constant.PoetIndexConstant;
import com.pat.api.constant.PoetSearchTempConstant;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PoetChapterMapper;
import com.pat.api.mapper.PoetInfoMapper;
import com.pat.api.mapper.PoetSectionMapper;
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

import java.util.*;

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
    private PoetInfoMapper poetInfoMapper;

    @Autowired
    private IPoetEsSearchTempService poetEsSearchTempService;

    @Autowired
    private IPoetInfoService poetInfoService;

    @Autowired
    private PoetSetMapper poetSetMapper;

    @Autowired
    private PoetChapterMapper poetChapterMapper;

    @Autowired
    private PoetSectionMapper poetSectionMapper;

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
            poetAggsInfoMO.setEnd(PoetCharConstant.CHAR_EMPTY);
            aggsInfos.add(poetAggsInfoMO);
            poetSearchPageMO.setAggsInfos(aggsInfos);
            poetSearchPageMO.setNoSources(true);
            if (esSearchBO.isHighlight()) {
                poetSearchPageMO.setNeedHighLight(true);
            }
            this.putMO(esSearchBO, poetSearchPageMO);
            return poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_INFO, poetSearchPageMO, PoetSearchTempConstant.POET_SEARCH_PAGE);
        } catch (Exception e) {
            log.error("error:-->[esSearchBO]={}", JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("搜索失败");
        }
    }

    @Override
    public PoetSearchResultBO searchBO(EsSearchBO esSearchBO) {
        try {
            PoetSearchResultBO poetSearchResultBO = new PoetSearchResultBO();
            poetSearchResultBO.setPageSize(esSearchBO.getSize());
            poetSearchResultBO.setPageNum(esSearchBO.getPageNum());
            String searchResult = this.search(esSearchBO);
            JSONObject searchJson = JSON.parseObject(searchResult);
            Integer totalVal = (Integer) JSONPath.eval(searchJson, "/hits/total/value");
            if (totalVal > 1000) {
                totalVal = 1000;
            }
            poetSearchResultBO.setTotal(totalVal);
            if (totalVal > 0) {
                JSONArray hits = (JSONArray) JSONPath.eval(searchJson, "/hits/hits");
                List<PoetInfoBO> poetInfoBOs = new ArrayList<PoetInfoBO>();
                if (hits != null) {
                    for (int i = 0; i < hits.size(); i++) {
                        JSONObject hit = hits.getJSONObject(i);
                        PoetInfoBO poetInfoBO = poetInfoService.getInfoById(hit.getLong("_id"));
                        if (esSearchBO.isHighlight()) {
                            dealHighLight(hit, poetInfoBO);
                        }
                        poetInfoBOs.add(poetInfoBO);
                    }
                    poetSearchResultBO.setPoetInfoBOs(poetInfoBOs);

                }
            }
            //聚合筛选
            JSONArray buckets = (JSONArray) JSONPath.eval(searchJson, "/aggregations/num_perKey/buckets");
            if (buckets != null && buckets.size() > 0) {
                List<String> propKeys = new ArrayList<String>();
                for (int i = 0; i < buckets.size(); i++) {
                    JSONObject bucket = buckets.getJSONObject(i);
                    propKeys.add(bucket.getString("key"));
                }
                poetSearchResultBO.setPropKeys(propKeys);
            }
            return poetSearchResultBO;
        } catch (BusinessException e) {
            log.error("busi error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw e;
        } catch (Exception e) {
            log.error("error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("系统错误");
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
            aggsInfos.get(aggsInfos.size() - 1).setEnd(PoetCharConstant.CHAR_EMPTY);
            poetSearchPageMO.setAggsInfos(aggsInfos);
            return poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_INFO, poetSearchPageMO, PoetSearchTempConstant.POET_SEARCH_PAGE);
        } catch (Exception e) {
            log.error("error:-->[esSearchBO]={}", JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("筛选失败");
        }
    }

    @Override
    public List<PoetAggsBO> aggsBO(EsSearchBO esSearchBO) {
        return null;
    }


    @Override
    public List<EsSuggestBO> suggest(EsSuggestBO esSuggestBO) {
        try {
            if (esSuggestBO == null) {
                esSuggestBO = new EsSuggestBO();
            }
            if (esSuggestBO.getKeyword() != null) {
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
        if (esSuggestBO.getKeyword() == null || "\\w+".matches(esSuggestBO.getKeyword())) {//纯字母数字
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
    }

    ;


    @Override
    public String get(Long id) {
        return poetEsSearchTempService.get(PoetIndexConstant.POET_INFO, id);
    }


    /* -----private method spilt----- */

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
        poetAggsInfoMO.setEnd(PoetCharConstant.CHAR_EMPTY);
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
            props.get(props.size() - 1).setEnd(PoetCharConstant.CHAR_EMPTY);
            List<String> propKeys = new ArrayList<String>();
            for (EsPropBO prop : props) {
                propKeys.add(prop.getPropKey());
            }
            poetSearchPageMO.setPropKeys(propKeys);
            poetSearchPageMO.setProps(props);
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
        if (StringUtils.hasText(esSuggestBO.getKeyword())) {
            poetSuggestPageMO.setHaveKeyword(true);
        } else {
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
        prefixSuggest.setEnd(PoetCharConstant.CHAR_EMPTY);
        suggestInfoMOs.add(prefixSuggest);

        poetSuggestPageMO.setSuggestInfos(suggestInfoMOs);
//      log.info("getPoetSuggestPageMO-->poetSuggestPageMO={}", JSON.toJSONString(poetSuggestPageMO));
        return poetSuggestPageMO;
    }

    private void putSuggests(JSONArray jsonArray, Map<Long, String> result) {
        if (jsonArray == null) {
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

    /**
     * 高亮处理
     *
     * @param hit
     * @param poetInfoBO
     */
    private void dealHighLight(JSONObject hit, PoetInfoBO poetInfoBO) {
        JSONObject highlight = hit.getJSONObject("highlight");
        if (highlight != null) {//  //高亮处理
            JSONArray title = highlight.getJSONArray("title");
            if (title != null && title.size() > 0) {
                String highlightTitle = title.getObject(0, String.class);
                poetInfoBO.setTitle(highlightTitle);
            }
            JSONArray author = highlight.getJSONArray("author");
            if (author != null && author.size() > 0) {
                String highlightAuthor = author.getObject(0, String.class);
                poetInfoBO.setAuthor(highlightAuthor);
            }
            JSONArray paragraphs = highlight.getJSONArray("paragraphs");
            if (paragraphs != null && paragraphs.size() > 0) {
                List<String> sourceParagraphs = poetInfoBO.getParagraphs();
                List<String> targetParagraphs = new ArrayList<String>();
                Map<String,String> targetKeys = new LinkedHashMap<String,String>();
                for (int j = 0; j < paragraphs.size(); j++) {
                    String highlightParagraph = paragraphs.getString(j);
                    String key = highlightParagraph.replaceAll("<em>|</em>", "");
                    targetKeys.put(key,highlightParagraph);
                }
                for (String sourceParagraph : sourceParagraphs) {
                    String targetVal = sourceParagraph;
                    for (Map.Entry<String, String> entry : targetKeys.entrySet()) {
                        if(sourceParagraph.contains(entry.getKey())){//高亮替换
                            targetVal = sourceParagraph.replaceAll(entry.getKey(),entry.getValue());
                            break;
                        }
                    }
                    targetParagraphs.add(targetVal);
                }
                poetInfoBO.setParagraphs(targetParagraphs);
            }
        }
    }
}
