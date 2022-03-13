package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * EsSuggestBO
 *
 * @author chouway
 * @date 2022.03.12
 */
@Data
public class EsSuggestBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyword;

    private Integer size;
}
