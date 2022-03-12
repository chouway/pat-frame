package com.pat.api.service.mo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PoetSuggestPageMO
 *
 * @author chouway
 * @date 2022.03.12
 */
@Data
public class PoetSuggestPageMO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer size;

    /**
     * search as type search
     */
    private String keyword;

    /**
     * completion
     */
    private List<PoetSuggestInfoMO> suggestInfos;

}
