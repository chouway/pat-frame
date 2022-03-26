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
public class PoetAggsValBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可筛选的属性Vals
     */
    private String aggsVal;

    /**
     * 全拼
     */
    private String fullPY;

    /**
     * 首拼
     */
    private String firstPY;
}
