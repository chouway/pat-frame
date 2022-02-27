package com.pat.app.pcp.synch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * PoetAbstractService
 *
 * @author chouway
 * @date 2022.02.25
 */
@Slf4j
public abstract class PoetAbstractService {

    @Value("${app.chinese-poetry.local-dir}")
    protected String LOCAL_DIR;

//    public abstract Patr synchData();
}
