package com.pat.app.poetry.synch.service.chinese;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * PoetSetService
 *
 * @author chouway
 * @date 2022.02.25
 */
@Service
public class PoetSetService extends PoetAbstractService{


    @Override
    public void synchData() {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchSingle(JSONObject jsonObject) {

    }
}
