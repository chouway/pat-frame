package com.pat.api.mapper;
import org.beetl.sql.annotation.entity.*;
import org.beetl.sql.mapper.BaseMapper;
import com.pat.api.entity.*;

import java.util.List;
import java.util.Map;

/*
* 
* gen by beetlsql3 mapper 2021-05-19
*/
public interface PatTestInfoMapper extends BaseMapper<PatTestInfo> {

    List<PatTestInfo> sample(Map params);
}
