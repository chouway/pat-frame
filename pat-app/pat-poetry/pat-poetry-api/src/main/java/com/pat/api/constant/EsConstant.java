package com.pat.api.constant;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * EsConstant
 *
 * @author chouway
 * @date 2022.03.07
 */
public class EsConstant {
    private EsConstant(){

    }

    /**
     * 索引模型 STR
     */
    public static final String INDEX_POET_INFO = "poet-info";

    public static final String INDEX_POET_SUGGEST = "poet-suggest";
    /**
     * 索引模型 END
     */

    /**
     * 分析器 STR
     */
    public static final String  ANALYZER_IK_SMART = "ik_smart";

    public static final String  ANALYZER_MAX_WORD = "ik_max_word";

    public static final String  ANALYZER_IK_PINYIN = "ik_pinyin_analyzer";
    /**
     * 分析器 END
     */


    /**
     * 关键字类型： 00 诗人  01 作品名  02 诗人 作品名 03 诗句  04  其它 STR
     */
    public static final String KEY_TYPE_AUTHOR = "00";
    public static final String KEY_TYPE_INFO = "01";
    public static final String KEY_TYPE_AUTHOR_INFO = "02";
    public static final String KEY_TYPE_PARAGRAPH = "03";
    public static final String KEY_TYPE_OTHER = "04";
    /**
     * 关键字类型 END
     */

    /**
     * 兼容性处理
     *  data-elastic  目前只兼容到 es 客户端7.15.2  现有es服务端升到8.0.0  先这样兼容处理 TODO  客户端版端兼容问题
     *
     * @param message
     * @return
     */
    public static boolean isOK(String message){
        if(message == null){
            return false;
        }
        if(message.indexOf("200 OK")!=-1||message.indexOf("201 Created")!=-1){
            return true;
        }
        return false;
    }
}
