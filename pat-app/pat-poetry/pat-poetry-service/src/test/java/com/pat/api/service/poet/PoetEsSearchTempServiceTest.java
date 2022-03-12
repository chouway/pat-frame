package com.pat.api.service.poet;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSON;
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
import java.util.List;

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
        String keyword  = "ren";
        PoetSuggestPageMO poetSuggestPageMO = this.getPoetSuggestPageMO(keyword);
        String result = poetEsSearchTempService.searchByTemp(indexName, poetSuggestPageMO, tempId);
        log.info("suggestByTemp-->result={}", result);
    }

    @Test
    public void renderSearchTempLocalSuggest(){
        String tempId = PoetSearchTempConstant.POET_SUGGEST_PAGE;
        String keyword  = "ren";
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