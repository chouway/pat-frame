package com.pat.app.poetry.synch.service.es;

import com.pat.app.poetry.synch.PoetSynchTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PoetEsAliasServiceTest
 *
 * @author chouway
 * @date 2022.03.10
 */
public class PoetEsAliasServiceTest extends PoetSynchTest {

    @Autowired
    private PoetEsAliasService poetEsAliasService;

    @Test
    public void alias_PoetInfoEO(){
        poetEsAliasService.addAlias("poet-info_v0","poet-info");
    }

    @Test
    public void alias_PoetSuggestEO(){
        poetEsAliasService.addAlias("poet-suggest_v0","poet-suggest");
    }

}