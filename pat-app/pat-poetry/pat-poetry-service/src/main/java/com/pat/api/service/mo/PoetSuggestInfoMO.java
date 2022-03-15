package com.pat.api.service.mo;

import com.pat.api.constant.PoetCharConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * PoetSuggestInfoMO
 *
 * @author chouway
 * @date 2022.03.12
 */
@Data
public class PoetSuggestInfoMO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String suggestName;

    private String keyword;

    private String field;

    private Integer size;

    private String end = PoetCharConstant.CHAR_COMMA;
}
