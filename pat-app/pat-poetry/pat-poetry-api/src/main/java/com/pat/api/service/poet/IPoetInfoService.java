package com.pat.api.service.poet;

import com.pat.api.bo.PoetBaikeBO;
import com.pat.api.bo.PoetInfoBO;

import java.util.List;

/**
 * IPoetInfoService
 *
 * @author chouway
 * @date 2022.03.14
 */
public interface IPoetInfoService {
    /**
     * 获取 文BO
     * @param id
     * @return
     */
    PoetInfoBO getInfoById(Long id);

    PoetBaikeBO getBaikeById(Long infoId);
}
