package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PoetSearchResultBO
 *
 * @author chouway
 * @date 2022.03.14
 */
@Data
public class PoetSearchResultBO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    private long total;

    /**
     * 一页大小
     */
    private long pageSize;

    /**
     * 页码 1, 2, 3, 等
     */
    private long pageNum;

    /**
     * 属性key
     */
    private List<String> propKeys;

    /**
     * 统计key value
     */
    private List<PoetAggsBO> poetAggsBO;
    
}
