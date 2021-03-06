package com.pat.api.service.poet;

import cn.hutool.extra.pinyin.PinyinEngine;
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
import org.apache.lucene.queryparser.classic.QueryParser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PoetEsSearchService
 *
 * @author chouway
 * @date 2022.03.03
 */
@Slf4j
@Service
public class PoetEsSearchService implements IPoetEsSearchService {

    private Integer DEFAUTE_SIZE = 8;

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
            poetSearchPageMO.setNoSources(true);
            if (esSearchBO.isHighlight()) {
                poetSearchPageMO.setNeedHighLight(true);
            }
            this.putMO(esSearchBO, poetSearchPageMO);
            return poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_INFO, poetSearchPageMO, PoetSearchTempConstant.POET_SEARCH_PAGE);
        } catch (Exception e) {
            log.error("error:-->[esSearchBO]={}", JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("????????????");
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

            return poetSearchResultBO;
        } catch (BusinessException e) {
            log.error("busi error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw e;
        } catch (Exception e) {
            log.error("error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("????????????");
        }
    }

    private final String AGGS_SPLIT_KEY = "_vpk_";//?????? + _vpk_ + key
    private final String AGGS_NAME_META = "%s" + AGGS_SPLIT_KEY + "%s";

    @Override
    public String aggs(EsSearchBO esSearchBO) {
        try {
            //???????????????
            Map<String, Object> result = this.aggsProKeys(esSearchBO);
            PoetSearchPageMO poetSearchPageMO = (PoetSearchPageMO) result.get("poetSearchPageMO");
            List<String> aggsPropKeys = (List<String>) result.get("aggsPropKeys");
            if (CollectionUtils.isEmpty(aggsPropKeys)) {
                return null;
            }
            //???????????????
            return aggsAgain(poetSearchPageMO, aggsPropKeys, esSearchBO.getSize());
        } catch (Exception e) {
            log.error("error:-->[esSearchBO]={}", JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("????????????");
        }
    }


    @Override
    public List<PoetAggsBO> aggsBO(EsSearchBO esSearchBO) {
        try {
            esSearchBO.setSize(8);
            String aggsStr = this.aggs(esSearchBO);
            if (!StringUtils.hasText(aggsStr)) {
                return this.combineProp(null, esSearchBO);
            }
//          log.info("aggsBO-->aggsStr={}", aggsStr);
            JSONObject aggsJSON = JSON.parseObject(aggsStr);
            JSONObject aggregations = aggsJSON.getJSONObject("aggregations");
            if (aggregations == null) {
                return null;
            }
            Map<String, Object> innerMap = aggregations.getInnerMap();
            if (CollectionUtils.isEmpty(innerMap)) {
                return null;
            }
            TreeMap<String, Object> treeMap = new TreeMap<>();
            treeMap.putAll(innerMap);
            //????????????
            List<PoetAggsBO> poetAggsBOs = new ArrayList<PoetAggsBO>();
            for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
                String entryKey = entry.getKey();
                String[] splits = entryKey.split(AGGS_SPLIT_KEY);
                String key = splits[1];
                String jsonPath = "/aggregations/" + entryKey + "/buckets/key";
                JSONArray jsonArray = (JSONArray) JSONPath.eval(aggsJSON, jsonPath);
                PoetAggsBO poetAggsBO = new PoetAggsBO();
                poetAggsBO.setKey(key);
                poetAggsBO.setVals(jsonArray != null ? jsonArray.toJavaList(String.class) : null);
                poetAggsBOs.add(poetAggsBO);
            }
            //??????????????? ?????????????????????????????????key val??????;
            poetAggsBOs = this.combineProp(poetAggsBOs, esSearchBO);
            return poetAggsBOs;
        } catch (BusinessException e) {
            log.error("busi error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw e;
        } catch (Exception e) {
            log.error("error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("????????????");
        }
    }


    @Override
    public List<EsSuggestBO> suggest(EsSuggestBO esSuggestBO) {
        try {
            if (esSuggestBO == null) {
                esSuggestBO = new EsSuggestBO();
            }
            if (esSuggestBO.getKeyword() != null) {
                esSuggestBO.setKeyword(esSuggestBO.getKeyword().trim());
                igornSpecPropKeyVal(esSuggestBO);
            }
            PoetSuggestPageMO poetSuggestPageMO = this.getPoetSuggestPageMO(esSuggestBO);
            String suggestStrs = poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_SUGGEST, poetSuggestPageMO, PoetSearchTempConstant.POET_SUGGEST_PAGE);
            return resolveSuggestResult(esSuggestBO, suggestStrs);
        } catch (Exception e) {
            log.error("error:-->[esSuggestBO]={}", JSON.toJSONString(new Object[]{esSuggestBO}), e);
            throw new BusinessException("????????????");
        }
    }

    private List<EsSuggestBO> resolveSuggestResult(EsSuggestBO esSuggestBO, String suggestStrs) {
        JSONObject jsonObject = JSON.parseObject(suggestStrs);
        JSONArray fullSuggests = (JSONArray) JSONPath.eval(jsonObject, "/suggest/fullSuggest/options");
        JSONArray prefixSuggests = (JSONArray) JSONPath.eval(jsonObject, "/suggest/prefixSuggest/options");
        JSONArray ikPreSuggests = (JSONArray) JSONPath.eval(jsonObject, "/suggest/ikPreSuggest/options");
        JSONArray hits = (JSONArray) JSONPath.eval(jsonObject, "/hits/hits");
        Map<Long, String> resultMap = new LinkedHashMap<Long, String>();
        if (esSuggestBO.getKeyword() == null || "\\w+".matches(esSuggestBO.getKeyword())) {//???????????????
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


    @Override
    public String get(Long id) {
        return poetEsSearchTempService.get(PoetIndexConstant.POET_INFO, id);
    }

    @Override
    public Map<String, Object> aggsProKeys(EsSearchBO esSearchBO) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            PoetSearchPageMO poetSearchPageMO = new PoetSearchPageMO();
            this.putMO(esSearchBO, poetSearchPageMO);
            poetSearchPageMO.setFrom(0);
            poetSearchPageMO.setSize(0);
            poetSearchPageMO.setNoSources(true);
            List<PoetAggsInfoMO> aggsInfos = new ArrayList<PoetAggsInfoMO>();
            PoetAggsInfoMO poetAggsInfoMO = new PoetAggsInfoMO();
            poetAggsInfoMO.setAggsName("num_perKey");
            poetAggsInfoMO.setField("propKeys");
            poetAggsInfoMO.setEnd(PoetCharConstant.CHAR_EMPTY);
            poetAggsInfoMO.setSize(esSearchBO.getSize());
            aggsInfos.add(poetAggsInfoMO);
            poetSearchPageMO.setAggsInfos(aggsInfos);
            //??????????????? ????????????????????????key;
            String aggsPropKeysStr = poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_INFO, poetSearchPageMO, PoetSearchTempConstant.POET_SEARCH_PAGE);
            JSONArray buckets = (JSONArray) JSONPath.eval(JSON.parseObject(aggsPropKeysStr), "/aggregations/num_perKey/buckets");
            List<String> aggsPropKeys = new ArrayList<String>();
            if (buckets != null && buckets.size() > 0) {
                for (int i = 0; i < buckets.size(); i++) {
                    JSONObject bucket = buckets.getJSONObject(i);
                    aggsPropKeys.add(bucket.getString("key"));
                }
            }
            result.put("poetSearchPageMO", poetSearchPageMO);
            //?????????????????????????????? ???????????? ?????????????????????key ?????????
            aggsPropKeys = combinedProps(aggsPropKeys, poetSearchPageMO);
            result.put("aggsPropKeys", aggsPropKeys);
            return result;
        } catch (BusinessException e) {
            log.error("busi error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw e;
        } catch (Exception e) {
            log.error("error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("??????????????????");
        }
    }


    @Override
    public List<PoetAggsKeyBO> getAggsKeys(EsSearchBO esSearchBO) {
        try {
            esSearchBO.setSize(MAX_NUM);
            Map<String, Object> aggsProKeysMap = this.aggsProKeys(esSearchBO);
            List<String> aggsKeys = (List<String>) aggsProKeysMap.get("aggsPropKeys");
            List<PoetAggsKeyBO> poetAggsKeyBOs = new ArrayList<PoetAggsKeyBO>();
            PinyinEngine pyEngine = PinyinUtil.getEngine();
            for (String aggsKey : aggsKeys) {
                PoetAggsKeyBO poetAggsKeyBO = new PoetAggsKeyBO();
                poetAggsKeyBO.setAggsKey(aggsKey);
                poetAggsKeyBO.setFullPY(pyEngine.getPinyin(this.toLowCaseAndKeepChineseAbc(aggsKey), ""));
                poetAggsKeyBO.setFirstPY(pyEngine.getFirstLetter(this.toLowCaseAndKeepChineseFirstAbc(aggsKey), ""));
                poetAggsKeyBOs.add(poetAggsKeyBO);
            }
            return poetAggsKeyBOs;
        } catch (BusinessException e) {
            log.error("busi error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw e;
        } catch (Exception e) {
            log.error("error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("????????????????????????");
        }
    }

    @Override
    public List<PoetAggsValBO> getAggsKeyVals(EsSearchBO esSearchBO) {
        try {
            String aggsKey = esSearchBO.getAggsKey();
            if (!StringUtils.hasText(aggsKey)) {
                throw new BusinessException("???????????????????????????");
            }
            List<String> aggsKeys = new ArrayList<String>();
            aggsKeys.add(aggsKey);
            PoetSearchPageMO poetSearchPageMO = new PoetSearchPageMO();
            this.putMO(esSearchBO, poetSearchPageMO);
            poetSearchPageMO.setFrom(0);
            poetSearchPageMO.setSize(0);
            poetSearchPageMO.setNoSources(true);
            String aggsAgainResult = this.aggsAgain(poetSearchPageMO, aggsKeys, MAX_NUM);
            JSONObject aggsResultJSON = JSON.parseObject(aggsAgainResult);
            String jsonPath = "/aggregations/" + String.format(AGGS_NAME_META, 0, aggsKey) + "/buckets/key";
            JSONArray jsonArray = (JSONArray) JSONPath.eval(aggsResultJSON, jsonPath);
            if (jsonArray == null) {
                return null;
            }
            List<PoetAggsValBO> poetAggsValBOs = new ArrayList<PoetAggsValBO>();
            List<String> aggsVals = jsonArray.toJavaList(String.class);
            PinyinEngine pyEngine = PinyinUtil.getEngine();
            for (String aggsVal : aggsVals) {
                PoetAggsValBO poetAggsValBO = new PoetAggsValBO();
                poetAggsValBO.setAggsVal(aggsVal);
                poetAggsValBO.setFullPY(pyEngine.getPinyin(this.toLowCaseAndKeepChineseAbc(aggsVal), ""));
                poetAggsValBO.setFirstPY(pyEngine.getFirstLetter(this.toLowCaseAndKeepChineseFirstAbc(aggsVal), ""));
                poetAggsValBOs.add(poetAggsValBO);
            }
            return poetAggsValBOs;
        } catch (BusinessException e) {
            log.error("busi error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw e;
        } catch (Exception e) {
            log.error("error:{}-->[esSearchBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{esSearchBO}), e);
            throw new BusinessException("?????????????????????");
        }
    }


    /* -----private method spilt----- */


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

        dealSpecs(esSearchBO, poetSearchPageMO);

        String key = esSearchBO.getKey();
        boolean hasKey = false;
        if (StringUtils.hasText(key)) {
            hasKey = true;
            poetSearchPageMO.setHasKey(hasKey);
            poetSearchPageMO.setKey(QueryParser.escape(key));
            String keyWord = this.getKeyWord(poetSearchPageMO);
            poetSearchPageMO.setKeyWord(keyWord);
        }
        List<EsPropBO> props = esSearchBO.getProps();
        boolean hasProps = false;
        if (!CollectionUtils.isEmpty(props)) {
            List<EsPropBO> checkProps = this.getCheckPropBOs(props);
            if (CollectionUtils.isEmpty(checkProps)) {
                return;
            }
            hasProps = true;
            poetSearchPageMO.setHasProps(hasProps);
            checkProps.get(checkProps.size() - 1).setEnd(PoetCharConstant.CHAR_EMPTY);
//          List<EsPropBO> propMos = this.cloneAndEscapeProps(checkProps);
            poetSearchPageMO.setProps(checkProps);
            esSearchBO.setProps(checkProps);
        }
        Boolean hasPropSpecs = poetSearchPageMO.getHasPropSpecs();
        Boolean hasPropLikes = poetSearchPageMO.getHasPropLikes();
        if (hasProps
                || (hasPropSpecs != null && hasPropSpecs)
                || (hasPropLikes != null && hasPropLikes)
        ) {
            poetSearchPageMO.setHasMust(true);
        }
    }

    private void dealSpecs(EsSearchBO esSearchBO, PoetSearchPageMO poetSearchPageMO) {
        dealSpecPropKeyVal(esSearchBO);
        dealSpecPropKeys(esSearchBO, poetSearchPageMO);
        dealSpecPropLikes(esSearchBO, poetSearchPageMO);
    }

    /**
     * ?????? ?????? {\s*%(?<data>.*?)%\s*}
     */
    private void dealSpecPropLikes(EsSearchBO esSearchBO, PoetSearchPageMO poetSearchPageMO) {
        String key = esSearchBO.getKey();
        Pattern compile = Pattern.compile("\\{\\s*%(?<data>.*?)%\\s*}");
        Matcher matcher = compile.matcher(key);
        List<String> propLikes = new ArrayList<>();

        StringBuffer sbf = new StringBuffer();
        while (matcher.find()) {
            String data = matcher.group("data");
            if (StringUtils.hasText(data)) {
                propLikes.add(String.format("*%s*",QueryParser.escape(data)));
            }

            matcher.appendReplacement(sbf, "");
        }
        if (CollectionUtils.isEmpty(propLikes)) {
            return;
        }
        matcher.appendTail(sbf);
        esSearchBO.setKey(sbf.toString());
        poetSearchPageMO.setHasPropLikes(true);
        esSearchBO.setPropLikes(propLikes);

        List<Map<String, Map<String, String>>> propLikesList = new ArrayList<Map<String, Map<String, String>>>();
        Set<String> set = new LinkedHashSet<String>(propLikes);
        List<String> tempList = new ArrayList<String>(set);//set??????
        for (String propLike : tempList) {
            Map<String, Map<String, String>> temMap = new HashMap<String, Map<String, String>>();
            Map<String, String> innerMap = new HashMap<String, String>();
            innerMap.put("propKeys", propLike);
            temMap.put("wildcard", innerMap);
            propLikesList.add(temMap);

            Map<String, Map<String, String>> temMap2 = new HashMap<String, Map<String, String>>();
            Map<String, String> innerMap2 = new HashMap<String, String>();
            innerMap2.put("propVals", propLike);
            temMap2.put("wildcard", innerMap2);
            propLikesList.add(temMap2);
        }
        poetSearchPageMO.setPropLikesList(propLikesList);

    }

    /**
     * ?????? ??????\{\s*'(?<data>.*?)'\s*}
     *
     * @param esSearchBO
     */
    private void dealSpecPropKeys(EsSearchBO esSearchBO, PoetSearchPageMO poetSearchPageMO) {
        String key = esSearchBO.getKey();
        Pattern compile = Pattern.compile("\\{\\s*'(?<data>.*?)'\\s*}");
        Matcher matcher = compile.matcher(key);
        List<String> propSpecs = new ArrayList<>();

        StringBuffer sbf = new StringBuffer();
        while (matcher.find()) {
            String data = matcher.group("data");
            if (StringUtils.hasText(data)) {
                propSpecs.add(data);
            }

            matcher.appendReplacement(sbf, "");
        }
        if (CollectionUtils.isEmpty(propSpecs)) {
            return;
        }
        matcher.appendTail(sbf);
        esSearchBO.setKey(sbf.toString());
        poetSearchPageMO.setPropSpecs(propSpecs);
        poetSearchPageMO.setHasPropSpecs(true);
    }


    /**
     * ?????????????????????  ???????????????????????????
     *
     * @param key
     * @param props
     * @return
     */
    private List<EsPropBO> getCheckPropBOs(List<EsPropBO> props) {
        Map<String, List<String>> keyValsMap = new LinkedHashMap<String, List<String>>();
        for (EsPropBO prop : props) {
            String propKey = prop.getPropKey();
            List<String> propVals = prop.getPropVals();
            if (!StringUtils.hasText(propKey)) {
                continue;
            }
            if (CollectionUtils.isEmpty(propVals)) {
                continue;
            }
            List<String> vals = null;
            if (keyValsMap.containsKey(propKey)) {
                vals = keyValsMap.get(propKey);
            } else {
                vals = new ArrayList<String>();
            }
            for (String propVal : propVals) {
                if (vals.contains(propVal)) {
                    continue;
                }
                vals.add(propVal);
            }
            if (vals.size() < 1) {
                continue;
            }
            keyValsMap.put(propKey, vals);
        }
        if (CollectionUtils.isEmpty(keyValsMap)) {
            return null;
        }
        List<EsPropBO> checkProps = new ArrayList<EsPropBO>();
        for (Map.Entry<String, List<String>> entry : keyValsMap.entrySet()) {
            EsPropBO esPropBO = new EsPropBO();
            esPropBO.setPropKey(entry.getKey());
            esPropBO.setPropVals(entry.getValue());
            checkProps.add(esPropBO);
        }
        return checkProps;
    }


    private PoetSuggestPageMO getPoetSuggestPageMO(EsSuggestBO esSuggestBO) {
        if (esSuggestBO.getSize() == null) {
            esSuggestBO.setSize(DEFAUTE_SIZE);
        }
        if (esSuggestBO.getSize() > DEFAUTE_SIZE) {
            esSuggestBO.setSize(DEFAUTE_SIZE);
        }
        Integer size = esSuggestBO.getSize();
        String keyword = esSuggestBO.getKeyword();

        PoetSuggestPageMO poetSuggestPageMO = new PoetSuggestPageMO();
        poetSuggestPageMO.setSize(size);
        if (StringUtils.hasText(esSuggestBO.getKeyword())) {
            poetSuggestPageMO.setHaveKeyword(true);
            keyword = QueryParser.escape(keyword);
            esSuggestBO.setKeyword(keyword);
        } else {
            poetSuggestPageMO.setHaveKeyword(false);
            return poetSuggestPageMO;
        }
        poetSuggestPageMO.setKeyword(keyword);

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
     * ????????????
     *
     * @param hit
     * @param poetInfoBO
     */
    private void dealHighLight(JSONObject hit, PoetInfoBO poetInfoBO) {
        JSONObject highlight = hit.getJSONObject("highlight");
        if (highlight != null) {//  //????????????
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
                Map<String, String> targetKeys = new LinkedHashMap<String, String>();
                for (int j = 0; j < paragraphs.size(); j++) {
                    String highlightParagraph = paragraphs.getString(j);
                    String key = highlightParagraph.replaceAll("<em>|</em>", "");
                    targetKeys.put(key, highlightParagraph);
                }
                for (String sourceParagraph : sourceParagraphs) {
                    String targetVal = sourceParagraph;
                    for (Map.Entry<String, String> entry : targetKeys.entrySet()) {
                        int index = sourceParagraph.indexOf(entry.getKey());
                        if (index > -1) {//????????????
                            targetVal = sourceParagraph.substring(0, index) + entry.getValue() + sourceParagraph.substring(index + entry.getKey().length());
                            break;
                        }
                    }
                    targetParagraphs.add(targetVal);
                }
                poetInfoBO.setParagraphs(targetParagraphs);
            }
        }
    }


    private final String SPEC_PROP_KEY_VAL = "\\{\\s*(?<key>[\\S]+)\\s+(?<val>[\\S]+)\\s*}";

    private void igornSpecPropKeyVal(EsSuggestBO esSuggestBO) {
        String keyword = esSuggestBO.getKeyword();
        if (!StringUtils.hasText(keyword)) {
            return;
        }
        esSuggestBO.setKeyword(keyword.replaceAll(SPEC_PROP_KEY_VAL, ""));
    }

    /**
     * String source = "{ ?????????    ???????????????}{?????????    ???????????????}";
     * ???key???????????????????????????
     *
     * @param esSearchBO
     * @param poetSearchPageMO
     */
    private void dealSpecPropKeyVal(EsSearchBO esSearchBO) {
        String key = esSearchBO.getKey();
        if (!StringUtils.hasText(key)) {
            return;
        }
        //???{ key  val}   ??????????????? ??? ?????? ???????????????key???  ???????????? (val)???????????????  ????????????????????????
        String regex = "\\{\\s*(?<key>[\\S]+)\\s+(?<val>.*?)\\s*}";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(key);
        Map<String, List<String>> specMap = new HashMap<String, List<String>>();
        while (matcher.find()) {
            String groupKey = matcher.group("key");
            String groupVal = matcher.group("val");
            List<String> groupVals = null;
            if (specMap.containsKey(groupKey)) {
                groupVals = specMap.get(groupKey);
            } else {
                groupVals = new ArrayList<String>();
            }
            if (groupVals.contains(groupVal)) {
                continue;
            }
            groupVals.add(groupVal);
            specMap.put(groupKey, groupVals);
        }
        if (CollectionUtils.isEmpty(specMap)) {
            return;
        }
        key = key.replaceAll(regex, "");
        esSearchBO.setKey(key);
        //?????????????????????????????? ??????
        List<EsPropBO> specPropBOs = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : specMap.entrySet()) {
            EsPropBO esPropBO = new EsPropBO();
            esPropBO.setPropKey(entry.getKey());
            esPropBO.setPropVals(entry.getValue());
            specPropBOs.add(esPropBO);
        }
        log.info("dealSpecProkeyVal-->specPropBOs={}", JSON.toJSONString(specPropBOs));
        List<EsPropBO> props = esSearchBO.getProps();
        if (CollectionUtils.isEmpty(props)) {

            esSearchBO.setProps(specPropBOs);
        } else {//?????????????????? ????????????
            for (EsPropBO prop : props) {//????????????
                String propKey = prop.getPropKey();
                for (EsPropBO specPropBO : specPropBOs) {//???????????????????????????
                    if (!propKey.equals(specPropBO)) {
                        continue;
                    }

                    List<String> propVals = prop.getPropVals();
                    for (String propVal : specPropBO.getPropVals()) {
                        if (!propVals.contains(propVal)) {//??????????????????
                            propVals.add(propVal);
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * ??????????????? ?????????????????????????????????key val??????;
     *
     * @param poetAggsBOs
     * @param esSearchBO
     */
    private List<PoetAggsBO> combineProp(List<PoetAggsBO> poetAggsBOs, EsSearchBO esSearchBO) {
        if (poetAggsBOs == null) {
            poetAggsBOs = new ArrayList<PoetAggsBO>();
        }
        List<EsPropBO> props = esSearchBO.getProps();
        if (CollectionUtils.isEmpty(props)) {
            return poetAggsBOs;
        }
        List<PoetAggsBO> combineAggsBOs = new ArrayList<PoetAggsBO>();
        if (CollectionUtils.isEmpty(poetAggsBOs)) {
            for (EsPropBO prop : esSearchBO.getProps()) {
                PoetAggsBO poetAggsBO = new PoetAggsBO();
                poetAggsBO.setKey(prop.getPropKey());
                poetAggsBO.setVals(prop.getPropVals());
                poetAggsBO.setChoosePreSize(prop.getPropVals().size());
                combineAggsBOs.add(poetAggsBO);
            }
            return combineAggsBOs;
        }

        List<PoetAggsBO> noChooseAggsBOs = new ArrayList<PoetAggsBO>();

        for (PoetAggsBO poetAggsBO : poetAggsBOs) {//?????????????????????????????????
            String key = poetAggsBO.getKey();
            boolean isChooseKey = false;
            for (EsPropBO prop : props) {//??????????????????
                String chooseKey = prop.getPropKey();
                if (!key.equals(chooseKey)) {
                    continue;
                }
                isChooseKey = true;
                List<String> combineVals = new ArrayList<String>();
                List<String> chooseVals = prop.getPropVals();
                combineVals.addAll(chooseVals);
                List<String> vals = poetAggsBO.getVals();
                for (String val : vals) {
                    if (combineVals.contains(val)) {
                        continue;
                    }
                    combineVals.add(val);
                }
                poetAggsBO.setVals(combineVals);
                poetAggsBO.setChoosePreSize(chooseVals.size());
                break;
            }
            if (isChooseKey) {
                combineAggsBOs.add(poetAggsBO);
            } else {
                noChooseAggsBOs.add(poetAggsBO);
            }
        }
        //??????????????????
        for (PoetAggsBO noChooseAggsBO : noChooseAggsBOs) {
            combineAggsBOs.add(noChooseAggsBO);
        }
        return combineAggsBOs;

    }

    private String getKeyWord(PoetSearchPageMO poetSearchPageMO) {
        //???????????????????????????
        Pattern compile = Pattern.compile("???([^???]+)???");

        Matcher matcher = compile.matcher(poetSearchPageMO.getKey());
        List<String> cleanTitleSpaceStrs = new ArrayList<String>();
        StringBuffer sbf = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(sbf, " " + group.replaceAll("\\s", ""));
        }
        matcher.appendTail(sbf);
        //??????????????????   ?????????????????????????????? ???????????? \ \
        String regex = "([a-zA-Z]+)(\\s+)([^\\s]+)";
        String nextStr = sbf.toString().replaceAll(regex, "$1\\\\$2\\\\$3");
        //????????? ?????????????????????????????????_ ?????? \ ???????????????  ????????????
//      return nextStr.replaceAll("[^\\u4e00-\\u9fa5_a-zA-Z0-9\\s\\\\]+", "");
        return nextStr;
    }


    private String aggsAgain(PoetSearchPageMO poetSearchPageMO, List<String> aggsPropKeys, int size) {
        List<PoetAggsInfoMO> aggsInfos = new ArrayList<PoetAggsInfoMO>();
        for (int i = 0; i < aggsPropKeys.size() && i < MAX_NUM; i++) {
            String aggsPropKey = aggsPropKeys.get(i);
            PoetAggsInfoMO poetAggsInfoMO2 = new PoetAggsInfoMO();
            poetAggsInfoMO2.setAggsName(String.format(AGGS_NAME_META, i, aggsPropKey));
            poetAggsInfoMO2.setField(String.format("properties.%s.keyword", aggsPropKey));
            poetAggsInfoMO2.setSize(size);
            aggsInfos.add(poetAggsInfoMO2);
        }
        aggsInfos.get(aggsInfos.size() - 1).setEnd(PoetCharConstant.CHAR_EMPTY);
        poetSearchPageMO.setAggsInfos(aggsInfos);
        //log.info("aggs-->poetSearchPageMO={}", JSON.toJSONString(poetSearchPageMO));
        return poetEsSearchTempService.searchByTemp(PoetIndexConstant.POET_INFO, poetSearchPageMO, PoetSearchTempConstant.POET_SEARCH_PAGE);
    }

    /**
     * key??????1
     *
     * @return
     */
    private String toLowCaseAndKeepChineseFirstAbc(String source) {
        if (source == null) {
            return null;
        }
        //?????????
        source = source.toLowerCase();
        //???????????????????????????????????????
        source = source.replaceAll("([a-z])[a-z]+", "$1");
        //????????????a-z???????????????
        source = source.replaceAll("[^\u4e00-\u9fa5a-z]+", "");
        return source;//??????
    }

    /**
     * key??????1
     *
     * @return
     */
    private String toLowCaseAndKeepChineseAbc(String source) {
        if (source == null) {
            return null;
        }
        //?????????
        source = source.toLowerCase();
        //????????????a-z???????????????
        source = source.replaceAll("[^\u4e00-\u9fa5a-z]+", "");
        return source;//??????
    }

    /**
     * ?????????????????????????????? ???????????? ?????????????????????key ?????????
     *
     * @param aggsPropKeys
     * @param poetSearchPageMO
     */
    private List<String> combinedProps(List<String> aggsPropKeys, PoetSearchPageMO poetSearchPageMO) {
        if (CollectionUtils.isEmpty(aggsPropKeys)) {
            return aggsPropKeys;
        }
        List<EsPropBO> props = poetSearchPageMO.getProps();
        if (CollectionUtils.isEmpty(props)) {
            return aggsPropKeys;
        }
        List<String> combinedProps = new ArrayList<String>();
        for (EsPropBO prop : props) {
            combinedProps.add(prop.getPropKey());
        }
        for (String aggsPropKey : aggsPropKeys) {
            if (!combinedProps.contains(aggsPropKey)) {
                combinedProps.add(aggsPropKey);
            }
        }
        return combinedProps;
    }

    /**
     * ???????????? propBos
     * @param checkProps
     * @return
     */
    /*private List<EsPropBO> cloneAndEscapeProps(List<EsPropBO> checkProps) {
        List<EsPropBO> propMos  = new ArrayList<EsPropBO>();
        for (EsPropBO checkProp : checkProps) {
            EsPropBO esPropBO = new EsPropBO();
            esPropBO.setEnd(checkProp.getEnd());
            esPropBO.setPropKey(QueryParser.escape(checkProp.getPropKey()));
            List<String> dealVals = new ArrayList<String>();
            for (String val : checkProp.getPropVals()) {
                dealVals.add(QueryParser.escape(val));
            }
            esPropBO.setPropVals(dealVals);
            propMos.add(esPropBO);
        }
        return propMos;
    }*/
}
