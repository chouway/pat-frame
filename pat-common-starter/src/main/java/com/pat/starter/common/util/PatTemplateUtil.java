package com.pat.starter.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import com.pat.api.exception.BusinessException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AibkTemplateUtil
 *
 * @author chouway
 * @date 2021.03.23
 */
@Slf4j
public class PatTemplateUtil {

    public static String CLASS_PATH_KEY = "classpath:";

    /**
     * 是否存在动态变量
     * ${}
     * <p>
     * 注 \w 匹配字母或数字或下划线或汉字 等价于 '[^A-Za-z0-9_]'。
     *
     * @param msg
     * @return boolean
     */
    public static boolean isExistDynamicParam(String msg) {
        if (StringUtils.hasText(msg)) {
            Pattern pattern = Pattern.compile("\\$\\{[\\w\\[\\].]+\\}");
            Matcher matcher = pattern.matcher(msg);
            return matcher.find();
        }
        return false;
    }

    /**
     * https://www.hutool.cn/docs/#/extra/%E6%A8%A1%E6%9D%BF%E5%BC%95%E6%93%8E/%E6%A8%A1%E6%9D%BF%E5%BC%95%E6%93%8E%E5%B0%81%E8%A3%85-TemplateUtil
     * 模板引擎封装-TemplateUtil  hutool
     *
     * @param msg
     * @param context
     * @return
     */
    public static String transform(String msg, Map<String, Object> context) {
        try {
            if (msg == null) {
                return null;
            }
            if (msg.startsWith(CLASS_PATH_KEY)) {
                String path = msg;
                log.debug("transform-->path={}", path);
                msg = getContentFromFile(path);
            }

            if (!isExistDynamicParam(msg)) {//不存在动态变量 直接返原串
                log.debug("transform-->msg={},isExistDynamicParam=false", msg);
                return msg;
            }
            TemplateConfig templateConfig = new TemplateConfig();
            templateConfig.setCustomEngine(FreemarkerEngine.class);//指定引擎 否,则自动判断
            //默认 模式TemplateConfig.ResourceMode.String
            TemplateEngine engine = TemplateUtil.createEngine(templateConfig);

            Template template = engine.getTemplate(msg);
            String render = template.render(context);
            log.debug("transform-->msg={},render={}", msg, render);
            return render;
        } catch (Exception e) {
            log.error("AibkTemplateUtil.transform error:-->[msg, context]={}", JSON.toJSONString(new Object[]{msg, context}), e);
            throw new RuntimeException("转换失败", e);
        }
    }

    public static String getContentFromFile(String resourceLocation) {
        try {

            if (StringUtils.isEmpty(resourceLocation)) {
                throw new BusinessException("文件路径为空");
            }

            if (resourceLocation.startsWith(CLASS_PATH_KEY)) {
                String classFilePath = resourceLocation.substring(CLASS_PATH_KEY.length());
                ClassPathResource classPathResource = new ClassPathResource(classFilePath);
                InputStream in = classPathResource.getInputStream();

                if (in != null) {
                    byte[] bytes = IoUtil.readBytes(in);
                    return new String(bytes, StandardCharsets.UTF_8);
                }
            } else {
                File temFile = new File(resourceLocation);
                if (temFile.exists()) {
                    File templateFile = ResourceUtils.getFile(resourceLocation);//直接读文件
                    byte[] bytes = Files.readAllBytes(templateFile.toPath());
                    return new String(bytes, StandardCharsets.UTF_8);
                }
            }
            throw new BusinessException("文件不存在:path=" + resourceLocation);
        } catch (Exception e) {
            log.error("error:-->[resourceLocation]={}", JSON.toJSONString(new Object[]{resourceLocation}), e);
            throw new RuntimeException("读取文件内容失败", e);
        }
    }

}
