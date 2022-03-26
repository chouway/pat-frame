package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PoetAggsKeyBO
 * 获取筛选 keys
 * @author chouway
 * @date 2022.03.25
 */
@Data
public class PoetAggsKeyBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可筛选的属性key
     */
    private String aggsKey;

    /**
     * 全拼
     */
    private String fullPY;

    /**
     * 首拼
     */
    private String firstPY;
}
