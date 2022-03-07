package com.pat.api.constant;

import java.io.Serializable;

/**
 * BaikeConstant
 *
 * @author chouway
 * @date 2022.03.04
 */
public class BaikeConstant implements Serializable {

    private static final long serialVersionUID = 1L;

    private BaikeConstant(){}

    /**
     * 百度类型 百度百科
     */
    public static final String BAIKE_TYPE_BAIDU = "00";

    /**
     * 相似度无效判定为0
     */
    public static final String STATUS_SIMILAR_IN_VALID = "0";

    /**
     * 相似度有效判定为1
     */
    public static final String STATUS_SIMILAR_VALID = "1";

    /**
     * 解析百科成功
     */
    public static final String STATUS_BAIKE_OK = "2";

    /**
     * 解析百科待定 需要人工介入
     */
    public static final String STATUS_BAIKE_TO_CHECK = "3";

}
