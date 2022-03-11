package com.pat.app.poetry.synch.service.es;

import com.pat.app.poetry.synch.PoetSynchTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PoetEsSuggestServiceTest
 *
 * @author chouway
 * @date 2022.03.11
 */
public class PoetEsSuggestServiceTest extends PoetSynchTest {

    @Autowired
    private PoetEsSuggestService poetEsSuggestService;

    @Test
    void initPoetSuggest() {
        poetEsSuggestService.initPoetSuggest();
    }


    @Test
    public void synchPoetSuggests(){
        poetEsSuggestService.synchPoetSuggests();
    }
}