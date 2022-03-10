package com.pat.app.poetry.synch.service.suggest;

import com.pat.api.entity.PoetAuthor;
import com.pat.api.mapper.PoetAuthorMapper;
import com.pat.api.mapper.PoetContentMapper;
import com.pat.api.mapper.PoetInfoMapper;
import com.pat.app.poetry.synch.repo.PoetSuggestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PoetSuggestService
 *
 * @author chouway
 * @date 2022.03.10
 */
@Slf4j
@Service
public class PoetEsSuggestService {

    @Autowired
    private PoetSuggestRepository poetSuggestRepository;

    @Autowired
    private PoetAuthorMapper poetAuthorMapper;

    @Autowired
    private PoetInfoMapper poetInfoMapper;

    @Autowired
    private PoetContentMapper poetContentMapper;

    /**
     * 同步建议
     */
    public void synchPoetSuggest(){

    }

    private void synchSuggestAuthor(){

    }

    private void synchSuggestInfo(){

    }

    private void synchSuggestContent(){

    }
}
