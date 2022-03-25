package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PoetAggsKeysBO
 * 获取筛选 keys
 * @author chouway
 * @date 2022.03.25
 */
@Data
public class PoetAggsKeysBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可筛选的属性key
     */
    private List<String> aggsKeys;

    /**
     * 全拼
     */
    private List<String> fullPY;

    /**
     * 首拼
     */
    private List<String> firstPY;
}
