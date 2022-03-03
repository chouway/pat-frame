package com.pat.api.service.mo;

import com.pat.api.constant.PoetSearchTempConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * PoetAggsInfoMO
 *
 * @author chouway
 * @date 2022.03.03
 */
@Data
public class PoetAggsInfoMO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 聚合自定义名称
     */
    private String aggsName;

    /**
     * 目标字段
     */
    private String field;

    /**
     * 最多显示几条
     */
    private Integer size;


    private String end = PoetSearchTempConstant.CHAR_COMMA;

}
