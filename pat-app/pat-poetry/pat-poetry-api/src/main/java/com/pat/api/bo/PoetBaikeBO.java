package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PoetBaikeBO
 *
 * @author chouway
 * @date 2022.03.14
 */
@Data
public class PoetBaikeBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String poetSet;

    private String poetChapter;

    private String poetSection;

    private String poetTitle;

    private String baikeTitle;

    private String baikeUrl;

    private List<String> baikeDescs;

    private List<PoetPropertyBO> propertyBOs;
}
