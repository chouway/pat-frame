package com.pat.app.poetry.synch.repo;

import com.pat.app.poetry.synch.PoetSynchTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PoetSuggestRepositoryTest
 *
 * @author chouway
 * @date 2022.03.09
 */
public class PoetSuggestRepositoryTest extends PoetSynchTest{

    @Autowired
    private PoetSuggestRepository suggestRepository;

    @Test
    public void count(){
        long count = suggestRepository.count();
        log.info("count-->count={}", count);
    }
}