package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PoetCustomAggsKeyBO
 * 自定义筛选 key
 * @author chouway
 * @date 2022.03.25
 */
@Data
public class PoetCustomAggsKeyBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可筛选的属性key
     */
    private List<String> aggsKey;

    /**
     * 全拼
     */
    private List<String> fullPY;

    /**
     * 首拼
     */
    private List<String> firstPY;
}
