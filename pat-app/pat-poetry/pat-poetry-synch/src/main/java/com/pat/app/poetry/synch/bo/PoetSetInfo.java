package com.pat.app.poetry.synch.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * PoetSetInfos
 *
 * @author chouway
 * @date 2022.02.28
 */
@Data
public class PoetSetInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名称
     */
    private String file;

    /**
     * 已处理数量
     */
    private Integer dealNum;
}
