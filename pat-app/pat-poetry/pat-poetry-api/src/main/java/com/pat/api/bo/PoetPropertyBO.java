package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * PoetPropertyBO
 *
 * @author chouway
 * @date 2022.03.14
 */
@Data
public class PoetPropertyBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    private String value;

}
