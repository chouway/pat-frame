package com.pat.api.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    private Long baikeId;

    private List<String> paragraphs;

    /**
     * 高亮内容
     */
    private Map<Integer,String> highlightParagraphs;

    /**
     * 高亮作者
     */
    private String highlightAuthor;

    /**
     * 高亮标题
     */
    private String highlightTitle;

}
