package com.pat.api.mapper;
import org.beetl.sql.annotation.entity.*;
import org.beetl.sql.mapper.BaseMapper;
import com.pat.api.entity.*;
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
    public String getContent(Long infoId);
}
