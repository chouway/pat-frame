package com.pat.api.mapper;
import org.beetl.sql.annotation.entity.*;
import org.beetl.sql.mapper.BaseMapper;
import com.pat.api.entity.*;

import java.util.List;

/*
* 
* gen by beetlsql3 mapper 2021-05-19
*/
public interface PatConfigMapper extends BaseMapper<PatConfig> {

    PatConfig uniqueByTypeAndCode(String typeCode, String configCode);

    List<PatConfig> getTreeById(Long id);
}
