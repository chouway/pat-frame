package com.pat.app.poetry.synch.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * SearchParamBO
 *
 * @author chouway
 * @date 2022.03.01
 */
@Data
public class SearchParamBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyword;

}
