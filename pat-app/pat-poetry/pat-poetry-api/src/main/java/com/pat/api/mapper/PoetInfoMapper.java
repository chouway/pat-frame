package com.pat.api.mapper;
import com.pat.api.bo.PoetInfoBO;
import org.beetl.sql.annotation.entity.*;
import org.beetl.sql.mapper.BaseMapper;
import com.pat.api.entity.*;

import java.util.Map;

/*
* 
* gen by beetlsql3 mapper 2022-03-15
*/
public interface PoetInfoMapper extends BaseMapper<PoetInfo> {
    /**
     * 根据id获取标题和作者和篇
     * @param infoId
     * @return
     */
    public Map<String,String> getInfo(Long infoId);


    /**
     * 获取 PoetInfoBO 对象
     * @param infoId
     * @return
     */
    public PoetInfoBO getPoetInfoBO(Long infoId);
}
