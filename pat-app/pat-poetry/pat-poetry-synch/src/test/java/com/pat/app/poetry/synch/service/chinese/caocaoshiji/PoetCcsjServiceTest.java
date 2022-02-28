package com.pat.app.poetry.synch.service.chinese.caocaoshiji;

import com.pat.app.poetry.synch.PoetSynchTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PoetCcsjServiceTest
 *
 * @author chouway
 * @date 2022.02.28
 */
public class PoetCcsjServiceTest extends PoetSynchTest {

    @Autowired
    private PoetCcsjService poetCcsjService;

    @Test
    public void synchData(){
        poetCcsjService.synchData();
    }

}