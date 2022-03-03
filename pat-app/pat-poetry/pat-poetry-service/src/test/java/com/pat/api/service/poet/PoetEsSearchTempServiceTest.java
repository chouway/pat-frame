package com.pat.api.service.poet;

import com.alibaba.fastjson.JSON;
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
    void renderSearchTemp() {
    }

    @Test
    public void renderSearchTempLocal(){
        String tempId = PoetSearchTempConstant.POET_SEARCH_PAGE;
        PoetSearchPageMO poetSearchPageMO = new PoetSearchPageMO();
        poetSearchPageMO.setFrom(0);
        poetSearchPageMO.setSize(10);
        poetSearchPageMO.setHasKey(true);
        poetSearchPageMO.setKey("abc");
        String render = poetEsSearchTempService.renderSearchTempLocal(tempId, poetSearchPageMO);
        log.info("renderSearchTempLocal-->render={}", render);

    }

    @Test
    void searchByTemp() {
    }
}