package com.pat.api.mapper;
import org.beetl.sql.annotation.entity.*;
import org.beetl.sql.mapper.BaseMapper;
import com.pat.api.entity.*;

import java.util.List;

/*
* 
* gen by beetlsql3 mapper 2022-02-28
*/
public interface PoetContentMapper extends BaseMapper<PoetContent> {
    /**
     * 根据infoId 获取 文本内容
     * @param infoId
     * @return
     */
    public List<String> getParagraphs(Long infoId);
}
