package com.pat.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * PoetInfoVO
 *
 * @author chouway
 * @date 2022.03.03
 */
@Data
public class PoetInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可逆加密id后获得,后续扩展接口用这个id 防止有序的id 暴露 被人攻击
     */
    private String eid;

    private Long id;

    private String title;

    private String subtitle;

    private String content;

    private Map<String,String> properties;
}
