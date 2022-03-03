package com.pat.api.bo;

import com.pat.api.constant.PoetSearchTempConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * EsPropBO
 *
 * @author chouway
 * @date 2022.03.03
 */
@Data
public class EsPropBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String propKey;

    private List<String> propVals;

    //前台可不处理 由模版服务处理的
    private String end = PoetSearchTempConstant.CHAR_COMMA;
}
