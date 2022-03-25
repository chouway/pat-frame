package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PoetAggsKeyValsBO
 * 获取筛选属性 vals
 * @author chouway
 * @date 2022.03.25
 */
@Data
public class PoetAggsKeyValsBO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 筛选的key
     */
    private String aggsKey;

    /**
     * 可筛选的属性Vals
     */
    private List<String> aggsVals;

    /**
     * 全拼
     */
    private List<String> fullPY;

    /**
     * 首拼
     */
    private List<String> firstPY;
}
