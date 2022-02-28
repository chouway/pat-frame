package com.pat.app.poetry.synch.service.chinese;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

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

    /**
     * 同步数据
     */
   public abstract void synchData();

    /**
     *
     * @param jsonObject
     */
   public abstract void synchSingle(JSONObject jsonObject);
}
