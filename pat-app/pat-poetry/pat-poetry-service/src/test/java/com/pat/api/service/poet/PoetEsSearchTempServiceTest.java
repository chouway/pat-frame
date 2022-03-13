package com.pat.api.service.poet;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pat.api.constant.PoetIndexConstant;
import com.pat.api.constant.PoetSearchTempConstant;
import com.pat.api.service.PoetServiceTest;
import com.pat.api.service.mo.PoetSearchPageMO;
import com.pat.api.service.mo.PoetSuggestInfoMO;
import com.pat.api.service.mo.PoetSuggestPageMO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PoetEsSearchTempServiceTest
 *
 * @author chouway
 * @date 2022.03.03
 */
public class PoetEsSearchTempServiceTest extends PoetServiceTest {

    @Autowired
    private PoetEsSearchTempService poetEsSearchTempService;

    @Test
    void pushSearchTemp2Es() {
        poetEsSearchTempService.pushSearchTemp2Es();
    }

    @Test
    public void getEsSearchTemps(){
        String esSearchTemps = poetEsSearchTempService.getEsSearchTemps();
        log.info("getEsSearchTemps-->esSearchTemps={}", esSearchTemps);
    }

    @Test
    void renderSearchTemp() {
        String tempId = PoetSearchTempConstant.POET_SEARCH_PAGE;
        PoetSearchPageMO poetSearchPageMO = getPoetSearchPageMO();
        String render = poetEsSearchTempService.renderSearchTemp(tempId, poetSearchPageMO);
        log.info("renderSearchTemp-->render={}", render);
    }

    @Test
    public void renderSearchTempLocalSearch(){
        String tempId = PoetSearchTempConstant.POET_SEARCH_PAGE;
        PoetSearchPageMO poetSearchPageMO = getPoetSearchPageMO();
        String render = poetEsSearchTempService.renderSearchTempLocal(tempId, poetSearchPageMO);
        log.info("renderSearchTempLocal-->render={}", render);

    }

    @Test
    public void searchByTemp(){
        String indexName = PoetIndexConstant.POET_INFO;
        PoetSearchPageMO poetSearchPageMO = getPoetSearchPageMO();
        String tempId = PoetSearchTempConstant.POET_SEARCH_PAGE;
        log.info("searchByTemp-->indexName={},tempId={}", indexName,tempId);
        log.info("searchByTemp-->poetSearchPageMO={}", JSON.toJSONString(poetSearchPageMO));
        String result = poetEsSearchTempService.searchByTemp(indexName, poetSearchPageMO,tempId);
        log.info("searchByTemp-->result={}", result);

    }

    @Test
    public void suggestByTemp(){
        String indexName = PoetIndexConstant.POET_SUGGEST;
        String tempId = PoetSearchTempConstant.POET_SUGGEST_PAGE;
        String keyword  = "人";
        PoetSuggestPageMO poetSuggestPageMO = this.getPoetSuggestPageMO(keyword);
        String result = poetEsSearchTempService.searchByTemp(indexName, poetSuggestPageMO, tempId);
        log.info("suggestByTemp-->result={}", result);
    }

    @Test
    public void resolveSuggestResult(){
        String keyword = "人";
        String source = "{\"took\":6,\"timed_out\":false,\"_shards\":{\"total\":2,\"successful\":2,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":{\"value\":13,\"relation\":\"eq\"},\"max_score\":null,\"hits\":[{\"_index\":\"poet-suggest_v0\",\"_id\":\"126\",\"_score\":1.0,\"_source\":{\"suggestText\":\"天地间，人为贵。\"},\"sort\":[1.0,0,\"03\",1]},{\"_index\":\"poet-suggest_v0\",\"_id\":\"142\",\"_score\":1.0,\"_source\":{\"suggestText\":\"对酒当歌，人生几何！譬如朝露，去日苦多。\"},\"sort\":[1.0,0,\"03\",17]},{\"_index\":\"poet-suggest_v0\",\"_id\":\"166\",\"_score\":1.0,\"_source\":{\"suggestText\":\"卢弓矢千，虎贲三百人。\"},\"sort\":[1.0,0,\"03\",41]},{\"_index\":\"poet-suggest_v0\",\"_id\":\"182\",\"_score\":1.0,\"_source\":{\"suggestText\":\"人耄耋，皆得以寿终。\"},\"sort\":[1.0,0,\"03\",57]},{\"_index\":\"poet-suggest_v0\",\"_id\":\"201\",\"_score\":1.0,\"_source\":{\"suggestText\":\"势利使人争，嗣还自相戕。\"},\"sort\":[1.0,0,\"03\",76]},{\"_index\":\"poet-suggest_v0\",\"_id\":\"205\",\"_score\":1.0,\"_source\":{\"suggestText\":\"生民百遗一，念之断人肠。\"},\"sort\":[1.0,0,\"03\",80]},{\"_index\":\"poet-suggest_v0\",\"_id\":\"222\",\"_score\":1.0,\"_source\":{\"suggestText\":\"溪谷少人民，雪落何霏霏！\"},\"sort\":[1.0,0,\"03\",97]},{\"_index\":\"poet-suggest_v0\",\"_id\":\"227\",\"_score\":1.0,\"_source\":{\"suggestText\":\"行行日已远，人马同时饥。\"},\"sort\":[1.0,0,\"03\",102]},{\"_index\":\"poet-suggest_v0\",\"_id\":\"232\",\"_score\":1.0,\"_source\":{\"suggestText\":\"仙人欲来，出随风，列之雨。\"},\"sort\":[1.0,0,\"03\",107]},{\"_index\":\"poet-suggest_v0\",\"_id\":\"306\",\"_score\":1.0,\"_source\":{\"suggestText\":\"绝人事，游浑元，若疾风游欻翩翩。\"},\"sort\":[1.0,0,\"03\",181]}]},\"suggest\":{\"fullSuggest\":[{\"text\":\"ren\",\"offset\":0,\"length\":3,\"options\":[{\"text\":\"人耄耋，皆得以寿终。\",\"_index\":\"poet-suggest_v0\",\"_id\":\"182\",\"_score\":1.0,\"_source\":{\"suggestText\":\"人耄耋，皆得以寿终。\"}},{\"text\":\"仁义为名，礼乐为荣。\",\"_index\":\"poet-suggest_v0\",\"_id\":\"347\",\"_score\":1.0,\"_source\":{\"suggestText\":\"仁义为名，礼乐为荣。\"}}]}],\"ikPreSuggest\":[{\"text\":\"人\",\"offset\":0,\"length\":1,\"options\":[{\"text\":\"人耄耋，皆得以寿终。\",\"_index\":\"poet-suggest_v0\",\"_id\":\"182\",\"_score\":1.0,\"_source\":{\"suggestText\":\"人耄耋，皆得以寿终。\"}}]}],\"prefixSuggest\":[{\"text\":\"r\",\"offset\":0,\"length\":1,\"options\":[{\"text\":\"人耄耋，皆得以寿终。\",\"_index\":\"poet-suggest_v0\",\"_id\":\"182\",\"_score\":1.0,\"_source\":{\"suggestText\":\"人耄耋，皆得以寿终。\"}},{\"text\":\"仁义为名，礼乐为荣。\",\"_index\":\"poet-suggest_v0\",\"_id\":\"347\",\"_score\":1.0,\"_source\":{\"suggestText\":\"仁义为名，礼乐为荣。\"}},{\"text\":\"冉冉老将至，何时返故乡？\",\"_index\":\"poet-suggest_v0\",\"_id\":\"279\",\"_score\":1.0,\"_source\":{\"suggestText\":\"冉冉老将至，何时返故乡？\"}},{\"text\":\"戎马不解鞍，铠甲不离傍。\",\"_index\":\"poet-suggest_v0\",\"_id\":\"278\",\"_score\":1.0,\"_source\":{\"suggestText\":\"戎马不解鞍，铠甲不离傍。\"}},{\"text\":\"日月之行，若出其中；\",\"_index\":\"poet-suggest_v0\",\"_id\":\"188\",\"_score\":1.0,\"_source\":{\"suggestText\":\"日月之行，若出其中；\"}},{\"text\":\"让国不用，饿殂首山。\",\"_index\":\"poet-suggest_v0\",\"_id\":\"366\",\"_score\":1.0,\"_source\":{\"suggestText\":\"让国不用，饿殂首山。\"}}]}]}}";
        JSONObject jsonObject = JSON.parseObject(source);
        JSONArray fullSuggests =(JSONArray) JSONPath.eval(jsonObject, "/suggest/fullSuggest/options");
        JSONArray prefixSuggests =(JSONArray) JSONPath.eval(jsonObject, "/suggest/prefixSuggest/options");
        JSONArray ikPreSuggests =(JSONArray) JSONPath.eval(jsonObject, "/suggest/ikPreSuggest/options");
        JSONArray hits =(JSONArray) JSONPath.eval(jsonObject, "/hits/hits");
        log.info("resolveSuggestResult-->fullSuggests={}", fullSuggests);
        log.info("resolveSuggestResult-->prefixSuggests={}", prefixSuggests);
        log.info("resolveSuggestResult-->ikPreSuggests={}", ikPreSuggests);
        log.info("resolveSuggestResult-->hits={}", hits);
        Map<Long,String> result = new LinkedHashMap<Long,String>();
        if("\\w+".matches(keyword)){//纯字母数字
            this.putSuggests(ikPreSuggests, result);
            this.putSuggests(fullSuggests, result);
            this.putSuggests(prefixSuggests, result);
            this.putSuggests(hits, result);
        }else{
            this.putSuggests(ikPreSuggests, result);
            this.putSuggests(hits, result);
            this.putSuggests(fullSuggests, result);
            this.putSuggests(prefixSuggests, result);
        }

        log.info("resolveSuggestResult-->result={}", JSON.toJSONString(result, SerializerFeature.MapSortField));

    }

    private void putSuggests(JSONArray jsonArray, Map<Long, String> result) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Long id = jsonObj.getLong("_id");
            if(result.containsKey(id)){
                continue;
            }
            result.put(id,jsonObj.getJSONObject("_source").getString("suggestText"));
        }
    }

    @Test
    public void renderSearchTempLocalSuggest(){
        String tempId = PoetSearchTempConstant.POET_SUGGEST_PAGE;
        String keyword  = "人";
        PoetSuggestPageMO poetSuggestPageMO = this.getPoetSuggestPageMO(keyword);
        String result = poetEsSearchTempService.renderSearchTempLocal(tempId, poetSuggestPageMO);
        log.info("renderSearchTempLocalSuggest-->result={}", result);
    }

    private PoetSuggestPageMO getPoetSuggestPageMO(String keyword){
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        int size = 10;
        PoetSuggestPageMO poetSuggestPageMO = new PoetSuggestPageMO();
        poetSuggestPageMO.setSize(size);
        poetSuggestPageMO.setKeyword(keyword);

        List<PoetSuggestInfoMO> suggestInfoMOs  = new ArrayList<PoetSuggestInfoMO>();

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
        fullSuggest.setKeyword(PinyinUtil.getPinyin(keyword, " "));
        suggestInfoMOs.add(fullSuggest);

        PoetSuggestInfoMO prefixSuggest = new PoetSuggestInfoMO();
        prefixSuggest.setSuggestName("prefixSuggest");
        prefixSuggest.setField("prefixPinyin");
        prefixSuggest.setSize(size);
        prefixSuggest.setKeyword(PinyinUtil.getFirstLetter(keyword, " "));
        prefixSuggest.setEnd(PoetSearchTempConstant.CHAR_EMPTY);
        suggestInfoMOs.add(prefixSuggest);

        poetSuggestPageMO.setSuggestInfos(suggestInfoMOs);
        log.info("getPoetSuggestPageMO-->poetSuggestPageMO={}", JSON.toJSONString(poetSuggestPageMO));
        return poetSuggestPageMO;



    }

    private PoetSearchPageMO getPoetSearchPageMO() {
        PoetSearchPageMO poetSearchPageMO = new PoetSearchPageMO();
        poetSearchPageMO.setFrom(0);
        poetSearchPageMO.setSize(10);
        poetSearchPageMO.setHasKey(true);
        poetSearchPageMO.setKey("关山");
        return poetSearchPageMO;
    }


}