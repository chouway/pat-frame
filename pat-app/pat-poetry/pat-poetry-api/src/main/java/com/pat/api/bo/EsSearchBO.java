package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * EsPropBO
 *
 * @author chouway
 * @date 2022.03.03
 */
@Data
public class EsSearchBO implements Serializable {

    private static final long serialVersionUID = 1L;



    /**
     * 关键字
     */
    private String key;

    /**
     * 选择的属性
     */
    private List<EsPropBO> props;

    /**
     * 偏移位置  默认从0
     */
    private Integer from;

    /**
     * 获取的数量
     */
    private Integer size;

    /**
     * 页码  1 ， 2， 3，4，
     */
    private Integer pageNum;

    /**
     * 聚合的属性Key
     */
    private List<String> aggsPropKeys;

    /**
     * 是否高亮
     */
    private boolean highlight;
    /**
     * 获取偏移量
     * from 有预设直接返回 ，当 上送的的页数及页码 则计算获取偏移量
     * @return
     */
    public Integer getFrom() {
        if (from != null) {
            return from;
        }
        if (size != null) {
            if (pageNum == null) {
                this.pageNum = 1;
            }
            this.from = this.size * (pageNum - 1);
            return this.from;
        }
        return from;
    }


}
