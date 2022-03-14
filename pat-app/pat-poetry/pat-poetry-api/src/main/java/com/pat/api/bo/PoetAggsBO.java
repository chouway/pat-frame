package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PoetAggsBO
 *
 * @author chouway
 * @date 2022.03.14
 */
@Data
public class PoetAggsBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    private List<String> vals;

    /**
     * val选中前面几个了 ，选中排前面
     */
    private int choosePreSize = 0;

}
