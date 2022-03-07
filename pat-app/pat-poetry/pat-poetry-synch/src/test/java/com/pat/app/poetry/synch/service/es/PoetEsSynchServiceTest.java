package com.pat.app.poetry.synch.service.es;

import com.pat.api.constant.EsConstant;
import com.pat.app.poetry.synch.PoetSynchTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PoetEsSynchServiceTest
 *
 * @author chouway
 * @date 2022.03.07
 */
public class PoetEsSynchServiceTest extends PoetSynchTest {

    @Autowired
    private PoetEsSynchService poetEsSynchService;

    @Test
    void synchPoetEs() {
    }

    @Test
    void saveEsStatus() {
    }

    @Test
    void participle() {
        Vector<String> participle = poetEsSynchService.participle("度关山(曹操) - 百度百科", EsConstant.ANALYZER_IK_SMART);
        log.info("participle-->participle={}", participle);

    }
}