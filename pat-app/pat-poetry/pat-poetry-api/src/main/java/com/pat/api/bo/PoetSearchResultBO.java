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
    private Integer total;

    /**
     * 一页大小
     */
    private Integer pageSize;

    /**
     * 页码 1, 2, 3, 等
     */
    private Integer pageNum;

    /**
     * 统计key value
     */
    private List<PoetAggsBO> poetAggsBO;

    /**
     * 诗信息
     */
    private List<PoetInfoBO>  poetInfoBOs;
    
}
