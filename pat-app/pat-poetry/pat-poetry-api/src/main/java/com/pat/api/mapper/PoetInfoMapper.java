package com.pat.api.mapper;
import org.beetl.sql.annotation.entity.*;
import org.beetl.sql.mapper.BaseMapper;
import com.pat.api.entity.*;

import java.util.Map;

/*
* 
* gen by beetlsql3 mapper 2022-03-07
*/
public interface PoetInfoMapper extends BaseMapper<PoetInfo> {
    /**
     * 根据id获取标题和作者
     * @param infoId
     * @return
     */
    public Map<String,String> getTitleAndAuthorById(Long infoId);
}
