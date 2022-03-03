package com.pat.app.poetry.synch.util;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.pat.api.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * FileUtils
 *
 * @author chouway
 * @date 2022.03.03
 */
@Slf4j
public class FileUtils {
    /**
     * 获取文本内容
     *                   java 原生 需要手动关流
     *                   byte[] bytes = Files.readAllBytes(templateFile.toPath());
     *                   return new String(bytes, StandardCharsets.UTF_8);
     *                  用 hutool FileUtil.readString 更方便些
     * @param resourcePath
     * @return
     */
    public static String getContent(String resourcePath) {
        try {
            File templateFile = ResourceUtils.getFile(resourcePath);//直接读文件
            return FileUtil.readString(templateFile, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("error:-->[resourcePath]={}", JSON.toJSONString(new Object[]{resourcePath}), e);
            throw new BusinessException("读取内容失败");
        }
    }
}
