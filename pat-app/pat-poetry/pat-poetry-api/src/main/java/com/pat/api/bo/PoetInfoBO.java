package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PoetInfoBO
 *
 * @author chouway
 * @date 2022.03.14
 */
@Data
public class PoetInfoBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String subtitle;

    private String author;

    private String baikeUrl;

    private String baikeTitle;

    private String baikeDesc;

    private List<String> paragraphs;

    private List<PoetPropertyBO> propertyBOs;
}
