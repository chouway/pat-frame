package com.pat.api.service.poet;

import com.alibaba.fastjson.JSON;
import com.pat.api.constant.PoetIndexConstant;
import com.pat.api.constant.PoetSearchTempConstant;
import com.pat.api.service.PoetServiceTest;
import com.pat.api.service.mo.PoetSearchPageMO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void renderSearchTempLocal(){
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
        String result = poetEsSearchTempService.searchByTemp(indexName, poetSearchPageMO,tempId );
        log.info("searchByTemp-->result={}", result);

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