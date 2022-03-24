package com.pat.app.poetry.synch.service.chinese.chuci;

import com.pat.app.poetry.synch.PoetSynchTest;
import com.pat.app.poetry.synch.service.chinese.PoetAbstractService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PoetChuciServiceTest
 *
 * @author chouway
 * @date 2022.03.24
 */
public class PoetChuciServiceTest  extends PoetSynchTest {

    @Autowired
    private PoetChuciService poetChuciService;

    @Test
    void synchInfo() {
        poetChuciService.synchData();
    }
}