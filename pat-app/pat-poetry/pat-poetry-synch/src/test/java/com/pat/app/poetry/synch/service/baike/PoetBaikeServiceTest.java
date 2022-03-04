package com.pat.app.poetry.synch.service.baike;

import com.pat.app.poetry.synch.PoetSynchTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PatBaikeServiceTest
 *
 * @author chouway
 * @date 2022.03.03
 */
public class PoetBaikeServiceTest extends PoetSynchTest {

    @Autowired
    private PoetBaikeService poetBaikeService;

    @Test
    public void synchBaiduBaikeProps() {
        Long infoId = 3L;
        poetBaikeService.synchBaiduBaikeProps(infoId);
    }
}