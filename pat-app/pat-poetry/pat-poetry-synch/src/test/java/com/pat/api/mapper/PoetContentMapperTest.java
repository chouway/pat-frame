package com.pat.api.mapper;

import com.pat.api.entity.PoetContent;
import com.pat.api.mapper.PoetContentMapper;
import com.pat.app.poetry.synch.PoetSynchTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PoetContentMapperTest
 *
 * @author chouway
 * @date 2022.03.01
 */
public class PoetContentMapperTest extends PoetSynchTest {

    @Autowired
    private PoetContentMapper poetContentMapper;

    @Test
    public void getContent(){
        Long infoId  =  1L;
        String content = poetContentMapper.getContent(infoId);
        log.info("getContent-->infoId={},content={}", infoId,content);
    }
}
