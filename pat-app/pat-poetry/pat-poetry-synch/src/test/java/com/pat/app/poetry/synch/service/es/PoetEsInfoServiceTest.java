package com.pat.app.poetry.synch.service.es;

import com.pat.api.constant.EsConstant;
import com.pat.app.poetry.synch.PoetSynchTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Vector;

/**
 * PoetEsInfoServiceTest
 *
 * @author chouway
 * @date 2022.03.07
 */
public class PoetEsInfoServiceTest extends PoetSynchTest {

    @Autowired
    private PoetEsInfoService poetEsInfoService;

    @Test
    void synchPoetEs() {
        poetEsInfoService.synchPoetEs();
    }

    @Test
    void participle() {
        Vector<String> participle = poetEsInfoService.participle("度关山(曹操) - 百度百科", EsConstant.ANALYZER_IK_SMART);
        log.info("participle-->participle={}", participle);

    }
}