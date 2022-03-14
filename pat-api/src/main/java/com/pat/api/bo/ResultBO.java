package com.pat.api.bo;

import lombok.Data;
import java.io.Serializable;


/**
 *
 */
@Data
public class ResultBO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T info;

    private String message;

    private boolean success;

    private String code;

}
